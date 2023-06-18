/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：手机app角色应用范围表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.ApplicationRoleApp;
import com.zhzx.server.rest.req.ApplicationRoleAppParam;

public interface ApplicationRoleAppService extends IService<ApplicationRoleApp> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(ApplicationRoleApp entity);


}
