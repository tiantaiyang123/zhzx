/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：班级表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.zhzx.server.domain.Clazz;
import com.zhzx.server.repository.base.ClazzBaseMapper;
import com.zhzx.server.vo.ClazzVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface ClazzMapper extends ClazzBaseMapper {
    IPage<ClazzVo> pageDetail(IPage<ClazzVo> page,
                                      @Param(Constants.WRAPPER) QueryWrapper<Clazz> wrapper);

    List<ClazzVo> clazzWithDutyTeacher(@Param("time") Date time,
                                       @Param("dutyType")String dutyType);

//    List<Clazz> getList(@Param("schoolyardId") Long schoolyardId,
//                        @Param("academicYear") String academicYear,
//                        @Param("gradeId") Long gradeId);

    Integer resetClazzStudentNum();

    List<ClazzVo> selectByTeacherDutyId(@Param("teacherDutyId") Long teacherDutyId);

    Map<String, String> getClazzSubjects(@Param("clazzId") Long clazzId);
}
