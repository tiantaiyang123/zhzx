/**
* 项目：中华中学管理平台
* 模型分组：学生管理
* 模型名称：晚自习考勤班级概况表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhzx.server.domain.AcademicYearSemester;
import com.zhzx.server.domain.Clazz;
import com.zhzx.server.domain.NightStudyAttendanceSub;
import com.zhzx.server.enums.StudentNightDutyTypeEnum;
import com.zhzx.server.repository.AcademicYearSemesterMapper;
import com.zhzx.server.repository.ClazzMapper;
import com.zhzx.server.repository.NightStudyAttendanceSubMapper;
import com.zhzx.server.service.NightStudyAttendanceSubService;
import com.zhzx.server.util.DateUtils;
import com.zhzx.server.vo.ClazzTeachingLogSubjectsVo;
import lombok.SneakyThrows;
import org.apache.catalina.webresources.WarResource;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class NightStudyAttendanceSubServiceImpl extends ServiceImpl<NightStudyAttendanceSubMapper, NightStudyAttendanceSub> implements NightStudyAttendanceSubService {

    @Resource
    private ClazzMapper clazzMapper;
    @Resource
    private AcademicYearSemesterMapper academicYearSemesterMapper;

    @Override
    public int updateAllFieldsById(NightStudyAttendanceSub entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
    }

    @Override
    @SneakyThrows
    public XSSFWorkbook exportExcel(Long schoolyardId, Long academicYearSemesterId, Long gradeId, Long clazzId, Date startTime, Date endTime) {
        AcademicYearSemester academicYearSemester = this.academicYearSemesterMapper.selectById(academicYearSemesterId);
        if (startTime.before(academicYearSemester.getStartTime())) startTime = academicYearSemester.getStartTime();
        if (endTime.after(academicYearSemester.getEndTime())) endTime = academicYearSemester.getEndTime();

        List<NightStudyAttendanceSub> nightStudyAttendanceSubList = this.baseMapper.selectListNightAttendance(schoolyardId, academicYearSemesterId, gradeId, clazzId, startTime, endTime);

        InputStream is = getClass().getResourceAsStream("/static/templates/晚自习学生填报统计.xlsx");
        XSSFWorkbook book = new XSSFWorkbook(is);

        XSSFCellStyle style = book.createCellStyle();
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        if (CollectionUtils.isNotEmpty(nightStudyAttendanceSubList)) {
            List<Clazz> clazzList = this.clazzMapper.selectList(Wrappers.<Clazz>lambdaQuery()
                    .eq(schoolyardId != null, Clazz::getSchoolyardId, schoolyardId)
                    .eq(gradeId != null, Clazz::getGradeId, gradeId)
                    .eq(academicYearSemesterId != null, Clazz::getAcademicYearSemesterId, academicYearSemesterId)
                    .eq(clazzId != null, Clazz::getId, clazzId));
            clazzList.sort(Comparator.comparing(Clazz::getGradeId).thenComparing(item -> Integer.parseInt(item.getName().replace("班", ""))));

            Map<Date, List<NightStudyAttendanceSub>> map = nightStudyAttendanceSubList
                    .stream()
                    .collect(Collectors.groupingBy(NightStudyAttendanceSub::getDayTime, TreeMap::new, Collectors.toList()));

            Comparator<NightStudyAttendanceSub> comparator = (a, b) -> {
                Long gradeIda = a.getClazz().getGradeId();
                Long gradeIdb = b.getClazz().getGradeId();
                if (gradeIda.equals(gradeIdb)) {
                    Integer clazzA = Integer.parseInt(a.getClazz().getName().replace("班", ""));
                    Integer clazzB = Integer.parseInt(b.getClazz().getName().replace("班", ""));
                    if (clazzA.equals(clazzB)) {
                        return a.getStage().equals(StudentNightDutyTypeEnum.STAGE_ONE) ? -1 : 1;
                    } else {
                        return clazzA.compareTo(clazzB);
                    }
                } else {
                    return gradeIda.compareTo(gradeIdb);
                }
            };


            int startRow0 = 0, startRow1 = 0;
            XSSFSheet sheet0 = book.getSheet("已填");
            XSSFSheet sheet1 = book.getSheet("未填");
            XSSFRow row0, row1;
            XSSFCell cell0, cell1;
            for (Map.Entry<Date, List<NightStudyAttendanceSub>> entry : map.entrySet()) {
                String head = DateUtils.format(entry.getKey(), "yyyy-MM-dd E");

                CellRangeAddress rangeAddress = new CellRangeAddress(startRow0, startRow0, 0, 6);
                sheet0.addMergedRegion(rangeAddress);

                row0 = sheet0.createRow(startRow0++);
                cell0 = row0.createCell(0, CellType.STRING);
                cell0.setCellStyle(style);
                cell0.setCellValue(head);

                row0 = sheet0.createRow(startRow0++);
                cell0 = row0.createCell(0, CellType.STRING);
                cell0.setCellStyle(style);
                cell0.setCellValue("年级");

                cell0 = row0.createCell(1, CellType.STRING);
                cell0.setCellStyle(style);
                cell0.setCellValue("班级");

                cell0 = row0.createCell(2, CellType.STRING);
                cell0.setCellStyle(style);
                cell0.setCellValue("阶段");

                cell0 = row0.createCell(3, CellType.STRING);
                cell0.setCellStyle(style);
                cell0.setCellValue("应到");

                cell0 = row0.createCell(4, CellType.STRING);
                cell0.setCellStyle(style);
                cell0.setCellValue("实到");

                cell0 = row0.createCell(5, CellType.STRING);
                cell0.setCellStyle(style);
                cell0.setCellValue("总结");

                cell0 = row0.createCell(6, CellType.STRING);
                cell0.setCellStyle(style);
                cell0.setCellValue("值班班长");

                List<NightStudyAttendanceSub> value= entry.getValue();
                value.sort(comparator);

                for (NightStudyAttendanceSub nightStudyAttendanceSub : value) {
                    row0 = sheet0.createRow(startRow0);
                    cell0 = row0.createCell(0, CellType.STRING);
                    cell0.setCellStyle(style);
                    cell0.setCellValue(nightStudyAttendanceSub.getClazz().getGrade().getName());

                    cell0 = row0.createCell(1, CellType.STRING);
                    cell0.setCellStyle(style);
                    cell0.setCellValue(nightStudyAttendanceSub.getClazz().getName());

                    cell0 = row0.createCell(2, CellType.STRING);
                    cell0.setCellStyle(style);
                    cell0.setCellValue(nightStudyAttendanceSub.getStage().getName());

                    cell0 = row0.createCell(3, CellType.STRING);
                    cell0.setCellStyle(style);
                    cell0.setCellValue(nightStudyAttendanceSub.getShouldNum());

                    cell0 = row0.createCell(4, CellType.STRING);
                    cell0.setCellStyle(style);
                    cell0.setCellValue(nightStudyAttendanceSub.getActualNum());

                    cell0 = row0.createCell(5, CellType.STRING);
                    cell0.setCellStyle(style);
                    cell0.setCellValue(nightStudyAttendanceSub.getSummarize());

                    cell0 = row0.createCell(6, CellType.STRING);
                    cell0.setCellStyle(style);
                    cell0.setCellValue(nightStudyAttendanceSub.getSign());

                    startRow0++;
                }

                rangeAddress = new CellRangeAddress(startRow1, startRow1, 0, 2);
                sheet1.addMergedRegion(rangeAddress);

                row1 = sheet1.createRow(startRow1++);
                cell1 = row1.createCell(0, CellType.STRING);
                cell1.setCellStyle(style);
                cell1.setCellValue(head);

                row1 = sheet1.createRow(startRow1++);
                cell1 = row1.createCell(0, CellType.STRING);
                cell1.setCellStyle(style);
                cell1.setCellValue("年级");

                cell1 = row1.createCell(1, CellType.STRING);
                cell1.setCellStyle(style);
                cell1.setCellValue("班级");

                cell1 = row1.createCell(2, CellType.STRING);
                cell1.setCellStyle(style);
                cell1.setCellValue("阶段");

                for (Clazz clazz : clazzList) {
                    Map<StudentNightDutyTypeEnum, NightStudyAttendanceSub> map1 =  value.stream().filter(item -> item.getClazzId().equals(clazz.getId())).collect(Collectors.toMap(NightStudyAttendanceSub::getStage, Function.identity()));
                    for (StudentNightDutyTypeEnum studentNightDutyTypeEnum : StudentNightDutyTypeEnum.values()) {
                        if (!map1.containsKey(studentNightDutyTypeEnum)) {
                            row1 = sheet1.createRow(startRow1);
                            cell1 = row1.createCell(0, CellType.STRING);
                            cell1.setCellStyle(style);
                            cell1.setCellValue(clazz.getGrade().getName());

                            cell1 = row1.createCell(1, CellType.STRING);
                            cell1.setCellStyle(style);
                            cell1.setCellValue(clazz.getName());

                            cell1 = row1.createCell(2, CellType.STRING);
                            cell1.setCellStyle(style);
                            cell1.setCellValue(studentNightDutyTypeEnum.getName());

                            startRow1++;
                        }
                    }
                }

                startRow1++;
                startRow0++;
            }
        }

        return book;
    }

}
