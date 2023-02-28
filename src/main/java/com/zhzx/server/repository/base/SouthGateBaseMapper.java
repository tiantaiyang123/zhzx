/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：南大门准备情况表
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
import com.zhzx.server.domain.SouthGate;

@Repository
public interface SouthGateBaseMapper extends BaseMapper<SouthGate> {
    /**
     * 根据 ID 更新所有字段
     *
     * @param entity 实体对象
     */
    int updateAllFieldsById(@Param(Constants.ENTITY) SouthGate entity);

    /**
     * 批量插入
     *
     * @param records 实体对象列表
     */
    int batchInsert(@Param("records") List<SouthGate> records);
}
