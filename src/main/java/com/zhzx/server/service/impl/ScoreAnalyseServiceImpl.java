package com.zhzx.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.*;
import com.zhzx.server.dto.StaffLessonTeacherDto;
import com.zhzx.server.dto.exam.*;
import com.zhzx.server.enums.ClazzNatureEnum;
import com.zhzx.server.enums.FunctionEnum;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.repository.*;
import com.zhzx.server.rest.req.ExamEdgeSubParam;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.ExamGoalService;
import com.zhzx.server.service.ScoreAnalyseService;
import com.zhzx.server.service.UserService;
import com.zhzx.server.util.StringUtils;
import com.zhzx.server.util.TwxUtils;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ScoreAnalyseServiceImpl implements ScoreAnalyseService {

    @Resource
    private SubjectMapper subjectMapper;

    @Resource
    private StaffLessonTeacherMapper staffLessonTeacherMapper;

    @Resource
    private ClazzMapper clazzMapper;

    @Resource
    private ExamResultMapper examResultMapper;

    @Resource
    private ExamGoalMapper examGoalMapper;

    @Resource
    private ExamGoalService examGoalService;

    @Resource
    private ExamMapper examMapper;

    @Resource
    private ExamEdgeSubMapper examEdgeSubMapper;

    @Resource
    private UserService userService;

    @Resource
    private ExamGoalWarningMapper examGoalWarningMapper;

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    private List<Long> getOrDefault(List<Long> clazzIds, Long examId) {
        if (CollectionUtils.isEmpty(clazzIds)) {
            Exam exam = examMapper.selectOne(Wrappers.<Exam>lambdaQuery().select(Exam::getAcademicYearSemesterId, Exam::getGradeId).eq(Exam::getId, examId));
            Long academicYearSemesterId = exam.getAcademicYearSemesterId();
            List<Clazz> clazzList = this.userService.mutateYear(academicYearSemesterId);
            if (CollectionUtils.isEmpty(clazzList)) {
                throw new ApiCode.ApiException(-5, "当前角色无班级！");
            }
            clazzIds = clazzList.stream().filter(item -> item.getGradeId().equals(exam.getGradeId())).map(Clazz::getId).collect(Collectors.toList());
        }
        return clazzIds;
    }

    private List<Map<String, Object>> generateDefault(Integer tolerance, List<ExamResultSimpleDto> examResultSimpleDtoList) {
        List<Map<String, Object>> inserts = new ArrayList<>();
        examResultSimpleDtoList.sort(Comparator.comparing(ExamResultSimpleDto::getScore).reversed());
        int highest = examResultSimpleDtoList.get(0).getScore().setScale(0, RoundingMode.DOWN).intValue();
        int modify = highest - highest % 10;
        for (int start = modify, j = 0; start >= 0 && j < 20; start -= tolerance, j++) {
            Map<String, Object> map = new HashMap<>();
            if (start == modify) {
                map.put("right", highest);
                map.put("left", modify);
            }
            map.put("right", start);
            map.put("left", Math.max(start - tolerance, 0));
            if (!map.get("right").toString().equals(map.get("left").toString())) {
                inserts.add(map);
            }
        }
        Collections.reverse(inserts);
        return inserts;
    }

    @Override
    public List<Map<String, Object>> partitionLineChart(Long examId, Long subjectId, List<Long> clazzIds, Integer tolerance) {
        if (examId == null) {
            return new ArrayList<>();
        }
        List<ExamResultSimpleDto> examResultSimpleDtos = this.examResultMapper.getSimpleResult(examId, null);
        if (CollectionUtils.isNotEmpty(examResultSimpleDtos)) {
            if (subjectId == 90001) {
                examResultSimpleDtos.forEach(item -> item.setScore(item.getTotalScore()));
            } else if (subjectId == 90002) {
                examResultSimpleDtos.forEach(item -> item.setScore(item.getTotalWeightedScore()));
            } else {
                Subject subject = subjectMapper.selectOne(Wrappers.<Subject>lambdaQuery()
                        .eq(Subject::getId, subjectId > 1e5 ? subjectId - 1e5 : subjectId));
                if (subject != null) {
                    String alias = subject.getSubjectAlias();
                    if ("chinese".equals(alias)) {
                        examResultSimpleDtos.forEach(item -> item.setScore(item.getChineseScore()));
                    } else if ("math".equals(alias)) {
                        examResultSimpleDtos.forEach(item -> item.setScore(item.getMathScore()));
                    } else if ("english".equals(alias)) {
                        examResultSimpleDtos.forEach(item -> item.setScore(item.getEnglishScore()));
                    } else if ("physics".equals(alias)) {
                        examResultSimpleDtos.forEach(item -> item.setScore(item.getPhysicsScore()));
                    } else if ("history".equals(alias)) {
                        examResultSimpleDtos.forEach(item -> item.setScore(item.getHistoryScore()));
                    } else if ("geography".equals(alias)) {
                        examResultSimpleDtos.forEach(item -> item.setScore(subjectId > 1e5 ?
                                item.getGeographyWeightedScore() : item.getGeographyScore()));
                    } else if ("politics".equals(alias)) {
                        examResultSimpleDtos.forEach(item -> item.setScore(subjectId > 1e5 ?
                                item.getPoliticsWeightedScore() : item.getPoliticsScore()));
                    } else if ("biology".equals(alias)) {
                        examResultSimpleDtos.forEach(item -> item.setScore(subjectId > 1e5 ?
                                item.getBiologyWeightedScore() : item.getBiologyScore()));
                    } else {
                        examResultSimpleDtos.forEach(item -> item.setScore(subjectId > 1e5 ?
                                item.getChemistryWeightedScore() : item.getChemistryScore()));
                    }
                }
            }
            Map<Long, List<ExamResultSimpleDto>> mapClazz = examResultSimpleDtos.stream()
                    .collect(Collectors.groupingBy(ExamResultSimpleDto::getClazzId));

            List<Map<String, Object>> xAxisList = this.generateDefault(tolerance, examResultSimpleDtos);
            clazzIds = this.getOrDefault(clazzIds, examId);
            for (Map<String, Object> item : xAxisList) {
                BigDecimal left = new BigDecimal(item.get("left").toString());
                BigDecimal right = new BigDecimal(item.get("right").toString());
                long all = 0L;
                for (Map.Entry<Long, List<ExamResultSimpleDto>> entry : mapClazz.entrySet()) {
                    Long k = entry.getKey();
                    List<ExamResultSimpleDto> v = entry.getValue();
                    ExamResultSimpleDto item1 = v.get(0);
                    String prefix = item1.getClazzNature().toString().equals("OTHER") ? "" : item1.getClazzNature().toString().equals("SCIENCE") ? "物" : "史";
                    String otherDivision = item1.getOtherDivision();
                    String suffix = prefix.equals("") ? "" : prefix.concat(otherDivision.substring(0, 1)).concat(otherDivision.substring(3, 4));
                    if (clazzIds.contains(k)) {
                        long curr = v.stream().filter(o -> o.getScore().compareTo(left) >= 0 && o.getScore().compareTo(right) < 0).count();
                        item.put("C" + k + "_" + suffix + item1.getClazzName(), curr);
                        all += curr;
                    }
                }
                item.put("C0" + "_" + "全部班级", all);
            }
            return xAxisList;
        }
        return new ArrayList<>();
    }

    @Override
    public Map<String, Object> scoreBarChart(Long examId, List<Long> clazzIds) {
        if (examId == null) {
            return new HashMap<>();
        }
        List<ExamGradeAnalyseClazzSituationDto> examGradeAnalyseClazzSituationDtos = this.examResultMapper.gradeAnalyseClazz(examId);
        Map<String, Object> map = new HashMap<>();
        if (CollectionUtils.isNotEmpty(examGradeAnalyseClazzSituationDtos)) {
            clazzIds = this.getOrDefault(clazzIds, examId);
            Iterator<ExamGradeAnalyseClazzSituationDto> iterator = examGradeAnalyseClazzSituationDtos.iterator();
            while (iterator.hasNext()) {
                ExamGradeAnalyseClazzSituationDto next = iterator.next();
                if (!clazzIds.contains(next.getId())) {
                    iterator.remove();
                }
            }
            examGradeAnalyseClazzSituationDtos.forEach(item -> {
                String prefix = item.getClazzNature().equals("OTHER") ? "" : item.getClazzNature().equals("SCIENCE") ? "物" : "史";
                String otherDivision = item.getOtherDivision();
                item.setSimpleDivision(prefix.equals("") ? "" : prefix.concat(otherDivision.substring(0, 1)).concat(otherDivision.substring(3, 4)));
            });
            Map<String, Object> gradeAverage = this.examResultMapper.gradeAverage(examId, null);
            map.put("bar", examGradeAnalyseClazzSituationDtos);
            map.put("gradeAverage", gradeAverage);
        }
        return map;
    }

    @Override
    public Map<String, Object> personBarChart(Long examId, Long studentId) {
        Map<String, Object> res = new HashMap<>();
        if (examId == null) {
            return res;
        }
        ExamResult examResult = this.examResultMapper.selectOne(Wrappers.<ExamResult>lambdaQuery().eq(ExamResult::getExamId, examId)
            .eq(ExamResult::getStudentId, studentId));
        if (examResult != null) {
            String clazzNature = examResult.getClazz().getClazzNature().toString();
            res.put("examResult", examResult);
            Map<String, Object> gradeAverage = this.examResultMapper.gradeAverage(examId, clazzNature);
            res.put("gradeAverage", gradeAverage);
            List<ExamGoalDto> examGoalDtoList = this.examGoalService.getDefault(examId, null);
            if (CollectionUtils.isNotEmpty(examGoalDtoList)) {
                examGoalDtoList = examGoalDtoList.stream().filter(o -> o.getSubjectType().equals(clazzNature)).collect(Collectors.toList());
            }
            res.put("goals", examGoalDtoList);
        }
        return res;
    }

    @Override
    public Map<String, Object> headInfoClazz(Long examId, Long clazzId) {
        Map<String, Object> res = new HashMap<>();
        if (examId == null) {
            return res;
        }
        List<ExamResultSimpleDto> examResultSimpleDtoList = this.examResultMapper.getSimpleResult(examId, null);
        if (CollectionUtils.isNotEmpty(examResultSimpleDtoList)) {
            boolean useWeighted = true;
            double dd = examResultSimpleDtoList.stream().collect(Collectors.averagingDouble(o -> o.getTotalWeightedScore().doubleValue()));
            if (dd == 0) {
                useWeighted = false;
                dd = examResultSimpleDtoList.stream().collect(Collectors.averagingDouble(o -> o.getTotalScore().doubleValue()));
            }
            BigDecimal scoreAvgNj = BigDecimal.valueOf(dd).setScale(1, RoundingMode.HALF_UP);
            res.put("scoreAvgNj", scoreAvgNj);
            Map<Long, List<ExamResultSimpleDto>> map = examResultSimpleDtoList.stream().collect(Collectors.groupingBy(ExamResultSimpleDto::getClazzId));
            if (!map.containsKey(clazzId)) {
                return res;
            }
            List<ExamResultSimpleDto> currClazz = map.get(clazzId);
            BigDecimal maxScore, minScore;
            if (useWeighted) {
                currClazz.sort(Comparator.comparing(ExamResultSimpleDto::getTotalWeightedScore, Comparator.naturalOrder()));
                maxScore = currClazz.get(currClazz.size() - 1).getTotalWeightedScore();
                minScore = currClazz.get(0).getTotalWeightedScore();
            } else {
                currClazz.sort(Comparator.comparing(ExamResultSimpleDto::getTotalScore, Comparator.naturalOrder()));
                maxScore = currClazz.get(currClazz.size() - 1).getTotalScore();
                minScore = currClazz.get(0).getTotalScore();
            }
            res.put("maxScore", maxScore);
            res.put("minScore", minScore);
            List<BigDecimal> decimalList = new ArrayList<>();
            for (Map.Entry<Long, List<ExamResultSimpleDto>> entry : map.entrySet()) {
                List<ExamResultSimpleDto> v = entry.getValue();
                double d;
                if (useWeighted) {
                    d = v.stream().collect(Collectors.averagingDouble(o -> o.getTotalWeightedScore().doubleValue()));
                } else {
                    d = v.stream().collect(Collectors.averagingDouble(o -> o.getTotalScore().doubleValue()));
                }
                BigDecimal scoreAvg = BigDecimal.valueOf(d).setScale(1, RoundingMode.HALF_UP);
                decimalList.add(scoreAvg);
                if (entry.getKey().equals(clazzId)) {
                    res.put("scoreAvg", scoreAvg);
                }
            }
            BigDecimal curr = new BigDecimal(res.get("scoreAvg").toString());
            res.put("rank", decimalList.stream().filter(item -> item.compareTo(curr) > 0).count() + 1);
            res.put("total", map.keySet().size());

            List<ExamGoalDto> examGoalDtoList = this.examGoalService.getDefault(examId, null);
            if (CollectionUtils.isNotEmpty(examGoalDtoList)) {
                List<ExamGoalClazzTotalDto> clazzGoalTotal = this.examGoalMapper.getClazzGoalTotal(clazzId, examGoalDtoList);
                res.put("clazzGoalTotal", clazzGoalTotal);
            }
        }
        return res;
    }

    @Override
    public Map<String, Object> tableInfoClazz(Long examId, Long clazzId, String orderByClause) {
        Map<String, Object> res = new HashMap<>();
        if (examId == null || clazzId == null) {
            return res;
        }
        IPage<ExamResult> page = new Page<>(1, 100);
        IPage<Map<String, Object>> iPage = this.examResultMapper.searchExamResultClazz(page, examId, new ArrayList<Long>(){{add(clazzId);}}, orderByClause, null);
        res.put("list", iPage);
        res.put("head", new ArrayList<>());
        List<ExamGoalDto> examGoalDtoList = this.examGoalService.getDefault(examId, null);
        if (examGoalDtoList == null) examGoalDtoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(examGoalDtoList)) {
            Clazz clazz = this.clazzMapper.selectById(clazzId);
            examGoalDtoList = examGoalDtoList.stream().filter(item -> item.getTotalCount() > 0 && item.getSubjectType().equals(clazz.getClazzNature().toString())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(examGoalDtoList)) {
                examGoalDtoList.sort(Comparator.comparing(ExamGoalDto::getTransGoalScore, Comparator.reverseOrder()));
                res.put("goals", examGoalDtoList);

                // 设置预警线
                Map<Long, Subject> subjectMap = this.subjectMapper
                        .selectList(Wrappers.<Subject>lambdaQuery().eq(Subject::getIsMain, YesNoEnum.YES))
                        .stream().collect(Collectors.toMap(Subject::getId, Function.identity()));
                List<ExamGoalWarning> examGoalWarningList = this.examGoalWarningMapper.selectList(Wrappers.<ExamGoalWarning>lambdaQuery()
                        .eq(ExamGoalWarning::getExamId, examId)
                        .eq(ExamGoalWarning::getSubjectType, clazz.getClazzNature().toString()));
                if (CollectionUtils.isNotEmpty(examGoalWarningList)) {
                    Map<Long, List<ExamGoalWarning>> map = examGoalWarningList.stream().collect(Collectors.groupingBy(ExamGoalWarning::getGoalId));
                    for (ExamGoalDto examGoalDto : examGoalDtoList) {
                        List<ExamGoalWarning> examGoalWarnings = map.get(examGoalDto.getId());
                        if (CollectionUtils.isNotEmpty(examGoalWarnings)) {
                            examGoalWarnings.sort(Comparator.comparingInt(ExamGoalWarning::getSortOrder));
                            Map<String, List<ExamGoalWarning>> examGoalWarningMap = new HashMap<>();
                            for (ExamGoalWarning examGoalWarning : examGoalWarnings) {
                                Long subjectId = examGoalWarning.getSubjectId();
                                Long stdSubjectId = subjectId > 100000 ? subjectId - 100000 : subjectId;
                                String suffix = subjectId > 100000 ? "Weighted" : "";
                                examGoalWarningMap.computeIfAbsent(subjectMap.get(stdSubjectId).getSubjectAlias().concat(suffix),
                                        o -> new ArrayList<>()).add(examGoalWarning);
                            }
                            examGoalDto.setExamGoalWarningMap(examGoalWarningMap);
                        }
                    }
                }
            }
        }
        boolean orderDesc = orderByClause.endsWith("desc");
        List<Map<String, Object>> records = iPage.getRecords();
        for (int i = 0; i < records.size(); i++) {
            Map<String, Object> curr = records.get(i);
            BigDecimal score = new BigDecimal(curr.get("totalWeightedScore").toString());
            BigDecimal next = i == records.size() - 1 ? null : new BigDecimal(records.get(i + 1).get("totalWeightedScore").toString());
            curr.put("symbol", "");
            for (ExamGoalDto examGoalDto : examGoalDtoList) {
                BigDecimal comp = examGoalDto.getTransGoalScore();
                if (next == null) {
                    if (score.compareTo(comp) >= 0) {
                        curr.put("symbol", examGoalDto.getName());
                        break;
                    }
                } else {
                    boolean condition = orderDesc ? score.compareTo(comp) >= 0 && next.compareTo(comp) < 0 : score.compareTo(comp) <= 0 && next.compareTo(comp) > 0;
                    if (condition) {
                        curr.put("symbol", examGoalDto.getName());
                        break;
                    }
                }
            }
        }
        return res;
    }

    @Override
    public Map<String, Object> headInfoGrade(Long examId, Long gradeId) {
        Map<String, Object> res = new HashMap<>();
        if (examId == null) {
            return res;
        }
        List<ExamResultSimpleDto> examResultSimpleDtoList = this.examResultMapper.getSimpleResult(examId, null);
        if (CollectionUtils.isNotEmpty(examResultSimpleDtoList)) {
            boolean useWeighted = (null != examResultSimpleDtoList.get(0).getTotalWeightedScore());
            double d;
            BigDecimal maxScore, minScore;
            if (useWeighted) {
                d = examResultSimpleDtoList.stream().collect(Collectors.averagingDouble(o -> o.getTotalWeightedScore().doubleValue()));
                examResultSimpleDtoList.sort(Comparator.comparing(ExamResultSimpleDto::getTotalWeightedScore, Comparator.naturalOrder()));
                maxScore = examResultSimpleDtoList.get(examResultSimpleDtoList.size() - 1).getTotalWeightedScore();
                minScore = examResultSimpleDtoList.get(0).getTotalWeightedScore();
            } else {
                d = examResultSimpleDtoList.stream().collect(Collectors.averagingDouble(o -> o.getTotalScore().doubleValue()));
                examResultSimpleDtoList.sort(Comparator.comparing(ExamResultSimpleDto::getTotalScore, Comparator.naturalOrder()));
                maxScore = examResultSimpleDtoList.get(examResultSimpleDtoList.size() - 1).getTotalScore();
                minScore = examResultSimpleDtoList.get(0).getTotalScore();
            }
            res.put("maxScore", maxScore);
            res.put("minScore", minScore);
            res.put("scoreAvg", BigDecimal.valueOf(d).setScale(1, RoundingMode.HALF_UP));

            List<ExamGoalDto> examGoalDtoList = this.examGoalService.getDefault(examId, null);
            if (CollectionUtils.isNotEmpty(examGoalDtoList)) {
                List<ExamGoalClazzTotalDto> clazzGoalTotal = this.examGoalMapper.getClazzGoalTotal(null, examGoalDtoList);
                Map<Long, List<ExamGoalClazzTotalDto>> map = clazzGoalTotal.stream().collect(Collectors.groupingBy(ExamGoalClazzTotalDto::getId));
                List<Map<String, Object>> list = new ArrayList<>();
                map.forEach((k, v) -> {
                    Map<String, Object> tmp = new HashMap<>();
                    tmp.put("id", k);
                    tmp.put("targetName", v.get(0).getTargetName());
                    tmp.put("clazzMeetingCnt", v.stream().mapToInt(ExamGoalClazzTotalDto::getClazzMeetingCnt).sum());
                    tmp.put("shouldMeetingCnt", v.stream().mapToInt(ExamGoalClazzTotalDto::getShouldMeetingCnt).sum());
                    list.add(tmp);
                });
                res.put("gradeGoalTotal", list);
            }

        }
        return res;
    }

    private void generateScore(List<ExamResultSimpleDto> examResultSimpleDtos, String alias) {
            if ("chinese".equals(alias)) {
                examResultSimpleDtos.forEach(item -> item.setScore(item.getChineseScore()));
            } else if ("math".equals(alias)) {
                examResultSimpleDtos.forEach(item -> item.setScore(item.getMathScore()));
            } else if ("english".equals(alias)) {
                examResultSimpleDtos.forEach(item -> item.setScore(item.getEnglishScore()));
            } else if ("physics".equals(alias)) {
                examResultSimpleDtos.forEach(item -> item.setScore(item.getPhysicsScore()));
            } else if ("history".equals(alias)) {
                examResultSimpleDtos.forEach(item -> item.setScore(item.getHistoryScore()));
            } else if ("geography".equals(alias)) {
                examResultSimpleDtos.forEach(item -> {
                    item.setScore(item.getGeographyScore());
                    item.setScoreWeighted(item.getGeographyWeightedScore());
                });
            } else if ("politics".equals(alias)) {
                examResultSimpleDtos.forEach(item -> {
                    item.setScore(item.getPoliticsScore());
                    item.setScoreWeighted(item.getGeographyWeightedScore());
                });
            } else if ("biology".equals(alias)) {
                examResultSimpleDtos.forEach(item -> {
                    item.setScore(item.getBiologyScore());
                    item.setScoreWeighted(item.getBiologyWeightedScore());
                });
            } else {
                examResultSimpleDtos.forEach(item -> {
                    item.setScore(item.getChemistryScore());
                    item.setScoreWeighted(item.getChemistryWeightedScore());
                });
            }
    }

    private BigDecimal std(BigDecimal avg, List<BigDecimal> decimalList) {
        BigDecimal total = BigDecimal.ZERO;
        if (avg.compareTo(BigDecimal.ZERO) != 0) {
            for (BigDecimal bigDecimal : decimalList) {
                BigDecimal curr = bigDecimal.subtract(avg);
                total = total.add(curr.multiply(curr).setScale(2, RoundingMode.HALF_UP));
            }
            total = BigDecimal.valueOf(Math.sqrt(total.divide(BigDecimal.valueOf(Math.max(decimalList.size(), 1)), 2, RoundingMode.HALF_UP).doubleValue())).setScale(1, RoundingMode.HALF_UP);
        }
        return total;
    }

    @Override
    public List<Map<String, Object>> subjectTableCompare(Long examId, Long subjectId, List<Long> clazzIds) {
        List<Map<String, Object>> list = new ArrayList<>();
        if (examId == null) {
            return list;
        }
        clazzIds = this.getOrDefault(clazzIds, examId);
        List<ExamResultSimpleDto> examResultSimpleDtoList = this.examResultMapper.getSimpleResultByList(examId, clazzIds);
        if (CollectionUtils.isNotEmpty(examResultSimpleDtoList)) {
            Subject subject = subjectMapper.selectOne(Wrappers.<Subject>lambdaQuery().eq(Subject::getId, subjectId));
            if (subject != null) {
                List<StaffLessonTeacherDto> staffLessonTeacherDtos = this.staffLessonTeacherMapper.selectByGradeAndClazz(null, subjectId, null, clazzIds);
                Map<String, List<StaffLessonTeacherDto>> staffLessonTeacherMap = staffLessonTeacherDtos.stream().collect(Collectors.groupingBy(item -> item.getClazzId().toString().concat(item.getSubjectId().toString())));

                this.generateScore(examResultSimpleDtoList, subject.getSubjectAlias());
                Map<Long, List<ExamResultSimpleDto>> map = examResultSimpleDtoList.stream().collect(Collectors.groupingBy(ExamResultSimpleDto::getClazzId));
                BigDecimal goodScore = BigDecimal.valueOf(subject.getMaxScore() * 0.85);
                BigDecimal passScore = BigDecimal.valueOf(subject.getMaxScore() * 0.6);
                List<BigDecimal> pmList = new ArrayList<>();
                List<BigDecimal> pmList1 = new ArrayList<>();
                int allStudent = 0, joinStudent = 0;
                int goodStudent = 0, passStudent = 0;
                int goodStudentWeighted = 0, passStudentWeighted = 0;
                for (Map.Entry<Long, List<ExamResultSimpleDto>> entry : map.entrySet()) {
                    Map<String, Object> tmp = new HashMap<>();
                    List<ExamResultSimpleDto> v = entry.getValue();
                    tmp.put("clazzName", v.get(0).getClazzName());
                    tmp.put("clazzId", entry.getKey());
                    tmp.put("teacher", staffLessonTeacherMap.get(entry.getKey().toString().concat(subjectId.toString())));
                    tmp.put("studentCount", v.get(0).getStudentCount());
                    List<BigDecimal> decimalList = v.stream().map(ExamResultSimpleDto::getScore).filter(score -> score.compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
                    tmp.put("joinCount", decimalList.size());
                    allStudent += v.get(0).getStudentCount();
                    joinStudent += decimalList.size();
                    BigDecimal joinCnt = new BigDecimal(tmp.get("joinCount").toString());
                    int goodCount = (int) decimalList.stream().filter(item -> item.compareTo(goodScore) >= 0).count();
                    goodStudent += goodCount;
                    int passCount = (int) decimalList.stream().filter(item -> item.compareTo(passScore) >= 0).count();
                    passStudent += passCount;
                    if (joinCnt.compareTo(BigDecimal.ZERO) == 0) {
                        joinCnt = BigDecimal.ONE;
                    }
                    tmp.put("goodRatio", BigDecimal.valueOf(goodCount).multiply(BigDecimal.valueOf(100)).divide(joinCnt, 1, RoundingMode.HALF_UP));
                    tmp.put("passRatio", BigDecimal.valueOf(passCount).multiply(BigDecimal.valueOf(100)).divide(joinCnt, 1, RoundingMode.HALF_UP));
                    double d = decimalList.stream().collect(Collectors.averagingDouble(BigDecimal::doubleValue));
                    BigDecimal scoreAvg = BigDecimal.valueOf(d).setScale(1, RoundingMode.HALF_UP);
                    pmList.add(scoreAvg);
                    tmp.put("scoreAvg", scoreAvg);
                    tmp.put("std", this.std(scoreAvg, decimalList));
                    if (YesNoEnum.YES.equals(subject.getHasWeight())) {
                        decimalList = v.stream().filter(item -> item.getScore().compareTo(BigDecimal.ZERO) > 0).map(ExamResultSimpleDto::getScoreWeighted).collect(Collectors.toList());
                        goodCount = (int) decimalList.stream().filter(item -> item.compareTo(goodScore) >= 0).count();
                        goodStudentWeighted += goodCount;
                        passCount = (int) decimalList.stream().filter(item -> item.compareTo(passScore) >= 0).count();
                        passStudentWeighted += passCount;
                        tmp.put("goodRatioWeighted", BigDecimal.valueOf(goodCount).multiply(BigDecimal.valueOf(100)).divide(joinCnt, 1, RoundingMode.HALF_UP));
                        tmp.put("passRatioWeighted", BigDecimal.valueOf(passCount).multiply(BigDecimal.valueOf(100)).divide(joinCnt, 1, RoundingMode.HALF_UP));
                        d = decimalList.stream().collect(Collectors.averagingDouble(BigDecimal::doubleValue));
                        scoreAvg = BigDecimal.valueOf(d).setScale(1, RoundingMode.HALF_UP);
                        pmList1.add(scoreAvg);
                        tmp.put("scoreAvgWeighted", scoreAvg);
                        tmp.put("stdWeighted", this.std(scoreAvg, decimalList));
                    }
                    list.add(tmp);
                }
                list.sort(Comparator.comparing(item -> Integer.parseInt(item.get("clazzName").toString().replace("班", "")), Comparator.naturalOrder()));

                Map<String, Object> totalMap = new HashMap<>();
                totalMap.put("clazzName", "总计");
                totalMap.put("clazzId", -1);
                totalMap.put("studentCount", allStudent);
                totalMap.put("joinCount", joinStudent);
                BigDecimal joinCnt = BigDecimal.valueOf(joinStudent);
                if (BigDecimal.ZERO.compareTo(joinCnt) == 0) {
                    joinCnt = BigDecimal.ONE;
                }
                totalMap.put("goodRatio", BigDecimal.valueOf(goodStudent).multiply(BigDecimal.valueOf(100)).divide(joinCnt, 1, RoundingMode.HALF_UP));
                totalMap.put("passRatio", BigDecimal.valueOf(passStudent).multiply(BigDecimal.valueOf(100)).divide(joinCnt, 1, RoundingMode.HALF_UP));

                BigDecimal var1 = BigDecimal.ZERO;
                BigDecimal var2 = BigDecimal.ZERO;
                for (ExamResultSimpleDto examResultSimpleDto : examResultSimpleDtoList) {
                    var1 = var1.add(examResultSimpleDto.getScore());
                    if (YesNoEnum.YES.equals(subject.getHasWeight())) {
                        var2 = var2.add(examResultSimpleDto.getScoreWeighted());
                    }
                }
                var1 = var1.divide(joinCnt, 1, RoundingMode.HALF_UP);
                var2 = var2.divide(joinCnt, 1, RoundingMode.HALF_UP);
                totalMap.put("scoreAvg", var1);
                totalMap.put("std", this.std(var1, pmList));
                if (YesNoEnum.YES.equals(subject.getHasWeight())) {
                    totalMap.put("stdWeighted", this.std(var2, pmList1));
                    totalMap.put("scoreAvgWeighted", var2);
                    totalMap.put("goodRatioWeighted", BigDecimal.valueOf(goodStudentWeighted).multiply(BigDecimal.valueOf(100)).divide(joinCnt, 1, RoundingMode.HALF_UP));
                    totalMap.put("passRatioWeighted", BigDecimal.valueOf(passStudentWeighted).multiply(BigDecimal.valueOf(100)).divide(joinCnt, 1, RoundingMode.HALF_UP));
                }

                list.forEach(item -> {
                    BigDecimal curr = new BigDecimal(item.get("scoreAvg").toString());
                    item.put("pm", pmList.stream().filter(o -> o.compareTo(curr) > 0).count() + 1);
                    if (item.containsKey("scoreAvgWeighted")) {
                        BigDecimal curr1 = new BigDecimal(item.get("scoreAvgWeighted").toString());
                        item.put("pmWeighted", pmList1.stream().filter(o -> o.compareTo(curr1) > 0).count() + 1);
                    }
                });
                list.add(totalMap);
            }
        }
        return list;
    }

    private int getRankDecimal(List<BigDecimal> list, BigDecimal b) {
        int count = 1;
        for (BigDecimal integer : list) {
            if (integer.compareTo(b) > 0) {
                count ++;
            }
        }
        return count;
    }

    private String getSubjects(String clazzDivision, List<Subject> subjectList) {
        if (StringUtils.isNullOrEmpty(clazzDivision))
            return subjectList.stream().map(Subject::getSubjectAlias).collect(Collectors.joining(","));
        Map<Long, String> map = subjectList.stream().collect(Collectors.toMap(Subject::getId, Subject::getSubjectAlias));
        String[] arr = clazzDivision.split(",");
        return Arrays.stream(arr).map(item -> map.get(Long.valueOf(item))).collect(Collectors.joining(","));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> goal(Long examId, List<Long> clazzIds) {
        Map<String, Object> res = new HashMap<>();
        if (examId == null) {
            return res;
        }
        List<ExamGoalDto> examGoalDtoList = this.examGoalService.getDefault(examId, null);
        if (CollectionUtils.isNotEmpty(examGoalDtoList)) {
            List<Subject> subjectList = this.subjectMapper.selectList(Wrappers.<Subject>lambdaQuery()
                    .eq(Subject::getIsMain, YesNoEnum.YES));
            Map<String, List<ExamGoalDto>> goalTotalColumns = examGoalDtoList.stream().collect(Collectors.groupingBy(ExamGoalDto::getSubjectType));
            goalTotalColumns.forEach((k, v) -> v.sort(Comparator.comparing(ExamGoalDto::getId)));
            res.put("goalTotalColumns", goalTotalColumns);

            clazzIds = this.getOrDefault(clazzIds, examId);
            List<ExamGoalWorkBenchDto> examGoalWorkBenchDtoList = this.examGoalMapper.getClazzGoalWorkBench(clazzIds, examGoalDtoList);
            if (CollectionUtils.isNotEmpty(examGoalWorkBenchDtoList)) {
                List<StaffLessonTeacherDto> staffLessonTeacherList = this.staffLessonTeacherMapper.selectByGradeAndClazz(null, null, null, clazzIds);
                Map<String, List<StaffLessonTeacherDto>> mStaffLessonTeacher = staffLessonTeacherList.stream().collect(
                        Collectors.groupingBy(item -> item.getClazzId().toString().concat(item.getSubject().getSubjectAlias()))
                );
                Map<String, List<Map<String, Object>>> goalTotalList = new HashMap<>();
                Map<Long, List<ExamGoalWorkBenchDto>> clazzTotalMap = examGoalWorkBenchDtoList.stream().collect(Collectors.groupingBy(ExamGoalWorkBenchDto::getClazzId));
                Map<String, List<BigDecimal>[]> map = new HashMap<>();
                Map<String, List<BigDecimal>[]> mapValid = new HashMap<>();
                clazzTotalMap.forEach((k, v) -> {
                    ExamGoalWorkBenchDto curr = v.get(0);
                    Map<String, Object> map1 = new HashMap<>();
                    map1.put("clazzId", k);
                    map1.put("clazzName", curr.getClazzName());
                    map1.put("clazzSubjects", this.getSubjects(curr.getClazzDivision(), subjectList));
                    map1.put("clazzNature", ClazzNatureEnum.valueOf(curr.getClazzNature()));
                    map1.put("studentCnt", curr.getStudentCnt());
                    map1.put("joinCnt", curr.getJoinCnt());
                    map1.put("chineseJoinCnt", curr.getChineseJoinCnt());
                    map1.put("mathJoinCnt", curr.getMathJoinCnt());
                    map1.put("englishJoinCnt", curr.getEnglishJoinCnt());
                    map1.put("physicsJoinCnt", curr.getPhysicsJoinCnt());
                    map1.put("chemistryJoinCnt", curr.getChemistryJoinCnt());
                    map1.put("biologyJoinCnt", curr.getBiologyJoinCnt());
                    map1.put("historyJoinCnt", curr.getHistoryJoinCnt());
                    map1.put("politicsJoinCnt", curr.getPoliticsJoinCnt());
                    map1.put("geographyJoinCnt", curr.getGeographyJoinCnt());
                    StaffLessonTeacher advisor = staffLessonTeacherList.stream().filter(item -> item.getClazzId().equals(k) && item.getIsClazzAdvisor().equals(YesNoEnum.YES)).findFirst().orElse(null);
                    map1.put("teacher", advisor == null ? null : advisor.getStaff());
                    map1.put("chineseTeacher", mStaffLessonTeacher.get(k.toString().concat("chinese")));
                    map1.put("mathTeacher", mStaffLessonTeacher.get(k.toString().concat("math")));
                    map1.put("englishTeacher", mStaffLessonTeacher.get(k.toString().concat("english")));
                    map1.put("physicsTeacher", mStaffLessonTeacher.get(k.toString().concat("physics")));
                    map1.put("chemistryTeacher", mStaffLessonTeacher.get(k.toString().concat("chemistry")));
                    map1.put("biologyTeacher", mStaffLessonTeacher.get(k.toString().concat("biology")));
                    map1.put("historyTeacher", mStaffLessonTeacher.get(k.toString().concat("history")));
                    map1.put("politicsTeacher", mStaffLessonTeacher.get(k.toString().concat("politics")));
                    map1.put("geographyTeacher", mStaffLessonTeacher.get(k.toString().concat("geography")));


                    Map<String, Object> map2 = new HashMap<>();
                    map1.put("goals", map2);
                    v.forEach(item -> {
                        String key = curr.getClazzNature().concat(",").concat(item.getTargetName());
                        if (!map.containsKey(key)) {
                            List<BigDecimal>[] list = new List[14];
                            TwxUtils.fill(list, ArrayList::new);
                            map.put(key, list);
                        }
                        if (!mapValid.containsKey(key)) {
                            List<BigDecimal>[] list = new List[13];
                            TwxUtils.fill(list, ArrayList::new);
                            mapValid.put(key, list);
                        }

                        Map<String, Object> map3 = new HashMap<>();
                        map3.put("clazzMeetingCnt", item.getClazzMeetingCnt());
                        map3.put("shouldMeetingCnt", item.getShouldMeetingCnt());

                        int fm = Math.max(1, item.getShouldMeetingCnt());

                        BigDecimal ratio = BigDecimal.valueOf(item.getClazzMeetingCnt()).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(fm), 1, RoundingMode.HALF_UP);
                        map3.put("ratio", ratio);
                        map.get(key)[0].add(ratio);

                        int fm1 = Math.max(1, item.getClazzMeetingCnt());

                        map3.put("chineseMeetingCnt", item.getChineseMeetingCnt());
                        map3.put("chineseOnlyMeetingCnt", item.getChineseOnlyMeetingCnt());
                        map3.put("chineseValidAvgScore", item.getChineseValidAvgScore());
                        ratio = BigDecimal.valueOf(item.getChineseMeetingCnt()).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(fm1), 1, RoundingMode.HALF_UP);
                        map3.put("chineseRatio", ratio);
                        map.get(key)[1].add(ratio);
                        mapValid.get(key)[0].add(item.getChineseValidAvgScore());

                        map3.put("mathMeetingCnt", item.getMathMeetingCnt());
                        map3.put("mathOnlyMeetingCnt", item.getMathOnlyMeetingCnt());
                        map3.put("mathValidAvgScore", item.getMathValidAvgScore());
                        ratio = BigDecimal.valueOf(item.getMathMeetingCnt()).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(fm1), 1, RoundingMode.HALF_UP);
                        map3.put("mathRatio", ratio);
                        map.get(key)[2].add(ratio);
                        mapValid.get(key)[1].add(item.getMathValidAvgScore());

                        map3.put("englishMeetingCnt", item.getEnglishMeetingCnt());
                        map3.put("englishOnlyMeetingCnt", item.getEnglishOnlyMeetingCnt());
                        map3.put("englishValidAvgScore", item.getEnglishValidAvgScore());
                        ratio = BigDecimal.valueOf(item.getEnglishMeetingCnt()).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(fm1), 1, RoundingMode.HALF_UP);
                        map3.put("englishRatio", ratio);
                        map.get(key)[3].add(ratio);
                        mapValid.get(key)[2].add(item.getEnglishValidAvgScore());

                        map3.put("physicsMeetingCnt", item.getPhysicsMeetingCnt());
                        map3.put("physicsOnlyMeetingCnt", item.getPhysicsOnlyMeetingCnt());
                        map3.put("physicsValidAvgScore", item.getPhysicsValidAvgScore());
                        ratio = BigDecimal.valueOf(item.getPhysicsMeetingCnt()).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(fm1), 1, RoundingMode.HALF_UP);
                        map3.put("physicsRatio", ratio);
                        map.get(key)[4].add(ratio);
                        mapValid.get(key)[3].add(item.getPhysicsValidAvgScore());

                        map3.put("historyMeetingCnt", item.getHistoryMeetingCnt());
                        map3.put("historyOnlyMeetingCnt", item.getHistoryOnlyMeetingCnt());
                        map3.put("historyValidAvgScore", item.getHistoryValidAvgScore());
                        ratio = BigDecimal.valueOf(item.getHistoryMeetingCnt()).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(fm1), 1, RoundingMode.HALF_UP);
                        map3.put("historyRatio", ratio);
                        map.get(key)[5].add(ratio);
                        mapValid.get(key)[4].add(item.getHistoryValidAvgScore());

                        map3.put("chemistryMeetingCnt", item.getChemistryMeetingCnt());
                        map3.put("chemistryOnlyMeetingCnt", item.getChemistryOnlyMeetingCnt());
                        map3.put("chemistryValidAvgScore", item.getChemistryValidAvgScore());
                        ratio = BigDecimal.valueOf(item.getChemistryMeetingCnt()).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(fm1), 1, RoundingMode.HALF_UP);
                        map3.put("chemistryRatio", ratio);
                        map.get(key)[6].add(ratio);
                        mapValid.get(key)[5].add(item.getChemistryValidAvgScore());

                        map3.put("chemistryWeightedMeetingCnt", item.getChemistryWeightedMeetingCnt());
                        map3.put("chemistryWeightedOnlyMeetingCnt", item.getChemistryWeightedOnlyMeetingCnt());
                        map3.put("chemistryWeightedValidAvgScore", item.getChemistryWeightedValidAvgScore());
                        ratio = BigDecimal.valueOf(item.getChemistryWeightedMeetingCnt()).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(fm1), 1, RoundingMode.HALF_UP);
                        map3.put("chemistryWeightedRatio", ratio);
                        map.get(key)[7].add(ratio);
                        mapValid.get(key)[6].add(item.getChemistryWeightedValidAvgScore());

                        map3.put("biologyMeetingCnt", item.getBiologyMeetingCnt());
                        map3.put("biologyOnlyMeetingCnt", item.getBiologyOnlyMeetingCnt());
                        map3.put("biologyValidAvgScore", item.getBiologyValidAvgScore());
                        ratio = BigDecimal.valueOf(item.getBiologyMeetingCnt()).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(fm1), 1, RoundingMode.HALF_UP);
                        map3.put("biologyRatio", ratio);
                        map.get(key)[8].add(ratio);
                        mapValid.get(key)[7].add(item.getBiologyValidAvgScore());

                        map3.put("biologyWeightedMeetingCnt", item.getBiologyWeightedMeetingCnt());
                        map3.put("biologyWeightedOnlyMeetingCnt", item.getBiologyWeightedOnlyMeetingCnt());
                        map3.put("biologyWeightedValidAvgScore", item.getBiologyWeightedValidAvgScore());
                        ratio = BigDecimal.valueOf(item.getBiologyWeightedMeetingCnt()).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(fm1), 1, RoundingMode.HALF_UP);
                        map3.put("biologyWeightedRatio", ratio);
                        map.get(key)[9].add(ratio);
                        mapValid.get(key)[8].add(item.getBiologyWeightedValidAvgScore());

                        map3.put("politicsMeetingCnt", item.getPoliticsMeetingCnt());
                        map3.put("politicsOnlyMeetingCnt", item.getPoliticsOnlyMeetingCnt());
                        map3.put("politicsValidAvgScore", item.getPoliticsValidAvgScore());
                        ratio = BigDecimal.valueOf(item.getPoliticsMeetingCnt()).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(fm1), 1, RoundingMode.HALF_UP);
                        map3.put("politicsRatio", ratio);
                        map.get(key)[10].add(ratio);
                        mapValid.get(key)[9].add(item.getPoliticsValidAvgScore());

                        map3.put("politicsWeightedMeetingCnt", item.getPoliticsWeightedMeetingCnt());
                        map3.put("politicsWeightedOnlyMeetingCnt", item.getPoliticsWeightedOnlyMeetingCnt());
                        map3.put("politicsWeightedValidAvgScore", item.getPoliticsWeightedValidAvgScore());
                        ratio = BigDecimal.valueOf(item.getPoliticsWeightedMeetingCnt()).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(fm1), 1, RoundingMode.HALF_UP);
                        map3.put("politicsWeightedRatio", ratio);
                        map.get(key)[11].add(ratio);
                        mapValid.get(key)[10].add(item.getPoliticsWeightedValidAvgScore());

                        map3.put("geographyMeetingCnt", item.getGeographyMeetingCnt());
                        map3.put("geographyOnlyMeetingCnt", item.getGeographyOnlyMeetingCnt());
                        map3.put("geographyValidAvgScore", item.getGeographyValidAvgScore());
                        ratio = BigDecimal.valueOf(item.getGeographyMeetingCnt()).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(fm1), 1, RoundingMode.HALF_UP);
                        map3.put("geographyRatio", ratio);
                        map.get(key)[12].add(ratio);
                        mapValid.get(key)[11].add(item.getGeographyValidAvgScore());

                        map3.put("geographyWeightedMeetingCnt", item.getGeographyWeightedMeetingCnt());
                        map3.put("geographyWeightedOnlyMeetingCnt", item.getGeographyWeightedOnlyMeetingCnt());
                        map3.put("geographyWeightedValidAvgScore", item.getGeographyWeightedValidAvgScore());
                        ratio = BigDecimal.valueOf(item.getGeographyWeightedMeetingCnt()).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(fm1), 1, RoundingMode.HALF_UP);
                        map3.put("geographyWeightedRatio", ratio);
                        map.get(key)[13].add(ratio);
                        mapValid.get(key)[12].add(item.getGeographyWeightedValidAvgScore());

                        map2.put(item.getTargetName(), map3);
                    });
                    goalTotalList.computeIfAbsent(curr.getClazzNature(), o -> new ArrayList<>()).add(map1);
                });

                goalTotalList.forEach((k, v) -> {
                    v.sort(Comparator.comparing(item -> Integer.parseInt(item.get("clazzName").toString().replace("班", ""))));
                    v.forEach(item -> {
                        Map<String, Object> map2 = (Map<String, Object>) item.get("goals");
                        map2.forEach((b, t) -> {
                            List<BigDecimal>[] list = map.get(k.concat(",").concat(b));
                            List<BigDecimal>[] listValid = mapValid.get(k.concat(",").concat(b));
                            Map<String, Object> curr = (Map<String, Object>) t;
                            curr.put("pm", this.getRankDecimal(list[0], (BigDecimal)curr.get("ratio")));
                            curr.put("chinesePm", this.getRankDecimal(list[1], (BigDecimal)curr.get("chineseRatio")));
                            curr.put("chineseValidPm", this.getRankDecimal(listValid[0], (BigDecimal)curr.get("chineseValidAvgScore")));
                            curr.put("mathPm", this.getRankDecimal(list[2], (BigDecimal)curr.get("mathRatio")));
                            curr.put("mathValidPm", this.getRankDecimal(listValid[1], (BigDecimal)curr.get("mathValidAvgScore")));
                            curr.put("englishPm", this.getRankDecimal(list[3], (BigDecimal)curr.get("englishRatio")));
                            curr.put("englishValidPm", this.getRankDecimal(listValid[2], (BigDecimal)curr.get("englishValidAvgScore")));
                            curr.put("physicsPm", this.getRankDecimal(list[4], (BigDecimal)curr.get("physicsRatio")));
                            curr.put("physicsValidPm", this.getRankDecimal(listValid[3], (BigDecimal)curr.get("physicsValidAvgScore")));
                            curr.put("historyPm", this.getRankDecimal(list[5], (BigDecimal)curr.get("historyRatio")));
                            curr.put("historyValidPm", this.getRankDecimal(listValid[4], (BigDecimal)curr.get("historyValidAvgScore")));
                            curr.put("chemistryPm", this.getRankDecimal(list[6], (BigDecimal)curr.get("chemistryRatio")));
                            curr.put("chemistryValidPm", this.getRankDecimal(listValid[5], (BigDecimal)curr.get("chemistryValidAvgScore")));
                            curr.put("chemistryWeightedPm", this.getRankDecimal(list[7], (BigDecimal)curr.get("chemistryWeightedRatio")));
                            curr.put("chemistryWeightedValidPm", this.getRankDecimal(listValid[6], (BigDecimal)curr.get("chemistryWeightedValidAvgScore")));
                            curr.put("biologyPm", this.getRankDecimal(list[8], (BigDecimal)curr.get("biologyRatio")));
                            curr.put("biologyValidPm", this.getRankDecimal(listValid[7], (BigDecimal)curr.get("biologyValidAvgScore")));
                            curr.put("biologyWeightedPm", this.getRankDecimal(list[9], (BigDecimal)curr.get("biologyWeightedRatio")));
                            curr.put("biologyWeightedValidPm", this.getRankDecimal(listValid[8], (BigDecimal)curr.get("biologyWeightedValidAvgScore")));
                            curr.put("politicsPm", this.getRankDecimal(list[10], (BigDecimal)curr.get("politicsRatio")));
                            curr.put("politicsValidPm", this.getRankDecimal(listValid[9], (BigDecimal)curr.get("politicsValidAvgScore")));
                            curr.put("politicsWeightedPm", this.getRankDecimal(list[11], (BigDecimal)curr.get("politicsWeightedRatio")));
                            curr.put("politicsWeightedValidPm", this.getRankDecimal(listValid[10], (BigDecimal)curr.get("politicsWeightedValidAvgScore")));
                            curr.put("geographyPm", this.getRankDecimal(list[12], (BigDecimal)curr.get("geographyRatio")));
                            curr.put("geographyValidPm", this.getRankDecimal(listValid[11], (BigDecimal)curr.get("geographyValidAvgScore")));
                            curr.put("geographyWeightedPm", this.getRankDecimal(list[13], (BigDecimal)curr.get("geographyWeightedRatio")));
                            curr.put("geographyWeightedValidPm", this.getRankDecimal(listValid[12], (BigDecimal)curr.get("geographyWeightedValidAvgScore")));
                        });
                    });
                });
                res.put("goalTotalList", goalTotalList);
            }
        }
        return res;
    }

    @Override
    public List<Map<String, Object>> goalDetail(Long examId, Long clazzId, Long subjectId) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<ExamGoalDto> examGoalDtoList = this.examGoalService.getDefault(examId, null);
        if (CollectionUtils.isNotEmpty(examGoalDtoList)) {
            Clazz clazz = this.clazzMapper.selectById(clazzId);
            examGoalDtoList = examGoalDtoList.stream().filter(item -> item.getSubjectType().equals(clazz.getClazzNature().toString())).sorted(Comparator.comparing(ExamGoalDto::getTransGoalScore, Comparator.reverseOrder())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(examGoalDtoList)) {
                List<ExamResult> examResults = this.examResultMapper.selectList(Wrappers.<ExamResult>lambdaQuery().eq(ExamResult::getClazzId, clazzId).eq(ExamResult::getExamId, examId));
                if (CollectionUtils.isNotEmpty(examResults)) {
                    String alias = subjectId == 90002 ? "zff" : subjectMapper.selectOne(Wrappers.<Subject>lambdaQuery().eq(Subject::getId, subjectId)).getSubjectAlias();
                    for (ExamGoalDto examGoalDto : examGoalDtoList) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("goal", examGoalDto);
                        List<ExamResult> examResultsOrigin = new ArrayList<>();
                        List<ExamResult> examResultsFu = new ArrayList<>();
                        if ("zff".equals(alias)) {
                            examResultsOrigin = examResults.stream().filter(item -> item.getTotalWeightedScore().compareTo(examGoalDto.getTransGoalScore()) >= 0).collect(Collectors.toList());
                        } else if ("chinese".equals(alias)) {
                            examResultsOrigin = examResults.stream().filter(item -> item.getChineseScore().compareTo(examGoalDto.getChineseGoalScore()) >= 0).collect(Collectors.toList());
                        } else if ("math".equals(alias)) {
                            examResultsOrigin = examResults.stream().filter(item -> item.getMathScore().compareTo(examGoalDto.getMathGoalScore()) >= 0).collect(Collectors.toList());
                        } else if ("english".equals(alias)) {
                            examResultsOrigin = examResults.stream().filter(item -> item.getEnglishScore().compareTo(examGoalDto.getEnglishGoalScore()) >= 0).collect(Collectors.toList());
                        } else if ("physics".equals(alias)) {
                            examResultsOrigin = examResults.stream().filter(item -> item.getPhysicsScore().compareTo(examGoalDto.getPhysicsGoalScore()) >= 0).collect(Collectors.toList());
                        } else if ("history".equals(alias)) {
                            examResultsOrigin = examResults.stream().filter(item -> item.getHistoryScore().compareTo(examGoalDto.getHistoryGoalScore()) >= 0).collect(Collectors.toList());
                        } else if ("geography".equals(alias)) {
                            examResultsOrigin = examResults.stream().filter(item -> item.getGeographyScore().compareTo(examGoalDto.getGeographyGoalScore()) >= 0).collect(Collectors.toList());
                            examResultsFu = examResults.stream().filter(item -> item.getGeographyWeightedScore().compareTo(examGoalDto.getGeographyWeightedGoalScore()) >= 0).collect(Collectors.toList());
                        } else if ("politics".equals(alias)) {
                            examResultsOrigin = examResults.stream().filter(item -> item.getPoliticsScore().compareTo(examGoalDto.getPoliticsGoalScore()) >= 0).collect(Collectors.toList());
                            examResultsFu = examResults.stream().filter(item -> item.getPoliticsWeightedScore().compareTo(examGoalDto.getPoliticsWeightedGoalScore()) >= 0).collect(Collectors.toList());
                        } else if ("biology".equals(alias)) {
                            examResultsOrigin = examResults.stream().filter(item -> item.getBiologyScore().compareTo(examGoalDto.getBiologyGoalScore()) >= 0).collect(Collectors.toList());
                            examResultsFu = examResults.stream().filter(item -> item.getBiologyWeightedScore().compareTo(examGoalDto.getBiologyWeightedGoalScore()) >= 0).collect(Collectors.toList());
                        } else if ("chemistry".equals(alias)){
                            examResultsOrigin = examResults.stream().filter(item -> item.getChemistryScore().compareTo(examGoalDto.getChemistryGoalScore()) >= 0).collect(Collectors.toList());
                            examResultsFu = examResults.stream().filter(item -> item.getChemistryWeightedScore().compareTo(examGoalDto.getChemistryWeightedGoalScore()) >= 0).collect(Collectors.toList());
                        }
                        map.put("examResultsOrigin", examResultsOrigin);
                        map.put("examResultsFu", examResultsFu);
                        list.add(map);
                    }
                }
            }
        }
        return list;
    }

    @Override
    @SneakyThrows
    public XSSFWorkbook subjectTableCompareExportExcel(Long examId, Long subjectId, List<Long> clazzIds) {
        List<Map<String, Object>> list = this.subjectTableCompare(examId, subjectId, clazzIds);
        InputStream is = getClass().getResourceAsStream("/static/templates/科目对比模板.xlsx");
        XSSFWorkbook book = new XSSFWorkbook(is);
        if (CollectionUtils.isNotEmpty(list)) {
            XSSFCellStyle style = book.createCellStyle();
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderBottom(BorderStyle.THIN);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);

            XSSFSheet sheet = book.getSheetAt(0);
            // 生成表头
            Subject subject = this.subjectMapper.selectById(subjectId);
            XSSFRow row = sheet.getRow(0);
            XSSFCell cell = row.getCell(3);
            cell.setCellValue(subject.getName());
            if (YesNoEnum.YES.equals(subject.getHasWeight())) {
                cell = row.getCell(8);
                cell.setCellValue(subject.getName().concat("赋"));
            } else {
                row.removeCell(row.getCell(8));
                XSSFRow row1 = sheet.getRow(1);
                for (int i = 8; i < 13; row1.removeCell(row1.getCell(i)), i++);
            }

            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> curr = list.get(i);
                row = sheet.createRow(i + 2);

                cell = row.createCell(0, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue(curr.get("clazzName").toString());

                cell = row.createCell(1, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue(curr.get("studentCount").toString());

                if (!curr.get("scoreAvg").toString().equals("0")) {
                    cell = row.createCell(2, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(curr.get("joinCount").toString());

                    cell = row.createCell(3, CellType.NUMERIC);
                    cell.setCellStyle(style);
                    cell.setCellValue(Double.parseDouble(curr.get("scoreAvg").toString()));

                    cell = row.createCell(4, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(curr.getOrDefault("pm", "").toString());

                    cell = row.createCell(5, CellType.NUMERIC);
                    cell.setCellStyle(style);
                    cell.setCellValue(Double.parseDouble(curr.get("std").toString()));

                    cell = row.createCell(6, CellType.NUMERIC);
                    cell.setCellStyle(style);
                    cell.setCellValue(Double.parseDouble(curr.get("goodRatio").toString()));

                    cell = row.createCell(7, CellType.NUMERIC);
                    cell.setCellStyle(style);
                    cell.setCellValue(Double.parseDouble(curr.get("passRatio").toString()));

                    if (YesNoEnum.YES.equals(subject.getHasWeight())) {
                        cell = row.createCell(8, CellType.NUMERIC);
                        cell.setCellStyle(style);
                        cell.setCellValue(Double.parseDouble(curr.get("scoreAvgWeighted").toString()));

                        cell = row.createCell(9, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue(curr.getOrDefault("pmWeighted", "").toString());

                        cell = row.createCell(10, CellType.NUMERIC);
                        cell.setCellStyle(style);
                        cell.setCellValue(Double.parseDouble(curr.get("stdWeighted").toString()));

                        cell = row.createCell(11, CellType.NUMERIC);
                        cell.setCellStyle(style);
                        cell.setCellValue(Double.parseDouble(curr.get("goodRatioWeighted").toString()));

                        cell = row.createCell(12, CellType.NUMERIC);
                        cell.setCellStyle(style);
                        cell.setCellValue(Double.parseDouble(curr.get("passRatioWeighted").toString()));
                    }
                }
            }

        }
        return book;
    }

    private final BeanUtilsBean bean = new BeanUtilsBean(new ConvertUtilsBean() {
        @Override
        public Object convert(Object value, Class clazz) {
            if (clazz.equals(Double.class)) {
                return new BigDecimal(value.toString());
            }
            return super.convert(value, clazz);
        }
    });

    private ExamGoalDto getGoal(List<ExamGoalDto> examGoalDtoList, BigDecimal score) {
        if (CollectionUtils.isEmpty(examGoalDtoList)) {
            return null;
        }
        for (ExamGoalDto examGoalDto : examGoalDtoList) {
            if (score.compareTo(examGoalDto.getTransGoalScore()) >= 0) {
                return examGoalDto;
            }
        }
       return null;
    }

    @Override
    @SneakyThrows
    public XSSFWorkbook tableInfoClazzExportExcel(Long examId, Long clazzId, String needGoal, String numberOrScore) {
        InputStream is = getClass().getResourceAsStream("/static/templates/学生成绩表(成绩分析).xlsx");
        XSSFWorkbook book = new XSSFWorkbook(is);
        IPage<Map<String, Object>> iPage = this.examResultMapper.searchExamResultClazz(new Page<>(1, 100), examId, new ArrayList<Long>(){{add(clazzId);}}, null, null);
        List<Map<String, Object>> list = iPage.getRecords();
        if (CollectionUtils.isNotEmpty(list)) {
            XSSFCellStyle style = book.createCellStyle();
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderBottom(BorderStyle.THIN);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);

            XSSFCellStyle styleGoal = null;

            XSSFSheet sheet = book.getSheetAt(0);
            // 生成表头
            XSSFRow row0 = sheet.getRow(0);
            XSSFRow row1 = sheet.getRow(1);
            XSSFCell cell;
            int columnAdd = 8;
            if (list.stream().anyMatch(item -> !item.get("physicsScore").toString().equals("0"))) {
                CellRangeAddress rangeAddress = new CellRangeAddress(0, 0, columnAdd, columnAdd + 1);
                sheet.addMergedRegion(rangeAddress);
                cell = row0.createCell(columnAdd, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue("物理");

                cell = row1.createCell(columnAdd++, CellType.NUMERIC);
                cell.setCellStyle(style);
                cell.setCellValue("分数");

                cell = row1.createCell(columnAdd++, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue("名次");
            }
            if (list.stream().anyMatch(item -> !item.get("historyScore").toString().equals("0"))) {
                CellRangeAddress rangeAddress = new CellRangeAddress(0, 0, columnAdd, columnAdd + 1);
                sheet.addMergedRegion(rangeAddress);
                cell = row0.createCell(columnAdd, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue("历史");

                cell = row1.createCell(columnAdd++, CellType.NUMERIC);
                cell.setCellStyle(style);
                cell.setCellValue("分数");

                cell = row1.createCell(columnAdd++, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue("名次");
            }
            if (list.stream().anyMatch(item -> !item.get("chemistryScore").toString().equals("0"))) {
                CellRangeAddress rangeAddress = new CellRangeAddress(0, 0, columnAdd, columnAdd + 3);
                sheet.addMergedRegion(rangeAddress);
                cell = row0.createCell(columnAdd, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue("化学");

                cell = row1.createCell(columnAdd++, CellType.NUMERIC);
                cell.setCellStyle(style);
                cell.setCellValue("分数");

                cell = row1.createCell(columnAdd++, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue("名次");

                cell = row1.createCell(columnAdd++, CellType.NUMERIC);
                cell.setCellStyle(style);
                cell.setCellValue("赋分");

                cell = row1.createCell(columnAdd++, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue("名次");
            }
            if (list.stream().anyMatch(item -> !item.get("biologyScore").toString().equals("0"))) {
                CellRangeAddress rangeAddress = new CellRangeAddress(0, 0, columnAdd, columnAdd + 3);
                sheet.addMergedRegion(rangeAddress);
                cell = row0.createCell(columnAdd, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue("生物");

                cell = row1.createCell(columnAdd++, CellType.NUMERIC);
                cell.setCellStyle(style);
                cell.setCellValue("分数");

                cell = row1.createCell(columnAdd++, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue("名次");

                cell = row1.createCell(columnAdd++, CellType.NUMERIC);
                cell.setCellStyle(style);
                cell.setCellValue("赋分");

                cell = row1.createCell(columnAdd++, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue("名次");
            }
            if (list.stream().anyMatch(item -> !item.get("politicsScore").toString().equals("0"))) {
                CellRangeAddress rangeAddress = new CellRangeAddress(0, 0, columnAdd, columnAdd + 3);
                sheet.addMergedRegion(rangeAddress);
                cell = row0.createCell(columnAdd, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue("政治");

                cell = row1.createCell(columnAdd++, CellType.NUMERIC);
                cell.setCellStyle(style);
                cell.setCellValue("分数");

                cell = row1.createCell(columnAdd++, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue("名次");

                cell = row1.createCell(columnAdd++, CellType.NUMERIC);
                cell.setCellStyle(style);
                cell.setCellValue("赋分");

                cell = row1.createCell(columnAdd++, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue("名次");
            }
            if (list.stream().anyMatch(item -> !item.get("geographyScore").toString().equals("0"))) {
                CellRangeAddress rangeAddress = new CellRangeAddress(0, 0, columnAdd, columnAdd + 3);
                sheet.addMergedRegion(rangeAddress);
                cell = row0.createCell(columnAdd, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue("地理");

                cell = row1.createCell(columnAdd++, CellType.NUMERIC);
                cell.setCellStyle(style);
                cell.setCellValue("分数");

                cell = row1.createCell(columnAdd++, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue("名次");

                cell = row1.createCell(columnAdd++, CellType.NUMERIC);
                cell.setCellStyle(style);
                cell.setCellValue("赋分");

                cell = row1.createCell(columnAdd++, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue("名次");
            }
            CellRangeAddress rangeAddress = new CellRangeAddress(0, 0, columnAdd, columnAdd + 3);
            sheet.addMergedRegion(rangeAddress);
            cell = row0.createCell(columnAdd, CellType.STRING);
            cell.setCellStyle(style);
            cell.setCellValue("全科");

            cell = row1.createCell(columnAdd++, CellType.NUMERIC);
            cell.setCellStyle(style);
            cell.setCellValue("总分");

            cell = row1.createCell(columnAdd++, CellType.STRING);
            cell.setCellStyle(style);
            cell.setCellValue("班名");

            cell = row1.createCell(columnAdd++, CellType.NUMERIC);
            cell.setCellStyle(style);
            cell.setCellValue("总赋分");

            cell = row1.createCell(columnAdd, CellType.STRING);
            cell.setCellStyle(style);
            cell.setCellValue("班名");

            if ("01".equals(numberOrScore)) {
                list.sort(Comparator.comparing(item -> StringUtils.isNullOrEmpty(item.get("studentNumber").toString()) ? 0 : Integer.parseInt(item.get("studentNumber").toString())));
            } else {
                list.sort(Comparator.comparing(item -> new BigDecimal(item.get("totalWeightedScore").toString()), Comparator.reverseOrder()));
            }
            List<ExamGoalDto> examGoalDtoList = null;
            if ("01".equals(needGoal)) {
                examGoalDtoList = this.examGoalService.getDefault(examId, null);
                Clazz clazz = this.clazzMapper.selectById(clazzId);
                examGoalDtoList = examGoalDtoList.stream().filter(item -> item.getTotalCount() > 0 && item.getSubjectType().equals(clazz.getClazzNature().toString())).sorted(Comparator.comparing(ExamGoalDto::getTransGoalScore, Comparator.reverseOrder())).collect(Collectors.toList());
                styleGoal = book.createCellStyle();
                styleGoal.cloneStyleFrom(style);
                styleGoal.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                styleGoal.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
            }
            XSSFRow row;
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                row = sheet.createRow(i + 2);

                cell = row.createCell(0, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue(map.get("studentName").toString());
                cell = row.createCell(1, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue(map.get("studentNumber").toString());
                ExamResult curr = new ExamResult();
                bean.populate(curr, map);
                ExamResultOtherDto examResultOtherDto = JSONObject.parseObject(map.get("other").toString(), ExamResultOtherDto.class);
                ExamGoalDto examGoalDto = this.getGoal(examGoalDtoList, curr.getTotalWeightedScore());

                if (curr.getChineseScore().compareTo(BigDecimal.ZERO) > 0) {
                    cell = row.createCell(2, CellType.NUMERIC);
                    if (examGoalDto != null && curr.getChineseScore().compareTo(examGoalDto.getChineseGoalScore()) < 0) {
                        cell.setCellStyle(styleGoal);
                    } else {
                        cell.setCellStyle(style);
                    }
                    cell.setCellValue(curr.getChineseScore().doubleValue());

                    cell = row.createCell(3, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(examResultOtherDto.getChineseRankBj());
                }

                if (curr.getMathScore().compareTo(BigDecimal.ZERO) > 0) {
                    cell = row.createCell(4, CellType.NUMERIC);
                    if (examGoalDto != null && curr.getMathScore().compareTo(examGoalDto.getMathGoalScore()) < 0) {
                        cell.setCellStyle(styleGoal);
                    } else {
                        cell.setCellStyle(style);
                    }
                    cell.setCellValue(curr.getMathScore().doubleValue());

                    cell = row.createCell(5, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(examResultOtherDto.getMathRankBj());
                }

                if (curr.getEnglishScore().compareTo(BigDecimal.ZERO) > 0) {
                    cell = row.createCell(6, CellType.NUMERIC);
                    if (examGoalDto != null && curr.getEnglishScore().compareTo(examGoalDto.getEnglishGoalScore()) < 0) {
                        cell.setCellStyle(styleGoal);
                    } else {
                        cell.setCellStyle(style);
                    }
                    cell.setCellValue(curr.getEnglishScore().doubleValue());

                    cell = row.createCell(7, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(examResultOtherDto.getEnglishRankBj());
                }

                if (curr.getPhysicsScore().compareTo(BigDecimal.ZERO) > 0) {
                    cell = row.createCell(8, CellType.NUMERIC);
                    if (examGoalDto != null && curr.getPhysicsScore().compareTo(examGoalDto.getPhysicsGoalScore()) < 0) {
                        cell.setCellStyle(styleGoal);
                    } else {
                        cell.setCellStyle(style);
                    }
                    cell.setCellValue(curr.getPhysicsScore().doubleValue());

                    cell = row.createCell(9, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(examResultOtherDto.getPhysicsRankBj());
                }

                if (curr.getHistoryScore().compareTo(BigDecimal.ZERO) > 0) {
                    cell = row.createCell(10, CellType.NUMERIC);
                    if (examGoalDto != null && curr.getHistoryScore().compareTo(examGoalDto.getHistoryGoalScore()) < 0) {
                        cell.setCellStyle(styleGoal);
                    } else {
                        cell.setCellStyle(style);
                    }
                    cell.setCellValue(curr.getHistoryScore().doubleValue());

                    cell = row.createCell(11, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(examResultOtherDto.getHistoryRankBj());
                }

                if (curr.getChemistryScore().compareTo(BigDecimal.ZERO) > 0) {
                    cell = row.createCell(12, CellType.NUMERIC);
                    if (examGoalDto != null && curr.getChemistryScore().compareTo(examGoalDto.getChemistryGoalScore()) < 0) {
                        cell.setCellStyle(styleGoal);
                    } else {
                        cell.setCellStyle(style);
                    }
                    cell.setCellValue(curr.getChemistryScore().doubleValue());

                    cell = row.createCell(13, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(examResultOtherDto.getChemistryRankBj());
                }

                if (curr.getChemistryWeightedScore().compareTo(BigDecimal.ZERO) > 0) {
                    cell = row.createCell(14, CellType.NUMERIC);
                    if (examGoalDto != null && curr.getChemistryWeightedScore().compareTo(examGoalDto.getChemistryWeightedGoalScore()) < 0) {
                        cell.setCellStyle(styleGoal);
                    } else {
                        cell.setCellStyle(style);
                    }
                    cell.setCellValue(curr.getChemistryWeightedScore().doubleValue());

                    cell = row.createCell(15, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(examResultOtherDto.getChemistryWeightedRankBj());
                }

                if (curr.getBiologyScore().compareTo(BigDecimal.ZERO) > 0) {
                    cell = row.createCell(16, CellType.NUMERIC);
                    if (examGoalDto != null && curr.getBiologyScore().compareTo(examGoalDto.getBiologyGoalScore()) < 0) {
                        cell.setCellStyle(styleGoal);
                    } else {
                        cell.setCellStyle(style);
                    }
                    cell.setCellValue(curr.getBiologyScore().doubleValue());

                    cell = row.createCell(17, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(examResultOtherDto.getBiologyRankBj());
                }

                if (curr.getBiologyWeightedScore().compareTo(BigDecimal.ZERO) > 0) {
                    cell = row.createCell(18, CellType.NUMERIC);
                    if (examGoalDto != null && curr.getBiologyWeightedScore().compareTo(examGoalDto.getBiologyWeightedGoalScore()) < 0) {
                        cell.setCellStyle(styleGoal);
                    } else {
                        cell.setCellStyle(style);
                    }
                    cell.setCellValue(curr.getBiologyWeightedScore().doubleValue());

                    cell = row.createCell(19, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(examResultOtherDto.getBiologyWeightedRankBj());
                }

                if (curr.getPoliticsScore().compareTo(BigDecimal.ZERO) > 0) {
                    cell = row.createCell(20, CellType.NUMERIC);
                    if (examGoalDto != null && curr.getPoliticsScore().compareTo(examGoalDto.getPoliticsGoalScore()) < 0) {
                        cell.setCellStyle(styleGoal);
                    } else {
                        cell.setCellStyle(style);
                    }
                    cell.setCellValue(curr.getPoliticsScore().doubleValue());

                    cell = row.createCell(21, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(examResultOtherDto.getPoliticsRankBj());
                }

                if (curr.getPoliticsWeightedScore().compareTo(BigDecimal.ZERO) > 0) {
                    cell = row.createCell(22, CellType.NUMERIC);
                    if (examGoalDto != null && curr.getPoliticsWeightedScore().compareTo(examGoalDto.getPoliticsWeightedGoalScore()) < 0) {
                        cell.setCellStyle(styleGoal);
                    } else {
                        cell.setCellStyle(style);
                    }
                    cell.setCellValue(curr.getPoliticsWeightedScore().doubleValue());

                    cell = row.createCell(23, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(examResultOtherDto.getPoliticsWeightedRankBj());
                }

                if (curr.getGeographyScore().compareTo(BigDecimal.ZERO) > 0) {
                    cell = row.createCell(24, CellType.NUMERIC);
                    if (examGoalDto != null && curr.getGeographyScore().compareTo(examGoalDto.getGeographyGoalScore()) < 0) {
                        cell.setCellStyle(styleGoal);
                    } else {
                        cell.setCellStyle(style);
                    }
                    cell.setCellValue(curr.getGeographyScore().doubleValue());

                    cell = row.createCell(25, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(examResultOtherDto.getGeographyRankBj());
                }

                if (curr.getGeographyWeightedScore().compareTo(BigDecimal.ZERO) > 0) {
                    cell = row.createCell(26, CellType.NUMERIC);
                    if (examGoalDto != null && curr.getGeographyWeightedScore().compareTo(examGoalDto.getGeographyWeightedGoalScore()) < 0) {
                        cell.setCellStyle(styleGoal);
                    } else {
                        cell.setCellStyle(style);
                    }
                    cell.setCellValue(curr.getGeographyWeightedScore().doubleValue());

                    cell = row.createCell(27, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(examResultOtherDto.getGeographyWeightedRankBj());
                }

                if (curr.getTotalScore().compareTo(BigDecimal.ZERO) > 0) {
                    cell = row.createCell(28, CellType.NUMERIC);
                    cell.setCellStyle(style);
                    cell.setCellValue(curr.getTotalScore().doubleValue());

                    cell = row.createCell(29, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(examResultOtherDto.getTotalRankBj());
                }

                if (curr.getTotalWeightedScore().compareTo(BigDecimal.ZERO) > 0) {
                    cell = row.createCell(30, CellType.NUMERIC);
                    cell.setCellStyle(style);
                    cell.setCellValue(curr.getTotalWeightedScore().doubleValue());
                    cell = row.createCell(31, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(examResultOtherDto.getTotalWeightedRankBj());
                }
            }
        }
        return book;
    }

    @SuppressWarnings("unchecked")
    private void putData(XSSFSheet sheet, XSSFCellStyle style, Subject subject, int startRow, List<Map<String, Object>> column, List<Map<String, Object>> data) {
        XSSFRow row0 = sheet.createRow(startRow);
        XSSFRow row1 = sheet.createRow(startRow + 1);
        XSSFCell cell;
        int columnAdd = 0;
        CellRangeAddress rangeAddress = new CellRangeAddress(startRow, startRow + 1, columnAdd, columnAdd);
        sheet.addMergedRegion(rangeAddress);
        cell = row0.createCell(columnAdd, CellType.STRING);
        cell.setCellStyle(style);
        cell.setCellValue("班级");
        columnAdd += 1;

        rangeAddress = new CellRangeAddress(startRow, startRow + 1, columnAdd, columnAdd);
        sheet.addMergedRegion(rangeAddress);
        cell = row0.createCell(columnAdd, CellType.STRING);
        cell.setCellStyle(style);
        cell.setCellValue("人数");
        columnAdd += 1;

        rangeAddress = new CellRangeAddress(startRow, startRow + 1, columnAdd, columnAdd);
        sheet.addMergedRegion(rangeAddress);
        cell = row0.createCell(columnAdd, CellType.STRING);
        cell.setCellStyle(style);
        cell.setCellValue("参考数");
        columnAdd += 1;

        for (Map<String, Object> map : column) {
            if (subject != null && subject.getHasWeight().equals(YesNoEnum.YES)) {
                rangeAddress = new CellRangeAddress(startRow, startRow, columnAdd, columnAdd + 6);
                sheet.addMergedRegion(rangeAddress);
                cell = row0.createCell(columnAdd, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue(map.get("name").toString());

                cell = row1.createCell(columnAdd++, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue("目标数");

                cell = row1.createCell(columnAdd++, CellType.NUMERIC);
                cell.setCellStyle(style);
                cell.setCellValue(subject.getName().concat("≥").concat(map.get(subject.getSubjectAlias() + "GoalScore").toString()));

                cell = row1.createCell(columnAdd++, CellType.NUMERIC);
                cell.setCellStyle(style);
                cell.setCellValue("比例");

                cell = row1.createCell(columnAdd++, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue("名次");

                cell = row1.createCell(columnAdd++, CellType.NUMERIC);
                cell.setCellStyle(style);
                cell.setCellValue(subject.getName().concat("赋分").concat("≥").concat(map.get(subject.getSubjectAlias() + "GoalScore").toString()));

                cell = row1.createCell(columnAdd++, CellType.NUMERIC);
                cell.setCellStyle(style);
                cell.setCellValue("比例");

                cell = row1.createCell(columnAdd++, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue("名次");
            } else {
                rangeAddress = new CellRangeAddress(startRow, startRow, columnAdd, columnAdd + 3);
                sheet.addMergedRegion(rangeAddress);
                cell = row0.createCell(columnAdd, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue(map.get("name").toString());

                cell = row1.createCell(columnAdd++, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue("目标数");

                String value = subject == null ? "总赋分≥" + map.get("transGoalScore").toString() : subject.getName().concat("≥").concat(map.get(subject.getSubjectAlias() + "GoalScore").toString());
                cell = row1.createCell(columnAdd++, CellType.NUMERIC);
                cell.setCellStyle(style);
                cell.setCellValue(value);

                cell = row1.createCell(columnAdd++, CellType.NUMERIC);
                cell.setCellStyle(style);
                cell.setCellValue("比例");

                cell = row1.createCell(columnAdd++, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue("名次");
            }
        }

        XSSFRow rowData;
        for (int i = 0; i < data.size(); i++) {
            rowData = sheet.createRow(startRow + 2 + i);
            int columnIndex = 0;
            Map<String, Object> map = data.get(i);

            cell = rowData.createCell(columnIndex++, CellType.STRING);
            cell.setCellStyle(style);
            cell.setCellValue(map.get("clazzName").toString());

            cell = rowData.createCell(columnIndex++, CellType.STRING);
            cell.setCellStyle(style);
            cell.setCellValue(map.get("studentCnt").toString());

            cell = rowData.createCell(columnIndex++, CellType.STRING);
            cell.setCellStyle(style);
            cell.setCellValue(map.get(subject == null ? "joinCnt" : subject.getSubjectAlias().concat("JoinCnt")).toString());

            if (map.containsKey("goals")) {
                Map<String, Object> goals = (Map<String, Object>) map.get("goals");
                for (Map<String, Object> stringObjectMap : column) {
                    Map<String, Object> goalMap = (Map<String, Object>) goals.get(stringObjectMap.get("name").toString());
                    cell = rowData.createCell(columnIndex++, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(goalMap.get("shouldMeetingCnt").toString());
                    if (subject != null && subject.getHasWeight().equals(YesNoEnum.YES)) {
                        cell = rowData.createCell(columnIndex++, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue(goalMap.get(subject.getSubjectAlias() + "MeetingCnt").toString());

                        cell = rowData.createCell(columnIndex++, CellType.NUMERIC);
                        cell.setCellStyle(style);
                        cell.setCellValue(Double.parseDouble(goalMap.get(subject.getSubjectAlias() + "Ratio").toString()));

                        cell = rowData.createCell(columnIndex++, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue(goalMap.get(subject.getSubjectAlias() + "Pm").toString());

                        cell = rowData.createCell(columnIndex++, CellType.NUMERIC);
                        cell.setCellStyle(style);
                        cell.setCellValue(goalMap.get(subject.getSubjectAlias() + "WeightedMeetingCnt").toString());

                        cell = rowData.createCell(columnIndex++, CellType.NUMERIC);
                        cell.setCellStyle(style);
                        cell.setCellValue(Double.parseDouble(goalMap.get(subject.getSubjectAlias() + "WeightedRatio").toString()));

                        cell = rowData.createCell(columnIndex++, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue(goalMap.get(subject.getSubjectAlias() + "WeightedPm").toString());
                    } else {
                        cell = rowData.createCell(columnIndex++, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue(goalMap.get(subject == null ? "clazzMeetingCnt" : subject.getSubjectAlias() + "MeetingCnt").toString());

                        cell = rowData.createCell(columnIndex++, CellType.NUMERIC);
                        cell.setCellStyle(style);
                        cell.setCellValue(Double.parseDouble(goalMap.get(subject == null ? "ratio" : subject.getSubjectAlias() + "Ratio").toString()));

                        cell = rowData.createCell(columnIndex++, CellType.STRING);
                        cell.setCellStyle(style);
                        cell.setCellValue(goalMap.get(subject == null ? "pm" : subject.getSubjectAlias() + "Pm").toString());
                    }
                }
            }
        }
    }


    @Override
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public XSSFWorkbook goalExportExcel(Long examId, Long subjectId, List<Long> clazzIds) {
        XSSFWorkbook book = new XSSFWorkbook();
        Map<String, Object> map = this.goal(examId, clazzIds);
        if (CollectionUtils.isNotEmpty(map)) {
            XSSFSheet sheet = book.createSheet("目标");

            XSSFCellStyle style = book.createCellStyle();
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderBottom(BorderStyle.THIN);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Subject subject = this.subjectMapper.selectById(subjectId);
            Map<String, List<ExamGoalDto>> map1 = (Map<String, List<ExamGoalDto>>) map.get("goalTotalColumns");
            Map<String, List<Map<String, Object>>> map2 = (Map<String, List<Map<String, Object>>>) map.get("goalTotalList");
            int startRow = 0;
            String[] keyArr = {"SCIENCE", "LIBERAL", "OTHER"};
            for (String s : keyArr) {
                if (map1.containsKey(s)) {
                    List<Map<String, Object>> column = map1.get(s).stream().map(
                            item -> {
                                Map<String, Object> map3;
                                try {
                                    map3 =  bean.getPropertyUtils().describe(item);
                                } catch (Exception e) {
                                    throw new RuntimeException("bean to map fail！");
                                }
                                return map3;
                            }
                    ).collect(Collectors.toList());
                    this.putData(sheet, style, subject, startRow, column, map2.get(s));
                    startRow += map2.get(s).size() + 4;
                }
            }
        }
        return book;
    }

    private void generateScore(Long subjectId, List<ExamResultSimpleDto> examResultSimpleDtos) {
        if (CollectionUtils.isNotEmpty(examResultSimpleDtos)) {
            ClazzNatureEnum first = examResultSimpleDtos.get(0).getClazzNature();
            if (subjectId == 90001) {
                examResultSimpleDtos.forEach(item -> item.setScore(item.getTotalScore()));
            } else if (subjectId == 90002) {
                examResultSimpleDtos.forEach(item -> item.setScore(item.getTotalWeightedScore()));
            } else if (subjectId == 90003 || (subjectId == 90004 && ClazzNatureEnum.OTHER.equals(first))) {
                examResultSimpleDtos.forEach(item -> item.setScore(
                        item.getChineseScore().add(item.getMathScore()).add(item.getEnglishScore())));
            } else if (subjectId == 90004) {
                examResultSimpleDtos.forEach(item -> item.setScore(
                        item.getChineseScore().add(item.getMathScore()).add(item.getEnglishScore()).add(item.getPhysicsScore()).add(item.getHistoryScore())));
            } else {
                Subject subject = subjectMapper.selectOne(Wrappers.<Subject>lambdaQuery()
                        .eq(Subject::getId, subjectId > 1e5 ? subjectId - 1e5 : subjectId));
                if (subject != null) {
                    String alias = subject.getSubjectAlias();
                    if ("chinese".equals(alias)) {
                        examResultSimpleDtos.forEach(item -> item.setScore(item.getChineseScore()));
                    } else if ("math".equals(alias)) {
                        examResultSimpleDtos.forEach(item -> item.setScore(item.getMathScore()));
                    } else if ("english".equals(alias)) {
                        examResultSimpleDtos.forEach(item -> item.setScore(item.getEnglishScore()));
                    } else if ("physics".equals(alias)) {
                        examResultSimpleDtos.forEach(item -> item.setScore(item.getPhysicsScore()));
                    } else if ("history".equals(alias)) {
                        examResultSimpleDtos.forEach(item -> item.setScore(item.getHistoryScore()));
                    } else if ("geography".equals(alias)) {
                        examResultSimpleDtos.forEach(item -> item.setScore(subjectId > 1e5 ?
                                item.getGeographyWeightedScore() : item.getGeographyScore()));
                    } else if ("politics".equals(alias)) {
                        examResultSimpleDtos.forEach(item -> item.setScore(subjectId > 1e5 ?
                                item.getPoliticsWeightedScore() : item.getPoliticsScore()));
                    } else if ("biology".equals(alias)) {
                        examResultSimpleDtos.forEach(item -> item.setScore(subjectId > 1e5 ?
                                item.getBiologyWeightedScore() : item.getBiologyScore()));
                    } else {
                        examResultSimpleDtos.forEach(item -> item.setScore(subjectId > 1e5 ?
                                item.getChemistryWeightedScore() : item.getChemistryScore()));
                    }
                }
            }
        }
    }

    @Override
    @SneakyThrows
    public Map<String, Object> edge(ExamEdgeSubParam param, YesNoEnum selfCustom, List<Long> clazzIds) {
        Map<String, Object> map = new HashMap<>();
        if (CollectionUtils.isEmpty(clazzIds)) {
            clazzIds = this.getOrDefault(clazzIds, param.getExamId());
        }

        List<ExamResultSimpleDto> examResultSimpleDtos = this.examResultMapper.getSimpleResultByList(param.getExamId(), clazzIds);

        Long subjectId = param.getSubjectId();
        Integer baseValue = param.getBaseValue();
        Integer upperValue = param.getUpperValue();
        Integer lowerValue = param.getLowerValue();

        ExamEdgeSub examEdgeSub = this.examEdgeSubMapper.selectOne(Wrappers.<ExamEdgeSub>lambdaQuery()
                .eq(ExamEdgeSub::getExamId, param.getExamId())
                .eq(ExamEdgeSub::getSubjectId, param.getSubjectId()));
        if (upperValue == null || lowerValue == null) {
            if (YesNoEnum.NO.equals(selfCustom) || examEdgeSub == null) return map;
            upperValue = examEdgeSub.getUpperValue();
            lowerValue = examEdgeSub.getLowerValue();
            baseValue = examEdgeSub.getBaseValue();
        } else if (YesNoEnum.YES.equals(selfCustom)){
            User user = (User) SecurityUtils.getSubject().getPrincipal();
            if (FunctionEnum.DEAN.equals(user.getStaff().getFunction())) {
                if (examEdgeSub == null) {
                    examEdgeSub = new ExamEdgeSub();
                    examEdgeSub.setExamId(param.getExamId());
                    examEdgeSub.setSubjectId(param.getSubjectId());
                    examEdgeSub.setBaseValue(param.getBaseValue());
                    examEdgeSub.setLowerValue(param.getLowerValue());
                    examEdgeSub.setUpperValue(param.getUpperValue());
                    this.examEdgeSubMapper.insert(examEdgeSub);
                } else {
                    examEdgeSub.setBaseValue(param.getBaseValue());
                    examEdgeSub.setLowerValue(param.getLowerValue());
                    examEdgeSub.setUpperValue(param.getUpperValue());
                    this.examEdgeSubMapper.updateById(examEdgeSub);
                }
            }
        }
        map.put("param", examEdgeSub);

        if (CollectionUtils.isNotEmpty(examResultSimpleDtos)) {
            this.generateScore(subjectId, examResultSimpleDtos);
            Map<Long, List<ExamResultSimpleDto>> map1 = examResultSimpleDtos.stream().collect(Collectors.groupingBy(ExamResultSimpleDto::getClazzId));
            List<Map<String, Object>> dataList = new ArrayList<>();
            map.put("dataList", dataList);
            BigDecimal low = BigDecimal.valueOf(lowerValue);
            BigDecimal up = BigDecimal.valueOf(upperValue);
            BigDecimal base = BigDecimal.valueOf(baseValue);
            String str = String.valueOf(subjectId > 100000L ? subjectId - 100000L : subjectId);
            for (Map.Entry<Long, List<ExamResultSimpleDto>> entry : map1.entrySet()) {
                List<ExamResultSimpleDto> value = entry.getValue();
                if (subjectId != 90001 && subjectId != 90002 && subjectId != 90003 && subjectId != 90004) {
                    String[] clazzDivision = value.get(0).getClazzDivision().split(",");
                    List<String> list = Arrays.asList(clazzDivision);
                    if (!list.contains(str)) continue;
                }
                Map<String, Object> map3 = new HashMap<>();
                map3.put("clazzId", entry.getKey());
                map3.put("clazzName", value.get(0).getClazzName());
                map3.put("otherDivision", value.get(0).getOtherDivision());
                int upperCnt = 0, lowerCnt = 0;
                for (ExamResultSimpleDto examResultSimpleDto : value) {
                    BigDecimal score = examResultSimpleDto.getScore();
                    if (score.compareTo(low) >= 0 && score.compareTo(up) <= 0) {
                        if (score.compareTo(base) >= 0) upperCnt ++;
                        else lowerCnt++;
                    }
                }
                map3.put("lowerCnt", lowerCnt);
                map3.put("upperCnt", upperCnt);
                dataList.add(map3);
            }
            dataList.sort(Comparator.comparing(item -> Integer.parseInt(item.get("clazzName").toString().replace("班", ""))));
        }
        return map;
    }

    @Override
    @SneakyThrows
    public List<ExamResult> edgeDetail(ExamEdgeSubParam param, List<Long> clazzIds) {
        BigDecimal low = BigDecimal.valueOf(param.getLowerValue());
        BigDecimal up = BigDecimal.valueOf(param.getUpperValue());

        String state = StringUtils.isNullOrEmpty(param.getState()) ? "00" : param.getState();
        String applyStr;
        String orderByStr = null;
        Long subjectId = param.getSubjectId();
        if (subjectId == 90001) {
            applyStr = "total_score";
        } else if (subjectId == 90002) {
            applyStr = "total_weighted_score";
        } else if (subjectId == 90003) {
            applyStr = "chinese_score + math_score + english_score";
            orderByStr = "(" + applyStr + ")";
        } else if (subjectId == 90004) {
            applyStr = "chinese_score + math_score + english_score + history_score + physics_score";
            orderByStr = "(" + applyStr + ")";
        } else {
            Subject subject = subjectMapper.selectOne(Wrappers.<Subject>lambdaQuery().eq(Subject::getId, subjectId > 1e5 ? subjectId - 1e5 : subjectId));
            applyStr = subject.getSubjectAlias().concat(subjectId > 1e5 ? "_weighted" : "").concat("_score");
        }
        if (StringUtils.isNullOrEmpty(orderByStr)) orderByStr = applyStr;

        QueryWrapper<ExamResult> wrapper = Wrappers.query();
        wrapper.eq("exam_id", param.getExamId());
        wrapper.in(CollectionUtils.isNotEmpty(clazzIds), "clazz_id", clazzIds);
        wrapper.apply(applyStr + (state.startsWith("0") ? ">" : ">=") + "{0}", low);
        wrapper.apply(applyStr + (state.endsWith("0") ? "<" : "<=") + "{0}", up);
        wrapper.orderByDesc(orderByStr);
        return this.examResultMapper.selectList(wrapper);
    }

    @Override
    public List<ExamGoalDto> goalList(Long examId) {
        if (examId == null) return new ArrayList<>();
        List<ExamGoalDto> examGoalDtoList = this.examGoalService.getDefault(examId, null);
        for (int i = 0; i < examGoalDtoList.size(); i++) {
            ExamGoalDto item = examGoalDtoList.get(i);
            String prefix = ClazzNatureEnum.valueOf(item.getSubjectType()).getName().replace("班", "");
            item.setName(prefix.concat("-").concat(item.getName()));
            item.setId((long) i);
        }
        return examGoalDtoList;
    }

    @Override
    @SuppressWarnings("unchecked")
    @SneakyThrows
    public List<Map<String, Object>> radioScore(List<Map<String, Object>> examResultSimpleDtoList) {
        if (CollectionUtils.isEmpty(examResultSimpleDtoList))
            return Collections.emptyList();
        List<Map<String, Object>> res = new ArrayList<>();
        BigDecimal five = BigDecimal.valueOf(5);
        BigDecimal qdw = new BigDecimal("7.5");
        CompletableFuture<Void>[] completableFutures = new CompletableFuture[examResultSimpleDtoList.size()];
        for (int i = 0; i < completableFutures.length; i++) {
            Map<String, Object> obj = examResultSimpleDtoList.get(i);
            Map<String, Object> map = new HashMap<>();
            res.add(map);
            CompletableFuture<Void> c = CompletableFuture.runAsync(() -> {
                Long examId = Long.valueOf(obj.get("examId").toString());
                String clazzNatureEnumStr = obj.get("clazzNature").toString();
                List<Map<String, Object>> examResultListAll = this.examResultMapper.getSimpleResult1(examId, null, clazzNatureEnumStr);

                map.put("examId", examId);
                map.put("name", obj.get("name"));

                for (String str : TwxUtils.arrPrefix) {
                    BigDecimal score;
                    String keyPrefix;
                    BigDecimal scoreWeighted = new BigDecimal(obj.getOrDefault(str + "WeightedScore", "0").toString());
                    if (scoreWeighted.compareTo(BigDecimal.ZERO) <= 0) {
                        score = new BigDecimal(obj.getOrDefault(str + "Score", "0").toString());
                        keyPrefix = str + "Score";
                    } else {
                        score = scoreWeighted;
                        keyPrefix = str + "WeightedScore";
                    }
                    if (score.compareTo(BigDecimal.ZERO) > 0) {
                        List<BigDecimal> decimalList = examResultListAll.stream()
                                .map(item -> new BigDecimal(item.get(keyPrefix).toString()))
                                .filter(item -> item.compareTo(BigDecimal.ZERO) > 0)
                                .collect(Collectors.toList());
                        double dd = decimalList.stream().collect(Collectors.averagingDouble(BigDecimal::doubleValue));
                        BigDecimal avg = BigDecimal.valueOf(dd).setScale(1, RoundingMode.HALF_UP);
                        BigDecimal std = this.std(avg, decimalList);
                        BigDecimal zScore = std.compareTo(BigDecimal.ZERO) == 0 ? std : ((score.subtract(avg)).divide(std, 2, RoundingMode.HALF_UP));
                        zScore = zScore.add(five);
                        String key = str + "ZScore";
                        map.put(key, zScore.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : (zScore.compareTo(qdw) > 0 ? qdw : zScore));
                        map.put(key + "Actual", zScore);
                    }
                }
            }, threadPoolExecutor);
            completableFutures[i] = c;
        }
        CompletableFuture.allOf(completableFutures).get();
        return res;
    }

}
