/**
 * 项目：中华中学管理平台
 * 模型分组：成绩管理
 * 模型名称：考试临界生分数段设置表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.ExamEdgeSub;
import com.zhzx.server.repository.ExamEdgeSubMapper;
import com.zhzx.server.repository.base.ExamEdgeSubBaseMapper;
import com.zhzx.server.service.ExamEdgeSubService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class ExamEdgeSubServiceImpl extends ServiceImpl<ExamEdgeSubMapper, ExamEdgeSub> implements ExamEdgeSubService {

    @Override
    public int updateAllFieldsById(ExamEdgeSub entity) {
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
    public boolean saveBatch(Collection<ExamEdgeSub> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(ExamEdgeSubBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }


}
