/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：大课间活动情况表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.BreakActivity;
import com.zhzx.server.domain.BreakActivityImages;
import com.zhzx.server.domain.Comment;
import com.zhzx.server.domain.User;
import com.zhzx.server.enums.ClassifyEnum;
import com.zhzx.server.enums.CommentStateEnum;
import com.zhzx.server.repository.BreakActivityImagesMapper;
import com.zhzx.server.repository.BreakActivityMapper;
import com.zhzx.server.repository.base.BreakActivityBaseMapper;
import com.zhzx.server.service.BreakActivityService;
import com.zhzx.server.service.CommentService;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BreakActivityServiceImpl extends ServiceImpl<BreakActivityMapper, BreakActivity> implements BreakActivityService {

    @Resource
    private BreakActivityImagesMapper breakActivityImagesMapper;
    @Resource
    private CommentService commentService;
    @Override
    public int updateAllFieldsById(BreakActivity entity) {
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
    public boolean saveBatch(Collection<BreakActivity> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(BreakActivityBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    @Override
    public BreakActivity createOrUpdate(BreakActivity entity) {
        if(entity.getId() == null){
            entity.setCheckTime(new Date());
            entity.setDefault().validate(true);
            this.baseMapper.insert(entity);
        }else{
            this.baseMapper.updateById(entity);
        }
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(CollectionUtils.isNotEmpty(entity.getCommentList())){
            List<Long> commentIds = entity.getCommentList().stream().filter(commentDto -> commentDto.getId() != null).map(commentDto -> commentDto.getId()).collect(Collectors.toList());
            List<Comment> commentList =  commentService.list(Wrappers.<Comment>lambdaQuery()
                    .eq(Comment::getClassify,"DAY_BREAK_ACTIVITY")
                    .eq(Comment::getDailyRoutineId,entity.getId())
                    .notIn(CollectionUtils.isNotEmpty(commentIds),Comment::getId,commentIds)
            );
            if(CollectionUtils.isNotEmpty(commentList)){
                commentService.removeByIds(commentList.stream().map(comment -> comment.getId()).collect(Collectors.toList()));
            }

            entity.getCommentList().stream().forEach(commentDto -> {
                if(commentDto.getId() != null){
                    Comment comment = commentService.getById(commentDto.getId());
                    if(comment != null){
                        commentDto.setState(comment.getState());
                    }else{
                        commentDto.setState(CommentStateEnum.TO_BE_PUSHED);
                    }
                }
                if(commentDto.getId() == null || commentDto.getState().equals(CommentStateEnum.TO_BE_PUSHED)){
                    commentDto.setClassify(ClassifyEnum.DAY_BREAK_ACTIVITY);
                    commentDto.setDailyRoutineId(entity.getId());
                    commentDto.setItemName(ClassifyEnum.DAY_BREAK_ACTIVITY.toString());
                    commentDto.setEditor(user.getRealName());
                    commentDto.setStartTime(entity.getStartTime());
                    commentDto.setEndTime(entity.getEndTime());
                    commentDto.setSchoolyardId(entity.getSchoolyardId());
                    commentDto.setDefault().validate(true);
                    commentService.createOrUpdateComment(commentDto);
                }
            });
        }

        breakActivityImagesMapper.delete(Wrappers.<BreakActivityImages>lambdaQuery()
                .eq(BreakActivityImages::getBreakActivityId,entity.getId())
        );
        if(CollectionUtils.isNotEmpty(entity.getBreakActivityImagesList())){

            entity.getBreakActivityImagesList().stream().forEach(southGateImages -> {
                southGateImages.setBreakActivityId(entity.getId());
                southGateImages.setDefault().validate(true);
            });
            breakActivityImagesMapper.batchInsert(entity.getBreakActivityImagesList());
        }
        return this.getById(entity.getId());
    }

    @Override
    public IPage pageDetail(IPage page, QueryWrapper<BreakActivity> wrapper) {
        List<BreakActivity> southGates = this.baseMapper.pageDetail(page,wrapper);
        page.setRecords(southGates);
        return page;
    }
}
