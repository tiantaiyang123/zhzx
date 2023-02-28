/**
 * 项目：中华中学管理平台
 * 模型分组：学生管理
 * 模型名称：因病缺课表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.domain;

import java.io.Serializable;

import com.zhzx.server.rest.res.ApiCode;
import lombok.*;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zhzx.server.domain.AcademicYearSemester;
import com.zhzx.server.domain.Clazz;
import com.zhzx.server.domain.Student;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`std_ill`")
@ApiModel(value = "Ill", description = "因病缺课表")
public class Ill extends BaseDomain {
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
     * 缺课天数
     */
    @TableField(value = "days")
    @ApiModelProperty(value = "缺课天数", required = true)
    private Integer days;
    /**
     * 因病缺课主要症状
     */
    @TableField(value = "symptom")
    @ApiModelProperty(value = "因病缺课主要症状", required = true)
    private String symptom;
    /**
     * 因病缺课疾病名称
     */
    @TableField(value = "ill_name")
    @ApiModelProperty(value = "因病缺课疾病名称", required = true)
    private String illName;
    /**
     * 就医情况
     */
    @TableField(value = "treatment")
    @ApiModelProperty(value = "就医情况", required = true)
    private String treatment;
    /**
     * 非因病缺课原因
     */
    @TableField(value = "reason")
    @ApiModelProperty(value = "非因病缺课原因")
    private String reason;
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

    /**
     * 设置默认值
     */
    public Ill setDefault() {
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
            if (throwException) throw new ApiCode.ApiException(-1, "学生ID sys_clazz.id不能为空！");
            return false;
        }
        if (this.getDays() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "缺课天数不能为空！");
            return false;
        }
        if (this.getSymptom() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "因病缺课主要症状不能为空！");
            return false;
        }
        if (this.getIllName() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "因病缺课疾病名称不能为空！");
            return false;
        }
        if (this.getTreatment() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "就医情况不能为空！");
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
