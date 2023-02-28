/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：教职工表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.*;
import com.zhzx.server.enums.*;
import com.zhzx.server.misc.shiro.ShiroEncrypt;
import com.zhzx.server.repository.*;
import com.zhzx.server.repository.base.StaffBaseMapper;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.StaffService;
import com.zhzx.server.util.CellUtils;
import com.zhzx.server.util.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StaffServiceImpl extends ServiceImpl<StaffMapper, Staff> implements StaffService {

    @Resource
    public UserMapper userMapper;

    @Resource
    public RoleMapper roleMapper;

    @Resource
    public ClazzMapper clazzMapper;

    @Resource
    public UserRoleMapper userRoleMapper;

    @Value("${web.upload-path}")
    private String uploadPath;

    @Override
    public int updateAllFieldsById(Staff entity) {
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
    public boolean saveBatch(Collection<Staff> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(StaffBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Staff saveStaff(Staff entity) {
        entity.setDefault().validate(true);
        QueryWrapper<Staff> staffWrapper = new QueryWrapper<Staff>()
                .eq("phone", entity.getPhone()).or()
                .eq("employee_number", entity.getEmployeeNumber());
        List<Staff> staffList = this.baseMapper.selectList(staffWrapper);
        if (staffList.size() != 0)
            throw new ApiCode.ApiException(-1, "手机号码或工号已存在");
        QueryWrapper<User> userWrapper = new QueryWrapper<User>()
                .eq("username", entity.getPhone()).or()
                .eq("login_number", entity.getEmployeeNumber());
        List<User> userList = this.userMapper.selectList(userWrapper);
        if (userList.size() != 0)
            throw new ApiCode.ApiException(-1, "手机号码或工号已存在");

        this.save(entity);

        User user = new User();
        user.setStaffId(entity.getId());
        user.setUsername(entity.getPhone());
        user.setLoginNumber(entity.getEmployeeNumber());
        user.setPassword(ShiroEncrypt.encrypt(StringUtils.getPassword(entity.getIdNumber())));
        user.setRealName(entity.getName());
        this.userMapper.insert(user);
        return this.getById(entity.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Staff updateStaff(Staff entity, boolean updateAllFields) {
        QueryWrapper<Staff> staffWrapper = new QueryWrapper<Staff>()
                .ne("id", entity.getId())
                .and(w -> w.eq("phone", entity.getPhone()).or().eq("employee_number", entity.getEmployeeNumber()));
        List<Staff> staffs = this.baseMapper.selectList(staffWrapper);
        if (staffs.size() != 0)
            throw new ApiCode.ApiException(-1, "手机号码或工号已存在");
        QueryWrapper<User> userWrapper = new QueryWrapper<User>()
                .ne("staff_id", entity.getId())
                .and(w -> w.eq("username", entity.getPhone()).or().eq("login_number", entity.getEmployeeNumber()));
        List<User> users = this.userMapper.selectList(userWrapper);
        if (users.size() != 0)
            throw new ApiCode.ApiException(-1, "手机号码或工号已存在");

        if (updateAllFields) {
            this.updateAllFieldsById(entity);
        } else {
            this.updateById(entity);
        }

        userWrapper = new QueryWrapper<User>().eq("staff_id", entity.getId());
        User user = this.userMapper.selectOne(userWrapper);
        if (user != null) {
            user.setUsername(entity.getPhone());
            user.setLoginNumber(entity.getEmployeeNumber());
            user.setRealName(entity.getName());
            this.userMapper.updateById(user);
        } else {
            user = new User();
            user.setUsername(entity.getPhone());
            user.setLoginNumber(entity.getEmployeeNumber());
            user.setRealName(entity.getName());
            user.setStaffId(entity.getId());
            this.userMapper.insert(user);
        }

        return this.getById(entity.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateIsDelete(Long id) {
        Staff staff = this.baseMapper.selectById(id);
        if (staff == null)
            throw new ApiCode.ApiException(-1, "该职工不存在");

        if (staff.getIsDelete() == YesNoEnum.NO)
            staff.setIsDelete(YesNoEnum.YES);
        else
            staff.setIsDelete(YesNoEnum.NO);
        this.baseMapper.updateById(staff);

        QueryWrapper<User> userWrapper = new QueryWrapper<User>().eq("staff_id", id);
        User user = this.userMapper.selectOne(userWrapper);
        if (user != null) {
            user.setIsDelete(staff.getIsDelete());
            this.userMapper.updateById(user);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Long id) {
        this.removeById(id);
        QueryWrapper<User> userWrapper = new QueryWrapper<User>().eq("staff_id", id);
        this.userMapper.delete(userWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importExcel(String fileUrl) {
        if (fileUrl == null || fileUrl == "")
            throw new ApiCode.ApiException(-1, "没有上传文件！");
        String[] items = fileUrl.split("/");
        File file = new File(uploadPath + "\\" + items[items.length - 2] + "\\" + items[items.length - 1]);
        if (!file.exists())
            throw new ApiCode.ApiException(-1, "文件不存在！");
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
                String department = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(0));
                String employeeNumber = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(1));
                String name = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(2));
                String phone = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(3));
                String gender = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(4));
                String education = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(5));
                String title = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(6));
                String cardNumber = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(7));
                String idNumber = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(8));
                String compilationSituation = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(9));
                String personnelSituation = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(10));
                if (StringUtils.isNullOrEmpty(employeeNumber)) {
                    errText += "\t工号不能为空";
                }
                if (StringUtils.isNullOrEmpty(name)) {
                    errText += "\t姓名不能为空";
                }
                if (StringUtils.isNullOrEmpty(phone)) {
                    errText += "\t手机号不能为空";
                }
                QueryWrapper<Staff> staffQueryWrapper1 = new QueryWrapper<Staff>().eq("phone", phone);
                Staff checkStaff1 = this.getOne(staffQueryWrapper1);
                QueryWrapper<Staff> staffQueryWrapper2 = new QueryWrapper<Staff>().eq("employee_number", employeeNumber);
                Staff checkStaff2 = this.getOne(staffQueryWrapper2);
                if (checkStaff1 != null && checkStaff2 != null && checkStaff1.getId().longValue() != checkStaff2.getId().longValue()) {
                    errText += "\t手机号和工号不匹配";
                }
                if (!StringUtils.isNullOrEmpty(errText))
                    sb.append(errText = "第" + rowIndex + "行 " + errText + "\r\n");
                if (StringUtils.isNullOrEmpty(errText)) {
                    // 职工
                    Staff staff = new Staff();
                    if (checkStaff1 != null)
                        staff = checkStaff1;
                    if (checkStaff2 != null)
                        staff = checkStaff2;
                    staff.setDepartment(StringUtils.isNullOrEmpty(department) ? "" : department);
                    staff.setEmployeeNumber(employeeNumber);
                    staff.setName(name);
                    staff.setPhone(phone);
                    staff.setGender((StringUtils.isNullOrEmpty(gender) ? "" : gender).equals("男") ? GenderEnum.M : GenderEnum.W);
                    staff.setEducation(StringUtils.isNullOrEmpty(education) ? "" : education);
                    staff.setTitle(StringUtils.isNullOrEmpty(title) ? "" : title);
                    staff.setCardNumber(StringUtils.isNullOrEmpty(cardNumber) ? "" : cardNumber);
                    staff.setIdNumber(StringUtils.isNullOrEmpty(idNumber) ? "" : idNumber);
                    staff.setCompilationSituation((StringUtils.isNullOrEmpty(compilationSituation) ? "" : compilationSituation).equals(CompilationSituationEnum.ORGANIZATION.getName()) ? CompilationSituationEnum.ORGANIZATION : CompilationSituationEnum.SCHOOL_EMPLOYMENT);
                    staff.setPersonnelSituation((StringUtils.isNullOrEmpty(personnelSituation) ? "" : personnelSituation).equals(PersonnelSituationEnum.STAFF.getName()) ? PersonnelSituationEnum.STAFF : PersonnelSituationEnum.TEACHER);
                    if (checkStaff1 != null || checkStaff2 != null)
                        this.updateById(staff);
                    else
                        this.save(staff);
                    // 用户
                    User user = new User();
                    QueryWrapper<User> userQueryWrapper1 = new QueryWrapper<User>().eq("username", phone);
                    User checkUser1 = this.userMapper.selectOne(userQueryWrapper1);
                    QueryWrapper<User> userQueryWrapper2 = new QueryWrapper<User>().eq("login_number", employeeNumber);
                    User checkUser2 = this.userMapper.selectOne(userQueryWrapper2);
                    if (checkUser1 != null) {
                        user = checkUser1;
                    }
                    if (checkUser2 != null) {
                        user = checkUser2;
                    }
                    user.setStaffId(staff.getId());
                    user.setUsername(staff.getPhone());
                    user.setLoginNumber(staff.getEmployeeNumber());
                    user.setPassword(ShiroEncrypt.encrypt(StringUtils.getPassword(staff.getIdNumber())));
                    user.setRealName(staff.getName());
                    if (checkUser1 != null || checkUser2 != null)
                        this.userMapper.updateById(user);
                    else {
                        this.userMapper.insert(user);
                        Role role = this.roleMapper.selectOne(new QueryWrapper<Role>().eq("name", "ROLE_STAFF"));
                        if (role != null) {
                            UserRole userRole = new UserRole();
                            userRole.setUserId(user.getId());
                            userRole.setRoleId(role.getId());
                            this.userRoleMapper.insert(userRole);
                        }
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
    public List<Staff> getListNoDuty(Date date, TeacherDutyTypeEnum dutyType) {
        return this.baseMapper.getListNoDuty(date, dutyType.toString());
    }

    @Resource
    private AcademicYearSemesterMapper academicYearSemesterMapper;

    @Resource
    private GradeMapper gradeMapper;

    @Resource
    private SubjectMapper subjectMapper;

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private StaffLessonTeacherMapper staffLessonTeacherMapper;

    @Resource
    private StaffSubjectMapper staffSubjectMapper;

    @Resource
    private ClazzTeacherMapper clazzTeacherMapper;

    @Override
    public void updateLessonTeacher() {
        // 获得当前学期
        AcademicYearSemester academicYearSemester = this.academicYearSemesterMapper.selectOne(Wrappers.<AcademicYearSemester>query().eq("is_default", "YES"));
        if (academicYearSemester == null) return;
        // 禁用任课老师
        StaffLessonTeacher staffLessonTeacher = new StaffLessonTeacher();
        staffLessonTeacher.setIsCurrent(YesNoEnum.NO);
        this.staffLessonTeacherMapper.update(staffLessonTeacher,
                Wrappers.<StaffLessonTeacher>query().inSql("clazz_id", "select id from sys_clazz where academic_year_semester_id=" + academicYearSemester.getId()));
        // 删除班级任课老师表
        this.clazzTeacherMapper.delete(Wrappers.<ClazzTeacher>query().inSql("clazz_id", "select id from sys_clazz where academic_year_semester_id=" + academicYearSemester.getId()));
        // 获得科目表
        Map<String, Subject> subjectMap = this.subjectMapper.selectList(new QueryWrapper<>()).stream().collect(Collectors.toMap(Subject::getName, Function.identity()));
        // 获得课程表
        List<Course> courseList = this.courseMapper.selectList(Wrappers.<Course>query().eq("academic_year_semester_id", academicYearSemester.getId()));
        Map<Long, List<String>> divisionMap = new HashMap<>();
        for (Course course : courseList) {
            if (!subjectMap.containsKey(course.getCourseName())) continue;
            if (course.getTeacherId() == null || course.getTeacherId().equals(-1L)) continue;
            Subject subject = subjectMap.get(course.getCourseName());
            staffLessonTeacher = this.staffLessonTeacherMapper.selectOne(Wrappers.<StaffLessonTeacher>query()
                    .eq("clazz_id", course.getClazzId())
                    .eq("staff_id", course.getTeacherId())
                    .eq("subject_id", subject.getId()));
            if (subject.getIsMain().equals(YesNoEnum.YES)) {
                divisionMap.computeIfAbsent(course.getClazzId(), o -> new ArrayList<>()).add(subject.getId().toString());
            }
            if (staffLessonTeacher == null) {
                staffLessonTeacher = new StaffLessonTeacher();
                staffLessonTeacher.setClazzId(course.getClazzId());
                staffLessonTeacher.setStaffId(course.getTeacherId());
                staffLessonTeacher.setSubjectId(subject.getId());
                staffLessonTeacher.setIsCurrent(YesNoEnum.YES);
                this.staffLessonTeacherMapper.insert(staffLessonTeacher);
            } else {
                if (staffLessonTeacher.getIsCurrent().equals(YesNoEnum.YES))
                    continue;
                else {
                    staffLessonTeacher.setIsCurrent(YesNoEnum.YES);
                    this.staffLessonTeacherMapper.updateById(staffLessonTeacher);
                }
            }
            ClazzTeacher clazzTeacher = this.clazzTeacherMapper.selectOne(Wrappers.<ClazzTeacher>query()
                    .eq("clazz_id", course.getClazzId())
                    .eq("name", course.getCourseName()));
            if (clazzTeacher == null) {
                clazzTeacher = new ClazzTeacher();
                clazzTeacher.setClazzId(course.getClazzId());
                clazzTeacher.setTeacher(course.getTeacherName());
                clazzTeacher.setName(course.getCourseName());
                this.clazzTeacherMapper.insert(clazzTeacher);
            } else {
                clazzTeacher.setTeacher(course.getTeacherName());
                this.clazzTeacherMapper.updateById(clazzTeacher);
            }

            List<StaffSubject> staffSubjectList = this.staffSubjectMapper.selectList(Wrappers.<StaffSubject>query()
                    .eq("staff_id", course.getTeacherId())
                    .eq("subject_id", subject.getId()));
            if (CollectionUtils.isEmpty(staffSubjectList)) {
                StaffSubject staffSubject = new StaffSubject();
                staffSubject.setStaffId(course.getTeacherId());
                staffSubject.setSubjectId(subject.getId());
                staffSubject.setIsCurrent(YesNoEnum.YES);
                this.staffSubjectMapper.insert(staffSubject);
            } else {
                if (staffSubjectList.stream().noneMatch(item -> item.getIsCurrent().equals(YesNoEnum.YES))) {
                    StaffSubject staffSubject = staffSubjectList.get(0);
                    staffSubject.setIsCurrent(YesNoEnum.YES);
                    this.staffSubjectMapper.updateById(staffSubject);
                } else {
                    staffSubjectList = staffSubjectList.stream().filter(item -> item.getIsCurrent().equals(YesNoEnum.YES)).collect(Collectors.toList());
                    for (int i = 1; i < staffSubjectList.size(); i++) {
                        StaffSubject curr = staffSubjectList.get(i);
                        curr.setIsCurrent(YesNoEnum.NO);
                        this.staffSubjectMapper.updateById(curr);
                    }
                }
            }
        }
//        divisionMap.forEach((k, v) -> {
//            if (CollectionUtils.isNotEmpty(v)) {
//                this.clazzMapper.update(null, Wrappers.<Clazz>lambdaUpdate().eq(Clazz::getId, k).set(Clazz::getClazzDivision, v.stream().distinct().collect(Collectors.joining(","))));
//            }
//        });
    }
}
