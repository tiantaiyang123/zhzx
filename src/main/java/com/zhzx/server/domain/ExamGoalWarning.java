/**
 * 项目：中华中学管理平台
 * 模型分组：成绩管理
 * 模型名称：考试分数预警表
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
@TableName("`dat_exam_goal_warning`")
@ApiModel(value = "ExamGoalWarning", description = "考试分数预警表")
public class ExamGoalWarning extends BaseDomain {
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
     * 目标id
     */
    @TableField(value = "goal_id")
    @ApiModelProperty(value = "目标id", required = true)
    private Long goalId;
    /**
     * 学科id,赋分90002，赋分分段100001
     */
    @TableField(value = "subject_id")
    @ApiModelProperty(value = "学科id,赋分90002，赋分分段100001", required = true)
    private Long subjectId;
    /**
     * 目标科目类型
     */
    @TableField(value = "subject_type")
    @ApiModelProperty(value = "目标科目类型", required = true)
    private GoalEnum subjectType;
    /**
     * 左边界分数
     */
    @TableField(value = "left_value")
    @ApiModelProperty(value = "左边界分数")
    private Integer leftValue;
    /**
     * 右边界分数
     */
    @TableField(value = "right_value")
    @ApiModelProperty(value = "右边界分数", required = true)
    private Integer rightValue;
    /**
     * 顺序
     */
    @TableField(value = "sort_order")
    @ApiModelProperty(value = "顺序", required = true)
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
    public ExamGoalWarning setDefault() {
        if (this.getSubjectType() == null) {
            this.setSubjectType(GoalEnum.SCIENCE);
        }
        if (this.getLeftValue() == null) {
            this.setLeftValue(0);
        }
        if (this.getRightValue() == null) {
            this.setRightValue(0);
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
        if (this.getGoalId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "目标id不能为空！");
            return false;
        }
        if (this.getSubjectId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "学科id,赋分90002，赋分分段100001不能为空！");
            return false;
        }
        if (this.getSubjectType() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "目标科目类型不能为空！");
            return false;
        }
        if (this.getRightValue() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "右边界分数不能为空！");
            return false;
        }
        if (this.getSortOrder() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "顺序不能为空！");
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
