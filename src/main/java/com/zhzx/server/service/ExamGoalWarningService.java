/**
 * 项目：中华中学管理平台
 * 模型分组：成绩管理
 * 模型名称：考试分数预警表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.ExamGoalWarning;
import com.zhzx.server.dto.exam.ExamGoalDto;

import java.util.List;
import java.util.Map;

public interface ExamGoalWarningService extends IService<ExamGoalWarning> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(ExamGoalWarning entity);

    void updateEnhance(ExamGoalWarning entity);

    Integer insertDefault(List<ExamGoalDto> examGoalDtoList);

    IPage<Map<String, Object>> selectByList(IPage<Map<String, Object>> page, QueryWrapper<ExamGoalWarning> wrapper);
}
