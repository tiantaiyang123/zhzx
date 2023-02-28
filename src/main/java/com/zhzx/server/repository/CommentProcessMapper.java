/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：意见与建议推送表
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
import com.zhzx.server.dto.CommentProcessDto;
import com.zhzx.server.dto.CommentTaskDto;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;
import com.zhzx.server.domain.CommentProcess;
import com.zhzx.server.repository.base.CommentProcessBaseMapper;

@Repository
public interface CommentProcessMapper extends CommentProcessBaseMapper {


    List<CommentProcessDto> headmasterPageDetail(@Param("page")IPage page,
                                                 @Param(Constants.WRAPPER)QueryWrapper<CommentProcess> wrapper);
}
