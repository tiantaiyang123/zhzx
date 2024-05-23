package com.zhzx.server.dto;

import com.zhzx.server.enums.YesNoEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MessageCombineDto {
    private Date time;
    private String title;
    private String content;
    private String jumpUrl;
    private String messageSystemEnum;
    private String messageTypeEnum;
    private String messageCreator;
    private String messageDepartment;
    private YesNoEnum isRead;
    @ApiModelProperty(value = "0消息1待办")
    private String mesOrWork;
    private String id;
    private List<Integer> ids;
}
