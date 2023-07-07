package com.zhzx.server.dto;

import com.zhzx.server.enums.MessageSystemEnum;
import com.zhzx.server.enums.MessageTypeEnum;
import lombok.Data;

import java.util.Date;

@Data
public class MessageCombineDto {
    private Date time;
    private String title;
    private String content;
    private String jumpUrl;
    private MessageSystemEnum messageSystemEnum;
    private MessageTypeEnum messageTypeEnum;
    private String messageCreator;
    private String messageDepartment;
    private String id;
}
