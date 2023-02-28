package com.zhzx.server.dto;

import com.zhzx.server.domain.Comment;
import com.zhzx.server.domain.FunctionDepartment;
import com.zhzx.server.domain.TeacherDutySubstitute;
import com.zhzx.server.enums.LeaderDutyTypeEnum;
import com.zhzx.server.enums.TeacherDutyTypeEnum;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by A2 on 2022/1/11.
 */
@Data
public class TeacherDutySubstituteDto extends TeacherDutySubstitute {

    private String content;

    private String originalTeacher;

    private String teacherName;

    private Long leaderId;

    private Long leaderOldId;

    private Date time;

    private TeacherDutyTypeEnum dutyType;

    private LeaderDutyTypeEnum leaderDutyType;

    private Long leaderDutyId;

    private String clazz;

    private Long currentLeaderId;
}
