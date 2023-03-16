/**
 * 项目：中华中学管理平台
 * 模型分组：成绩管理
 * 模型名称：副科考试结果表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import java.util.Collection;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zhzx.server.service.ExamResultMinorService;
import com.zhzx.server.repository.ExamResultMinorMapper;
import com.zhzx.server.repository.base.ExamResultMinorBaseMapper;
import com.zhzx.server.domain.ExamResultMinor;

@Service
public class ExamResultMinorServiceImpl extends ServiceImpl<ExamResultMinorMapper, ExamResultMinor> implements ExamResultMinorService {

    @Override
    public int updateAllFieldsById(ExamResultMinor entity) {
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
    public boolean saveBatch(Collection<ExamResultMinor> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(ExamResultMinorBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }


}
