/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：手机app应用配置表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.repository;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;
import com.zhzx.server.domain.ApplicationApp;
import com.zhzx.server.repository.base.ApplicationAppBaseMapper;

@Repository
public interface ApplicationAppMapper extends ApplicationAppBaseMapper {


}