/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：学生表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.Student;
import com.zhzx.server.enums.StudentTypeEnum;
import com.zhzx.server.vo.StudentInfoVo;
import com.zhzx.server.vo.StudentParamVo;
import com.zhzx.server.vo.StudentVo;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;
import java.util.Map;

public interface StudentService extends IService<Student> {

    /**
     * 更新全部字段
     *
     * @param entity
     * @return
     */
    int updateAllFieldsById(Student entity);

    IPage<Map<String, Object>> queryStudentByPage(IPage<Student> page, QueryWrapper<Student> wrapper);

    Student saveStudent(Student entity);

    Student updateStudent(Student entity, boolean updateAllFields);

    List<Student> listByClazz(Long clazzId);

    void importExcel(Long schoolyardId, Long academicYearSemesterId, Long gradeId, String fileUrl);

    List<StudentVo> selectListWithClazz(StudentTypeEnum studentTypeEnum,Long senderId);

    Integer studentCount(Long gradeId,Long clazzId,StudentTypeEnum studentTypeEnum);

    IPage<StudentInfoVo> selectInfoByPage(IPage<StudentInfoVo> page, StudentParamVo param);

    List<Student> listByClazzStudent(Long clazzId, Long academicYearSemesterId, String studentName);

    XSSFWorkbook exportExcel(StudentParamVo param);

    void updateTimeWhenClazzChange(Long studentId, Long studentClazzId);
}
