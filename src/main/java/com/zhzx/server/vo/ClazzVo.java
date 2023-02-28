package com.zhzx.server.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.zhzx.server.domain.Clazz;
import com.zhzx.server.domain.DailyAttendance;
import com.zhzx.server.domain.NightStudyAttendance;
import com.zhzx.server.dto.LeaderNightStudyDutyDto;
import com.zhzx.server.enums.YesNoEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class ClazzVo extends Clazz {

    @ApiModelProperty(value = "第四门科目 文科:history 理科:physics 不分科:''")
    @TableField(exist = false)
    private String fourthSubject;

    @ApiModelProperty(value = "另两门科目 不分科:''")
    @TableField(exist = false)
    private String otherTwoSubejct;

    @ApiModelProperty(value = "所有科目")
    @TableField(exist = false)
    private String allSubjects;

    @ApiModelProperty(value = "当天值班老师名称")
    @TableField(exist = false)
    private String teacherDutyName;

    @ApiModelProperty(value = "当天值班老师id")
    @TableField(exist = false)
    private Long teacherDutyTeacherId;

    @ApiModelProperty(value = "所属年级")
    @TableField(exist = false)
    private String gradeName;

    @ApiModelProperty(value = "时间")
    @TableField(exist = false)
    private Date time;

    @ApiModelProperty(value = "第一阶段值班老师")
    @TableField(exist = false)
    private String stageOneTeacher;

    @ApiModelProperty(value = "第二阶段值班老师")
    @TableField(exist = false)
    private String stageTwoTeacher;

    @ApiModelProperty(value = "总值班")
    @TableField(exist = false)
    private String totalDutyTeacher;

    @ApiModelProperty(value = "年级总值班")
    @TableField(exist = false)
    private String gradeDutyTeacher;

    @ApiModelProperty(value = "班级学生数量")
    @TableField(exist = false)
    private Integer studentNum;

    @ApiModelProperty(value = "teacherLeaderConfirm")
    @TableField(exist = false)
    private String teacherLeaderConfirm;

    @ApiModelProperty(value = "isLeaderConfirm")
    @TableField(exist = false)
    private YesNoEnum isLeaderConfirm;

    @ApiModelProperty(value = "isConfirm")
    @TableField(exist = false)
    private YesNoEnum isConfirm;

    @TableField(exist = false)
    private List<DailyAttendance> dailyAttendanceList;

    @TableField(exist = false)
    private List<NightStudyAttendance> nightStudyAttendanceList;

    @ApiModelProperty(value = "DailyAttendance")
    @TableField(exist = false)
    private Map<String,List<DailyAttendance>> dailyAttendanceMap;

    @ApiModelProperty(value = "NightStudyAttendance")
    @TableField(exist = false)
    private Map<String,List<NightStudyAttendance>> nightStudyAttendanceMap;

    @ApiModelProperty(value = "NightStudyAttendanceOne")
    @TableField(exist = false)
    private Map<String,List<NightStudyAttendance>> nightStudyAttendanceMapOne;

    @ApiModelProperty(value = "NightStudyAttendanceTwo")
    @TableField(exist = false)
    private Map<String,List<NightStudyAttendance>> nightStudyAttendanceMapTwo;


    private Integer illNum;

    private Integer thingsNum;

    private Integer absenteeism;

    private Integer dailyAbsenceNum;

    private Integer nightAbsenceOneNum;

    private Integer nightAbsenceTwoNum;

    private Integer accommodationNum;

    private List<LeaderNightStudyDutyDto> leaderNightStudyDutyDtos;

    private String schoolyardName;
    private Long schoolyardId;
}
