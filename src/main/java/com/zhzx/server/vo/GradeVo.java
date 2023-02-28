package com.zhzx.server.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.zhzx.server.domain.DailyAttendance;
import com.zhzx.server.domain.Grade;
import com.zhzx.server.domain.NightStudyAttendance;
import com.zhzx.server.enums.YesNoEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class GradeVo{

    @ApiModelProperty(value = "学生数量")
    @TableField(exist = false)
    private Integer studentNum;

    @ApiModelProperty(value = "年级id")
    private Long gradeId;

    @ApiModelProperty(value = "年级")
    private String gradeName;

    @ApiModelProperty(value = "DailyAttendance")
    private Map<String,List<DailyAttendance>> dailyAttendanceMap;

    @ApiModelProperty(value = "DailyAttendance")
    private Map<String,List<NightStudyAttendance>> nightStudyAttendance;

    @ApiModelProperty(value = "DailyAttendance")
    private Map<String,List<NightStudyAttendance>> nightStudyAttendanceOne;

    @ApiModelProperty(value = "DailyAttendance")
    private Map<String,List<NightStudyAttendance>> nightStudyAttendanceTwo;

    private Integer illNum;

    private Integer thingsNum;

    private Integer dailyAbsenceNum;

    private Integer nightAbsenceOneNum;

    private Integer nightAbsenceTwoNum;

    private Integer accommodationNum;
}
