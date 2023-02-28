/**
 * 项目：中华中学管理平台
 * 模型分组：学生管理
 * 模型名称：班级教学日志表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.repository;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;
import com.zhzx.server.domain.ClazzTeachingLog;
import com.zhzx.server.repository.base.ClazzTeachingLogBaseMapper;

@Repository
public interface ClazzTeachingLogMapper extends ClazzTeachingLogBaseMapper {

    List<ClazzTeachingLog> listWeekLog(@Param("schoolyardId") Long schoolyardId,
                                       @Param("schoolyardId") Long academicYearSemesterId,
                                       @Param("schoolyardId") Long gradeId,
                                       @Param("schoolyardId") Long clazzId,
                                       @Param("schoolyardId") Integer week);
}
