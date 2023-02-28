/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：课程表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.Clazz;
import com.zhzx.server.domain.Course;

import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface CourseService extends IService<Course> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(Course entity);

    List<Map<String, String>> getList(Long academicYearSemesterId, Long  gradeId,Long clazzId);

    BufferedImage getImage(Long academicYearSemesterId, Long  gradeId, Long clazzId) throws Exception;

    void importExcel(Long academicYearSemesterId, Long  gradeId, String fileUrl);

    Map<String,Object> getCurrentCourse(Integer gradeId, Date time);

    Course getCurrentCourseByClazzId(Clazz clazz, Date time) ;

    Map<String,Object> getStaffTeacherCourse(Long staffId);

    Map<String,Object> getStaffTeacherClazz(Long staffId);

    List<Long> getMeetingTime(List<Long> teacherIdList, Integer week);

    List<Map<String, String>> getListSubject(Long academicYearSemesterId, Long  gradeId,Long subjectId);
}
