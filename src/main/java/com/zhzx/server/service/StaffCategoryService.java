/**
* 项目：中华中学管理平台
* 模型分组：系统管理
* 模型名称：老师分类表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.StaffCategory;
import com.zhzx.server.rest.req.StaffCategoryParam;

public interface StaffCategoryService extends IService<StaffCategory> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(StaffCategory entity);


}
