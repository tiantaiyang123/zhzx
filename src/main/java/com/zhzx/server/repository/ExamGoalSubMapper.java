/**
* 项目：中华中学管理平台
* 模型分组：成绩管理
* 模型名称：考试分段表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.repository;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.zhzx.server.domain.ExamGoalSub;
import com.zhzx.server.repository.base.ExamGoalSubBaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamGoalSubMapper extends ExamGoalSubBaseMapper {

    List<ExamGoalSub> selectListSimple(@Param("ew") Wrapper<ExamGoalSub> queryWrapper);

}
