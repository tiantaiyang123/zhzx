/**
* 项目：中华中学管理平台
* 模型分组：系统管理
* 模型名称：老师分类表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.springframework.stereotype.Service;
import com.zhzx.server.service.StaffCategoryService;
import com.zhzx.server.repository.StaffCategoryMapper;
import com.zhzx.server.domain.StaffCategory;
import com.zhzx.server.rest.req.StaffCategoryParam;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StaffCategoryServiceImpl extends ServiceImpl<StaffCategoryMapper, StaffCategory> implements StaffCategoryService {

    @Override
    public int updateAllFieldsById(StaffCategory entity) {
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
    public boolean saveBatch(Collection<StaffCategory> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(StaffCategoryMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

}
