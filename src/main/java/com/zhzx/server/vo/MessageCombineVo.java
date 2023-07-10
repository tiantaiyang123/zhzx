package com.zhzx.server.vo;

import com.zhzx.server.enums.MessageSystemEnum;
import com.zhzx.server.enums.MessageTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class MessageCombineVo implements Serializable {
    @ApiModelProperty(value = "起始时间")
    private Date timeFrom;
    @ApiModelProperty(value = "终止时间")
    private Date timeTo;
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "内容")
    private String content;
    @ApiModelProperty(value = "消息来源系统")
    private MessageSystemEnum messageSystemEnum;
    @ApiModelProperty(value = "消息类型")
    private MessageTypeEnum messageTypeEnum;
    @ApiModelProperty(value = "消息创建人姓名")
    private String messageCreator;
    @ApiModelProperty(value = "消息创建部门")
    private String messageDepartment;
    @ApiModelProperty(value = "是否已读")
    private String isRead;
    @ApiModelProperty(value = "接收人登录用户名")
    private String staffEName;
    @ApiModelProperty(value = "教职工Id")
    private String staffId;
}
