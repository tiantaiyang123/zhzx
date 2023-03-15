/**
 * 项目：中华中学管理平台
 * 模型分组：成绩管理
 * 模型名称：目标模板详情表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zhzx.server.enums.GoalEnum;
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
@TableName("`dat_exam_goal_template_sub`")
@ApiModel(value = "ExamGoalTemplateSub", description = "目标模板详情表")
public class ExamGoalTemplateSub extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
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
     * 班级ID
     */
    @TableField(value = "clazz_id")
    @ApiModelProperty(value = "班级ID", required = true)
    private Long clazzId;
    /**
     * 班级
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "班级")
    private Clazz clazz;
    /**
     * 目标名称
     */
    @TableField(value = "goal_name")
    @ApiModelProperty(value = "目标名称", required = true)
    private String goalName;
    /**
     * 总分目标值
     */
    @TableField(value = "goal_value")
    @ApiModelProperty(value = "总分目标值", required = true)
    private Integer goalValue;
    /**
     * 排序
     */
    @TableField(value = "sort_order")
    @ApiModelProperty(value = "排序", required = true)
    private Integer sortOrder;
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
    public ExamGoalTemplateSub setDefault() {
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
        if (this.getExamGoalTemplateId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "目标模板ID dat_exam_goal_template.id不能为空！");
            return false;
        }
        if (this.getClazzId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "班级ID不能为空！");
            return false;
        }
        if (this.getGoalName() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "目标名称不能为空！");
            return false;
        }
        if (this.getGoalValue() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "总分目标值不能为空！");
            return false;
        }
        if (this.getSortOrder() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "总分目标值不能为空！");
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
