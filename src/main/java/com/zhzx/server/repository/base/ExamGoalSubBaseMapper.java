/**
* 项目：中华中学管理平台
* 模型分组：成绩管理
* 模型名称：考试分段表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.repository.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.zhzx.server.domain.ExamGoalSub;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamGoalSubBaseMapper extends BaseMapper<ExamGoalSub> {
    /**
     * 根据 ID 更新所有字段
     *
     * @param entity 实体对象
     */
    int updateAllFieldsById(@Param(Constants.ENTITY) ExamGoalSub entity);

    /**
     * 批量插入
     *
     * @param records 实体对象列表
     */
    int batchInsert(@Param("records") List<ExamGoalSub> records);
}
