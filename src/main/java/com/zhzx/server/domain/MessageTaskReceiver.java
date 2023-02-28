/**
* 项目：中华中学管理平台
* 模型分组：消息管理
* 模型名称：消息发送人员表
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
import com.zhzx.server.enums.ReceiverEnum;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`mes_message_task_receiver`")
@ApiModel(value = "MessageTaskReceiver", description = "消息发送人员表")
public class MessageTaskReceiver extends BaseDomain {
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
    public MessageTaskReceiver setDefault() {
        if (this.getReceiverType() == null) {
            this.setReceiverType(ReceiverEnum.STUDENT);
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
