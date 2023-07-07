package com.zhzx.server.dto.xcx;

import com.baomidou.mybatisplus.annotation.TableField;
import com.zhzx.server.domain.BaseDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class WxXcxMessageDto extends BaseDomain {
    /**
     * 通知类型0消息1待办
     */
    @ApiModelProperty(value = "通知类型0消息1待办", required = true)
    private Integer messageType;
    /**
     * 消息类型0其他1收文2发文3阅文4请示5听课6请假7会议
     */
    @ApiModelProperty(value = "消息类型0其他1收文2发文3阅文4请示5听课6请假7会议 ", required = true)
    private Integer type;
    /**
     * 接收人e办公登录名
     */
    @ApiModelProperty(value = "接收人e办公登录名", required = true)
    private String userLoginName;
    /**
     * 接收人手机号
     */
    @ApiModelProperty(value = "接收人手机号")
    private String userPhone;
    /**
     * 源系统消息ID
     */
    @ApiModelProperty(value = "源系统消息ID", required = true)
    private String sourceId;
    /**
     * 操作类型0新增1更新2删除
     */
    @ApiModelProperty(value = "操作类型0新增1更新2删除", required = true)
    private Integer operateType;
    /**
     * 消息标题
     */
    @ApiModelProperty(value = "消息标题", required = true)
    private String messageTitle;
    /**
     * 消息内容
     */
    @ApiModelProperty(value = "消息内容", required = true)
    private String messageContent;
    /**
     * 消息创建时间
     */
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
    @ApiModelProperty(value = "消息发送人名称")
    private String messageCreateUser;
    /**
     * 是否已读0未读1已读
     */
    @ApiModelProperty(value = "是否已读0未读1已读")
    private Integer isRead;
    /**
     * 跳转链接
     */
    @ApiModelProperty(value = "跳转链接")
    private String jumpUrl;
}
