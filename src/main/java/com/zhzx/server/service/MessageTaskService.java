/**
* 项目：中华中学管理平台
* 模型分组：消息管理
* 模型名称：消息任务表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.MessageTask;
import com.zhzx.server.dto.MessageTaskDto;
import com.zhzx.server.rest.req.MessageTaskParam;

public interface MessageTaskService extends IService<MessageTask> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(MessageTask entity);


    MessageTask create(MessageTaskDto entity);

    MessageTask update(MessageTaskDto entity);

    Integer delete(Long id);

    Page<MessageTaskDto> getPage (Page authorityPage, QueryWrapper<MessageTask> wrapper);
}
