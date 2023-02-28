/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：系统配置表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zhzx.server.service.SettingsService;
import com.zhzx.server.repository.SettingsMapper;
import com.zhzx.server.repository.base.SettingsBaseMapper;
import com.zhzx.server.domain.Settings;
import com.zhzx.server.rest.req.SettingsParam;

@Service
public class SettingsServiceImpl extends ServiceImpl<SettingsMapper, Settings> implements SettingsService {

    @Override
    public int updateAllFieldsById(Settings entity) {
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
    public boolean saveBatch(Collection<Settings> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(SettingsBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }


}
