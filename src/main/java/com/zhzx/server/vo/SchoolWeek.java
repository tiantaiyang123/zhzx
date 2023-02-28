package com.zhzx.server.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class SchoolWeek {
    @ApiModelProperty(value = "学年学期ID")
    private Long academicYearSemesterId;

    @ApiModelProperty(value = "周数")
    private int week;

    @ApiModelProperty(value = "开始时间")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endTime;
}
