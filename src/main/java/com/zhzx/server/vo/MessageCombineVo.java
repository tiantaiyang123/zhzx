package com.zhzx.server.vo;

import com.zhzx.server.enums.MessageSystemEnum;
import com.zhzx.server.enums.MessageTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class MessageCombineVo {
    private Date timeFrom;
    private Date timeTo;
    private String title;
    private String content;
    private MessageSystemEnum messageSystemEnum;
    private MessageTypeEnum messageTypeEnum;
    private String messageCreator;
    private String messageDepartment;
    private String isRead;
    @ApiModelProperty(value = "接收人登录用户名")
    private String staffEName;
}
