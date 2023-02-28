/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：午餐情况表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.Lunch;
import com.zhzx.server.rest.req.LunchParam;
import org.apache.ibatis.annotations.Param;

public interface LunchService extends IService<Lunch> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(Lunch entity);


    Lunch createOrUpdate(Lunch entity);

    IPage pageDetail(IPage page, QueryWrapper<Lunch> wrapper);
}
