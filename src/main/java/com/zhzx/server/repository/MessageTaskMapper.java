/**
* 项目：中华中学管理平台
* 模型分组：消息管理
* 模型名称：消息任务表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.repository;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.dto.MessageTaskDto;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;
import com.zhzx.server.domain.MessageTask;
import com.zhzx.server.repository.base.MessageTaskBaseMapper;

@Repository
public interface MessageTaskMapper extends MessageTaskBaseMapper {

    List<MessageTaskDto> getPage(@Param("page") Page authorityPage,
                                    @Param("ew") QueryWrapper<MessageTask> wrapper);
}
