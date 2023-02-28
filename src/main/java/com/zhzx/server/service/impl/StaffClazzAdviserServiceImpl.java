/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：班主任表
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
import com.zhzx.server.service.StaffClazzAdviserService;
import com.zhzx.server.repository.StaffClazzAdviserMapper;
import com.zhzx.server.repository.base.StaffClazzAdviserBaseMapper;
import com.zhzx.server.domain.StaffClazzAdviser;
import com.zhzx.server.rest.req.StaffClazzAdviserParam;

@Service
public class StaffClazzAdviserServiceImpl extends ServiceImpl<StaffClazzAdviserMapper, StaffClazzAdviser> implements StaffClazzAdviserService {

    @Override
    public int updateAllFieldsById(StaffClazzAdviser entity) {
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
    public boolean saveBatch(Collection<StaffClazzAdviser> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(StaffClazzAdviserBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }


}
