/**
 * 项目：中华中学管理平台
 * 模型分组：第三方数据管理
 * 模型名称：微信小程序通知表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.WxXcxMessage;
import com.zhzx.server.dto.MessageCombineDto;
import com.zhzx.server.dto.xcx.WxXcxMessageDto;
import com.zhzx.server.repository.WxXcxMessageMapper;
import com.zhzx.server.repository.base.WxXcxMessageBaseMapper;
import com.zhzx.server.service.WxXcxMessageService;
import com.zhzx.server.vo.MessageCombineVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WxXcxMessageServiceImpl extends ServiceImpl<WxXcxMessageMapper, WxXcxMessage> implements WxXcxMessageService {

    @Override
    public int updateAllFieldsById(WxXcxMessage entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
    }

    @Override
    @Transactional( rollbackFor = Exception.class )
    public void syncWxXcxMessage(String code, List<WxXcxMessageDto> wxXcxMessageDtoList) {
        if (CollectionUtils.isNotEmpty(wxXcxMessageDtoList)) {
            List<WxXcxMessage> wxXcxMessageList = new ArrayList<>();
            for (WxXcxMessageDto wxXcxMessageDto : wxXcxMessageDtoList) {
                WxXcxMessage wxXcxMessage = new WxXcxMessage();
                wxXcxMessage.setMessageType(wxXcxMessageDto.getMessageType());
                wxXcxMessage.setType(wxXcxMessageDto.getType());
                wxXcxMessage.setUserLoginName(wxXcxMessageDto.getUserLoginName());
                wxXcxMessage.setUserPhone(wxXcxMessageDto.getUserPhone());
                wxXcxMessage.setSourceId(wxXcxMessageDto.getSourceId());
                wxXcxMessage.setOperateType(wxXcxMessageDto.getOperateType());
                wxXcxMessage.setMessageTitle(wxXcxMessageDto.getMessageTitle());
                wxXcxMessage.setMessageContent(wxXcxMessageDto.getMessageContent());
                wxXcxMessage.setMessageCreateDate(wxXcxMessageDto.getMessageCreateDate());
                wxXcxMessage.setMessageCreateDepartment(wxXcxMessageDto.getMessageCreateDepartment());
                wxXcxMessage.setMessageCreateUser(wxXcxMessageDto.getMessageCreateUser());
                wxXcxMessage.setJumpUrl(wxXcxMessageDto.getJumpUrl());
                wxXcxMessage.setDefault().validate(true);
                wxXcxMessageList.add(wxXcxMessage);
            }
            Map<Integer, List<WxXcxMessage>> map = wxXcxMessageList.stream().collect(Collectors.groupingBy(WxXcxMessage::getOperateType));

            List<WxXcxMessage> wxXcxMessageAddList = map.get(0);
            if (CollectionUtils.isNotEmpty(wxXcxMessageAddList)) {
                this.baseMapper.batchInsert(wxXcxMessageAddList);
            }
        }
    }

    @Override
    public IPage<MessageCombineDto> pageApp(String orderByClause, Integer pageNum, Integer pageSize, MessageCombineVo messageCombineVo) {
        IPage<MessageCombineDto>  iPage = new Page<>(pageNum, pageSize);
        List<MessageCombineDto> messageCombineDtos = this.baseMapper.pageApp(iPage, orderByClause, messageCombineVo);
        iPage.setRecords(messageCombineDtos);
        return iPage;
    }

    /**
     * 批量插入
     *
     * @param entityList ignore
     * @param batchSize  ignore
     * @return ignore
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveBatch(Collection<WxXcxMessage> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(WxXcxMessageBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }


}
