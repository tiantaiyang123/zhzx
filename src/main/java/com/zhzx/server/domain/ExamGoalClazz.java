/**
 * 项目：中华中学管理平台
 * 模型分组：成绩管理
 * 模型名称：考试目标班级表
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
import com.zhzx.server.domain.ExamGoal;
import com.zhzx.server.domain.Clazz;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`dat_exam_goal_clazz`")
@ApiModel(value = "ExamGoalClazz", description = "考试目标班级表")
public class ExamGoalClazz extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * dat_exam_goal.id
     */
    @TableField(value = "exam_goal_id")
    @ApiModelProperty(value = "dat_exam_goal.id", required = true)
    private Long examGoalId;
    /**
     * dat_exam_goal.id
     */
    @ApiModelProperty(value = "dat_exam_goal.id")
    @TableField(exist = false)
    private ExamGoal examGoal;
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
     * 总分目标值
     */
    @TableField(value = "goal_value")
    @ApiModelProperty(value = "总分目标值", required = true)
    private Integer goalValue;
    /**
     * 单科目标值
     */
    @TableField(value = "subject_value")
    @ApiModelProperty(value = "单科目标值", required = true)
    private Integer subjectValue;

    /**
     * 设置默认值
     */
    public ExamGoalClazz setDefault() {
        if (this.getGoalValue() == null) {
            this.setGoalValue(0);
        }
        if (this.getSubjectValue() == null) {
            this.setSubjectValue(0);
        }
        return this;
    }

    /**
     * 数据验证
     */
    public Boolean validate(Boolean throwException) {
        if (this.getExamGoalId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "dat_exam_goal.id不能为空！");
            return false;
        }
        if (this.getClazzId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "班级ID sys_clazz.id不能为空！");
            return false;
        }
        if (this.getGoalValue() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "总分目标值不能为空！");
            return false;
        }
        if (this.getSubjectValue() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "单科目标值不能为空！");
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
