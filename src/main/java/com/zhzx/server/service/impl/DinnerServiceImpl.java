/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：晚餐情况表
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
import com.zhzx.server.domain.Comment;
import com.zhzx.server.domain.Dinner;
import com.zhzx.server.domain.DinnerImages;
import com.zhzx.server.domain.User;
import com.zhzx.server.enums.ClassifyEnum;
import com.zhzx.server.enums.CommentStateEnum;
import com.zhzx.server.repository.DinnerImagesMapper;
import com.zhzx.server.repository.DinnerMapper;
import com.zhzx.server.repository.base.DinnerBaseMapper;
import com.zhzx.server.service.CommentService;
import com.zhzx.server.service.DinnerService;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DinnerServiceImpl extends ServiceImpl<DinnerMapper, Dinner> implements DinnerService {

    @Resource
    private CommentService commentService;
    @Resource
    private DinnerImagesMapper dinnerImagesMapper;
    @Override
    public int updateAllFieldsById(Dinner entity) {
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
    public boolean saveBatch(Collection<Dinner> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(DinnerBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    @Override
    @Transactional
    public Dinner createOrUpdate(Dinner entity) {
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
                    .eq(Comment::getClassify,"DAY_DINNER")
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
                commentDto.setClassify(ClassifyEnum.DAY_DINNER);
                commentDto.setDailyRoutineId(entity.getId());
                commentDto.setItemName(ClassifyEnum.DAY_DINNER.toString());
                commentDto.setEditor(user.getRealName());
                commentDto.setStartTime(entity.getStartTime());
                commentDto.setEndTime(entity.getEndTime());
                commentDto.setSchoolyardId(entity.getSchoolyardId());
                commentDto.setDefault().validate(true);
                commentService.createOrUpdateComment(commentDto);
            }
        });}


        dinnerImagesMapper.delete(Wrappers.<DinnerImages>lambdaQuery()
                .eq(DinnerImages::getDinnerId,entity.getId())
        );
        if(CollectionUtils.isNotEmpty(entity.getDinnerImagesList())){

            entity.getDinnerImagesList().stream().forEach(southGateImages -> {
                southGateImages.setDinnerId(entity.getId());
                southGateImages.setDefault().validate(true);
            });
            dinnerImagesMapper.batchInsert(entity.getDinnerImagesList());
        }
        return this.getById(entity.getId());
    }

    @Override
    public IPage pageDetail(IPage page, QueryWrapper<Dinner> wrapper) {
        List<Dinner> southGates = this.baseMapper.pageDetail(page,wrapper);
        page.setRecords(southGates);
        return page;
    }
}
