/**
 * 项目：中华中学管理平台
 * 模型分组：成绩管理
 * 模型名称：考试目标表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zhzx.server.enums.GoalEnum;
import com.zhzx.server.enums.SettingEnum;
import com.zhzx.server.enums.TargetEnum;
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
@TableName("`dat_exam_goal`")
@ApiModel(value = "ExamGoal", description = "考试目标表")
public class ExamGoal extends BaseDomain {
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
     * 目标名称
     */
    @TableField(value = "name")
    @ApiModelProperty(value = "目标名称", required = true)
    private String name;
    /**
     * 设置方式
     */
    @TableField(value = "setting_type")
    @ApiModelProperty(value = "设置方式", required = true)
    private SettingEnum settingType;
    /**
     * 目标类型
     */
    @TableField(value = "target_type")
    @ApiModelProperty(value = "目标类型", required = true)
    private TargetEnum targetType;
    /**
     * 目标科目类型
     */
    @TableField(value = "subject_type")
    @ApiModelProperty(value = "目标科目类型", required = true)
    private GoalEnum subjectType;
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
     * 手动输入的各班级目标参考，用json格式
     */
    @TableField(value = "goal_reference", select = false)
    @ApiModelProperty(value = "手动输入的各班级目标参考，用json格式", required = true)
    private String goalReference;
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
    public ExamGoal setDefault() {
        if (this.getGoalReference() == null) {
            this.setGoalReference("");
        }
        if (this.getSettingType() == null) {
            this.setSettingType(SettingEnum.SCORE);
        }
        if (this.getTargetType() == null) {
            this.setTargetType(TargetEnum.YSF);
        }
        if (this.getSubjectType() == null) {
            this.setSubjectType(GoalEnum.SCIENCE);
        }
        if (this.getGoalValue() == null) {
            this.setGoalValue(0);
        }
        if (this.getSubjectValue() == null) {
            this.setSubjectValue(0);
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
        if (this.getName() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "目标名称不能为空！");
            return false;
        }
        if (this.getSettingType() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "设置方式不能为空！");
            return false;
        }
        if (this.getTargetType() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "目标类型不能为空！");
            return false;
        }
        if (this.getSubjectType() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "目标科目类型不能为空！");
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
        if (this.getGoalReference() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "手动输入的各班级目标参考，用json格式不能为空！");
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
