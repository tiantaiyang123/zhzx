/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：学年学期表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.AcademicYearSemester;
import com.zhzx.server.rest.req.AcademicYearSemesterParam;
import com.zhzx.server.vo.SchoolWeek;

public interface AcademicYearSemesterService extends IService<AcademicYearSemester> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(AcademicYearSemester entity);

    void setCurrentYearSemester(Long academicYearSemesterId);

    AcademicYearSemester getCurrentYearSemester(Long academicYearSemesterId);

    AcademicYearSemester getYearSemesterByDate(String dateStr);

    List<SchoolWeek> getWeeks(Long academicYearSemesterId);
}
