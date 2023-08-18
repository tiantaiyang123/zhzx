/**
 * 项目：中华中学管理平台
 * 模型分组：第三方数据管理
 * 模型名称：微信小程序通知表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.WxXcxMessage;
import com.zhzx.server.dto.MessageCombineDto;
import com.zhzx.server.dto.xcx.WxXcxMessageDto;
import com.zhzx.server.vo.MessageCombineVo;

import java.util.List;

public interface WxXcxMessageService extends IService<WxXcxMessage> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(WxXcxMessage entity);


    void syncWxXcxMessage(String code, List<WxXcxMessageDto> wxXcxMessageDtoList);

    IPage<MessageCombineDto> pageApp(String orderByClause, Integer pageNum, Integer pageSize, MessageCombineVo messageCombineVo);

    Object updateIsReadApp(MessageCombineDto messageCombineDto);

    Object countApp(MessageCombineVo messageCombineVo);
}
