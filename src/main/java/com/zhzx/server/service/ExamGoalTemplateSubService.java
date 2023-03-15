/**
 * 项目：中华中学管理平台
 * 模型分组：成绩管理
 * 模型名称：目标模板详情表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.ExamGoalTemplateSub;
import com.zhzx.server.vo.ExamGoalTemplateVo;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;
import java.util.Map;

public interface ExamGoalTemplateSubService extends IService<ExamGoalTemplateSub> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(ExamGoalTemplateSub entity);

    List<ExamGoalTemplateSub> createOrUpdate(ExamGoalTemplateVo examGoalTemplateVo);

    void insertExamGoals(Long examGoalTemplateId, Long examId);

    Map<String, Object> searchDetail(Long academicYearSemesterId, Long gradeId, Long examGoalTemplateId);

    XSSFWorkbook exportExcel(Long id);
}
