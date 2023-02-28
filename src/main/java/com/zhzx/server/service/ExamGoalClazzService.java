/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：学校管理
 * 模型名称：考试目标班级表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.ExamGoalClazz;
import com.zhzx.server.rest.req.ExamGoalClazzParam;

public interface ExamGoalClazzService extends IService<ExamGoalClazz> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(ExamGoalClazz entity);


    ExamGoalClazz saveExamGoalClazz(ExamGoalClazz entity);

    ExamGoalClazz updateExamGoalClazz(ExamGoalClazz entity, boolean updateAllFields);

    void deleteExamGoalClazz(Long id);

    int saveBatchExamGoalClazz(List<ExamGoalClazz> entityList);
}
