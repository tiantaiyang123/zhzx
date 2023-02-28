/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：学校管理
 * 模型名称：考试目标表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.ExamGoal;
import com.zhzx.server.dto.exam.ExamGoalDto;
import com.zhzx.server.vo.ExamGoalMutateVo;
import com.zhzx.server.vo.ExamGoalVo;

import java.util.List;
import java.util.Map;

public interface ExamGoalService extends IService<ExamGoal> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(ExamGoal entity);

    int removeAll(Long id);

    Map<String, Object> goalStationSchool(Long examId, Long academicYearSemesterId, Long gradeId);

    List<Map<String, Object>> goalStation(Long examId, Long goalId, Long clazzId);

    List<ExamGoalDto> getAllGoal(Long examId, Long goalId);

    List<ExamGoalDto> getDefault(Long examId, Long goalId);

    List<ExamGoal> addUpdateTotal(Long examId, List<ExamGoal> examGoalList);

    ExamGoal addUpdateEach(ExamGoalVo examGoalVo);

    Map<String, Object> getGoalTotal(Long clazzId, Long examId, List<ExamGoalDto> examGoalDtoList);

    Map<String, Object> getGoalSubject(Long clazzId, Long examId, List<ExamGoalDto> examGoalDtoList);

    int updateTotalGoalScoreCache(ExamGoalMutateVo examGoalMutateVo);

    void importExcel(Long examId, String fileUrl);
}
