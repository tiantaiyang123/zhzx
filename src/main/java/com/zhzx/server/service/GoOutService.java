/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：走读生提前出门表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.GoOut;
import com.zhzx.server.rest.req.GoOutParam;

public interface GoOutService extends IService<GoOut> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(GoOut entity);


    GoOut createOrUpdate(GoOut entity);

    IPage pageDetail(IPage page, QueryWrapper<GoOut> wrapper);
}
