/**
 * 项目：中华中学管理平台
 * 模型分组：成绩管理
 * 模型名称：考试发布子表
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

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`dat_exam_publish_relation`")
@ApiModel(value = "ExamPublishRelation", description = "考试发布子表")
public class ExamPublishRelation extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * 考试发布表ID dat_exam_publish.id
     */
    @TableField(value = "exam_publish_id")
    @ApiModelProperty(value = "考试发布表ID dat_exam_publish.id", required = true)
    private Long examPublishId;
    /**
     * 考试ID dat_exam.id
     */
    @TableField(value = "exam_id")
    @ApiModelProperty(value = "考试ID dat_exam.id", required = true)
    private Long examId;
    /**
     * 考试ID dat_exam.id
     */
    @ApiModelProperty(value = "考试ID dat_exam.id")
    @TableField(exist = false)
    private Exam exam;
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
    public ExamPublishRelation setDefault() {
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
        if (this.getExamPublishId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "考试发布表ID dat_exam_publish.id不能为空！");
            return false;
        }
        if (this.getExamId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "考试ID dat_exam.id不能为空！");
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
