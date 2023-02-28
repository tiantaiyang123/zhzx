/**
 * 项目：中华中学管理平台
 * 模型分组：学生管理
 * 模型名称：晚自习考勤表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
 */

package com.zhzx.server.domain;

import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.rest.res.ApiCode;
import lombok.*;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import com.zhzx.server.enums.StudentNightDutyTypeEnum;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`std_night_study_attendance`")
@ApiModel(value = "NightStudyAttendance", description = "晚自习考勤表")
public class NightStudyAttendance extends BaseDomain {
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
     * 自习阶段
     */
    @TableField(value = "stage")
    @ApiModelProperty(value = "自习阶段", required = true)
    private StudentNightDutyTypeEnum stage;
    /**
     * 应到人数
     */
    @TableField(value = "should_num")
    @ApiModelProperty(value = "应到人数", required = true)
    private Integer shouldNum;
    /**
     * 实到人数
     */
    @TableField(value = "actual_num")
    @ApiModelProperty(value = "实到人数", required = true)
    private Integer actualNum;
    /**
     * 分类 sys_label.classify=WZXKQ
     */
    @TableField(value = "classify")
    @ApiModelProperty(value = "分类 sys_label.classify=WZXKQ", required = true)
    private String classify;
    /**
     * 原因
     */
    @TableField(value = "reason")
    @ApiModelProperty(value = "原因")
    private String reason;
    /**
     * 值日班长总结
     */
    @TableField(value = "summarize")
    @ApiModelProperty(value = "值日班长总结")
    private String summarize;
    /**
     * 值日班长签名
     */
    @TableField(value = "sign")
    @ApiModelProperty(value = "值日班长签名", required = true)
    private String sign;
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

    @ApiModelProperty(value = "是否全勤 [YES.是 NO.否]")
    @TableField(exist = false)
    private YesNoEnum isFullAttendence;

    /**
     * 设置默认值
     */
    public NightStudyAttendance setDefault() {
        if (this.getShouldNum() == null) {
            this.setShouldNum(0);
        }
        if (this.getActualNum() == null) {
            this.setActualNum(0);
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
        if (this.getStage() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "自习阶段不能为空！");
            return false;
        }
        if (this.getShouldNum() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "应到人数不能为空！");
            return false;
        }
        if (this.getActualNum() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "实到人数不能为空！");
            return false;
        }
        if (this.getClassify() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "分类 sys_label.classify=WZXKQ不能为空！");
            return false;
        }
        if (this.getSign() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "值日班长签名不能为空！");
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
