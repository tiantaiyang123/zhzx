/**
* 项目：中华中学管理平台
* 模型分组：消息管理
* 模型名称：消息发送人员表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.repository;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;
import com.zhzx.server.domain.MessageTaskReceiver;
import com.zhzx.server.repository.base.MessageTaskReceiverBaseMapper;

@Repository
public interface MessageTaskReceiverMapper extends MessageTaskReceiverBaseMapper {

    int batchInsert(@Param("records") List<MessageTaskReceiver> records);
}
