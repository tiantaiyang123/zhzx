/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：学生表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.*;
import com.zhzx.server.enums.GenderEnum;
import com.zhzx.server.enums.StudentTypeEnum;
import com.zhzx.server.misc.shiro.ShiroEncrypt;
import com.zhzx.server.repository.*;
import com.zhzx.server.repository.base.StudentBaseMapper;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.StudentService;
import com.zhzx.server.util.CellUtils;
import com.zhzx.server.util.StringUtils;
import com.zhzx.server.vo.StudentInfoVo;
import com.zhzx.server.vo.StudentParamVo;
import com.zhzx.server.vo.StudentVo;
import lombok.SneakyThrows;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Resource
    private AcademicYearSemesterMapper academicYearSemesterMapper;

    @Resource
    private SchoolyardMapper schoolyardMapper;

    @Resource
    private GradeMapper gradeMapper;

    @Resource
    private ClazzMapper clazzMapper;

    @Resource
    private StudentClazzMapper studentClazzMapper;

    @Resource
    public UserMapper userMapper;

    @Resource
    public RoleMapper roleMapper;

    @Resource
    public UserRoleMapper userRoleMapper;

    @Value("${web.upload-path}")
    private String uploadPath;
    
    @Override
    public int updateAllFieldsById(Student entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
    }

    @Override
    public IPage<Map<String, Object>> queryStudentByPage(IPage<Student> page, QueryWrapper<Student> wrapper) {
        return this.getBaseMapper().queryStudentByPage(page, wrapper);
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
    public boolean saveBatch(Collection<Student> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(StudentBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    /**
     * 新增
     * @param entity
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Student saveStudent(Student entity) {
        entity.setDefault().validate(true);
        QueryWrapper<Student> studentWrapper = new QueryWrapper<Student>()
                .eq("order_number", entity.getOrderNumber()).or()
                .eq("id_number", entity.getIdNumber());
        List<Student> studentList = this.baseMapper.selectList(studentWrapper);
        if (studentList.size() != 0)
            throw new ApiCode.ApiException(-1, "编号和身份证号已存在");
        QueryWrapper<User> userWrapper = new QueryWrapper<User>()
                .eq("username", entity.getOrderNumber())
                .eq("login_number", entity.getIdNumber());
        List<User> userList = this.userMapper.selectList(userWrapper);
        if (userList.size() != 0)
            throw new ApiCode.ApiException(-1, "编号和身份证号已存在");

        this.save(entity);

        User user = new User();
        user.setStudentId(entity.getId());
        user.setUsername(entity.getOrderNumber());
        user.setLoginNumber(entity.getIdNumber());
        user.setPassword(ShiroEncrypt.encrypt(StringUtils.getPassword(entity.getIdNumber())));
        user.setRealName(entity.getName());
        this.userMapper.insert(user);

        return this.getById(entity.getId());
    }

    /**
     * 修改
     * @param entity
     * @param updateAllFields
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Student updateStudent(Student entity, boolean updateAllFields) {
        QueryWrapper<Student> studentWrapper = new QueryWrapper<Student>()
                .ne("id", entity.getId())
                .and(w -> w.eq("order_number", entity.getOrderNumber()).or().eq("id_number", entity.getIdNumber()));
        List<Student> students = this.baseMapper.selectList(studentWrapper);
        if (students.size() != 0)
            throw new ApiCode.ApiException(-1, "编号和身份证号已存在");
        QueryWrapper<User> userWrapper = new QueryWrapper<User>()
                .ne("student_id", entity.getId())
                .and(w -> w.eq("username", entity.getOrderNumber()).or().eq("login_number", entity.getIdNumber()));
        List<User> users = this.userMapper.selectList(userWrapper);
        if (users.size() != 0)
            throw new ApiCode.ApiException(-1, "编号和身份证号已存在");

        if (updateAllFields) {
            this.updateAllFieldsById(entity);
        } else {
            this.updateById(entity);
        }

        userWrapper = new QueryWrapper<User>().eq("student_id", entity.getId());
        User user = this.userMapper.selectOne(userWrapper);
        if (user != null) {
            user.setUsername(entity.getOrderNumber());
            user.setLoginNumber(entity.getIdNumber());
            user.setRealName(entity.getName());
            this.userMapper.updateById(user);
        } else {
            user = new User();
            user.setUsername(entity.getOrderNumber());
            user.setLoginNumber(entity.getIdNumber());
            user.setRealName(entity.getName());
            user.setStudentId(entity.getId());
            this.userMapper.insert(user);
        }

        return this.getById(entity.getId());
    }


    @Override
    public List<Student> listByClazz(Long clazzId) {

        return this.baseMapper.listByClazz(clazzId);
    }

    @Override
    public List<Student> listByClazzStudent(Long clazzId, Long academicYearSemesterId, String studentName) {
        return this.baseMapper.listByClazzStudent(clazzId, academicYearSemesterId, studentName);
    }

    @Override
    @SneakyThrows
    public XSSFWorkbook exportExcel(StudentParamVo param) {
        InputStream is = getClass().getResourceAsStream("/static/templates/学生导入模板.xlsx");
        XSSFWorkbook book = new XSSFWorkbook(is);

        IPage<StudentInfoVo> studentInfoVoIPage = this.selectInfoByPage(new Page<>(1, 9999), param);
        List<StudentInfoVo> studentInfoVoList = studentInfoVoIPage.getRecords();
        if (CollectionUtils.isNotEmpty(studentInfoVoList)) {
            XSSFCellStyle style = book.createCellStyle();
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderBottom(BorderStyle.THIN);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);

            Map<String, List<StudentInfoVo>> map = studentInfoVoList.stream()
                    .filter(t -> !StringUtils.isNullOrEmpty(t.getClazzName()))
                    .collect(Collectors.groupingBy(t -> t.getClazzName().substring(0, 2)));

            for (Map.Entry<String, List<StudentInfoVo>> mesl : map.entrySet()) {
                XSSFSheet sheet = book.getSheet(mesl.getKey() + "年级");
                XSSFRow row;
                XSSFCell cell;

                List<StudentInfoVo> studentInfoVos = mesl.getValue();
                for (int i = 1; i <= studentInfoVos.size(); ++i) {
                    StudentInfoVo studentInfoVo = studentInfoVos.get(i - 1);
                    row = sheet.createRow(i);

                    cell = row.createCell(0, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(studentInfoVo.getClazzName().replace(mesl.getKey(), ""));

                    cell = row.createCell(1, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(studentInfoVo.getStudentNumber());

                    cell = row.createCell(2, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(studentInfoVo.getIdNumber());

                    cell = row.createCell(3, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(studentInfoVo.getOrderNumber());

                    cell = row.createCell(4, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(studentInfoVo.getCardNumber());

                    cell = row.createCell(5, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(studentInfoVo.getName());

                    cell = row.createCell(6, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(studentInfoVo.getGender().getName());

                    cell = row.createCell(7, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(studentInfoVo.getNationality());

                    cell = row.createCell(8, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(studentInfoVo.getStudentType().getName());

                    cell = row.createCell(9, CellType.STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(studentInfoVo.getAdmissionWay());
                }
            }
        }

        return book;
    }

    @Override
    public void updateTimeWhenClazzChange(Long studentId, Long studentClazzId) {
        if (null == studentId) {
            List<StudentClazz> studentClazzList = this.studentClazzMapper.selectList(
                    Wrappers.<StudentClazz>lambdaQuery()
                            .select(StudentClazz::getStudentId)
                            .eq(StudentClazz::getId, studentClazzId)
            );
            studentId = studentClazzList.get(0).getStudentId();
        }
        this.baseMapper.update(
                null,
                Wrappers.<Student>lambdaUpdate().set(Student::getUpdateTime, new Date()).eq(Student::getId, studentId)
        );
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importExcel(Long schoolyardId, Long academicYearSemesterId, Long gradeId, String fileUrl) {
        if (fileUrl == null || fileUrl == "")
            throw new ApiCode.ApiException(-1, "没有上传文件！");
        String[] items = fileUrl.split("/");
        File file = new File(uploadPath + "\\" + items[items.length - 2] + "\\" + items[items.length - 1]);
        if (!file.exists())
            throw new ApiCode.ApiException(-1, "文件不存在！");
        Schoolyard schoolyard = this.schoolyardMapper.selectById(schoolyardId);
        AcademicYearSemester academicYearSemester = this.academicYearSemesterMapper.selectById(academicYearSemesterId);
        Grade grade = this.gradeMapper.selectById(gradeId);
        try {
            // 读取 Excel
            XSSFWorkbook book = new XSSFWorkbook(file);
            XSSFSheet sheet = book.getSheetAt(0);
            file.delete();
            //获得总行数
            int rowNum = sheet.getPhysicalNumberOfRows();
            // 错误行
            StringBuilder sb = new StringBuilder();
            String errText = "";
            // 读取单元格数据
            for (int rowIndex = 1; rowIndex < rowNum; rowIndex++) {
                errText = "";
                String clazzName = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(0));
                String studentNumber = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(1));
                String idNumber = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(2));
                String orderNumber = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(3));
                String cardNumber = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(4));
                String name = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(5));
                String gender = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(6));
                String nationality = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(7));
                String studentType = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(8));
                String admissionWay = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(9));
                if (StringUtils.isNullOrEmpty(idNumber) && StringUtils.isNullOrEmpty(orderNumber)) {
                    errText += "\t身份证号和编号不能都为空";
                }
                if (StringUtils.isNullOrEmpty(name)) {
                    errText += "\t姓名不能为空";
                }
                QueryWrapper<Student> studentQueryWrapper1 = new QueryWrapper<Student>().eq("id_number", idNumber);
                Student checkStudent1 = this.getOne(studentQueryWrapper1);
                QueryWrapper<Student> studentQueryWrapper2 = new QueryWrapper<Student>().eq("order_number", orderNumber);
                Student checkStudent2 = this.getOne(studentQueryWrapper2);
                if (checkStudent1 != null && checkStudent2 != null && checkStudent1.getId().longValue() != checkStudent2.getId().longValue()) {
                    errText += "\t身份证号和编号不匹配";
                }
                if (!StringUtils.isNullOrEmpty(errText))
                    sb.append(errText = "第" + rowIndex + "行 " + errText + "\r\n");
                if (StringUtils.isNullOrEmpty(errText)) {
                    // 学生
                    Student student = new Student();
                    if (checkStudent1 != null)
                        student = checkStudent1;
                    if (checkStudent2 != null)
                        student = checkStudent2;

                    if (StringUtils.isNullOrEmpty(orderNumber)) {
                        orderNumber = idNumber;
                    }
                    if (StringUtils.isNullOrEmpty(idNumber)) {
                        idNumber = orderNumber;
                    }
                    student.setStudentNumber(StringUtils.isNullOrEmpty(studentNumber) ? "" : studentNumber);
                    student.setIdNumber(StringUtils.isNullOrEmpty(idNumber) ? "" : idNumber);
                    student.setOrderNumber(StringUtils.isNullOrEmpty(orderNumber) ? "" : orderNumber);
                    student.setStudentNumber(StringUtils.isNullOrEmpty(studentNumber) ? "0" : studentNumber);
                    student.setName(name);
                    student.setGender((StringUtils.isNullOrEmpty(gender) ? "" : gender).equals("男") ? GenderEnum.M : GenderEnum.W);
                    student.setNationality(StringUtils.isNullOrEmpty(nationality) ? "汉" : nationality);
                    student.setAdmissionWay(StringUtils.isNullOrEmpty(admissionWay) ? "" : admissionWay);
                    // use pre
                    student.setCardNumber(StringUtils.isNullOrEmpty(cardNumber) ? (StringUtils.isNullOrEmpty(student.getCardNumber()) ? "" : student.getCardNumber()) : cardNumber);
                    student.setStudentType((StringUtils.isNullOrEmpty(studentType) ? (student.getStudentType() == null ? "" : student.getStudentType().getName()) : studentType).equals(StudentTypeEnum.DAY.getName()) ? StudentTypeEnum.DAY : StudentTypeEnum.LIVE);
                    if (checkStudent1 != null || checkStudent2 != null)
                        this.updateById(student);
                    else
                        this.save(student);
                    // 用户
                    User user = new User();
                    QueryWrapper<User> userQueryWrapper1 = new QueryWrapper<User>().eq("username", orderNumber);
                    User checkUser1 = this.userMapper.selectOne(userQueryWrapper1);
                    QueryWrapper<User> userQueryWrapper2 = new QueryWrapper<User>().eq("login_number", idNumber);
                    User checkUser2 = this.userMapper.selectOne(userQueryWrapper2);
                    if (checkUser1 != null) {
                        user = checkUser1;
                    }
                    if (checkUser2 != null) {
                        user = checkUser2;
                    }
                    user.setStudentId(student.getId());
                    user.setUsername(student.getOrderNumber());
                    user.setLoginNumber(student.getIdNumber());
                    user.setPassword(ShiroEncrypt.encrypt(StringUtils.getPassword(student.getIdNumber())));
                    user.setRealName(student.getName());
                    if (checkUser1 != null || checkUser2 != null)
                        this.userMapper.updateById(user);
                    else {
                        this.userMapper.insert(user);
                        Role role = this.roleMapper.selectOne(new QueryWrapper<Role>().eq("name", "ROLE_STUDENT"));
                        if (role != null) {
                            UserRole userRole = new UserRole();
                            userRole.setUserId(user.getId());
                            userRole.setRoleId(role.getId());
                            this.userRoleMapper.insert(userRole);
                        }
                    }
                    // 设置班级
                    QueryWrapper<Clazz> clazzQueryWrapper = new QueryWrapper<Clazz>();
                    clazzQueryWrapper.eq("name", clazzName);
                    clazzQueryWrapper.eq("academic_year_semester_id", academicYearSemesterId);
                    clazzQueryWrapper.eq("grade_id", grade.getId());
                    Clazz clazz = this.clazzMapper.selectOne(clazzQueryWrapper);
                    if (clazz == null) continue;
                    QueryWrapper<StudentClazz> studentClazzQueryWrapper = new QueryWrapper<StudentClazz>();
                    studentClazzQueryWrapper.eq("clazz_id", clazz.getId());
                    studentClazzQueryWrapper.eq("student_id", student.getId());
                    StudentClazz studentClazz = this.studentClazzMapper.selectOne(studentClazzQueryWrapper);
                    if (studentClazz == null) {
                        studentClazz = new StudentClazz();
                        studentClazz.setClazzId(clazz.getId());
                        studentClazz.setStudentId(student.getId());
                        this.studentClazzMapper.insert(studentClazz);
                    }
                }
            }
            if (sb.length() != 0) {
                throw new ApiCode.ApiException(-1, "数据有错！\n" + sb.toString());
            }
        } catch (IOException | InvalidFormatException e) {
            throw new ApiCode.ApiException(-1, "导入数据失败！");
        }
    }

    @Override
    public List<StudentVo> selectListWithClazz(StudentTypeEnum studentTypeEnum,Long senderId) {
        String typeEnum = studentTypeEnum == null ? null : studentTypeEnum.toString();
        return this.baseMapper.selectListWithClazz(typeEnum,senderId);
    }

    @Override
    public Integer studentCount(Long gradeId,Long clazzId,StudentTypeEnum studentTypeEnum) {
        String typeEnum = studentTypeEnum == null ? null : studentTypeEnum.toString();
        return this.baseMapper.count(typeEnum,gradeId,clazzId);
    }

    @Override
    public IPage<StudentInfoVo> selectInfoByPage(IPage<StudentInfoVo> page, StudentParamVo param) {
        return this.baseMapper.selectInfoByPage(page, param);
    }
}
