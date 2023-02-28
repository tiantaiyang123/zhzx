/**
 * 项目：中华中学管理平台
 * 模型分组：一日常规管理
 * 模型名称：领导值班代班表
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
import com.zhzx.server.dto.TeacherDutySubstituteDto;
import com.zhzx.server.rest.req.LeaderDutySubstituteParam;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;
import com.zhzx.server.domain.LeaderDutySubstitute;
import com.zhzx.server.repository.base.LeaderDutySubstituteBaseMapper;

@Repository
public interface LeaderDutySubstituteMapper extends LeaderDutySubstituteBaseMapper {


    List<TeacherDutySubstituteDto> searchMyLogPage(@Param("page") IPage<LeaderDutySubstitute> page,@Param("param") LeaderDutySubstituteParam param);
}
