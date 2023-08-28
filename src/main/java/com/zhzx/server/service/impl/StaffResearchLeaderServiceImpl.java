/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：教研组长表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.Role;
import com.zhzx.server.domain.StaffResearchLeader;
import com.zhzx.server.domain.User;
import com.zhzx.server.domain.UserRole;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.repository.RoleMapper;
import com.zhzx.server.repository.StaffResearchLeaderMapper;
import com.zhzx.server.repository.UserMapper;
import com.zhzx.server.repository.UserRoleMapper;
import com.zhzx.server.repository.base.StaffResearchLeaderBaseMapper;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.StaffResearchLeaderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;

@Service
public class StaffResearchLeaderServiceImpl extends ServiceImpl<StaffResearchLeaderMapper, StaffResearchLeader> implements StaffResearchLeaderService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    public int updateAllFieldsById(StaffResearchLeader entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StaffResearchLeader addWithRole(StaffResearchLeader entity) {
        Role role = this.roleMapper.selectOne(new QueryWrapper<Role>().eq("name", "ROLE_JIAOYANZUZHANG"));
        if (null == role) {
            throw new ApiCode.ApiException(-1, "缺少教研组长角色");
        }

        this.baseMapper.insert(entity);

        User user = this.userMapper.selectOne(
                Wrappers.<User>lambdaQuery()
                        .select(User::getId)
                        .eq(User::getStaffId, entity.getStaffId())
        );
        UserRole userRole = this.userRoleMapper.selectOne(
                new QueryWrapper<UserRole>().eq("user_id", user.getId()).eq("role_id", role.getId())
        );
        if (null == userRole) {
            userRole = new UserRole();
            userRole.setRoleId(role.getId());
            userRole.setUserId(user.getId());
            this.userRoleMapper.insert(userRole);
        }
        return entity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StaffResearchLeader updateWithRole(StaffResearchLeader entity, boolean updateAllFields) {
        if (updateAllFields) {
            this.baseMapper.updateAllFieldsById(entity);
        } else {
            this.baseMapper.updateById(entity);
        }

        Role role = this.roleMapper.selectOne(new QueryWrapper<Role>().eq("name", "ROLE_JIAOYANZUZHANG"));
        User user = this.userMapper.selectById(
                Wrappers.<User>lambdaQuery()
                        .select(User::getId)
                        .eq(User::getStaffId, entity.getStaffId())
        );
        if (YesNoEnum.NO.equals(entity.getIsCurrent())) {
            this.userRoleMapper.delete(new QueryWrapper<UserRole>().eq("user_id", user.getId()).eq("role_id", role.getId()));
        } else {
            UserRole userRole = this.userRoleMapper.selectOne(
                    new QueryWrapper<UserRole>().eq("user_id", user.getId()).eq("role_id", role.getId())
            );
            if (null == userRole) {
                userRole = new UserRole();
                userRole.setRoleId(role.getId());
                userRole.setUserId(user.getId());
                this.userRoleMapper.insert(userRole);
            }
        }

        return entity;
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
    public boolean saveBatch(Collection<StaffResearchLeader> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(StaffResearchLeaderBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }


}
