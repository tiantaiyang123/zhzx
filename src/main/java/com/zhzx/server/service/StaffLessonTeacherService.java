/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：任课老师表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.StaffLessonTeacher;
import com.zhzx.server.dto.StaffLessonTeacherDto;
import com.zhzx.server.rest.req.StaffLessonTeacherParam;
import org.apache.ibatis.annotations.Param;

public interface StaffLessonTeacherService extends IService<StaffLessonTeacher> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(StaffLessonTeacher entity);

    List<StaffLessonTeacherDto> selectByGradeAndClazz(Long gradeId, Long subjectId, Long examId, List<Long> clazzIds);

}
