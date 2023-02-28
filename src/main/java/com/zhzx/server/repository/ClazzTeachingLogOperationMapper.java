/**
 * 项目：中华中学管理平台
 * 模型分组：学生管理
 * 模型名称：班级教学日志作业量反馈表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.vo.ClazzTeachingLogOperationVo;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;
import com.zhzx.server.domain.ClazzTeachingLogOperation;
import com.zhzx.server.repository.base.ClazzTeachingLogOperationBaseMapper;

@Repository
public interface ClazzTeachingLogOperationMapper extends ClazzTeachingLogOperationBaseMapper {


    List<ClazzTeachingLogOperationVo> pageDetail(Page<Object> objectPage,
                                                 @Param("schoolyardId") Long schoolyardId,
                                                 @Param("academicYearSemesterId") Long academicYearSemesterId,
                                                 @Param("gradeId") Long gradeId,
                                                 @Param("clazzId") Long clazzId,
                                                 @Param("week") Integer week,
                                                 @Param("startTime") Date startTime,
                                                 @Param("endTime") Date endTime);
}
