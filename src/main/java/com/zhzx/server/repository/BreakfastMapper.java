/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：早餐情况表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.repository;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;
import com.zhzx.server.domain.Breakfast;
import com.zhzx.server.repository.base.BreakfastBaseMapper;

@Repository
public interface BreakfastMapper extends BreakfastBaseMapper {


    List<Breakfast> pageDetail(IPage page,
                                        @Param(Constants.WRAPPER)QueryWrapper<Breakfast> wrapper);
}
