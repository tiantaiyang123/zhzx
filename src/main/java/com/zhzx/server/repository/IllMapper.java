/**
 * 项目：中华中学管理平台
 * 模型分组：学生管理
 * 模型名称：因病缺课表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.zhzx.server.domain.ExamResult;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;
import com.zhzx.server.domain.Ill;
import com.zhzx.server.repository.base.IllBaseMapper;

@Repository
public interface IllMapper extends IllBaseMapper {

    IPage<Ill> searchIll(IPage<Ill> page,
                         @Param("schoolyardId") Long schoolyardId,
                         @Param("academicYearSemesterId") Long academicYearSemesterId,
                         @Param("gradeId") Long gradeId,
                         @Param("clazzId") Long clazzId,
                         @Param("week") Integer week,
                         @Param("registerDateFrom") String registerDateFrom,
                         @Param("registerDateTo") String registerDateTo,
                         @Param("orderByClause") String orderByClause);
}
