/**
 * 项目：中华中学管理平台
 * 模型分组：成绩管理
 * 模型名称：考试发布子表
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
import com.zhzx.server.service.ExamPublishRelationService;
import com.zhzx.server.repository.ExamPublishRelationMapper;
import com.zhzx.server.repository.base.ExamPublishRelationBaseMapper;
import com.zhzx.server.domain.ExamPublishRelation;
import com.zhzx.server.rest.req.ExamPublishRelationParam;

@Service
public class ExamPublishRelationServiceImpl extends ServiceImpl<ExamPublishRelationMapper, ExamPublishRelation> implements ExamPublishRelationService {

    @Override
    public int updateAllFieldsById(ExamPublishRelation entity) {
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
    public boolean saveBatch(Collection<ExamPublishRelation> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(ExamPublishRelationBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }


}
