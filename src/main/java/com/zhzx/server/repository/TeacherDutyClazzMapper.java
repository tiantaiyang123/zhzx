/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：教师值班班级表
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
import com.zhzx.server.enums.TeacherDutyTypeEnum;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;
import com.zhzx.server.domain.TeacherDutyClazz;
import com.zhzx.server.repository.base.TeacherDutyClazzBaseMapper;

@Repository
public interface TeacherDutyClazzMapper extends TeacherDutyClazzBaseMapper {


    void updateClazzId(@Param("teacherDutyClazzIds") List<Long> teacherDutyClazzIds,
                       @Param("gradeId")Long gradeId,
                       @Param("academicYearSemesterId")Long academicYearSemesterId);

    Integer leaderConfirm(@Param("teacherDutyId") Long teacherDutyId);

    Long getTeacherDutyId(@Param("time") Date time,
                          @Param("clazzId") Long clazzId,
                          @Param("dutyType") String dutyType);

    int batchInsertWithId(@Param("records") List<TeacherDutyClazz> teacherDutyClazzes);

    /**
     * app中根据班级的集合循环更新day_teacher_duty_clazz表中的数据
     */
    Integer updateByClazzIds(@Param("newId")Long newId,@Param("clazzIds")List<Long> clazzIds,@Param("oldId")Long oldId);
}
