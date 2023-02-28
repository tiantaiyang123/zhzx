package com.zhzx.server.dto;

import com.zhzx.server.domain.Incident;
import com.zhzx.server.domain.TeacherDuty;
import lombok.Data;

import java.util.List;

/**
 * Created by A2 on 2022/1/11.
 */
@Data
public class TeacherDutyDto extends TeacherDuty{

    //偶发时间情况
    private List<Incident> incidentList;

    //意见与建议表
    private List<CommentDto> comment;

    //值班老师
    private String teacherName;

    //年级总值班及总值班

    private String gradeOneTeacher;
    private String gradeTwoTeacher;
    private String gradeThreeTeacher;
    private String totalDutyTeacher;

    private String gradeName;
    private String schoolyardName;

}
