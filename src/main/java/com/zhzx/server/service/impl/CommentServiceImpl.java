/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：意见与建议表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Resource;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.*;
import com.zhzx.server.dto.CommentDto;
import com.zhzx.server.dto.CommentTaskDto;
import com.zhzx.server.enums.CommentProcessSourceEnum;
import com.zhzx.server.enums.CommentStateEnum;
import com.zhzx.server.enums.ReceiverEnum;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.repository.*;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.MessageService;
import com.zhzx.server.service.UserService;
import com.zhzx.server.util.JsonUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zhzx.server.service.CommentService;
import com.zhzx.server.repository.base.CommentBaseMapper;
import com.zhzx.server.rest.req.CommentParam;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Resource
    private CommentProcessMapper commentProcessMapper;
    @Resource
    private CommentTaskMapper commentTaskMapper;
    @Resource
    private FunctionDepartmentMapper functionDepartmentMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private CommentImagesMapper commentImagesMapper;
    @Resource
    private TeacherDutyMapper teacherDutyMapper;
    @Resource
    private MessageMapper messageMapper;


    @Override
    public int updateAllFieldsById(Comment entity) {
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
    public boolean saveBatch(Collection<Comment> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(CommentBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    @Override
    @Transactional
    public CommentDto create(CommentDto entity) {
        return this.createOrUpdateComment(entity);
    }

    @Override
    public Integer removeById(Long id) {
        return this.baseMapper.removeById(id);
    }

    @Override
    public Integer removeByIds(List<Long> ids) {
        return this.baseMapper.removeByIds(ids);
    }

    @Override
    public CommentDto createOrUpdateComment(CommentDto entity) {
        if(entity.getId() == null){
            Comment comment = new Comment();
            BeanUtils.copyProperties(entity,comment);
            comment.setStartTime(new Date());
            comment.setEndTime(new Date());
            comment.setEditor(((User)SecurityUtils.getSubject().getPrincipal()).getRealName());
            comment.setDefault().validate(true);
            this.baseMapper.insert(comment);
            entity.setId(comment.getId());
            entity.setState(comment.getState());
        }else{
            Comment comment = new Comment();
            BeanUtils.copyProperties(entity,comment);
            comment.setDefault().validate(true);
            this.baseMapper.updateById(comment);
            Comment comment1 = this.baseMapper.selectById(comment.getId());
            entity.setId(comment.getId());
            entity.setState(comment1.getState());
        }
        if(CollectionUtils.isNotEmpty(entity.getPicList())){
            List<CommentImages> commentImagesList = new ArrayList<>();
            entity.getPicList().stream().forEach(pic->{
                CommentImages commentImages = new CommentImages();
                commentImages.setCommentId(entity.getId());
                commentImages.setUrl(pic);
                commentImages.setDefault().validate(true);
                commentImagesList.add(commentImages);
            });
            commentImagesMapper.batchInsert(commentImagesList);
        }
        return entity;
    }

    @Override
    public IPage<Object> deanPageDetail(IPage page, QueryWrapper<Comment> wrapper) {
        List<CommentDto> commentDtoList = this.baseMapper.deanPageDetail(page,wrapper);
        List<FunctionDepartment> functionDepartments = functionDepartmentMapper.selectList(Wrappers.<FunctionDepartment>lambdaQuery()
                .eq(FunctionDepartment::getParentId,1).or().eq(FunctionDepartment::getParentId,2)
        );
        Map<Object,Object> columnMap = new HashMap<>();

        List<FunctionDepartment> officeDepartment = functionDepartments.stream().filter(functionDepartment -> functionDepartment.getParentId().equals(1L)).collect(Collectors.toList());
        List<FunctionDepartment> gradeDepartment = functionDepartments.stream().filter(functionDepartment -> functionDepartment.getParentId().equals(2L)).collect(Collectors.toList());

        //部门
        Map<Long,List<FunctionDepartment>> officeMap = officeDepartment.stream().collect(Collectors.groupingBy(FunctionDepartment::getId));
        List<Map<String,Object>> columnList = new ArrayList<>();
        officeMap.keySet().forEach(id->{
            Map<String,Object> column = new HashMap<>();
            column.put("id",id);
            column.put("name",officeMap.get(id).get(0).getName());
            columnList.add(column);
        });
        columnMap.put("office",columnList);

        //年级组
        List<Map<String,Object>> gradeColumnList = new ArrayList<>();
        Map<Long,List<FunctionDepartment>> gradeMap = gradeDepartment.stream().collect(Collectors.groupingBy(FunctionDepartment::getId));
        gradeMap.keySet().forEach(id->{
            Map<String,Object> column = new HashMap<>();
            column.put("id",id);
            column.put("name",gradeMap.get(id).get(0).getName());
            gradeColumnList.add(column);
        });
        columnMap.put("grade",gradeColumnList);


        commentDtoList.stream().forEach(commentDto -> {
            commentDto.setColumnMap(columnMap);
            commentDto.getCommentProcessDtoList().stream().forEach(commentProcessDto -> {
                Map<Long,CommentTaskDto> taskDtoMap = new HashMap<>();


                StringBuilder gradeSb = new StringBuilder();
                StringBuilder officeSb = new StringBuilder();
                commentProcessDto.getCommentTaskDtoList().stream().forEach(commentTaskDto -> {
                    if(officeMap.containsKey(commentTaskDto.getFunctionDepartmentId())){
                        officeSb.append(officeMap.get(commentTaskDto.getFunctionDepartmentId()).get(0).getName());
                        officeSb.append(",");
                    }else if(gradeMap.containsKey(commentTaskDto.getFunctionDepartmentId())){
                        gradeSb.append(gradeMap.get(commentTaskDto.getFunctionDepartmentId()).get(0).getName());
                        gradeSb.append(",");
                    }
                    taskDtoMap.put(commentTaskDto.getFunctionDepartmentId(),commentTaskDto);
                });
                commentProcessDto.setTaskDtoMap(taskDtoMap);
                commentProcessDto.setGradeDepartment(gradeSb.toString());
                commentProcessDto.setOfficeDepartment(officeSb.toString());
            });
        });
        page.setRecords(commentDtoList);
        return page;
    }

    @Override
    @Transactional
    public CommentDto push(CommentDto entity) {

        if(entity.getId() == null) throw new ApiCode.ApiException(-5,"id 必传");
        this.baseMapper.removeProcessByCommentId(entity.getId());
        List<Message> messageList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(entity.getOfficeDepartmentList())){
            CommentProcess commentProcess = new CommentProcess();
            commentProcess.setHeadmaster(((User)SecurityUtils.getSubject().getPrincipal()).getRealName());
            commentProcess.setNeedInstruction(entity.getNeedInstruction());
            commentProcess.setCommentId(entity.getId());
            commentProcess.setSource(CommentProcessSourceEnum.OFFICE);
            commentProcess.setState(CommentStateEnum.PENDING);
            if(YesNoEnum.YES.equals(entity.getNeedInstruction())){
                commentProcess.setState(CommentStateEnum.TO_BE_INSTRUCTION);
            }
            commentProcess.setDefault().validate(true);
            this.commentProcessMapper.insert(commentProcess);
            List<CommentTask> commentTasks = new ArrayList<>();
            entity.getOfficeDepartmentList().stream().forEach(grade->{
                CommentTask commentTask = new CommentTask();
                commentTask.setCommentProcessId(commentProcess.getId());
                commentTask.setFunctionDepartmentId(grade.getId());
                User principal = userMapper.selectById(grade.getPrincipalId());
                if(principal == null){
                    throw new ApiCode.ApiException(-5,"未查询到职能部门负责人");
                }
                commentTask.setPrincipal(principal.getRealName());
                commentTask.setUserId(principal.getStaffId());
                commentTask.setDefault().validate(true);
                commentTasks.add(commentTask);
            });
            commentTaskMapper.batchInsert(commentTasks);
            //校长室推送
            if(YesNoEnum.YES.equals(entity.getNeedInstruction())){
                User user = ((User)SecurityUtils.getSubject().getPrincipal());
                messageList.addAll(parseMessage(commentProcess,null,user));
            }else{
                messageList.addAll(parseMessage(null,commentTasks,null));
            }
        }
        if(CollectionUtils.isNotEmpty(entity.getGradeDepartmentList())){
            CommentProcess commentProcess = new CommentProcess();
            commentProcess.setHeadmaster(((User)SecurityUtils.getSubject().getPrincipal()).getRealName());
            commentProcess.setCommentId(entity.getId());
            commentProcess.setNeedInstruction(entity.getNeedInstruction());
            commentProcess.setSource(CommentProcessSourceEnum.GRADE);
            commentProcess.setState(CommentStateEnum.PENDING);
            if(YesNoEnum.YES.equals(entity.getNeedInstruction())){
                commentProcess.setState(CommentStateEnum.TO_BE_INSTRUCTION);
            }
            commentProcess.setDefault().validate(true);
            this.commentProcessMapper.insert(commentProcess);
            List<CommentTask> commentTasks = new ArrayList<>();
            entity.getGradeDepartmentList().stream().forEach(grade->{
                CommentTask commentTask = new CommentTask();
                commentTask.setCommentProcessId(commentProcess.getId());
                commentTask.setFunctionDepartmentId(grade.getId());
                User principal = userMapper.selectById(grade.getPrincipalId());
                if(principal == null){
                    throw new ApiCode.ApiException(-5,"未查询到职能部门负责人");
                }
                commentTask.setPrincipal(principal.getRealName());
                commentTask.setUserId(principal.getStaffId());
                commentTask.setDefault().validate(true);
                commentTasks.add(commentTask);
            });
            commentTaskMapper.batchInsert(commentTasks);

            //校长室推送
            if(YesNoEnum.YES.equals(entity.getNeedInstruction())){
                User user = ((User)SecurityUtils.getSubject().getPrincipal());
                messageList.addAll(parseMessage(commentProcess,null,user));
            }else{
                messageList.addAll(parseMessage(null,commentTasks,null));
            }
        }
        entity.setState(CommentStateEnum.PUSHED);
        this.baseMapper.updateById(entity);
        this.messageMapper.batchInsert(messageList);
        return entity;
    }

    @Override
    public Map<String, Object> searchByNightStudyId(Long nightStudyId) {

        Map<String, Object> map = new HashMap<>();
        List<CommentDto> commentDtoList = teacherDutyMapper.queryCommentById(nightStudyId);
        List<CommentImages> commentImagesList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(commentDtoList)){
            commentDtoList.stream().forEach(commentDto -> {
                if(CollectionUtils.isNotEmpty(commentDto.getCommentImagesList())){
                    commentImagesList.addAll(commentDto.getCommentImagesList());
                }
            });
        }
        map.put("comment",teacherDutyMapper.queryCommentById(nightStudyId));
        map.put("picList", commentImagesList);
        return map;
    }

    private List<Message> parseMessage(CommentProcess commentProcess,List<CommentTask> commentTaskList,User user){

        if(commentProcess != null){
            Message message = new Message();
            message.setMessageTaskId(-2L);
            message.setName("commentProcess_"+commentProcess.getId());
            message.setTitle("一日常规推送");
            Map<String,Object> map = new HashMap<>();
            map.put("commentProcess_"+commentProcess.getId(),commentProcess.getId());
            map.put("commentProcess",commentProcess);
            message.setContent(JsonUtils.toJson(map));
            message.setSenderId(-1L);
            message.setSenderName("系统");
            message.setReceiverName(user.getRealName());
            message.setReceiverId(user.getStaffId());
            message.setReceiverType(ReceiverEnum.TEACHER);
            message.setSendTime(new Date());
            message.setIsSend(YesNoEnum.YES);
            message.setDefault().validate(true);
            return new ArrayList<Message>(){{this.add(message);}};
        }else if(commentTaskList != null){
            return commentTaskList.stream().map(commentTask->{
                Message message = new Message();
                message.setMessageTaskId(-2L);
                message.setName("commentTask_"+commentTask.getId());
                message.setTitle("一日常规推送");
                Map<String,Object> map = new HashMap<>();
                map.put("commentTask_"+commentTask.getId(),commentTask.getId());
                map.put("commentTask",commentTask);
                message.setContent(JsonUtils.toJson(map));
                message.setSenderId(-1L);
                message.setSenderName("系统");
                message.setReceiverName(commentTask.getPrincipal());
                message.setReceiverId(commentTask.getUserId());
                message.setReceiverType(ReceiverEnum.TEACHER);
                message.setSendTime(new Date());
                message.setIsSend(YesNoEnum.YES);
                message.setDefault().validate(true);
                return message;
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }


}
