/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：角色表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.Role;
import com.zhzx.server.rest.req.RoleParam;

public interface RoleService extends IService<Role> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(Role entity);

    /**
     * 更新权限
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    Role updateAuthority(Role entity);

}
