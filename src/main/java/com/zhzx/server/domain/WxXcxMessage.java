/**
 * 项目：中华中学管理平台
 * 模型分组：第三方数据管理
 * 模型名称：微信小程序通知表
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
@TableName("`tir_wx_xcx_message`")
@ApiModel(value = "WxXcxMessage", description = "微信小程序通知表")
public class WxXcxMessage extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * 通知类型0消息1待办
     */
    @TableField(value = "message_type")
    @ApiModelProperty(value = "通知类型0消息1待办", required = true)
    private Integer messageType;
    /**
     * 消息类型0其他1收文2发文3阅文4请示5听课6请假7会议 
     */
    @TableField(value = "type")
    @ApiModelProperty(value = "消息类型0其他1收文2发文3阅文4请示5听课6请假7会议 ", required = true)
    private Integer type;
    /**
     * 接收人e办公登录名
     */
    @TableField(value = "user_login_name")
    @ApiModelProperty(value = "接收人e办公登录名", required = true)
    private String userLoginName;
    /**
     * 接收人手机号
     */
    @TableField(value = "user_phone")
    @ApiModelProperty(value = "接收人手机号")
    private String userPhone;
    /**
     * 源系统消息ID
     */
    @TableField(value = "source_id")
    @ApiModelProperty(value = "源系统消息ID", required = true)
    private String sourceId;
    /**
     * 操作类型0新增1更新2删除
     */
    @TableField(value = "operate_type")
    @ApiModelProperty(value = "操作类型0新增1更新2删除", required = true)
    private Integer operateType;
    /**
     * 消息标题
     */
    @TableField(value = "message_title")
    @ApiModelProperty(value = "消息标题", required = true)
    private String messageTitle;
    /**
     * 消息内容
     */
    @TableField(value = "message_content", select = false)
    @ApiModelProperty(value = "消息内容", required = true)
    private String messageContent;
    /**
     * 消息创建时间
     */
    @TableField(value = "message_create_date")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "消息创建时间", required = true)
    private java.util.Date messageCreateDate;
    /**
     * 消息创建人部门
     */
    @TableField(value = "message_create_department")
    @ApiModelProperty(value = "消息创建人部门", required = true)
    private String messageCreateDepartment;
    /**
     * 消息发送人名称
     */
    @TableField(value = "message_create_user")
    @ApiModelProperty(value = "消息发送人名称")
    private String messageCreateUser;
    /**
     * 发送次数
     */
    @TableField(value = "send_num")
    @ApiModelProperty(value = "发送次数", required = true)
    private Integer sendNum;
    /**
     * 消息版本
     */
    @TableField(value = "message_version")
    @ApiModelProperty(value = "消息版本", required = true)
    private Integer messageVersion;
    /**
     * 跳转链接
     */
    @TableField(value = "jump_url", select = false)
    @ApiModelProperty(value = "跳转链接")
    private String jumpUrl;
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
    public WxXcxMessage setDefault() {
        if (this.getSendNum() == null) {
            this.setSendNum(1);
        }
        if (this.getMessageVersion() == null) {
            this.setMessageVersion(1);
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
        if (this.getMessageType() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "通知类型0消息1待办不能为空！");
            return false;
        }
        if (this.getType() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "消息类型0其他1收文2发文3阅文4请示5听课6请假7会议 不能为空！");
            return false;
        }
        if (this.getUserLoginName() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "接收人e办公登录名不能为空！");
            return false;
        }
        if (this.getSourceId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "源系统消息ID不能为空！");
            return false;
        }
        if (this.getOperateType() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "操作类型0新增1更新2删除不能为空！");
            return false;
        }
        if (this.getMessageTitle() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "消息标题不能为空！");
            return false;
        }
        if (this.getMessageContent() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "消息内容不能为空！");
            return false;
        }
        if (this.getMessageCreateDate() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "消息创建时间不能为空！");
            return false;
        }
        if (this.getMessageCreateDepartment() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "消息创建人部门不能为空！");
            return false;
        }
        if (this.getSendNum() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "发送次数不能为空！");
            return false;
        }
        if (this.getMessageVersion() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "消息版本不能为空！");
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
