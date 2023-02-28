/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：早读情况表
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
import com.zhzx.server.domain.MorningReading;
import com.zhzx.server.domain.MorningReadingImages;
import com.zhzx.server.domain.User;
import com.zhzx.server.enums.ClassifyEnum;
import com.zhzx.server.enums.CommentStateEnum;
import com.zhzx.server.enums.GradeEnum;
import com.zhzx.server.repository.MorningReadingImagesMapper;
import com.zhzx.server.repository.MorningReadingMapper;
import com.zhzx.server.repository.base.MorningReadingBaseMapper;
import com.zhzx.server.service.CommentService;
import com.zhzx.server.service.MorningReadingService;
import com.zhzx.server.util.StringUtils;
import com.zhzx.server.vo.CheckItemVo;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MorningReadingServiceImpl extends ServiceImpl<MorningReadingMapper, MorningReading> implements MorningReadingService {

    @Resource
    private CommentService commentService;
    @Resource
    private MorningReadingImagesMapper morningReadingImagesMapper;
    @Override
    public int updateAllFieldsById(MorningReading entity) {
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
    public boolean saveBatch(Collection<MorningReading> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(MorningReadingBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    @Override
    public MorningReading createOrUpdate(MorningReading entity) {
        if(entity.getId() == null){
            entity.setCheckTime(new Date());
            entity.setDefault().validate(true);
            this.baseMapper.insert(entity);
        }else{
            this.baseMapper.updateById(entity);
        }
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(CollectionUtils.isNotEmpty(entity.getCommentList())) {
            List<Long> commentIds = entity.getCommentList().stream().filter(commentDto -> commentDto.getId() != null).map(commentDto -> commentDto.getId()).collect(Collectors.toList());
            List<Comment> commentList =  commentService.list(Wrappers.<Comment>lambdaQuery()
                    .like(Comment::getClassify,"DAY_MORNING_READING")
                    .eq(Comment::getDailyRoutineId,entity.getId())
                    .notIn(CollectionUtils.isNotEmpty(commentIds),Comment::getId,commentIds)
            );
            if(CollectionUtils.isNotEmpty(commentList)){
                commentService.removeByIds(commentList.stream().map(comment -> comment.getId()).collect(Collectors.toList()));
            }
            entity.getCommentList().stream().forEach(commentDto -> {
                if (commentDto.getId() != null) {
                    Comment comment = commentService.getById(commentDto.getId());
                    if (comment != null) {
                        commentDto.setState(comment.getState());
                    } else {
                        commentDto.setState(CommentStateEnum.TO_BE_PUSHED);
                    }
                }
                if (commentDto.getId() == null || commentDto.getState().equals(CommentStateEnum.TO_BE_PUSHED)) {
                    commentDto.setDailyRoutineId(entity.getId());
                    commentDto.setEditor(user.getRealName());
                    commentDto.setStartTime(entity.getStartTime());
                    commentDto.setEndTime(entity.getEndTime());
                    commentDto.setSchoolyardId(entity.getSchoolyardId());
                    commentDto.setDefault().validate(true);
                    commentService.createOrUpdateComment(commentDto);
                }
            });
        }
        morningReadingImagesMapper.delete(Wrappers.<MorningReadingImages>lambdaQuery()
                .eq(MorningReadingImages::getMorningReadingId,entity.getId())
        );
        if(CollectionUtils.isNotEmpty(entity.getMorningReadingImagesList()) ){

            entity.getMorningReadingImagesList().stream().forEach(southGateImages -> {
                southGateImages.setMorningReadingId(entity.getId());
                southGateImages.setDefault().validate(true);
            });
            morningReadingImagesMapper.batchInsert(entity.getMorningReadingImagesList());
        }
        return this.getById(entity.getId());
    }

    @Override
    public IPage pageDetail(IPage page, QueryWrapper<MorningReading> wrapper) {
        List<MorningReading> southGates = this.baseMapper.pageDetail(page,wrapper);
        southGates.stream().forEach(morningReading -> {
            List<CheckItemVo> checkItemVos = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                CheckItemVo checkItemVo = new CheckItemVo();
                checkItemVo.setStartTime(morningReading.getStartTime());
                checkItemVo.setEndTime(morningReading.getEndTime());
                if (i == 0) {
                    checkItemVo.setCheckSubItem("高一年级");
                    checkItemVo.setCheckResult(morningReading.getReadingOrder1().getName());
                    checkItemVo.setShift(morningReading.getEvaluationLevel1().getName());
                }
                if (i == 1) {
                    checkItemVo.setCheckSubItem("高二年级");
                    checkItemVo.setCheckResult(morningReading.getReadingOrder2().getName());
                    checkItemVo.setShift(morningReading.getEvaluationLevel2().getName());
                }
                if (i == 2) {
                    checkItemVo.setCheckSubItem("高三年级");
                    checkItemVo.setCheckResult(morningReading.getReadingOrder3().getName());
                    checkItemVo.setShift(morningReading.getEvaluationLevel3().getName());
                }
                String urls = "" ;
                for (MorningReadingImages image : morningReading.getMorningReadingImagesList()) {
                    if (i == 0 && image.getImageClassify().equals(GradeEnum.ONE)) {
                        urls = urls + ',' + image.getUrl();
                    } else if (i == 1 && image.getImageClassify().equals(GradeEnum.TWO)) {
                        urls = urls + ',' + image.getUrl();
                    } else if (i == 2 && image.getImageClassify().equals(GradeEnum.THREE)) {
                        urls = urls + ',' + image.getUrl();
                    }
                }
                if (!StringUtils.isNullOrEmpty(urls)) {
                    checkItemVo.setImageUrls(urls.substring(1));
                }
                if (i == 0 ) {
                    String comment = morningReading.getCommentList().stream()
                            .filter(commentDto -> ClassifyEnum.DAY_MORNING_READING_1.equals(commentDto.getClassify()))
                            .map(commentDto -> commentDto.getContent()).collect(Collectors.joining(","));
                    checkItemVo.setComment(comment);
                } else if (i == 1 ) {
                    String comment = morningReading.getCommentList().stream()
                            .filter(commentDto -> ClassifyEnum.DAY_MORNING_READING_2.equals(commentDto.getClassify()))
                            .map(commentDto -> commentDto.getContent()).collect(Collectors.joining(","));
                    checkItemVo.setComment(comment);
                } else if (i == 2 ) {
                    String comment = morningReading.getCommentList().stream()
                            .filter(commentDto -> ClassifyEnum.DAY_MORNING_READING_3.equals(commentDto.getClassify()))
                            .map(commentDto -> commentDto.getContent()).collect(Collectors.joining(","));
                    checkItemVo.setComment(comment);
                }
                checkItemVo.setUpdateTime(morningReading.getCheckTime());
                checkItemVo.setLeaderName(morningReading.getLeaderName());
                checkItemVos.add(checkItemVo);
            }
            morningReading.setCheckItemVoList(checkItemVos);
        });
        page.setRecords(southGates);
        return page;
    }
}
