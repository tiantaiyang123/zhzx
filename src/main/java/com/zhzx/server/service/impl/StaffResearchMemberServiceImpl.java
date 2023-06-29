/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：教研组组员表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.StaffResearchMember;
import com.zhzx.server.repository.StaffResearchMemberMapper;
import com.zhzx.server.repository.base.StaffResearchMemberBaseMapper;
import com.zhzx.server.service.StaffResearchMemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class StaffResearchMemberServiceImpl extends ServiceImpl<StaffResearchMemberMapper, StaffResearchMember> implements StaffResearchMemberService {

    @Override
    public int updateAllFieldsById(StaffResearchMember entity) {
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
    public boolean saveBatch(Collection<StaffResearchMember> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(StaffResearchMemberBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }


}
