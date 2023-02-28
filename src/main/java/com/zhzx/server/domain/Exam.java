/**
* 项目：中华中学管理平台
* 模型分组：成绩管理
* 模型名称：考试表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zhzx.server.enums.YesNoEnum;
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
@TableName("`dat_exam`")
@ApiModel(value = "Exam", description = "考试表")
public class Exam extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * 校区ID sys_schoolyard.id
     */
    @TableField(value = "schoolyard_id")
    @ApiModelProperty(value = "校区ID sys_schoolyard.id", required = true)
    private Long schoolyardId;
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
     * 年级ID sys_grade.id
     */
    @TableField(value = "grade_id")
    @ApiModelProperty(value = "年级ID sys_grade.id", required = true)
    private Long gradeId;
    /**
     * 年级ID sys_grade.id
     */
    @ApiModelProperty(value = "年级ID sys_grade.id")
    @TableField(exist = false)
    private Grade grade;
    /**
     * 目标模板ID dat_exam_goal_template.id
     */
    @TableField(value = "exam_goal_template_id")
    @ApiModelProperty(value = "目标模板ID dat_exam_goal_template.id", required = true)
    private Long examGoalTemplateId;
    /**
     * 目标模板ID dat_exam_goal_template.id
     */
    @ApiModelProperty(value = "目标模板ID dat_exam_goal_template.id")
    @TableField(exist = false)
    private ExamGoalTemplate examGoalTemplate;
    /**
     * 考试名称
     */
    @TableField(value = "name")
    @ApiModelProperty(value = "考试名称", required = true)
    private String name;
    /**
     * 考试分类ID dat_exam_type.id
     */
    @TableField(value = "exam_type_id")
    @ApiModelProperty(value = "考试分类ID dat_exam_type.id", required = true)
    private Long examTypeId;
    /**
     * 考试分类ID dat_exam_type.id
     */
    @ApiModelProperty(value = "考试分类ID dat_exam_type.id")
    @TableField(exist = false)
    private ExamType examType;
    /**
     * 考试科目 多选 用[,]分割 sys_subject.name
     */
    @TableField(value = "exam_subjects")
    @ApiModelProperty(value = "考试科目 多选 用[,]分割 sys_subject.name", required = true)
    private String examSubjects;
    /**
     * 考试开始日期
     */
    @TableField(value = "exam_start_date")
    @ApiModelProperty(value = "考试开始日期", required = true)
    private String examStartDate;
    /**
     * 考试结束日期
     */
    @TableField(value = "exam_end_date")
    @ApiModelProperty(value = "考试结束日期", required = true)
    private String examEndDate;
    /**
     * 学业等级更新日期，对应grade表同名字段
     */
    @TableField(value = "academy_ratio_update_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "学业等级更新日期，对应grade表同名字段", required = true)
    private java.util.Date academyRatioUpdateTime;
    /**
     * 是否发布
     */
    @TableField(value = "is_publish")
    @ApiModelProperty(value = "是否发布", required = true)
    private YesNoEnum isPublish;

    @TableField(value = "is_teacher_seen")
    @ApiModelProperty(value = "是否教师可见", required = true)
    private YesNoEnum isTeacherSeen;

    /**
     * 是否需要计算赋分 [YES.是 NO.否]
     */
    @TableField(value = "cal_natural_score")
    @ApiModelProperty(value = "是否需要计算赋分 [YES.是 NO.否]", required = true)
    private YesNoEnum calNaturalScore;
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
    public Exam setDefault() {
        if (this.getAcademyRatioUpdateTime() == null) {
            this.setAcademyRatioUpdateTime(new java.util.Date());
        }
        if (this.getIsPublish() == null) {
            this.setIsPublish(YesNoEnum.NO);
        }
        if (this.getIsTeacherSeen() == null) {
            this.setIsTeacherSeen(YesNoEnum.NO);
        }
        if (this.getCalNaturalScore() == null) {
            this.setCalNaturalScore(YesNoEnum.NO);
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
        if (this.getSchoolyardId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "schoolyard_id不能为空！");
            return false;
        }
        if (this.getAcademicYearSemesterId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "academic_year_semester_id不能为空！");
            return false;
        }
        if (this.getGradeId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "grade_id不能为空！");
            return false;
        }
        if (this.getName() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "name不能为空！");
            return false;
        }
        if (this.getExamTypeId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "exam_type_id不能为空！");
            return false;
        }
        if (this.getExamSubjects() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "exam_subjects不能为空！");
            return false;
        }
        if (this.getExamStartDate() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "exam_start_date不能为空！");
            return false;
        }
        if (this.getExamEndDate() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "exam_end_date不能为空！");
            return false;
        }
        if (this.getAcademyRatioUpdateTime() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "academy_ratio_update_time不能为空！");
            return false;
        }
        if (this.getIsPublish() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "is_publish不能为空！");
            return false;
        }
        if (this.getCalNaturalScore() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "cal_natural_score不能为空！");
            return false;
        }
        if (this.getCreateTime() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "create_time不能为空！");
            return false;
        }
        if (this.getUpdateTime() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "update_time不能为空！");
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
