package com.zhzx.server.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhzx.server.domain.NightStudyDuty;
import com.zhzx.server.domain.Staff;
import com.zhzx.server.dto.TeacherDutyGradeTotalDto;
import com.zhzx.server.dto.TeacherDutyGradeTotalSubstitueDto;
import com.zhzx.server.enums.LeaderDutyTypeEnum;
import com.zhzx.server.enums.RoutineEnum;
import com.zhzx.server.rest.req.TeacherDutySubstituteParam;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TeacherDutyGradeTotalService {
    NightStudyDuty getCorrespondLeaderNightStudyDuty(Long schoolyardId, Date time);

    Map<String, Object> nightRoutine(Date time, RoutineEnum type);

    Object updateTeacherDuty(Long id);

    Object cancelTeacherDuty(Long teacherDutyId, Long teacherId);

    List<Staff> cancelTeacherList(Long teacherDutyId);

    List<TeacherDutyGradeTotalDto> updateTeacherDutyList(Date timeFrom, Date timeTo);

    Map<String, Object> nightRoutineCommentAndIncident(LeaderDutyTypeEnum dutyType, Date time);

    IPage<TeacherDutyGradeTotalSubstitueDto> searchMyLogPage(IPage<TeacherDutyGradeTotalSubstitueDto> page, TeacherDutySubstituteParam param, Boolean bool);
}
