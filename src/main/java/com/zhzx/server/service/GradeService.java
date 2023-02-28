/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：年级表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.Grade;
import com.zhzx.server.enums.TeacherDutyTypeEnum;
import com.zhzx.server.vo.ClazzVo;
import com.zhzx.server.vo.GradeVo;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface GradeService extends IService<Grade> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(Grade entity);

//    List<Grade> getCurrentGrades();
//
//    List<Grade> getSemesterGrades(Long academicYearSemesterId);

    List<ClazzVo> getGradeList(Date time, TeacherDutyTypeEnum teacherDutyTypeEnum);

//    void importExcel(String fileUrl);
//
//    XSSFWorkbook exportExcel() throws IOException, InvalidFormatException;
}
