/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：南大门准备情况表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.SouthGate;
import com.zhzx.server.rest.req.SouthGateParam;

public interface SouthGateService extends IService<SouthGate> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(SouthGate entity);


    SouthGate createOrUpdate(SouthGate entity);

    IPage<SouthGate> pageDetail(IPage<SouthGate> page, QueryWrapper<SouthGate> wrapper);
}
