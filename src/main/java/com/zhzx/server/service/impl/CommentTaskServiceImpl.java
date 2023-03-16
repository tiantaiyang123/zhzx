/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：意见与建议处理表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.*;
import com.zhzx.server.dto.CommentProcessDto;
import com.zhzx.server.dto.CommentTaskDto;
import com.zhzx.server.enums.CommentStateEnum;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zhzx.server.service.CommentTaskService;
import com.zhzx.server.repository.base.CommentTaskBaseMapper;
import com.zhzx.server.rest.req.CommentTaskParam;

@Service
public class CommentTaskServiceImpl extends ServiceImpl<CommentTaskMapper, CommentTask> implements CommentTaskService {

    @Resource
    private CommentProcessMapper commentProcessMapper;
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private MessageMapper messageMapper;
    @Resource
    private CommentImagesMapper commentImagesMapper;

    @Override
    public int updateAllFieldsById(CommentTask entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
    }

    /**
     * 批量插入
     *
     * @param entityList ignore
     * @param batchSize  ignore
     * @return ignore
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveBatch(Collection<CommentTask> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(CommentTaskBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    @Override
    public IPage<CommentTaskDto> pageDetail(IPage page, QueryWrapper<CommentTask> wrapper) {
        List<CommentTaskDto> commentTasks = this.baseMapper.pageDetail(page,wrapper);
        page.setRecords(commentTasks);
        return page;
    }

    @Override
    public CommentTaskDto detail(Long commentTaskId) {
        CommentTaskDto commentTaskDto = new CommentTaskDto();
        CommentTask commentTask = this.baseMapper.selectById(commentTaskId);
        BeanUtils.copyProperties(commentTask,commentTaskDto);
        CommentProcess commentProcess = commentProcessMapper.selectById(commentTask.getCommentProcessId());
        Comment comment = commentMapper.selectById(commentProcess.getCommentId());
        List<CommentImages> images = commentImagesMapper.selectList(Wrappers.<CommentImages>lambdaQuery()
                .eq(CommentImages::getCommentId,commentProcess.getCommentId())
        );
        commentTaskDto.setCommentImagesList(images);
        commentTaskDto.setStartTime(comment.getStartTime());
        commentTaskDto.setEndTime(comment.getEndTime());
        commentTaskDto.setInfoFillPeople(comment.getEditor());
        commentTaskDto.setCommentContent(comment.getContent());
        commentTaskDto.setInstructions(commentProcess.getInstructions());
        messageMapper.update(new Message(),Wrappers.<Message>lambdaUpdate()
                .set(Message::getIsRead, YesNoEnum.YES)
                .eq(Message::getName,"commentTask_"+commentTask.getId())
        );
        return commentTaskDto;
    }

    @Override
    @Transactional
    public int verify(CommentTaskDto commentTaskDto) {
        //修改部门推送信息
        this.baseMapper.update(new CommentTask(),Wrappers.<CommentTask>lambdaUpdate()
                .set(CommentTask::getContent, commentTaskDto.getContent())
                .eq(CommentTask::getId,commentTaskDto.getId())
        );
        IPage page = new Page<>(1, 100);
        QueryWrapper<CommentProcess> wrapper = new QueryWrapper<>();
        wrapper.eq("dcp.id", commentTaskDto.getCommentProcessId());
        //查询信息，修改上级状态
        List<CommentProcessDto> commentProcessDtoList = commentProcessMapper.headmasterPageDetail(page,wrapper);
        if(CollectionUtils.isNotEmpty(commentProcessDtoList)){
            for(CommentProcessDto commentProcessDto:commentProcessDtoList){
                if(CollectionUtils.isNotEmpty(commentProcessDto.getCommentTaskDtoList())){
                    List<CommentTaskDto> commentTaskDtoList = commentProcessDto.getCommentTaskDtoList().stream().filter(item-> StringUtils.isBlank(item.getContent())).collect(Collectors.toList());
                    if(CollectionUtils.isEmpty(commentTaskDtoList)){
                       commentProcessMapper.update(new CommentProcess(),Wrappers.<CommentProcess>lambdaUpdate()
                               .set(CommentProcess::getState,CommentStateEnum.PROCESSED)
                               .eq(CommentProcess::getId,commentProcessDto.getId())
                       );
                       List<CommentProcess> commentProcessList = commentProcessMapper.selectList(Wrappers.<CommentProcess>lambdaQuery()
                               .eq(CommentProcess::getCommentId,commentProcessDto.getCommentId())
                       );
                        commentProcessList = commentProcessList.stream().filter(item->!CommentStateEnum.PROCESSED.equals(item.getState())).collect(Collectors.toList());
                       //修改上级信息
                       if(CollectionUtils.isEmpty(commentProcessList)){
                           commentMapper.update(new Comment(),Wrappers.<Comment>lambdaUpdate()
                                   .set(Comment::getState,CommentStateEnum.PROCESSED)
                                   .eq(Comment::getId, commentProcessDto.getCommentId())
                           );
                       }
                    }
                }
            }
        }
        return messageMapper.update(new Message(),Wrappers.<Message>lambdaUpdate()
                .set(Message::getIsWrite, YesNoEnum.YES)
                .set(Message::getIsRead, YesNoEnum.YES)
                .eq(Message::getName,"commentTask_"+commentTaskDto.getId())
        );
    }
}
