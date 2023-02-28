package com.zhzx.server.dto;

import com.zhzx.server.domain.TeacherDutyClazz;
import lombok.Data;

import java.util.List;

@Data
public class TeacherDutyGradeTotalSubstitueDto extends TeacherDutySubstituteDto {
    List<TeacherDutyClazz> teacherDutyGradeTotalClazzList;
}
