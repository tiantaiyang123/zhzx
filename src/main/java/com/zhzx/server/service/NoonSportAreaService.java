/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：午班运动区秩序表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.NoonSportArea;
import com.zhzx.server.rest.req.NoonSportAreaParam;

public interface NoonSportAreaService extends IService<NoonSportArea> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(NoonSportArea entity);


    NoonSportArea createOrUpdate(NoonSportArea entity);

    IPage pageDetail(IPage page, QueryWrapper<NoonSportArea> wrapper);
}
