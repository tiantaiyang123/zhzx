/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：教研组组员表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.StaffResearchLeader;
import com.zhzx.server.domain.StaffResearchMember;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.repository.StaffResearchLeaderMapper;
import com.zhzx.server.repository.StaffResearchMemberMapper;
import com.zhzx.server.repository.base.StaffResearchMemberBaseMapper;
import com.zhzx.server.service.StaffResearchMemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StaffResearchMemberServiceImpl extends ServiceImpl<StaffResearchMemberMapper, StaffResearchMember> implements StaffResearchMemberService {

    @Resource
    private StaffResearchLeaderMapper staffResearchLeaderMapper;

    @Override
    @Transactional( rollbackFor = Exception.class )
    public List<StaffResearchMember> multiSave(Long subjectId, List<Long> staffIds) {
        List<StaffResearchMember> staffResearchMembers = new ArrayList<>();
        if (CollectionUtils.isEmpty(staffIds)) return staffResearchMembers;

        List<StaffResearchLeader> staffResearchLeaderList = Optional.ofNullable(
                this.staffResearchLeaderMapper.selectList(
                        Wrappers.<StaffResearchLeader>lambdaQuery()
                                .select(StaffResearchLeader::getStaffId)
                                .eq(StaffResearchLeader::getSubjectId, subjectId)
                                .eq(StaffResearchLeader::getIsCurrent, YesNoEnum.YES)
                )
        ).orElse(new ArrayList<>());
        List<Long> staffIdsLeader = staffResearchLeaderList.stream().map(StaffResearchLeader::getStaffId).collect(Collectors.toList());

        staffIds.forEach(staffId -> {
            if (!staffIdsLeader.contains(staffId)) {
                StaffResearchMember staffResearchMember = new StaffResearchMember();
                staffResearchMember.setSubjectId(subjectId);
                staffResearchMember.setStaffId(staffId);
                staffResearchMember.setIsCurrent(YesNoEnum.YES);
                staffResearchMember.setIsLeader(YesNoEnum.NO);
                staffResearchMembers.add(staffResearchMember);
            }
        });

        this.getBaseMapper().delete(
                Wrappers.<StaffResearchMember>lambdaQuery()
                        .eq(StaffResearchMember::getSubjectId, subjectId)
        );
        if (CollectionUtils.isNotEmpty(staffResearchMembers)) {
            this.getBaseMapper().batchInsert(staffResearchMembers);
        }
        return staffResearchMembers;
    }

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
