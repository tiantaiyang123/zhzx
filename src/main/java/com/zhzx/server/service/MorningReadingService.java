/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：早读情况表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.MorningReading;
import com.zhzx.server.rest.req.MorningReadingParam;

public interface MorningReadingService extends IService<MorningReading> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(MorningReading entity);


    MorningReading createOrUpdate(MorningReading entity);

    IPage pageDetail(IPage page, QueryWrapper<MorningReading> wrapper);
}
