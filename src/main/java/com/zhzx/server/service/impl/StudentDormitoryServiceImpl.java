/**
* 项目：中华中学管理平台
* 模型分组：系统管理
* 模型名称：宿舍学生表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhzx.server.domain.*;
import com.zhzx.server.enums.StudentTypeEnum;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.repository.*;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.StudentDormitoryService;
import com.zhzx.server.util.CellUtils;
import com.zhzx.server.util.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentDormitoryServiceImpl extends ServiceImpl<StudentDormitoryMapper, StudentDormitory> implements StudentDormitoryService {

    @Value("${web.upload-path}")
    private String uploadPath;
    @Resource
    private StudentMapper studentMapper;
    @Resource
    private DormitoryMapper dormitoryMapper;
    @Resource
    private GradeMapper gradeMapper;
    @Resource
    private ClazzMapper clazzMapper;

    @Override
    public int updateAllFieldsById(StudentDormitory entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudentDormitory saveEntity(StudentDormitory entity) {
        entity.setDefault().validate(true);
        StudentDormitory studentDormitory = new StudentDormitory();
        studentDormitory.setIsDefault(YesNoEnum.NO);
        this.getBaseMapper().update(studentDormitory, (new QueryWrapper<StudentDormitory>()).eq("student_id",entity.getStudentId()));
        entity.setIsDefault(YesNoEnum.YES);
        this.getBaseMapper().insert(entity);
        return this.getBaseMapper().selectById(entity.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importStudentDormitory(String fileUrl) {
        String[] items = fileUrl.split("/");
        File file = new File(uploadPath + "\\" + items[items.length - 2] + "\\" + items[items.length - 1]);
        if (!file.exists())
            throw new ApiCode.ApiException(-1, "文件不存在！");
        try {
            // 读取 Excel
            XSSFWorkbook book = new XSSFWorkbook(file);
            XSSFSheet sheet = book.getSheetAt(0);
            file.delete();

            List<Dormitory> dormitoryList = this.dormitoryMapper.selectList(null);
            if (CollectionUtils.isEmpty(dormitoryList)) {
                throw new ApiCode.ApiException(-1, "宿舍不存在！");
            }
            User user = (User) SecurityUtils.getSubject().getPrincipal();
            Long academicYearSemesterId = user.getAcademicYearSemester().getId();
            Map<String, Long> dormitoryMap = dormitoryList.stream().collect(Collectors
                    .toMap(o -> o.getBuilding().concat(o.getFloor()).concat(o.getName()), Dormitory::getId));
            Map<String, Long> gradeMap = this.gradeMapper.selectList(Wrappers.<Grade>lambdaQuery().eq(Grade::getSchoolyardId, 1L)).stream().collect(Collectors.toMap(Grade::getName, Grade::getId));
            Map<String, Long> clazzMap = this.clazzMapper.selectList(Wrappers.<Clazz>lambdaQuery().eq(Clazz::getAcademicYearSemesterId, academicYearSemesterId))
                    .stream().collect(Collectors.toMap(item -> item.getName().concat(item.getGradeId().toString()), Clazz::getId));

            this.studentMapper.updateDormitory(null, null, academicYearSemesterId);

            //获得总行数
            int rowNum = sheet.getPhysicalNumberOfRows();
            // 错误行
            StringBuilder sb = new StringBuilder();
            String errText;
            // 读取单元格数据
            for (int rowIndex = 1; rowIndex < rowNum; rowIndex++) {
                errText = "";
                String gradeName = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(0));
                String clazzName = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(1));
                String studentName = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(2));
                if (StringUtils.isNullOrEmpty(clazzName) || StringUtils.isNullOrEmpty(studentName)) {
                    continue;
                }
                String orderNumber = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(3));
                String building = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(4));
                String floor = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(5));
                String name = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(6));
                String bed = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(7));
                if (StringUtils.isNullOrEmpty(gradeName)) {
                    errText += "\t年级为空";
                }
                if (StringUtils.isNullOrEmpty(building) || StringUtils.isNullOrEmpty(floor) || StringUtils.isNullOrEmpty(name) || StringUtils.isNullOrEmpty(bed)) {
                    errText += "\t宿舍信息不能为空";
                }
                if (!gradeMap.containsKey(gradeName)) {
                    errText += "\t年级名称不存在";
                }
                if (!clazzName.endsWith("班")) {
                    clazzName = clazzName + "班";
                }
                String clazzKey = clazzName.concat(gradeMap.get(gradeName).toString());
                if (!clazzMap.containsKey(clazzKey)) {
                    errText += "\t班级名称不存在";
                }

                String key = building.concat(floor).concat(name);
                if (!dormitoryMap.containsKey(key)) {
                    errText += "\t宿舍信息不存在";
                }
                Student student;
                if (!StringUtils.isNullOrEmpty(orderNumber)) {
                    QueryWrapper<Student> studentQueryWrapper1 = new QueryWrapper<Student>().eq("order_number", orderNumber);
                    student = studentMapper.selectOne(studentQueryWrapper1);
                } else {
                    student = studentMapper.selectOne(Wrappers.<Student>lambdaQuery().eq(Student::getName, studentName)
                            .inSql(Student::getId, "select student_id from sys_student_clazz where clazz_id=" + clazzMap.get(clazzKey)));
                }
                if(student == null || !student.getName().equals(studentName)){
                    errText += "\t编号不正确或姓名不匹配，未查询到学生";
                }
                if (!StringUtils.isNullOrEmpty(errText))
                    sb.append(errText = "第" + rowIndex + "行 " + errText + "\r\n");
                if (StringUtils.isNullOrEmpty(errText)) {
                    QueryWrapper<StudentDormitory> studentQueryWrapper2 = new QueryWrapper<StudentDormitory>()
                            .eq("student_id", student.getId())
                            .eq("dormitory_id", dormitoryMap.get(key))
                            .eq("bed", bed);
                    StudentDormitory studentDormitory = this.baseMapper.selectOne(studentQueryWrapper2);
                    if (studentDormitory == null) {
                        studentDormitory = new StudentDormitory();
                    }
                    studentDormitory.setDormitoryId(dormitoryMap.get(key));
                    studentDormitory.setBed(bed);
                    studentDormitory.setIsDefault(YesNoEnum.YES);
                    // 学生
                    if(studentDormitory.getId() != null){
                        this.baseMapper.updateById(studentDormitory);
                    }else{
                        studentDormitory.setStudentId(student.getId());
                        this.baseMapper.insert(studentDormitory);
                    }

                    this.studentMapper.update(null, Wrappers.<Student>lambdaUpdate().set(Student::getStudentType, StudentTypeEnum.LIVE).eq(Student::getId, student.getId()));
                }
            }
            if (sb.length() != 0) {
                throw new ApiCode.ApiException(-1, "数据有错！\n" + sb);
            }
        } catch (IOException | InvalidFormatException e) {
            throw new ApiCode.ApiException(-1, "导入数据失败！");
        }
    }
}
