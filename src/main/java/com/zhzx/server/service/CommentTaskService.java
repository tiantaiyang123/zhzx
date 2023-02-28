/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：意见与建议处理表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.CommentTask;
import com.zhzx.server.dto.CommentTaskDto;
import com.zhzx.server.rest.req.CommentTaskParam;

public interface CommentTaskService extends IService<CommentTask> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(CommentTask entity);


    IPage<CommentTaskDto> pageDetail(IPage page, QueryWrapper<CommentTask> wrapper);

    CommentTaskDto detail(Long commentTaskId);

    int verify(CommentTaskDto commentTaskDto);
}
