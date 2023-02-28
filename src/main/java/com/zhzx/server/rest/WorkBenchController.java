package com.zhzx.server.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhzx.server.domain.*;
import com.zhzx.server.dto.ClazzTeacherDto;
import com.zhzx.server.dto.LeaderNightStudyDutyDto;
import com.zhzx.server.dto.exam.ExamClazzAnalyseClazzAvgDto;
import com.zhzx.server.enums.*;
import com.zhzx.server.repository.ExamResultMapper;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.*;
import com.zhzx.server.util.DateUtils;
import com.zhzx.server.util.TwxUtils;
import com.zhzx.server.vo.ClazzVo;
import com.zhzx.server.vo.GradeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by 11345 on 2022/3/22.
 */
@Slf4j
@RestController
@Api(tags = "WorkBenchController", description = "工作台")
@RequestMapping("/v1/system/workbench")
public class WorkBenchController {
    @Resource
    private StaffClazzAdviserService staffClazzAdviserService;
    @Resource
    private StaffGradeLeaderService gradeLeaderService;
    @Resource
    private StaffLessonTeacherService staffLessonTeacherService;
    @Resource
    private ClazzService clazzService;
    @Resource
    private AcademicYearSemesterService academicYearSemesterService;
    @Resource
    private NightStudyDutyService nightStudyDutyService;
    @Resource
    private CourseService courseService;
    @Resource
    private LeaderDutyService leaderDutyService;
    @Resource
    private TeacherDutyService teacherDutyService;
    @Resource
    private StudentService studentService;
    @Resource
    private DailyAttendanceService dailyAttendanceService;
    @Resource
    private NightStudyAttendanceService nightStudyAttendanceService;
    @Resource
    private StaffService staffService;
    @Resource
    private LabelService labelService;
    @Resource
    private TeachingResultService teachingResultService;
    @Resource
    private SubjectService subjectService;
    @Resource
    private ExamResultMapper examResultMapper;
    @Resource
    private StaffSubjectService staffSubjectService;
    @Resource
    private UserService userService;

    /**
     *
     */
    @GetMapping("/real-time/data/clazzLeader")
    @ApiOperation("班主任实时数据看板")
    public ApiResponse<Object> realTimeDateClazzLeader(@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam(value = "time") Date time) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(user.getStaffId() == null)
            throw new ApiCode.ApiException(-5,"该用户非职工，无法查看数据");
        StaffClazzAdviser staffClazzAdviser = staffClazzAdviserService.getOne(Wrappers.<StaffClazzAdviser>lambdaQuery()
                .eq(StaffClazzAdviser::getStaffId,user.getStaffId())
                .eq(StaffClazzAdviser::getIsCurrent, YesNoEnum.YES)
        );

        if(staffClazzAdviser == null){
            throw new ApiCode.ApiException(-5,"该用户不是班主任");
        }

        AcademicYearSemester academicYearSemester = academicYearSemesterService.getOne(Wrappers.<AcademicYearSemester>lambdaQuery().eq(AcademicYearSemester::getIsDefault,YesNoEnum.YES));
        //获取班主任看板数据
        List<ClazzVo> clazzVos = clazzService.getClazzStatistics(new ArrayList<Long>(){{this.add(staffClazzAdviser.getClazzId());}},null,
                academicYearSemester.getId(),null,null, DateUtils.format("00:00:00",time),DateUtils.format("23:59:59",time));
        ClazzVo clazzVo = clazzVos.get(0);
        if(clazzVo.getDailyAttendanceMap() != null){
            Map<String,List<DailyAttendance>> map = clazzVos.get(0).getDailyAttendanceMap();
            Integer count = 0;
            if(map.containsKey("病假")){
                count += map.get("病假").size();
                clazzVo.setIllNum(map.get("病假").size());
            }
            if(map.containsKey("事假")){
                count += map.get("事假").size();
                clazzVo.setThingsNum(map.get("事假").size());
            }
            count = map.values().stream().mapToInt(item->item.size()).sum() - count;
            clazzVo.setDailyAbsenceNum(count);
        }
        if(clazzVo.getNightStudyAttendanceMap() != null){
            Map<String,List<NightStudyAttendance>> map = clazzVo.getNightStudyAttendanceMap();
            if(map.containsKey("缺席")){
                long stageOne = map.get("缺席").stream().filter(item-> StudentNightDutyTypeEnum.STAGE_ONE.equals(item.getStage())).count();
                long stageTwo = map.get("缺席").stream().filter(item-> StudentNightDutyTypeEnum.STAGE_TWO.equals(item.getStage())).count();
                clazzVo.setNightAbsenceOneNum((int)stageOne);
                clazzVo.setNightAbsenceTwoNum((int)stageTwo);
            }
        }
        List<LeaderNightStudyDutyDto> nightStudyDutyDtos = nightStudyDutyService.getDetail(time,clazzVo.getId());
        clazzVo.setLeaderNightStudyDutyDtos(nightStudyDutyDtos);
        return ApiResponse.ok(clazzVo);

    }

    @GetMapping("/real-time/data/gradeLeader")
    @ApiOperation("年级组实时数据看板")
    public ApiResponse<Object> realTimeDateGradeLeader(@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam(value = "time") Date time,
                                                       @RequestParam(required = false) Long gradeId) {

        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(user.getStaffId() == null)
            throw new ApiCode.ApiException(-5,"该用户非职工，无法查看数据");
        AcademicYearSemester academicYearSemester = academicYearSemesterService.getOne(Wrappers.<AcademicYearSemester>lambdaQuery().eq(AcademicYearSemester::getIsDefault,YesNoEnum.YES));
        if(gradeId == null){
            StaffGradeLeader staffGradeLeader = gradeLeaderService.getOne(Wrappers.<StaffGradeLeader>lambdaQuery()
                    .eq(StaffGradeLeader::getStaffId,user.getStaffId())
                    .eq(StaffGradeLeader::getAcademicYearSemesterId,academicYearSemester.getId())
                    .eq(StaffGradeLeader::getIsCurrent, YesNoEnum.YES)
            );
            if(staffGradeLeader == null)
                throw new ApiCode.ApiException(-5,"用户不是年级组人员");
            gradeId = staffGradeLeader.getGradeId();
        }

        //获取年纪组长看板数据
        List<Long> gradeIds = new ArrayList<>();
        gradeIds.add(gradeId);
        List<GradeVo> gradeVos = clazzService.getGradeStatistics(gradeIds,academicYearSemester.getId(),null,null,DateUtils.format("00:00:00",time),DateUtils.format("23:59:59",time));

        GradeVo gradeVo = gradeVos.get(0);
        if(gradeVo.getDailyAttendanceMap() != null){
            Map<String,List<DailyAttendance>> map = gradeVo.getDailyAttendanceMap();
            Integer count = 0;
            if(map.containsKey("病假")){
                count += map.get("病假").size();
                gradeVo.setIllNum(map.get("病假").size());
            }
            if(map.containsKey("事假")){
                count += map.get("事假").size();
                gradeVo.setThingsNum(map.get("事假").size());
            }
            count = map.values().stream().mapToInt(item->item.size()).sum() - count;
            gradeVo.setDailyAbsenceNum(count);
        }
        if(gradeVo.getNightStudyAttendance() != null){
            Map<String,List<NightStudyAttendance>> map = gradeVo.getNightStudyAttendance();
            if(map.containsKey("缺席")){
                long stageOne = map.get("缺席").stream().filter(item-> StudentNightDutyTypeEnum.STAGE_ONE.equals(item.getStage())).count();
                long stageTwo = map.get("缺席").stream().filter(item-> StudentNightDutyTypeEnum.STAGE_TWO.equals(item.getStage())).count();
                gradeVo.setNightAbsenceOneNum((int)stageOne);
                gradeVo.setNightAbsenceTwoNum((int)stageTwo);
            }
        }
        return ApiResponse.ok(gradeVo);
    }

    @GetMapping("/real-time/data/gradeLeader/clazzInfo")
    @ApiOperation("年级组班级数据")
    public ApiResponse<Object> realTimeDateGradeLeaderClazzInfo(@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam(value = "time") Date time,
                                                                @RequestParam(required = false) Long gradeId) {

        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(user.getStaffId() == null)
            throw new ApiCode.ApiException(-5,"该用户非职工，无法查看数据");
        AcademicYearSemester academicYearSemester = academicYearSemesterService.getOne(Wrappers.<AcademicYearSemester>lambdaQuery().eq(AcademicYearSemester::getIsDefault,YesNoEnum.YES));
        if(gradeId == null){
            StaffGradeLeader staffGradeLeader = gradeLeaderService.getOne(Wrappers.<StaffGradeLeader>lambdaQuery()
                    .eq(StaffGradeLeader::getStaffId,user.getStaffId())
                    .eq(StaffGradeLeader::getAcademicYearSemesterId,academicYearSemester.getId())
                    .eq(StaffGradeLeader::getIsCurrent, YesNoEnum.YES)
            );
            if(staffGradeLeader == null)
                throw new ApiCode.ApiException(-5,"用户不是年级组人员");
            gradeId = staffGradeLeader.getGradeId();
        }

        //获取年纪组长各班级数据
        List<Long> gradeIds = new ArrayList<>();
        gradeIds.add(gradeId);
        List<ClazzVo> clazzVos = clazzService.getClazzStatistics(null,gradeIds,
                academicYearSemester.getId(),null,null, DateUtils.format("00:00:00",time),DateUtils.format("23:59:59",time));

        for(ClazzVo clazzVo : clazzVos){
            if(clazzVo.getDailyAttendanceMap() != null){
                Map<String,List<DailyAttendance>> map = clazzVos.get(0).getDailyAttendanceMap();
                Integer count = 0;
                if(map.containsKey("病假")){
                    count += map.get("病假").size();
                    clazzVo.setIllNum(map.get("病假").size());
                }
                if(map.containsKey("事假")){
                    count += map.get("事假").size();
                    clazzVo.setThingsNum(map.get("事假").size());
                }
                count = map.values().stream().mapToInt(item->item.size()).sum() - count;
                clazzVo.setDailyAbsenceNum(count);
            }
            if(clazzVo.getNightStudyAttendanceMap() != null){
                Map<String,List<NightStudyAttendance>> map = clazzVo.getNightStudyAttendanceMap();
                if(map.containsKey("缺席")){
                    long stageOne = map.get("缺席").stream().filter(item-> StudentNightDutyTypeEnum.STAGE_ONE.equals(item.getStage())).count();
                    long stageTwo = map.get("缺席").stream().filter(item-> StudentNightDutyTypeEnum.STAGE_TWO.equals(item.getStage())).count();
                    clazzVo.setNightAbsenceOneNum((int)stageOne);
                    clazzVo.setNightAbsenceTwoNum((int)stageTwo);
                }
            }
        }
        return ApiResponse.ok(clazzVos);
    }


    @GetMapping("/real-time/data/clazzTeacher/clazzInfo")
    @ApiOperation("任课教师班级数据")
    public ApiResponse<Object> realTimeDateClazzTeacherClazzInfo(@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam(value = "time") Date time) {

        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(user.getStaffId() == null)
            throw new ApiCode.ApiException(-5,"该用户非职工，无法查看数据");
        AcademicYearSemester academicYearSemester = academicYearSemesterService.getOne(Wrappers.<AcademicYearSemester>lambdaQuery().eq(AcademicYearSemester::getIsDefault,YesNoEnum.YES));

        List<StaffLessonTeacher> staffLessonTeacherList = staffLessonTeacherService.list(Wrappers.<StaffLessonTeacher>lambdaQuery()
                .eq(StaffLessonTeacher::getStaffId,user.getStaffId())
                .eq(StaffLessonTeacher::getIsCurrent, YesNoEnum.YES)
        );

        if(CollectionUtils.isEmpty(staffLessonTeacherList))
            throw new ApiCode.ApiException(-5,"用户不是任课教师");

        List<Long> clazzIds = staffLessonTeacherList.stream().map(item->item.getClazzId()).collect(Collectors.toList());
        //任课教师班级数据
        List<ClazzVo> clazzVos = clazzService.getClazzStatistics(clazzIds,null,
                academicYearSemester.getId(),null,null, DateUtils.format("00:00:00",time),DateUtils.format("23:59:59",time));
        Map<String,Object> map = new HashMap<>();
        List<Label> labels = labelService.list(Wrappers.<Label>lambdaQuery().eq(Label::getClassify,"RCKQ"));
        map.put("label",labels);
        map.put("clazzVos",clazzVos);
        return ApiResponse.ok(map);
    }


    @GetMapping("/real-time/data/jiaowu")
    @ApiOperation("教务处数据看板")
    public ApiResponse<Object> realTimeDateJiaowu(@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam(value = "time") Date time) {

        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(user.getStaffId() == null || user.getStaffId() == 0)
            throw new ApiCode.ApiException(-5,"该用户非职工，无法查看数据");
        AcademicYearSemester academicYearSemester = academicYearSemesterService.getOne(Wrappers.<AcademicYearSemester>lambdaQuery().eq(AcademicYearSemester::getIsDefault,YesNoEnum.YES));
        //任课教师班级数据
        //获取年纪组长看板数据
        List<GradeVo> gradeVos = clazzService.getGradeStatistics(new ArrayList<Long>(){{this.add(1L);this.add(2L);this.add(3L);}},
                academicYearSemester.getId(),null,null,DateUtils.format("00:00:00",time),DateUtils.format("23:59:59",time));

        for (GradeVo gradeVo : gradeVos){
            if(gradeVo.getDailyAttendanceMap() != null){
                Map<String,List<DailyAttendance>> map = gradeVo.getDailyAttendanceMap();
                Integer count = 0;
                if(map.containsKey("病假")){
                    count += map.get("病假").size();
                    gradeVo.setIllNum(map.get("病假").size());
                }
                if(map.containsKey("事假")){
                    count += map.get("事假").size();
                    gradeVo.setThingsNum(map.get("事假").size());
                }
                count = map.values().stream().mapToInt(item->item.size()).sum() - count;
                gradeVo.setDailyAbsenceNum(count);
            }
            if(gradeVo.getNightStudyAttendance() != null){
                Map<String,List<NightStudyAttendance>> map = gradeVo.getNightStudyAttendance();
                if(map.containsKey("缺席")){
                    long stageOne = map.get("缺席").stream().filter(item-> StudentNightDutyTypeEnum.STAGE_ONE.equals(item.getStage())).count();
                    long stageTwo = map.get("缺席").stream().filter(item-> StudentNightDutyTypeEnum.STAGE_TWO.equals(item.getStage())).count();
                    gradeVo.setNightAbsenceOneNum((int)stageOne);
                    gradeVo.setNightAbsenceTwoNum((int)stageTwo);
                }
            }
        }
        return ApiResponse.ok(gradeVos);
    }

    @GetMapping("/real-time/data/jiaowu/clazzInfo")
    @ApiOperation("教务处班级数据")
    public ApiResponse<Object> realTimeDateJiaowu(@RequestParam Long gradeId,
                                                  @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam(value = "time") Date time) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(user.getStaffId() == null || user.getStaffId() == 0)
            throw new ApiCode.ApiException(-5,"该用户非职工，无法查看数据");
        AcademicYearSemester academicYearSemester = academicYearSemesterService.getOne(Wrappers.<AcademicYearSemester>lambdaQuery().eq(AcademicYearSemester::getIsDefault,YesNoEnum.YES));
        //获取年纪组长各班级数据
        List<ClazzVo> clazzVos = clazzService.getClazzStatistics(null,new ArrayList<Long>(){{this.add(gradeId);}},
                academicYearSemester.getId(),null,null, DateUtils.format("00:00:00",time),DateUtils.format("23:59:59",time));

        for(ClazzVo clazzVo : clazzVos){
            if(clazzVo.getDailyAttendanceMap() != null){
//                Map<String,List<DailyAttendance>> map = clazzVos.get(0).getDailyAttendanceMap();
                Map<String,List<DailyAttendance>> map = clazzVo.getDailyAttendanceMap();
                Integer count = 0;
                if(map.containsKey("病假")){
                    count += map.get("病假").size();
                    clazzVo.setIllNum(map.get("病假").size());
                }
                if(map.containsKey("事假")){
                    count += map.get("事假").size();
                    clazzVo.setThingsNum(map.get("事假").size());
                }
                count = map.values().stream().mapToInt(item->item.size()).sum() - count;
                clazzVo.setDailyAbsenceNum(count);
            }
            if(clazzVo.getNightStudyAttendanceMap() != null){
                Map<String,List<NightStudyAttendance>> map = clazzVo.getNightStudyAttendanceMap();
                if(map.containsKey("缺席")){
                    long stageOne = map.get("缺席").stream().filter(item-> StudentNightDutyTypeEnum.STAGE_ONE.equals(item.getStage())).count();
                    long stageTwo = map.get("缺席").stream().filter(item-> StudentNightDutyTypeEnum.STAGE_TWO.equals(item.getStage())).count();
                    clazzVo.setNightAbsenceOneNum((int)stageOne);
                    clazzVo.setNightAbsenceTwoNum((int)stageTwo);
                }
            }
        }
        return ApiResponse.ok(clazzVos);
    }

    @GetMapping("/user/Info")
    @ApiOperation("工作台用户基本信息")
    public ApiResponse<Object> realTimeDateUserInfo() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(user.getStaffId() == null || user.getStaffId() == 0)
            throw new ApiCode.ApiException(-5,"该用户非职工，无法查看数据");
        AcademicYearSemester academicYearSemester = academicYearSemesterService.getOne(Wrappers.<AcademicYearSemester>lambdaQuery().eq(AcademicYearSemester::getIsDefault,YesNoEnum.YES));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Integer week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        QueryWrapper<Course> queryWrapper = new QueryWrapper<Course>()
                .eq("academic_year_semester_id", academicYearSemester.getId())
                .eq("week", week)
                .eq("teacher_id", user.getStaffId());
        List<Course> courseList = courseService.list(queryWrapper);
        LeaderDuty leaderDuty = leaderDutyService.getOne(Wrappers.<LeaderDuty>lambdaQuery()
                .eq(LeaderDuty::getLeaderId,user.getStaffId())
                .between(LeaderDuty::getStartTime, DateUtils.parse("00:00:00",new Date()),DateUtils.parse("23:59:59",new Date()))
        );
        TeacherDuty teacherDuty = teacherDutyService.getOne(Wrappers.<TeacherDuty>lambdaQuery()
                .eq(TeacherDuty::getTeacherId,user.getStaffId())
                .between(TeacherDuty::getStartTime, DateUtils.parse("00:00:00",new Date()),DateUtils.parse("23:59:59",new Date()))
        );
        Integer count = 0;
        if(leaderDuty !=null){
            count += 1;
        }
        if(teacherDuty !=null){
            count += 1;
        }

        Map<String,Object> map = new HashMap<>();
        map.put("course",courseList);
        map.put("user",user);
        map.put("duty",count);
        return ApiResponse.ok(map);
    }

    @GetMapping("/clazzInfo")
    @ApiOperation("班级基本数据")
    public ApiResponse<Object> realTimeDateClazzInfo(@RequestParam Long clazzId,
                                                     @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam(value = "time") Date time) {
        Clazz clazz = clazzService.getById(clazzId);
        List<Student> studentList = studentService.listByClazz(clazzId);
        Course course = courseService.getCurrentCourseByClazzId(clazz,time);


        Map<String,Object> map = new HashMap<>();
        map.put("clazz",clazz);
        map.put("course",course);
        map.put("male",studentList.stream().filter(item->
                GenderEnum.M.equals(item.getGender())
        ).collect(Collectors.toList()).size());
        map.put("female",studentList.stream().filter(item->
                GenderEnum.W.equals(item.getGender())
        ).collect(Collectors.toList()).size());
        map.put("accommodation",studentList.stream().filter(item->
                StudentTypeEnum.LIVE.equals(item.getStudentType())
        ).collect(Collectors.toList()).size());
        map.put("maleAccommodation",studentList.stream().filter(item->
                StudentTypeEnum.LIVE.equals(item.getStudentType()) && GenderEnum.M.equals(item.getGender())
                ).collect(Collectors.toList()).size());
        map.put("femaleAccommodation",studentList.stream().filter(item->
                StudentTypeEnum.LIVE.equals(item.getStudentType()) && GenderEnum.W.equals(item.getGender())
        ).collect(Collectors.toList()).size());
        return ApiResponse.ok(map);
    }

    @GetMapping("/clazz/subject")
    @ApiOperation("班级课程信息数据")
    public ApiResponse<Object> realTimeDateClazzSubject(@RequestParam Long clazzId,
                                                        @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam(value = "time") Date time) {
        AcademicYearSemester academicYearSemester = academicYearSemesterService.getOne(Wrappers.<AcademicYearSemester>lambdaQuery().eq(AcademicYearSemester::getIsDefault,YesNoEnum.YES));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        Integer week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        QueryWrapper<Course> queryWrapper = new QueryWrapper<Course>()
                .eq("academic_year_semester_id", academicYearSemester.getId())
                .eq("clazz_id", clazzId)
                .eq("week", week);
        List<Course> courseList = courseService.list(queryWrapper);
        return ApiResponse.ok(courseList);
    }

    @GetMapping("/clazz/teacher/list")
    @ApiOperation("班级教师信息数据")
    public ApiResponse<Object> realTimeDateClazzTeacherList(@RequestParam Long clazzId,
                                                        @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam(value = "time") Date time) {
        AcademicYearSemester academicYearSemester = academicYearSemesterService.getOne(Wrappers.<AcademicYearSemester>lambdaQuery().eq(AcademicYearSemester::getIsDefault,YesNoEnum.YES));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        Integer week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        QueryWrapper<Course> queryWrapper = new QueryWrapper<Course>()
                .eq("academic_year_semester_id", academicYearSemester.getId())
                .eq("clazz_id", clazzId)
                .eq("week", week);
        List<Course> courseList = courseService.list(queryWrapper);

        List<StaffLessonTeacher> staffLessonTeacherList = staffLessonTeacherService.list(Wrappers.<StaffLessonTeacher>lambdaQuery()
                .eq(StaffLessonTeacher::getClazzId,clazzId)
                .eq(StaffLessonTeacher::getIsCurrent,YesNoEnum.YES)
        );
        List<ClazzTeacherDto> clazzTeacherDtos = new ArrayList<>();
        Map<Long,List<StaffLessonTeacher>> map = staffLessonTeacherList.stream().collect(Collectors.groupingBy(StaffLessonTeacher::getStaffId));
        for(Long staffId : map.keySet()){
            StaffLessonTeacher staffLessonTeacher = map.get(staffId).get(0);
            ClazzTeacherDto clazzTeacherDto = new ClazzTeacherDto();
            if(staffLessonTeacher.getStaff() != null){
                clazzTeacherDto.setStaffId(staffLessonTeacher.getStaffId());
                clazzTeacherDto.setName(staffLessonTeacher.getStaff().getName());
                Integer count = courseList.stream().filter(course -> course.getTeacherId().equals(staffLessonTeacher.getStaffId())).collect(Collectors.toList()).size();
                clazzTeacherDto.setCount(count);
                List<StaffSubject> staffSubjectList = staffSubjectService.list(Wrappers.<StaffSubject>lambdaQuery()
                        .eq(StaffSubject::getStaffId,staffLessonTeacher.getStaffId())
                        .eq(StaffSubject::getIsCurrent,YesNoEnum.YES)
                );
                clazzTeacherDto.setSubjectName(staffSubjectList.stream().map(i->i.getSubject() == null ? i.getSubjectId().toString() : i.getSubject().getName()).collect(Collectors.joining(",")));
                clazzTeacherDtos.add(clazzTeacherDto);
            }
        }
        return ApiResponse.ok(clazzTeacherDtos);
    }

    @GetMapping("/clazz/subject/meeting")
    @ApiOperation("班级课程推荐会议")
    public ApiResponse<Object> realTimeDateClazzSubjectMeeting(@RequestParam List<Long> teacherIdList,
                                                               @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam(value = "time") Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        Integer week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        List<Long> sortOrder = courseService.getMeetingTime(teacherIdList,week);
        return ApiResponse.ok(sortOrder);
    }

    @GetMapping("/statistics/daily/group/time")
    @ApiOperation("白班统计数据(按时间分类)")
    public ApiResponse<Object> dailyStatisticsGroupByTime(@RequestParam(value = "gradeId",required = false)Long gradeId,
                                                          @RequestParam(value = "clazzId",required = false)Long clazzId,
                                                          @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam(value = "startTime",required = false) Date startTime,
                                                          @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam(value = "endTime") Date endTime) {
        AcademicYearSemester academicYearSemester = academicYearSemesterService.getOne(Wrappers.<AcademicYearSemester>lambdaQuery().eq(AcademicYearSemester::getIsDefault,YesNoEnum.YES));
        if(startTime == null){
            startTime = academicYearSemester.getStartTime();
        }
        List<Map<String,Object>> ob = dailyAttendanceService.searchStatistics(null,academicYearSemester.getId(),gradeId,clazzId,null,null,startTime,endTime,null);
        return ApiResponse.ok(ob);
    }

    @GetMapping("/statistics/daily/group/clazz")
    @ApiOperation("白班统计数据(按班级分类)")
    public ApiResponse<Object> dailyStatisticsGroupByClazz(@RequestParam(value = "gradeId",required = false)Long gradeId,
                                               @RequestParam(value = "clazzId",required = false)Long clazzId,
                                                           @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam(value = "startTime",required = false) Date startTime,
                                                           @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam(value = "endTime") Date endTime) {
        AcademicYearSemester academicYearSemester = academicYearSemesterService.getOne(Wrappers.<AcademicYearSemester>lambdaQuery().eq(AcademicYearSemester::getIsDefault,YesNoEnum.YES));
        if(startTime == null){
            startTime = academicYearSemester.getStartTime();
        }
        List<Map<String,Object>> ob = dailyAttendanceService.searchStatisticsGroupByClazz(null,academicYearSemester.getId(),gradeId,clazzId,null,null,startTime,endTime,null);
        return ApiResponse.ok(ob);
    }

    @GetMapping("/statistics/night/group/time")
    @ApiOperation("晚班统计数据(按时间分类)")
    public ApiResponse<Object> nightStatisticsGroupByTime(@RequestParam(value = "dutyType",required = false)TeacherDutyTypeEnum dutyType,
                                                          @RequestParam(value = "gradeId",required = false)Long gradeId,
                                                          @RequestParam(value = "clazzId",required = false)Long clazzId,
                                                          @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam(value = "startTime",required = false) Date startTime,
                                                          @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam(value = "endTime") Date endTime) {
        AcademicYearSemester academicYearSemester = academicYearSemesterService.getOne(Wrappers.<AcademicYearSemester>lambdaQuery().eq(AcademicYearSemester::getIsDefault,YesNoEnum.YES));
        if(startTime == null){
            startTime = academicYearSemester.getStartTime();
        }
        List<Map<String,Object>> ob = nightStudyAttendanceService.searchNightStatisticsGroupByTime(null,academicYearSemester.getId(),gradeId,clazzId,null,null,startTime,endTime,null,dutyType);
        return ApiResponse.ok(ob);
    }

    @GetMapping("/statistics/night/group/clazz")
    @ApiOperation("晚班统计数据(按班级分类)")
    public ApiResponse<Object> nightStatisticsGroupByClazz(@RequestParam(value = "dutyType",required = false)TeacherDutyTypeEnum dutyType,
                                                           @RequestParam(value = "gradeId",required = false)Long gradeId,
                                                           @RequestParam(value = "clazzId",required = false)Long clazzId,
                                                           @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam(value = "startTime",required = false) Date startTime,
                                                           @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam(value = "endTime") Date endTime) {
        AcademicYearSemester academicYearSemester = academicYearSemesterService.getOne(Wrappers.<AcademicYearSemester>lambdaQuery().eq(AcademicYearSemester::getIsDefault,YesNoEnum.YES));
        if(startTime == null){
            startTime = academicYearSemester.getStartTime();
        }
        List<Map<String,Object>> ob = nightStudyAttendanceService.searchNightStatisticsGroupByClazz(null,academicYearSemester.getId(),gradeId,clazzId,null,null,startTime,endTime,null,dutyType);
        return ApiResponse.ok(ob);
    }

    @GetMapping("/statistics/teaching-result/card")
    @ApiOperation("教学成果工作台(卡片)")
    public ApiResponse<Object> teachingResultStatisticsCard(@RequestParam(value = "type") String type) {
        Map<String, Object> res = new HashMap<>();
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (user.getStaffId() <= 0)
            throw new ApiCode.ApiException(-5,"该用户非职工，无法查看数据");
        List<TeachingResult> teachingResults = null;
        if ("01".equals(type)) {
            // 我的成果
            teachingResults = this.teachingResultService.list(Wrappers.<TeachingResult>lambdaQuery()
                    .eq(TeachingResult::getTeacherId, user.getStaffId())
                    .eq(TeachingResult::getState, TeachingResultStateEnum.PASSED));
        } else {
            Set<Long> teacherIds = new HashSet<>();
            teacherIds.add(user.getStaffId());
            List<StaffResearchLeader> staffResearchLeaders = user.getStaff().getStaffResearchLeaderList();
            List<StaffGradeLeader> staffGradeLeaders = user.getStaff().getStaffGradeLeaderList();
            // 教研组长
            if (CollectionUtils.isNotEmpty(staffResearchLeaders)) {
                List<Long> subjectIds = staffResearchLeaders.stream().map(StaffResearchLeader::getSubjectId).collect(Collectors.toList());
                List<StaffSubject> staffSubjects = this.staffSubjectService.list(Wrappers.<StaffSubject>lambdaQuery()
                        .in(StaffSubject::getSubjectId, subjectIds));
                if (CollectionUtils.isNotEmpty(staffSubjects)) {
                    teacherIds.addAll(staffSubjects.stream().map(StaffSubject::getStaffId).collect(Collectors.toList()));
                }
            }
            // 年级组长
            if (CollectionUtils.isNotEmpty(staffGradeLeaders)) {
                List<Long> gradeIds = staffGradeLeaders.stream().map(StaffGradeLeader::getGradeId).collect(Collectors.toList());
                List<Clazz> clazzList = clazzService.list(Wrappers.<Clazz>lambdaQuery()
                        .select(Clazz::getId)
                        .in(Clazz::getGradeId, gradeIds));
                if (CollectionUtils.isNotEmpty(clazzList)) {
                    List<StaffLessonTeacher> staffLessonTeachers = this.staffLessonTeacherService.list(Wrappers.<StaffLessonTeacher>lambdaQuery()
                            .in(StaffLessonTeacher::getClazzId, clazzList.stream().map(Clazz::getId).collect(Collectors.toList())));
                    if (CollectionUtils.isNotEmpty(staffLessonTeachers)) {
                        teacherIds.addAll(staffLessonTeachers.stream().map(StaffLessonTeacher::getStaffId).collect(Collectors.toList()));
                    }
                }
            }
            teachingResults = this.teachingResultService.list(Wrappers.<TeachingResult>lambdaQuery()
                    .in(TeachingResult::getTeacherId, new ArrayList<>(teacherIds))
                    .eq(TeachingResult::getState, TeachingResultStateEnum.PASSED));
        }
        if (CollectionUtils.isEmpty(teachingResults))
            teachingResults = new ArrayList<>();
        // 基本信息
        Map<String, Object> baseInfo = new HashMap<>();
        List<TeachingResult> teachingResultsGrry = teachingResults.stream()
                .filter(item -> "个人荣誉".equals(item.getResultClassify().getName()) && Objects.equals(item.getTeacherId(), user.getStaffId()))
                .collect(Collectors.toList());
        baseInfo.put("grry", CollectionUtils.isEmpty(teachingResultsGrry) ? "" :
                teachingResultsGrry.stream().map(TeachingResult::getName).distinct().collect(Collectors.joining(",")));
        baseInfo.put("name", user.getStaff().getName());
        baseInfo.put("sex", user.getStaff().getGender().getName());
        baseInfo.put("age", TwxUtils.getAgeByIDNumber(user.getStaff().getIdNumber()));
        baseInfo.put("education", user.getStaff().getEducation());
        baseInfo.put("title", user.getStaff().getTitle());
        List<Subject> subjects = this.subjectService.list();
        List<StaffLessonTeacher> staffLessonTeachers = user.getStaff().getStaffLessonTeacherList();
        List<Long> subjectIds = staffLessonTeachers.stream().map(StaffLessonTeacher::getSubjectId).distinct().collect(Collectors.toList());
        String subject = subjects.stream().filter(o -> subjectIds.contains(o.getId())).map(Subject::getName).collect(Collectors.joining(","));
        baseInfo.put("subject", subject);
        res.put("baseInfo", baseInfo);
        // 授课信息 曲线图
        Map<String, Object> scoreInfo = new HashMap<>();
        if (CollectionUtils.isNotEmpty(staffLessonTeachers)) {
            List<ExamClazzAnalyseClazzAvgDto> list = this.examResultMapper.clazzSubjectAnalyse(1L, user.getAcademicYearSemester().getYear());
            this.getScore(scoreInfo, list, staffLessonTeachers, subjects);
        }
        res.put("lessonCount", staffLessonTeachers.size());
        res.put("scoreInfo", scoreInfo);
        // 成果信息 柱状图
        Map<String, Long> resultInfo = teachingResults.stream()
                .collect(Collectors.collectingAndThen(Collectors.groupingBy(o -> o.getResultDate().substring(0, 4), Collectors.counting()), TreeMap::new));
        res.put("resultInfo", resultInfo.values());
        List<Map<String, Object>> list = new ArrayList<>();
        resultInfo.forEach((k, v) -> {
            Map<String, Object> map = new HashMap<>();
            map.put("time", k);
            map.put("count", v);
            list.add(map);
        });
        res.put("resultInfoChart", list);
        res.put("resultInfoTotal", teachingResults.size());
        // 教师成长 --暂无
        return ApiResponse.ok(res);
    }

    @GetMapping("/statistics/teaching-result/calculate")
    @ApiOperation("教学成果工作台(统计)")
    public ApiResponse<Object> teachingResultStatistics(@RequestParam(value = "type") String type,
                                                        @DateTimeFormat(pattern="yyyy-MM") @RequestParam(value = "startTime", required = false) Date startTime,
                                                        @DateTimeFormat(pattern="yyyy-MM") @RequestParam(value = "endTime", required = false) Date endTime) throws ParseException {
        Map<String, Object> res = new HashMap<>();
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (user.getStaffId() <= 0)
            throw new ApiCode.ApiException(-5,"该用户非职工，无法查看数据");
        List<TeachingResult> teachingResults = null;
        if ("01".equals(type)) {
            // 我的成果
            teachingResults = this.teachingResultService.list(Wrappers.<TeachingResult>lambdaQuery()
                    .eq(TeachingResult::getTeacherId, user.getStaffId())
                    .eq(TeachingResult::getState, TeachingResultStateEnum.PASSED));
        } else {
            Set<Long> teacherIds = new HashSet<>();
            teacherIds.add(user.getStaffId());
            List<StaffResearchLeader> staffResearchLeaders = user.getStaff().getStaffResearchLeaderList();
            List<StaffGradeLeader> staffGradeLeaders = user.getStaff().getStaffGradeLeaderList();
            // 教研组长
            if (CollectionUtils.isNotEmpty(staffResearchLeaders)) {
                List<Long> subjectIds = staffResearchLeaders.stream().map(StaffResearchLeader::getSubjectId).collect(Collectors.toList());
                List<StaffSubject> staffSubjects = this.staffSubjectService.list(Wrappers.<StaffSubject>lambdaQuery()
                        .in(StaffSubject::getSubjectId, subjectIds));
                if (CollectionUtils.isNotEmpty(staffSubjects)) {
                    teacherIds.addAll(staffSubjects.stream().map(StaffSubject::getStaffId).collect(Collectors.toList()));
                }
            }
            // 年级组长
            if (CollectionUtils.isNotEmpty(staffGradeLeaders)) {
                List<Long> gradeIds = staffGradeLeaders.stream().map(StaffGradeLeader::getGradeId).collect(Collectors.toList());
                List<Clazz> clazzList = clazzService.list(Wrappers.<Clazz>lambdaQuery()
                        .select(Clazz::getId)
                        .in(Clazz::getGradeId, gradeIds));
                if (CollectionUtils.isNotEmpty(clazzList)) {
                    List<StaffLessonTeacher> staffLessonTeachers = this.staffLessonTeacherService.list(Wrappers.<StaffLessonTeacher>lambdaQuery()
                            .in(StaffLessonTeacher::getClazzId, clazzList.stream().map(Clazz::getId).collect(Collectors.toList())));
                    if (CollectionUtils.isNotEmpty(staffLessonTeachers)) {
                        teacherIds.addAll(staffLessonTeachers.stream().map(StaffLessonTeacher::getStaffId).collect(Collectors.toList()));
                    }
                }
            }
            teachingResults = this.teachingResultService.list(Wrappers.<TeachingResult>lambdaQuery()
                    .in(TeachingResult::getTeacherId, new ArrayList<>(teacherIds))
                    .eq(TeachingResult::getState, TeachingResultStateEnum.PASSED));
        }
        if (CollectionUtils.isEmpty(teachingResults)) {
            teachingResults = new ArrayList<>();
        } else {
            if (startTime != null && endTime != null) {
                Iterator<TeachingResult> iterator = teachingResults.iterator();
                while (iterator.hasNext()) {
                    Date curr = DateUtils.parse(iterator.next().getResultDate(), "yyyy-MM");
                    if (curr.before(startTime) || curr.after(endTime)) {
                        iterator.remove();
                    }
                }
            }
        }
        // 授课
        res.put("lessonCount", user.getStaff().getStaffLessonTeacherList().size());
        // 研究课题
        res.put("researchTopic", teachingResults.stream()
                .filter(o -> o.getResultClassify().getName().contains("课题")
                        || o.getResultClassify().getName().contains("研究")).count());
        // 获奖项目
        res.put("awardProject", teachingResults.stream()
                .filter(o -> o.getResultClassify().getName().contains("荣誉")
                        || o.getResultClassify().getName().contains("奖")).count());
        // 著作论文
        res.put("workPaper", teachingResults.stream()
                .filter(o -> o.getResultClassify().getName().contains("教材") || o.getResultClassify().getName().contains("论文")
                    || o.getResultClassify().getName().contains("出版")).count());
        // 公开课
        res.put("openClass", teachingResults.stream()
                .filter(o -> o.getResultClassify().getName().contains("示范课") || o.getResultClassify().getName().contains("讲座")).count());
        // 培训进修
        res.put("trainingFurther", teachingResults.stream()
                .filter(o -> o.getResultClassify().getName().contains("进修") || o.getResultClassify().getName().contains("培训")).count());
        return ApiResponse.ok(res);
    }

    private BigDecimal getSubject(String subjectName, ExamClazzAnalyseClazzAvgDto obj) {
        BigDecimal count;
        if ("语文".equals(subjectName)) {
            count = obj.getChineseAvg();
        } else if ("数学".equals(subjectName)) {
            count = obj.getMathAvg();
        } else if ("英语".equals(subjectName)) {
            count = obj.getEnglishAvg();
        } else if ("物理".equals(subjectName)) {
            count = obj.getPhysicsAvg();
        } else if ("历史".equals(subjectName)) {
            count = obj.getHistoryAvg();
        } else if ("化学".equals(subjectName)) {
            count = obj.getChemistryAvg();
        } else if ("生物".equals(subjectName)) {
            count = obj.getBiologyAvg();
        } else if ("政治".equals(subjectName)) {
            count = obj.getPoliticsAvg();
        } else {
            count = obj.getGeographyAvg();
        }
        return count;
    }

    private void getScore(Map<String, Object> scoreInfo, List<ExamClazzAnalyseClazzAvgDto> list, List<StaffLessonTeacher> staffLessonTeachers, List<Subject> subjects) {
        if (CollectionUtils.isNotEmpty(list)) {
            subjects = subjects.stream().filter(item -> YesNoEnum.YES.equals(item.getIsMain())).collect(Collectors.toList());
            Map<Long, String> map1 = subjects.stream().collect(Collectors.toMap(Subject::getId, Subject::getName));
            List<Clazz> clazzList = this.clazzService.list(Wrappers.<Clazz>lambdaQuery()
                    .select(Clazz::getId, Clazz::getName));
            Map<Long, String> map2 = clazzList.stream().collect(Collectors.toMap(Clazz::getId, Clazz::getName));
            List<BigDecimal> scoreList = new ArrayList<>();
            BigDecimal base = BigDecimal.ZERO;
            for (StaffLessonTeacher item : staffLessonTeachers) {
                if (!map1.containsKey(item.getSubjectId())) {
                    continue;
                }
                String subjectName = map1.get(item.getSubjectId());
                String clazzName = map2.get(item.getClazzId());
                String key = clazzName.concat(subjectName);
                List<Integer> list1 = new ArrayList<>();
                scoreInfo.put(key, list1);
                for (int i = 0; i < list.size(); i++) {
                    ExamClazzAnalyseClazzAvgDto curr = list.get(i);
                    BigDecimal score = this.getSubject(subjectName, curr);
                    if (i == 0 || (i < list.size() - 1 && Objects.equals(list.get(i + 1).getExamId(), curr.getExamId()))) {
                        scoreList.add(score);
                    }
                    if (curr.getClazzName().equals(clazzName)) {
                        base = score;
                    }
                    if (i == list.size() - 1 || !Objects.equals(list.get(i + 1).getExamId(), curr.getExamId())) {
                        int count = 1;
                        for (BigDecimal bigDecimal : scoreList) {
                            if (bigDecimal.compareTo(base) > 0) {
                                count++;
                            }
                        }
                        list1.add(count);
                        scoreList.clear();
                    }
                }
            }
        }
    }

    @GetMapping("/get/user/tab")
    @ApiOperation("获取工作台tab")
    public ApiResponse<Object> getUserTab() {
       User user = (User)SecurityUtils.getSubject().getPrincipal();
       if(user.getStaffId() == null || user.getStaffId() == 0)
           throw new ApiCode.ApiException(-5,"用户非教职工，无法查看");
       Staff staff = staffService.getById(user.getStaffId());
       Map<String,Object> map = new HashMap<>();
       if(CollectionUtils.isNotEmpty(staff.getStaffClazzAdviserList())){
           List<StaffClazzAdviser> staffClazzAdviserList = staff.getStaffClazzAdviserList().stream()
                   .filter(i->YesNoEnum.YES.equals(i.getIsCurrent())).collect(Collectors.toList());
           if(CollectionUtils.isNotEmpty(staffClazzAdviserList)){
               Clazz clazz = clazzService.getById(staffClazzAdviserList.get(0).getClazzId());
               staffClazzAdviserList.get(0).setClazz(clazz);
               map.put("clazzAdviser",staffClazzAdviserList.get(0));
           }
       }
       if(CollectionUtils.isNotEmpty(staff.getStaffGradeLeaderList())){
           List<StaffGradeLeader> staffGradeLeaderList = staff.getStaffGradeLeaderList().stream()
                   .filter(i->YesNoEnum.YES.equals(i.getIsCurrent())).collect(Collectors.toList());
           if(CollectionUtils.isNotEmpty(staffGradeLeaderList)){
               map.put("gradeLeader",staff.getStaffGradeLeaderList().get(0));
           }
       }
       if(CollectionUtils.isNotEmpty(staff.getStaffLessonTeacherList())){
           Map<Long,List<Subject>> subjectMap = subjectService.list(Wrappers.<Subject>lambdaQuery()
                   .eq(Subject::getIsMain,YesNoEnum.YES)).stream().collect(Collectors.groupingBy(Subject::getId));
           List<StaffLessonTeacher> staffLessonTeacherList = staff.getStaffLessonTeacherList();
           Iterator<StaffLessonTeacher> it = staffLessonTeacherList.iterator();
           Map<Long,Integer> flag = new HashMap<>();
           while (it.hasNext()){
               StaffLessonTeacher staffLessonTeacher  = it.next();
               if(flag.containsKey(staffLessonTeacher.getClazzId())
                       || YesNoEnum.NO.equals(staffLessonTeacher.getIsCurrent())
                       || !subjectMap.containsKey(staffLessonTeacher.getSubjectId())){
                   it.remove();
               }else{
                   flag.put(staffLessonTeacher.getClazzId(),1);
               }
           }
           staffLessonTeacherList.stream().forEach(staffLessonTeacher -> {
               Clazz clazz = clazzService.getById(staffLessonTeacher.getClazzId());
               staffLessonTeacher.setSubject(subjectMap.get(staffLessonTeacher.getSubjectId()).get(0));
               staffLessonTeacher.setClazz(clazz);
           });
           if(CollectionUtils.isNotEmpty(staffLessonTeacherList)){
               map.put("lessonTeacher",staffLessonTeacherList);
           }
       }

       if(staff.getFunction() != null){
           if(FunctionEnum.PRINCIPAL.equals(staff.getFunction())
                   || FunctionEnum.DEAN.equals(staff.getFunction())){
               map.put("all",-1);
           }
       }
        return ApiResponse.ok(map);
    }

    @GetMapping("/mutate-year")
    @ApiOperation("切换学年获取班级")
    public ApiResponse<Object> mutateYear(@RequestParam(value = "academicYearSemesterId", required = false) Long academicYearSemesterId) {
        return ApiResponse.ok(userService.mutateYear(academicYearSemesterId));
    }
}
