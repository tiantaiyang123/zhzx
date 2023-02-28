/**
* 项目：中华中学管理平台
* 模型分组：消息管理
* 模型名称：消息模板表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.zhzx.server.service.MessageTemplateService;
import com.zhzx.server.repository.MessageTemplateMapper;
import com.zhzx.server.domain.MessageTemplate;
import com.zhzx.server.rest.req.MessageTemplateParam;

@Service
public class MessageTemplateServiceImpl extends ServiceImpl<MessageTemplateMapper, MessageTemplate> implements MessageTemplateService {

    @Override
    public int updateAllFieldsById(MessageTemplate entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
    }



}
