package com.zhzx.server.aspect;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhzx.server.domain.Log;
import com.zhzx.server.domain.Message;
import com.zhzx.server.domain.TeachingResult;
import com.zhzx.server.domain.User;
import com.zhzx.server.dto.annotation.MessageInfo;
import com.zhzx.server.enums.ReceiverEnum;
import com.zhzx.server.enums.TeachingResultStateEnum;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.misc.SpringUtils;
import com.zhzx.server.service.LogService;
import com.zhzx.server.service.MessageService;
import com.zhzx.server.service.TeachingResultService;
import com.zhzx.server.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.JoinPoint;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Project: server <br>
 * Description:
 *
 * @author xiongwei
 * 2020/12/3 12:22
 */

@Slf4j
@Component
public class MessageInfoAspect {

    @Async( "threadPoolExecutor" )
    public void handle(JoinPoint pjp, Method method) throws Throwable {
        MessageInfo messageInfo = method.getAnnotation(MessageInfo.class);
        logHandle(messageInfo,pjp.getArgs());
    }

    public void logHandle(MessageInfo messageInfo,Object[] args) {
        Message message = new Message();
        message.setMessageTaskId(-1L);
        message.setName(messageInfo.name());
        message.setTitle(messageInfo.title());
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            User user = (User) subject.getPrincipal();
            message.setSenderId(user.getId());
            message.setSenderName(user.getRealName());
        } else {
            message.setSenderId(0L);
            message.setSenderName("anon");
        }
        switch (messageInfo.title()){
            case "审核" : verifyTeachingResult(message,args,messageInfo);break;
            case "添加" : createOrUpdateTeachingResult(message,args,messageInfo); break;
            case "修改" : createOrUpdateTeachingResult(message,args,messageInfo);break;
            case "登录" : createSysLog(messageInfo,args);break;
            default:;
        }
    }



    private void createOrUpdateTeachingResult(Message message,Object[] args,MessageInfo messageInfo){

        MessageService messageService = SpringUtils.getBean(MessageService.class);
        Map<String,Object> map = new HashMap<>();

        Map<String,Object> param = (Map<String, Object>) args[0];
        param = tidyName(param);
        TeachingResult teachingResult = JSON.parseObject(JSON.toJSONString(param),TeachingResult.class);
        if(teachingResult.getId() == null ){
            teachingResult.setId(Long.parseLong(param.get("teachingResultId").toString()));
        }
        map.put("teachingResult_"+teachingResult.getId(),teachingResult.getId());
        map.put("commentProcess",teachingResult);
        Message message1 = messageService.getOne(Wrappers.<Message>lambdaQuery()
                .eq(Message::getName,"teachingResult_"+teachingResult.getId())
                .eq(Message::getIsWrite,YesNoEnum.NO)
        );
        if(message1 == null){
            message.setContent(JsonUtils.toJson(map).replace("\r\n", ""));
            message.setName("teachingResult_"+teachingResult.getId());
            message.setTitle(message.getSenderName()+"创建的成果"+teachingResult.getName()+"需要审核");
            message.setReceiverId(1L);
            message.setReceiverName("徐飞");
            message.setSendTime(new Date());
            message.setIsSend(YesNoEnum.YES);
            message.setReceiverType(ReceiverEnum.TEACHER);
            message.setMessageTaskId(-2L);
            message.setDefault().validate(true);
            messageService.save(message);
        }else{
            message1.setContent(JsonUtils.toJson(map).replace("\r\n", ""));
            message1.setTitle(message.getSenderName()+"创建的成果"+teachingResult.getName()+"需要审核");
            message1.setSendTime(new Date());
            message1.setIsSend(YesNoEnum.YES);
            message1.setIsWrite(YesNoEnum.NO);
            message1.setIsRead(YesNoEnum.NO);
            messageService.updateById(message1);
        }
    }

    private void verifyTeachingResult(Message message,Object[] args,MessageInfo messageInfo){
        //成果审核，成果所有人收到消息
        TeachingResultService teachingResultService = SpringUtils.getBean(TeachingResultService.class);
        TeachingResult teachingResult = (TeachingResult) args[0];
        TeachingResult exist = teachingResultService.getById(teachingResult.getId());
        if(TeachingResultStateEnum.PASSED.equals(teachingResult.getState())){
            message.setContent("您的成果"+exist.getName()+"审核"+"通过了");
        }else{
            message.setContent("您的成果"+exist.getName()+"审核"+"已驳回");
        }
        message.setReceiverId(exist.getTeacherId());
        message.setReceiverName(exist.getTeacher() == null? " " : exist.getTeacher().getName());
        message.setSendTime(new Date());
        message.setIsSend(YesNoEnum.YES);
        message.setReceiverType(ReceiverEnum.TEACHER);
        message.setDefault().validate(true);
        MessageService messageService = SpringUtils.getBean(MessageService.class);
        messageService.save(message);

        messageService.update(Wrappers.<Message>lambdaUpdate()
                .set(Message::getIsWrite,YesNoEnum.YES)
                .eq(Message::getName,"teachingResult_"+teachingResult.getId())
        );
    }

    private void createSysLog(MessageInfo messageInfo,Object[] args){
        LogService logService = SpringUtils.getBean(LogService.class);
        Log log = new Log().setDefault();
        log.setCode("login");
        log.setValue(messageInfo.content());
        User user = (User) args[0];
        log.setEditorId(user.getId());
        log.setEditorName(user.getRealName());
        log.setDefault().validate(true);
        logService.save(log);
    }

    private Map<String, Object> tidyName(Map<String, Object> params) {
        if (params.containsKey("rych"))
            params.put("name", params.get("rych"));
        if (params.containsKey("zzmc"))
            params.put("name", params.get("zzmc"));
        if (params.containsKey("bsmc"))
            params.put("name", params.get("bsmc"));
        if (params.containsKey("bsmc"))
            params.put("name", params.get("bsmc"));
        if (params.containsKey("pxmc"))
            params.put("name", params.get("pxmc"));
        if (params.containsKey("jzmc"))
            params.put("name", params.get("jzmc"));
        if (params.containsKey("ktmc"))
            params.put("name", params.get("ktmc"));
        if (params.containsKey("lwbt"))
            params.put("name", params.get("lwbt"));
        if (params.containsKey("hjqk"))
            params.put("name", params.get("hjqk"));
        if (params.containsKey("wkmc"))
            params.put("name", params.get("wkmc"));
        return params;
    }
}
