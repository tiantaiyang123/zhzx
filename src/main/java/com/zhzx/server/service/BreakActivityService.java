/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：大课间活动情况表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.BreakActivity;
import com.zhzx.server.rest.req.BreakActivityParam;

public interface BreakActivityService extends IService<BreakActivity> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(BreakActivity entity);


    BreakActivity createOrUpdate(BreakActivity entity);

    IPage pageDetail(IPage page, QueryWrapper<BreakActivity> wrapper);
}
