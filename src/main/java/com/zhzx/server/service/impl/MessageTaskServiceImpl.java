/**
* 项目：中华中学管理平台
* 模型分组：消息管理
* 模型名称：消息任务表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.service.impl;

import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhzx.server.domain.Message;
import com.zhzx.server.domain.MessageTaskReceiver;
import com.zhzx.server.domain.User;
import com.zhzx.server.dto.MessageTaskDto;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.repository.MessageMapper;
import com.zhzx.server.repository.MessageTaskReceiverMapper;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.util.JsonToCornUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import com.zhzx.server.service.MessageTaskService;
import com.zhzx.server.repository.MessageTaskMapper;
import com.zhzx.server.domain.MessageTask;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageTaskServiceImpl extends ServiceImpl<MessageTaskMapper, MessageTask> implements MessageTaskService {

    @Resource
    private MessageTaskReceiverMapper messageTaskReceiverMapper;
    @Resource
    private MessageMapper messageMapper;

    @Override
    public int updateAllFieldsById(MessageTask entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
    }

    @Override
    @Transactional
    public MessageTask create(MessageTaskDto entity) {
        if(CollectionUtils.isEmpty(entity.getMessageTaskReceiverList())){
            throw new ApiCode.ApiException(-5,"接收人不能为空");
        }
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        entity.setEditorId(user.getStaffId());
        entity.setEditorName(user.getRealName());
        String corn = JsonToCornUtils.jsonToCron(entity.getCronJson());
        JSONObject jsonObject = JSON.parseObject(entity.getCronJson());
        if(jsonObject.containsKey("noticeDayTime")){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(jsonObject.getDate("noticeDayTime"));
            calendar.add(Calendar.DAY_OF_MONTH,-1);
            entity.setStartTime(calendar.getTime());
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(jsonObject.getDate("noticeDayTime"));
            calendar1.add(Calendar.DAY_OF_MONTH,1);
            entity.setEndTime(calendar1.getTime());
        }
        entity.setCron(corn);
        entity.setDefault().validate(true);
        this.baseMapper.insert(entity);

        List<MessageTaskReceiver> messageTaskReceiverList = entity.getMessageTaskReceiverList().stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparingLong(MessageTaskReceiver::getReceiverId))), ArrayList::new
                )
        );
        messageTaskReceiverList.forEach(messageTaskReceiver -> {
            messageTaskReceiver.setMessageTaskId(entity.getId());
            messageTaskReceiver.setReceiverType(entity.getReceiverType());
            messageTaskReceiver.setDefault().validate(true);
        });
        messageTaskReceiverMapper.batchInsert(messageTaskReceiverList);
        return entity;
    }

    @Transactional
    @Override
    public MessageTask update(MessageTaskDto entity) {
        if(CollectionUtils.isEmpty(entity.getMessageTaskReceiverList())){
            throw new ApiCode.ApiException(-5,"接收人不能为空");
        }
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        entity.setEditorId(user.getStaffId());
        entity.setEditorName(user.getRealName());
        String corn = JsonToCornUtils.jsonToCron(entity.getCronJson());
        JSONObject jsonObject = JSON.parseObject(entity.getCronJson());
        if(jsonObject.containsKey("noticeDayTime")){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(jsonObject.getDate("noticeDayTime"));
            calendar.add(Calendar.DAY_OF_MONTH,-1);
            entity.setStartTime(calendar.getTime());
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(jsonObject.getDate("noticeDayTime"));
            calendar1.add(Calendar.DAY_OF_MONTH,1);
            entity.setEndTime(calendar1.getTime());
        }
        entity.setCron(corn);

        entity.setDefault().validate(true);
        this.baseMapper.updateById(entity);
        messageTaskReceiverMapper.delete(Wrappers.<MessageTaskReceiver>lambdaQuery()
                .eq(MessageTaskReceiver::getMessageTaskId,entity.getId())
        );
        List<MessageTaskReceiver> messageTaskReceiverList = entity.getMessageTaskReceiverList().stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparingLong(MessageTaskReceiver::getReceiverId))), ArrayList::new
                )
        );
        messageTaskReceiverList.forEach(messageTaskReceiver -> {
            messageTaskReceiver.setMessageTaskId(entity.getId());
            messageTaskReceiver.setReceiverType(entity.getReceiverType());
            messageTaskReceiver.setDefault().validate(true);
        });
        messageTaskReceiverMapper.batchInsert(messageTaskReceiverList);
        Integer message = messageMapper.update(new Message(),Wrappers.<Message>lambdaUpdate()
                .set(Message::getContent,entity.getContent())
                .set(Message::getTitle,entity.getTitle())
                .set(Message::getName,entity.getName())
                .eq(Message::getMessageTaskId,entity.getId())
                .eq(Message::getIsSend,YesNoEnum.NO)
        );
        return entity;
    }

    @Override
    @Transactional
    public Integer delete(Long id) {
        Message message = messageMapper.selectOne(Wrappers.<Message>lambdaQuery()
                .eq(Message::getIsRead, YesNoEnum.YES)
                .eq(Message::getMessageTaskId,id)
        );
        if(message != null){
            throw new ApiCode.ApiException(-5,"已发送消息，无法删除");
        }

        this.baseMapper.deleteById(id);
        messageTaskReceiverMapper.delete(Wrappers.<MessageTaskReceiver>lambdaQuery()
                .eq(MessageTaskReceiver::getMessageTaskId,id)
        );
        return this.messageMapper.delete(Wrappers.<Message>lambdaQuery()
                .eq(Message::getMessageTaskId,id)
        );
    }

    @Override
    public Page<MessageTaskDto> getPage(Page authorityPage, QueryWrapper<MessageTask> wrapper) {
        List<MessageTaskDto> messageTaskDtoList = this.baseMapper.getPage(authorityPage,wrapper);
        authorityPage.setRecords(messageTaskDtoList);
        return authorityPage;
    }

}
