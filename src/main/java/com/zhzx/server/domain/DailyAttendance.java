/**
 * 项目：中华中学管理平台
 * 模型分组：学生管理
 * 模型名称：日常考勤表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
 */

package com.zhzx.server.domain;


import com.zhzx.server.rest.res.ApiCode;
import lombok.*;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;


@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`std_daily_attendance`")
@ApiModel(value = "DailyAttendance", description = "日常考勤表")
public class DailyAttendance extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * 学年学期ID sys_academic_year_semester.id
     */
    @TableField(value = "academic_year_semester_id")
    @ApiModelProperty(value = "学年学期ID sys_academic_year_semester.id", required = true)
    private Long academicYearSemesterId;
    /**
     * 学年学期ID sys_academic_year_semester.id
     */
    @ApiModelProperty(value = "学年学期ID sys_academic_year_semester.id")
    @TableField(exist = false)
    private AcademicYearSemester academicYearSemester;
    /**
     * 周数
     */
    @TableField(value = "week")
    @ApiModelProperty(value = "周数", required = true)
    private Integer week;
    /**
     * 日期
     */
    @TableField(value = "register_date")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "日期", required = true)
    private java.util.Date registerDate;
    /**
     * 班级ID sys_clazz.id
     */
    @TableField(value = "clazz_id")
    @ApiModelProperty(value = "班级ID sys_clazz.id", required = true)
    private Long clazzId;
    /**
     * 班级ID sys_clazz.id
     */
    @ApiModelProperty(value = "班级ID sys_clazz.id")
    @TableField(exist = false)
    private Clazz clazz;
    /**
     * 学生ID sys_student.id
     */
    @TableField(value = "student_id")
    @ApiModelProperty(value = "学生ID sys_student.id", required = true)
    private Long studentId;
    /**
     * 学生ID sys_student.id
     */
    @ApiModelProperty(value = "学生ID sys_student.id")
    @TableField(exist = false)
    private Student student;
    /**
     * 分类 sys_label.classify=RCKQ
     */
    @TableField(value = "classify")
    @ApiModelProperty(value = "分类 sys_label.classify=RCKQ", required = true)
    private String classify;
    /**
     * 原因
     */
    @TableField(value = "reason")
    @ApiModelProperty(value = "原因")
    private String reason;
    /**
     * 所缺课时数
     */
    @TableField(value = "academic_hour")
    @ApiModelProperty(value = "所缺课时数", required = true)
    private Integer academicHour;
    /**
     *
     */
    @TableField(value = "create_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "", required = true)
    private java.util.Date createTime;
    /**
     *
     */
    @TableField(value = "update_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "", required = true)
    private java.util.Date updateTime;

    @TableField(exist = false)
    @ApiModelProperty(value = "")
    private String studentIds;

    /**
     * 设置默认值
     */
    public DailyAttendance setDefault() {
        if (this.getAcademicHour() == null) {
            this.setAcademicHour(0);
        }
        if (this.getCreateTime() == null) {
            this.setCreateTime(new java.util.Date());
        }
        if (this.getUpdateTime() == null) {
            this.setUpdateTime(new java.util.Date());
        }
        return this;
    }

    /**
     * 数据验证
     */
    public Boolean validate(Boolean throwException) {
        if (this.getAcademicYearSemesterId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "学年学期ID sys_academic_year_semester.id不能为空！");
            return false;
        }
        if (this.getWeek() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "周数不能为空！");
            return false;
        }
        if (this.getRegisterDate() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "日期不能为空！");
            return false;
        }
        if (this.getClazzId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "班级ID sys_clazz.id不能为空！");
            return false;
        }
        if (this.getStudentId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "学生ID sys_student.id不能为空！");
            return false;
        }
        if (this.getClassify() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "分类 sys_label.classify=RCKQ不能为空！");
            return false;
        }
        if (this.getAcademicHour() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "所缺课时数不能为空！");
            return false;
        }
        return true;
    }

    /**
     * 数据验证
     */
    public Boolean validate() {
        return this.validate(false);
    }

}
