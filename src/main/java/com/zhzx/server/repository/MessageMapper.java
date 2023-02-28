/**
* 项目：中华中学管理平台
* 模型分组：消息管理
* 模型名称：消息表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;
import com.zhzx.server.domain.Message;
import com.zhzx.server.repository.base.MessageBaseMapper;

@Repository
public interface MessageMapper extends MessageBaseMapper {


    int batchInsert(@Param("records") List<Message> messageList);

    List<Map<String,Object>> calculateMessage(@Param("time") Date date);
}
