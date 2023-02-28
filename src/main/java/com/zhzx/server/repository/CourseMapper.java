/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：课程表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.repository;

import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;
import com.zhzx.server.repository.base.CourseBaseMapper;

import java.util.List;

@Repository
public interface CourseMapper extends CourseBaseMapper {

    void updateClazzId(@Param("academicYearSemesterId") Long academicYearSemesterId,
                       @Param("gradeId") Long gradeId);

    void updateTeacherId(@Param("academicYearSemesterId") Long academicYearSemesterId,
                             @Param("gradeId") Long gradeId);

    List<Long> getMeetingTime(@Param("teacherIdList")List<Long> teacherIdList, @Param("week")Integer week);
}
