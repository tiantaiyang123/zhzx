/**
* 项目：中华中学管理平台
* 模型分组：消息管理
* 模型名称：消息模板表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.MessageTemplate;
import com.zhzx.server.rest.req.MessageTemplateParam;

public interface MessageTemplateService extends IService<MessageTemplate> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(MessageTemplate entity);


}
