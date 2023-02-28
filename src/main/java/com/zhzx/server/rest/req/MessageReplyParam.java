/**
 * 项目：中华中学管理平台
 * 模型分组：消息管理
 * 模型名称：消息回复表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest.req;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;
import com.zhzx.server.domain.MessageReply;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "消息回复表参数", description = "")
public class MessageReplyParam implements Serializable {
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
     * 接收人ID sys_staff.id
     */
    @ApiModelProperty(value = "接收人ID sys_staff.id")
    private Long receiverId;
    /**
     * 接收人 sys_staff.real_name
     */
    @ApiModelProperty(value = "接收人 sys_staff.real_name")
    private String receiverName;
    /**
     * 接收人微信名称
     */
    @ApiModelProperty(value = "接收人微信名称")
    private String receiverWxName;
    /**
     * 回复内容
     */
    @ApiModelProperty(value = "回复内容")
    private String replyContent;
    /**
     * 回复类型
     */
    @ApiModelProperty(value = "回复类型")
    private String replyType;
    /**
     * 回复时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "回复时间")
    private java.util.Date replyTime;
    /**
     * 回复时间 下限值(大于等于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "回复时间 下限值(大于等于)")
    private java.util.Date replyTimeFrom;
    /**
     * 回复时间 上限值(小于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "回复时间 上限值(小于)")
    private java.util.Date replyTimeTo;
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
    public QueryWrapper<MessageReply> toQueryWrapper() {
        QueryWrapper<MessageReply> wrapper = Wrappers.<MessageReply>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
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
        if (this.getReceiverWxName() != null) {
            if (this.getReceiverWxName().startsWith("%") && this.getReceiverWxName().endsWith("%")) {
                wrapper.like("receiver_wx_name", this.getReceiverWxName().substring(1, this.getReceiverWxName().length() - 1));
            } else if (this.getReceiverWxName().startsWith("%") && !this.getReceiverWxName().endsWith("%")) {
                wrapper.likeLeft("receiver_wx_name", this.getReceiverWxName().substring(1));
            } else if (this.getReceiverWxName().endsWith("%")) {
                wrapper.likeRight("receiver_wx_name", this.getReceiverWxName().substring(0, this.getReceiverWxName().length() - 1));
            } else {
                wrapper.eq("receiver_wx_name", this.getReceiverWxName());
            }
        }
        if (this.getReplyContent() != null) {
            if (this.getReplyContent().startsWith("%") && this.getReplyContent().endsWith("%")) {
                wrapper.like("reply_content", this.getReplyContent().substring(1, this.getReplyContent().length() - 1));
            } else if (this.getReplyContent().startsWith("%") && !this.getReplyContent().endsWith("%")) {
                wrapper.likeLeft("reply_content", this.getReplyContent().substring(1));
            } else if (this.getReplyContent().endsWith("%")) {
                wrapper.likeRight("reply_content", this.getReplyContent().substring(0, this.getReplyContent().length() - 1));
            } else {
                wrapper.eq("reply_content", this.getReplyContent());
            }
        }
        if (this.getReplyType() != null) {
            if (this.getReplyType().startsWith("%") && this.getReplyType().endsWith("%")) {
                wrapper.like("reply_type", this.getReplyType().substring(1, this.getReplyType().length() - 1));
            } else if (this.getReplyType().startsWith("%") && !this.getReplyType().endsWith("%")) {
                wrapper.likeLeft("reply_type", this.getReplyType().substring(1));
            } else if (this.getReplyType().endsWith("%")) {
                wrapper.likeRight("reply_type", this.getReplyType().substring(0, this.getReplyType().length() - 1));
            } else {
                wrapper.eq("reply_type", this.getReplyType());
            }
        }
        wrapper.eq(this.getReplyTime() != null, "reply_time", this.getReplyTime());
        wrapper.ge(this.getReplyTimeFrom() != null, "reply_time", this.getReplyTimeFrom());
        wrapper.lt(this.getReplyTimeTo() != null, "reply_time", this.getReplyTimeTo());
        wrapper.eq(this.getCreatedTime() != null, "created_time", this.getCreatedTime());
        wrapper.ge(this.getCreatedTimeFrom() != null, "created_time", this.getCreatedTimeFrom());
        wrapper.lt(this.getCreatedTimeTo() != null, "created_time", this.getCreatedTimeTo());
        wrapper.eq(this.getUpdatedTime() != null, "updated_time", this.getUpdatedTime());
        wrapper.ge(this.getUpdatedTimeFrom() != null, "updated_time", this.getUpdatedTimeFrom());
        wrapper.lt(this.getUpdatedTimeTo() != null, "updated_time", this.getUpdatedTimeTo());
        return wrapper;
    }

}
