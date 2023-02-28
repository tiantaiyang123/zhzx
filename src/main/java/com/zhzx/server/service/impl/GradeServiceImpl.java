/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：年级表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.AcademicYearSemester;
import com.zhzx.server.domain.Grade;
import com.zhzx.server.domain.Schoolyard;
import com.zhzx.server.enums.TeacherDutyTypeEnum;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.repository.ClazzMapper;
import com.zhzx.server.repository.GradeMapper;
import com.zhzx.server.repository.base.GradeBaseMapper;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.AcademicYearSemesterService;
import com.zhzx.server.service.GradeService;
import com.zhzx.server.service.SchoolyardService;
import com.zhzx.server.util.CellUtils;
import com.zhzx.server.util.StringUtils;
import com.zhzx.server.vo.ClazzVo;
import com.zhzx.server.vo.GradeVo;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GradeServiceImpl extends ServiceImpl<GradeMapper, Grade> implements GradeService {
    @Resource
    private AcademicYearSemesterService academicYearSemesterService;

    @Resource
    private ClazzMapper clazzMapper;

    @Override
    public int updateAllFieldsById(Grade entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
    }

    /**
     * 批量插入
     *
     * @param entityList ignore
     * @param batchSize  ignore
     * @return ignore
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveBatch(Collection<Grade> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(GradeBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    @Override
    public List<ClazzVo> getGradeList(Date time, TeacherDutyTypeEnum teacherDutyTypeEnum) {
        List<ClazzVo> clazzList = clazzMapper.clazzWithDutyTeacher(time, teacherDutyTypeEnum.toString());
        return clazzList;
    }

//    @Override
//    public List<Grade> getCurrentGrades() {
//        List<Grade> grades = new ArrayList<>();
//        AcademicYearSemester academicYearSemester = academicYearSemesterService.getCurrentAcademicYearSemester(null);
//        if (academicYearSemester != null) {
//            QueryWrapper<Grade> gradeQueryWrapper = new QueryWrapper<Grade>().eq("academic_year", academicYearSemester.getYear());
//            grades = this.list(gradeQueryWrapper);
//        }
//        return grades;
//    }

//    @Override
//    public List<Grade> getSemesterGrades(Long academicYearSemesterId) {
//        List<Grade> grades = new ArrayList<>();
//        AcademicYearSemester academicYearSemester = academicYearSemesterService.getById(academicYearSemesterId);
//        if (academicYearSemester != null) {
//            QueryWrapper<Grade> gradeQueryWrapper = new QueryWrapper<Grade>().eq("academic_year", academicYearSemester.getYear());
//            grades = this.list(gradeQueryWrapper);
//        }
//        return grades;
//    }

//    @Value("${web.upload-path}")
//    private String uploadPath;
//
//    @Resource
//    private SchoolyardService schoolyardService;
//
//    @Transactional(rollbackFor = Exception.class)
//    @Override
//    public void importExcel(String fileUrl) {
//        if (fileUrl == null || fileUrl == "")
//            throw new ApiCode.ApiException(-1, "没有上传文件！");
//        String[] items = fileUrl.split("/");
//        File file = new File(uploadPath + "\\" + items[items.length - 2] + "\\" + items[items.length - 1]);
//        if (!file.exists())
//            throw new ApiCode.ApiException(-1, "文件不存在！");
//        try {
//            // 读取 Excel
//            XSSFWorkbook book = new XSSFWorkbook(file);
//            XSSFSheet sheet = book.getSheetAt(0);
//            file.delete();
//            //获得总行数
//            int rowNum = sheet.getPhysicalNumberOfRows();
//            // 年级列表
//            List<Grade> grades = new ArrayList<>();
//            // 现有校区列表
//            List<Schoolyard> currentSchoolyards = schoolyardService.list();
//            // 现有学年列表
//            List<AcademicYearSemester> currentAcademicYearSemesters = academicYearSemesterService.list();
//            // 错误行
//            StringBuilder sb = new StringBuilder();
//            String errText = "";
//            // 读取单元格数据
//            for (int rowIndex = 1; rowIndex < rowNum; rowIndex++) {
//                errText = "";
//                String schoolyardId = "";
//                String schoolyardName = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(0));
//                String academicYear = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(1));
//                String name = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(2));
//                String clazzCount = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(3));
//                String gradeLeader = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(4));
//                String teamLeader = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(5));
//                String isGraduation = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(6));
//                String levelA = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(7));
//                String levelB = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(8));
//                String levelC = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(9));
//                String levelD = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(10));
//                String levelE = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(11));
//                clazzCount = StringUtils.isNullOrEmpty(clazzCount) ? "0" : clazzCount;
//                isGraduation = "是".equals(isGraduation) ? YesNoEnum.YES.toString() : YesNoEnum.NO.toString();
//                if (StringUtils.isNullOrEmpty(schoolyardName)) {
//                    errText += "\t校区不存在";
//                } else {
//                    List<Schoolyard> list = currentSchoolyards.stream().filter(item -> item.getName().equals(schoolyardName))
//                            .collect(Collectors.toList());
//                    schoolyardId = list.size() == 0 ? "" : String.valueOf(list.get(0).getId());
//                    if (StringUtils.isNullOrEmpty(schoolyardId)) {
//                        errText += "\t校区不存在";
//                    }
//                }
//                if (StringUtils.isNullOrEmpty(academicYear)) {
//                    errText += "\t学年不存在";
//                } else {
//                    List<AcademicYearSemester> list = currentAcademicYearSemesters.stream().filter(item -> item.getYear().equals(academicYear))
//                            .collect(Collectors.toList());
//                    if (list.size() == 0) {
//                        errText += "\t学年不存在";
//                    }
//                }
//                if (StringUtils.isNullOrEmpty(name)) {
//                    errText += "\t名称";
//                } else if ("".equals(errText)) {
//                    QueryWrapper<Grade> queryWrapper = new QueryWrapper<Grade>()
//                            .eq("schoolyard_id", Long.parseLong(schoolyardId))
//                            .eq("academic_year", academicYear)
//                            .eq("name", name);
//                    List<Grade> list = this.list(queryWrapper);
//                    if (list.size() != 0) {
//                        errText += "\t该校区学年下名称已存在";
//                    } else {
//                        final Long finalSchoolyardId = Long.parseLong(schoolyardId);
//                        List<Grade> gradeList = grades.stream().filter(item -> item.getSchoolyardId() == finalSchoolyardId && item.getAcademicYear().equals(academicYear) && item.getName().equals(name))
//                                .collect(Collectors.toList());
//                        if (gradeList.size() != 0) {
//                            errText += "\t该校区学年下名称已存在";
//                        }
//                    }
//                }
//                if (!StringUtils.isNullOrEmpty(errText))
//                    sb.append(errText = "第" + rowIndex + "行 " + errText + "\n");
//                if (StringUtils.isNullOrEmpty(errText)) {
//                    Grade grade = new Grade();
//                    grade.setSchoolyardId(Long.parseLong(schoolyardId));
//                    grade.setAcademicYear(academicYear);
//                    grade.setName(name);
//                    grade.setClazzCount((int) Double.parseDouble(clazzCount));
//                    grade.setGradeLeader(gradeLeader);
//                    grade.setTeamLeader(teamLeader);
//                    grade.setIsGraduation(YesNoEnum.valueOf(isGraduation));
//                    grade.setAcademyRatioA(Long.valueOf(levelA));
//                    grade.setAcademyRatioB(Long.valueOf(levelB));
//                    grade.setAcademyRatioC(Long.valueOf(levelC));
//                    grade.setAcademyRatioD(Long.valueOf(levelD));
//                    grade.setAcademyRatioE(Long.valueOf(levelE));
//                    grades.add(grade);
//                }
//            }
//            if (sb.length() != 0)
//                throw new ApiCode.ApiException(-1, "数据有错！\n" + sb.toString());
//            this.saveBatch(grades, grades.size());
//        } catch (IOException | InvalidFormatException e) {
//            throw new ApiCode.ApiException(-1, "导入数据失败！");
//        }
//    }
//
//    @Override
//    public XSSFWorkbook exportExcel() throws IOException {
//        InputStream is = getClass().getResourceAsStream("/static/templates/年级模板.xlsx");
//        XSSFWorkbook book = new XSSFWorkbook(is);
//        XSSFSheet sheet = book.getSheetAt(0);
//        XSSFCellStyle style = book.createCellStyle();
//        style.setBorderLeft(BorderStyle.THIN);
//        style.setBorderTop(BorderStyle.THIN);
//        style.setBorderRight(BorderStyle.THIN);
//        style.setBorderBottom(BorderStyle.THIN);
//        int rowNum = sheet.getPhysicalNumberOfRows();
//        List<Grade> grades = this.list();
//        if (grades.size() == 0) {
//            for (int i = 0; i < rowNum; i++)
//                for (int j = 0; j < sheet.getRow(i).getPhysicalNumberOfCells(); j++)
//                    sheet.getRow(i).getCell(j).setCellValue("");
//        }
//        for (int i = 0; i < grades.size(); i++) {
//            Grade grade = grades.get(i);
//            XSSFRow row = sheet.createRow(i + 1);
//            XSSFCell cell = row.createCell(0);
//            cell.setCellStyle(style);
//            cell.setCellValue(grade.getSchoolyard().getName());
//
//            cell = row.createCell(1);
//            cell.setCellStyle(style);
//            cell.setCellValue(grade.getAcademicYear());
//
//            cell = row.createCell(2);
//            cell.setCellStyle(style);
//            cell.setCellValue(grade.getName());
//
//            cell = row.createCell(3);
//            cell.setCellStyle(style);
//            cell.setCellValue(grade.getClazzCount());
//
//            cell = row.createCell(4);
//            cell.setCellStyle(style);
//            cell.setCellValue(grade.getGradeLeader());
//
//            cell = row.createCell(5);
//            cell.setCellStyle(style);
//            cell.setCellValue(grade.getTeamLeader());
//
//            cell = row.createCell(6);
//            cell.setCellStyle(style);
//            cell.setCellValue(grade.getIsGraduation().getName());
//
//            cell = row.createCell(7);
//            cell.setCellStyle(style);
//            cell.setCellValue(grade.getAcademyRatioA());
//
//            cell = row.createCell(8);
//            cell.setCellStyle(style);
//            cell.setCellValue(grade.getAcademyRatioB());
//
//            cell = row.createCell(9);
//            cell.setCellStyle(style);
//            cell.setCellValue(grade.getAcademyRatioC());
//
//            cell = row.createCell(10);
//            cell.setCellStyle(style);
//            cell.setCellValue(grade.getAcademyRatioD());
//
//            cell = row.createCell(11);
//            cell.setCellStyle(style);
//            cell.setCellValue(grade.getAcademyRatioE());
//        }
//        return book;
//    }
}
