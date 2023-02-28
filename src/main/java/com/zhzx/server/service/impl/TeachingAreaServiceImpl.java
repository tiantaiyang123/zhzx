/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：教学区秩序表
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
import com.zhzx.server.domain.TeachingArea;
import com.zhzx.server.domain.TeachingAreaImages;
import com.zhzx.server.domain.User;
import com.zhzx.server.enums.ClassifyEnum;
import com.zhzx.server.enums.CommentStateEnum;
import com.zhzx.server.enums.GradeEnum;
import com.zhzx.server.repository.TeachingAreaImagesMapper;
import com.zhzx.server.repository.TeachingAreaMapper;
import com.zhzx.server.repository.base.TeachingAreaBaseMapper;
import com.zhzx.server.service.CommentService;
import com.zhzx.server.service.TeachingAreaService;
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
public class TeachingAreaServiceImpl extends ServiceImpl<TeachingAreaMapper, TeachingArea> implements TeachingAreaService {

    @Resource
    private CommentService commentService;
    @Resource
    private TeachingAreaImagesMapper teachingAreaImagesMapper;
    @Override
    public int updateAllFieldsById(TeachingArea entity) {
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
    public boolean saveBatch(Collection<TeachingArea> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(TeachingAreaBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    @Override
    public TeachingArea createOrUpdate(TeachingArea entity) {
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
                    .like(Comment::getClassify,"DAY_TEACHING_AREA")
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
                if(commentDto.getId() == null || commentDto.getState().equals(CommentStateEnum.TO_BE_PUSHED)) {
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
        teachingAreaImagesMapper.delete(Wrappers.<TeachingAreaImages>lambdaQuery()
                .eq(TeachingAreaImages::getTeachingAreaId,entity.getId())
        );
        if(CollectionUtils.isNotEmpty(entity.getTeachingAreaImagesList())){

            entity.getTeachingAreaImagesList().stream().forEach(southGateImages -> {
                southGateImages.setTeachingAreaId(entity.getId());
                southGateImages.setDefault().validate(true);
            });
            teachingAreaImagesMapper.batchInsert(entity.getTeachingAreaImagesList());
        }
        return this.getById(entity.getId());
    }

    @Override
    public IPage pageDetail(IPage page, QueryWrapper<TeachingArea> wrapper) {
        List<TeachingArea> southGates = this.baseMapper.pageDetail(page,wrapper);
        southGates.stream().forEach(teachingArea -> {
            List<CheckItemVo> checkItemVos = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                CheckItemVo checkItemVo = new CheckItemVo();
                checkItemVo.setStartTime(teachingArea.getStartTime());
                checkItemVo.setEndTime(teachingArea.getEndTime());
                if (i == 0) {
                    checkItemVo.setCheckSubItem("高一年级");
                    checkItemVo.setCheckResult(teachingArea.getTeachingAreaOrder1().getName());
                }
                if (i == 1) {
                    checkItemVo.setCheckSubItem("高二年级");
                    checkItemVo.setCheckResult(teachingArea.getTeachingAreaOrder2().getName());
                }
                if (i == 2) {
                    checkItemVo.setCheckSubItem("高三年级");
                    checkItemVo.setCheckResult(teachingArea.getTeachingAreaOrder3().getName());
                }
                String urls = "" ;
                for (TeachingAreaImages image : teachingArea.getTeachingAreaImagesList()) {
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
                    String comment = teachingArea.getCommentList().stream()
                            .filter(commentDto -> ClassifyEnum.DAY_TEACHING_AREA_1.equals(commentDto.getClassify()))
                            .map(commentDto -> commentDto.getContent()).collect(Collectors.joining(","));
                    checkItemVo.setComment(comment);
                } else if (i == 1 ) {
                    String comment = teachingArea.getCommentList().stream()
                            .filter(commentDto -> ClassifyEnum.DAY_TEACHING_AREA_2.equals(commentDto.getClassify()))
                            .map(commentDto -> commentDto.getContent()).collect(Collectors.joining(","));
                    checkItemVo.setComment(comment);
                } else if (i == 2 ) {
                    String comment = teachingArea.getCommentList().stream()
                            .filter(commentDto -> ClassifyEnum.DAY_TEACHING_AREA_3.equals(commentDto.getClassify()))
                            .map(commentDto -> commentDto.getContent()).collect(Collectors.joining(","));
                    checkItemVo.setComment(comment);
                }
                checkItemVo.setUpdateTime(teachingArea.getCheckTime());
                checkItemVo.setLeaderName(teachingArea.getLeaderName());
                checkItemVos.add(checkItemVo);
            }
            teachingArea.setCheckItemVoList(checkItemVos);
        });
        page.setRecords(southGates);
        return page;
    }
}
