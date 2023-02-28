/**
 * 项目：中华中学管理平台
 * 模型分组：成绩管理
 * 模型名称：考试分数预警表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.zhzx.server.domain.ExamGoalWarning;
import com.zhzx.server.repository.base.ExamGoalWarningBaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface ExamGoalWarningMapper extends ExamGoalWarningBaseMapper {

    IPage<Map<String, Object>> selectMap(IPage<Map<String, Object>> page,
                                         @Param(Constants.WRAPPER) QueryWrapper<ExamGoalWarning> wrapper);
}
