/**
 * 项目：中华中学管理平台
 * 模型分组：成绩管理
 * 模型名称：考试结果表
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
import com.zhzx.server.domain.Exam;
import com.zhzx.server.domain.Student;
import com.zhzx.server.domain.Clazz;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`dat_exam_result`")
@ApiModel(value = "ExamResult", description = "考试结果表")
public class ExamResult extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * dat_exam.id
     */
    @TableField(value = "exam_id")
    @ApiModelProperty(value = "dat_exam.id", required = true)
    private Long examId;
    /**
     * dat_exam.id
     */
    @ApiModelProperty(value = "dat_exam.id")
    @TableField(exist = false)
    private Exam exam;
    /**
     * sys_student.id
     */
    @TableField(value = "student_id")
    @ApiModelProperty(value = "sys_student.id", required = true)
    private Long studentId;
    /**
     * sys_student.id
     */
    @ApiModelProperty(value = "sys_student.id")
    @TableField(exist = false)
    private Student student;
    /**
     * sys_clazz.id
     */
    @TableField(value = "clazz_id")
    @ApiModelProperty(value = "sys_clazz.id", required = true)
    private Long clazzId;
    /**
     * sys_clazz.id
     */
    @ApiModelProperty(value = "sys_clazz.id")
    @TableField(exist = false)
    private Clazz clazz;
    /**
     * 原始分总分
     */
    @TableField(value = "total_score")
    @ApiModelProperty(value = "原始分总分", required = true)
    private java.math.BigDecimal totalScore;
    /**
     * 原始分赋分
     */
    @TableField(value = "total_weighted_score")
    @ApiModelProperty(value = "原始分赋分", required = true)
    private java.math.BigDecimal totalWeightedScore;
    /**
     * 语文成绩
     */
    @TableField(value = "chinese_score")
    @ApiModelProperty(value = "语文成绩", required = true)
    private java.math.BigDecimal chineseScore;
    /**
     * 数学成绩
     */
    @TableField(value = "math_score")
    @ApiModelProperty(value = "数学成绩", required = true)
    private java.math.BigDecimal mathScore;
    /**
     * 英语成绩
     */
    @TableField(value = "english_score")
    @ApiModelProperty(value = "英语成绩", required = true)
    private java.math.BigDecimal englishScore;
    /**
     * 物理成绩
     */
    @TableField(value = "physics_score")
    @ApiModelProperty(value = "物理成绩", required = true)
    private java.math.BigDecimal physicsScore;
    /**
     * 化学原始分
     */
    @TableField(value = "chemistry_score")
    @ApiModelProperty(value = "化学原始分", required = true)
    private java.math.BigDecimal chemistryScore;
    /**
     * 化学赋分
     */
    @TableField(value = "chemistry_weighted_score")
    @ApiModelProperty(value = "化学赋分", required = true)
    private java.math.BigDecimal chemistryWeightedScore;
    /**
     * 生物原始分
     */
    @TableField(value = "biology_score")
    @ApiModelProperty(value = "生物原始分", required = true)
    private java.math.BigDecimal biologyScore;
    /**
     * 生物赋分
     */
    @TableField(value = "biology_weighted_score")
    @ApiModelProperty(value = "生物赋分", required = true)
    private java.math.BigDecimal biologyWeightedScore;
    /**
     * 历史成绩
     */
    @TableField(value = "history_score")
    @ApiModelProperty(value = "历史成绩", required = true)
    private java.math.BigDecimal historyScore;
    /**
     * 政治原始分
     */
    @TableField(value = "politics_score")
    @ApiModelProperty(value = "政治原始分", required = true)
    private java.math.BigDecimal politicsScore;
    /**
     * 政治赋分
     */
    @TableField(value = "politics_weighted_score")
    @ApiModelProperty(value = "政治赋分", required = true)
    private java.math.BigDecimal politicsWeightedScore;
    /**
     * 地理原始分
     */
    @TableField(value = "geography_score")
    @ApiModelProperty(value = "地理原始分", required = true)
    private java.math.BigDecimal geographyScore;
    /**
     * 地理赋分
     */
    @TableField(value = "geography_weighted_score")
    @ApiModelProperty(value = "地理赋分", required = true)
    private java.math.BigDecimal geographyWeightedScore;
    /**
     * 班级排名
     */
    @TableField(value = "clazz_rank")
    @ApiModelProperty(value = "班级排名", required = true)
    private Integer clazzRank;
    /**
     * 年级排名
     */
    @TableField(value = "grade_rank")
    @ApiModelProperty(value = "年级排名", required = true)
    private Integer gradeRank;
    /**
     * 其他成绩信息
     */
    @TableField(value = "other", select = false)
    @ApiModelProperty(value = "其他成绩信息")
    private String other;
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
     * 副科分数
     */
    @TableField(exist = false)
    private ExamResultMinor examResultMinor;

    /**
     * 设置默认值
     */
    public ExamResult setDefault() {
        if (this.getClazzRank() == null) {
            this.setClazzRank(0);
        }
        if (this.getGradeRank() == null) {
            this.setGradeRank(0);
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
        if (this.getExamId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "dat_exam.id不能为空！");
            return false;
        }
        if (this.getStudentId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "sys_student.id不能为空！");
            return false;
        }
        if (this.getClazzId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "sys_clazz.id不能为空！");
            return false;
        }
        if (this.getTotalScore() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "原始分总分不能为空！");
            return false;
        }
        if (this.getTotalWeightedScore() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "原始分赋分不能为空！");
            return false;
        }
        if (this.getChineseScore() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "语文成绩不能为空！");
            return false;
        }
        if (this.getMathScore() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "数学成绩不能为空！");
            return false;
        }
        if (this.getEnglishScore() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "英语成绩不能为空！");
            return false;
        }
        if (this.getPhysicsScore() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "物理成绩不能为空！");
            return false;
        }
        if (this.getChemistryScore() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "化学原始分不能为空！");
            return false;
        }
        if (this.getChemistryWeightedScore() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "化学赋分不能为空！");
            return false;
        }
        if (this.getBiologyScore() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "生物原始分不能为空！");
            return false;
        }
        if (this.getBiologyWeightedScore() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "生物赋分不能为空！");
            return false;
        }
        if (this.getHistoryScore() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "历史成绩不能为空！");
            return false;
        }
        if (this.getPoliticsScore() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "政治原始分不能为空！");
            return false;
        }
        if (this.getPoliticsWeightedScore() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "政治赋分不能为空！");
            return false;
        }
        if (this.getGeographyScore() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "地理原始分不能为空！");
            return false;
        }
        if (this.getGeographyWeightedScore() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "地理赋分不能为空！");
            return false;
        }
        if (this.getClazzRank() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "班级排名不能为空！");
            return false;
        }
        if (this.getGradeRank() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "年级排名不能为空！");
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
