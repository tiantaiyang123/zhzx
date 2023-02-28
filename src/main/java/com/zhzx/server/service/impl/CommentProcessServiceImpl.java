/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：意见与建议推送表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.*;
import com.zhzx.server.dto.CommentProcessDto;
import com.zhzx.server.dto.CommentTaskDto;
import com.zhzx.server.enums.CommentStateEnum;
import com.zhzx.server.enums.ReceiverEnum;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.repository.*;
import com.zhzx.server.repository.base.CommentProcessBaseMapper;
import com.zhzx.server.service.CommentProcessService;
import com.zhzx.server.util.JsonUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommentProcessServiceImpl extends ServiceImpl<CommentProcessMapper, CommentProcess> implements CommentProcessService {
    @Resource
    private FunctionDepartmentMapper functionDepartmentMapper;
    @Resource
    private MessageMapper messageMapper;
    @Resource
    private CommentTaskMapper commentTaskMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private CommentImagesMapper commentImagesMapper;

    @Override
    public int updateAllFieldsById(CommentProcess entity) {
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
    public boolean saveBatch(Collection<CommentProcess> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(CommentProcessBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    @Override
    public IPage<CommentProcessDto> headmasterPageDetail(IPage page, QueryWrapper<CommentProcess> wrapper) {
        List<CommentProcessDto> commentProcessDtoList = this.baseMapper.headmasterPageDetail(page,wrapper);
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

        commentProcessDtoList.stream().forEach(commentProcessDto -> {
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
            });
            commentProcessDto.setColumnMap(columnMap);
            commentProcessDto.setOfficeDepartment(officeSb.toString());
            commentProcessDto.setGradeDepartment(gradeSb.toString());
        });
        page.setRecords(commentProcessDtoList);
        return  page;
    }

    @Override
    @Transactional
    public int headmasterVerify(CommentProcess commentProcess) {
        messageMapper.update(new Message(),Wrappers.<Message>lambdaUpdate()
                .set(Message::getIsWrite, YesNoEnum.YES)
                .set(Message::getIsRead,YesNoEnum.YES)
                .eq(Message::getName,"commentProcess_"+commentProcess.getId())
        );
        this.baseMapper.update(new CommentProcess(),Wrappers.<CommentProcess>lambdaUpdate()
                .set(CommentProcess::getState, CommentStateEnum.PENDING)
                .set(CommentProcess::getInstructions, commentProcess.getInstructions())
                .eq(CommentProcess::getId,commentProcess.getId())
        );
        List<CommentTask> commentTaskList = commentTaskMapper.selectList(Wrappers.<CommentTask>lambdaQuery()
                .eq(CommentTask::getCommentProcessId,commentProcess.getId())
        );
        List<Message> messageList = new ArrayList<>();
        for (CommentTask commentTask : commentTaskList) {
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
            FunctionDepartment functionDepartment = functionDepartmentMapper.selectById(commentTask.getFunctionDepartmentId());
            User user = userMapper.selectById(functionDepartment.getPrincipalId());
            message.setReceiverId(user.getStaffId());
            message.setReceiverType(ReceiverEnum.TEACHER);
            message.setSendTime(new Date());
            message.setIsSend(YesNoEnum.YES);
            message.setSendNum(1);
            message.setDefault().validate(true);
            messageList.add(message);
        }
        return this.messageMapper.batchInsert(messageList);

    }

    @Override
    public CommentProcessDto headmasterDetail(Long commentProcessId) {
        IPage page = new Page(1,20);
        QueryWrapper<CommentProcess> wrapper = new QueryWrapper<>();
        wrapper.eq("dcp.id",commentProcessId);
        List<CommentProcessDto>  commentProcessDtos = this.baseMapper.headmasterPageDetail(page,wrapper);
        if(CollectionUtils.isNotEmpty(commentProcessDtos)){
            CommentProcessDto commentProcessDto = commentProcessDtos.get(0);
            List<CommentImages> images = commentImagesMapper.selectList(Wrappers.<CommentImages>lambdaQuery()
                    .eq(CommentImages::getCommentId,commentProcessDto.getCommentId())
            );
            commentProcessDto.setCommentImagesList(images);
            return commentProcessDto;
        }
        return null;
    }


}
