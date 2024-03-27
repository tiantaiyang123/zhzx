/**
* 项目：中华中学管理平台
* 模型分组：消息管理
* 模型名称：消息表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.service.impl;

import java.util.*;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhzx.server.domain.MessageTask;
import com.zhzx.server.enums.ReceiverEnum;
import com.zhzx.server.enums.YesNoEnum;
import org.springframework.stereotype.Service;
import com.zhzx.server.service.MessageService;
import com.zhzx.server.repository.MessageMapper;
import com.zhzx.server.domain.Message;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Override
    public int updateAllFieldsById(Message entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
    }

    @Override
    public int batchInsert(List<Message> messageList) {
        return this.baseMapper.batchInsert(messageList);
    }

    @Override
    public void calculateMessage(Date date) {
        List<Map<String,Object>> mapList = this.baseMapper.calculateMessage(date);
        if(CollectionUtils.isNotEmpty(mapList)){
            for (Map<String,Object> map: mapList){
                Message message = new Message();
                message.setMessageTaskId(-1L);
                message.setName(map.get("title")+"消息的反馈通知");
                message.setTitle(map.get("title")+"消息的反馈通知");
                if(Objects.equals(map.get("needWrite"),"YES")){

                    if(Integer.parseInt(map.get("writeNum").toString()) == 0){
                        message.setContent(String.format("发送：%s, 已填：%s ",map.get("sendNum"),map.get("writeNum")));
                    }else{
                        Long messageTaskId = Long.parseLong(map.get("message_task_id").toString());
                        List<Message> messageList = this.baseMapper.selectList(Wrappers.<Message>lambdaQuery()
                                .eq(Message::getIsWrite,YesNoEnum.YES)
                                .eq(Message::getMessageTaskId,messageTaskId)
                        );
                        String statistics = this.messageStatistics(messageList);
                        message.setContent(String.format("发送：%s, 已填：%s%s",map.get("sendNum"),map.get("writeNum"),statistics));
                    }
                }else{
                    message.setContent(String.format("发送：%s, 已读：%s",map.get("sendNum"),map.get("readNum")));
                }
                message.setSenderId(-1L);
                message.setSenderName("系统");
                message.setReceiverId(Long.parseLong(map.get("editorId").toString()));
                message.setReceiverName(map.get("editorName").toString());
                message.setReceiverType(ReceiverEnum.TEACHER);
                message.setSendTime(new Date());
                message.setIsSend(YesNoEnum.YES);
                try {
                    message.setDefault().validate(true);
                    this.baseMapper.insert(message);
                }catch (Exception e){
                    log.error("计算消息失败"+e.getMessage());
                }
            }
        }
    }

    @Override
    public int updateIsSendOnMessage(MessageTask entity) {
        List<Message> messageList = this.baseMapper.selectList
                (Wrappers.<Message>lambdaQuery().eq(Message::getMessageTaskId,entity.getId()));
        //判断查询的数量
        List<Message> messages = new ArrayList<>();
        if (messageList.size()>0){
            messageList.forEach(message -> {
                message.setIsSend(entity.getSendType());
                messages.add(message);
            });

        }
        int updateBatch = this.baseMapper.updateBatch(messages);
        return updateBatch;
    }


    private String messageStatistics(List<Message> messages){
        //格式 {"是否出行",{"是"：11;"否"：12}}
        Map<String,Map<String,Integer>> resultMap = new HashMap<>();
        for(Message message : messages){
            //解析json
            JSONObject jsonObject = JSONObject.parseObject(message.getContent());
            JSONArray formDataList = jsonObject.getJSONArray("formDataList");
            JSONObject model = jsonObject.getJSONObject("model");
            for (int i = 0; i < formDataList.size(); i++) {
                JSONObject formDataJSONObject = formDataList.getJSONObject(i);
                String type = formDataJSONObject.getString("name");
                if(Objects.equals("Radio",type)){
                    String label = formDataJSONObject.getString("label");
                    Map<String,Integer> statistics = new HashMap<>();

                    String fieldId = formDataJSONObject.getString("fieldId");
                    String value = model.getString(fieldId);

                    if(resultMap.containsKey(label)){
                        Map<String,Integer> integerMap = resultMap.get(label);
                        if(integerMap.containsKey(value)){
                            statistics.put(value,integerMap.get(value)+1);
                        }else{
                            statistics.put(value,1);
                        }
                    }else{
                        statistics.put(value,1);
                    }
                    resultMap.put(label,statistics);
                }
            }
        }
        if(resultMap != null && resultMap.keySet() != null && resultMap.keySet().size() > 0){
            StringBuilder stringBuilder = new StringBuilder();
            resultMap.keySet().stream().forEach(item ->{
                stringBuilder.append(", "+item + ":");
                Map<String,Integer> integerMap = resultMap.get(item);
                integerMap.keySet().stream().forEach(value->{
                    stringBuilder.append(value+" " +integerMap.get(value)+"人 ");
                });
            });
            return stringBuilder.toString();
        }
        return "";
    }
}
