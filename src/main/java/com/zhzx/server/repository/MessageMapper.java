/**
* 项目：中华中学管理平台
* 模型分组：消息管理
* 模型名称：消息表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.repository;

import com.zhzx.server.domain.Message;
import com.zhzx.server.repository.base.MessageBaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface MessageMapper extends MessageBaseMapper {


    int batchInsert(@Param("records") List<Message> messageList);

    int updateBatch(@Param("messages")List<Message> messages);

    List<Map<String,Object>> calculateMessage(@Param("time") Date date);
}
