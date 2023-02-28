/**
* 项目：中华中学管理平台
* 模型分组：消息管理
* 模型名称：消息表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.rest.req;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhzx.server.domain.Message;
import com.zhzx.server.enums.ReceiverEnum;
import com.zhzx.server.enums.YesNoEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.List;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "消息表参数", description = "")
public class MessageParam implements Serializable {
    /**
     * ID系统自动生成
     */
    @ApiModelProperty(value = "ID系统自动生成")
    private Long id;
    /**
     * ID系统自动生成 IN值List
     */
    @ApiModelProperty(value = "ID系统自动生成 IN值List")
    private List<Long> idList;
    /**
     * 任务id
     */
    @ApiModelProperty(value = "任务id")
    private Long messageTaskId;
    /**
     * 任务id IN值List
     */
    @ApiModelProperty(value = "任务id IN值List")
    private List<Long> messageTaskIdList;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;
    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;
    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String content;
    /**
     * 发送人ID sys_user.id
     */
    @ApiModelProperty(value = "发送人ID sys_user.id")
    private Long senderId;
    /**
     * 发送人 sys_user.real_name
     */
    @ApiModelProperty(value = "发送人 sys_user.real_name")
    private String senderName;
    /**
     * 接收人ID sys_user.id
     */
    @ApiModelProperty(value = "接收人ID sys_user.id")
    private Long receiverId;
    /**
     * 接收人 sys_user.real_name
     */
    @ApiModelProperty(value = "接收人 sys_user.real_name")
    private String receiverName;
    /**
     * 接收人类型
     */
    @ApiModelProperty(value = "接收人类型")
    private ReceiverEnum receiverType;
    /**
     * 接收人类型 IN值List
     */
    @ApiModelProperty(value = "接收人类型 IN值List")
    private List<String> receiverTypeList;
    /**
     * 发送时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "发送时间")
    private java.util.Date sendTime;
    /**
     * 发送时间 下限值(大于等于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "发送时间 下限值(大于等于)")
    private java.util.Date sendTimeFrom;
    /**
     * 发送时间 上限值(小于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "发送时间 上限值(小于)")
    private java.util.Date sendTimeTo;
    /**
     * 是否已发送
     */
    @ApiModelProperty(value = "是否已发送")
    private YesNoEnum isSend;
    /**
     * 是否已发送 IN值List
     */
    @ApiModelProperty(value = "是否已发送 IN值List")
    private List<String> isSendList;
    /**
     * 是否已读
     */
    @ApiModelProperty(value = "是否已读")
    private YesNoEnum isRead;
    /**
     * 是否已读 IN值List
     */
    @ApiModelProperty(value = "是否已读 IN值List")
    private List<String> isReadList;
    /**
     * 是否已填
     */
    @ApiModelProperty(value = "是否已填")
    private YesNoEnum isWrite;
    /**
     * 是否已填 IN值List
     */
    @ApiModelProperty(value = "是否已填 IN值List")
    private List<String> isWriteList;
    /**
     * 
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "")
    private java.util.Date createdTime;
    /**
     *  下限值(大于等于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = " 下限值(大于等于)")
    private java.util.Date createdTimeFrom;
    /**
     *  上限值(小于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = " 上限值(小于)")
    private java.util.Date createdTimeTo;
    /**
     * 
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "")
    private java.util.Date updatedTime;
    /**
     *  下限值(大于等于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = " 下限值(大于等于)")
    private java.util.Date updatedTimeFrom;
    /**
     *  上限值(小于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = " 上限值(小于)")
    private java.util.Date updatedTimeTo;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<Message> toQueryWrapper() {
        QueryWrapper<Message> wrapper = Wrappers.<Message>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.in(this.getMessageTaskIdList() != null && this.getMessageTaskIdList().size() > 0, "message_task_id", this.getMessageTaskIdList());
        wrapper.eq(this.getMessageTaskId() != null, "message_task_id", this.getMessageTaskId());
        if (this.getName() != null) {
            if (this.getName().startsWith("%") && this.getName().endsWith("%")) {
                wrapper.like("name", this.getName().substring(1, this.getName().length() - 1));
            } else if (this.getName().startsWith("%") && !this.getName().endsWith("%")) {
                wrapper.likeLeft("name", this.getName().substring(1));
            } else if (this.getName().endsWith("%")) {
                wrapper.likeRight("name", this.getName().substring(0, this.getName().length() - 1));
            } else {
                wrapper.eq("name", this.getName());
            }
        }
        if (this.getTitle() != null) {
            if (this.getTitle().startsWith("%") && this.getTitle().endsWith("%")) {
                wrapper.like("title", this.getTitle().substring(1, this.getTitle().length() - 1));
            } else if (this.getTitle().startsWith("%") && !this.getTitle().endsWith("%")) {
                wrapper.likeLeft("title", this.getTitle().substring(1));
            } else if (this.getTitle().endsWith("%")) {
                wrapper.likeRight("title", this.getTitle().substring(0, this.getTitle().length() - 1));
            } else {
                wrapper.eq("title", this.getTitle());
            }
        }
        if (this.getContent() != null) {
            if (this.getContent().startsWith("%") && this.getContent().endsWith("%")) {
                wrapper.like("content", this.getContent().substring(1, this.getContent().length() - 1));
            } else if (this.getContent().startsWith("%") && !this.getContent().endsWith("%")) {
                wrapper.likeLeft("content", this.getContent().substring(1));
            } else if (this.getContent().endsWith("%")) {
                wrapper.likeRight("content", this.getContent().substring(0, this.getContent().length() - 1));
            } else {
                wrapper.eq("content", this.getContent());
            }
        }
        wrapper.eq(this.getSenderId() != null, "sender_id", this.getSenderId());
        if (this.getSenderName() != null) {
            if (this.getSenderName().startsWith("%") && this.getSenderName().endsWith("%")) {
                wrapper.like("sender_name", this.getSenderName().substring(1, this.getSenderName().length() - 1));
            } else if (this.getSenderName().startsWith("%") && !this.getSenderName().endsWith("%")) {
                wrapper.likeLeft("sender_name", this.getSenderName().substring(1));
            } else if (this.getSenderName().endsWith("%")) {
                wrapper.likeRight("sender_name", this.getSenderName().substring(0, this.getSenderName().length() - 1));
            } else {
                wrapper.eq("sender_name", this.getSenderName());
            }
        }
        wrapper.eq(this.getReceiverId() != null, "receiver_id", this.getReceiverId());
        if (this.getReceiverName() != null) {
            if (this.getReceiverName().startsWith("%") && this.getReceiverName().endsWith("%")) {
                wrapper.like("receiver_name", this.getReceiverName().substring(1, this.getReceiverName().length() - 1));
            } else if (this.getReceiverName().startsWith("%") && !this.getReceiverName().endsWith("%")) {
                wrapper.likeLeft("receiver_name", this.getReceiverName().substring(1));
            } else if (this.getReceiverName().endsWith("%")) {
                wrapper.likeRight("receiver_name", this.getReceiverName().substring(0, this.getReceiverName().length() - 1));
            } else {
                wrapper.eq("receiver_name", this.getReceiverName());
            }
        }
        wrapper.eq(this.getReceiverType() != null, "receiver_type", this.getReceiverType());
        wrapper.in(this.getReceiverTypeList() != null && this.getReceiverTypeList().size() > 0, "receiver_type", this.getReceiverTypeList());
        wrapper.eq(this.getSendTime() != null, "send_time", this.getSendTime());
        wrapper.ge(this.getSendTimeFrom() != null, "send_time", this.getSendTimeFrom());
        wrapper.lt(this.getSendTimeTo() != null, "send_time", this.getSendTimeTo());
        wrapper.eq(this.getIsSend() != null, "is_send", this.getIsSend());
        wrapper.in(this.getIsSendList() != null && this.getIsSendList().size() > 0, "is_send", this.getIsSendList());
        wrapper.eq(this.getIsRead() != null, "is_read", this.getIsRead());
        wrapper.in(this.getIsReadList() != null && this.getIsReadList().size() > 0, "is_read", this.getIsReadList());
        wrapper.eq(this.getIsWrite() != null, "is_write", this.getIsWrite());
        wrapper.in(this.getIsWriteList() != null && this.getIsWriteList().size() > 0, "is_write", this.getIsWriteList());
        wrapper.eq(this.getCreatedTime() != null, "created_time", this.getCreatedTime());
        wrapper.ge(this.getCreatedTimeFrom() != null, "created_time", this.getCreatedTimeFrom());
        wrapper.lt(this.getCreatedTimeTo() != null, "created_time", this.getCreatedTimeTo());
        wrapper.eq(this.getUpdatedTime() != null, "updated_time", this.getUpdatedTime());
        wrapper.ge(this.getUpdatedTimeFrom() != null, "updated_time", this.getUpdatedTimeFrom());
        wrapper.lt(this.getUpdatedTimeTo() != null, "updated_time", this.getUpdatedTimeTo());
        return wrapper;
    }

}
