/**
 * 项目：中华中学管理平台
 * 模型分组：消息管理
 * 模型名称：消息回复表
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

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`mes_message_reply`")
@ApiModel(value = "MessageReply", description = "消息回复表")
public class MessageReply extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * 接收人ID sys_staff.id
     */
    @TableField(value = "receiver_id")
    @ApiModelProperty(value = "接收人ID sys_staff.id")
    private Long receiverId;
    /**
     * 接收人 sys_staff.real_name
     */
    @TableField(value = "receiver_name")
    @ApiModelProperty(value = "接收人 sys_staff.real_name")
    private String receiverName;
    /**
     * 接收人微信名称
     */
    @TableField(value = "receiver_wx_name")
    @ApiModelProperty(value = "接收人微信名称", required = true)
    private String receiverWxName;
    /**
     * 回复内容
     */
    @TableField(value = "reply_content")
    @ApiModelProperty(value = "回复内容", required = true)
    private String replyContent;
    /**
     * 回复类型
     */
    @TableField(value = "reply_type")
    @ApiModelProperty(value = "回复类型", required = true)
    private String replyType;
    /**
     * 回复时间
     */
    @TableField(value = "reply_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "回复时间", required = true)
    private java.util.Date replyTime;
    /**
     * 
     */
    @TableField(value = "created_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "", required = true)
    private java.util.Date createdTime;
    /**
     * 
     */
    @TableField(value = "updated_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "", required = true)
    private java.util.Date updatedTime;

    /**
     * 设置默认值
     */
    public MessageReply setDefault() {
        if (this.getReplyTime() == null) {
            this.setReplyTime(new java.util.Date());
        }
        if (this.getCreatedTime() == null) {
            this.setCreatedTime(new java.util.Date());
        }
        if (this.getUpdatedTime() == null) {
            this.setUpdatedTime(new java.util.Date());
        }
        return this;
    }

    /**
     * 数据验证
     */
    public Boolean validate(Boolean throwException) {
        if (this.getReceiverWxName() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "接收人微信名称不能为空！");
            return false;
        }
        if (this.getReplyContent() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "回复内容不能为空！");
            return false;
        }
        if (this.getReplyType() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "回复类型不能为空！");
            return false;
        }
        if (this.getReplyTime() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "回复时间不能为空！");
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
