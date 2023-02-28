/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：教学区秩序表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.TeachingArea;
import com.zhzx.server.rest.req.TeachingAreaParam;

public interface TeachingAreaService extends IService<TeachingArea> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(TeachingArea entity);


    TeachingArea createOrUpdate(TeachingArea entity);

    IPage pageDetail(IPage page, QueryWrapper<TeachingArea> wrapper);
}
