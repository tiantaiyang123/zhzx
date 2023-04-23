/**
* 项目：中华中学管理平台
* 模型分组：成绩管理
* 模型名称：考试分段表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhzx.server.domain.*;
import com.zhzx.server.dto.StaffLessonTeacherDto;
import com.zhzx.server.dto.exam.ExamGradeAnalyseClazzSituationDto;
import com.zhzx.server.dto.exam.ExamResultSimpleDto;
import com.zhzx.server.enums.ClazzNatureEnum;
import com.zhzx.server.enums.ExamCompareEnum;
import com.zhzx.server.enums.FunctionEnum;
import com.zhzx.server.enums.GoalEnum;
import com.zhzx.server.misc.SpringUtils;
import com.zhzx.server.repository.*;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.ExamGoalSubService;
import com.zhzx.server.service.UserService;
import com.zhzx.server.util.StringUtils;
import com.zhzx.server.util.TwxUtils;
import com.zhzx.server.vo.ExamGoalSubStyleVo;
import com.zhzx.server.vo.ExamGoalSubTotalVo;
import com.zhzx.server.vo.ExamGoalSubVo;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExamGoalSubServiceImpl extends ServiceImpl<ExamGoalSubMapper, ExamGoalSub> implements ExamGoalSubService {

    @Resource
    private ExamResultMapper examResultMapper;

    @Resource
    private SubjectMapper subjectMapper;

    @Resource
    private ExamGoalSubMapper examGoalSubMapper;

    @Resource
    private ExamMapper examMapper;

    @Resource
    private ClazzMapper clazzMapper;

    @Resource
    private UserService userService;

    @Resource
    private StaffLessonTeacherMapper staffLessonTeacherMapper;

    @Override
    public int updateAllFieldsById(ExamGoalSub entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<ExamGoalSub> saveAll(List<ExamGoalSubVo> examGoalSubVos, Long examId, Long subjectId, String subjectType) {
        List<ExamGoalSub> inserts = new ArrayList<>();
        examGoalSubVos.forEach(item -> {
            ExamGoalSub examGoalSub = new ExamGoalSub();
            examGoalSub.setExamId(examId);
            examGoalSub.setSubjectId(subjectId);
            examGoalSub.setSubjectType(GoalEnum.valueOf(subjectType));
            examGoalSub.setType(item.getOp());
            examGoalSub.setGoalValue(item.getScore());
            examGoalSub.setStyleJson(JSONObject.toJSONString(item.getStyle()));
            inserts.add(examGoalSub);
        });

        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (!FunctionEnum.DEAN.equals(user.getStaff().getFunction()))
            return inserts;
        this.examGoalSubMapper.delete(Wrappers.<ExamGoalSub>lambdaQuery()
                .eq(ExamGoalSub::getExamId, examId)
                .eq(ExamGoalSub::getSubjectId, subjectId)
                .eq(ExamGoalSub::getSubjectType, GoalEnum.valueOf(subjectType)));
        if (CollectionUtils.isNotEmpty(inserts))
            this.examGoalSubMapper.batchInsert(inserts);
        return inserts;
    }

    private List<ExamResultSimpleDto> generateScore(Long examId, Long subjectId) {
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
        }
        return examResultSimpleDtos;
    }

    private List<ExamGoalSub> generateDefault(String clazzNature, Long subjectId, Long examId, List<ExamGoalSub> examGoalSubs, List<ExamResultSimpleDto> examResultSimpleDtoList) {
        List<ExamGoalSub> inserts = new ArrayList<>();
        Map<String, Integer> map2 = new HashMap<>();
        if (subjectId == 90001 || subjectId == 90002) {
            Map<ClazzNatureEnum, List<ExamResultSimpleDto>> map1 = examResultSimpleDtoList.stream()
                    .collect(Collectors.groupingBy(ExamResultSimpleDto::getClazzNature));
            final List<String> currHas = CollectionUtils.isNotEmpty(examGoalSubs) ?
                    examGoalSubs.stream().map(item -> item.getSubjectType().toString()).collect(Collectors.toList())
                    : new ArrayList<>();
            map1.forEach((k, v) -> {
                if (!currHas.contains(k.toString())) {
                    if (StringUtils.isNullOrEmpty(clazzNature) || clazzNature.equals(k.toString())) {
                        v.sort(Comparator.comparing(ExamResultSimpleDto::getScore).reversed());
                        int highest = v.get(0).getScore().setScale(0, RoundingMode.DOWN).intValue();
                        map2.put(k.toString(), highest - highest % 10);
                    }
                }
            });
        } else {
            if (CollectionUtils.isEmpty(examGoalSubs)) {
                if (StringUtils.isNullOrEmpty(clazzNature) || "OTHER".equals(clazzNature)) {
                    examResultSimpleDtoList.sort(Comparator.comparing(ExamResultSimpleDto::getScore).reversed());
                    int highest = examResultSimpleDtoList.get(0).getScore().setScale(0, RoundingMode.DOWN).intValue();
                    map2.put("OTHER", highest - highest % 10);
                }
            }
        }
        for (int i = 0; i <= 11; i++) {
            for (Map.Entry<String, Integer> m : map2.entrySet()) {
                if (m.getValue() > 0) {
                    ExamGoalSub examGoalSub = new ExamGoalSub();
                    examGoalSub.setExamId(examId);
                    examGoalSub.setSubjectId(subjectId);
                    examGoalSub.setGoalValue(m.getValue());
                    examGoalSub.setSubjectType(GoalEnum.valueOf(m.getKey()));
                    examGoalSub.setStyleJson("");
                    examGoalSub.setType(i == 11 ? ExamCompareEnum.LE : ExamCompareEnum.GE);
                    inserts.add(examGoalSub);
                    m.setValue(i == 10 ? examGoalSub.getGoalValue() : examGoalSub.getGoalValue() - 10);
                }
            }
        }
        return inserts;
    }

    private boolean checkArguments(List<ExamGoalSubVo> examGoalSubVos) {
        List<String> filterList = examGoalSubVos.stream().map(item -> item.getOp().toString().concat(item.getScore().toString())).distinct().collect(Collectors.toList());
        return filterList.size() > 20 || filterList.size() < examGoalSubVos.size();
    }

    private List<Map<String, Object>> getRange(List<ExamGoalSub> v) {
        List<Map<String, Object>> lineChart = new ArrayList<>();
        for (int i = 1; i < v.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("left", v.get(i).getGoalValue());
            map.put("right", v.get(i - 1).getGoalValue());
            lineChart.add(map);
        }
        return lineChart;
    }

    @Override
    public Map<String, Object> partition(ExamGoalSubTotalVo examGoalSubTotalVo) {
        Map<String, Object> map = new HashMap<>();
        Long examId = examGoalSubTotalVo.getExamId();
        Long subjectId = examGoalSubTotalVo.getSubjectId();
        if (examId == null || subjectId == null) {
            return map;
        }
        String clazzNature = examGoalSubTotalVo.getClazzNature();
        List<Long> clazzIds = examGoalSubTotalVo.getClazzIds();
        String type = examGoalSubTotalVo.getType();
        if (clazzIds == null && "02".equals(type)) {
            Exam exam = examMapper.selectOne(Wrappers.<Exam>lambdaQuery().select(Exam::getAcademicYearSemesterId, Exam::getGradeId).eq(Exam::getId, examId));
            Long academicYearSemesterId = exam.getAcademicYearSemesterId();
            List<Clazz> clazzList = this.userService.mutateYear(academicYearSemesterId);
            if (CollectionUtils.isEmpty(clazzList)) {
                throw new ApiCode.ApiException(-5, "当前角色无班级！");
            }
            clazzIds = clazzList.stream().filter(item -> item.getGradeId().equals(exam.getGradeId())).map(Clazz::getId).collect(Collectors.toList());
        }
        List<ExamGoalSubVo> examGoalSubVos = examGoalSubTotalVo.getExamGoalSubVos();
        List<ExamResultSimpleDto> examResultSimpleDtoList = this.generateScore(examId, subjectId);
        if (CollectionUtils.isNotEmpty(examResultSimpleDtoList)) {
            Map<String, List<ExamGoalSub>> maps;
            List<ExamGoalSub> examGoalSubs;
            if (CollectionUtils.isEmpty(examGoalSubVos)) {
                LambdaQueryWrapper<ExamGoalSub> wrapper = Wrappers.<ExamGoalSub>lambdaQuery()
                        .eq(ExamGoalSub::getExamId, examId)
                        .eq(ExamGoalSub::getSubjectId, subjectId);
                if (!StringUtils.isNullOrEmpty(clazzNature)) {
                    wrapper.eq(ExamGoalSub::getSubjectType, GoalEnum.valueOf(clazzNature));
                }
                synchronized (this) {
                    examGoalSubs = this.examGoalSubMapper.selectListSimple(wrapper);
                    List<ExamGoalSub> inserts = this.generateDefault(clazzNature, subjectId, examId, examGoalSubs, examResultSimpleDtoList);
                    if (CollectionUtils.isNotEmpty(inserts)) {
                        this.examGoalSubMapper.batchInsert(inserts);
                        if (examGoalSubs == null) {
                            examGoalSubs = inserts;
                        } else {
                            examGoalSubs.addAll(inserts);
                        }
                    }
                }
            } else {
                if (StringUtils.isNullOrEmpty(clazzNature)) {
                    throw new ApiCode.ApiException(-5, "分段性质不能为空！");
                }
                if (this.checkArguments(examGoalSubVos)) {
                    throw new ApiCode.ApiException(-5, "分段重复或分段数超过20");
                }
                // 自调用
                ExamGoalSubService examGoalSubService = SpringUtils.getBean(ExamGoalSubService.class);
                examGoalSubs = examGoalSubService.saveAll(examGoalSubVos, examId, subjectId, clazzNature);
            }
            maps = examGoalSubs.stream().collect(Collectors.groupingBy(o -> o.getSubjectType().toString()));
            Map<Long, List<ExamResultSimpleDto>> mapClazz = examResultSimpleDtoList.stream()
                    .collect(Collectors.groupingBy(ExamResultSimpleDto::getClazzId));
            if (CollectionUtils.isNotEmpty(clazzIds)) {
                Iterator<Long> iterator = mapClazz.keySet().iterator();
                while (iterator.hasNext()) {
                    Long next = iterator.next();
                    if (!clazzIds.contains(next)) {
                        iterator.remove();
                    }
                }

                if (subjectId == 90001 || subjectId == 90002) {
                    List<String> natureList = this.clazzMapper.selectList(Wrappers.<Clazz>lambdaQuery().select(Clazz::getClazzNature).in(Clazz::getId, clazzIds)).stream().map(item -> item.getClazzNature().toString()).distinct().collect(Collectors.toList());
                    maps.keySet().removeIf(next -> !natureList.contains(next));
                }
            }
            // 任课教师
            List<StaffLessonTeacherDto> staffLessonTeacherList = this.staffLessonTeacherMapper.selectByGradeAndClazz(null, subjectId, examId, clazzIds);
            Map<String, List<StaffLessonTeacherDto>> staffLessonTeacherMap = staffLessonTeacherList.stream().collect(Collectors.groupingBy(item -> item.getClazzId().toString().concat(item.getMSubjectId().toString())));

            maps.forEach((k, v) -> {
                Map<String, Object> mapSingle = new HashMap<>();
                mapSingle.put("clazzName", "合计");
                mapSingle.put("clazzId", -1L);
                mapSingle.put("studentCnt", 0);
                mapSingle.put("joinCnt", 0);
                mapSingle.put("scoreAvg", BigDecimal.ZERO);
                v.sort((o1, o2) -> {
                    if (o1.getType().equals(o2.getType())) {
                        return o2.getGoalValue().compareTo(o1.getGoalValue());
                    }
                    if (ExamCompareEnum.GE.equals(o1.getType())) {
                        return -1;
                    }
                    return 1;
                });
                Map<String, Object> map1 = new HashMap<>();
                map1.put("columns", v);
                List<Map<String, Object>> data = new ArrayList<>();
                List<BigDecimal> rankList = new ArrayList<>();
                mapClazz.forEach((u, t) -> {
                    ExamResultSimpleDto examResultSimpleDto = t.get(0);
                    if ("OTHER".equals(k) || examResultSimpleDto.getClazzNature().toString().equals(k)) {
                        Map<String, Object> curr = new HashMap<>();
                        curr.put("clazzName", examResultSimpleDto.getClazzName());
                        curr.put("clazzId", u);
                        curr.put("teacher", staffLessonTeacherMap.get(u.toString().concat(subjectId.toString())));
                        curr.put("clazzLevel", examResultSimpleDto.getClazzLevel());
                        curr.put("otherDivision", examResultSimpleDto.getOtherDivision());
                        List<BigDecimal> tmpList = t.stream().map(ExamResultSimpleDto::getScore).filter(item -> item.compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
                        int size = tmpList.size();
                        BigDecimal sumScore = tmpList.stream().reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
                        BigDecimal scoreAvg = sumScore.divide(BigDecimal.valueOf(Math.max(size, 1)), 1, RoundingMode.HALF_UP);
                        rankList.add(scoreAvg);
                        curr.put("scoreAvg", scoreAvg);
                        curr.put("studentCnt", examResultSimpleDto.getStudentCount());
                        curr.put("joinCnt", size);
                        mapSingle.put("studentCnt", (int)mapSingle.get("studentCnt") + examResultSimpleDto.getStudentCount());
                        mapSingle.put("joinCnt", (int)mapSingle.get("joinCnt") + size);
                        mapSingle.put("scoreAvg", ((BigDecimal)mapSingle.get("scoreAvg")).add(sumScore));
                        v.forEach(item -> {
                            BigDecimal score = new BigDecimal(item.getGoalValue());
                            String title = "C_" + item.getType().toString() + item.getGoalValue();
                            curr.put(title, t.stream().filter(o ->
                                    (ExamCompareEnum.LE.equals(item.getType())) == (o.getScore().compareTo(score) < 0))
                                    .count());
                            mapSingle.put(title, (long)mapSingle.getOrDefault(title, 0L) + (long)curr.get(title));
                        });
                        data.add(curr);
                    }
                });
                data.sort(Comparator.comparing(item -> Integer.parseInt(item.get("clazzName").toString().replace("班","")), Comparator.naturalOrder()));
                rankList.sort(Comparator.reverseOrder());
                data.forEach(item -> item.put("scoreAvgRank", TwxUtils.binarySearchEnhance1(rankList, (BigDecimal) item.get("scoreAvg"))));
                mapSingle.put("scoreAvg", ((BigDecimal)mapSingle.get("scoreAvg")).divide(BigDecimal.valueOf(Math.max((int)mapSingle.get("joinCnt"), 1)), 1, RoundingMode.HALF_UP));
                data.add(mapSingle);
                map1.put("data", data);
                map.put(k, map1);
            });

            // 工作台折线图
            if ("02".equals(type)) {
                Map<String, Object> mapLineChart = new HashMap<>();
                map.put("lineChart", mapLineChart);
                long[] arr = new long[1];
                maps.forEach((k, v) -> {
                    List<Map<String, Object>> lineChart = this.getRange(v);
                    mapLineChart.put(k, lineChart);
                    lineChart.forEach(item -> {
                        BigDecimal left = new BigDecimal(item.get("left").toString());
                        BigDecimal right = new BigDecimal(item.get("right").toString());
                        arr[0] = 0;
                        mapClazz.forEach((u, t) -> {
                            ExamResultSimpleDto examResultSimpleDto = t.get(0);
                            if ("OTHER".equals(k) || examResultSimpleDto.getClazzNature().toString().equals(k)) {
                                long count = t.stream().filter(o -> o.getScore().compareTo(left) >= 0 && o.getScore().compareTo(right) < 0).count();
                                String prefix = examResultSimpleDto.getClazzNature().toString().equals("OTHER") ? "" : examResultSimpleDto.getClazzNature().toString().equals("SCIENCE") ? "物" : "史";
                                String otherDivision = examResultSimpleDto.getOtherDivision();
                                String suffix = prefix.equals("") ? "" : prefix.concat(otherDivision.substring(0, 1)).concat(otherDivision.substring(3, 4));
                                item.put("C" + u + "_" + suffix + examResultSimpleDto.getClazzName(), count);
                                arr[0] += count;
                            }
                        });
                        item.put("C0" + "_" + "全部班级", arr[0]);
                    });
                });

            }
        }
        return map;
    }

    @Override
    public Map<String, Object> clazzAverage(Long examId) {
        Map<String, Object> map = new HashMap<>();
        List<ExamGradeAnalyseClazzSituationDto> res = this.examResultMapper.gradeAnalyseClazz(examId);
        if (CollectionUtils.isNotEmpty(res)) {
            Map<String, Integer> cntMap = new HashMap<>();
            Map<String, List<ExamGradeAnalyseClazzSituationDto>> map1 = res.stream().collect(Collectors.groupingBy(ExamGradeAnalyseClazzSituationDto::getClazzNature));
            map1.forEach((k, t) -> t.forEach(item -> {
                cntMap.put(k, cntMap.getOrDefault(k, 0) + item.getStudentCount());
                item.setChineseRank((int) t.stream().filter(o -> o.getChineseAvg().compareTo(item.getChineseAvg()) > 0).count() + 1);
                item.setMathRank((int) t.stream().filter(o -> o.getMathAvg().compareTo(item.getMathAvg()) > 0).count() + 1);
                item.setEnglishRank((int) t.stream().filter(o -> o.getEnglishAvg().compareTo(item.getEnglishAvg()) > 0).count() + 1);
                item.setPhysicsRank((int) t.stream().filter(o -> o.getPhysicsAvg().compareTo(item.getPhysicsAvg()) > 0).count() + 1);
                item.setHistoryRank((int) t.stream().filter(o -> o.getHistoryAvg().compareTo(item.getHistoryAvg()) > 0).count() + 1);
                item.setChemistryRank((int) t.stream().filter(o -> o.getChemistryAvg().compareTo(item.getChemistryAvg()) > 0).count() + 1);
                item.setChemistryWeightedRank((int) t.stream().filter(o -> o.getChemistryWeightedAvg().compareTo(item.getChemistryWeightedAvg()) > 0).count() + 1);
                item.setBiologyRank((int) t.stream().filter(o -> o.getBiologyAvg().compareTo(item.getBiologyAvg()) > 0).count() + 1);
                item.setBiologyWeightedRank((int) t.stream().filter(o -> o.getBiologyWeightedAvg().compareTo(item.getBiologyWeightedAvg()) > 0).count() + 1);
                item.setPoliticsRank((int) t.stream().filter(o -> o.getPoliticsAvg().compareTo(item.getPoliticsAvg()) > 0).count() + 1);
                item.setPoliticsWeightedRank((int) t.stream().filter(o -> o.getPoliticsWeightedAvg().compareTo(item.getPoliticsWeightedAvg()) > 0).count() + 1);
                item.setGeographyRank((int) t.stream().filter(o -> o.getGeographyAvg().compareTo(item.getGeographyAvg()) > 0).count() + 1);
                item.setGeographyWeightedRank((int) t.stream().filter(o -> o.getGeographyWeightedAvg().compareTo(item.getGeographyWeightedAvg()) > 0).count() + 1);
                item.setTotalRank((int) t.stream().filter(o -> o.getTotalAvg().compareTo(item.getTotalAvg()) > 0).count() + 1);
                item.setTotalWeightedRank((int) t.stream().filter(o -> o.getTotalWeightedAvg().compareTo(item.getTotalWeightedAvg()) > 0).count() + 1);
            }));

            // 添加分科数据
            List<String> natureList = new ArrayList<>(map1.keySet());
            for (int i = 0; i < natureList.size(); i++) {
                String s = natureList.get(i);
                Map<String, Object> averageNatureMap = this.examResultMapper.gradeAverage(examId, s);
                ExamGradeAnalyseClazzSituationDto examGradeAnalyseClazzSituationDto = new ExamGradeAnalyseClazzSituationDto();
                examGradeAnalyseClazzSituationDto.setId((long) -i);
                examGradeAnalyseClazzSituationDto.setName(ClazzNatureEnum.valueOf(s).getName());
                examGradeAnalyseClazzSituationDto.setClazzNature(s);
                examGradeAnalyseClazzSituationDto.setOtherDivision("");
                examGradeAnalyseClazzSituationDto.setStudentCount(cntMap.get(s));
                examGradeAnalyseClazzSituationDto.setJoinCount(Integer.parseInt(averageNatureMap.get("joinCount").toString()));
                examGradeAnalyseClazzSituationDto.setTotalAvg(new BigDecimal(averageNatureMap.get("totalAvgNj").toString()));
                examGradeAnalyseClazzSituationDto.setTotalWeightedAvg(new BigDecimal(averageNatureMap.get("totalWeightedAvgNj").toString()));
                examGradeAnalyseClazzSituationDto.setChineseAvg(new BigDecimal(averageNatureMap.get("chineseAvgNj").toString()));
                examGradeAnalyseClazzSituationDto.setMathAvg(new BigDecimal(averageNatureMap.get("mathAvgNj").toString()));
                examGradeAnalyseClazzSituationDto.setEnglishAvg(new BigDecimal(averageNatureMap.get("englishAvgNj").toString()));
                examGradeAnalyseClazzSituationDto.setHistoryAvg(new BigDecimal(averageNatureMap.get("historyAvgNj").toString()));
                examGradeAnalyseClazzSituationDto.setPhysicsAvg(new BigDecimal(averageNatureMap.get("physicsAvgNj").toString()));
                examGradeAnalyseClazzSituationDto.setGeographyAvg(new BigDecimal(averageNatureMap.get("geographyAvgNj").toString()));
                examGradeAnalyseClazzSituationDto.setGeographyWeightedAvg(new BigDecimal(averageNatureMap.get("geographyWeightedAvgNj").toString()));
                examGradeAnalyseClazzSituationDto.setBiologyAvg(new BigDecimal(averageNatureMap.get("biologyAvgNj").toString()));
                examGradeAnalyseClazzSituationDto.setBiologyWeightedAvg(new BigDecimal(averageNatureMap.get("biologyWeightedAvgNj").toString()));
                examGradeAnalyseClazzSituationDto.setPoliticsAvg(new BigDecimal(averageNatureMap.get("politicsAvgNj").toString()));
                examGradeAnalyseClazzSituationDto.setPoliticsWeightedAvg(new BigDecimal(averageNatureMap.get("politicsWeightedAvgNj").toString()));
                examGradeAnalyseClazzSituationDto.setChemistryAvg(new BigDecimal(averageNatureMap.get("chemistryAvgNj").toString()));
                examGradeAnalyseClazzSituationDto.setChemistryWeightedAvg(new BigDecimal(averageNatureMap.get("chemistryWeightedAvgNj").toString()));
                res.add(examGradeAnalyseClazzSituationDto);
            }

            List<String> natures = Arrays.stream(ClazzNatureEnum.values()).map(ClazzNatureEnum::getName).collect(Collectors.toList());
            res.sort((o1, o2) -> {
                String clazzNature1 = o1.getClazzNature();
                String clazzNature2 = o2.getClazzNature();
                if (!clazzNature1.equals(clazzNature2)) {
                    return clazzNature1.equals(ClazzNatureEnum.SCIENCE.toString()) ? -1 : 1;
                }
                String name1 = o1.getName();
                String name2 = o2.getName();
                if (natures.contains(name1) || natures.contains(name2))
                    return natures.contains(name1) ? 1 : -1;
                int i = Integer.parseInt(name1.replace("班", ""));
                int j = Integer.parseInt(name2.replace("班", ""));
                return Integer.compare(i, j);
            });
            map.put("res", res);
        }
        return map;
    }

    @Override
    @SuppressWarnings("unchecked")
    @SneakyThrows
    public XSSFWorkbook clazzAverageExportExcel(Long examId) {
        List<ExamGradeAnalyseClazzSituationDto> list = (List<ExamGradeAnalyseClazzSituationDto>) this.clazzAverage(examId).get("res");
        InputStream is = getClass().getResourceAsStream("/static/templates/班级对比模板.xlsx");
        XSSFWorkbook book = new XSSFWorkbook(is);
        if (CollectionUtils.isNotEmpty(list)) {
            IndexedColors[] colors = new IndexedColors[5];
            colors[0] = IndexedColors.BLUE;
            colors[1] = IndexedColors.GREEN;
            colors[2] = IndexedColors.YELLOW;
            colors[3] = IndexedColors.PINK;
            colors[4] = IndexedColors.SKY_BLUE;

            XSSFCellStyle style = book.createCellStyle();
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderBottom(BorderStyle.THIN);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            XSSFSheet sheet = book.getSheetAt(0);
            XSSFRow row;
            XSSFCell cell;
            String key = "";
            for (int i = 0, iColor = -1; i < list.size(); i++) {
                XSSFCellStyle currStyle = book.createCellStyle();
                currStyle.cloneStyleFrom(style);
                ExamGradeAnalyseClazzSituationDto curr = list.get(i);
                row = sheet.createRow(i + 2);

                String keyCurr = curr.getClazzNature().concat("_").concat(curr.getOtherDivision());
                if (!keyCurr.equals(key)) {
                    iColor = (iColor + 1) % colors.length;
                }
                currStyle.setFillForegroundColor(colors[iColor].getIndex());
                key = keyCurr;

                cell = row.createCell(0, CellType.STRING);
                cell.setCellStyle(currStyle);
                cell.setCellValue(curr.getName());

                cell = row.createCell(1, CellType.STRING);
                cell.setCellStyle(currStyle);
                cell.setCellValue(curr.getStudentCount());

                cell = row.createCell(2, CellType.STRING);
                cell.setCellStyle(currStyle);
                cell.setCellValue(curr.getJoinCount());

                if (curr.getChineseAvg().compareTo(BigDecimal.ZERO) > 0) {
                    cell = row.createCell(3, CellType.NUMERIC);
                    cell.setCellStyle(currStyle);
                    cell.setCellValue(curr.getChineseAvg().doubleValue());
                    cell = row.createCell(4, CellType.STRING);
                    cell.setCellStyle(currStyle);
                    cell.setCellValue(curr.getChineseRank());
                }

                if (curr.getMathAvg().compareTo(BigDecimal.ZERO) > 0) {
                    cell = row.createCell(5, CellType.NUMERIC);
                    cell.setCellStyle(currStyle);
                    cell.setCellValue(curr.getMathAvg().doubleValue());
                    cell = row.createCell(6, CellType.STRING);
                    cell.setCellStyle(currStyle);
                    cell.setCellValue(curr.getMathRank());
                }

                if (curr.getEnglishAvg().compareTo(BigDecimal.ZERO) > 0) {
                    cell = row.createCell(7, CellType.NUMERIC);
                    cell.setCellStyle(currStyle);
                    cell.setCellValue(curr.getEnglishAvg().doubleValue());
                    cell = row.createCell(8, CellType.STRING);
                    cell.setCellStyle(currStyle);
                    cell.setCellValue(curr.getEnglishRank());
                }

                if (curr.getPhysicsAvg().compareTo(BigDecimal.ZERO) > 0) {
                    cell = row.createCell(9, CellType.NUMERIC);
                    cell.setCellStyle(currStyle);
                    cell.setCellValue(curr.getPhysicsAvg().doubleValue());
                    cell = row.createCell(10, CellType.STRING);
                    cell.setCellStyle(currStyle);
                    cell.setCellValue(curr.getPhysicsRank());
                }

                if (curr.getHistoryAvg().compareTo(BigDecimal.ZERO) > 0) {
                    cell = row.createCell(11, CellType.NUMERIC);
                    cell.setCellStyle(currStyle);
                    cell.setCellValue(curr.getHistoryAvg().doubleValue());
                    cell = row.createCell(12, CellType.STRING);
                    cell.setCellStyle(currStyle);
                    cell.setCellValue(curr.getHistoryRank());
                }

                if (curr.getChemistryAvg().compareTo(BigDecimal.ZERO) > 0) {
                    cell = row.createCell(13, CellType.NUMERIC);
                    cell.setCellStyle(currStyle);
                    cell.setCellValue(curr.getChemistryAvg().doubleValue());
                    cell = row.createCell(14, CellType.STRING);
                    cell.setCellStyle(currStyle);
                    cell.setCellValue(curr.getChemistryRank());
                }

                if (curr.getChemistryWeightedAvg().compareTo(BigDecimal.ZERO) > 0) {
                    cell = row.createCell(15, CellType.NUMERIC);
                    cell.setCellStyle(currStyle);
                    cell.setCellValue(curr.getChemistryWeightedAvg().doubleValue());
                    cell = row.createCell(16, CellType.STRING);
                    cell.setCellStyle(currStyle);
                    cell.setCellValue(curr.getChemistryWeightedRank());
                }

                if (curr.getBiologyAvg().compareTo(BigDecimal.ZERO) > 0) {
                    cell = row.createCell(17, CellType.NUMERIC);
                    cell.setCellStyle(currStyle);
                    cell.setCellValue(curr.getBiologyAvg().doubleValue());
                    cell = row.createCell(18, CellType.STRING);
                    cell.setCellStyle(currStyle);
                    cell.setCellValue(curr.getBiologyRank());
                }

                if (curr.getBiologyWeightedAvg().compareTo(BigDecimal.ZERO) > 0) {
                    cell = row.createCell(19, CellType.NUMERIC);
                    cell.setCellStyle(currStyle);
                    cell.setCellValue(curr.getBiologyWeightedAvg().doubleValue());
                    cell = row.createCell(20, CellType.STRING);
                    cell.setCellStyle(currStyle);
                    cell.setCellValue(curr.getBiologyWeightedRank());
                }

                if (curr.getPoliticsAvg().compareTo(BigDecimal.ZERO) > 0) {
                    cell = row.createCell(21, CellType.NUMERIC);
                    cell.setCellStyle(currStyle);
                    cell.setCellValue(curr.getPoliticsAvg().doubleValue());
                    cell = row.createCell(22, CellType.STRING);
                    cell.setCellStyle(currStyle);
                    cell.setCellValue(curr.getPoliticsRank());
                }

                if (curr.getPoliticsWeightedAvg().compareTo(BigDecimal.ZERO) > 0) {
                    cell = row.createCell(23, CellType.NUMERIC);
                    cell.setCellStyle(currStyle);
                    cell.setCellValue(curr.getPoliticsWeightedAvg().doubleValue());
                    cell = row.createCell(24, CellType.STRING);
                    cell.setCellStyle(currStyle);
                    cell.setCellValue(curr.getPoliticsWeightedRank());
                }

                if (curr.getGeographyAvg().compareTo(BigDecimal.ZERO) > 0) {
                    cell = row.createCell(25, CellType.NUMERIC);
                    cell.setCellStyle(currStyle);
                    cell.setCellValue(curr.getGeographyAvg().doubleValue());
                    cell = row.createCell(26, CellType.STRING);
                    cell.setCellStyle(currStyle);
                    cell.setCellValue(curr.getGeographyRank());
                }

                if (curr.getGeographyWeightedAvg().compareTo(BigDecimal.ZERO) > 0) {
                    cell = row.createCell(27, CellType.NUMERIC);
                    cell.setCellStyle(currStyle);
                    cell.setCellValue(curr.getGeographyWeightedAvg().doubleValue());
                    cell = row.createCell(28, CellType.STRING);
                    cell.setCellStyle(currStyle);
                    cell.setCellValue(curr.getGeographyWeightedRank());
                }

                if (curr.getTotalAvg().compareTo(BigDecimal.ZERO) > 0) {
                    cell = row.createCell(29, CellType.NUMERIC);
                    cell.setCellStyle(currStyle);
                    cell.setCellValue(curr.getTotalAvg().doubleValue());
                    cell = row.createCell(30, CellType.STRING);
                    cell.setCellStyle(currStyle);
                    cell.setCellValue(curr.getTotalRank());
                }

                if (curr.getTotalWeightedAvg().compareTo(BigDecimal.ZERO) > 0) {
                    cell = row.createCell(31, CellType.NUMERIC);
                    cell.setCellStyle(currStyle);
                    cell.setCellValue(curr.getTotalWeightedAvg().doubleValue());
                    cell = row.createCell(32, CellType.STRING);
                    cell.setCellStyle(currStyle);
                    cell.setCellValue(curr.getTotalWeightedRank());
                }
            }
        }
        return book;
    }

    private void mutateStyle(XSSFCellStyle style, ExamGoalSubStyleVo examGoalSubStyleVo) {
        if (examGoalSubStyleVo != null && !StringUtils.isNullOrEmpty(examGoalSubStyleVo.getBackground())) {
            String s1 = examGoalSubStyleVo.getBackground().replace("#", "");
            if (s1.length() == 3) s1 += s1;
            if (s1.length() == 6) {
                XSSFColor xssfColor = new XSSFColor(new java.awt.Color(Integer.parseInt(s1, 16)), new DefaultIndexedColorMap());
                style.setFillForegroundColor(xssfColor);
            }
        }
    }

    private void putData(XSSFSheet sheet, XSSFCellStyle style, int startRow, List<ExamGoalSub> column, List<Map<String, Object>> data) {
        XSSFRow row0 = sheet.createRow(startRow);
        XSSFCell cell;
        cell = row0.createCell(0, CellType.STRING);
        cell.setCellStyle(style);
        cell.setCellValue("班级");

        cell = row0.createCell(1, CellType.STRING);
        cell.setCellStyle(style);
        cell.setCellValue("人数");

        cell = row0.createCell(2, CellType.STRING);
        cell.setCellStyle(style);
        cell.setCellValue("参考数");

        ExamGoalSubStyleVo examGoalSubStyleVo;

        for (int i = 0, columnAdd = 3; i < column.size(); i++, columnAdd++) {
            ExamGoalSub curr = column.get(i);
            cell = row0.createCell(columnAdd, CellType.STRING);
            examGoalSubStyleVo = JSONObject.parseObject(curr.getStyleJson(), ExamGoalSubStyleVo.class);
            XSSFCellStyle styleMutate = (XSSFCellStyle) style.clone();
            this.mutateStyle(styleMutate, examGoalSubStyleVo);
            cell.setCellStyle(styleMutate);
            cell.setCellValue((curr.getType().equals(ExamCompareEnum.GE) ? "≥" : "<") + curr.getGoalValue());
        }

        for (int i = 0, rn = startRow + 1; i < data.size(); i++, rn++) {
            Map<String, Object> curr = data.get(i);
            row0 = sheet.createRow(rn);

            cell = row0.createCell(0, CellType.STRING);
            cell.setCellStyle(style);
            cell.setCellValue(curr.get("clazzName").toString());

            cell = row0.createCell(1, CellType.STRING);
            cell.setCellStyle(style);
            cell.setCellValue(curr.get("studentCnt").toString());

            cell = row0.createCell(2, CellType.STRING);
            cell.setCellStyle(style);
            cell.setCellValue(curr.get("joinCnt").toString());

            int columnAdd = 3;
            for (ExamGoalSub examGoalSub : column) {
                cell = row0.createCell(columnAdd++, CellType.STRING);
                examGoalSubStyleVo = JSONObject.parseObject(examGoalSub.getStyleJson(), ExamGoalSubStyleVo.class);
                XSSFCellStyle styleMutate = (XSSFCellStyle) style.clone();
                this.mutateStyle(styleMutate, examGoalSubStyleVo);
                cell.setCellStyle(styleMutate);
                cell.setCellValue(curr.get("C_" + examGoalSub.getType().toString() + examGoalSub.getGoalValue()).toString());
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public XSSFWorkbook partitionExportExcel(ExamGoalSubTotalVo examGoalSubTotalVo) {
        XSSFWorkbook book = new XSSFWorkbook();
        if (CollectionUtils.isEmpty(examGoalSubTotalVo.getExamGoalSubVos())) {
            Map<String, Object> map = this.partition(examGoalSubTotalVo);
            if (CollectionUtils.isNotEmpty(map)) {
                XSSFCellStyle style = book.createCellStyle();
                style.setBorderLeft(BorderStyle.THIN);
                style.setBorderTop(BorderStyle.THIN);
                style.setBorderRight(BorderStyle.THIN);
                style.setAlignment(HorizontalAlignment.CENTER);
                style.setVerticalAlignment(VerticalAlignment.CENTER);
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                style.setBorderBottom(BorderStyle.THIN);

                XSSFSheet sheet = book.createSheet("分段");
                int startRow = 0;
                String[] keyArr = {"SCIENCE", "LIBERAL", "OTHER"};
                for (String s : keyArr) {
                    if (map.containsKey(s)) {
                        Map<String, Object> mapS = (Map<String, Object>) map.get(s);
                        List<Map<String, Object>> data = (List<Map<String, Object>>) mapS.get("data");
                        this.putData(sheet, style, startRow, (List<ExamGoalSub>) mapS.get("columns"), data);
                        startRow += data.size() + 4;
                    }
                }
            }
        }
        return book;
    }

}
