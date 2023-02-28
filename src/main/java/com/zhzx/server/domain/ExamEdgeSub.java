/**
 * 项目：中华中学管理平台
 * 模型分组：成绩管理
 * 模型名称：考试临界生分数段设置表
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

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`dat_exam_edge_sub`")
@ApiModel(value = "ExamEdgeSub", description = "考试临界生分数段设置表")
public class ExamEdgeSub extends BaseDomain {
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
     * 学科id,原始分分段90001，赋分分段100001，三总90003，四总90004
     */
    @TableField(value = "subject_id")
    @ApiModelProperty(value = "学科id,原始分分段90001，赋分分段100001", required = true)
    private Long subjectId;
    /**
     * 基准分数
     */
    @TableField(value = "base_value")
    @ApiModelProperty(value = "基准分数", required = true)
    private Integer baseValue;
    /**
     * 浮动分数
     */
    @TableField(value = "float_value")
    @ApiModelProperty(value = "浮动分数", required = true)
    private Integer floatValue;
    /**
     * 上限分数
     */
    @TableField(value = "upper_value")
    @ApiModelProperty(value = "上限分数", required = true)
    private Integer upperValue;
    /**
     * 下限分数
     */
    @TableField(value = "lower_value")
    @ApiModelProperty(value = "下限分数", required = true)
    private Integer lowerValue;
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
    public ExamEdgeSub setDefault() {
        if (this.getBaseValue() == null) {
            this.setBaseValue(0);
        }
        if (this.getFloatValue() == null) {
            this.setFloatValue(0);
        }
        if (this.getUpperValue() == null) {
            this.setUpperValue(0);
        }
        if (this.getLowerValue() == null) {
            this.setLowerValue(0);
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
        if (this.getSubjectId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "学科id,原始分分段90001，赋分分段100001不能为空！");
            return false;
        }
        if (this.getBaseValue() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "基准分数不能为空！");
            return false;
        }
        if (this.getFloatValue() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "浮动分数不能为空！");
            return false;
        }
        if (this.getUpperValue() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "上限分数不能为空！");
            return false;
        }
        if (this.getLowerValue() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "下限分数不能为空！");
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
