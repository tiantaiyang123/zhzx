/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：南大门准备情况表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.repository;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.zhzx.server.domain.Clazz;
import com.zhzx.server.vo.ClazzVo;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;
import com.zhzx.server.domain.SouthGate;
import com.zhzx.server.repository.base.SouthGateBaseMapper;

@Repository
public interface SouthGateMapper extends SouthGateBaseMapper {

    List<SouthGate> pageDetail(IPage<SouthGate> page,
                               @Param(Constants.WRAPPER) QueryWrapper<SouthGate> wrapper);
}
