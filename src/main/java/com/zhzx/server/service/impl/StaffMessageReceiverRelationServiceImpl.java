/**
* 项目：中华中学管理平台
* 模型分组：系统管理
* 模型名称：老师微信发送人关联表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.StaffCategory;
import com.zhzx.server.domain.StaffCategoryRelation;
import com.zhzx.server.dto.StaffCategoryDto;
import com.zhzx.server.repository.StaffCategoryMapper;
import com.zhzx.server.service.StaffCategoryService;
import com.zhzx.server.util.TreeUtils;
import org.springframework.stereotype.Service;
import com.zhzx.server.service.StaffMessageReceiverRelationService;
import com.zhzx.server.repository.StaffMessageReceiverRelationMapper;
import com.zhzx.server.domain.StaffMessageReceiverRelation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffMessageReceiverRelationServiceImpl extends ServiceImpl<StaffMessageReceiverRelationMapper, StaffMessageReceiverRelation> implements StaffMessageReceiverRelationService {

    @Resource
    private StaffCategoryMapper staffCategoryMapper;

    @Override
    public int updateAllFieldsById(StaffMessageReceiverRelation entity) {
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
    public boolean saveBatch(Collection<StaffMessageReceiverRelation> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(StaffMessageReceiverRelationMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    @Override
    public List<StaffCategoryDto> getReceiverTree(Long staffId) {
        if(staffId == null){
            List<StaffCategoryDto> staffCategoryDtos = staffCategoryMapper.selectCategory(null);
            List<StaffCategoryDto> tree = TreeUtils.listToTree(staffCategoryDtos);
            return tree;
        }else{
            List<StaffCategoryDto> staffCategoryDtos = staffCategoryMapper.selectCategory(staffId);
            List<StaffCategoryDto> tree = TreeUtils.listToTree(staffCategoryDtos);
            return tree;
        }
    }

    @Override
    @Transactional
    public int batchInsertOrUpdate(List<StaffMessageReceiverRelation> records) {
        this.baseMapper.delete(Wrappers.<StaffMessageReceiverRelation>lambdaQuery()
                .eq(StaffMessageReceiverRelation::getStaffMessageId,records.get(0).getStaffMessageId())
                .eq(StaffMessageReceiverRelation::getReceiverType,records.get(0).getReceiverType())
        );
        return this.baseMapper.batchInsert(records);
    }


}
