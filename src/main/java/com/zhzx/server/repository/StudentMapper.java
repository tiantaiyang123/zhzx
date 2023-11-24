/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：学生表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.zhzx.server.domain.Clazz;
import com.zhzx.server.domain.Student;
import com.zhzx.server.repository.base.StudentBaseMapper;
import com.zhzx.server.vo.StudentInfoVo;
import com.zhzx.server.vo.StudentParamVo;
import com.zhzx.server.vo.StudentVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface StudentMapper extends StudentBaseMapper {

    void updateDormitory(@Param("clazzId") Long clazzId,
                         @Param("gradeId") Long gradeId,
                         @Param("academicYearSemesterId") Long academicYearSemesterId);

    IPage<Map<String, Object>> queryStudentByPage(IPage<Student> page,
                                                  @Param(Constants.WRAPPER) QueryWrapper<Student> wrapper);

    List<Student> listByClazz(@Param("clazzId") Long clazzId);

    List<Student> listByClazzStudent(@Param("clazzId") Long clazzId,
                                     @Param("academicYearSemesterId") Long academicYearSemesterId,
                                     @Param("studentName") String studentName);

    Clazz getCurrentClazzByStudentId(@Param("studentId") Long studentId);

    List<StudentVo> selectListWithClazz(@Param("studentTypeEnum") String studentTypeEnum,
                                        @Param("senderId")Long senderId);

    Integer count(@Param("studentType") String studentType,
                  @Param("gradeId") Long gradeId,
                  @Param("clazzId") Long clazzId);

    IPage<StudentInfoVo> selectInfoByPage(IPage<StudentInfoVo> page,
                                          @Param("param") StudentParamVo param);

    List<Student> listSimpleIncrStudent(@Param("param") StudentParamVo param);
}
