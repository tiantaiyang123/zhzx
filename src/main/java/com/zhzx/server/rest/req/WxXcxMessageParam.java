/**
 * 项目：中华中学管理平台
 * 模型分组：第三方数据管理
 * 模型名称：微信小程序通知表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest.req;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhzx.server.domain.WxXcxMessage;
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
@ApiModel(value = "微信小程序通知表参数", description = "")
public class WxXcxMessageParam implements Serializable {
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
     * 通知类型0消息1待办
     */
    @ApiModelProperty(value = "通知类型0消息1待办")
    private Integer messageType;
    /**
     * 消息类型0其他1收文2发文3阅文4请示5听课6请假7会议 
     */
    @ApiModelProperty(value = "消息类型0其他1收文2发文3阅文4请示5听课6请假7会议 ")
    private Integer type;
    /**
     * 接收人e办公登录名
     */
    @ApiModelProperty(value = "接收人e办公登录名")
    private String userLoginName;
    /**
     * 接收人手机号
     */
    @ApiModelProperty(value = "接收人手机号")
    private String userPhone;
    /**
     * 源系统消息ID
     */
    @ApiModelProperty(value = "源系统消息ID")
    private String sourceId;
    /**
     * 操作类型0新增1更新2删除
     */
    @ApiModelProperty(value = "操作类型0新增1更新2删除")
    private Integer operateType;
    /**
     * 消息标题
     */
    @ApiModelProperty(value = "消息标题")
    private String messageTitle;
    /**
     * 消息内容
     */
    @ApiModelProperty(value = "消息内容")
    private String messageContent;
    /**
     * 消息创建时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "消息创建时间")
    private java.util.Date messageCreateDate;
    /**
     * 消息创建时间 下限值(大于等于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "消息创建时间 下限值(大于等于)")
    private java.util.Date messageCreateDateFrom;
    /**
     * 消息创建时间 上限值(小于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "消息创建时间 上限值(小于)")
    private java.util.Date messageCreateDateTo;
    /**
     * 消息创建人部门
     */
    @ApiModelProperty(value = "消息创建人部门")
    private String messageCreateDepartment;
    /**
     * 消息发送人名称
     */
    @ApiModelProperty(value = "消息发送人名称")
    private String messageCreateUser;
    /**
     * 发送次数
     */
    @ApiModelProperty(value = "发送次数")
    private Integer sendNum;
    /**
     * 消息版本
     */
    @ApiModelProperty(value = "消息版本")
    private Integer messageVersion;
    /**
     * 跳转链接
     */
    @ApiModelProperty(value = "跳转链接")
    private String jumpUrl;
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
    public QueryWrapper<WxXcxMessage> toQueryWrapper() {
        QueryWrapper<WxXcxMessage> wrapper = Wrappers.<WxXcxMessage>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getMessageType() != null, "message_type", this.getMessageType());
        wrapper.eq(this.getType() != null, "type", this.getType());
        if (this.getUserLoginName() != null) {
            if (this.getUserLoginName().startsWith("%") && this.getUserLoginName().endsWith("%")) {
                wrapper.like("user_login_name", this.getUserLoginName().substring(1, this.getUserLoginName().length() - 1));
            } else if (this.getUserLoginName().startsWith("%") && !this.getUserLoginName().endsWith("%")) {
                wrapper.likeLeft("user_login_name", this.getUserLoginName().substring(1));
            } else if (this.getUserLoginName().endsWith("%")) {
                wrapper.likeRight("user_login_name", this.getUserLoginName().substring(0, this.getUserLoginName().length() - 1));
            } else {
                wrapper.eq("user_login_name", this.getUserLoginName());
            }
        }
        if (this.getUserPhone() != null) {
            if (this.getUserPhone().startsWith("%") && this.getUserPhone().endsWith("%")) {
                wrapper.like("user_phone", this.getUserPhone().substring(1, this.getUserPhone().length() - 1));
            } else if (this.getUserPhone().startsWith("%") && !this.getUserPhone().endsWith("%")) {
                wrapper.likeLeft("user_phone", this.getUserPhone().substring(1));
            } else if (this.getUserPhone().endsWith("%")) {
                wrapper.likeRight("user_phone", this.getUserPhone().substring(0, this.getUserPhone().length() - 1));
            } else {
                wrapper.eq("user_phone", this.getUserPhone());
            }
        }
        if (this.getSourceId() != null) {
            if (this.getSourceId().startsWith("%") && this.getSourceId().endsWith("%")) {
                wrapper.like("source_id", this.getSourceId().substring(1, this.getSourceId().length() - 1));
            } else if (this.getSourceId().startsWith("%") && !this.getSourceId().endsWith("%")) {
                wrapper.likeLeft("source_id", this.getSourceId().substring(1));
            } else if (this.getSourceId().endsWith("%")) {
                wrapper.likeRight("source_id", this.getSourceId().substring(0, this.getSourceId().length() - 1));
            } else {
                wrapper.eq("source_id", this.getSourceId());
            }
        }
        wrapper.eq(this.getOperateType() != null, "operate_type", this.getOperateType());
        if (this.getMessageTitle() != null) {
            if (this.getMessageTitle().startsWith("%") && this.getMessageTitle().endsWith("%")) {
                wrapper.like("message_title", this.getMessageTitle().substring(1, this.getMessageTitle().length() - 1));
            } else if (this.getMessageTitle().startsWith("%") && !this.getMessageTitle().endsWith("%")) {
                wrapper.likeLeft("message_title", this.getMessageTitle().substring(1));
            } else if (this.getMessageTitle().endsWith("%")) {
                wrapper.likeRight("message_title", this.getMessageTitle().substring(0, this.getMessageTitle().length() - 1));
            } else {
                wrapper.eq("message_title", this.getMessageTitle());
            }
        }
        if (this.getMessageContent() != null) {
            if (this.getMessageContent().startsWith("%") && this.getMessageContent().endsWith("%")) {
                wrapper.like("message_content", this.getMessageContent().substring(1, this.getMessageContent().length() - 1));
            } else if (this.getMessageContent().startsWith("%") && !this.getMessageContent().endsWith("%")) {
                wrapper.likeLeft("message_content", this.getMessageContent().substring(1));
            } else if (this.getMessageContent().endsWith("%")) {
                wrapper.likeRight("message_content", this.getMessageContent().substring(0, this.getMessageContent().length() - 1));
            } else {
                wrapper.eq("message_content", this.getMessageContent());
            }
        }
        wrapper.eq(this.getMessageCreateDate() != null, "message_create_date", this.getMessageCreateDate());
        wrapper.ge(this.getMessageCreateDateFrom() != null, "message_create_date", this.getMessageCreateDateFrom());
        wrapper.lt(this.getMessageCreateDateTo() != null, "message_create_date", this.getMessageCreateDateTo());
        if (this.getMessageCreateDepartment() != null) {
            if (this.getMessageCreateDepartment().startsWith("%") && this.getMessageCreateDepartment().endsWith("%")) {
                wrapper.like("message_create_department", this.getMessageCreateDepartment().substring(1, this.getMessageCreateDepartment().length() - 1));
            } else if (this.getMessageCreateDepartment().startsWith("%") && !this.getMessageCreateDepartment().endsWith("%")) {
                wrapper.likeLeft("message_create_department", this.getMessageCreateDepartment().substring(1));
            } else if (this.getMessageCreateDepartment().endsWith("%")) {
                wrapper.likeRight("message_create_department", this.getMessageCreateDepartment().substring(0, this.getMessageCreateDepartment().length() - 1));
            } else {
                wrapper.eq("message_create_department", this.getMessageCreateDepartment());
            }
        }
        if (this.getMessageCreateUser() != null) {
            if (this.getMessageCreateUser().startsWith("%") && this.getMessageCreateUser().endsWith("%")) {
                wrapper.like("message_create_user", this.getMessageCreateUser().substring(1, this.getMessageCreateUser().length() - 1));
            } else if (this.getMessageCreateUser().startsWith("%") && !this.getMessageCreateUser().endsWith("%")) {
                wrapper.likeLeft("message_create_user", this.getMessageCreateUser().substring(1));
            } else if (this.getMessageCreateUser().endsWith("%")) {
                wrapper.likeRight("message_create_user", this.getMessageCreateUser().substring(0, this.getMessageCreateUser().length() - 1));
            } else {
                wrapper.eq("message_create_user", this.getMessageCreateUser());
            }
        }
        wrapper.eq(this.getSendNum() != null, "send_num", this.getSendNum());
        wrapper.eq(this.getMessageVersion() != null, "message_version", this.getMessageVersion());
        if (this.getJumpUrl() != null) {
            if (this.getJumpUrl().startsWith("%") && this.getJumpUrl().endsWith("%")) {
                wrapper.like("jump_url", this.getJumpUrl().substring(1, this.getJumpUrl().length() - 1));
            } else if (this.getJumpUrl().startsWith("%") && !this.getJumpUrl().endsWith("%")) {
                wrapper.likeLeft("jump_url", this.getJumpUrl().substring(1));
            } else if (this.getJumpUrl().endsWith("%")) {
                wrapper.likeRight("jump_url", this.getJumpUrl().substring(0, this.getJumpUrl().length() - 1));
            } else {
                wrapper.eq("jump_url", this.getJumpUrl());
            }
        }
        wrapper.eq(this.getCreatedTime() != null, "created_time", this.getCreatedTime());
        wrapper.ge(this.getCreatedTimeFrom() != null, "created_time", this.getCreatedTimeFrom());
        wrapper.lt(this.getCreatedTimeTo() != null, "created_time", this.getCreatedTimeTo());
        wrapper.eq(this.getUpdatedTime() != null, "updated_time", this.getUpdatedTime());
        wrapper.ge(this.getUpdatedTimeFrom() != null, "updated_time", this.getUpdatedTimeFrom());
        wrapper.lt(this.getUpdatedTimeTo() != null, "updated_time", this.getUpdatedTimeTo());
        return wrapper;
    }

}
