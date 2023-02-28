/**
* 项目：中华中学管理平台
* 模型分组：消息管理
* 模型名称：消息表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.service;

import java.util.Date;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.Message;

public interface MessageService extends IService<Message> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(Message entity);

    int batchInsert(List<Message> messageList);

    void calculateMessage(Date date);
}
