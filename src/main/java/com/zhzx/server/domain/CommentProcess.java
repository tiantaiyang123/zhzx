/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：意见与建议推送表
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
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.enums.CommentProcessSourceEnum;
import com.zhzx.server.enums.CommentStateEnum;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`day_comment_process`")
@ApiModel(value = "CommentProcess", description = "意见与建议推送表")
public class CommentProcess extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * 意见与建议ID day_comment.id
     */
    @TableField(value = "comment_id")
    @ApiModelProperty(value = "意见与建议ID day_comment.id", required = true)
    private Long commentId;
    /**
     * 是否需要校长室批示
     */
    @TableField(value = "need_instruction")
    @ApiModelProperty(value = "是否需要校长室批示", required = true)
    private YesNoEnum needInstruction;
    /**
     * 校长室批示
     */
    @TableField(value = "instructions")
    @ApiModelProperty(value = "校长室批示")
    private String instructions;
    /**
     * 批示人
     */
    @TableField(value = "headmaster")
    @ApiModelProperty(value = "批示人", required = true)
    private String headmaster;
    /**
     * 来源
     */
    @TableField(value = "source")
    @ApiModelProperty(value = "来源", required = true)
    private CommentProcessSourceEnum source;
    /**
     * 状态
     */
    @TableField(value = "state")
    @ApiModelProperty(value = "状态", required = true)
    private CommentStateEnum state;
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
    public CommentProcess setDefault() {
        if (this.getState() == null) {
            this.setState(CommentStateEnum.TO_BE_PUSHED);
        }
        if (this.getNeedInstruction() == null) {
            this.setNeedInstruction(YesNoEnum.NO);
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
        if (this.getCommentId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "意见与建议ID day_comment.id不能为空！");
            return false;
        }
        if (this.getNeedInstruction() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "是否需要校长室批示不能为空！");
            return false;
        }
        if (this.getHeadmaster() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "批示人不能为空！");
            return false;
        }
        if (this.getSource() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "来源不能为空！");
            return false;
        }
        if (this.getState() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "状态不能为空！");
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
