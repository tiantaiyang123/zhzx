/**
* 项目：中华中学管理平台
* 模型分组：系统管理
* 模型名称：老师微信发送人关联表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.repository;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.zhzx.server.domain.StaffCategoryRelation;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;
import com.zhzx.server.domain.StaffMessageReceiverRelation;
import com.zhzx.server.repository.base.StaffMessageReceiverRelationBaseMapper;

@Repository
public interface StaffMessageReceiverRelationMapper extends StaffMessageReceiverRelationBaseMapper {


    int batchInsert(@Param("records") List<StaffMessageReceiverRelation> records);
}
