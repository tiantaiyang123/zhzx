/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：学校管理
 * 模型名称：考试结果表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.google.common.base.CharMatcher;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.zhzx.server.domain.*;
import com.zhzx.server.dto.exam.*;
import com.zhzx.server.enums.ClazzNatureEnum;
import com.zhzx.server.enums.ExamNaturalSettingEnum;
import com.zhzx.server.enums.SemesterEnum;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.misc.SpringUtils;
import com.zhzx.server.repository.*;
import com.zhzx.server.repository.base.ExamResultBaseMapper;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.ExamGoalService;
import com.zhzx.server.service.ExamResultService;
import com.zhzx.server.service.ExamService;
import com.zhzx.server.service.SubjectService;
import com.zhzx.server.util.CellUtils;
import com.zhzx.server.util.JsonUtils;
import com.zhzx.server.util.StringUtils;
import com.zhzx.server.util.TwxUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.*;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ExamResultServiceImpl extends ServiceImpl<ExamResultMapper, ExamResult> implements ExamResultService {

    @Resource
    private ExamResultMinorMapper examResultMinorMapper;

    @Resource
    private ExamMapper examMapper;

    @Resource
    private SubjectService subjectService;

    @Resource
    private StudentMapper studentMapper;
    
    @Resource
    private ExamResultMapper examResultMapper;

    @Resource
    private ClazzMapper clazzMapper;

    @Resource
    private ExamGoalNaturalMapper examGoalNaturalMapper;

    @Resource
    private ExamGoalNaturalTemplateMapper examGoalNaturalTemplateMapper;

    @Resource
    private ExamGoalMapper examGoalMapper;

    @Resource
    private AcademicYearSemesterMapper academicYearSemesterMapper;

    @Override
    public int updateAllFieldsById(ExamResult entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
    }

    @Override
    public Map<String, Object> clazzTrendStation(Long gradeId) {
        Map<String, Object> res = new HashMap<>();
        Map<String, List<ExamGradeAnalyseClazzSituationDto>> multimap1 = new HashMap<>();
        Map<String, List<ExamGradeAnalyseClazzSituationDto>> multimap2 = null;
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        final Long academicYearSemesterId = user.getAcademicYearSemester().getId();
        List<Long> academicYearSemesterList = new ArrayList<>();
        academicYearSemesterList.add(academicYearSemesterId);
        if (SemesterEnum.Q2.equals(user.getAcademicYearSemester().getSemester())) {
            AcademicYearSemester academicYearSemester = this.academicYearSemesterMapper.selectOne(Wrappers.<AcademicYearSemester>lambdaQuery()
                    .eq(AcademicYearSemester::getYear, user.getAcademicYearSemester().getYear())
                    .eq(AcademicYearSemester::getSemester, SemesterEnum.Q1));
            academicYearSemesterList.add(academicYearSemester.getId());
        }
        LambdaQueryWrapper<Exam> wrapper = Wrappers.<Exam>lambdaQuery()
                .eq(Exam::getGradeId, gradeId)
                .in(Exam::getAcademicYearSemesterId, academicYearSemesterList)
                .lt(Exam::getExamEndDate, new Date())
                .orderByAsc(Exam::getExamEndDate);
        List<Exam> examList = this.examMapper.selectList(wrapper);
        if (examList == null) {
            examList = new ArrayList<>();
        }
        if (CollectionUtils.isNotEmpty(examList)) {
            List<Long> list = examList.stream().map(Exam::getId).collect(Collectors.toList());
            List<ExamGradeAnalyseClazzSituationDto> curr = this.getBaseMapper().clazzTrendStation(list);

            // 过滤掉没有结果的考试
            List<Long> examWithResult = curr.stream().map(ExamGradeAnalyseClazzSituationDto::getExamId).distinct().collect(Collectors.toList());
            examList = examList.stream().filter(o -> examWithResult.contains(o.getId())).collect(Collectors.toList());

            Multimap<Long, ExamGradeAnalyseClazzSituationDto> multimap = HashMultimap.create();
            List<Long> list1 = null;
            if (academicYearSemesterList.size() > 1) {
                list1 = examList.stream().filter(o -> Objects.equals(o.getAcademicYearSemesterId(), academicYearSemesterId)).map(Exam::getId).collect(Collectors.toList());
                multimap2 = new HashMap<>();
            }
            for (ExamGradeAnalyseClazzSituationDto item : curr) {
                multimap.put(item.getExamId(), item);
                multimap1.computeIfAbsent(item.getName(), o -> new ArrayList<>()).add(item);
                if (list1 != null && list1.contains(item.getExamId())) {
                    multimap2.computeIfAbsent(item.getName(), o -> new ArrayList<>()).add(item);
                }
            }
            curr.forEach(item -> {
                item.setTotalRank((int)multimap.get(item.getExamId()).stream().filter(o -> o.getTotalAvg().compareTo(item.getTotalAvg()) > 0).count() + 1);
                item.setTotalWeightedRank((int)multimap.get(item.getExamId()).stream().filter(o -> o.getTotalWeightedAvg().compareTo(item.getTotalWeightedAvg()) > 0).count() + 1);
                item.setChineseRank((int)multimap.get(item.getExamId()).stream().filter(o -> o.getChineseAvg().compareTo(item.getChineseAvg()) > 0).count() + 1);
                item.setMathRank((int)multimap.get(item.getExamId()).stream().filter(o -> o.getMathAvg().compareTo(item.getMathAvg()) > 0).count() + 1);
                item.setEnglishRank((int)multimap.get(item.getExamId()).stream().filter(o -> o.getEnglishAvg().compareTo(item.getEnglishAvg()) > 0).count() + 1);
                item.setHistoryRank((int)multimap.get(item.getExamId()).stream().filter(o -> o.getHistoryAvg().compareTo(item.getHistoryAvg()) > 0).count() + 1);
                item.setPhysicsRank((int)multimap.get(item.getExamId()).stream().filter(o -> o.getPhysicsAvg().compareTo(item.getPhysicsAvg()) > 0).count() + 1);
                item.setChemistryRank((int)multimap.get(item.getExamId()).stream().filter(o -> o.getChemistryAvg().compareTo(item.getChemistryAvg()) > 0).count() + 1);
                item.setChemistryWeightedRank((int)multimap.get(item.getExamId()).stream().filter(o -> o.getChemistryWeightedAvg().compareTo(item.getChemistryWeightedAvg()) > 0).count() + 1);
                item.setBiologyRank((int)multimap.get(item.getExamId()).stream().filter(o -> o.getBiologyAvg().compareTo(item.getBiologyAvg()) > 0).count() + 1);
                item.setBiologyWeightedRank((int)multimap.get(item.getExamId()).stream().filter(o -> o.getBiologyWeightedAvg().compareTo(item.getBiologyWeightedAvg()) > 0).count() + 1);
                item.setPoliticsRank((int)multimap.get(item.getExamId()).stream().filter(o -> o.getPoliticsAvg().compareTo(item.getPoliticsAvg()) > 0).count() + 1);
                item.setPoliticsWeightedRank((int)multimap.get(item.getExamId()).stream().filter(o -> o.getPoliticsWeightedAvg().compareTo(item.getPoliticsWeightedAvg()) > 0).count() + 1);
                item.setGeographyRank((int)multimap.get(item.getExamId()).stream().filter(o -> o.getGeographyAvg().compareTo(item.getGeographyAvg()) > 0).count() + 1);
                item.setGeographyWeightedRank((int)multimap.get(item.getExamId()).stream().filter(o -> o.getGeographyWeightedAvg().compareTo(item.getGeographyWeightedAvg()) > 0).count() + 1);
            });
        }
        res.put("dataAll", multimap1);
        res.put("dataCurr", academicYearSemesterList.size() == 1 ? multimap1 : multimap2);
        res.put("examAll", examList);
        res.put("examCurr", academicYearSemesterList.size() == 1 ? examList : examList.stream().filter(o -> Objects.equals(o.getAcademicYearSemesterId(), academicYearSemesterId)).collect(Collectors.toList()));
        return res;
    }

    @Override
    public List<ExamResultSimpleDto> personalListStation(Long examId, Long clazzId) {
        List<ExamResultSimpleDto> curr = new ArrayList<>();
        if (clazzId != null) {
            curr = this.examResultMapper.getSimpleResult(examId, clazzId);
            if (CollectionUtils.isNotEmpty(curr)) {
                Map<Long, String> map;
                Exam exam = this.examMapper.selectById(examId);
                List<Exam> preExamList = this.examMapper.selectList(Wrappers.<Exam>lambdaQuery()
                        .select(Exam::getId, Exam::getExamEndDate)
                        .eq(Exam::getGradeId, exam.getGradeId())
                        .eq(Exam::getSchoolyardId, exam.getSchoolyardId())
                        .eq(Exam::getAcademicYearSemesterId, exam.getAcademicYearSemesterId())
                        .lt(Exam::getExamEndDate, exam.getExamEndDate())
                        .orderByDesc(Exam::getExamEndDate));
                if (CollectionUtils.isNotEmpty(preExamList)) {
                    List<ExamResultSimpleDto> pre = this.getBaseMapper().getSimpleResult(preExamList.get(0).getId(), clazzId);
                    if (CollectionUtils.isNotEmpty(pre)) {
                        map = pre.stream().collect(Collectors.toMap(ExamResultSimpleDto::getStudentId, ExamResultSimpleDto::getOther));
                        curr.forEach(item -> {
                            item.setOtherPre(map.getOrDefault(item.getStudentId(), ""));
                        });
                    }
                }
            }
        }
        return curr;
    }

    private void getScore(Subject subject, List<ExamGradeAnalyseClazzSituationDto> curr, List<ExamGradeAnalyseClazzSituationDto> pre) {
        Map<Long, ExamGradeAnalyseClazzSituationDto> map = pre.stream().collect(Collectors.toMap(ExamGradeAnalyseClazzSituationDto::getId, o -> o));
        if (subject == null) {
            curr.forEach(item -> {
                item.setScore(item.getTotalAvg());
                if (map.containsKey(item.getId())) {
                    item.setScoreLast(map.get(item.getId()).getTotalAvg());
                }
            });
        } else {
            String alias = subject.getSubjectAlias();
            if ("chinese".equals(alias)) {
                curr.forEach(item -> {
                    item.setScore(item.getChineseAvg());
                    if (map.containsKey(item.getId())) {
                        item.setScoreLast(map.get(item.getId()).getChineseAvg());
                    }
                });
            } else if ("math".equals(alias)) {
                curr.forEach(item -> {
                    item.setScore(item.getMathAvg());
                    if (map.containsKey(item.getId())) {
                        item.setScoreLast(map.get(item.getId()).getMathAvg());
                    }
                });
            } else if ("english".equals(alias)) {
                curr.forEach(item -> {
                    item.setScore(item.getEnglishAvg());
                    if (map.containsKey(item.getId())) {
                        item.setScoreLast(map.get(item.getId()).getEnglishAvg());
                    }
                });
            } else if ("physics".equals(alias)) {
                curr.forEach(item -> {
                    item.setScore(item.getPhysicsAvg());
                    if (map.containsKey(item.getId())) {
                        item.setScoreLast(map.get(item.getId()).getPhysicsAvg());
                    }
                });
            } else if ("history".equals(alias)) {
                curr.forEach(item -> {
                    item.setScore(item.getHistoryAvg());
                    if (map.containsKey(item.getId())) {
                        item.setScoreLast(map.get(item.getId()).getHistoryAvg());
                    }
                });
            } else if ("geography".equals(alias)) {
                curr.forEach(item -> {
                    item.setScore(item.getGeographyAvg());
                    if (map.containsKey(item.getId())) {
                        item.setScoreLast(map.get(item.getId()).getGeographyAvg());
                    }
                });
            } else if ("politics".equals(alias)) {
                curr.forEach(item -> {
                    item.setScore(item.getPoliticsAvg());
                    if (map.containsKey(item.getId())) {
                        item.setScoreLast(map.get(item.getId()).getPoliticsAvg());
                    }
                });
            } else if ("biology".equals(alias)) {
                curr.forEach(item -> {
                    item.setScore(item.getBiologyAvg());
                    if (map.containsKey(item.getId())) {
                        item.setScoreLast(map.get(item.getId()).getBiologyAvg());
                    }
                });
            } else {
                curr.forEach(item -> {
                    item.setScore(item.getChemistryAvg());
                    if (map.containsKey(item.getId())) {
                        item.setScoreLast(map.get(item.getId()).getChemistryAvg());
                    }
                });
            }
        }
        curr.forEach(item -> {
            item.setRatio(BigDecimal.ZERO);
            item.setPm((int) curr.stream().filter(o -> o.getScore().compareTo(item.getScore()) > 0).count() + 1);
            if (item.getScoreLast() != null) {
                item.setPmLast((int) curr.stream().filter(o -> (o.getScoreLast() == null ? BigDecimal.ZERO : o.getScoreLast()).compareTo(item.getScoreLast()) > 0).count() + 1);
            }
        });
    }

    @Override
    public List<ExamGradeAnalyseClazzSituationDto> scoreListStation(Long examId, Long subjectId) {
        List<ExamGradeAnalyseClazzSituationDto> curr = this.getBaseMapper().gradeAnalyseClazz(examId);
        if (CollectionUtils.isNotEmpty(curr)) {
            List<ExamGradeAnalyseClazzSituationDto> pre = new ArrayList<>();
            Subject subject = this.subjectService.getById(subjectId);
            Exam exam = this.examMapper.selectById(examId);
            List<Exam> preExamList = this.examMapper.selectList(Wrappers.<Exam>lambdaQuery()
                    .select(Exam::getId, Exam::getExamEndDate)
                    .eq(Exam::getGradeId, exam.getGradeId())
                    .eq(Exam::getSchoolyardId, exam.getSchoolyardId())
                    .eq(Exam::getAcademicYearSemesterId, exam.getAcademicYearSemesterId())
                    .lt(Exam::getExamEndDate, exam.getExamEndDate())
                    .orderByDesc(Exam::getExamEndDate));
            if (CollectionUtils.isNotEmpty(preExamList)) {
                pre = this.getBaseMapper().gradeAnalyseClazz(preExamList.get(0).getId());
            }
            this.getScore(subject, curr, pre);
            // 目标比例
            ExamGoalService examGoalService = SpringUtils.getBean("examGoalServiceImpl");
            List<ExamGoalDto> examGoalDtoList = examGoalService.getDefault(examId, null);
            if (CollectionUtils.isNotEmpty(examGoalDtoList)) {
                List<ExamGoalClazzTotalDto> clazzGoalTotal = this.examGoalMapper.getClazzGoalTotal(null, examGoalDtoList);
                for (ExamGradeAnalyseClazzSituationDto item : curr) {
                    int x = 0, y = 0;
                    for (ExamGoalClazzTotalDto item1 : clazzGoalTotal) {
                        if (item1.getClazzId().equals(item.getId())) {
                            x += item1.getShouldMeetingCnt();
                            y += item1.getClazzMeetingCnt();
                        }
                    }
                    item.setRatio(BigDecimal.valueOf(y).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(x), 1, RoundingMode.HALF_UP));
                }
            }
        }
        return curr;
    }

    @Override
    public Map<String, Object> gradeAnalyseSubject(Long examId, String clazzNature, String subjectName, Integer highest, Integer lowest, Integer tolerance) {
        if (this.examMapper.selectById(examId) == null) {
            throw new ApiCode.ApiException(-1, "考试ID无效");
        }
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> finalRes = new LinkedList<>();
        List<String> regionDifferList = TwxUtils.regionDiffer(highest, lowest, tolerance);
        List<Map<String, String>> columns = new ArrayList<>();
        Map<String, String> column1 = new HashMap<>();
        column1.put("name", "班级");
        columns.add(column1);
        Map<String, String> column2 = new HashMap<>();
        column2.put("name", "总人数");
        columns.add(column2);
        Map<String, String> column3 = new HashMap<>();
        column3.put("name", "参考数");
        columns.add(column3);
        Map<String, String> column4 = new HashMap<>();
        column4.put("name", "均分");
        columns.add(column4);
        Map<String, String> column5 = new HashMap<>();
        column5.put("name", "名次");
        columns.add(column5);
        for (String str : regionDifferList) {
            Map<String, String> m = new HashMap<>();
            if (str.startsWith("<>")) {
                int indexComma = str.indexOf("-");
                m.put("name", str.substring(2, indexComma) + "-" + str.substring(indexComma + 1));
            } else {
                m.put("name", str);
            }
            columns.add(m);
        }
        map.put("columns", columns);
        Map<String, Object> mapTotal = new HashMap<String, Object>(){
            {
                put("班级", "合计");
                put("均分", 0);
                put("名次", "");
                put("班级性质", "");
            }
        };
        for (String str : regionDifferList) {
            Map<String, Object> paramMap = new HashMap<>();
            List<Map<String, Object>> currRes = new LinkedList<>();
            paramMap.put("subjectName", subjectName);
            paramMap.put("clazzNature", clazzNature);
            paramMap.put("examId", examId);
            String columnName = "";
            if (str.startsWith("<>")) {
                int indexComma = str.indexOf("-");
                paramMap.put("scoreL", str.substring(2, indexComma));
                paramMap.put("scoreR", str.substring(indexComma + 1));
                columnName = paramMap.get("scoreL").toString() + "-" + paramMap.get("scoreR");
                paramMap.put("columnName", columnName);
                currRes = this.getBaseMapper().getGradeAnalyseBT(paramMap);
            } else if (str.startsWith(">=")) {
                paramMap.put("score", str.substring(2));
                columnName = str;
                paramMap.put("columnName", columnName);
                currRes = this.getBaseMapper().getGradeAnalyseAE(paramMap);
            } else if (str.startsWith("<=")) {
                paramMap.put("score", str.substring(2));
                columnName = str;
                paramMap.put("columnName", columnName);
                currRes = this.getBaseMapper().getGradeAnalyseBE(paramMap);
            }

            final String obj = columnName;
            mapTotal.put(columnName, currRes.stream().mapToInt(item -> Integer.parseInt(item.get(obj).toString())).sum());
            if (paramMap.getOrDefault("score", "-100").toString().equals(String.valueOf(lowest))) {
                finalRes.addAll(currRes);
                continue;
            }
            for (int i = finalRes.size() - 1; i >= 0; i--) {
                finalRes.get(i).put(columnName, currRes.get(i).get(columnName));
            }
        }

        // 处理排名
        List<BigDecimal> list = new LinkedList<>();
        finalRes.forEach(item -> list.add(new BigDecimal(item.get("均分").toString())));
        list.sort(Comparator.reverseOrder());

        int sumTotal = 0, sumJoin = 0;
        for (Map<String, Object> stringObjectMap : finalRes) {
            stringObjectMap.put("名次", TwxUtils.binarySearchEnhance1(list, new BigDecimal(stringObjectMap.get("均分").toString())));
            sumTotal += Integer.parseInt(stringObjectMap.get("总人数").toString());
            sumJoin += Integer.parseInt(stringObjectMap.get("参考数").toString());
        }
        mapTotal.put("总人数", sumTotal);
        mapTotal.put("参考数", sumJoin);
        if ("总分".equals(subjectName)) {
            finalRes.add(mapTotal);
        }
        map.put("data", finalRes);
        return map;
    }

    @Override
    public IPage<Map<String, Object>> queryExamResultByPage(IPage<ExamResult> page, QueryWrapper<ExamResult> wrapper) {
        return this.getBaseMapper().queryExamResultByPage(page, wrapper);
    }

    @Override
    @SuppressWarnings("unchecked")
    public IPage<ExamPagerDto> queryExamResultPager(IPage<ExamPagerDto> page, Map<String, Object> map, Long examId) {
        // 成绩单分页查询结果
        IPage<ExamPagerDto> iPage = this.getBaseMapper().queryExamResultPager(page, map);
        // 当前考试全年级所有学生成绩
        List<ExamResult> examResultsGrade = this.examResultMapper.selectList(Wrappers.<ExamResult>query().lambda().eq(ExamResult::getExamId, examId));
        if (CollectionUtils.isNotEmpty(examResultsGrade)) {
            List<ExamPagerDto> examPagerDtoList = iPage.getRecords();
            // 前一次考试全年级分数
            List<ExamPreDto> examPreDtoList = this.getBaseMapper().getExamPrePager(examId);
            Set<Long> idsSet;
            Map<Long, int[]> preMap = null;
            if (CollectionUtils.isNotEmpty(examPreDtoList)) {
                // 获取前一次考试科名
                preMap = new HashMap<>(1 << 5, 0.8f);
                idsSet = new HashSet<>();
                List<BigDecimal>[] totalScoreArr = new List[3];
                TwxUtils.fill(totalScoreArr, ArrayList::new);
                List<BigDecimal>[] totalWeightedScoreArr = new List[3];
                TwxUtils.fill(totalWeightedScoreArr, ArrayList::new);
                for (ExamPreDto item : examPreDtoList) {
                    idsSet.add(item.getStudentId());
                    if (ClazzNatureEnum.OTHER.equals(item.getNature())) {
                        totalScoreArr[0].add(item.getTotalScore());
                        totalWeightedScoreArr[0].add(item.getTotalWeightedScore());
                    } else if (ClazzNatureEnum.SCIENCE.equals(item.getNature())){
                        totalScoreArr[1].add(item.getTotalScore());
                        totalWeightedScoreArr[1].add(item.getTotalWeightedScore());
                    } else {
                        totalScoreArr[2].add(item.getTotalScore());
                        totalWeightedScoreArr[2].add(item.getTotalWeightedScore());
                    }
                }
                TwxUtils.arrSort(totalScoreArr, Comparator.reverseOrder());
                TwxUtils.arrSort(totalWeightedScoreArr, Comparator.reverseOrder());
                for (ExamPagerDto item : examPagerDtoList) {
                    if (idsSet.contains(item.getStudentId())) {
                        int[] tmp = preMap.computeIfAbsent(item.getStudentId(), o -> new int[2]);
                        if (ClazzNatureEnum.OTHER.equals(item.getNature())) {
                            tmp[0] = TwxUtils.binarySearchEnhance1(totalScoreArr[0], item.getTotalScore());
                            tmp[1] = TwxUtils.binarySearchEnhance1(totalWeightedScoreArr[0], item.getTotalWeightedScore());
                        } else if (ClazzNatureEnum.SCIENCE.equals(item.getNature())){
                            tmp[0] = TwxUtils.binarySearchEnhance1(totalScoreArr[1], item.getTotalScore());
                            tmp[1] = TwxUtils.binarySearchEnhance1(totalWeightedScoreArr[1], item.getTotalWeightedScore());
                        } else {
                            tmp[0] = TwxUtils.binarySearchEnhance1(totalScoreArr[2], item.getTotalScore());
                            tmp[1] = TwxUtils.binarySearchEnhance1(totalWeightedScoreArr[2], item.getTotalWeightedScore());
                        }
                    }
                }
            }
            // 各班学生成绩
            Map<Long, List<BigDecimal>[]> examResultsClazz = new HashMap<>();
            // 分科学生成绩
            Map<ClazzNatureEnum, List<BigDecimal>[]> examResultsNature = new HashMap<>();
            examResultsGrade.forEach(item -> {
                List<BigDecimal>[] arr = examResultsClazz.computeIfAbsent(item.getClazzId(), o -> new List[15]);
                if (arr[0] == null) {
                    TwxUtils.fill(arr, ArrayList::new);
                }
                arr[0].add(item.getChineseScore());
                arr[1].add(item.getMathScore());
                arr[2].add(item.getEnglishScore());
                arr[3].add(item.getPhysicsScore());
                arr[4].add(item.getChemistryScore());
                arr[5].add(item.getChemistryWeightedScore());
                arr[6].add(item.getBiologyScore());
                arr[7].add(item.getBiologyWeightedScore());
                arr[8].add(item.getHistoryScore());
                arr[9].add(item.getPoliticsScore());
                arr[10].add(item.getPoliticsWeightedScore());
                arr[11].add(item.getGeographyScore());
                arr[12].add(item.getGeographyWeightedScore());
                arr[13].add(item.getTotalScore());
                arr[14].add(item.getTotalWeightedScore());
                List<BigDecimal>[] arrNature = examResultsNature.computeIfAbsent(item.getClazz().getClazzNature(), o -> new List[2]);
                if (arrNature[0] == null) {
                    TwxUtils.fill(arrNature, ArrayList::new);
                }
                arrNature[0].add(item.getTotalScore());
                arrNature[1].add(item.getTotalWeightedScore());
            });
            examResultsNature.values().forEach(i -> TwxUtils.arrSort(i, Comparator.reverseOrder()));
            examResultsClazz.values().forEach(i -> TwxUtils.arrSort(i, Comparator.reverseOrder()));
            // 处理排名
            for (ExamPagerDto examPagerDto : examPagerDtoList) {
                List<BigDecimal>[] m = examResultsClazz.get(examPagerDto.getClazzId());
                examPagerDto.setChineseRank(TwxUtils.binarySearchEnhance1(m[0], examPagerDto.getChineseScore()));
                examPagerDto.setMathRank(TwxUtils.binarySearchEnhance1(m[1], examPagerDto.getMathScore()));
                examPagerDto.setEnglishRank(TwxUtils.binarySearchEnhance1(m[2], examPagerDto.getEnglishScore()));
                if (examPagerDto.getPhysicsScore().compareTo(BigDecimal.ZERO) != 0) {
                    examPagerDto.setPhysicsRank(TwxUtils.binarySearchEnhance1(m[3], examPagerDto.getPhysicsScore()));
                }
                if (examPagerDto.getChemistryScore().compareTo(BigDecimal.ZERO) != 0) {
                    examPagerDto.setChemistryRank(TwxUtils.binarySearchEnhance1(m[4],  examPagerDto.getChemistryScore()));
                }
                if (examPagerDto.getChemistryWeightedScore().compareTo(BigDecimal.ZERO) != 0) {
                    examPagerDto.setChemistryWeightedRank(TwxUtils.binarySearchEnhance1(m[5], examPagerDto.getChemistryWeightedScore()));
                }
                if (examPagerDto.getBiologyScore().compareTo(BigDecimal.ZERO) != 0) {
                    examPagerDto.setBiologyRank(TwxUtils.binarySearchEnhance1(m[6], examPagerDto.getBiologyScore()));
                }
                if (examPagerDto.getBiologyWeightedScore().compareTo(BigDecimal.ZERO) != 0) {
                    examPagerDto.setBiologyWeightedRank(TwxUtils.binarySearchEnhance1(m[7], examPagerDto.getBiologyWeightedScore()));
                }
                if (examPagerDto.getHistoryScore().compareTo(BigDecimal.ZERO) != 0) {
                    examPagerDto.setHistoryRank(TwxUtils.binarySearchEnhance1(m[8], examPagerDto.getHistoryScore()));
                }
                if (examPagerDto.getPoliticsScore().compareTo(BigDecimal.ZERO) != 0) {
                    examPagerDto.setPoliticsRank(TwxUtils.binarySearchEnhance1(m[9], examPagerDto.getPoliticsScore()));
                }
                if (examPagerDto.getPoliticsWeightedScore().compareTo(BigDecimal.ZERO) != 0) {
                    examPagerDto.setPoliticsWeightedRank(TwxUtils.binarySearchEnhance1(m[10], examPagerDto.getPoliticsWeightedScore()));
                }
                if (examPagerDto.getGeographyScore().compareTo(BigDecimal.ZERO) != 0) {
                    examPagerDto.setGeographyRank(TwxUtils.binarySearchEnhance1(m[11], examPagerDto.getGeographyScore()));
                }
                if (examPagerDto.getGeographyWeightedScore().compareTo(BigDecimal.ZERO) != 0) {
                    examPagerDto.setGeographyWeightedRank(TwxUtils.binarySearchEnhance1(m[12], examPagerDto.getGeographyWeightedScore()));
                }
                examPagerDto.setTotalRank(TwxUtils.binarySearchEnhance1(m[13],  examPagerDto.getTotalScore()));
                examPagerDto.setTotalWeightedRank(TwxUtils.binarySearchEnhance1(m[14], examPagerDto.getTotalWeightedScore()));
                examPagerDto.setTotalRankNj(TwxUtils.binarySearchEnhance1(examResultsNature.get(examPagerDto.getNature())[0], examPagerDto.getTotalScore()));
                examPagerDto.setTotalWeightedRankNj(TwxUtils.binarySearchEnhance1(examResultsNature.get(examPagerDto.getNature())[1], examPagerDto.getTotalWeightedScore()));
                if (preMap != null && preMap.containsKey(examPagerDto.getStudentId())) {
                    examPagerDto.setTotalRankPre(preMap.get(examPagerDto.getStudentId())[0]);
                    examPagerDto.setTotalWeightRankPre(preMap.get(examPagerDto.getStudentId())[1]);
                }
            }
        }
        return iPage;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> gradeAnalyseClazz(Long examId) {
        // 当前考试所有的班级性质
        Map<String, Object> map = new HashMap<>();
        List<String> natureList = this.examResultMapper.getAllNatureByExam(examId);
        map.put("natureList", natureList);
        List<ExamGradeAnalyseClazzSituationDto> res = this.getBaseMapper().gradeAnalyseClazz(examId);
        if (CollectionUtils.isNotEmpty(res)) {
            List<BigDecimal>[] arr = new List[15];
            TwxUtils.fill(arr, ArrayList::new);
            res.forEach(item -> {
                arr[0].add(item.getChineseAvg());
                arr[1].add(item.getMathAvg());
                arr[2].add(item.getEnglishAvg());
                arr[3].add(item.getPhysicsAvg());
                arr[4].add(item.getChemistryAvg());
                arr[5].add(item.getChemistryWeightedAvg());
                arr[6].add(item.getBiologyAvg());
                arr[7].add(item.getBiologyWeightedAvg());
                arr[8].add(item.getHistoryAvg());
                arr[9].add(item.getPoliticsAvg());
                arr[10].add(item.getPoliticsWeightedAvg());
                arr[11].add(item.getGeographyAvg());
                arr[12].add(item.getGeographyWeightedAvg());
                arr[13].add(item.getTotalAvg());
                arr[14].add(item.getTotalWeightedAvg());
            });
            for (List<BigDecimal> bigDecimals : arr) {
                bigDecimals.sort(Comparator.reverseOrder());
            }
            for (ExamGradeAnalyseClazzSituationDto e : res) {
                e.setChineseRank(TwxUtils.binarySearchEnhance1(arr[0], e.getChineseAvg()));
                e.setMathRank(TwxUtils.binarySearchEnhance1(arr[1], e.getMathAvg()));
                e.setEnglishRank(TwxUtils.binarySearchEnhance1(arr[2], e.getEnglishAvg()));
                e.setPhysicsRank(TwxUtils.binarySearchEnhance1(arr[3], e.getPhysicsAvg()));
                e.setChemistryRank(TwxUtils.binarySearchEnhance1(arr[4], e.getChemistryAvg()));
                e.setChemistryWeightedRank(TwxUtils.binarySearchEnhance1(arr[5], e.getChemistryWeightedAvg()));
                e.setBiologyRank(TwxUtils.binarySearchEnhance1(arr[6], e.getBiologyAvg()));
                e.setBiologyWeightedRank(TwxUtils.binarySearchEnhance1(arr[7], e.getBiologyWeightedAvg()));
                e.setHistoryRank(TwxUtils.binarySearchEnhance1(arr[8], e.getHistoryAvg()));
                e.setPoliticsRank(TwxUtils.binarySearchEnhance1(arr[9], e.getPoliticsAvg()));
                e.setPoliticsWeightedRank(TwxUtils.binarySearchEnhance1(arr[10], e.getPoliticsWeightedAvg()));
                e.setGeographyRank(TwxUtils.binarySearchEnhance1(arr[11], e.getGeographyAvg()));
                e.setGeographyWeightedRank(TwxUtils.binarySearchEnhance1(arr[12], e.getGeographyWeightedAvg()));
                e.setTotalRank(TwxUtils.binarySearchEnhance1(arr[13], e.getTotalAvg()));
                e.setTotalWeightedRank(TwxUtils.binarySearchEnhance1(arr[14], e.getTotalWeightedAvg()));
            }
        }
        map.put("res", res);
        return map;
    }

    @Override
    public Map<String, Object> printExam(Long studentId, Long examId, Long academicYearSemesterId) {
        Map<String, Object> map = new HashMap<>();
        map.put("studentInfo", new ArrayList<>());
        map.put("chatInfo", new HashMap<>());
        if (studentId != null) {
            List<ExamClazzAnalyseStudentDto> studentConditionList = this.getExamCard(studentId, examId, academicYearSemesterId);
            if (studentConditionList != null) {
                map.put("studentInfo", studentConditionList);
                try {
                    map.put("chatInfo", this.getChartInfo1(studentConditionList.get(0).getClazz(), studentConditionList));
                } catch (Exception e) {
                    throw new ApiCode.ApiException(-1, "获取图表失败");
                }
            }
        }
        return map;
    }

    private Map<Integer, List<ExamChartDto>> getChartInfo1(Clazz clazz, List<ExamClazzAnalyseStudentDto> studentConditionList) throws Exception {
        Map<Integer, List<ExamChartDto>> chatInfo = new HashMap<>();
        if (CollectionUtils.isNotEmpty(studentConditionList)) {
            for (int i = 1; i <= 7; i++) {
                chatInfo.put(i, new ArrayList<>());
            }
            String fourthSubject = null;
            String other1 = null;
            String other2 = null;
            if (!ClazzNatureEnum.OTHER.equals(clazz.getClazzNature())) {
                long history = studentConditionList.stream().filter(o -> o.getHistoryScore().compareTo(BigDecimal.ZERO) > 0).count();
                long chemistry = studentConditionList.stream().filter(o -> o.getChemistryScore().compareTo(BigDecimal.ZERO) > 0).count();
                long biology = studentConditionList.stream().filter(o -> o.getBiologyScore().compareTo(BigDecimal.ZERO) > 0).count();
                long politics = studentConditionList.stream().filter(o -> o.getPoliticsScore().compareTo(BigDecimal.ZERO) > 0).count();
                long geography = studentConditionList.stream().filter(o -> o.getGeographyScore().compareTo(BigDecimal.ZERO) > 0).count();
                fourthSubject = history > 0 ? "历史" : "物理";
                if (chemistry > 0 && biology > 0) {
                    other1 = "化学"; other2 = "生物";
                } else if (chemistry > 0 && politics > 0) {
                    other1 = "化学"; other2 = "政治";
                } else if (chemistry > 0 && geography > 0) {
                    other1 = "化学"; other2 = "地理";
                } else if (biology > 0 && politics > 0) {
                    other1 = "生物"; other2 = "政治";
                } else if (biology > 0 && geography > 0) {
                    other1 = "生物"; other2 = "地理";
                } else if (politics > 0 && geography > 0) {
                    other1 = "政治"; other2 = "地理";
                }

            }
            for (ExamClazzAnalyseStudentDto obj : studentConditionList) {
                // 语文
                Map<String, Object> ywMap = new HashMap<>();
                ywMap.put(obj.getName(), obj.getChineseRankBj());
                if (chatInfo.get(1).size() == 0) {
                    ExamChartDto yw = new ExamChartDto();
                    yw.setName("语文");
                    yw.setData(new ArrayList<>());
                    yw.getData().add(ywMap);
                    chatInfo.get(1).add(yw);
                } else {
                    chatInfo.get(1).get(0).getData().add(ywMap);
                }
                // 数学
                Map<String, Object> ssMap = new HashMap<>();
                ssMap.put(obj.getName(), obj.getMathRankBj());
                if (chatInfo.get(2).size() == 0) {
                    ExamChartDto yw = new ExamChartDto();
                    yw.setName("数学");
                    yw.setData(new ArrayList<>());
                    yw.getData().add(ssMap);
                    chatInfo.get(2).add(yw);
                } else {
                    chatInfo.get(2).get(0).getData().add(ssMap);
                }
                // 英语
                Map<String, Object> map3 = new HashMap<>();
                map3.put(obj.getName(), obj.getEnglishRankBj());
                if (chatInfo.get(3).size() == 0) {
                    ExamChartDto yw = new ExamChartDto();
                    yw.setName("英语");
                    yw.setData(new ArrayList<>());
                    yw.getData().add(map3);
                    chatInfo.get(3).add(yw);
                } else {
                    chatInfo.get(3).get(0).getData().add(map3);
                }
                // 总分 总赋分科名
                Map<String, Object> map4 = new HashMap<>();
                Map<String, Object> map5 = new HashMap<>();
                map4.put(obj.getName(), obj.getTotalRankNj());
                map5.put(obj.getName(), obj.getTotalWeightedRankNj());
                if (chatInfo.get(7).size() == 0) {
                    ExamChartDto yw = new ExamChartDto();
                    yw.setName("总分");
                    yw.setData(new ArrayList<>());
                    yw.getData().add(map4);
                    chatInfo.get(7).add(yw);
                    ExamChartDto yw1 = new ExamChartDto();
                    yw1.setName("总赋分");
                    yw1.setData(new ArrayList<>());
                    yw1.getData().add(map5);
                    chatInfo.get(7).add(yw1);
                } else {
                    chatInfo.get(7).get(0).getData().add(map4);
                    chatInfo.get(7).get(1).getData().add(map5);
                }
                if (StringUtils.isNullOrEmpty(fourthSubject)) {
                    Map<String, Object> mapwl = new HashMap<>();
                    Map<String, Object> mapls = new HashMap<>();
                    mapls.put(obj.getName(), obj.getHistoryRankBj());
                    mapwl.put(obj.getName(), obj.getPhysicsRankBj());
                    if (chatInfo.get(4).size() == 0) {
                        ExamChartDto yw = new ExamChartDto();
                        yw.setName("物理");
                        yw.setData(new ArrayList<>());
                        yw.getData().add(mapwl);
                        chatInfo.get(4).add(yw);
                        ExamChartDto yw1 = new ExamChartDto();
                        yw1.setName("历史");
                        yw1.setData(new ArrayList<>());
                        yw1.getData().add(mapls);
                        chatInfo.get(4).add(yw1);
                    } else {
                        chatInfo.get(4).get(0).getData().add(mapwl);
                        chatInfo.get(4).get(1).getData().add(mapls);
                    }
                } else {
                    // 四总和总赋分
                    Map<String, Object> map6 = new HashMap<>();
                    Map<String, Object> map7 = new HashMap<>();
                    map6.put(obj.getName(), obj.getFourTotalRankBj());
                    map7.put(obj.getName(), obj.getTotalWeightedRankBj());
                    if (chatInfo.get(6).size() == 0) {
                        ExamChartDto yw = new ExamChartDto();
                        yw.setName("四总");
                        yw.setData(new ArrayList<>());
                        yw.getData().add(map6);
                        chatInfo.get(6).add(yw);
                        ExamChartDto yw1 = new ExamChartDto();
                        yw1.setName("总赋分");
                        yw1.setData(new ArrayList<>());
                        yw1.getData().add(map7);
                        chatInfo.get(6).add(yw1);
                    } else {
                        chatInfo.get(6).get(0).getData().add(map6);
                        chatInfo.get(6).get(1).getData().add(map7);
                    }
                    // 第四门科目
                    Map<String, Object> map8 = new HashMap<>();
                    if ("历史".equals(fourthSubject)) {
                        map8.put(obj.getName(), obj.getHistoryRankBj());
                    } else {
                        map8.put(obj.getName(), obj.getPhysicsRankBj());
                    }
                    if (chatInfo.get(4).size() == 0) {
                        ExamChartDto yw = new ExamChartDto();
                        yw.setName(fourthSubject);
                        yw.setData(new ArrayList<>());
                        yw.getData().add(map8);
                        chatInfo.get(4).add(yw);
                    } else {
                        chatInfo.get(4).get(0).getData().add(map8);
                    }
                }
                if (!StringUtils.isNullOrEmpty(other1)) {
                    // 其余两门科目
                    Map<String, Object> map9 = new HashMap<>();
                    Map<String, Object> map10 = new HashMap<>();
                    if ("化学".equals(other1) && "生物".equals(other2)) {
                        map9.put(obj.getName(), obj.getChemistryScore());
                        map10.put(obj.getName(), obj.getBiologyScore());
                    } else if ("化学".equals(other1) && "政治".equals(other2)) {
                        map9.put(obj.getName(), obj.getChemistryScore());
                        map10.put(obj.getName(), obj.getPoliticsScore());
                    } else if ("化学".equals(other1)) {
                        map9.put(obj.getName(), obj.getChemistryScore());
                        map10.put(obj.getName(), obj.getGeographyScore());
                    } else if ("生物".equals(other1) && "政治".equals(other2)) {
                        map9.put(obj.getName(), obj.getBiologyScore());
                        map10.put(obj.getName(), obj.getPoliticsScore());
                    } else if ("生物".equals(other1)) {
                        map9.put(obj.getName(), obj.getBiologyScore());
                        map10.put(obj.getName(), obj.getGeographyScore());
                    } else {
                        map9.put(obj.getName(), obj.getPoliticsScore());
                        map10.put(obj.getName(), obj.getGeographyScore());
                    }
                    if (chatInfo.get(5).size() == 0) {
                        ExamChartDto yw = new ExamChartDto();
                        yw.setName(other1);
                        yw.setData(new ArrayList<>());
                        yw.getData().add(map9);
                        chatInfo.get(5).add(yw);
                        ExamChartDto yw1 = new ExamChartDto();
                        yw1.setName(other2);
                        yw1.setData(new ArrayList<>());
                        yw1.getData().add(map10);
                        chatInfo.get(5).add(yw1);
                    } else {
                        chatInfo.get(5).get(0).getData().add(map9);
                        chatInfo.get(5).get(1).getData().add(map10);
                    }
                }
            }
        }
        return chatInfo;
    }

    @Override
    public List<ExamClazzAnalyseStudentDto> getExamCard(Long studentId, Long examId, Long academicYearSemesterId) {
        if (studentId != null) {
            List<ExamResult> examResultList = this.examResultMapper.getExamCard(studentId, examId, academicYearSemesterId);
            if (CollectionUtils.isNotEmpty(examResultList)) {
                List<ExamClazzAnalyseStudentDto> res = new ArrayList<>();
                examResultList.forEach(item -> {
                    ExamClazzAnalyseStudentDto examClazzAnalyseStudentDto = new ExamClazzAnalyseStudentDto();
                    examClazzAnalyseStudentDto.setId(item.getId());
                    examClazzAnalyseStudentDto.setName(item.getExam().getName());
                    examClazzAnalyseStudentDto.setStudent(item.getStudent());
                    examClazzAnalyseStudentDto.setClazz(item.getClazz());
                    examClazzAnalyseStudentDto.setChineseScore(item.getChineseScore());
                    examClazzAnalyseStudentDto.setMathScore(item.getMathScore());
                    examClazzAnalyseStudentDto.setEnglishScore(item.getEnglishScore());
                    examClazzAnalyseStudentDto.setPhysicsScore(item.getPhysicsScore());
                    examClazzAnalyseStudentDto.setHistoryScore(item.getHistoryScore());
                    examClazzAnalyseStudentDto.setBiologyScore(item.getBiologyScore());
                    examClazzAnalyseStudentDto.setBiologyWeightedScore(item.getBiologyWeightedScore());
                    examClazzAnalyseStudentDto.setChemistryScore(item.getChemistryScore());
                    examClazzAnalyseStudentDto.setChemistryWeightedScore(item.getChemistryWeightedScore());
                    examClazzAnalyseStudentDto.setGeographyScore(item.getGeographyScore());
                    examClazzAnalyseStudentDto.setGeographyWeightedScore(item.getGeographyWeightedScore());
                    examClazzAnalyseStudentDto.setPoliticsScore(item.getPoliticsScore());
                    examClazzAnalyseStudentDto.setPoliticsWeightedScore(item.getPoliticsWeightedScore());
                    ExamResultOtherDto examResultOtherDto = JSONObject.parseObject(item.getOther(), ExamResultOtherDto.class);
                    try {
                        BeanUtils.copyProperties(examClazzAnalyseStudentDto, examResultOtherDto);
                    } catch (Exception e1) {
                        throw new ApiCode.ApiException(-1, e1.getMessage());
                    }
                    res.add(examClazzAnalyseStudentDto);
                });
                return res;
            }
        }
        return new ArrayList<>();
    }

    private Map<Integer, List<ExamChartDto>> getChartInfo(String s, Clazz clazz, List<ExamClazzAnalyseClazzAndStudentDto> studentConditionList) throws Exception {
        Map<Integer, List<ExamChartDto>> chatInfo = new HashMap<>();
        if (CollectionUtils.isNotEmpty(studentConditionList) && !StringUtils.isNullOrEmpty(s)) {
            for (int i = 1; i <= 7; i++) {
                chatInfo.put(i, new ArrayList<>());
            }
            String fourthSubject = null;
            if (!ClazzNatureEnum.OTHER.equals(clazz.getClazzNature())) {
                fourthSubject = ClazzNatureEnum.SCIENCE.equals(clazz.getClazzNature()) ? "物理" : "历史";
            }
            for (ExamClazzAnalyseClazzAndStudentDto obj : studentConditionList) {
                // 语文
                Map<String, Object> ywMap = new HashMap<>();
                ywMap.put(obj.getName(), obj.getChineseScore());
                if (chatInfo.get(1).size() == 0) {
                    ExamChartDto yw = new ExamChartDto();
                    yw.setName("语文");
                    yw.setData(new ArrayList<>());
                    yw.getData().add(ywMap);
                    chatInfo.get(1).add(yw);
                } else {
                    chatInfo.get(1).get(0).getData().add(ywMap);
                }
                // 数学
                Map<String, Object> ssMap = new HashMap<>();
                ssMap.put(obj.getName(), obj.getMathScore());
                if (chatInfo.get(2).size() == 0) {
                    ExamChartDto yw = new ExamChartDto();
                    yw.setName("数学");
                    yw.setData(new ArrayList<>());
                    yw.getData().add(ssMap);
                    chatInfo.get(2).add(yw);
                } else {
                    chatInfo.get(2).get(0).getData().add(ssMap);
                }
                // 英语
                Map<String, Object> map3 = new HashMap<>();
                map3.put(obj.getName(), obj.getEnglishScore());
                if (chatInfo.get(3).size() == 0) {
                    ExamChartDto yw = new ExamChartDto();
                    yw.setName("英语");
                    yw.setData(new ArrayList<>());
                    yw.getData().add(map3);
                    chatInfo.get(3).add(yw);
                } else {
                    chatInfo.get(3).get(0).getData().add(map3);
                }
                // 总分 总赋分科名
                Map<String, Object> map4 = new HashMap<>();
                Map<String, Object> map5 = new HashMap<>();
                map4.put(obj.getName(), obj.getTotalScore());
                map5.put(obj.getName(), obj.getTotalWeightedScore());
                if (chatInfo.get(7).size() == 0) {
                    ExamChartDto yw = new ExamChartDto();
                    yw.setName("总分(原始)");
                    yw.setData(new ArrayList<>());
                    yw.getData().add(map4);
                    chatInfo.get(7).add(yw);
                    ExamChartDto yw1 = new ExamChartDto();
                    yw1.setName("总分(赋分)");
                    yw1.setData(new ArrayList<>());
                    yw1.getData().add(map5);
                    chatInfo.get(7).add(yw1);
                } else {
                    chatInfo.get(7).get(0).getData().add(map4);
                    chatInfo.get(7).get(1).getData().add(map5);
                }
                if (StringUtils.isNullOrEmpty(fourthSubject)) {
                    Map<String, Object> mapwl = new HashMap<>();
                    Map<String, Object> mapls = new HashMap<>();
                    mapls.put(obj.getName(), obj.getHistoryScore());
                    mapwl.put(obj.getName(), obj.getPhysicsScore());
                    if (chatInfo.get(4).size() == 0) {
                        ExamChartDto yw = new ExamChartDto();
                        yw.setName("物理");
                        yw.setData(new ArrayList<>());
                        yw.getData().add(mapwl);
                        chatInfo.get(4).add(yw);
                        ExamChartDto yw1 = new ExamChartDto();
                        yw1.setName("历史");
                        yw1.setData(new ArrayList<>());
                        yw1.getData().add(mapls);
                        chatInfo.get(4).add(yw1);
                    } else {
                        chatInfo.get(4).get(0).getData().add(mapwl);
                        chatInfo.get(4).get(1).getData().add(mapls);
                    }
                } else {
                    // 四总和总赋分
                    Map<String, Object> map6 = new HashMap<>();
                    Map<String, Object> map7 = new HashMap<>();
                    map6.put(obj.getName(), obj.getFourTotal());
                    map7.put(obj.getName(), obj.getTotalWeightedScore());
                    if (chatInfo.get(6).size() == 0) {
                        ExamChartDto yw = new ExamChartDto();
                        yw.setName("四总");
                        yw.setData(new ArrayList<>());
                        yw.getData().add(map6);
                        chatInfo.get(6).add(yw);
                        ExamChartDto yw1 = new ExamChartDto();
                        yw1.setName("总分");
                        yw1.setData(new ArrayList<>());
                        yw1.getData().add(map7);
                        chatInfo.get(6).add(yw1);
                    } else {
                        chatInfo.get(6).get(0).getData().add(map6);
                        chatInfo.get(6).get(1).getData().add(map7);
                    }
                    // 第四门科目
                    Map<String, Object> map8 = new HashMap<>();
                    if ("历史".equals(fourthSubject)) {
                        map8.put(obj.getName(), obj.getHistoryScore());
                    } else {
                        map8.put(obj.getName(), obj.getPhysicsScore());
                    }
                    if (chatInfo.get(4).size() == 0) {
                        ExamChartDto yw = new ExamChartDto();
                        yw.setName(fourthSubject);
                        yw.setData(new ArrayList<>());
                        yw.getData().add(map8);
                        chatInfo.get(4).add(yw);
                    } else {
                        chatInfo.get(4).get(0).getData().add(map8);
                    }

                    // 其余两门科目
                    // 政化地生
                    Map<String, Object> map9 = new HashMap<>();
                    Map<String, Object> map10 = new HashMap<>();
                    if ("0011".equals(s)) {
                        map9.put(obj.getName(), obj.getGeographyScore());
                        map10.put(obj.getName(), obj.getBiologyScore());
                        if (chatInfo.get(5).size() == 0) {
                            ExamChartDto yw = new ExamChartDto();
                            yw.setName("地理");
                            yw.setData(new ArrayList<>());
                            yw.getData().add(map9);
                            chatInfo.get(5).add(yw);
                            ExamChartDto yw1 = new ExamChartDto();
                            yw1.setName("生物");
                            yw1.setData(new ArrayList<>());
                            yw1.getData().add(map10);
                            chatInfo.get(5).add(yw1);
                        } else {
                            chatInfo.get(5).get(0).getData().add(map9);
                            chatInfo.get(5).get(1).getData().add(map10);
                        }
                    } else if ("0101".equals(s)) {
                        map9.put(obj.getName(), obj.getBiologyScore());
                        map10.put(obj.getName(), obj.getChemistryScore());
                        if (chatInfo.get(5).size() == 0) {
                            ExamChartDto yw = new ExamChartDto();
                            yw.setName("生物");
                            yw.setData(new ArrayList<>());
                            yw.getData().add(map9);
                            chatInfo.get(5).add(yw);
                            ExamChartDto yw1 = new ExamChartDto();
                            yw1.setName("化学");
                            yw1.setData(new ArrayList<>());
                            yw1.getData().add(map10);
                            chatInfo.get(5).add(yw1);
                        } else {
                            chatInfo.get(5).get(0).getData().add(map9);
                            chatInfo.get(5).get(1).getData().add(map10);
                        }
                    } else if ("1001".equals(s)) {
                        map9.put(obj.getName(), obj.getBiologyScore());
                        map10.put(obj.getName(), obj.getPoliticsScore());
                        if (chatInfo.get(5).size() == 0) {
                            ExamChartDto yw = new ExamChartDto();
                            yw.setName("生物");
                            yw.setData(new ArrayList<>());
                            yw.getData().add(map9);
                            chatInfo.get(5).add(yw);
                            ExamChartDto yw1 = new ExamChartDto();
                            yw1.setName("政治");
                            yw1.setData(new ArrayList<>());
                            yw1.getData().add(map10);
                            chatInfo.get(5).add(yw1);
                        } else {
                            chatInfo.get(5).get(0).getData().add(map9);
                            chatInfo.get(5).get(1).getData().add(map10);
                        }
                    } else if ("0110".equals(s)) {
                        map9.put(obj.getName(), obj.getGeographyScore());
                        map10.put(obj.getName(), obj.getChemistryScore());
                        if (chatInfo.get(5).size() == 0) {
                            ExamChartDto yw = new ExamChartDto();
                            yw.setName("地理");
                            yw.setData(new ArrayList<>());
                            yw.getData().add(map9);
                            chatInfo.get(5).add(yw);
                            ExamChartDto yw1 = new ExamChartDto();
                            yw1.setName("化学");
                            yw1.setData(new ArrayList<>());
                            yw1.getData().add(map10);
                            chatInfo.get(5).add(yw1);
                        } else {
                            chatInfo.get(5).get(0).getData().add(map9);
                            chatInfo.get(5).get(1).getData().add(map10);
                        }
                    } else if ("1010".equals(s)) {
                        map9.put(obj.getName(), obj.getGeographyScore());
                        map10.put(obj.getName(), obj.getPoliticsScore());
                        if (chatInfo.get(5).size() == 0) {
                            ExamChartDto yw = new ExamChartDto();
                            yw.setName("地理");
                            yw.setData(new ArrayList<>());
                            yw.getData().add(map9);
                            chatInfo.get(5).add(yw);
                            ExamChartDto yw1 = new ExamChartDto();
                            yw1.setName("政治");
                            yw1.setData(new ArrayList<>());
                            yw1.getData().add(map10);
                            chatInfo.get(5).add(yw1);
                        } else {
                            chatInfo.get(5).get(0).getData().add(map9);
                            chatInfo.get(5).get(1).getData().add(map10);
                        }
                    } else {
                        map9.put(obj.getName(), obj.getChemistryScore());
                        map10.put(obj.getName(), obj.getPoliticsScore());
                        if (chatInfo.get(5).size() == 0) {
                            ExamChartDto yw = new ExamChartDto();
                            yw.setName("化学");
                            yw.setData(new ArrayList<>());
                            yw.getData().add(map9);
                            chatInfo.get(5).add(yw);
                            ExamChartDto yw1 = new ExamChartDto();
                            yw1.setName("政治");
                            yw1.setData(new ArrayList<>());
                            yw1.getData().add(map10);
                            chatInfo.get(5).add(yw1);
                        } else {
                            chatInfo.get(5).get(0).getData().add(map9);
                            chatInfo.get(5).get(1).getData().add(map10);
                        }
                    }
                }
            }
        }
        return chatInfo;
    }

    @Override
    public Map<String, Object> studentAnalyse(Long schoolyardId, Long academicYearSemesterId, Long clazzId, Long studentId, String type) {
        Map<String, Object> res = new HashMap<>();
        Clazz clazz = this.clazzMapper.selectById(clazzId);
        if (clazz != null) {
            if (schoolyardId == null) {
                schoolyardId = 1L;
            }
            List<ExamClazzAnalyseClazzAndStudentDto> examResultSimpleDtoList = this.examResultMapper.studentAnalyse(schoolyardId, academicYearSemesterId, clazz.getClazzNature().toString(), clazzId, studentId);
            if (CollectionUtils.isNotEmpty(examResultSimpleDtoList)) {
                ExamResultOtherDto examResultOtherDto;
                for (ExamClazzAnalyseClazzAndStudentDto item : examResultSimpleDtoList) {
                    examResultOtherDto = JSONObject.parseObject(item.getOther(), ExamResultOtherDto.class);
                    try {
                        BeanUtils.copyProperties(item, examResultOtherDto);
                    } catch (Exception e1) {
                        throw new ApiCode.ApiException(-1, e1.getMessage());
                    }
                }
                res.put("studentInfo", examResultSimpleDtoList);

                StringBuilder sb = new StringBuilder("0000");
                if (examResultSimpleDtoList.stream().anyMatch(item -> item.getPoliticsScore().compareTo(BigDecimal.ZERO) > 0)) {
                    sb.replace(0, 1, "1");
                }
                if (examResultSimpleDtoList.stream().anyMatch(item -> item.getChemistryScore().compareTo(BigDecimal.ZERO) > 0)) {
                    sb.replace(1, 2, "1");
                }
                if (examResultSimpleDtoList.stream().anyMatch(item -> item.getGeographyScore().compareTo(BigDecimal.ZERO) > 0)) {
                    sb.replace(2, 3, "1");
                }
                if (examResultSimpleDtoList.stream().anyMatch(item -> item.getBiologyScore().compareTo(BigDecimal.ZERO) > 0)) {
                    sb.replace(3, 4, "1");
                }
                String prefix = "11";
                if (ClazzNatureEnum.SCIENCE.equals(clazz.getClazzNature())) {
                    prefix = "10";
                } else if (ClazzNatureEnum.LIBERAL.equals(clazz.getClazzNature())) {
                    prefix = "01";
                }
                res.put("state", prefix + sb);
                switch (type) {
                    case "01":
                        // 全部
                        break;
                    case "02":
                        // 全科
                        examResultSimpleDtoList = examResultSimpleDtoList.stream().filter(item -> "".equals(item.getExamSubjects()) || CharMatcher.is(',').countIn(item.getExamSubjects()) == 8).collect(Collectors.toList());
                        break;
                    case "03":
                        // 本月
                        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
                        examResultSimpleDtoList = examResultSimpleDtoList.stream().filter(item -> item.getExamEndDate().substring(0, 7).equals(now)).collect(Collectors.toList());
                        break;
                    case "04":
                        // 本学期
                    default:
                }
                try {
                    res.put("chartInfo", getChartInfo(sb.toString(), clazz, examResultSimpleDtoList));
                } catch (Exception e2) {
                    throw new ApiCode.ApiException(-2, e2.getMessage());
                }
            }
        }
        return res;
    }

    @Override
    public Map<String, Object> studentAnalysePager(Long schoolyardId, Long examPublishId, Long studentId, String type) {
        Map<String, Object> res = new HashMap<>();
        if (examPublishId != null && studentId != null) {
            if (schoolyardId == null) {
                schoolyardId = 1L;
            }
            List<ExamClazzAnalyseClazzAndStudentDto> examResultSimpleDtoList = this.examResultMapper.studentAnalysePager(schoolyardId, examPublishId, studentId);
            if (CollectionUtils.isNotEmpty(examResultSimpleDtoList)) {
                ExamResultOtherDto examResultOtherDto;
                for (ExamClazzAnalyseClazzAndStudentDto item : examResultSimpleDtoList) {
                    examResultOtherDto = JSONObject.parseObject(item.getOther(), ExamResultOtherDto.class);
                    try {
                        BeanUtils.copyProperties(item, examResultOtherDto);
                    } catch (Exception e1) {
                        throw new ApiCode.ApiException(-1, e1.getMessage());
                    }
                }
                res.put("studentInfo", examResultSimpleDtoList);

                StringBuilder sb = new StringBuilder("000000");
                if (examResultSimpleDtoList.stream().anyMatch(item -> item.getPhysicsScore().compareTo(BigDecimal.ZERO) > 0)) {
                    sb.replace(0, 1, "1");
                }
                if (examResultSimpleDtoList.stream().anyMatch(item -> item.getHistoryScore().compareTo(BigDecimal.ZERO) > 0)) {
                    sb.replace(1, 2, "1");
                }
                if (examResultSimpleDtoList.stream().anyMatch(item -> item.getPoliticsScore().compareTo(BigDecimal.ZERO) > 0)) {
                    sb.replace(2, 3, "1");
                }
                if (examResultSimpleDtoList.stream().anyMatch(item -> item.getChemistryScore().compareTo(BigDecimal.ZERO) > 0)) {
                    sb.replace(3, 4, "1");
                }
                if (examResultSimpleDtoList.stream().anyMatch(item -> item.getGeographyScore().compareTo(BigDecimal.ZERO) > 0)) {
                    sb.replace(4, 5, "1");
                }
                if (examResultSimpleDtoList.stream().anyMatch(item -> item.getBiologyScore().compareTo(BigDecimal.ZERO) > 0)) {
                    sb.replace(5, 6, "1");
                }
                res.put("state", sb.toString());

                switch (type) {
                    case "01":
                        // 全部
                        break;
                    case "02":
                        // 全科
                        examResultSimpleDtoList = examResultSimpleDtoList.stream().filter(item -> "".equals(item.getExamSubjects()) || CharMatcher.is(',').countIn(item.getExamSubjects()) == 8).collect(Collectors.toList());
                        break;
                    case "03":
                        // 本月
                        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
                        examResultSimpleDtoList = examResultSimpleDtoList.stream().filter(item -> item.getExamEndDate().substring(0, 7).equals(now)).collect(Collectors.toList());
                        break;
                    case "04":
                        // 本学期
                    default:
                }
                try {
                    res.put("chartInfo", getChartInfo2(sb.toString(), examResultSimpleDtoList));
                } catch (Exception e2) {
                    throw new ApiCode.ApiException(-2, e2.getMessage());
                }
            }
        }
        return res;
    }

    @Override
    public Map<Long, Object> subjectJoinCount(List<Long> examIds, ClazzNatureEnum clazzNatureEnum) {
        Map<Long, Object> map = new HashMap<>();
        if (CollectionUtils.isEmpty(examIds) || null == clazzNatureEnum)
            return map;
        examIds = examIds.stream().distinct().collect(Collectors.toList());
        examIds.forEach(examId -> map.put(examId, this.baseMapper.subjectJoinCount(examId, clazzNatureEnum.toString())));
        return map;
    }

    @Override
    public List<ExamResult> searchByPageExistOrDefault(Long schoolyardId, Long academicYearSemesterId, Long gradeId, Long examId, Long clazzId, Long studentId, String studentName, String orderByClause) {
        List<ExamResult> res = new ArrayList<>();
        if (null == clazzId || null == examId || null == academicYearSemesterId) return res;

        IPage<ExamResult> page = new Page<>(1, 100);
        page = this.getBaseMapper().searchExamResult(page, schoolyardId, academicYearSemesterId, gradeId, examId, clazzId, studentId, studentName, orderByClause);
        Map<Long, ExamResult> examResultMap = page.getRecords().stream().collect(Collectors.toMap(ExamResult::getStudentId, Function.identity()));

        List<Student> studentList = this.studentMapper.listByClazzStudent(clazzId, academicYearSemesterId, studentName);
        if (null != studentId) studentList = studentList.stream().filter(item -> studentId.equals(item.getId())).collect(Collectors.toList());
        studentList.sort(Comparator.comparing(item -> Integer.parseInt(item.getStudentNumber()), Comparator.naturalOrder()));

        studentList.forEach(item -> {
            ExamResult examResult = examResultMap.getOrDefault(item.getId(), new ExamResult());
            if (null == examResult.getId()) {
                examResult.setClazzId(clazzId);
                examResult.setStudentId(item.getId());
                examResult.setExamId(examId);
            }
            res.add(examResult);
         });

        return res;
    }

    @Override
    public List<ExamResult> batchCreateOrUpdate(List<ExamResult> entityList) {
        entityList = entityList.stream().filter(item -> null != item.getTotalScore() && item.getTotalScore().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());

        entityList.forEach(item -> {
            if (null == item.getId()) {
                item.setDefault().validate(true);
                this.baseMapper.insert(item);
            } else {
                this.baseMapper.updateById(item);
            }

            if (null != item.getExamResultMinor()) {
                ExamResultMinor examResultMinor = item.getExamResultMinor();
                this.examResultMinorMapper.insert(examResultMinor);
            }
        });

        return entityList;
    }

    private Map<Integer, List<ExamChartDto>> getChartInfo2(String s, List<ExamClazzAnalyseClazzAndStudentDto> studentConditionList) throws Exception {
        Map<Integer, List<ExamChartDto>> chatInfo = new HashMap<>();
        for (int i = 1; i <= 8; i++) {
            chatInfo.put(i, new ArrayList<>());
        }
        if (CollectionUtils.isNotEmpty(studentConditionList) && !StringUtils.isNullOrEmpty(s)) {
            for (ExamClazzAnalyseClazzAndStudentDto obj : studentConditionList) {
                // 语文
                Map<String, Object> ywMap = new HashMap<>();
                ywMap.put(obj.getName(), obj.getChineseScore());
                if (chatInfo.get(1).size() == 0) {
                    ExamChartDto yw = new ExamChartDto();
                    yw.setName("语文");
                    yw.setData(new ArrayList<>());
                    yw.getData().add(ywMap);
                    chatInfo.get(1).add(yw);
                } else {
                    chatInfo.get(1).get(0).getData().add(ywMap);
                }
                // 数学
                Map<String, Object> ssMap = new HashMap<>();
                ssMap.put(obj.getName(), obj.getMathScore());
                if (chatInfo.get(2).size() == 0) {
                    ExamChartDto yw = new ExamChartDto();
                    yw.setName("数学");
                    yw.setData(new ArrayList<>());
                    yw.getData().add(ssMap);
                    chatInfo.get(2).add(yw);
                } else {
                    chatInfo.get(2).get(0).getData().add(ssMap);
                }
                // 英语
                Map<String, Object> map3 = new HashMap<>();
                map3.put(obj.getName(), obj.getEnglishScore());
                if (chatInfo.get(3).size() == 0) {
                    ExamChartDto yw = new ExamChartDto();
                    yw.setName("英语");
                    yw.setData(new ArrayList<>());
                    yw.getData().add(map3);
                    chatInfo.get(3).add(yw);
                } else {
                    chatInfo.get(3).get(0).getData().add(map3);
                }
                // 总分 总赋分科名
                Map<String, Object> map4 = new HashMap<>();
                Map<String, Object> map5 = new HashMap<>();
                map4.put(obj.getName(), obj.getTotalScore());
                map5.put(obj.getName(), obj.getTotalWeightedScore());
                if (chatInfo.get(8).size() == 0) {
                    ExamChartDto yw = new ExamChartDto();
                    yw.setName("总分(原始)");
                    yw.setData(new ArrayList<>());
                    yw.getData().add(map4);
                    chatInfo.get(8).add(yw);
                    ExamChartDto yw1 = new ExamChartDto();
                    yw1.setName("总分(赋分)");
                    yw1.setData(new ArrayList<>());
                    yw1.getData().add(map5);
                    chatInfo.get(8).add(yw1);
                } else {
                    chatInfo.get(8).get(0).getData().add(map4);
                    chatInfo.get(8).get(1).getData().add(map5);
                }

                if (obj.getPhysicsScore().compareTo(BigDecimal.ZERO) > 0) {
                    Map<String, Object> mapwl = new HashMap<>();
                    mapwl.put(obj.getName(), obj.getPhysicsScore());
                    if (chatInfo.get(4).size() == 0) {
                        ExamChartDto yw = new ExamChartDto();
                        yw.setName("物理");
                        yw.setData(new ArrayList<>());
                        yw.getData().add(mapwl);
                        chatInfo.get(4).add(yw);
                    } else {
                        chatInfo.get(4).get(0).getData().add(mapwl);
                    }
                }
                if (obj.getHistoryScore().compareTo(BigDecimal.ZERO) > 0) {
                    Map<String, Object> mapwl = new HashMap<>();
                    mapwl.put(obj.getName(), obj.getPhysicsScore());
                    if (chatInfo.get(5).size() == 0) {
                        ExamChartDto yw = new ExamChartDto();
                        yw.setName("历史");
                        yw.setData(new ArrayList<>());
                        yw.getData().add(mapwl);
                        chatInfo.get(5).add(yw);
                    } else {
                        chatInfo.get(5).get(0).getData().add(mapwl);
                    }
                }

                if (obj.getFourTotal().compareTo(BigDecimal.ZERO) > 0) {
                    // 四总和总赋分
                    Map<String, Object> map6 = new HashMap<>();
                    Map<String, Object> map7 = new HashMap<>();
                    map6.put(obj.getName(), obj.getFourTotal());
                    map7.put(obj.getName(), obj.getTotalWeightedScore());
                    if (chatInfo.get(7).size() == 0) {
                        ExamChartDto yw = new ExamChartDto();
                        yw.setName("四总");
                        yw.setData(new ArrayList<>());
                        yw.getData().add(map6);
                        chatInfo.get(7).add(yw);
                        ExamChartDto yw1 = new ExamChartDto();
                        yw1.setName("总分");
                        yw1.setData(new ArrayList<>());
                        yw1.getData().add(map7);
                        chatInfo.get(7).add(yw1);
                    } else {
                        chatInfo.get(7).get(0).getData().add(map6);
                        chatInfo.get(7).get(1).getData().add(map7);
                    }
                }

                // 其余两门科目
                // 政化地生
                Map<String, Object> map9 = new HashMap<>();
                Map<String, Object> map10 = new HashMap<>();
                if (s.endsWith("0011")) {
                    map9.put(obj.getName(), obj.getGeographyScore());
                    map10.put(obj.getName(), obj.getBiologyScore());
                    if (chatInfo.get(6).size() == 0) {
                        ExamChartDto yw = new ExamChartDto();
                        yw.setName("地理");
                        yw.setData(new ArrayList<>());
                        yw.getData().add(map9);
                        chatInfo.get(6).add(yw);
                        ExamChartDto yw1 = new ExamChartDto();
                        yw1.setName("生物");
                        yw1.setData(new ArrayList<>());
                        yw1.getData().add(map10);
                        chatInfo.get(6).add(yw1);
                    } else {
                        chatInfo.get(6).get(0).getData().add(map9);
                        chatInfo.get(6).get(1).getData().add(map10);
                    }
                } else if (s.endsWith("0101")) {
                    map9.put(obj.getName(), obj.getBiologyScore());
                    map10.put(obj.getName(), obj.getChemistryScore());
                    if (chatInfo.get(6).size() == 0) {
                        ExamChartDto yw = new ExamChartDto();
                        yw.setName("生物");
                        yw.setData(new ArrayList<>());
                        yw.getData().add(map9);
                        chatInfo.get(6).add(yw);
                        ExamChartDto yw1 = new ExamChartDto();
                        yw1.setName("化学");
                        yw1.setData(new ArrayList<>());
                        yw1.getData().add(map10);
                        chatInfo.get(6).add(yw1);
                    } else {
                        chatInfo.get(6).get(0).getData().add(map9);
                        chatInfo.get(6).get(1).getData().add(map10);
                    }
                } else if (s.endsWith("1001")) {
                    map9.put(obj.getName(), obj.getBiologyScore());
                    map10.put(obj.getName(), obj.getPoliticsScore());
                    if (chatInfo.get(6).size() == 0) {
                        ExamChartDto yw = new ExamChartDto();
                        yw.setName("生物");
                        yw.setData(new ArrayList<>());
                        yw.getData().add(map9);
                        chatInfo.get(6).add(yw);
                        ExamChartDto yw1 = new ExamChartDto();
                        yw1.setName("政治");
                        yw1.setData(new ArrayList<>());
                        yw1.getData().add(map10);
                        chatInfo.get(6).add(yw1);
                    } else {
                        chatInfo.get(6).get(0).getData().add(map9);
                        chatInfo.get(6).get(1).getData().add(map10);
                    }
                } else if (s.endsWith("0110")) {
                    map9.put(obj.getName(), obj.getGeographyScore());
                    map10.put(obj.getName(), obj.getChemistryScore());
                    if (chatInfo.get(6).size() == 0) {
                        ExamChartDto yw = new ExamChartDto();
                        yw.setName("地理");
                        yw.setData(new ArrayList<>());
                        yw.getData().add(map9);
                        chatInfo.get(6).add(yw);
                        ExamChartDto yw1 = new ExamChartDto();
                        yw1.setName("化学");
                        yw1.setData(new ArrayList<>());
                        yw1.getData().add(map10);
                        chatInfo.get(6).add(yw1);
                    } else {
                        chatInfo.get(6).get(0).getData().add(map9);
                        chatInfo.get(6).get(1).getData().add(map10);
                    }
                } else if (s.endsWith("1010")) {
                    map9.put(obj.getName(), obj.getGeographyScore());
                    map10.put(obj.getName(), obj.getPoliticsScore());
                    if (chatInfo.get(6).size() == 0) {
                        ExamChartDto yw = new ExamChartDto();
                        yw.setName("地理");
                        yw.setData(new ArrayList<>());
                        yw.getData().add(map9);
                        chatInfo.get(6).add(yw);
                        ExamChartDto yw1 = new ExamChartDto();
                        yw1.setName("政治");
                        yw1.setData(new ArrayList<>());
                        yw1.getData().add(map10);
                        chatInfo.get(6).add(yw1);
                    } else {
                        chatInfo.get(6).get(0).getData().add(map9);
                        chatInfo.get(6).get(1).getData().add(map10);
                    }
                } else {
                    map9.put(obj.getName(), obj.getChemistryScore());
                    map10.put(obj.getName(), obj.getPoliticsScore());
                    if (chatInfo.get(6).size() == 0) {
                        ExamChartDto yw = new ExamChartDto();
                        yw.setName("化学");
                        yw.setData(new ArrayList<>());
                        yw.getData().add(map9);
                        chatInfo.get(6).add(yw);
                        ExamChartDto yw1 = new ExamChartDto();
                        yw1.setName("政治");
                        yw1.setData(new ArrayList<>());
                        yw1.getData().add(map10);
                        chatInfo.get(6).add(yw1);
                    } else {
                        chatInfo.get(6).get(0).getData().add(map9);
                        chatInfo.get(6).get(1).getData().add(map10);
                    }
                }
            }
        }
        return chatInfo;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> clazzAnalyse(Long examId, Long clazzId, Long studentId) throws Exception {
        Map<String, Object> finalRes = new HashMap<>();
        if (clazzId == null) {
            List<?> list = new ArrayList<>();
            finalRes.put("title", "");
            finalRes.put("goalScoreNames", list);
            finalRes.put("subjectInfo", list);
            finalRes.put("studentInfo", list);
            finalRes.put("chatInfo", new HashMap<>());
            return finalRes;
        }
        // 获取班级基本信息(除目标)
        Clazz clazz = this.clazzMapper.selectById(clazzId);
        if (clazz == null) {
            throw new ApiCode.ApiException(-1, "班级ID无效");
        }
        // 获取此次考试 所有目标 同时将比例转化为人数
        ExamGoalService examGoalServiceProxy = SpringUtils.getBean("examGoalServiceImpl");
        List<ExamGoalDto> examGoalDtoList = examGoalServiceProxy.getAllGoal(examId, null);
        // 获取班级总分 赋分目标完成情况
        Map<String, Object> clazzGoalTotal = examGoalServiceProxy.getGoalTotal(clazzId, examId, examGoalDtoList);
        List<ExamGoalTotalCombineDto> list = (List<ExamGoalTotalCombineDto>) ((Map<ClazzNatureEnum, Object>)clazzGoalTotal.get("goalTotalList")).getOrDefault(clazz.getClazzNature(), new ArrayList<>());
        StringBuilder sb = new StringBuilder();
        sb.append(clazz.getGrade().getName()).append(clazz.getName())
                .append(" 班主任：").append(clazz.getHeadTeacher())
                .append(" 学生人数：").append(clazz.getStudentCount());
        if (CollectionUtils.isNotEmpty(list)) {
            list.get(0).getGoals().forEach((k, v) -> sb.append(" ").append(k).append("人数 ").append(v.getClazzMeetingCnt()).append("人"));
        }
        finalRes.put("title", sb.toString());

        // 获取班级科目目标完成情况
        Map<String, Object> clazzGoalSubject = examGoalServiceProxy.getGoalSubject(clazzId, examId, examGoalDtoList);
        Map<String, Object> goalSubjects = (Map<String, Object>) clazzGoalSubject.get("goalSubjectMap");
        finalRes.put("goalScoreNames", goalSubjects.keySet());

        // 整合科目信息
        Map<String, Object> infoMap = this.getBaseMapper().getSubjectConditionFromExam(examId, clazzId);
        if (infoMap == null || infoMap.isEmpty()) {
            throw new ApiCode.ApiException(-1, "考试ID无效");
        }
        List<Map<String, Object>> subjectInfoList = new ArrayList<>();
        if (infoMap.get("subjects") != null && !"".equals(infoMap.get("subjects").toString())) {
            String[] subjectsArray = infoMap.get("subjects").toString().split("[,]");
            String[] teachersArray = infoMap.get("teachers").toString().split("[*]");
            String[] scoresArray = infoMap.get("scores").toString().split("[,]");
            String[] aliasesArray = infoMap.get("aliases").toString().split("[,]");
            for (int i = 0; i < subjectsArray.length; i++) {
                Map<String, Object> param = new HashMap<>();
                // 将对应学科目标全部放入Map
                String str = aliasesArray[i].concat("MeetingCnt");
                goalSubjects.forEach((k, v) -> {
                    ExamGoalClazzSubjectDto curr = ((Map<ClazzNatureEnum, List<ExamGoalClazzSubjectDto>>) v).get(clazz.getClazzNature()).get(0);
                    try {
                        Method m = ExamGoalClazzSubjectDto.class.getMethod("get" + str.substring(0, 1).toUpperCase() + str.substring(1));
                        param.put(k, m.invoke(curr));
                    } catch (Exception e) {
                        throw new ApiCode.ApiException(-1, "获取方法失败");
                    }
                });
                param.put("name", subjectsArray[i]);
                param.put("yxRatio", 0);
                param.put("jGRatio", 0);
                param.put("teacher", teachersArray[i]);
                param.put("score", scoresArray[i]);
                int join = Integer.parseInt(infoMap.get(aliasesArray[i].concat("Join")).toString());
                param.put("joinCnt", join);
                param.put("qkCnt", clazz.getStudentCount() - join);
                param.put("maxScore", infoMap.get(aliasesArray[i].concat("Max")));
                param.put("minScore", infoMap.get(aliasesArray[i].concat("Min")));
                param.put("avgScore", infoMap.get(aliasesArray[i].concat("Avg")));
                BigDecimal joinCnt = new BigDecimal(String.valueOf(join));
                if (joinCnt.compareTo(BigDecimal.ZERO) != 0) {
                    param.put(
                            "yxRatio",
                            new BigDecimal(infoMap.get(aliasesArray[i].concat("Yx")).toString())
                                    .multiply(BigDecimal.valueOf(100))
                                    .divide(joinCnt, 1, RoundingMode.HALF_UP)
                    );
                    param.put(
                            "jGRatio",
                            new BigDecimal(infoMap.get(aliasesArray[i].concat("Jg")).toString())
                                    .multiply(BigDecimal.valueOf(100))
                                    .divide(joinCnt, 1, RoundingMode.HALF_UP)
                    );
                }
                subjectInfoList.add(param);
            }
        }
        finalRes.put("subjectInfo", subjectInfoList);

        // 查询学生当前考试 前六次考试情况
        List<ExamClazzAnalyseClazzAndStudentDto> studentConditionList = new ArrayList<>();
        if (studentId != null) {
            studentConditionList = this.getBaseMapper().getStudentConditionFour(examId, studentId, clazz.getClazzNature().toString(), clazzId);

        }
        finalRes.put("studentInfo", studentConditionList);
        finalRes.put("chatInfo", this.getChartInfo("", clazz, studentConditionList));
        return finalRes;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean calculate(Long examId, String name, String includeWeighted) {
        Exam exam = this.examMapper.selectById(examId);
        if (exam == null) {
            throw new ApiCode.ApiException(-5, "考试不存在");
        }

        // 不算赋分的考试
        if (YesNoEnum.NO.equals(exam.getCalNaturalScore())) {
            includeWeighted = "01";
        }

        // 学业等级设置参数
        LambdaQueryWrapper<ExamGoalNaturalTemplate> wrapper = Wrappers.<ExamGoalNaturalTemplate>lambdaQuery()
                .eq(ExamGoalNaturalTemplate::getGradeId, exam.getGrade().getId())
                .eq(ExamGoalNaturalTemplate::getName, name);
        List<ExamGoalNaturalTemplate> examGoalNaturalTemplates = this.examGoalNaturalTemplateMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(examGoalNaturalTemplates)) {
            LambdaQueryWrapper<ExamGoalNatural> wrapper1 = Wrappers.<ExamGoalNatural>lambdaQuery()
                    .eq(ExamGoalNatural::getExamId, examId);
            List<ExamGoalNatural> examGoalNaturals = this.examGoalNaturalMapper.selectList(wrapper1);
            if (CollectionUtils.isEmpty(examGoalNaturals)) {
                throw new ApiCode.ApiException(-5, "赋分比例不存在或重复！");
            }
            examGoalNaturalTemplates = examGoalNaturalTemplates == null ? new ArrayList<>() : examGoalNaturalTemplates;
            for (ExamGoalNatural item : examGoalNaturals) {
                ExamGoalNaturalTemplate examGoalNaturalTemplate = new ExamGoalNaturalTemplate();
                examGoalNaturalTemplate.setAcademyRatioA(item.getAcademyRatioA());
                examGoalNaturalTemplate.setAcademyRatioB(item.getAcademyRatioB());
                examGoalNaturalTemplate.setAcademyRatioC(item.getAcademyRatioC());
                examGoalNaturalTemplate.setAcademyRatioD(item.getAcademyRatioD());
                examGoalNaturalTemplate.setAcademyRatioE(item.getAcademyRatioE());
                examGoalNaturalTemplate.setSubject(item.getSubject());
                examGoalNaturalTemplate.setName(item.getName());
                examGoalNaturalTemplate.setSubjectId(item.getSubjectId());
                examGoalNaturalTemplate.setSettingType(item.getSettingType());
                examGoalNaturalTemplates.add(examGoalNaturalTemplate);
            }
        }
        Map<String, ExamGoalNaturalTemplate> stringExamGoalNaturalTemplateMap = examGoalNaturalTemplates
                    .stream().collect(Collectors.toMap(o -> o.getSubject().getSubjectAlias(), o1 -> o1));

        // 全年级学生分数
        IPage<ExamResult> page = this.baseMapper.searchExamResult(new Page<>(1, 2000),null, null, null, examId, null, null, null, null);
        List<ExamResult> examResultList = page.getRecords();
        if (CollectionUtils.isEmpty(examResultList)) {
            return true;
        }

        // 存储所有学科分数和学业等级设置参数
        Map<String, List<BigDecimal>> subjectScoreMap = new HashMap<>();
        Map<String, Subject> subjectMap = examGoalNaturalTemplates.stream().collect(Collectors.toMap(item -> item.getSubject().getSubjectAlias(), ExamGoalNaturalTemplate::getSubject));
        try {
            for (ExamResult examResult : examResultList) {
                for (Map.Entry<String, Subject> entry : subjectMap.entrySet()) {
                    String key = entry.getKey();
                    Subject subject = entry.getValue();
                    String methodName = "get" + key.substring(0, 1).toUpperCase() + key.substring(1) + "Score";
                    BigDecimal score = BigDecimal.ZERO;
                    if (YesNoEnum.NO.equals(subject.getHasWeight()) && YesNoEnum.NO.equals(subject.getIsMain())) {
                        if (null != examResult.getExamResultMinor()) {
                            Method method = ExamResultMinor.class.getMethod(methodName);
                            score = (BigDecimal) method.invoke(examResult.getExamResultMinor());
                        }
                    } else {
                        Method method = ExamResult.class.getMethod(methodName);
                        score = (BigDecimal) method.invoke(examResult);
                    }

                    if (BigDecimal.ZERO.compareTo(score) < 0) {
                        subjectScoreMap.computeIfAbsent(key, o -> new ArrayList<>()).add(score);
                    }
                }
            }
        } catch (Exception e0) {
            throw new ApiCode.ApiException(-5, e0.getClass().toString());
        }
        subjectScoreMap.values().forEach(item -> item.sort(Comparator.reverseOrder()));

        Date now = new Date();
        BigDecimal hundred = BigDecimal.valueOf(100);
        // 科目参与人数, 科目排名(百分比), 科目分数, 区间最高分, 区间最低分, 赋分
        BigDecimal joinCnt, rank, score, originMax, originMin, weightScore;
        try {
            for (ExamResult examResult : examResultList) {
                    for (Map.Entry<String, Subject> entry : subjectMap.entrySet()) {
                        String key = entry.getKey();
                        Subject subject = entry.getValue();
                        String methodName = key.substring(0, 1).toUpperCase() + key.substring(1);
                        boolean isMinor = true;
                        if (YesNoEnum.NO.equals(subject.getHasWeight()) && YesNoEnum.NO.equals(subject.getIsMain())) {
                            if (null != examResult.getExamResultMinor()) {
                                Method method = ExamResultMinor.class.getMethod(methodName + "Score");
                                score = (BigDecimal) method.invoke(examResult.getExamResultMinor());
                            } else {
                                score = BigDecimal.ZERO;
                            }
                        } else {
                            isMinor = false;
                            Method method = ExamResult.class.getMethod(methodName + "Score");
                            score = (BigDecimal) method.invoke(examResult);
                        }

                        if (score.compareTo(BigDecimal.ZERO) == 0) {
                            continue;
                        }

                        List<BigDecimal> currScoreList = subjectScoreMap.get(key);
                        joinCnt = BigDecimal.valueOf(currScoreList.size());
                        rank = BigDecimal.valueOf(TwxUtils.binarySearchEnhance1(currScoreList, score)).multiply(hundred).divide(joinCnt, 2, RoundingMode.HALF_UP);
                        ExamGoalNaturalTemplate examGoalNaturalTemplate = stringExamGoalNaturalTemplateMap.get(key);

                        int begin = 1, end = currScoreList.size();
                        int mapLeft = 99, mapRight = 30;
                        BigDecimal ratioA = BigDecimal.valueOf(examGoalNaturalTemplate.getAcademyRatioA());
                        BigDecimal ratioB = BigDecimal.valueOf(examGoalNaturalTemplate.getAcademyRatioB());
                        BigDecimal ratioC = BigDecimal.valueOf(examGoalNaturalTemplate.getAcademyRatioC());
                        BigDecimal ratioD = BigDecimal.valueOf(examGoalNaturalTemplate.getAcademyRatioD());
                        String level;
                        if (ExamNaturalSettingEnum.COUNT.equals(examGoalNaturalTemplate.getSettingType())) {
                            ratioB = ratioB.add(ratioA);
                            ratioC = ratioC.add(ratioB);
                            ratioD = ratioD.add(ratioC);
                            // 获取对应等级区间
                            if (rank.compareTo(ratioA) <= 0) {
                                level = "A";
                                mapRight = 86;
                                end = joinCnt.multiply(ratioA).divide(hundred, 0, RoundingMode.HALF_UP).intValue();
                            } else if (rank.compareTo(ratioB) <= 0) {
                                level = "B";
                                mapLeft = 85;
                                mapRight = 71;
                                begin = joinCnt.multiply(ratioA).divide(hundred, 0, RoundingMode.HALF_UP).intValue();
                                end = joinCnt.multiply(ratioB).divide(hundred, 0, RoundingMode.HALF_UP).intValue();
                            } else if (rank.compareTo(ratioC) <= 0) {
                                level = "C";
                                mapLeft = 70;
                                mapRight = 56;
                                begin = joinCnt.multiply(ratioB).divide(hundred, 0, RoundingMode.HALF_UP).intValue();
                                end = joinCnt.multiply(ratioC).divide(hundred, 0, RoundingMode.HALF_UP).intValue();
                            } else if (rank.compareTo(ratioD) <= 0) {
                                level = "D";
                                mapLeft = 55;
                                mapRight = 41;
                                begin = joinCnt.multiply(ratioC).divide(hundred, 0, RoundingMode.HALF_UP).intValue();
                                end = joinCnt.multiply(ratioD).divide(hundred, 0, RoundingMode.HALF_UP).intValue();
                            } else {
                                level = "E";
                                mapLeft = 40;
                                begin = joinCnt.multiply(ratioD).divide(hundred, 0, RoundingMode.HALF_UP).intValue();
                            }
                            originMax = currScoreList.get(begin - 1);
                            originMin = currScoreList.get(end - 1);
                            // 这里做一步修正 防止舍入出现负数的情况
                            if (score.compareTo(originMax) > 0) {
                                // 理论上不会越界
                                originMax = currScoreList.get(begin - 2);
                            }
                            if (score.compareTo(originMin) < 0) {
                                // 理论上不会越界
                                originMin = currScoreList.get(end);
                            }
                        } else {
                            BigDecimal[] bigDecimals;
                            // 获取对应等级区间 以及区间实际最高最低分
                            if (score.compareTo(ratioA) <= 0) {
                                level = "A";
                                mapRight = 86;
                                bigDecimals = TwxUtils.binarySearchEnhance2(currScoreList, currScoreList.get(0), ratioA);
                            } else if (score.compareTo(ratioB) <= 0) {
                                level = "B";
                                mapLeft = 85;
                                mapRight = 71;
                                bigDecimals = TwxUtils.binarySearchEnhance2(currScoreList, ratioA.add(BigDecimal.ONE), ratioB);
                            } else if (score.compareTo(ratioC) <= 0) {
                                level = "C";
                                mapLeft = 70;
                                mapRight = 56;
                                bigDecimals = TwxUtils.binarySearchEnhance2(currScoreList, ratioB.add(BigDecimal.ONE), ratioC);
                            } else if (score.compareTo(ratioD) <= 0) {
                                level = "D";
                                mapLeft = 55;
                                mapRight = 41;
                                bigDecimals = TwxUtils.binarySearchEnhance2(currScoreList, ratioC.add(BigDecimal.ONE), ratioD);
                            } else {
                                level = "E";
                                mapLeft = 40;
                                bigDecimals = TwxUtils.binarySearchEnhance2(currScoreList, ratioD.add(BigDecimal.ONE), currScoreList.get(currScoreList.size() - 1));
                            }
                            originMax = bigDecimals[0];
                            originMin = bigDecimals[1];
                        }

                        if (score.compareTo(originMax) == 0) {
                            weightScore = BigDecimal.valueOf(mapLeft);
                        } else if (score.compareTo(originMin) == 0) {
                            weightScore = BigDecimal.valueOf(mapRight);
                        } else {
                            BigDecimal scale = originMax.subtract(score).divide(score.subtract(originMin), 3, RoundingMode.HALF_UP);
                            weightScore = BigDecimal.valueOf(mapLeft).add(scale.multiply(BigDecimal.valueOf(mapRight)))
                                    .divide(BigDecimal.ONE.add(scale), 0, RoundingMode.HALF_UP);
                        }
                        if (!"01".equals(includeWeighted) && YesNoEnum.YES.equals(subject.getHasWeight())) {
                            Method method1 = ExamResult.class.getMethod("set" + methodName + "WeightedScore", BigDecimal.class);
                            method1.invoke(examResult, weightScore);
                        }
                        // 设置学业等级
                        Method method1 = ExamResult.class.getMethod("set" + methodName + "Level", String.class);
                        method1.invoke(examResult, level);
                    }
                examResult.setTotalWeightedScore(examResult.getChineseScore().add(examResult.getMathScore())
                            .add(examResult.getEnglishScore()).add(examResult.getPhysicsScore()).add(examResult.getHistoryScore()
                                    .add(examResult.getBiologyWeightedScore()).add(examResult.getChemistryWeightedScore())
                                    .add(examResult.getGeographyWeightedScore()).add(examResult.getPoliticsWeightedScore())));
            }
        } catch (Exception e1) {
            throw new ApiCode.ApiException(-5, e1.getClass().toString());
        }

        int size = examResultList.size();
        for (int i = 0, j = Math.min(100, size); i <= size; ) {
            this.getBaseMapper().updateWeightedScore(examResultList.subList(i, j));
            i = (j == size ? j + 1 : j);
            j = Math.min(j + 100, size);
        }

        List<ExamGoalNatural> insertList = new ArrayList<>();
        List<Long> subjectIds = new ArrayList<>();
        examGoalNaturalTemplates.forEach(item -> {
            ExamGoalNatural examGoalNatural = new ExamGoalNatural();
            examGoalNatural.setExamId(examId);
            examGoalNatural.setName(item.getName());
            examGoalNatural.setSettingType(item.getSettingType());
            examGoalNatural.setSubjectId(item.getSubjectId());
            examGoalNatural.setAcademyRatioA(item.getAcademyRatioA());
            examGoalNatural.setAcademyRatioB(item.getAcademyRatioB());
            examGoalNatural.setAcademyRatioC(item.getAcademyRatioC());
            examGoalNatural.setAcademyRatioD(item.getAcademyRatioD());
            examGoalNatural.setAcademyRatioE(item.getAcademyRatioE());
            examGoalNatural.setDefault().validate(true);
            insertList.add(examGoalNatural);
            subjectIds.add(item.getSubjectId());
        });
        examGoalNaturalMapper.delete(Wrappers.<ExamGoalNatural>lambdaQuery()
                .eq(ExamGoalNatural::getExamId, examId).in(ExamGoalNatural::getSubjectId, subjectIds));
        examGoalNaturalMapper.batchInsert(insertList);
        examMapper.update(null, Wrappers.<Exam>lambdaUpdate().eq(Exam::getId, examId).set(Exam::getIsPublish, YesNoEnum.NO));
        return true;
    }

    /**
     * 批量插入
     *
     * @param entityList ignore
     * @param batchSize  ignore
     * @return ignore
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveBatch(Collection<ExamResult> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(ExamResultBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    @Override
    public IPage<ExamResult> searchExamResult(IPage<ExamResult> page, Long schoolyardId, Long academicYearSemesterId, Long gradeId, Long examId, Long clazzId, Long studentId, String studentName, String orderByClause) {
        return this.getBaseMapper().searchExamResult(page, schoolyardId, academicYearSemesterId, gradeId, examId, clazzId, studentId, studentName, orderByClause);
    }

    @Override
    public IPage<Map<String, Object>> searchExamResultClazz(IPage<ExamResult> page, Long examId, List<Long> clazzId, String orderByClause, String studentName) {
        if (orderByClause.startsWith("studentNumber")) {
            orderByClause = (orderByClause.endsWith("desc") ? "len desc," : "len,").concat(orderByClause);
        }
        if (clazzId == null) {
            return new Page<>(1, 9999);
        }
        return this.getBaseMapper().searchExamResultClazz(page, examId, clazzId, orderByClause, studentName);
    }

    @Override
    public IPage<Map<String, Object>> searchExamResultStudent(IPage<ExamResult> page, Long schoolyardId, Long academicYearSemesterId, Long gradeId, Long examId, Long clazzId, Long studentId, String studentName, String orderByClause) {
        if (orderByClause.startsWith("studentNumber")) {
            orderByClause = (orderByClause.endsWith("desc") ? "len desc," : "len,").concat(orderByClause);
        }
        return this.getBaseMapper().searchExamResultStudent(page, schoolyardId, academicYearSemesterId, gradeId, examId, clazzId, studentId, studentName, orderByClause);
    }

    @Override
    public IPage<Map<String, Object>> searchExamResultGrade(IPage<ExamResult> page, String clazzNature, Long examId, String orderByClause, Integer limitNum, String studentName, List<Long> clazzIds) {
        return this.getBaseMapper().searchExamResultGrade(page, clazzNature, examId, orderByClause, limitNum, studentName, clazzIds);
    }

    @Value("${web.upload-path}")
    private String uploadPath;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importExcel(Long examId,String fileUrl) {
        if (fileUrl == null || fileUrl == "")
            throw new ApiCode.ApiException(-1, "没有上传文件！");
        String[] items = fileUrl.split("/");
        File file = new File(uploadPath + File.separator + items[items.length - 2] + File.separator + items[items.length - 1]);
        if (!file.exists())
            throw new ApiCode.ApiException(-1, "文件不存在！");
        try {
            List<String> clazzAndStudentList = this.getBaseMapper().getClazzAndStudentList(examId);
            // 读取 Excel
            XSSFWorkbook book = new XSSFWorkbook(file);
            XSSFSheet sheet = book.getSheetAt(0);
            file.delete();
            //获得总行数
            int rowNum = sheet.getPhysicalNumberOfRows();
            // 成绩列表
            List<ExamResult> examResults = new ArrayList<>();
            // 错误行
            StringBuilder sb = new StringBuilder();
            String errText = "";
            // 读取单元格数据
            for (int rowIndex = 1; rowIndex < rowNum; rowIndex++) {
                errText = "";
                String clazzName = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(0)).concat("班");
                String studentName = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(2)).trim();
                String chineseScore = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(3));
                String mathScore = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(4));
                String englishScore = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(5));
                String physicsScore = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(6));
                String chemistryScore = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(7));
                String chemistryWeightedScore = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(8));
                String biologyScore = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(9));
                String biologyWeightedScore = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(10));
                String historyScore = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(11));
                String politicsScore = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(12));
                String politicsWeightedScore = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(13));
                String geographyScore = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(14));
                String geographyWeightedScore = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(15));
                if (StringUtils.isNullOrEmpty(clazzName)) {
                    errText += "\t班级不存在";
                }
                if (StringUtils.isNullOrEmpty(studentName)) {
                    errText += "\t学生不存在";
                }
                List<Student> students = this.studentMapper.selectList(Wrappers.<Student>lambdaQuery().eq(Student::getName, studentName));
                if (CollectionUtils.isEmpty(students)) {
                    continue;
                }
                String[] clazzAndStudentInfos = new String[0];
                if (!StringUtils.isNullOrEmpty(clazzName) && !StringUtils.isNullOrEmpty(studentName)) {
                    List<String> clazzAndStudentInfoList = clazzAndStudentList.stream().filter(data -> data.contains(',' + clazzName + ',' + studentName)).collect(Collectors.toList());
                    if (clazzAndStudentInfoList.size() == 0) {
                        errText += "\t班级学生信息不匹配";
                    } else {
                        clazzAndStudentInfos = clazzAndStudentInfoList.get(0).split(",");
                    }
                }
                if (!StringUtils.isNullOrEmpty(errText))
                    sb.append(errText = "第" + rowIndex + "行 " + errText + "\n");
                if (StringUtils.isNullOrEmpty(errText)) {
                    ExamResult examResult = new ExamResult();
                    examResult.setClazzId(Long.parseLong(clazzAndStudentInfos[0]));
                    examResult.setStudentId(Long.parseLong(clazzAndStudentInfos[1]));
                    examResult.setExamId(examId);
                    examResult.setChineseScore(new BigDecimal(StringUtils.isNullOrEmpty(chineseScore) ? "0" : chineseScore));
                    examResult.setMathScore(new BigDecimal(StringUtils.isNullOrEmpty(mathScore) ? "0" : mathScore));
                    examResult.setEnglishScore(new BigDecimal(StringUtils.isNullOrEmpty(englishScore) ? "0" : englishScore));
                    examResult.setPhysicsScore(new BigDecimal(StringUtils.isNullOrEmpty(physicsScore) ? "0" : physicsScore));
                    examResult.setChemistryScore(new BigDecimal(StringUtils.isNullOrEmpty(chemistryScore) ? "0" : chemistryScore));
                    examResult.setChemistryWeightedScore(new BigDecimal(StringUtils.isNullOrEmpty(chemistryWeightedScore) ? "0" : chemistryWeightedScore));
                    examResult.setBiologyScore(new BigDecimal(StringUtils.isNullOrEmpty(biologyScore) ? "0" : biologyScore));
                    examResult.setBiologyWeightedScore(new BigDecimal(StringUtils.isNullOrEmpty(biologyWeightedScore) ? "0" : biologyWeightedScore));
                    examResult.setHistoryScore(new BigDecimal(StringUtils.isNullOrEmpty(historyScore) ? "0" : historyScore));
                    examResult.setPoliticsScore(new BigDecimal(StringUtils.isNullOrEmpty(politicsScore) ? "0" : politicsScore));
                    examResult.setPoliticsWeightedScore(new BigDecimal(StringUtils.isNullOrEmpty(politicsWeightedScore) ? "0" : politicsWeightedScore));
                    examResult.setGeographyScore(new BigDecimal(StringUtils.isNullOrEmpty(geographyScore) ? "0" : geographyScore));
                    examResult.setGeographyWeightedScore(new BigDecimal(StringUtils.isNullOrEmpty(geographyWeightedScore) ? "0" : geographyWeightedScore));

                    BigDecimal partScore = examResult.getChineseScore().add(examResult.getMathScore())
                            .add(examResult.getEnglishScore()).add(examResult.getPhysicsScore())
                            .add(examResult.getHistoryScore());

                    BigDecimal totalScore = partScore.add(examResult.getChemistryScore()).add(examResult.getBiologyScore())
                                    .add(examResult.getPoliticsScore()).add(examResult.getGeographyScore());
                    if (BigDecimal.ZERO.compareTo(totalScore) == 0) continue;
                    examResult.setTotalScore(totalScore);

                    BigDecimal totalWeightedScore = partScore.add(examResult.getChemistryWeightedScore()).add(examResult.getBiologyWeightedScore())
                            .add(examResult.getPoliticsWeightedScore()).add(examResult.getGeographyWeightedScore());
                    examResult.setTotalWeightedScore(totalWeightedScore);
                    examResult.setOther("");
                    examResult.setDefault();
                    examResults.add(examResult);
                }
            }
            if (sb.length() != 0)
                throw new ApiCode.ApiException(-1, "数据有错！\n" + sb.toString());
            QueryWrapper<ExamResult> queryWrapper = new QueryWrapper<ExamResult>().eq("exam_id", examId);
            queryWrapper.in("clazz_id", clazzAndStudentList.stream().map(data -> Long.valueOf(data.split(",")[0])).collect(Collectors.toList()));
            this.getBaseMapper().delete(queryWrapper);
            this.saveBatch(examResults, examResults.size());

            ExamService examService = SpringUtils.getBean("examServiceImpl");
            examService.publish(examId);

        } catch (IOException | InvalidFormatException e) {
            throw new ApiCode.ApiException(-1, "导入数据失败！");
        }
    }

    @Override
    public XSSFWorkbook exportExcel(String type, Long clazzId, Exam exam) throws IOException {
        InputStream is = getClass().getResourceAsStream("/static/templates/成绩模板.xlsx");
        XSSFWorkbook book = new XSSFWorkbook(is);
        XSSFSheet sheet = book.getSheetAt(1);
        XSSFCellStyle style = book.createCellStyle();
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        int rowNum = sheet.getPhysicalNumberOfRows();
        QueryWrapper<ExamResult> queryWrapper = new QueryWrapper<ExamResult>();
        queryWrapper.eq("exam_id", exam.getId());
        if (clazzId != null)
            queryWrapper.eq("clazz_id", clazzId);
        List<ExamResult> examResultList = this.list(queryWrapper);
        if (examResultList.size() == 0) {
            for (int i = 0; i < rowNum; i++)
                for (int j = 0; j < sheet.getRow(i).getPhysicalNumberOfCells(); j++)
                    sheet.getRow(i).getCell(j).setCellValue("");
        }
        if ("01".equals(type)) {
            examResultList.sort(Comparator.comparing((ExamResult item) -> Integer.parseInt(item.getClazz().getName().replace("班",""))).thenComparing((ExamResult item) -> StringUtils.isNullOrEmpty(item.getStudent().getStudentNumber()) ? 0 : Integer.parseInt(item.getStudent().getStudentNumber())));
        } else {
            examResultList.sort(Comparator.comparing((ExamResult item) -> Integer.parseInt(item.getClazz().getName().replace("班",""))).thenComparing(ExamResult::getTotalWeightedScore));
        }
        for (int i = 0; i < examResultList.size(); i++) {
            ExamResult examResult = examResultList.get(i);
            XSSFRow row = sheet.createRow(i + 1);

            XSSFCell cell = row.createCell(0, CellType.STRING);
            cell.setCellStyle(style);
            cell.setCellValue(examResult.getClazz().getName());

            cell = row.createCell(1, CellType.STRING);
            cell.setCellStyle(style);
            cell.setCellValue(examResult.getStudent().getName());

            cell = row.createCell(2, CellType.NUMERIC);
            cell.setCellStyle(style);
            cell.setCellValue(examResult.getTotalScore().setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());

            cell = row.createCell(3, CellType.NUMERIC);
            cell.setCellStyle(style);
            cell.setCellValue(examResult.getTotalWeightedScore().setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());

            cell = row.createCell(4, CellType.NUMERIC);
            cell.setCellStyle(style);
            cell.setCellValue(examResult.getChineseScore().setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());

            cell = row.createCell(5, CellType.NUMERIC);
            cell.setCellStyle(style);
            cell.setCellValue(examResult.getMathScore().setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());

            cell = row.createCell(6, CellType.NUMERIC);
            cell.setCellStyle(style);
            cell.setCellValue(examResult.getEnglishScore().setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());

            cell = row.createCell(7, CellType.NUMERIC);
            cell.setCellStyle(style);
            cell.setCellValue(examResult.getPhysicsScore().setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());

            cell = row.createCell(8, CellType.NUMERIC);
            cell.setCellStyle(style);
            cell.setCellValue(examResult.getChemistryScore().setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());

            cell = row.createCell(9, CellType.NUMERIC);
            cell.setCellStyle(style);
            cell.setCellValue(examResult.getChemistryWeightedScore().setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());

            cell = row.createCell(10, CellType.NUMERIC);
            cell.setCellStyle(style);
            cell.setCellValue(examResult.getBiologyScore().setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());

            cell = row.createCell(11, CellType.NUMERIC);
            cell.setCellStyle(style);
            cell.setCellValue(examResult.getBiologyWeightedScore().setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());

            cell = row.createCell(12, CellType.NUMERIC);
            cell.setCellStyle(style);
            cell.setCellValue(examResult.getHistoryScore().setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());

            cell = row.createCell(13, CellType.NUMERIC);
            cell.setCellStyle(style);
            cell.setCellValue(examResult.getPoliticsScore().setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());

            cell = row.createCell(14, CellType.NUMERIC);
            cell.setCellStyle(style);
            cell.setCellValue(examResult.getPoliticsWeightedScore().setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());

            cell = row.createCell(15, CellType.NUMERIC);
            cell.setCellStyle(style);
            cell.setCellValue(examResult.getGeographyScore().setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());

            cell = row.createCell(16, CellType.NUMERIC);
            cell.setCellStyle(style);
            cell.setCellValue(examResult.getGeographyWeightedScore().setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
        }
        return book;
    }

    @Override
    public XSSFWorkbook exportExcelPager(List<String> columnList, Long examId, Map<String, Object> map) throws Exception {
        XSSFWorkbook book = new XSSFWorkbook();
        XSSFCellStyle style = book.createCellStyle();
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);

        ExamResultService examResultServiceProxy = SpringUtils.getBean("examResultServiceImpl");
        IPage<ExamPagerDto> page = new Page<>(1, 9999);
        IPage<ExamPagerDto> iPage = examResultServiceProxy.queryExamResultPager(page, map, examId);
        List<ExamPagerDto> examPagerDtoList = iPage.getRecords();
        if (CollectionUtils.isNotEmpty(examPagerDtoList)) {
            // 列名排序
            Map<String, String> columnMap = TwxUtils.getColumnMap();
            List<String> columnListOrder = new LinkedList<>();
            columnMap.forEach((k, v) -> {
                if (columnList == null || columnList.contains(k)) {
                    columnListOrder.add(k);
                }
            });

            // 生成表头
            XSSFSheet sheet = book.createSheet();
            XSSFRow row0 = sheet.createRow(0);
            XSSFCell cell;
            for (int i = 0; i < columnListOrder.size(); i++) {
                cell = row0.createCell(i, CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue(columnMap.get(columnListOrder.get(i)));
            }

            // 插入数据
            Class<?> clazz = ExamPagerDto.class;
            Method method;
            for (int i = 0; i < examPagerDtoList.size(); i++) {
                ExamPagerDto obj = examPagerDtoList.get(i);
                XSSFRow row = sheet.createRow(i + 1);
                XSSFCell cellData;
                for (int j = 0; j < columnListOrder.size(); j++) {
                    String curr = columnListOrder.get(j);
                    String upperCase = curr.substring(0, 1).toUpperCase().concat(curr.substring(1));
                    if (curr.endsWith("Change")) {
                        method = clazz.getMethod("get" + upperCase.replace("Change", "Pre"));
                        // 前一次名次
                        int prev = (int) method.invoke(obj);
                        int current;
                        if (curr.startsWith("totalWeighted")) {
                            current = (int) clazz.getMethod("getTotalRank").invoke(obj);
                        } else {
                            current = (int) clazz.getMethod("getTotalWeightedRank").invoke(obj);
                        }
                        cellData = row.createCell(j, CellType.STRING);
                        cellData.setCellValue(prev == 0 ? "暂无" : (prev < current ? "↓ " : "↑ ").concat(String.valueOf(current - prev)));
                    } else if (curr.endsWith("Score") || curr.indexOf("Rank") >= 0) {
                        cellData = row.createCell(j, CellType.NUMERIC);
                        method = clazz.getMethod("get" + upperCase);
                        String value = method.invoke(obj).toString();
                        if (curr.endsWith("Score"))
                            cellData.setCellValue(new BigDecimal(value).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
                        else
                            cellData.setCellValue(Integer.parseInt(value));
                    } else {
                        cellData = row.createCell(j, CellType.STRING);
                        method = clazz.getMethod("get" + upperCase);
                        cellData.setCellValue(method.invoke(obj).toString());
                    }
                    cellData.setCellStyle(style);
                }
            }
        }
        return book;
    }

    @Override
    public IPage<Map<String, Object>> studentScorePager(IPage<ExamResult> page, Long schoolyardId, Long gradeId, List<Long> clazzIds, Long examPublishId, String studentName) {
        if (CollectionUtils.isEmpty(clazzIds)) {
            User user = (User) SecurityUtils.getSubject().getPrincipal();
            clazzIds = user.getClazzList().stream().filter(item -> item.getGradeId().equals(gradeId)).map(item -> item.getId()).collect(Collectors.toList());
            if (clazzIds.size() == 0) {
                return new Page<>();
            }
        }
        return this.baseMapper.studentScorePager(page, schoolyardId, gradeId, clazzIds, examPublishId, studentName);
    }

    @Override
    public IPage<ExamResult> searchExamResultCard(IPage<ExamResult> page, Long examPublishId, Long studentId) {
        if (examPublishId == null || studentId == null) {
            return page;
        }
        return this.getBaseMapper().searchExamResultCard(page, examPublishId, studentId);
    }

}
