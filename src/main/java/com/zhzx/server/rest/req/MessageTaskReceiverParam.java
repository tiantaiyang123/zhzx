/**
* 项目：中华中学管理平台
* 模型分组：消息管理
* 模型名称：消息发送人员表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
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
import com.zhzx.server.enums.ReceiverEnum;
import com.zhzx.server.domain.MessageTaskReceiver;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "消息发送人员表参数", description = "")
public class MessageTaskReceiverParam implements Serializable {
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
    public QueryWrapper<MessageTaskReceiver> toQueryWrapper() {
        QueryWrapper<MessageTaskReceiver> wrapper = Wrappers.<MessageTaskReceiver>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getMessageTaskId() != null, "message_task_id", this.getMessageTaskId());
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
        wrapper.eq(this.getCreatedTime() != null, "created_time", this.getCreatedTime());
        wrapper.ge(this.getCreatedTimeFrom() != null, "created_time", this.getCreatedTimeFrom());
        wrapper.lt(this.getCreatedTimeTo() != null, "created_time", this.getCreatedTimeTo());
        wrapper.eq(this.getUpdatedTime() != null, "updated_time", this.getUpdatedTime());
        wrapper.ge(this.getUpdatedTimeFrom() != null, "updated_time", this.getUpdatedTimeFrom());
        wrapper.lt(this.getUpdatedTimeTo() != null, "updated_time", this.getUpdatedTimeTo());
        return wrapper;
    }

}
