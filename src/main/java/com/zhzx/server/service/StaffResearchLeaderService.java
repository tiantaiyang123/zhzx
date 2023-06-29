/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：教研组长表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.StaffResearchLeader;

public interface StaffResearchLeaderService extends IService<StaffResearchLeader> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(StaffResearchLeader entity);


    StaffResearchLeader addWithRole(StaffResearchLeader entity);

    StaffResearchLeader updateWithRole(StaffResearchLeader entity, boolean updateAllFields);
}
