package com.zhzx.server.service;

import com.zhzx.server.domain.ExamResult;
import com.zhzx.server.dto.exam.ExamGoalDto;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.rest.req.ExamEdgeSubParam;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;
import java.util.Map;

public interface ScoreAnalyseService {

    List<Map<String, Object>> partitionLineChart(Long examId, Long subjectId, List<Long> clazzIds, Integer tolerance);

    Map<String, Object> scoreBarChart(Long examId, List<Long> clazzIds);

    Map<String, Object> personBarChart(Long examId, Long studentId);

    Map<String, Object> headInfoClazz(Long examId, Long clazzId);

    Map<String, Object> tableInfoClazz(Long examId, Long clazzId, String orderByClause);

    Map<String, Object> headInfoGrade(Long examId, Long clazzId);

    List<Map<String, Object>> subjectTableCompare(Long examId, Long subjectId, List<Long> clazzIds);

    Map<String, Object> goal(Long examId, List<Long> clazzIds);

    List<Map<String, Object>> goalDetail(Long examId, Long clazzId, Long subjectId);

    XSSFWorkbook subjectTableCompareExportExcel(Long examId, Long subjectId, List<Long> clazzIds);

    XSSFWorkbook tableInfoClazzExportExcel(Long examId, Long clazzId, String needGoal, String numberOrScore);

    XSSFWorkbook goalExportExcel(Long examId, Long subjectId, List<Long> clazzIds);

    Map<String, Object> edge(ExamEdgeSubParam param, YesNoEnum selfCustom, List<Long> clazzIds);

    List<ExamResult> edgeDetail(ExamEdgeSubParam param, List<Long> clazzIds);

    List<ExamGoalDto> goalList(Long examId);

    List<Map<String, Object>> radioScore(List<Map<String, Object>> examResultSimpleDtoList);
}
