/**
* 项目：中华中学管理平台
* 模型分组：成绩管理
* 模型名称：考试赋分模板表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.ExamGoalNaturalTemplate;
import com.zhzx.server.vo.ExamGoalNaturalTemplateVo;

import java.util.List;

public interface ExamGoalNaturalTemplateService extends IService<ExamGoalNaturalTemplate> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(ExamGoalNaturalTemplate entity);

    void deleteAll(ExamGoalNaturalTemplateVo ExamGoalNaturalTemplateVo);

    List<ExamGoalNaturalTemplate> saveAll(ExamGoalNaturalTemplateVo ExamGoalNaturalTemplateVo);
}
