/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：教研组组员表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.StaffResearchMember;

public interface StaffResearchMemberService extends IService<StaffResearchMember> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(StaffResearchMember entity);

}
