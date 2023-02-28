/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：大课间活动情况表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.repository;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;
import com.zhzx.server.domain.BreakActivity;
import com.zhzx.server.repository.base.BreakActivityBaseMapper;

@Repository
public interface BreakActivityMapper extends BreakActivityBaseMapper {


    List<BreakActivity> pageDetail(IPage page,@Param(Constants.WRAPPER) QueryWrapper<BreakActivity> wrapper);
}
