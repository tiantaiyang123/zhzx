package com.zhzx.server.dto;

import com.zhzx.server.domain.TeacherDuty;
import com.zhzx.server.domain.TeacherDutyClazz;
import lombok.Data;

import java.util.List;

@Data
public class TeacherDutyGradeTotalDto extends TeacherDuty {
    String schoolyardName;
    List<TeacherDutyClazz> teacherDutyGradeTotalClazzList;
    Long gradeId;
}
