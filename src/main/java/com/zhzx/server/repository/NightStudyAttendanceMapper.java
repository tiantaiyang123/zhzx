/**
 * 项目：中华中学管理平台
 * 模型分组：学生管理
 * 模型名称：晚自习考勤表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.NightStudyAttendance;
import com.zhzx.server.repository.base.NightStudyAttendanceBaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface NightStudyAttendanceMapper extends NightStudyAttendanceBaseMapper {

    IPage<NightStudyAttendance> searchNightStudyAttendance(IPage<NightStudyAttendance> page,
                                                           @Param("schoolyardId") Long schoolyardId,
                                                           @Param("academicYearSemesterId") Long academicYearSemesterId,
                                                           @Param("gradeId") Long gradeId,
                                                           @Param("clazzId") Long clazzId,
                                                           @Param("week") Integer week,
                                                           @Param("stage") String stage,
                                                           @Param("registerDateFrom") String registerDateFrom,
                                                           @Param("registerDateTo") String registerDateTo,
                                                           @Param("orderByClause") String orderByClause);

    Integer updateStudentNum(@Param("clazzId") Long clazzId,
                             @Param("time") Date time,
                             @Param("shouldStudentCount") Integer shouldStudentCount,
                             @Param("actualStudentCount") Integer actualStudentCount,
                             @Param("dutyType") String dutyType);

    NightStudyAttendance getOneByTimeAndClazzId(@Param("clazzId") Long clazzId,@Param("time") Date startTime,
                                                @Param("stage") String stage);

    List<Map<String,Object>> searchNightStatisticsGroupByTime(@Param("academicYearSemesterId") Long academicYearSemesterId,
                                                            @Param("gradeId") Long gradeId,
                                                            @Param("clazzId") Long clazzId,
                                                            @Param("week") Integer week,
                                                            @Param("registerDateFrom") Date registerDateFrom,
                                                            @Param("registerDateTo") Date registerDateTo,
                                                            @Param("classifies") List<String> classifies,
                                                            @Param("orderByClause") String orderByClause,
                                                            @Param("stage") String stage);

    List<Map<String,Object>> searchNightStatisticsGroupByClazz(@Param("academicYearSemesterId") Long academicYearSemesterId,
                                                             @Param("gradeId") Long gradeId,
                                                             @Param("clazzId") Long clazzId,
                                                             @Param("week") Integer week,
                                                             @Param("registerDateFrom") Date registerDateFrom,
                                                             @Param("registerDateTo") Date registerDateTo,
                                                             @Param("classifies") List<String> classifies,
                                                             @Param("orderByClause") String orderByClause,
                                                             @Param("stage") String stage);

    Page<NightStudyAttendance> pageDetail(@Param("page") Page<NightStudyAttendance> page, @Param(Constants.WRAPPER) QueryWrapper<NightStudyAttendance> wrapper);
}
