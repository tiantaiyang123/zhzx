/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：教学管理
 * 模型名称：课题研究表
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
import com.zhzx.server.service.TeachingResultKtyjService;
import com.zhzx.server.repository.TeachingResultKtyjMapper;
import com.zhzx.server.repository.base.TeachingResultKtyjBaseMapper;
import com.zhzx.server.domain.TeachingResultKtyj;
import com.zhzx.server.rest.req.TeachingResultKtyjParam;

@Service
public class TeachingResultKtyjServiceImpl extends ServiceImpl<TeachingResultKtyjMapper, TeachingResultKtyj> implements TeachingResultKtyjService {

    @Override
    public int updateAllFieldsById(TeachingResultKtyj entity) {
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
    public boolean saveBatch(Collection<TeachingResultKtyj> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(TeachingResultKtyjBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }


}
