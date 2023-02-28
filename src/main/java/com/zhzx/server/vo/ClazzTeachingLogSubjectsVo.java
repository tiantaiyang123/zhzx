package com.zhzx.server.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.zhzx.server.domain.AcademicYearSemester;
import com.zhzx.server.domain.Clazz;
import com.zhzx.server.domain.ClazzTeachingLogSubjects;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class ClazzTeachingLogSubjectsVo extends ClazzTeachingLogSubjects {

    @TableField(exist = false)
    @ApiModelProperty(value = "学年学期ID sys_academic_year_semester.id", required = true)
    private Long academicYearSemesterId;
    /**
     * 学年学期ID sys_academic_year_semester.id
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "学年学期ID sys_academic_year_semester.id")
    private AcademicYearSemester academicYearSemester;
    /**
     * 周数
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "周数", required = true)
    private Integer week;
    /**
     * 日期
     */
    @TableField(exist = false)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "日期", required = true)
    private java.util.Date registerDate;
    /**
     * 班级ID sys_clazz.id
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "班级ID sys_clazz.id", required = true)
    private Long clazzId;
    /**
     * 班级ID sys_clazz.id
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "班级ID sys_clazz.id")
    private Clazz clazz;
    /**
     * 特殊情况
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "特殊情况")
    private String remark;
    /**
     * 应到人数
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "应到人数", required = true)
    private Integer shouldNum;
    /**
     * 实到人数
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "实到人数", required = true)
    private Integer actualNum;
}
