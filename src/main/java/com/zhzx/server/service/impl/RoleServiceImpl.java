/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：角色表
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
import com.zhzx.server.service.RoleService;
import com.zhzx.server.repository.RoleMapper;
import com.zhzx.server.repository.base.RoleBaseMapper;
import com.zhzx.server.domain.Role;
import com.zhzx.server.rest.req.RoleParam;
import org.springframework.transaction.annotation.Transactional;
import com.zhzx.server.domain.RoleAuthority;
import com.zhzx.server.domain.Authority;
import com.zhzx.server.service.RoleAuthorityService;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Resource
    private RoleAuthorityService roleAuthorityService;

    @Override
    public int updateAllFieldsById(Role entity) {
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
    public boolean saveBatch(Collection<Role> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(RoleBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }


    /**
     * 更新权限
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Role updateAuthority(Role entity) {
        this.roleAuthorityService.remove(Wrappers.<RoleAuthority>query().eq("role_id", entity.getId()));

        List<RoleAuthority> roleAuthorities = new ArrayList<>();
        for (Authority authority : entity.getAuthorityList()) {
            RoleAuthority roleAuthority = new RoleAuthority();
            roleAuthority.setRoleId(entity.getId());
            roleAuthority.setAuthorityId(authority.getId());
            roleAuthorities.add(roleAuthority);
        }
        this.roleAuthorityService.saveBatch(roleAuthorities);
        return this.getById(entity.getId());
    }
}
