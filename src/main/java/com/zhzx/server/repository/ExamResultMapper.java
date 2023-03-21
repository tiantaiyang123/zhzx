/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：学校管理
 * 模型名称：考试结果表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.zhzx.server.domain.ExamResult;
import com.zhzx.server.dto.exam.*;
import com.zhzx.server.repository.base.ExamResultBaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ExamResultMapper extends ExamResultBaseMapper {

    List<ExamResult> getExamCard(@Param("studentId") Long studentId,
                                 @Param("examId") Long examId,
                                 @Param("academicYearSemesterId") Long academicYearSemesterId);

    void updateWeightedScore(@Param("entity") List<ExamResult> examResultList);

    void updateRank(@Param("entity") List<ExamResult> examResultList);

    List<String> getAllNatureByExam(@Param("examId") Long examId);

    List<ExamResultSimpleDto> getSimpleResult(@Param("examId") Long examId, @Param("clazzId") Long clazzId);

    List<Map<String, Object>> getSimpleResult1(@Param("examId") Long examId, @Param("clazzId") Long clazzId, @Param("clazzNature") String clazzNature);

    List<ExamResultSimpleDto> getSimpleResultByList(@Param("examId") Long examId, @Param("entity") List<Long> clazzIds);

    List<ExamClazzAnalyseClazzAndStudentDto> studentAnalyse(@Param("schoolyardId") Long schoolyardId,
                                                            @Param("academicYearSemesterId") Long academicYearSemesterId,
                                                            @Param("clazzNature") String clazzNature,
                                                            @Param("clazzId") Long clazzId,
                                                            @Param("studentId") Long studentId);

    List<ExamClazzAnalyseClazzAvgDto> clazzSubjectAnalyse(@Param("schoolyardId") Long schoolyardId,
                                                          @Param("year") String year);

    List<ExamGradeAnalyseClazzSituationDto> gradeAnalyseClazz(Long examId);

    Map<String, Object> gradeAverage(@Param("examId")Long examId, @Param("clazzNature")String clazzNature);

    List<ExamGradeAnalyseClazzSituationDto> clazzTrendStation(@Param("entity") List<Long> examId);

    List<Map<String, Object>> getGradeAnalyseAE(Map<String, Object> params);

    List<Map<String, Object>> getGradeAnalyseBE(Map<String, Object> params);

    List<Map<String, Object>> getGradeAnalyseBT(Map<String, Object> params);

    IPage<Map<String, Object>> queryExamResultByPage(IPage<ExamResult> page,
                                                     @Param(Constants.WRAPPER) QueryWrapper<ExamResult> wrapper);

    List<ExamPreDto> getExamPrePager(@Param("examId") Long examId);

    IPage<ExamPagerDto> queryExamResultPager(IPage<ExamPagerDto> page,
                                             @Param("entity") Map<String, Object> map);

    Map<String, Object> getSubjectConditionFromExam(@Param("examId") Long examId, @Param("clazzId") Long clazzId);

    List<ExamClazzAnalyseClazzAndStudentDto> getStudentConditionFour(@Param("examId") Long examId,
                                                                     @Param("studentId") Long studentId,
                                                                     @Param("clazzNature") String clazzNature,
                                                                     @Param("clazzId") Long clazzId);

    IPage<ExamResult> searchExamResult(IPage<ExamResult> page,
                                       @Param("schoolyardId") Long schoolyardId,
                                       @Param("academicYearSemesterId") Long academicYearSemesterId,
                                       @Param("gradeId") Long gradeId,
                                       @Param("examId") Long examId,
                                       @Param("clazzId") Long clazzId,
                                       @Param("studentId") Long studentId,
                                       @Param("studentName") String studentName,
                                       @Param("orderByClause") String orderByClause);

    IPage<ExamResult> searchExamResultCard(IPage<ExamResult> page,
                                           @Param("examPublishId") Long examPublishId,
                                       @Param("studentId") Long studentId);

    List<ExamClazzAnalyseClazzAndStudentDto> studentAnalysePager(@Param("schoolyardId") Long schoolyardId,
                                                                 @Param("examPublishId") Long examPublishId,
                                                            @Param("studentId") Long studentId);

    IPage<Map<String, Object>> searchExamResultClazz(IPage<ExamResult> page,
                                                     @Param("examId") Long examId,
                                                     @Param("clazzId") List<Long> clazzId,
                                                     @Param("orderByClause") String orderByClause,
                                                     @Param("studentName") String studentName);

    IPage<Map<String, Object>> searchExamResultStudent(IPage<ExamResult> page,
                                       @Param("schoolyardId") Long schoolyardId,
                                       @Param("academicYearSemesterId") Long academicYearSemesterId,
                                       @Param("gradeId") Long gradeId,
                                       @Param("examId") Long examId,
                                       @Param("clazzId") Long clazzId,
                                       @Param("studentId") Long studentId,
                                       @Param("studentName") String studentName,
                                       @Param("orderByClause") String orderByClause);

    IPage<Map<String, Object>> searchExamResultGrade(IPage<ExamResult> page,
                                                     @Param("clazzNature") String clazzNature,
                                                     @Param("examId") Long examId,
                                                     @Param("orderByClause") String orderByClause,
                                                     @Param("limitNum") Integer limitNum,
                                                     @Param("studentName")String studentName,
                                                     @Param("entity")List<Long> clazzIds);

    List<String> getClazzAndStudentList(@Param("examId") Long examId);

    IPage<Map<String, Object>> studentScorePager(IPage<ExamResult> page,
                                                 @Param("schoolyardId") Long schoolyardId,
                                                 @Param("gradeId") Long gradeId,
                                                 @Param("clazzIds") List<Long> clazzIds,
                                                 @Param("examPublishId") Long examPublishId,
                                                 @Param("studentName") String studentName);

    Map<String, Object> subjectJoinCount(@Param("examId") Long examId,
                                         @Param("clazzNatureEnum") String clazzNatureEnum);
}
