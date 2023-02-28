/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：教学管理
 * 模型名称：示范课、研究课开设表
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
import com.zhzx.server.service.TeachingResultSfkksService;
import com.zhzx.server.repository.TeachingResultSfkksMapper;
import com.zhzx.server.repository.base.TeachingResultSfkksBaseMapper;
import com.zhzx.server.domain.TeachingResultSfkks;
import com.zhzx.server.rest.req.TeachingResultSfkksParam;

@Service
public class TeachingResultSfkksServiceImpl extends ServiceImpl<TeachingResultSfkksMapper, TeachingResultSfkks> implements TeachingResultSfkksService {

    @Override
    public int updateAllFieldsById(TeachingResultSfkks entity) {
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
    public boolean saveBatch(Collection<TeachingResultSfkks> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(TeachingResultSfkksBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }


}
