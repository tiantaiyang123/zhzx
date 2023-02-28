/**
* 项目：中华中学管理平台
* 模型分组：成绩管理
* 模型名称：考试分段表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.ExamGoalSub;
import com.zhzx.server.vo.ExamGoalSubTotalVo;
import com.zhzx.server.vo.ExamGoalSubVo;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;
import java.util.Map;

public interface ExamGoalSubService extends IService<ExamGoalSub> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(ExamGoalSub entity);

    List<ExamGoalSub> saveAll(List<ExamGoalSubVo> examGoalSubVos, Long examId, Long subjectId, String subjectType);

    Map<String, Object> partition(ExamGoalSubTotalVo examGoalSubTotalVo);

    Map<String, Object> clazzAverage(Long examId);

    XSSFWorkbook clazzAverageExportExcel(Long examId);

    XSSFWorkbook partitionExportExcel(ExamGoalSubTotalVo examGoalSubTotalVo);
}
