/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：教师值班代班表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.repository;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.zhzx.server.dto.TeacherDutyGradeTotalSubstitueDto;
import com.zhzx.server.dto.TeacherDutySubstituteDto;
import com.zhzx.server.rest.req.TeacherDutySubstituteParam;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;
import com.zhzx.server.domain.TeacherDutySubstitute;
import com.zhzx.server.repository.base.TeacherDutySubstituteBaseMapper;

@Repository
public interface TeacherDutySubstituteMapper extends TeacherDutySubstituteBaseMapper {


    List<TeacherDutySubstituteDto> searchMyLogPage(@Param("page") IPage<TeacherDutySubstituteDto> page,
                                                   @Param("param") TeacherDutySubstituteParam param);

    List<TeacherDutyGradeTotalSubstitueDto> searchMyLogPageGradeTotal(@Param("page") IPage<TeacherDutyGradeTotalSubstitueDto> page,
                                                                      @Param("param") TeacherDutySubstituteParam param);
}
