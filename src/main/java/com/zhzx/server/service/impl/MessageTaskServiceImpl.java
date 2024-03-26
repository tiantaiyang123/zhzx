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
import com.zhzx.server.rest.res.ApiResponse;
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
        //获取登录者的登录信息
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

        //获取能接受消息的集合的所有人
        List<MessageTaskReceiver> messageTaskReceiverList = entity.getMessageTaskReceiverList().stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparingLong(MessageTaskReceiver::getReceiverId))), ArrayList::new
                )
        );
        //创建消息集合
        List<Message> messages = new ArrayList<>();
        if (!messageTaskReceiverList.isEmpty()) {
            messageTaskReceiverList.forEach(messageTaskReceiver -> {
            messageTaskReceiver.setMessageTaskId(entity.getId());
            messageTaskReceiver.setReceiverType(entity.getReceiverType());
            messageTaskReceiver.setDefault().validate(true);
            //创建消息对象
                Message message = createMessage(user, entity, messageTaskReceiver);
                messages.add(message);
        });
        }
        messageTaskReceiverMapper.batchInsert(messageTaskReceiverList);
        //插入信息表
        this.messageMapper.batchInsert(messages);
        return entity;
    }
    // 创建消息的方法
    private Message createMessage(User user, MessageTaskDto entity, MessageTaskReceiver messageTaskReceiver) {
        Message message = new Message();
        message.setSenderId(user.getId());
        message.setSenderName(user.getRealName());
        message.setSendTime(entity.getStartTime());
        message.setMessageTaskId(entity.getId());
        message.setName(entity.getTitle());
        message.setTitle(entity.getTitle());
        message.setContent(entity.getContent());
        message.setReceiverId(messageTaskReceiver.getReceiverId());
        message.setReceiverName(messageTaskReceiver.getReceiverName());
        message.setReceiverType(messageTaskReceiver.getReceiverType());
        message.setIsSend(entity.getSendType());
        message.setIsRead(YesNoEnum.NO);
        message.setIsWrite(YesNoEnum.NO);
        message.setNeedWrite(entity.getNeedWrite());
        message.setSendNum(0);
        message.setCreatedTime(new Date());
        message.setUpdatedTime(new Date());
        return message;
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
        int i = this.baseMapper.updateById(entity);
        if (i>0){
            //将接收人表中的数据删除
            messageTaskReceiverMapper.delete(Wrappers.<MessageTaskReceiver>lambdaQuery()
                    .eq(MessageTaskReceiver::getMessageTaskId,entity.getId())
            );
            messageMapper.delete(Wrappers.<Message>lambdaQuery().eq(Message::getMessageTaskId,entity.getId()));
            List<MessageTaskReceiver> messageTaskReceiverList = entity.getMessageTaskReceiverList().stream().collect(
                    Collectors.collectingAndThen(
                            Collectors.toCollection(() -> new TreeSet<>(Comparator.comparingLong(MessageTaskReceiver::getReceiverId))), ArrayList::new
                    )
            );
            List<Message> messages =new ArrayList<>();
            messageTaskReceiverList.forEach(messageTaskReceiver -> {
                messageTaskReceiver.setMessageTaskId(entity.getId());
                messageTaskReceiver.setReceiverType(entity.getReceiverType());
                messageTaskReceiver.setDefault().validate(true);
                //创建messages对象
                Message message = createMessage(user, entity, messageTaskReceiver);
                messages.add(message);
            });
            messageTaskReceiverMapper.batchInsert(messageTaskReceiverList);
            //插入信息表
            this.messageMapper.batchInsert(messages);
        }
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
