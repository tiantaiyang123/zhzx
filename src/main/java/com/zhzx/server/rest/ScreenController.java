package com.zhzx.server.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.*;
import com.zhzx.server.dto.TeacherDutyDto;
import com.zhzx.server.enums.*;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.*;
import com.zhzx.server.util.DateUtils;
import com.zhzx.server.util.StringUtils;
import com.zhzx.server.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by A2 on 2022/2/24.
 */
@Slf4j
@RestController
@Api(tags = "ScreenController", description = "大屏数据")
@RequestMapping("/v1/system/screen")
public class ScreenController {
    @Resource
    private StaffService staffService;
    @Resource
    private ClazzService clazzService;
    @Resource
    private TeacherDutyService teacherDutyService;
    @Resource
    private StudentService studentService;
    @Resource
    private GradeService gradeService;
    @Resource
    private DailyAttendanceService dailyAttendanceService;
    @Resource
    private NightStudyAttendanceService nightStudyAttendanceService;
    @Resource
    private LabelService labelService;
    @Resource
    private CourseService courseService;
    @Resource
    private AcademicYearSemesterService academicYearSemesterService;
    @Resource
    private LeaderDutyService leaderDutyService;
    @Resource
    private IllService illService;
    @Resource
    private NightStudyAttendanceSubService nightStudyAttendanceSubService;
    /**
     *基本人数
     */
    @GetMapping("/staff")
    @ApiOperation("基本人数")
    public ApiResponse<Map<String,Object>> getStaff() {
        Map<String,Object> map = new HashMap<>();
        List<Staff> staffList = this.staffService.list(Wrappers.<Staff>lambdaQuery().eq(Staff::getIsDelete, YesNoEnum.NO));
        if(CollectionUtils.isNotEmpty(staffList)){
            map.put("total",staffList.size());
            map.put("teacher",staffList.stream().filter(staff -> PersonnelSituationEnum.TEACHER.equals(staff.getPersonnelSituation())).count());
            map.put("staff",staffList.stream().filter(staff -> PersonnelSituationEnum.STAFF.equals(staff.getPersonnelSituation())).count());
            map.put("male",staffList.stream().filter(staff -> GenderEnum.M.equals(staff.getGender())).count());
            map.put("female",staffList.stream().filter(staff -> GenderEnum.W.equals(staff.getGender())).count());
        }
        return ApiResponse.ok(map);
    }
    @GetMapping("/student")
    @ApiOperation("学生信息")
    public ApiResponse<Map<String,Object>> getStudent(@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam("startTime") Date startTime,
                                                      @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam("endTime") Date endTime) {
        Map<String,Object> map = new HashMap<>();
        List<StudentVo> studentList = studentService.selectListWithClazz(null,null);
        if(CollectionUtils.isNotEmpty(studentList)){
            map.put("total",studentList.size());
            map.put("male",studentList.stream().filter(student -> GenderEnum.M.equals(student.getGender())).count());
            map.put("female",studentList.stream().filter(student -> GenderEnum.W.equals(student.getGender())).count());
            AcademicYearSemester academicYearSemester = academicYearSemesterService.getOne(Wrappers.<AcademicYearSemester>lambdaQuery().eq(AcademicYearSemester::getIsDefault,YesNoEnum.YES));
            Integer count = dailyAttendanceService.count(Wrappers.<DailyAttendance>lambdaQuery()
                    .eq(DailyAttendance::getAcademicYearSemesterId,academicYearSemester.getId())
                    .ge(DailyAttendance::getRegisterDate,startTime)
                    .le(DailyAttendance::getRegisterDate,endTime)
            );
            map.put("absence",count);
        }
        return ApiResponse.ok(map);
    }

    @GetMapping("/student/accommodation")
    @ApiOperation("学生住宿信息")
    public ApiResponse<Map<String,Object>> getStudentAccommodation() {
        Map<String,Object> map = new HashMap<>();
        List<StudentVo> studentList = studentService.selectListWithClazz(null,null);
        if(CollectionUtils.isNotEmpty(studentList)){
            map.put("gradeOneLive",studentList.stream().filter(item->item.getGradeId() == 1L && StudentTypeEnum.LIVE.equals(item.getStudentType())).collect(Collectors.toList()).size());
            map.put("gradeOneOut",studentList.stream().filter(item->item.getGradeId() == 1L && StudentTypeEnum.DAY.equals(item.getStudentType())).collect(Collectors.toList()).size());
            map.put("gradeTwoLive",studentList.stream().filter(item->item.getGradeId() == 2L && StudentTypeEnum.LIVE.equals(item.getStudentType())).collect(Collectors.toList()).size());
            map.put("gradeTwoOut",studentList.stream().filter(item->item.getGradeId() == 2L && StudentTypeEnum.DAY.equals(item.getStudentType())).collect(Collectors.toList()).size());
            map.put("gradeThreeLive",studentList.stream().filter(item->item.getGradeId() == 3L && StudentTypeEnum.LIVE.equals(item.getStudentType())).collect(Collectors.toList()).size());
            map.put("gradeThreeOut",studentList.stream().filter(item->item.getGradeId() == 3L && StudentTypeEnum.DAY.equals(item.getStudentType())).collect(Collectors.toList()).size());
        }
        return ApiResponse.ok(map);
    }

    /**
     *老师请假情况
     */
    @GetMapping("/staff/leave")
    @ApiOperation("老师请假情况（暂无）")
    public ApiResponse<Map<String,Object>> getTeacherScreen() {
        Map<String,Object> map = new HashMap<>();
        return ApiResponse.ok(map);
    }

    /**
     *职员信息
     */
    @GetMapping("/staff/positional")
    @ApiOperation("职员职称信息")
    public ApiResponse<Map<String,Object>> getStaffPositional() {
        List<Staff> staffList = staffService.list(Wrappers.<Staff>lambdaQuery()
                .eq(Staff::getIsDelete,YesNoEnum.NO)
                .eq(Staff::getPersonnelSituation,PersonnelSituationEnum.TEACHER)
        );
        Map<String,List<Staff>> map = staffList.stream().map(staff -> {
            if(StringUtils.isNullOrEmpty(staff.getTitle())){
                staff.setTitle("其他");
            }
            return staff;
        }).collect(Collectors.groupingBy(Staff::getTitle));
        LinkedHashMap<String,List<Staff>> result = new LinkedHashMap<>();
        if(map.containsKey("正高级")){
            result.put("正高级",map.get("正高级"));
            map.remove("正高级");
        }
        if(map.containsKey("高级")){
            result.put("高级",map.get("高级"));
            map.remove("高级");
        }
        if(map.containsKey("一级")){
            result.put("一级",map.get("一级"));
            map.remove("一级");
        }
        if(map.containsKey("二级")){
            result.put("二级",map.get("二级"));
            map.remove("二级");
        }
        List<String> stringList = map.keySet().stream().filter(item->!Objects.equals("其他",item)).collect(Collectors.toList());
        stringList.stream().forEach(item->{
            result.put(item,map.get(item));
        });
        if(map.containsKey("其他")){
            result.put("其他",map.get("其他"));
            map.remove("其他");
        }
        return ApiResponse.ok(result);
    }

    /**
     *职员信息
     */
    @GetMapping("/staff/honor")
    @ApiOperation("职员荣誉信息")
    public ApiResponse<Map<String,Object>> getStaffHonor() {
        List<Staff> staffList = staffService.list(Wrappers.<Staff>lambdaQuery()
                .eq(Staff::getIsDelete,YesNoEnum.NO)
                .eq(Staff::getPersonnelSituation,PersonnelSituationEnum.TEACHER)
        );
        Map<String,List<Staff>> map = staffList.stream()
                .filter(item->item.getHonor() != null && item.getHonor() != "")
                .collect(Collectors.groupingBy(Staff::getHonor));
        LinkedHashMap<String,List<Staff>> result = new LinkedHashMap<>();
        if(map.containsKey("名特优教师")){
            result.put("名特优教师",map.get("名特优教师"));
            map.remove("名特优教师");
        }
        if(map.containsKey("特级教师")){
            result.put("特级教师",map.get("特级教师"));
            map.remove("特级教师");
        }
        if(map.containsKey("市学科带头人")){
            result.put("市学科带头人",map.get("市学科带头人"));
            map.remove("市学科带头人");
        }
        if(map.containsKey("市优秀青年教师")){
            result.put("市优秀青年教师",map.get("市优秀青年教师"));
            map.remove("市优秀青年教师");
        }
        if(map.containsKey("区学科带头人")){
            result.put("区学科带头人",map.get("区学科带头人"));
            map.remove("区学科带头人");
        }
        if(map.containsKey("区优秀青年教师")){
            result.put("区优秀青年教师",map.get("区优秀青年教师"));
            map.remove("区优秀青年教师");
        }
        List<String> stringList = map.keySet().stream().filter(item->!Objects.equals("",item)).collect(Collectors.toList());
        stringList.stream().forEach(item->{
            result.put(item,map.get(item));
        });
        return ApiResponse.ok(result);
    }


    @GetMapping("/current/course")
    @ApiOperation("查询当前课程")
    public ApiResponse<Map<String,Object>> getCurrentCourse(@RequestParam Integer gradeId,
                                                            @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam Date time) {
        return ApiResponse.ok(courseService.getCurrentCourse(gradeId,time));
    }

    /**
     *职员信息
     */
    @GetMapping("/student/attendance/workbench")
    @ApiOperation("学生请假")
    public ApiResponse<Map<String,Object>> getStudentAttendance(@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam("startTime") Date startTime,
                                                                @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam("endTime") Date endTime,
                                                                @RequestParam(required = false) StudentTypeEnum studentTypeEnum) {


        AcademicYearSemester academicYearSemester = academicYearSemesterService.getOne(Wrappers.<AcademicYearSemester>lambdaQuery().eq(AcademicYearSemester::getIsDefault,YesNoEnum.YES));
        List<DailyAttendance> dailyAttendanceList = dailyAttendanceService.list(Wrappers.<DailyAttendance>lambdaQuery()
                .eq(DailyAttendance::getAcademicYearSemesterId,academicYearSemester.getId())
                .ge(DailyAttendance::getRegisterDate,startTime)
                .le(DailyAttendance::getRegisterDate,endTime)
        );
        if(CollectionUtils.isEmpty(dailyAttendanceList))
            return ApiResponse.ok(null);

        if(studentTypeEnum != null){
           if(StudentTypeEnum.DAY.equals(studentTypeEnum)){
               List<DailyAttendance> day = dailyAttendanceList.stream().filter(item->StudentTypeEnum.DAY.equals(item.getStudent().getStudentType())).collect(Collectors.toList());
               Map<String,List<DailyAttendance>> map1 = day.stream().collect(Collectors.groupingBy(DailyAttendance::getClassify));
               return ApiResponse.ok(map1);
           }
           if(StudentTypeEnum.LIVE.equals(studentTypeEnum)){
               List<DailyAttendance> day = dailyAttendanceList.stream().filter(item->StudentTypeEnum.LIVE.equals(item.getStudent().getStudentType())).collect(Collectors.toList());
               Map<String,List<DailyAttendance>> map1 = day.stream().collect(Collectors.groupingBy(DailyAttendance::getClassify));
               return ApiResponse.ok(map1);
           }
        }else{
            Map<String,List<DailyAttendance>> map1 = dailyAttendanceList.stream().collect(Collectors.groupingBy(DailyAttendance::getClassify));
            return ApiResponse.ok(map1);
        }
        return ApiResponse.ok(null);
    }

    /**
     *职员信息
     */
    @GetMapping("/student/attendance")
    @ApiOperation("学生考勤")
    public ApiResponse<Map<String,Object>> getStudentAttendance(@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam("startTime") Date startTime,
                                                                @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam("endTime") Date endTime) {

        Map<String,Object> map = new HashMap<>();
        QueryWrapper<Clazz> wrapper = new QueryWrapper<>();
        AcademicYearSemester academicYearSemester = academicYearSemesterService.getOne(Wrappers.<AcademicYearSemester>lambdaQuery().eq(AcademicYearSemester::getIsDefault,YesNoEnum.YES));
        wrapper.eq("academic_year_semester_id" ,academicYearSemester.getId());
        List<Clazz> clazzes = clazzService.list(wrapper);
        Map<Long,List<Clazz>> gradeMap = clazzes.stream().collect(Collectors.groupingBy(Clazz::getGradeId));
        if(gradeMap.containsKey(1L)){
            map.put("gradeOneShould",gradeMap.get(1L).stream().mapToInt(clazz -> clazz.getStudentCount()).sum());
            List<Map<String,Object>> ob = dailyAttendanceService.searchStatistics(null,academicYearSemester.getId(),1L,null,null,null,startTime,endTime,null);
            map.put("gradeOneTimeLeave",ob);
        }
        if(gradeMap.containsKey(2L)){
            map.put("gradeTwoShould",gradeMap.get(2L).stream().mapToInt(clazz -> clazz.getStudentCount()).sum());
            List<Map<String,Object>> ob = dailyAttendanceService.searchStatistics(null,academicYearSemester.getId(),2L,null,null,null,startTime,endTime,null);
            map.put("gradeTwoTimeLeave",ob);
        }
        if(gradeMap.containsKey(3L)){
            map.put("gradeThreeShould",gradeMap.get(3L).stream().mapToInt(clazz -> clazz.getStudentCount()).sum());
            List<Map<String,Object>> ob = dailyAttendanceService.searchStatistics(null,academicYearSemester.getId(),3L,null,null,null,startTime,endTime,null);
            map.put("gradeThreeTimeLeave",ob);
        }
        TeacherDutyDto teacherDutyDto = teacherDutyService.getGradeTeacherDuty(new Date());
        map.put("totalDutyTeacher",teacherDutyDto.getTotalDutyTeacher());
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.CHINA);
        map.put("week",sdf.format(new Date()));
        return ApiResponse.ok(map);
    }
    /**
     *职员信息
     */
    @GetMapping("/student/attendance/V2")
    @ApiOperation("学生考勤2.0")
    public ApiResponse<Map<String,Object>> getStudentAttendanceV2(@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam("startTime") Date startTime,
                                                                @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam("endTime") Date endTime) {

        Map<String,Object> map = new HashMap<>();
        QueryWrapper<Clazz> wrapper = new QueryWrapper<>();
        AcademicYearSemester academicYearSemester = academicYearSemesterService.getOne(Wrappers.<AcademicYearSemester>lambdaQuery().eq(AcademicYearSemester::getIsDefault,YesNoEnum.YES));
        wrapper.eq("academic_year_semester_id" ,academicYearSemester.getId());
        List<Clazz> clazzes = clazzService.list(wrapper);
        Map<Long,List<Clazz>> gradeMap = clazzes.stream().collect(Collectors.groupingBy(Clazz::getGradeId));
        List<NightStudyAttendanceSub> nightStudyAttendanceSubs = this.nightStudyAttendanceSubService.list(Wrappers.<NightStudyAttendanceSub>lambdaQuery()
                .apply("to_days(register_date)" + "=" + "to_days({0})", endTime));
        if (nightStudyAttendanceSubs == null) nightStudyAttendanceSubs = new ArrayList<>();
        Map<Long, Map<StudentNightDutyTypeEnum, List<NightStudyAttendanceSub>>> map1 = nightStudyAttendanceSubs.stream()
                .collect(Collectors.groupingBy(item -> item.getClazz().getGradeId(), Collectors.groupingBy(NightStudyAttendanceSub::getStage)));
        if(gradeMap.containsKey(1L)){
            map.put("gradeOneShould",gradeMap.get(1L).stream().mapToInt(clazz -> clazz.getStudentCount()).sum());
            List<Map<String,Object>> dayAttendance = dailyAttendanceService.searchStatistics(null,academicYearSemester.getId(),1L,null,null,null,startTime,endTime,null);
            List<Map<String,Object>> nightAttendance = nightStudyAttendanceService.searchNightStatisticsGroupByTime(null,academicYearSemester.getId(),1L,null,null,null,startTime,endTime,null,null);
            List<Map<String,Object>> mergeAttendance = merge(dayAttendance,nightAttendance);
            map.put("gradeOneTimeLeave",mergeAttendance);
            Map<StudentNightDutyTypeEnum, List<NightStudyAttendanceSub>> maps = map1.getOrDefault(1L, new HashMap<>());
            Map<StudentNightDutyTypeEnum, Integer> actualMap = new HashMap<>();
            maps.forEach((k, v) -> actualMap.put(k, v.stream().mapToInt(NightStudyAttendanceSub::getActualNum).sum()));
            map.put("gradeOneActual", actualMap);
        }
        if(gradeMap.containsKey(2L)){
            map.put("gradeTwoShould",gradeMap.get(2L).stream().mapToInt(clazz -> clazz.getStudentCount()).sum());
            List<Map<String,Object>> dayAttendance = dailyAttendanceService.searchStatistics(null,academicYearSemester.getId(),2L,null,null,null,startTime,endTime,null);
            List<Map<String,Object>> nightAttendance = nightStudyAttendanceService.searchNightStatisticsGroupByTime(null,academicYearSemester.getId(),2L,null,null,null,startTime,endTime,null,null);
            List<Map<String,Object>> mergeAttendance = merge(dayAttendance,nightAttendance);
            map.put("gradeTwoTimeLeave",mergeAttendance);
            Map<StudentNightDutyTypeEnum, List<NightStudyAttendanceSub>> maps = map1.getOrDefault(2L, new HashMap<>());
            Map<StudentNightDutyTypeEnum, Integer> actualMap = new HashMap<>();
            maps.forEach((k, v) -> actualMap.put(k, v.stream().mapToInt(NightStudyAttendanceSub::getActualNum).sum()));
            map.put("gradeTwoActual", actualMap);
        }
        if(gradeMap.containsKey(3L)){
            map.put("gradeThreeShould",gradeMap.get(3L).stream().mapToInt(clazz -> clazz.getStudentCount()).sum());
            List<Map<String,Object>> dayAttendance = dailyAttendanceService.searchStatistics(null,academicYearSemester.getId(),3L,null,null,null,startTime,endTime,null);
            List<Map<String,Object>> nightAttendance = nightStudyAttendanceService.searchNightStatisticsGroupByTime(null,academicYearSemester.getId(),3L,null,null,null,startTime,endTime,null,null);
            List<Map<String,Object>> mergeAttendance = merge(dayAttendance,nightAttendance);
            map.put("gradeThreeTimeLeave",mergeAttendance);
            Map<StudentNightDutyTypeEnum, List<NightStudyAttendanceSub>> maps = map1.getOrDefault(3L, new HashMap<>());
            Map<StudentNightDutyTypeEnum, Integer> actualMap = new HashMap<>();
            maps.forEach((k, v) -> actualMap.put(k, v.stream().mapToInt(NightStudyAttendanceSub::getActualNum).sum()));
            map.put("gradeThreeActual", actualMap);
        }
        TeacherDutyDto teacherDutyDto = teacherDutyService.getGradeTeacherDuty(new Date());
        map.put("totalDutyTeacher",teacherDutyDto.getTotalDutyTeacher());
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.CHINA);
        map.put("week",sdf.format(new Date()));
        return ApiResponse.ok(map);
    }

    private List<Map<String,Object>> merge(List<Map<String,Object>> day,List<Map<String,Object>> night){
        if(CollectionUtils.isEmpty(day)){
            return night;
        }
        if(CollectionUtils.isEmpty(night)){
            return day;
        }
        for (Map<String,Object> map : day){
            boolean flag = false;
            for (Map<String,Object> nightMap : night){
                if(map.get("registerDate").toString().equals(nightMap.get("registerDate").toString())){
                    if(map.containsKey("leaveNum")){
                        nightMap.put("dayTotal",map.get("leaveNum"));
                    }
                    if(map.containsKey("illNum")){
                        nightMap.put("illNum",map.get("illNum"));
                    }
                    if(map.containsKey("thingsNum")){
                        nightMap.put("thingsNum",map.get("thingsNum"));
                    }
                    flag = true;
                    break;
                }
            }
            if(!flag){
                night.add(map);
            }
        }
        return night;
    }

    /**
     *职员信息
     */
    @GetMapping("/student/info")
    @ApiOperation("学生信息")
    public ApiResponse<Map<String,Object>> getStudentInfo(@RequestParam(required = false) StudentTypeEnum studentTypeEnum) {
        Map<String,Object> map = new HashMap<>();
        List<StudentVo> studentList = studentService.selectListWithClazz(studentTypeEnum,null);
        if(CollectionUtils.isNotEmpty(studentList)){
            map.put("totalStudent",studentList.size());
            List<StudentVo> gradeOne = studentList.stream().filter(item->item.getGradeId().equals(1L)).collect(Collectors.toList());
            List<StudentVo> gradeOneMale = gradeOne.stream().filter(item->item.getGender().equals(GenderEnum.M)).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(gradeOne)){
                if(CollectionUtils.isNotEmpty(gradeOneMale)){
                    map.put("gradeOneMale",gradeOneMale.size());
                    map.put("gradeOneFemale",gradeOne.size() - gradeOneMale.size());
                }else{
                    map.put("gradeOneMale",0);
                    map.put("gradeOneFemale",gradeOne.size());
                }
            }else{
                map.put("gradeOneMale",0);
                map.put("gradeOneFemale",0);
            }

            List<StudentVo> gradeTwo = studentList.stream().filter(item->item.getGradeId().equals(2L)).collect(Collectors.toList());
            List<StudentVo> gradeTwoMale = gradeTwo.stream().filter(item->item.getGender().equals(GenderEnum.M)).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(gradeTwo)){
                if(CollectionUtils.isNotEmpty(gradeTwoMale )){
                    map.put("gradeTwoMale",gradeTwoMale.size());
                    map.put("gradeTwoFemale",gradeTwo.size() - gradeTwoMale.size());
                }else{
                    map.put("gradeTwoMale",0);
                    map.put("gradeTwoFemale",gradeTwo.size());
                }
            }else{
                map.put("gradeTwoMale",0);
                map.put("gradeTwoFemale",0);
            }
            List<StudentVo> gradeThree = studentList.stream().filter(item->item.getGradeId().equals(3L) ).collect(Collectors.toList());
            List<StudentVo> gradeThreeMale = gradeThree.stream().filter(item->item.getGender().equals(GenderEnum.M)).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(gradeThree)){
                if(CollectionUtils.isNotEmpty(gradeThreeMale)){
                    map.put("gradeThreeMale",gradeThreeMale.size());
                    map.put("gradeThreeFemale",gradeThree.size() - gradeThreeMale.size());
                }else{
                    map.put("gradeThreeMale",0);
                    map.put("gradeThreeFemale",gradeThree.size());
                }
            }else{
                map.put("gradeThreeMale",0);
                map.put("gradeThreeFemale",0);
            }
        }
        return ApiResponse.ok(map);
    }

    /**
     *职员信息
     */
    @GetMapping("/grade/info")
    @ApiOperation("获取年级班级信息")
    public ApiResponse<Map<String,Object>> getGradeInfo() {
        Map<String,Object> map = new HashMap<>();
        List<ClazzVo> clazzVos = gradeService.getGradeList(new Date(),TeacherDutyTypeEnum.STAGE_ONE);
        map.put("gradeOne",clazzVos.stream().filter(clazzVo -> clazzVo.getGradeId().equals(1)).collect(Collectors.toList()));
        map.put("gradeTwo",clazzVos.stream().filter(clazzVo -> clazzVo.getGradeId().equals(2)).collect(Collectors.toList()));
        map.put("gradeThree",clazzVos.stream().filter(clazzVo -> clazzVo.getGradeId().equals(3)).collect(Collectors.toList()));
        return ApiResponse.ok(map);
    }

    /**
     *职员信息
     */
    @GetMapping("/detail/info")
    @ApiOperation("详细信息")
    public ApiResponse<ClazzVo> getClazzDetailInfo(@RequestParam Long clazzId,
                                                   @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam("time") Date time) {
        Clazz clazz = clazzService.getById(clazzId);
        ClazzVo clazzVo = new ClazzVo();
        BeanUtils.copyProperties(clazz,clazzVo);
        IPage<DailyAttendance> page =  new Page<>(1, 10000);
        String registerDate = DateUtils.format(time,"yyyy-MM-dd");
        IPage<DailyAttendance> page1 = dailyAttendanceService.searchDailyAttendance(page,null,null,null,clazzId,null,registerDate,registerDate,null);
        if(CollectionUtils.isNotEmpty(page1.getRecords())){
            List<DailyAttendance> dailyAttendances = page1.getRecords();
            List<DailyAttendance> dailyAttendances1 = new ArrayList<>();
            Map<Long,String> map = new HashMap<>();
            dailyAttendances.stream().forEach(dailyAttendance -> {
                if(!map.containsKey(dailyAttendance.getStudentId())){
                    dailyAttendances1.add(dailyAttendance);
                    map.put(dailyAttendance.getStudentId(),"1");
                }
            });
            clazzVo.setDailyAttendanceMap(dailyAttendances1.stream().collect(Collectors.groupingBy(DailyAttendance::getClassify)));
        }
        return ApiResponse.ok(clazzVo);
    }

    /**
     *常规信息警告
     */
    @GetMapping("/routine/info/warning")
    @ApiOperation("常规信息警告")
    public ApiResponse<List<CheckItemVo>> routineInfoWarning(@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam("time") Date time,
                                                             @RequestParam(required = false,defaultValue = "NO") YesNoEnum today) throws ParseException {
        return ApiResponse.ok(leaderDutyService.getRoutineInfoWarning(DateUtils.format(time,"yyyy-MM-dd"),YesNoEnum.NO,today));
    }

    /**
     *常规信息
     */
    @GetMapping("/routine/info")
    @ApiOperation("常规信息")
    public ApiResponse<List<CheckItemVo>> routineInfo(@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam("time") Date time) throws ParseException{
        List<CheckItemVo> routineInfoVo = leaderDutyService.getRoutineInfoWarning(DateUtils.format(time,"yyyy-MM-dd"),YesNoEnum.YES, YesNoEnum.NO);

        return ApiResponse.ok(routineInfoVo);
    }

    /**:
     *
     */
    @GetMapping("/ipad/statistics/grade")
    @ApiOperation("ipad 统计年级信息")
    public ApiResponse<Object> padStatisticsGrade(@RequestParam(value = "registerDateFrom", required = false) String registerDateFrom,
                                                  @RequestParam(value = "registerDateTo", required = false) String registerDateTo) {

        AcademicYearSemester academicYearSemester = academicYearSemesterService.getOne(Wrappers.<AcademicYearSemester>lambdaQuery().eq(AcademicYearSemester::getIsDefault,YesNoEnum.YES));
        List<GradeVo> gradeVos = clazzService.getGradeStatistics(new ArrayList<Long>(){{this.add(1L);this.add(2L);this.add(3L);}},
                academicYearSemester.getId(),null,null,registerDateFrom,registerDateTo);
        List<Label> day = new ArrayList<>();
        List<Label> night = new ArrayList<>();
        Label label = new Label();
        label.setName("应到");
        day.add(label);
        night.add(label);
        List<Label> labels = labelService.list(Wrappers.<Label>lambdaQuery().eq(Label::getClassify,"RCKQ"));
        day.addAll(labels);
        List<Label> nightLabels = labelService.list(Wrappers.<Label>lambdaQuery().eq(Label::getClassify,"WZXKQ"));
        night.addAll(nightLabels);

        Map<String,Object> map = new HashMap();
        map.put("day",day);
        map.put("night",night);
        map.put("GradeVoList",gradeVos);
        return ApiResponse.ok(map);
    }



    /**
     *
     */
    @GetMapping("/ipad/statistics/clazz")
    @ApiOperation("ipad 统计班级信息")
    public ApiResponse<Object> padStatisticsClazz(@RequestParam(value = "gradeId", required = false) Long gradeId,
                                             @RequestParam(value = "registerDateFrom", required = false) String registerDateFrom,
                                             @RequestParam(value = "registerDateTo", required = false) String registerDateTo) {

        AcademicYearSemester academicYearSemester = academicYearSemesterService.getOne(Wrappers.<AcademicYearSemester>lambdaQuery().eq(AcademicYearSemester::getIsDefault,YesNoEnum.YES));
        List<Label> day = new ArrayList<>();
        List<Label> night = new ArrayList<>();
        Label label = new Label();
        label.setName("应到");
        day.add(label);
        night.add(label);
        List<Label> labels = labelService.list(Wrappers.<Label>lambdaQuery().eq(Label::getClassify,"RCKQ"));
        day.addAll(labels);
        List<Label> nightLabels = labelService.list(Wrappers.<Label>lambdaQuery().eq(Label::getClassify,"WZXKQ"));
        night.addAll(nightLabels);
        Map<String,Object> map = new HashMap();
        map.put("day",day);
        map.put("night",night);
        List<ClazzVo> clazzVoList = clazzService.getClazzStatistics(null,new ArrayList<Long>(){{this.add(gradeId);}},academicYearSemester.getId(),null,null,registerDateFrom,registerDateTo);
        map.put("clazzVoList",clazzVoList);
        return ApiResponse.ok(map);
    }

    /**
     *
     */
    @GetMapping("/ill")
    @ApiOperation("新冠疫情")
    public ApiResponse<Object> padStatisticsClazz(@RequestParam(value = "registerDateFrom", required = false) String registerDateFrom,
                                                  @RequestParam(value = "registerDateTo", required = false) String registerDateTo) {

        AcademicYearSemester academicYearSemester = academicYearSemesterService.getOne(Wrappers.<AcademicYearSemester>lambdaQuery().eq(AcademicYearSemester::getIsDefault,YesNoEnum.YES));
        List<Label> labels = labelService.list(Wrappers.<Label>lambdaQuery().eq(Label::getClassify,"BZ"));
        Map<String,Object> resultMap = new HashMap();
        resultMap.put("label",labels);
        IPage<Ill> page = new Page<>(1, 99999);
        IPage<Ill> illPage = illService.searchIll(page,null,academicYearSemester.getId(),null,null,null,registerDateFrom,registerDateTo,null);
        if(CollectionUtils.isNotEmpty(illPage.getRecords())){
            List<Map<String,Object>> list = new ArrayList<>();
            Map<Long,List<Ill>> gradeIllMap = illPage.getRecords().stream().collect(Collectors.groupingBy(item->item.getClazz().getGradeId()));
            for (Long key : gradeIllMap.keySet()){
                Map<String,List<Ill>> illResultMap = new HashMap<>();

                Map<String,List<Ill>> illMap =  gradeIllMap.get(key).stream().collect(Collectors.groupingBy(Ill::getSymptom));

                for(String ill : illMap.keySet()){
                    String[] label = ill.split(",");
                    for (int i = 0; i < label.length; i++) {
                        if(illResultMap.containsKey(label[i].trim())){
                            List<Ill> ills = new ArrayList<>();
                            ills.addAll(illResultMap.get(label[i].trim()));
                            ills.addAll(illMap.get(ill));
                            illResultMap.put(label[i].trim(),ills);
                        }else{
                            illResultMap.put(label[i].trim(),illMap.get(ill));
                        }
                    }
                }
                Map<String,Object> gradeMap = new HashMap();
                gradeMap.putAll(illResultMap);
                gradeMap.put("grade", key);
                list.add(gradeMap);
            }
            resultMap.put("gradeList",list);
        }
        return ApiResponse.ok(resultMap);
    }
}
