/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：意见与建议表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.Comment;
import com.zhzx.server.dto.CommentDto;
import com.zhzx.server.dto.CommentTaskDto;
import com.zhzx.server.rest.req.CommentParam;

public interface CommentService extends IService<Comment> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(Comment entity);

    CommentDto create(CommentDto entity);

    Integer removeById(Long id);

    Integer removeByIds(List<Long> ids);

    CommentDto createOrUpdateComment(CommentDto entity);

    IPage<Object> deanPageDetail(IPage page, QueryWrapper<Comment> wrapper);

    CommentDto push(CommentDto entity);

    Map<String,Object> searchByNightStudyId(Long nightStudyId);
}
