/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：学校管理
 * 模型名称：考试结果表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.Exam;
import com.zhzx.server.domain.ExamResult;
import com.zhzx.server.dto.exam.*;
import com.zhzx.server.enums.ClazzNatureEnum;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ExamResultService extends IService<ExamResult> {

    int updateAllFieldsById(ExamResult entity);

    List<ExamResultSimpleDto> personalListStation(Long examId, Long clazzId);

    List<ExamGradeAnalyseClazzSituationDto> scoreListStation(Long examId, Long subjectId);

    Map<String, Object> gradeAnalyseSubject(Long examId, String clazzNature, String subjectName, Integer highest, Integer lowest, Integer tolerance);

    IPage<Map<String, Object>> queryExamResultByPage(IPage<ExamResult> page, QueryWrapper<ExamResult> wrapper);

    IPage<ExamPagerDto> queryExamResultPager(IPage<ExamPagerDto> page, Map<String, Object> map, Long examId);

    Map<String, Object> gradeAnalyseClazz(Long examId);

    List<ExamClazzAnalyseStudentDto> getExamCard(Long studentId, Long examId, Long academicYearSemesterId);

    Map<String, Object> printExam(Long studentId, Long examId, Long academicYearSemesterId);

    Map<String, Object> clazzAnalyse(Long examId, Long clazzId, Long studentId) throws Exception;

    Map<String, Object> studentAnalyse(Long schoolyardId, Long academicYearSemesterId, Long clazzId, Long studentId, String type);

    Boolean calculate(Long examId, String name);

    Map<String, Object> clazzTrendStation(Long gradeId);

    IPage<ExamResult> searchExamResult(IPage<ExamResult> page, Long schoolyardId, Long academicYearSemesterId, Long gradeId, Long examId, Long clazzId, Long studentId, String studentName, String orderByClause);

    IPage<Map<String, Object>> searchExamResultClazz(IPage<ExamResult> page, Long examId, List<Long> clazzId, String orderByClause, String studentName);

    IPage<Map<String, Object>> searchExamResultStudent(IPage<ExamResult> page, Long schoolyardId, Long academicYearSemesterId, Long gradeId, Long examId, Long clazzId, Long studentId, String studentName, String orderByClause);

    IPage<Map<String, Object>> searchExamResultGrade(IPage<ExamResult> page, String clazzNature, Long examId, String orderByClause, Integer limitNum, String studentName, List<Long> clazzIds);

    void importExcel(Long examId, String fileUrl);

    XSSFWorkbook exportExcel(String type, Long clazzId, Exam exam) throws IOException, InvalidFormatException;

    XSSFWorkbook exportExcelPager(List<String> columnList, Long examId, Map<String, Object> map) throws Exception;

    IPage<Map<String, Object>> studentScorePager(IPage<ExamResult> page, Long schoolyardId, Long gradeId, List<Long> clazzIds, Long examPublishId, String studentName);

    IPage<ExamResult> searchExamResultCard(IPage<ExamResult> page, Long examPublishId, Long studentId);

    Map<String, Object> studentAnalysePager(Long schoolyardId, Long examPublishId, Long studentId, String type);

    Map<Long, Object> subjectJoinCount(List<Long> examIds, ClazzNatureEnum clazzNatureEnum);
}
