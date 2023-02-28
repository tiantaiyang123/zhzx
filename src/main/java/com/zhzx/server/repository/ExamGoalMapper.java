/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：学校管理
 * 模型名称：考试目标表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.repository;

import com.zhzx.server.dto.MapResultHandlerDto;
import com.zhzx.server.dto.exam.*;
import com.zhzx.server.repository.base.ExamGoalBaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamGoalMapper extends ExamGoalBaseMapper {

    void getGoalForEachClazz(@Param("examId") Long examId, MapResultHandlerDto<Long, String> mapResultHandler);

    List<ExamGoalDto> getAllGoal(@Param("examId") Long examId, @Param("goalId") Long goalId);

    List<ExamGoalClazzTotalDto> getClazzGoalTotal(@Param("clazzId") Long clazzId, @Param("entity") List<ExamGoalDto> params);

    List<ExamGoalClazzSubjectDto> getClazzGoalSubject(@Param("clazzId") Long clazzId, @Param("entity") List<ExamGoalDto> params);

    List<ExamGoalWorkBenchDto> getClazzGoalWorkBench(@Param("entity1") List<Long> clazzIds, @Param("entity") List<ExamGoalDto> params);

}
