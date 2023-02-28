/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：用户角色关系表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.UserRole;
import com.zhzx.server.rest.req.UserRoleParam;

public interface UserRoleService extends IService<UserRole> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(UserRole entity);


}
