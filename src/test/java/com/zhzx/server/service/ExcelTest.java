package com.zhzx.server.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.*;
import com.zhzx.server.enums.StudentTypeEnum;
import com.zhzx.server.msdomain.AccountInfo;
import com.zhzx.server.msrepository.AccountInfoMapper;
import com.zhzx.server.repository.ClazzMapper;
import com.zhzx.server.repository.ClazzTeachingLogSubjectsMapper;
import com.zhzx.server.repository.NightStudyDutyClazzMapper;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.task.PublicCourseTaskComp;
import com.zhzx.server.util.DateUtils;
import com.zhzx.server.util.JsonUtils;
import com.zhzx.server.vo.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@SpringBootTest
public class ExcelTest {
    @Resource
    private CourseService courseService;
    @Resource
    private GradeService gradeService;
    @Resource
    private ClazzMapper clazzMapper;
    @Resource
    private SettingsService settingsService;
    @Resource
    private AcademicYearSemesterService academicYearSemesterService;
    @Resource
    private ClazzTeachingLogSubjectsMapper clazzTeachingLogSubjectsMapper;
    @Resource
    private ClazzTeachingLogService clazzTeachingLogService;
    @Resource
    private LeaderDutyService leaderDutyService;
    @Resource
    private NightStudyDutyClazzMapper nightStudyDutyClazzMapper;

    @Resource
    private ExamService examService;

    @Resource
    private ExamResultService examResultService;

    @Resource
    private StudentService studentService;

    @Test
    public void selectInfoByPage() {
        IPage<StudentInfoVo> page = new Page<>(1, 10);
        StudentParamVo studentParamVo = new StudentParamVo();
        studentParamVo.setClazzName("10班");
        studentParamVo.setName("王");
        IPage<StudentInfoVo> ddd = this.studentService.selectInfoByPage(page, studentParamVo);
        int ccc = 0;
    }

    @Test
    public void getImage() throws Exception {
        courseService.getImage(null, 1L, null);
    }

    @Test
    public void textCourse() {
        AcademicYearSemester academicYearSemester = academicYearSemesterService.getCurrentYearSemester(null);
        if (academicYearSemester == null) return;
        // 获得课程表最后更新日期
        Map<String, Object> courseMap = this.courseService.getMap(new QueryWrapper<Course>()
                .select("max(update_time) as maxDate")
                .eq("academic_year_semester_id", academicYearSemester.getId()));
        String dateTimeStr = DateUtils.format((Date) courseMap.get("maxDate"), "yyyyMMddHHmmss");
        // 获得课程结果表最后日期
        Settings settings = this.settingsService.getOne(Wrappers.<Settings>lambdaQuery()
                .like(Settings::getCode, "COURSE_" + academicYearSemester.getId() + "_")
        );
        // 判断是否需要更新
        Boolean needCalculate = true;
        if (settings != null) {
            String[] codeArray = settings.getCode().split("_");
            if (codeArray.length == 5)
                needCalculate = codeArray[codeArray.length - 1].compareTo(dateTimeStr) < 0;
        }
        if (!needCalculate) return;
        // 清除
        this.settingsService.remove(Wrappers.<Settings>lambdaQuery()
                .like(Settings::getCode, "COURSE_" + academicYearSemester.getId() + "_")
        );
        // 添加
        List<Grade> gradeList = this.gradeService.list();
        for (Grade grade : gradeList) {
            // 年级课表
            String key = "COURSE_" + academicYearSemester.getId() + "_" + grade.getId() + "_null" + "_" + dateTimeStr;
            List<Map<String, String>> courseList = courseService.getList(academicYearSemester.getId(), grade.getId(), null);
            settings = new Settings();
            settings.setCode(key);
            settings.setRemark(key);
            settings.setParams(JsonUtils.toJson(courseList));
            this.settingsService.save(settings);
            // 班级课表
            List<Clazz> clazzList = this.clazzMapper.selectList(new QueryWrapper<Clazz>()
                    .eq("academic_year_semester_id", academicYearSemester.getId())
                    .eq("grade_id", grade.getId())
            );
            for (Clazz clazz : clazzList) {
                key = "COURSE_" + academicYearSemester.getId() + "_" + grade.getId() + "_" + clazz.getId() + "_" + dateTimeStr;
                courseList = courseService.getList(academicYearSemester.getId(), grade.getId(), clazz.getId());
                settings = new Settings();
                settings.setCode(key);
                settings.setRemark(key);
                settings.setParams(JsonUtils.toJson(courseList));
                this.settingsService.save(settings);
            }
        }
    }
//    @Test
//    public void testImportExcel() {
////        gradeService.importExcel("http://localhost:8088/upload/20220125/demo.xlsx");
////        courseService.importExcel(1L, 1L, "http://localhost:8088/upload/20220123/demo.xlsx");
//    }

    @Test
    public void testExportExcel() throws IOException, InvalidFormatException {
        examResultService.exportExcel("01", 1L, examService.getById(1L));
        //      gradeService.exportExcel();
//        courseService.importExcel(1L, 1L, "http://localhost:8088/upload/20220123/demo.xlsx");
    }

    @Test
    public void testWeek() {
        academicYearSemesterService.getWeeks(1L);
    }

    @Test
    public void testListAudit() {
        IPage<ClazzTeachingLogSubjectsVo> page = new Page<>(1, 10);
        page = clazzTeachingLogSubjectsMapper.listAuditPage(page, null, null, null, null, null, null, null, null);
        int ccc = 0;
    }

    @Test
    public void testListWeekLog() {
        IPage<ClazzTeachingLogSubjectsVo> page = new Page<>(1, 10);
        List<ClazzTeachingLog> clazzTeachingLogList = clazzTeachingLogService.listWeekLog(null, null, null, 1L, 1);
        int ccc = 0;
    }

    @Test
    public void testGetRoutineInfo() {
        RoutineInfoVo routineInfoVo = leaderDutyService.getRoutineInfo(7L, "2022-01-13");
        int cc = 0;
    }

    @Test
    public void testGetNightStudyDutyClazzList() {
        // List<NightStudyDutyClazzVo> nightStudyDutyClazzList = nightStudyDutyClazzMapper.getDutyClazzList(2L);
        //   NightStudyInfoVo nightStudyInfoVo = leaderDutyService.getNightStudyInfo(null, "2022-01-13");
        int cc = 0;
    }

    @Resource
    private StaffService staffService;

    @Test
    public void testTask() {
        this.staffService.updateLessonTeacher();
    }

    @Resource
    private PublicCourseTaskComp publicCourseTaskComp;


    @Test
    public void testTaskTomorrow() {
        this.publicCourseTaskComp.afterPublicCourses();
    }

    /**
     * 测试sqlserver
     */
    @Resource
    private AccountInfoMapper accountInfoMapper;

    @Test
    public void testGetPIN() {
        AccountInfo accountInfo = accountInfoMapper.selectOneByPIN("320107196704030331");
        //System.out.println(accountInfo);
    }


}
