/**
 * 项目：中华中学管理平台
 * 模型分组：学生管理
 * 模型名称：日常考勤表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhzx.server.domain.DailyAttendance;
import com.zhzx.server.domain.DailyAttendanceSub;
import com.zhzx.server.repository.base.DailyAttendanceBaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface DailyAttendanceMapper extends DailyAttendanceBaseMapper {

    IPage<DailyAttendance> searchDailyAttendance(IPage<DailyAttendance> page,
                                                 @Param("schoolyardId") Long schoolyardId,
                                                 @Param("academicYearSemesterId") Long academicYearSemesterId,
                                                 @Param("gradeId") Long gradeId,
                                                 @Param("clazzId") Long clazzId,
                                                 @Param("week") Integer week,
                                                 @Param("registerDateFrom") String registerDateFrom,
                                                 @Param("registerDateTo") String registerDateTo,
                                                 @Param("orderByClause") String orderByClause);

    List<Map<String,Object>> searchStatisticsGroupByDate(@Param("schoolyardId") Long schoolyardId,
                                                           @Param("academicYearSemesterId") Long academicYearSemesterId,
                                                           @Param("gradeId") Long gradeId,
                                                           @Param("clazzId") Long clazzId,
                                                           @Param("week") Integer week,
                                                           @Param("classifies")List<String> classifies,
                                                           @Param("registerDateFrom") Date registerDateFrom,
                                                           @Param("registerDateTo") Date registerDateTo,
                                                           @Param("orderByClause") String orderByClause);

    List<Map<String,Object>> searchStatisticsGroupByClazz(@Param("schoolyardId") Long schoolyardId,
                                                        @Param("academicYearSemesterId") Long academicYearSemesterId,
                                                        @Param("gradeId") Long gradeId,
                                                        @Param("clazzId") Long clazzId,
                                                        @Param("week") Integer week,
                                                        @Param("classifies")List<String> classifies,
                                                        @Param("registerDateFrom") Date registerDateFrom,
                                                        @Param("registerDateTo") Date registerDateTo,
                                                        @Param("orderByClause") String orderByClause);

    Map<String,Object> isAbnormal(@Param("time") Date time,@Param("gradeId") Long gradeId,@Param("academicYearSemesterId") Long academicYearSemesterId);

    DailyAttendanceSub selectDetailById(@Param("id") Long id);

    IPage<DailyAttendanceSub> commonQuery(IPage<DailyAttendanceSub> page,
                                          @Param("schoolyardId") Long schoolyardId,
                                          @Param("academicYearSemesterId") Long academicYearSemesterId,
                                          @Param("gradeId") Long gradeId,
                                          @Param("clazzId") Long clazzId,
                                          @Param("week") Integer week,
                                          @Param("registerDateFrom") String registerDateFrom,
                                          @Param("registerDateTo") String registerDateTo);
}
