/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：意见与建议推送表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.CommentProcess;
import com.zhzx.server.domain.CommentTask;
import com.zhzx.server.dto.CommentProcessDto;
import com.zhzx.server.dto.CommentTaskDto;
import com.zhzx.server.rest.req.CommentProcessParam;

public interface CommentProcessService extends IService<CommentProcess> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(CommentProcess entity);


    IPage<CommentProcessDto> headmasterPageDetail(IPage page, QueryWrapper<CommentProcess> wrapper);

    int headmasterVerify(CommentProcess commentProcess);

    CommentProcessDto headmasterDetail(Long commentProcessId);
}
