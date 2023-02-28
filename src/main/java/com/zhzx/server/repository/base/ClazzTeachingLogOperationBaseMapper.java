/**
 * 项目：中华中学管理平台
 * 模型分组：学生管理
 * 模型名称：班级教学日志作业量反馈表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.repository.base;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;
import com.zhzx.server.domain.ClazzTeachingLogOperation;

@Repository
public interface ClazzTeachingLogOperationBaseMapper extends BaseMapper<ClazzTeachingLogOperation> {
    /**
     * 根据 ID 更新所有字段
     *
     * @param entity 实体对象
     */
    int updateAllFieldsById(@Param(Constants.ENTITY) ClazzTeachingLogOperation entity);

    /**
     * 批量插入
     *
     * @param records 实体对象列表
     */
    int batchInsert(@Param("records") List<ClazzTeachingLogOperation> records);
}
