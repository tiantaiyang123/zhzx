/**
* 项目：中华中学管理平台
* 模型分组：系统管理
* 模型名称：老师分类关联表
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
import com.zhzx.server.service.StaffCategoryRelationService;
import com.zhzx.server.repository.StaffCategoryRelationMapper;
import com.zhzx.server.domain.StaffCategoryRelation;
import com.zhzx.server.rest.req.StaffCategoryRelationParam;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StaffCategoryRelationServiceImpl extends ServiceImpl<StaffCategoryRelationMapper, StaffCategoryRelation> implements StaffCategoryRelationService {

    @Override
    public int updateAllFieldsById(StaffCategoryRelation entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
    }

    /**
     * 批量插入
     *
     * @param entityList ignore
     * @return ignore
     */
    @Override
    @Transactional
    public Integer batchInsertOrUpdate(List<StaffCategoryRelation> entityList) {
        this.baseMapper.delete(Wrappers.<StaffCategoryRelation>lambdaQuery()
                .eq(StaffCategoryRelation::getStaffCategoryId,entityList.get(0).getStaffCategoryId())
        );
        return this.baseMapper.batchInsert(entityList);
    }

}
