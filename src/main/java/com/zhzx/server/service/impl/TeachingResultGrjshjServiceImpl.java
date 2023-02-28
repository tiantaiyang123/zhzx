/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：教学管理
 * 模型名称：个人竞赛获奖表
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
import com.zhzx.server.service.TeachingResultGrjshjService;
import com.zhzx.server.repository.TeachingResultGrjshjMapper;
import com.zhzx.server.repository.base.TeachingResultGrjshjBaseMapper;
import com.zhzx.server.domain.TeachingResultGrjshj;
import com.zhzx.server.rest.req.TeachingResultGrjshjParam;

@Service
public class TeachingResultGrjshjServiceImpl extends ServiceImpl<TeachingResultGrjshjMapper, TeachingResultGrjshj> implements TeachingResultGrjshjService {

    @Override
    public int updateAllFieldsById(TeachingResultGrjshj entity) {
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
    public boolean saveBatch(Collection<TeachingResultGrjshj> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(TeachingResultGrjshjBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }


}
