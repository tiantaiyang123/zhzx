/**
 * 项目：中华中学管理平台
 * 模型分组：学生管理
 * 模型名称：晚自习考勤表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.NightStudyAttendance;
import com.zhzx.server.dto.NightDutyClassDto;
import com.zhzx.server.enums.TeacherDutyTypeEnum;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface NightStudyAttendanceService extends IService<NightStudyAttendance> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(NightStudyAttendance entity);

    IPage<NightStudyAttendance> searchNightStudyAttendance(IPage<NightStudyAttendance> page, Long schoolyardId, Long academicYearSemesterId, Long gradeId, Long clazzId, Integer week, String registerDateFrom, String registerDateTo, String orderByClause);

    List<Map<String,Object>> searchNightStatisticsGroupByTime(Long schoolyardId, Long academicYearSemesterId, Long gradeId, Long clazzId, Integer week,List<String> classifies,Date registerDateFrom, Date registerDateTo, String orderByClause,TeacherDutyTypeEnum dutyType);

    List<Map<String,Object>> searchNightStatisticsGroupByClazz(Long schoolyardId, Long academicYearSemesterId, Long gradeId, Long clazzId, Integer week,List<String> classifies,Date registerDateFrom, Date registerDateTo, String orderByClause,TeacherDutyTypeEnum dutyType);

    int saveMultiStudent(NightStudyAttendance entity, String studentIds);

    Object fullAttendance(NightDutyClassDto nightDutyClassDto, Integer week);

    Page<NightStudyAttendance> pageDetail(Page<NightStudyAttendance> page, QueryWrapper<NightStudyAttendance> wrapper);
}
