/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：班主任表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.StaffClazzAdviser;
import com.zhzx.server.rest.req.StaffClazzAdviserParam;

public interface StaffClazzAdviserService extends IService<StaffClazzAdviser> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(StaffClazzAdviser entity);


}
