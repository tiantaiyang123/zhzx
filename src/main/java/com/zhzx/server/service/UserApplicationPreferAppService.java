/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：手机app用户首页应用表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.UserApplicationPreferApp;

import java.util.List;

public interface UserApplicationPreferAppService extends IService<UserApplicationPreferApp> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(UserApplicationPreferApp entity);


    Integer createOrUpdate(List<Long> idList);
}
