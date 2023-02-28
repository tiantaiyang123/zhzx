/**
 * 项目：中华中学管理平台
 * 模型分组：学生管理
 * 模型名称：日常考勤表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.DailyAttendance;
import com.zhzx.server.domain.DailyAttendanceSub;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface DailyAttendanceService extends IService<DailyAttendance> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(DailyAttendance entity);

    IPage<DailyAttendance> searchDailyAttendance(IPage<DailyAttendance> page, Long schoolyardId, Long academicYearSemesterId, Long gradeId, Long clazzId, Integer week, String registerDateFrom, String registerDateTo, String orderByClause);

    List<Map<String,Object>> searchStatistics(Long schoolyardId, Long academicYearSemesterId, Long gradeId, Long clazzId, Integer week,List<String> classifies,Date registerDateFrom, Date registerDateTo, String orderByClause);

    List<Map<String,Object>> searchStatisticsGroupByClazz(Long schoolyardId, Long academicYearSemesterId, Long gradeId, Long clazzId, Integer week,List<String> classifies,Date registerDateFrom, Date registerDateTo, String orderByClause);

    DailyAttendanceSub appSave(DailyAttendance entity);

    IPage<DailyAttendanceSub> commonQuery(IPage<DailyAttendanceSub> page, Long schoolyardId, Long academicYearSemesterId, Long gradeId, Long clazzId, Integer week, String registerDateFrom, String registerDateTo);

    Integer appDelete(Long id);

    XSSFWorkbook exportExcel(Long schoolyardId, Long academicYearSemesterId, Long gradeId, Long clazzId, Integer week, String registerDateFrom, String registerDateTo);
}
