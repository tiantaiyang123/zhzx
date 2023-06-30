/**
 * 项目：中华中学管理平台
 * 模型分组：第三方数据管理
 * 模型名称：微信小程序通知表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.WxXcxMessage;
import com.zhzx.server.repository.WxXcxMessageMapper;
import com.zhzx.server.repository.base.WxXcxMessageBaseMapper;
import com.zhzx.server.service.WxXcxMessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class WxXcxMessageServiceImpl extends ServiceImpl<WxXcxMessageMapper, WxXcxMessage> implements WxXcxMessageService {

    @Override
    public int updateAllFieldsById(WxXcxMessage entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
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
