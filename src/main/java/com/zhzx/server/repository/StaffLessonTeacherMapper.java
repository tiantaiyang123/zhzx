/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：任课老师表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.repository;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.zhzx.server.dto.StaffLessonTeacherDto;
import com.zhzx.server.enums.YesNoEnum;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;
import com.zhzx.server.domain.StaffLessonTeacher;
import com.zhzx.server.repository.base.StaffLessonTeacherBaseMapper;

@Repository
public interface StaffLessonTeacherMapper extends StaffLessonTeacherBaseMapper {

    List<StaffLessonTeacherDto> selectByGradeAndClazz(@Param("gradeId") Long gradeId,
                                                      @Param("subjectId") Long subjectId,
                                                      @Param("examId") Long examId,
                                                      @Param("entity") List<Long> clazzIds);
}
