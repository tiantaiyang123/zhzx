/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：年级组长表
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
import com.zhzx.server.service.StaffGradeLeaderService;
import com.zhzx.server.repository.StaffGradeLeaderMapper;
import com.zhzx.server.repository.base.StaffGradeLeaderBaseMapper;
import com.zhzx.server.domain.StaffGradeLeader;
import com.zhzx.server.rest.req.StaffGradeLeaderParam;

@Service
public class StaffGradeLeaderServiceImpl extends ServiceImpl<StaffGradeLeaderMapper, StaffGradeLeader> implements StaffGradeLeaderService {

    @Override
    public int updateAllFieldsById(StaffGradeLeader entity) {
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
    public boolean saveBatch(Collection<StaffGradeLeader> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(StaffGradeLeaderBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }


}
