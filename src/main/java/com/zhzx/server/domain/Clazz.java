/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：班级表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zhzx.server.enums.ClazzNatureEnum;
import com.zhzx.server.rest.res.ApiCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`sys_clazz`")
@ApiModel(value = "Clazz", description = "班级表")
public class Clazz extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * 学年学期 sys_academic_year_semester.id
     */
    @TableField(value = "academic_year_semester_id")
    @ApiModelProperty(value = "学年学期 sys_academic_year_semester.id", required = true)
    private Long academicYearSemesterId;
    /**
     * 学年学期 sys_academic_year_semester.id
     */
    @ApiModelProperty(value = "学年学期 sys_academic_year_semester.id")
    @TableField(exist = false)
    private AcademicYearSemester academicYearSemester;
    /**
     * 学年学期 sys_academic_year_semester.id
     */
    @ApiModelProperty(value = "科目简称")
    @TableField(exist = false)
    private String simpleDivision;
    /**
     * 年级 sys_grade.id
     */
    @TableField(value = "schoolyard_id")
    @ApiModelProperty(value = "校区 sys_schoolyard.id", required = true)
    private Long schoolyardId;
    /**
     * 年级 sys_grade.id
     */
    @ApiModelProperty(value = "校区 sys_schoolyard.id")
    @TableField(exist = false)
    private Schoolyard schoolyard;
    /**
     * 年级 sys_grade.id
     */
    @TableField(value = "grade_id")
    @ApiModelProperty(value = "年级 sys_grade.id", required = true)
    private Long gradeId;
    /**
     * 年级 sys_grade.id
     */
    @ApiModelProperty(value = "年级 sys_grade.id")
    @TableField(exist = false)
    private Grade grade;
    /**
     * 名称 sys_label.classify=CLAZZ
     */
    @TableField(value = "name")
    @ApiModelProperty(value = "名称 sys_label.classify=CLAZZ", required = true)
    private String name;
    /**
     * 学生人数
     */
    @TableField(value = "student_count")
    @ApiModelProperty(value = "学生人数", required = true)
    private Integer studentCount;
    /**
     * 入学年份
     */
    @TableField(value = "start_year")
    @ApiModelProperty(value = "入学年份", required = true)
    private Integer startYear;
    /**
     * 班主任 sys_staff.name
     */
    @TableField(value = "head_teacher")
    @ApiModelProperty(value = "班主任 sys_staff.name", required = true)
    private String headTeacher;
    /**
     * 班级性质
     */
    @TableField(value = "clazz_nature")
    @ApiModelProperty(value = "班级性质", required = true)
    private ClazzNatureEnum clazzNature;
    /**
     * 班级分科 多选 用[,]分割 sys_subject.id
     */
    @TableField(value = "clazz_division")
    @ApiModelProperty(value = "班级分科 多选 用[,]分割 sys_subject.id", required = true)
    private String clazzDivision;
    /**
     * 学科水平科目 多选 用[,]分割 sys_subject.id
     */
    @TableField(value = "subject_level")
    @ApiModelProperty(value = "学科水平科目 多选 用[,]分割 sys_subject.id")
    private String subjectLevel;
    /**
     * 班级层次
     */
    @TableField(value = "clazz_level")
    @ApiModelProperty(value = "班级层次", required = true)
    private String clazzLevel;
    /**
     * 晚一上课人数
     */
    @TableField(value = "night_stage_one_num")
    @ApiModelProperty(value = "晚一上课人数")
    private Long nightStageOneNum;
    /**
     * 晚二上课人数
     */
    @TableField(value = "night_stage_two_num")
    @ApiModelProperty(value = "晚二上课人数")
    private Long nightStageTwoNum;
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
    public Clazz setDefault() {
        if (this.getStudentCount() == null) {
            this.setStudentCount(0);
        }
        if (this.getStartYear() == null) {
            this.setStartYear(0);
        }
        if (this.getClazzNature() == null) {
            this.setClazzNature(ClazzNatureEnum.SCIENCE);
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
            if (throwException) throw new ApiCode.ApiException(-1, "学年学期 sys_academic_year_semester.id不能为空！");
            return false;
        }
        if (this.getSchoolyardId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "校区 sys_schoolyard.id不能为空！");
            return false;
        }
        if (this.getGradeId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "年级 sys_grade.id不能为空！");
            return false;
        }
        if (this.getName() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "名称 sys_label.classify=CLAZZ不能为空！");
            return false;
        }
        if (this.getStudentCount() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "学生人数不能为空！");
            return false;
        }
        if (this.getStartYear() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "入学年份不能为空！");
            return false;
        }
        if (this.getHeadTeacher() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "班主任 sys_staff.name不能为空！");
            return false;
        }
        if (this.getClazzNature() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "班级性质不能为空！");
            return false;
        }
        if (this.getClazzDivision() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "班级分科 多选 用[,]分割 sys_subject.id不能为空！");
            return false;
        }
        if (this.getClazzLevel() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "班级层次不能为空！");
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
