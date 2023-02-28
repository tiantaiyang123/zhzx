/**
 * 项目：中华中学管理平台
 * 模型分组：成绩管理
 * 模型名称：成绩单表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zhzx.server.rest.res.ApiCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`dat_exam_score_report`")
@ApiModel(value = "ExamScoreReport", description = "成绩单表")
public class ExamScoreReport extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * ID系统自动生成
     */
    @ApiModelProperty(value = "ID系统自动生成")
    @TableField(exist = false)
    private List<ExamPublishRelation> examPublishRelationList;
    /**
     * 班级ID sys_clazz.id
     */
    @TableField(value = "clazz_id")
    @ApiModelProperty(value = "班级ID sys_clazz.id", required = true)
    private Long clazzId;
    /**
     * 学生ID sys_student.id
     */
    @TableField(value = "student_id")
    @ApiModelProperty(value = "学生ID sys_student.id", required = true)
    private Long studentId;

    @ApiModelProperty(value = "学年学期ID sys_academic_year_semester.id")
    @TableField(exist = false)
    private Student student;
    /**
     * 学科ID sys_subject.id
     */
    @TableField(value = "subject_id")
    @ApiModelProperty(value = "学科ID sys_subject.id", required = true)
    private Long subjectId;

    @ApiModelProperty(value = "学年学期ID sys_academic_year_semester.id")
    @TableField(exist = false)
    private Subject subject;
    /**
     * 平时成绩
     */
    @TableField(value = "usual_score")
    @ApiModelProperty(value = "平时成绩", required = true)
    private Long usualScore;
    /**
     * 期中成绩
     */
    @TableField(value = "mid_score")
    @ApiModelProperty(value = "期中成绩", required = true)
    private Long midScore;
    /**
     * 期末成绩
     */
    @TableField(value = "end_score")
    @ApiModelProperty(value = "期末成绩", required = true)
    private Long endScore;
    /**
     * 总评成绩
     */
    @TableField(value = "total_score")
    @ApiModelProperty(value = "总评成绩", required = true)
    private Long totalScore;
    /**
     * 操作人ID sys_user.id
     */
    @TableField(value = "editor_id")
    @ApiModelProperty(value = "操作人ID sys_user.id", required = true)
    private Long editorId;
    /**
     * 操作人 sys_user.real_name
     */
    @TableField(value = "editor_name")
    @ApiModelProperty(value = "操作人 sys_user.real_name", required = true)
    private String editorName;
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
    public ExamScoreReport setDefault() {
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
        if (this.getClazzId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "班级ID sys_clazz.id不能为空！");
            return false;
        }
        if (this.getStudentId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "学生ID sys_student.id不能为空！");
            return false;
        }
        if (this.getSubjectId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "学科ID sys_subject.id不能为空！");
            return false;
        }
        if (this.getUsualScore() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "平时成绩不能为空！");
            return false;
        }
        if (this.getMidScore() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "期中成绩不能为空！");
            return false;
        }
        if (this.getEndScore() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "期末成绩不能为空！");
            return false;
        }
        if (this.getTotalScore() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "总评成绩不能为空！");
            return false;
        }
        if (this.getEditorId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "操作人ID sys_user.id不能为空！");
            return false;
        }
        if (this.getEditorName() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "操作人 sys_user.real_name不能为空！");
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
