/**
* 项目：中华中学管理平台
* 模型分组：消息管理
* 模型名称：消息表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.domain;

import java.io.Serializable;

import com.zhzx.server.enums.MessageSystemEnum;
import com.zhzx.server.enums.MessageTypeEnum;
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
import com.zhzx.server.enums.ReceiverEnum;
import com.zhzx.server.enums.YesNoEnum;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`mes_message`")
@ApiModel(value = "Message", description = "消息表")
public class Message extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * 任务id
     */
    @TableField(value = "message_task_id")
    @ApiModelProperty(value = "任务id", required = true)
    private Long messageTaskId;
    /**
     * 名称
     */
    @TableField(value = "name")
    @ApiModelProperty(value = "名称", required = true)
    private String name;
    /**
     * 标题
     */
    @TableField(value = "title")
    @ApiModelProperty(value = "标题", required = true)
    private String title;
    /**
     * 内容
     */
    @TableField(value = "content", select = false)
    @ApiModelProperty(value = "内容", required = true)
    private String content;
    /**
     * 发送人ID sys_user.id
     */
    @TableField(value = "sender_id")
    @ApiModelProperty(value = "发送人ID sys_user.id", required = true)
    private Long senderId;
    /**
     * 发送人 sys_user.real_name
     */
    @TableField(value = "sender_name")
    @ApiModelProperty(value = "发送人 sys_user.real_name", required = true)
    private String senderName;
    /**
     * 接收人ID sys_user.id
     */
    @TableField(value = "receiver_id")
    @ApiModelProperty(value = "接收人ID sys_user.id", required = true)
    private Long receiverId;
    /**
     * 接收人 sys_user.real_name
     */
    @TableField(value = "receiver_name")
    @ApiModelProperty(value = "接收人 sys_user.real_name", required = true)
    private String receiverName;
    /**
     * 接收人类型
     */
    @TableField(value = "receiver_type")
    @ApiModelProperty(value = "接收人类型", required = true)
    private ReceiverEnum receiverType;
    /**
     * 发送时间
     */
    @TableField(value = "send_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "发送时间", required = true)
    private java.util.Date sendTime;
    /**
     * 重发时间
     */
    @TableField(value = "repeat_send_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "重发时间")
    private java.util.Date repeatSendTime;
    /**
     * 是否已发送
     */
    @TableField(value = "is_send")
    @ApiModelProperty(value = "是否已发送", required = true)
    private YesNoEnum isSend;
    /**
     * 是否已读
     */
    @TableField(value = "is_read")
    @ApiModelProperty(value = "是否已读", required = true)
    private YesNoEnum isRead;
    /**
     * 是否已填
     */
    @TableField(value = "is_write")
    @ApiModelProperty(value = "是否已填", required = true)
    private YesNoEnum isWrite;
    /**
     * 是否需要填写
     */
    @TableField(value = "need_write")
    @ApiModelProperty(value = "是否需要填写", required = true)
    private YesNoEnum needWrite;
    /**
     * 发送次数
     */
    @TableField(value = "send_num")
    @ApiModelProperty(value = "发送次数", required = true)
    private Integer sendNum;
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
    public Message setDefault() {
        if (this.getReceiverType() == null) {
            this.setReceiverType(ReceiverEnum.STUDENT);
        }
        if (this.getIsSend() == null) {
            this.setIsSend(YesNoEnum.NO);
        }
        if (this.getIsRead() == null) {
            this.setIsRead(YesNoEnum.NO);
        }
        if (this.getIsWrite() == null) {
            this.setIsWrite(YesNoEnum.NO);
        }
        if (this.getNeedWrite() == null) {
            this.setNeedWrite(YesNoEnum.NO);
        }
        if (this.getSendNum() == null) {
            this.setSendNum(0);
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
        if (this.getMessageTaskId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "message_task_id不能为空！");
            return false;
        }
        if (this.getName() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "name不能为空！");
            return false;
        }
        if (this.getTitle() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "title不能为空！");
            return false;
        }
        if (this.getContent() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "content不能为空！");
            return false;
        }
        if (this.getSenderId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "sender_id不能为空！");
            return false;
        }
        if (this.getSenderName() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "sender_name不能为空！");
            return false;
        }
        if (this.getReceiverId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "receiver_id不能为空！");
            return false;
        }
        if (this.getReceiverName() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "receiver_name不能为空！");
            return false;
        }
        if (this.getReceiverType() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "receiver_type不能为空！");
            return false;
        }
        if (this.getSendTime() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "send_time不能为空！");
            return false;
        }
        if (this.getIsSend() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "is_send不能为空！");
            return false;
        }
        if (this.getIsRead() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "is_read不能为空！");
            return false;
        }
        if (this.getIsWrite() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "is_write不能为空！");
            return false;
        }
        if (this.getNeedWrite() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "need_write不能为空！");
            return false;
        }
        if (this.getSendNum() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "send_num不能为空！");
            return false;
        }
        if (this.getCreatedTime() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "created_time不能为空！");
            return false;
        }
        if (this.getUpdatedTime() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "updated_time不能为空！");
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
