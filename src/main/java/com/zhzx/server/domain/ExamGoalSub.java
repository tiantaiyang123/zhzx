/**
* 项目：中华中学管理平台
* 模型分组：成绩管理
* 模型名称：考试分段表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
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
import com.zhzx.server.enums.GoalEnum;
import com.zhzx.server.enums.ExamCompareEnum;
import com.zhzx.server.domain.Exam;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`dat_exam_goal_sub`")
@ApiModel(value = "ExamGoalSub", description = "考试分段表")
public class ExamGoalSub extends BaseDomain {
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
     * 学科id,原始分分段90001，赋分分段100001
     */
    @TableField(value = "subject_id")
    @ApiModelProperty(value = "学科id,原始分分段90001，赋分分段100001", required = true)
    private Long subjectId;
    /**
     * 目标科目类型
     */
    @TableField(value = "subject_type")
    @ApiModelProperty(value = "目标科目类型", required = true)
    private GoalEnum subjectType;
    /**
     * 分段逻辑
     */
    @TableField(value = "type")
    @ApiModelProperty(value = "分段逻辑", required = true)
    private ExamCompareEnum type;
    /**
     * 目标值
     */
    @TableField(value = "goal_value")
    @ApiModelProperty(value = "目标值", required = true)
    private Integer goalValue;
    /**
     * 分数段样式，用json格式
     */
    @TableField(value = "style_json")
    @ApiModelProperty(value = "分数段样式，用json格式", required = true)
    private String styleJson;
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
    public ExamGoalSub setDefault() {
        if (this.getSubjectType() == null) {
            this.setSubjectType(GoalEnum.SCIENCE);
        }
        if (this.getType() == null) {
            this.setType(ExamCompareEnum.GE);
        }
        if (this.getGoalValue() == null) {
            this.setGoalValue(0);
        }
        if (this.getStyleJson() == null) {
            this.setStyleJson("0");
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
            if (throwException) throw new ApiCode.ApiException(-1, "exam_id不能为空！");
            return false;
        }
        if (this.getSubjectId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "subject_id不能为空！");
            return false;
        }
        if (this.getSubjectType() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "subject_type不能为空！");
            return false;
        }
        if (this.getType() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "type不能为空！");
            return false;
        }
        if (this.getGoalValue() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "goal_value不能为空！");
            return false;
        }
        if (this.getStyleJson() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "style_json不能为空！");
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
