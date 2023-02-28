/**
* 项目：中华中学管理平台
* 模型分组：成绩管理
* 模型名称：小题得分情况表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.repository;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.zhzx.server.domain.ExamGoalSub;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;
import com.zhzx.server.domain.PracticeTopic;
import com.zhzx.server.repository.base.PracticeTopicBaseMapper;

@Repository
public interface PracticeTopicMapper extends PracticeTopicBaseMapper {

    /**
     * 批量插入
     *
     * @param records 实体对象列表
     */
    int batchInsert(@Param("records") List<PracticeTopic> records);

}
