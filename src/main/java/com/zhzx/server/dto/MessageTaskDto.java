package com.zhzx.server.dto;

import com.zhzx.server.domain.Comment;
import com.zhzx.server.domain.FunctionDepartment;
import com.zhzx.server.domain.MessageTask;
import com.zhzx.server.enums.ReceiverEnum;
import com.zhzx.server.enums.YesNoEnum;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by A2 on 2022/1/11.
 */
@Data
public class MessageTaskDto extends MessageTask{

   private String isSend;

   private ReceiverEnum receiverType;
}
