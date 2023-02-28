/**
 * 项目：中华中学管理平台
 * 模型分组：教学管理
 * 模型名称：微课获奖表
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
import com.zhzx.server.service.TeachingResultWkhjService;
import com.zhzx.server.repository.TeachingResultWkhjMapper;
import com.zhzx.server.repository.base.TeachingResultWkhjBaseMapper;
import com.zhzx.server.domain.TeachingResultWkhj;
import com.zhzx.server.rest.req.TeachingResultWkhjParam;

@Service
public class TeachingResultWkhjServiceImpl extends ServiceImpl<TeachingResultWkhjMapper, TeachingResultWkhj> implements TeachingResultWkhjService {

    @Override
    public int updateAllFieldsById(TeachingResultWkhj entity) {
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
    public boolean saveBatch(Collection<TeachingResultWkhj> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(TeachingResultWkhjBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }


}
