/**
* 项目：中华中学管理平台
* 模型分组：系统管理
* 模型名称：老师微信表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhzx.server.domain.StaffMessageReceiverRelation;
import com.zhzx.server.enums.ReceiverEnum;
import com.zhzx.server.repository.StaffMessageReceiverRelationMapper;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.StaffMessageReceiverRelationService;
import org.springframework.stereotype.Service;
import com.zhzx.server.service.StaffMessageService;
import com.zhzx.server.repository.StaffMessageMapper;
import com.zhzx.server.domain.StaffMessage;
import com.zhzx.server.rest.req.StaffMessageParam;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StaffMessageServiceImpl extends ServiceImpl<StaffMessageMapper, StaffMessage> implements StaffMessageService {

    @Resource
    private StaffMessageReceiverRelationMapper staffMessageReceiverRelationMapper;

    @Override
    public int updateAllFieldsById(StaffMessage entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
    }

    @Override
    @Transactional
    public StaffMessage create(StaffMessage staffMessage) {
        StaffMessage exist = this.baseMapper.selectOne(Wrappers.<StaffMessage>lambdaQuery().eq(StaffMessage::getStaffId,staffMessage.getStaffId()));
        if(exist != null){
            throw new ApiCode.ApiException(-5,"用户已存在，无法重新创建");
        }
        staffMessage.setDefault().validate(true);
        this.baseMapper.insert(staffMessage);

        List<StaffMessageReceiverRelation> list = new ArrayList<>();
        Map<Long,Long> map = new HashMap<>();
        if(CollectionUtils.isNotEmpty(staffMessage.getTeacherList())){
            staffMessage.getTeacherList().stream().forEach(item->{
                if(!map.containsKey(item.getStaffId())){
                    item.setReceiverId(item.getStaffId());
                    item.setReceiverType(ReceiverEnum.TEACHER);
                    item.setStaffMessageId(staffMessage.getId());
                    item.setDefault().validate(true);
                    list.add(item);
                    map.put(item.getStaffId(),item.getStaffId());
                }
            });
        }
        if(CollectionUtils.isNotEmpty(staffMessage.getStudentList())){
            staffMessage.getStudentList().stream().forEach(item->{
                item.setReceiverId(item.getStudentId());
                item.setReceiverType(ReceiverEnum.STUDENT);
                item.setStaffMessageId(staffMessage.getId());
                item.setDefault().validate(true);
                list.add(item);
            });
        }
        staffMessageReceiverRelationMapper.batchInsert(list);
        return staffMessage;
    }

    @Override
    @Transactional
    public StaffMessage updateWithSonList(StaffMessage staffMessage) {
        StaffMessage exist = this.baseMapper.selectById(staffMessage.getId());
        if(exist == null){
            throw new ApiCode.ApiException(-5,"未查询到记录");
        }
        staffMessageReceiverRelationMapper.delete(Wrappers.<StaffMessageReceiverRelation>lambdaQuery()
                .eq(StaffMessageReceiverRelation::getStaffMessageId,staffMessage.getId())
        );
        List<StaffMessageReceiverRelation> list = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(staffMessage.getTeacherList())){
            Map<Long,Long> map = new HashMap<>();
            for(StaffMessageReceiverRelation staffMessageReceiverRelation : staffMessage.getTeacherList()){
                if(map.containsKey(staffMessageReceiverRelation.getStaffId())){
                    continue;
                }
                staffMessageReceiverRelation.setReceiverId(staffMessageReceiverRelation.getStaffId());
                staffMessageReceiverRelation.setReceiverType(ReceiverEnum.TEACHER);
                staffMessageReceiverRelation.setStaffMessageId(staffMessage.getId());
                staffMessageReceiverRelation.setDefault().validate(true);
                list.add(staffMessageReceiverRelation);
                map.put(staffMessageReceiverRelation.getStaffId(),staffMessageReceiverRelation.getStaffId());
            }
        }
        if(CollectionUtils.isNotEmpty(staffMessage.getStudentList())){
            staffMessage.getStudentList().stream().forEach(item->{
                item.setReceiverId(item.getStudentId());
                item.setReceiverType(ReceiverEnum.STUDENT);
                item.setStaffMessageId(staffMessage.getId());
                item.setDefault().validate(true);
                list.add(item);
            });
        }
        staffMessageReceiverRelationMapper.batchInsert(list);
        return null;
    }

}
