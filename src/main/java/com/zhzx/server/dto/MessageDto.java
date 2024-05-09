package com.zhzx.server.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.zhzx.server.domain.Comment;
import com.zhzx.server.domain.FunctionDepartment;
import com.zhzx.server.domain.Message;
import com.zhzx.server.enums.MessageSystemEnum;
import com.zhzx.server.enums.MessageTypeEnum;
import com.zhzx.server.enums.YesNoEnum;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by A2 on 2022/1/11.
 */
@Data
public class MessageDto extends Message{

    private String token;

    private YesNoEnum refuseStatus;
}
