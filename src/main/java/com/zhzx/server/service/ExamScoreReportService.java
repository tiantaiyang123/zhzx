/**
 * 项目：中华中学管理平台
 * 模型分组：成绩管理
 * 模型名称：成绩单表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.ExamScoreReport;

import java.util.List;

public interface ExamScoreReportService extends IService<ExamScoreReport> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(ExamScoreReport entity);

    void importExcel(String fileUrl, Long academicYearSemesterId, Long subjectId, Long clazzId);

    void calculate(Long subjectId, Long clazzId, Long examIdMiddle, Long examIdEnd);

    Object batchCreateOrUpdate(Long subjectId, Long clazzId, List<ExamScoreReport> entityList);
}
