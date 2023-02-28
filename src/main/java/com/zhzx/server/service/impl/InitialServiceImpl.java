package com.zhzx.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhzx.server.domain.*;
import com.zhzx.server.enums.ClazzNatureEnum;
import com.zhzx.server.enums.GenderEnum;
import com.zhzx.server.enums.StudentTypeEnum;
import com.zhzx.server.misc.shiro.ShiroEncrypt;
import com.zhzx.server.repository.*;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.InitialService;
import com.zhzx.server.util.CellUtils;
import com.zhzx.server.util.StringUtils;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InitialServiceImpl implements InitialService {

    @Value("${web.upload-path}")
    private String uploadPath;
    @Resource
    private GradeMapper gradeMapper;
    @Resource
    private StudentMapper studentMapper;
    @Resource
    private ClazzMapper clazzMapper;
    @Resource
    public UserMapper userMapper;
    @Resource
    public RoleMapper roleMapper;
    @Resource
    public UserRoleMapper userRoleMapper;
    @Resource
    private StudentClazzMapper studentClazzMapper;
    @Resource
    private ExamResultMapper examResultMapper;

    @Override
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public void importExcel(Long schoolyardId, String gradeName, Long academicYearSemesterId, String fileUrl) {
        String[] items = fileUrl.split("/");
        File file = new File(uploadPath + "\\" + items[items.length - 2] + "\\" + items[items.length - 1]);
        if (!file.exists())
            throw new ApiCode.ApiException(-1, "文件不存在！");
        Long gradeId = gradeMapper.selectOne(Wrappers.<Grade>lambdaQuery().select(Grade::getId).eq(Grade::getName, gradeName).eq(Grade::getSchoolyardId, schoolyardId)).getId();
        Map<String, Long> clazzMap = clazzMapper.selectList(new QueryWrapper<Clazz>().eq("academic_year_semester_id", academicYearSemesterId).eq("grade_id", gradeId)).stream().collect(Collectors.toMap(Clazz::getName, Clazz::getId));
        Workbook book = null;
        try {
             book = WorkbookFactory.create(file);
            Sheet sheet = book.getSheet("设置");
            int rowNum = sheet.getPhysicalNumberOfRows();
            StringBuilder sb = new StringBuilder();
            String errText = "";
            Long prevId = null;
            String prevName = null;
            for (int rowIndex = 1; rowIndex < rowNum; rowIndex++) {
                errText = "";
                String studentNumber = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(2));
                String studentName = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(3));
                String gender = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(4));
                String clazzName = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(1)).concat("班");
                String subjects = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(5));
                if (clazzName.length() == 1) {
                    errText += "\t班级不能为空";
                }
                if (StringUtils.isNullOrEmpty(studentName)) {
                    continue;
                }
                if (!clazzMap.containsKey(clazzName)) {
                    errText += "\t班级不存在";
                }
                if (!StringUtils.isNullOrEmpty(errText))
                    sb.append(errText = "第" + rowIndex + "行 " + errText + "\r\n");
                if (StringUtils.isNullOrEmpty(errText)) {
                    // 班级
                    if (prevName == null || !prevName.equals(clazzName)) {
                        Clazz clazz = new Clazz();
                        clazz.setId(clazzMap.get(clazzName));
                        if (StringUtils.isNullOrEmpty(subjects)) {
                            clazz.setClazzNature(ClazzNatureEnum.OTHER);
                        } else if (subjects.startsWith("物")) {
                            clazz.setClazzNature(ClazzNatureEnum.SCIENCE);
                        } else {
                            clazz.setClazzNature(ClazzNatureEnum.LIBERAL);
                        }
                        clazzMapper.updateById(clazz);
                        prevId = clazz.getId();
                    }
                    prevName = clazzName;

                    // 学生
                    studentName = studentName.trim();
                    QueryWrapper<Student> studentQueryWrapper2 = new QueryWrapper<Student>().eq("name", studentName);
                    Student checkStudent1 = studentMapper.selectOne(studentQueryWrapper2);
                    Long studentId;
                    if (checkStudent1 == null) {
                        String rn = String.valueOf(RandomUtils.nextInt(1000000, 10000000));
                        Student student = new Student();
                        student.setIdNumber("");
                        student.setCardNumber("");
                        student.setNationality("汉");
                        student.setStudentType(StudentTypeEnum.DAY);
                        student.setAdmissionWay("");
                        student.setOrderNumber(rn);
                        student.setStudentNumber(studentNumber);
                        student.setName(studentName);
                        student.setGender((StringUtils.isNullOrEmpty(gender) ? "" : gender).equals("男") ? GenderEnum.M : GenderEnum.W);
                        studentMapper.insert(student);
                        studentId = student.getId();

                        // 用户
                        User user = new User();
                        user.setStudentId(studentId);
                        user.setUsername(rn);
                        user.setLoginNumber(rn);
                        user.setPassword(ShiroEncrypt.encrypt("winner!"));
                        user.setRealName(studentName);
                        this.userMapper.insert(user);
                        Role role = this.roleMapper.selectOne(new QueryWrapper<Role>().eq("name", "ROLE_STUDENT"));
                        if (role != null) {
                            UserRole userRole = new UserRole();
                            userRole.setUserId(user.getId());
                            userRole.setRoleId(role.getId());
                            this.userRoleMapper.insert(userRole);
                        }
                    } else {
                        studentId = checkStudent1.getId();
                    }

                    // 学生班级对应
                    QueryWrapper<StudentClazz> studentClazzQueryWrapper = new QueryWrapper<>();
                    studentClazzQueryWrapper.eq("clazz_id", prevId);
                    studentClazzQueryWrapper.eq("student_id", studentId);
                    StudentClazz studentClazz = this.studentClazzMapper.selectOne(studentClazzQueryWrapper);
                    if (studentClazz == null) {
                        studentClazz = new StudentClazz();
                        studentClazz.setClazzId(prevId);
                        studentClazz.setStudentId(studentId);
                        this.studentClazzMapper.insert(studentClazz);
                    }
                }
            }
            if (sb.length() != 0) {
                throw new ApiCode.ApiException(-1, "数据有错！\n" + sb.toString());
            }
        } finally {
            if (book != null)
                book.close();
            file.delete();
        }
    }

    @Override
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public void importExcelScore(Long examId, String fileUrl) {
        String[] items = fileUrl.split("/");
        File file = new File(uploadPath + "\\" + items[items.length - 2] + "\\" + items[items.length - 1]);
        if (!file.exists())
            throw new ApiCode.ApiException(-1, "文件不存在！");
        Workbook book = null;
        try {
            List<String> clazzAndStudentList = this.examResultMapper.getClazzAndStudentList(examId);
            book = WorkbookFactory.create(file);
            Sheet sheet = book.getSheet("登分");
            FormulaEvaluator evaluator = book.getCreationHelper().createFormulaEvaluator();
            //获得总行数
            int rowNum = sheet.getPhysicalNumberOfRows();
            // 成绩列表
            List<ExamResult> examResults = new ArrayList<>();
            // 错误行
            StringBuilder sb = new StringBuilder();
            String errText = "";
            // 读取单元格数据
            for (int rowIndex = 1; rowIndex < rowNum; rowIndex++) {
                errText = "";
                if (sheet.getRow(rowIndex) == null) {
                    continue;
                }
                String clazzName = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(1), evaluator).concat("班");
                if (clazzName.length() == 1) {
                    continue;
                }
                String studentName = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(3), evaluator).trim();
                if (StringUtils.isNullOrEmpty(studentName)) {
                    continue;
                }
                String chineseScore = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(4));
                String mathScore = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(6));
                String englishScore = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(8));
                String physicsScore = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(10));
                String chemistryScore = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(12));
                String chemistryWeightedScore = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(13));
                String biologyScore = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(15));
                String biologyWeightedScore = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(16));
                String historyScore = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(18));
                String politicsScore = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(20));
                String politicsWeightedScore = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(21));
                String geographyScore = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(23));
                String geographyWeightedScore = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(24));
                String[] clazzAndStudentInfos = new String[0];
                if (!StringUtils.isNullOrEmpty(clazzName) && !StringUtils.isNullOrEmpty(studentName)) {
                    List<String> clazzAndStudentInfoList = clazzAndStudentList.stream().filter(data -> data.contains(',' + clazzName + ',' + studentName)).collect(Collectors.toList());
                    if (clazzAndStudentInfoList.size() == 0) {
                        errText += "\t班级学生信息不匹配";
                    } else {
                        clazzAndStudentInfos = clazzAndStudentInfoList.get(0).split(",");
                    }
                }
                if (!StringUtils.isNullOrEmpty(errText))
                    sb.append(errText = "第" + rowIndex + "行 " + errText + "\n");
                if (StringUtils.isNullOrEmpty(errText)) {
                    ExamResult examResult = new ExamResult();
                    examResult.setClazzId(Long.parseLong(clazzAndStudentInfos[0]));
                    examResult.setStudentId(Long.parseLong(clazzAndStudentInfos[1]));
                    examResult.setExamId(examId);
                    examResult.setChineseScore(new BigDecimal(StringUtils.isNullOrEmpty(chineseScore) ? "0" : chineseScore));
                    examResult.setMathScore(new BigDecimal(StringUtils.isNullOrEmpty(mathScore) ? "0" : mathScore));
                    examResult.setEnglishScore(new BigDecimal(StringUtils.isNullOrEmpty(englishScore) ? "0" : englishScore));
                    examResult.setPhysicsScore(new BigDecimal(StringUtils.isNullOrEmpty(physicsScore) ? "0" : physicsScore));
                    examResult.setChemistryScore(new BigDecimal(StringUtils.isNullOrEmpty(chemistryScore) ? "0" : chemistryScore));
                    examResult.setChemistryWeightedScore(new BigDecimal(StringUtils.isNullOrEmpty(chemistryWeightedScore) ? "0" : chemistryWeightedScore));
                    examResult.setBiologyScore(new BigDecimal(StringUtils.isNullOrEmpty(biologyScore) ? "0" : biologyScore));
                    examResult.setBiologyWeightedScore(new BigDecimal(StringUtils.isNullOrEmpty(biologyWeightedScore) ? "0" : biologyWeightedScore));
                    examResult.setHistoryScore(new BigDecimal(StringUtils.isNullOrEmpty(historyScore) ? "0" : historyScore));
                    examResult.setPoliticsScore(new BigDecimal(StringUtils.isNullOrEmpty(politicsScore) ? "0" : politicsScore));
                    examResult.setPoliticsWeightedScore(new BigDecimal(StringUtils.isNullOrEmpty(politicsWeightedScore) ? "0" : politicsWeightedScore));
                    examResult.setGeographyScore(new BigDecimal(StringUtils.isNullOrEmpty(geographyScore) ? "0" : geographyScore));
                    examResult.setGeographyWeightedScore(new BigDecimal(StringUtils.isNullOrEmpty(geographyWeightedScore) ? "0" : geographyWeightedScore));

                    BigDecimal partScore = examResult.getChineseScore().add(examResult.getMathScore())
                            .add(examResult.getEnglishScore()).add(examResult.getPhysicsScore())
                            .add(examResult.getHistoryScore());

                    BigDecimal totalScore = partScore.add(examResult.getChemistryScore()).add(examResult.getBiologyScore())
                            .add(examResult.getPoliticsScore()).add(examResult.getGeographyScore());
                    examResult.setTotalScore(totalScore);
                    if (totalScore.compareTo(BigDecimal.ZERO) == 0) {
                        continue;
                    }

                    BigDecimal totalWeightedScore = partScore.add(examResult.getChemistryWeightedScore()).add(examResult.getBiologyWeightedScore())
                            .add(examResult.getPoliticsWeightedScore()).add(examResult.getGeographyWeightedScore());
                    examResult.setTotalWeightedScore(totalWeightedScore);
                    examResult.setOther("");
                    examResult.setDefault();
                    examResults.add(examResult);
                }
            }
            if (sb.length() != 0)
                throw new ApiCode.ApiException(-1, "数据有错！\n" + sb.toString());
            QueryWrapper<ExamResult> queryWrapper = new QueryWrapper<ExamResult>().eq("exam_id", examId);
            queryWrapper.in("clazz_id", clazzAndStudentList.stream().map(data -> Long.valueOf(data.split(",")[0])).collect(Collectors.toList()));
            this.examResultMapper.delete(queryWrapper);
            this.examResultMapper.batchInsert(examResults);
        } finally {
            if (book != null)
                book.close();
            file.delete();
        }
    }

    @Override
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public void importExcelStudent(Long academicYearSemesterId, String fileUrl) {
        String[] items = fileUrl.split("/");
        File file = new File(uploadPath + "\\" + items[items.length - 2] + "\\" + items[items.length - 1]);
        if (!file.exists())
            throw new ApiCode.ApiException(-1, "文件不存在！");
        try {
            // 读取 Excel
            XSSFWorkbook book = new XSSFWorkbook(file);
            for (int i = 0; i < 3; i++) {
                XSSFSheet sheet = book.getSheetAt(i);
                Long gradeId = gradeMapper.selectOne(Wrappers.<Grade>lambdaQuery().select(Grade::getId).eq(Grade::getName, sheet.getSheetName().concat("年级")).eq(Grade::getSchoolyardId, 1L)).getId();
                Map<String, Long> clazzMap = clazzMapper.selectList(new QueryWrapper<Clazz>().eq("academic_year_semester_id", academicYearSemesterId).eq("grade_id", gradeId)).stream().collect(Collectors.toMap(Clazz::getName, Clazz::getId));
                //获得总行数
                int rowNum = sheet.getPhysicalNumberOfRows();
                // 错误行
                StringBuilder sb = new StringBuilder();
                String errText = "";
                // 读取单元格数据
                for (int rowIndex = 1; rowIndex < rowNum; rowIndex++) {
                    String clazzName = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(1)).concat("班");
                    String studentNumber = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(2));
                    String idNumber = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(5));
                    String orderNumber = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(0));
                    String name = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(3)).trim();
                    String gender = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(4));
                    if (StringUtils.isNullOrEmpty(idNumber) && StringUtils.isNullOrEmpty(orderNumber)) {
                        errText += "\t身份证号和编号不能都为空";
                    }
                    if (StringUtils.isNullOrEmpty(name)) {
                        errText += "\t姓名不能为空";
                    }
                    if (!clazzMap.containsKey(clazzName)) {
                        errText += "\t班级不存在";
                    }
                    Long clazzId = clazzMap.get(clazzName);
                    QueryWrapper<Student> studentQueryWrapper1 = new QueryWrapper<Student>().eq("id_number", idNumber);
                    Student checkStudent1 = studentMapper.selectOne(studentQueryWrapper1);
                    if (!StringUtils.isNullOrEmpty(errText))
                        sb.append(errText = "第" + rowIndex + "行 " + errText + "\r\n");
                    if (StringUtils.isNullOrEmpty(errText)) {
                        // 学生
                        Student student = new Student();
                        if (checkStudent1 != null)
                            student = checkStudent1;
                        student.setStudentNumber(StringUtils.isNullOrEmpty(studentNumber) ? "" : studentNumber);
                        student.setIdNumber(StringUtils.isNullOrEmpty(idNumber) ? "" : idNumber);
                        student.setOrderNumber(StringUtils.isNullOrEmpty(orderNumber) ? "" : orderNumber);
                        student.setCardNumber("");
                        student.setStudentNumber(StringUtils.isNullOrEmpty(studentNumber) ? "" : studentNumber);
                        student.setName(name);
                        student.setGender((StringUtils.isNullOrEmpty(gender) ? "" : gender).equals("男") ? GenderEnum.M : GenderEnum.W);
                        student.setNationality("汉");
                        student.setStudentType(StudentTypeEnum.DAY);
                        student.setAdmissionWay("");
                        if (checkStudent1 != null)
                            studentMapper.updateById(student);
                        else
                            studentMapper.insert(student);
                        // 用户
                        User user = new User();
                        QueryWrapper<User> userQueryWrapper2 = new QueryWrapper<User>().eq("login_number", idNumber);
                        User checkUser2 = this.userMapper.selectOne(userQueryWrapper2);
                        if (checkUser2 != null) {
                            user = checkUser2;
                        }
                        user.setStudentId(student.getId());
                        user.setUsername(student.getOrderNumber());
                        user.setLoginNumber(student.getIdNumber());
                        user.setPassword(ShiroEncrypt.encrypt("winner!"));
                        user.setRealName(student.getName());
                        if (checkUser2 != null)
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
                        QueryWrapper<StudentClazz> studentClazzQueryWrapper = new QueryWrapper<>();
                        studentClazzQueryWrapper.eq("clazz_id", clazzId);
                        studentClazzQueryWrapper.eq("student_id", student.getId());
                        StudentClazz studentClazz = this.studentClazzMapper.selectOne(studentClazzQueryWrapper);
                        if (studentClazz == null) {
                            studentClazz = new StudentClazz();
                            studentClazz.setClazzId(clazzId);
                            studentClazz.setStudentId(student.getId());
                            this.studentClazzMapper.insert(studentClazz);
                        }
                    }
                }
                if (sb.length() != 0) {
                    throw new ApiCode.ApiException(-1, "数据有错！\n" + sb.toString());
                }
            }
        } catch (IOException | InvalidFormatException e) {
            throw new ApiCode.ApiException(-1, "导入数据失败！");
        } finally {
            file.delete();
        }
    }

    public static void main(String[] args) {
        readLine("d:/stu.txt");
    }

    @SneakyThrows
    private static void readLine(String fileUrl) {
        String userPre = "INSERT INTO sys_user_role (`user_id`, `role_id`) " +
                "select id, 2 from sys_user where real_name='%s';\n";
        Set<String> set = new HashSet<>();
        StringBuilder sb = new StringBuilder();
        File myFile = new File(fileUrl);
        InputStreamReader Reader = new InputStreamReader(new FileInputStream(myFile), StandardCharsets.UTF_8);

        BufferedReader bufferedReader = new BufferedReader(Reader);
        String lineTxt;

        while ((lineTxt = bufferedReader.readLine()) != null) {
            String[] arr = lineTxt.split("[\\s]");
            for (String s : arr) {
                if (!StringUtils.isNullOrEmpty(s) && !set.contains(s)) {
                    set.add(s);
                    sb.append(String.format(userPre, s));
                }
            }
            set.clear();
        }
        FileWriter fos = new FileWriter("d:/sql.txt");
        fos.write(sb.toString());
        fos.flush();
        fos.close();
        Reader.close();
    }

    @SneakyThrows
    private static void updateSql(String fileUrl) {
        File file = new File(fileUrl);
        if (file.exists()) {
            Workbook book = null;
            StringBuilder sb;
            String userPre = "update sys_user set password = '%s' where real_name = '%s';\n";
            try {
                sb = new StringBuilder();
                book = WorkbookFactory.create(file);
                Sheet sheet = book.getSheetAt(0);
                int rowNum = sheet.getPhysicalNumberOfRows();
                // 读取单元格数据
                for (int rowIndex = 1; rowIndex < rowNum; rowIndex++) {
                    if (sheet.getRow(rowIndex) == null) {
                        continue;
                    }
                    String name = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(3));
                    String idNumber = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(12));
                    String password = ShiroEncrypt.encrypt(StringUtils.getPassword(idNumber));
                    sb.append(String.format(userPre, password, name));
                }
                FileWriter fos = new FileWriter("d:/sql.txt");
                fos.write(sb.toString());
                fos.flush();
                fos.close();
            } finally {
                if (book != null)
                    book.close();
                file.delete();
            }
        }
    }

    @SneakyThrows
    private static void importSql(String fileUrl) {
        File file = new File(fileUrl);
        if (file.exists()) {
            Workbook book = null;
            StringBuilder sb;
            long staffId = 1, userId = 2;
            String staffPre = "insert into sys_staff (id, department, employee_number, name, gender, phone, education, title, card_number,id_number,personnel_situation,`function`,honor)\n" +
                    "values(%d,'%s','%s','%s','%s','%s','','%s','%s','%s','%s','%s','%s');\n";
            String userPre = "insert into sys_user (id, username, login_number, password, real_name, staff_id)\n" +
                    "values(%d,'%s','%s','%s','%s',%d);\n";
            try {
                sb = new StringBuilder();
                book = WorkbookFactory.create(file);
                Sheet sheet = book.getSheetAt(0);
                int rowNum = sheet.getPhysicalNumberOfRows();
                // 读取单元格数据
                for (int rowIndex = 1; rowIndex < rowNum; rowIndex++) {
                    if (sheet.getRow(rowIndex) == null) {
                        continue;
                    }
                    String department = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(1));
                    String employeeNumber = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(2));
                    String name = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(3));
                    String phone = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(11));
                    String gender = "男".equals(CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(4))) ? "M" : "W";
                    String title = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(9));
                    String cardNumber = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(5));
                    String idNumber = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(12));
                    String password = ShiroEncrypt.encrypt(StringUtils.getPassword(idNumber));
                    String honor = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(13));
                    String personnelSituation = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(6));
                    personnelSituation = StringUtils.isNullOrEmpty(personnelSituation) || !"教师".equals(personnelSituation) ? "STAFF" : "TEACHER";
                    sb.append(String.format(staffPre, staffId, department, employeeNumber,name,gender,phone,title,cardNumber,idNumber,personnelSituation,"OTHER", honor));
                    sb.append(String.format(userPre, userId, phone, employeeNumber, password, name, staffId));
                    staffId ++;
                    userId ++;
                }
                FileWriter fos = new FileWriter("d:/sql.txt");
                fos.write(sb.toString());
                fos.flush();
                fos.close();
            } finally {
                if (book != null)
                    book.close();
                file.delete();
            }
        }
    }
}
