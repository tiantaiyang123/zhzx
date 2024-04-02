/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：领导值班表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.*;
import com.zhzx.server.dto.CommentDto;
import com.zhzx.server.dto.DayRoutineDto;
import com.zhzx.server.dto.LeaderDutyDto;
import com.zhzx.server.dto.TeacherDutyDto;
import com.zhzx.server.enums.*;
import com.zhzx.server.repository.*;
import com.zhzx.server.repository.base.LeaderDutyBaseMapper;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.AcademicYearSemesterService;
import com.zhzx.server.service.LeaderDutyService;
import com.zhzx.server.service.TeacherDutyService;
import com.zhzx.server.util.CellUtils;
import com.zhzx.server.util.DateUtils;
import com.zhzx.server.util.StringUtils;
import com.zhzx.server.vo.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LeaderDutyServiceImpl extends ServiceImpl<LeaderDutyMapper, LeaderDuty> implements LeaderDutyService {
    @Value("${web.upload-path}")
    private String uploadPath;
    @Resource
    private StaffMapper staffMapper;
    @Resource
    private TeacherDutyService teacherDutyService;
    @Resource
    private FunctionDepartmentMapper functionDepartmentMapper;
    @Resource
    private LeaderDutySubstituteMapper leaderDutySubstituteMapper;
    @Resource
    private NightStudyDutyMapper nightStudyDutyMapper;
    @Resource
    private DailyAttendanceMapper dailyAttendanceMapper;

    @Override
    public int updateAllFieldsById(LeaderDuty entity) {
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
    public boolean saveBatch(Collection<LeaderDuty> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(LeaderDutyBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    @Override
    public DayRoutineDto dayRoutine(Date time, RoutineEnum type) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Staff staff = staffMapper.selectById(user.getStaffId());

        DayRoutineDto dayRoutineDto = this.baseMapper.dayRoutine(time, type.toString(), staff.getId(), LeaderDutyTypeEnum.ROUTINE.toString());
        if(dayRoutineDto == null)  return new DayRoutineDto();
        if(CollectionUtils.isNotEmpty(dayRoutineDto.getIncidentList())){
            dayRoutineDto.getIncidentList().addAll((List<Incident>) DayRoutineDto.parse(dayRoutineDto).get("incident"));
        }else{
            dayRoutineDto.setIncidentList((List<Incident>) DayRoutineDto.parse(dayRoutineDto).get("incident"));
        }
        if(CollectionUtils.isNotEmpty(dayRoutineDto.getComment())){
            dayRoutineDto.getComment().addAll((List<CommentDto>) DayRoutineDto.parse(dayRoutineDto).get("comment"));
        }else{
            dayRoutineDto.setComment((List<CommentDto>) DayRoutineDto.parse(dayRoutineDto).get("comment"));
        }
        TeacherDutyDto teacherDutyDto = teacherDutyService.getGradeTeacherDuty(time);

        dayRoutineDto.setTotalDutyTeacher(teacherDutyDto.getTotalDutyTeacher());
        dayRoutineDto.setGradeOneTeacher(teacherDutyDto.getGradeOneTeacher());
        dayRoutineDto.setGradeTwoTeacher(teacherDutyDto.getGradeTwoTeacher());
        dayRoutineDto.setGradeThreeTeacher(teacherDutyDto.getGradeThreeTeacher());
        Map<String, List<FunctionDepartment>> departmentMap = getDepartmentMap();
        dayRoutineDto.setDepartmentMap(departmentMap);
        dayRoutineDto.getComment().stream().forEach(commentDto -> {
            List<FunctionDepartment> office = commentDto.getDepartmentList().stream().filter(department -> department.getParentId() == 1).collect(Collectors.toList());
            List<FunctionDepartment> grade = commentDto.getDepartmentList().stream().filter(department -> department.getParentId() == 2).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(office)){
                office = new ArrayList<>();
                FunctionDepartment functionDepartment = new FunctionDepartment();
                functionDepartment.setName("教务处");
                office.add(functionDepartment);
            }else{
                FunctionDepartment functionDepartment = new FunctionDepartment();
                functionDepartment.setName("教务处");
                office.add(functionDepartment);
            }
            commentDto.setOfficeDepartmentList(office);
            commentDto.setGradeDepartmentList(grade);
        });
        return dayRoutineDto;
    }

    @Override
    public Page<LeaderDutyDto> getLeaderDutyForm(Integer pageNum, Integer pageSize, Date timeFrom, Date timeTo, String leaderDutyName, String phone, Long schoolyardId) {
        Page<LeaderDutyDto> page = new Page();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        //分页查询时间
        List<LeaderDutyDto> timeList = this.baseMapper.getLeaderDutyFormTime(page, timeFrom, timeTo, leaderDutyName, phone, schoolyardId);
        if (CollectionUtils.isEmpty(timeList)) return page;

        List<LeaderDutyDto> returnList = new ArrayList<>();

        List<Date> dateList = timeList.stream().map(leaderDutyDto -> leaderDutyDto.getTime()).collect(Collectors.toList());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateList.get(dateList.size() - 1));
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        List<LeaderDutyDto> leaderDutyDtoList = this.baseMapper.getLeaderDutyForm(dateList.get(0), calendar.getTime(), schoolyardId);
        Map<Date, List<LeaderDutyDto>> map = leaderDutyDtoList
                .stream()
                .filter(item -> timeList.stream().anyMatch(item1 -> item1.getTime().equals(item.getTime()) && item1.getSchoolyardId().equals(item.getSchoolyardId())))
                .collect(Collectors.groupingBy(LeaderDutyDto::getTime));
        map.keySet().stream().sorted().forEach(key -> {
            List<LeaderDutyDto> teacherList = map.get(key);
            Map<String, List<LeaderDutyDto>> mapSchoolyard = teacherList.stream().collect(Collectors.groupingBy(LeaderDutyDto::getSchoolyardName));
            mapSchoolyard.forEach((k, v) -> {
                LeaderDutyDto leaderDutyDto = new LeaderDutyDto();
                for (LeaderDutyDto leaderDutyDto1 : v) {
                    leaderDutyDto.setTime(key);
                    leaderDutyDto.setSchoolyardName(k);
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.CHINA);
                    leaderDutyDto.setWeek(sdf.format(key));
                    if (LeaderDutyTypeEnum.NIGHT_STUDY.equals(leaderDutyDto1.getDutyType())) {
                        leaderDutyDto.setNightLeaderName(leaderDutyDto1.getTeacherName());
                        leaderDutyDto.setPhone(leaderDutyDto1.getPhone());
                    } else if (LeaderDutyTypeEnum.ROUTINE.equals(leaderDutyDto1.getDutyType())) {
                        leaderDutyDto.setRoutineLeaderName(leaderDutyDto1.getTeacherName());
                        leaderDutyDto.setPhone(leaderDutyDto1.getPhone());
                    }
                }
                returnList.add(leaderDutyDto);
            });
        });
        page.setRecords(returnList);
        return page;
    }

    private Map<String, List<FunctionDepartment>> getDepartmentMap() {
        List<FunctionDepartment> functionDepartments = functionDepartmentMapper.selectList(Wrappers.<FunctionDepartment>lambdaQuery()
                .eq(FunctionDepartment::getParentId, 1).or().eq(FunctionDepartment::getParentId, 2)
                .orderByAsc(FunctionDepartment::getSortOrder)
        );
        List<FunctionDepartment> officeDepartment = functionDepartments.stream().filter(functionDepartment -> functionDepartment.getParentId().equals(1L)).collect(Collectors.toList());
        List<FunctionDepartment> gradeDepartment = functionDepartments.stream().filter(functionDepartment -> functionDepartment.getParentId().equals(2L)).collect(Collectors.toList());

        Map<String, List<FunctionDepartment>> departmentMap = new HashMap<>();
        departmentMap.put("office", officeDepartment);
        departmentMap.put("grade", gradeDepartment);
        return departmentMap;
    }

    @Resource
    private AcademicYearSemesterService academicYearSemesterService;
    @Resource
    private SouthGateMapper southGateMapper;
    @Resource
    private BreakfastMapper breakfastMapper;
    @Resource
    private MorningReadingMapper morningReadingMapper;
    @Resource
    private BreakActivityMapper breakActivityMapper;
    @Resource
    private LunchMapper lunchMapper;
    @Resource
    private TeachingAreaMapper teachingAreaMapper;
    @Resource
    private NoonSportAreaMapper noonSportAreaMapper;
    @Resource
    private DinnerMapper dinnerMapper;
    @Resource
    private GoOutMapper goOutMapper;
    @Resource
    private NightSportAreaMapper nightSportAreaMapper;
    @Resource
    private IncidentMapper incidentMapper;
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private GradeMapper gradeMapper;
    @Resource
    private ClazzMapper clazzMapper;
    @Resource
    private NightStudyDutyClazzMapper nightStudyDutyClazzMapper;

    @Override
    public RoutineInfoVo getRoutineInfo(Long staffId, String dutyDate) {
        if (StringUtils.isNullOrEmpty(dutyDate)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.add(calendar.DAY_OF_MONTH, -1);
            dutyDate = sdf.format(calendar.getTime());
        }
        RoutineInfoVo routineInfoVo = new RoutineInfoVo();
        QueryWrapper<LeaderDuty> queryWrapper = new QueryWrapper<LeaderDuty>();
        queryWrapper.eq("duty_type", "ROUTINE");
        //todo pad历史记录
        queryWrapper.eq("schoolyard_id", 1L);
        if (staffId != null)
            queryWrapper.eq("leader_id", staffId);
        queryWrapper.ge("start_time", java.sql.Timestamp.valueOf(dutyDate + " 00:00:00"));
        queryWrapper.le("start_time", java.sql.Timestamp.valueOf(dutyDate + " 23:59:59"));
        LeaderDuty leaderDuty = this.getOne(queryWrapper);
        if (leaderDuty == null) return null;
        routineInfoVo.setLeaderId(leaderDuty.getLeaderId());
        routineInfoVo.setLeader(leaderDuty.getLeader());
        routineInfoVo.setDutyDate(java.sql.Date.valueOf(dutyDate));
        routineInfoVo.setSchoolyard(leaderDuty.getSchoolyard());
        // 检查项目
        List<CheckItemVo> checkItemVoList = new ArrayList<>();
        // 早班
        // 南大门
        SouthGate southGate = this.southGateMapper.selectOne((new QueryWrapper<SouthGate>()).eq("leader_duty_id", leaderDuty.getId()));
        if (southGate != null) {
            for (int i = 0; i < 3; i++) {
                CheckItemVo checkItemVo = new CheckItemVo();
                checkItemVo.setShift("早班");
                checkItemVo.setStartTime(southGate.getStartTime());
                checkItemVo.setEndTime(southGate.getEndTime());
                checkItemVo.setCheckItem("南大门准备情况");
                if (i == 0) {
                    checkItemVo.setCheckSubItem("校医就位");
                    checkItemVo.setCheckResult(southGate.getDoctorInPlace().getName());
                }
                if (i == 1) {
                    checkItemVo.setCheckSubItem("保安就位");
                    checkItemVo.setCheckResult(southGate.getGuardInPlace().getName());
                }
                if (i == 2) {
                    checkItemVo.setCheckSubItem("远红外测温仪");
                    checkItemVo.setCheckResult(southGate.getThermometerInPlace().getName());
                }
                String urls = "";
                for (SouthGateImages image : southGate.getSouthGateImagesList()) {
                    if (i == 0 && image.getImageClassify().equals(SouthGateImageClassifyEnum.DOCTOR)) {
                        urls = urls + ',' + image.getUrl();
                    } else if (i == 1 && image.getImageClassify().equals(SouthGateImageClassifyEnum.GUARD)) {
                        urls = urls + ',' + image.getUrl();
                    } else if (i == 2 && image.getImageClassify().equals(SouthGateImageClassifyEnum.THERMOMETER)) {
                        urls = urls + ',' + image.getUrl();
                    }
                }
                if (!StringUtils.isNullOrEmpty(urls)) {
                    checkItemVo.setImageUrls(urls.substring(1));
                }
                checkItemVo.setUpdateTime(southGate.getUpdateTime());
                checkItemVoList.add(checkItemVo);
            }
        }
        // 早餐
        Breakfast breakfast = this.breakfastMapper.selectOne((new QueryWrapper<Breakfast>()).eq("leader_duty_id", leaderDuty.getId()));
        if (breakfast != null) {
            for (int i = 0; i < 2; i++) {
                CheckItemVo checkItemVo = new CheckItemVo();
                checkItemVo.setShift("早班");
                checkItemVo.setStartTime(breakfast.getStartTime());
                checkItemVo.setEndTime(breakfast.getEndTime());
                checkItemVo.setCheckItem("早餐情况");
                if (i == 0) {
                    checkItemVo.setCheckSubItem("是否正常");
                    checkItemVo.setCheckResult(breakfast.getDiningOrder().getName());
                }
                if (i == 1) {
                    checkItemVo.setCheckSubItem("是否陪餐");
                    checkItemVo.setCheckResult(breakfast.getIsAccompanyMeal().getName());
                }
                String urls = "";
                for (BreakfastImages image : breakfast.getBreakfastImagesList()) {
                    urls = urls + ',' + image.getUrl();
                }
                if (!StringUtils.isNullOrEmpty(urls)) {
                    checkItemVo.setImageUrls(urls.substring(1));
                }
                checkItemVo.setUpdateTime(breakfast.getUpdateTime());
                checkItemVoList.add(checkItemVo);
            }
        }
        // 早读
        MorningReading morningReading = this.morningReadingMapper.selectOne((new QueryWrapper<MorningReading>()).eq("leader_duty_id", leaderDuty.getId()));
        if (morningReading != null) {
            for (int i = 0; i < 3; i++) {
                CheckItemVo checkItemVo = new CheckItemVo();
                checkItemVo.setShift("早班");
                checkItemVo.setStartTime(morningReading.getStartTime());
                checkItemVo.setEndTime(morningReading.getEndTime());
                checkItemVo.setCheckItem("早读");
                if (i == 0) {
                    checkItemVo.setCheckSubItem("高一年级");
                    checkItemVo.setCheckResult(morningReading.getReadingOrder1().getName());
                }
                if (i == 1) {
                    checkItemVo.setCheckSubItem("高二年级");
                    checkItemVo.setCheckResult(morningReading.getReadingOrder2().getName());
                }
                if (i == 2) {
                    checkItemVo.setCheckSubItem("高三年级");
                    checkItemVo.setCheckResult(morningReading.getReadingOrder3().getName());
                }
                String urls = "";
                for (MorningReadingImages image : morningReading.getMorningReadingImagesList()) {
                    if (i == 0 && image.getImageClassify().equals(GradeEnum.ONE)) {
                        urls = urls + ',' + image.getUrl();
                    } else if (i == 1 && image.getImageClassify().equals(GradeEnum.TWO)) {
                        urls = urls + ',' + image.getUrl();
                    } else if (i == 2 && image.getImageClassify().equals(GradeEnum.THREE)) {
                        urls = urls + ',' + image.getUrl();
                    }
                }
                if (!StringUtils.isNullOrEmpty(urls)) {
                    checkItemVo.setImageUrls(urls.substring(1));
                }
                checkItemVo.setUpdateTime(morningReading.getUpdateTime());
                checkItemVoList.add(checkItemVo);
            }
        }
        // 大课间活动
        BreakActivity breakActivity = this.breakActivityMapper.selectOne((new QueryWrapper<BreakActivity>()).eq("leader_duty_id", leaderDuty.getId()));
        if (breakActivity != null) {
            if (breakActivity.getActivityOrder() == DayOrderEnum.NORMAL) {
                for (int i = 0; i < 3; i++) {
                    CheckItemVo checkItemVo = new CheckItemVo();
                    checkItemVo.setShift("早班");
                    checkItemVo.setStartTime(breakActivity.getStartTime());
                    checkItemVo.setEndTime(breakActivity.getEndTime());
                    checkItemVo.setCheckItem("大课间活动");
                    if (i == 0)
                        checkItemVo.setCheckSubItem("高一年级");
                    else if (i == 1)
                        checkItemVo.setCheckSubItem("高二年级");
                    else if (i == 2)
                        checkItemVo.setCheckSubItem("高三年级");
                    checkItemVo.setCheckResult(breakActivity.getActivityOrder().getName());
                    checkItemVo.setUpdateTime(breakActivity.getUpdateTime());
                    checkItemVoList.add(checkItemVo);
                }
            } else {
                for (int i = 0; i < 3; i++) {
                    CheckItemVo checkItemVo = new CheckItemVo();
                    checkItemVo.setShift("早班");
                    checkItemVo.setStartTime(breakActivity.getStartTime());
                    checkItemVo.setEndTime(breakActivity.getEndTime());
                    checkItemVo.setCheckItem("大课间活动");
                    String gradeName = "";
                    if (i == 0) {
                        gradeName = "高一年级";
                    } else if (i == 1) {
                        gradeName = "高二年级";
                    } else if (i == 2) {
                        gradeName = "高三年级";
                    }
                    checkItemVo.setCheckSubItem(gradeName);
                    if (breakActivity.getActivityGrade().indexOf(gradeName) >= 0) {
                        checkItemVo.setCheckResult(breakActivity.getActivityOrder().getName());
                    } else {
                        checkItemVo.setCheckResult(DayOrderEnum.NORMAL.getName());
                    }
                    String urls = "";
                    for (BreakActivityImages image : breakActivity.getBreakActivityImagesList()) {
                        urls = urls + ',' + image.getUrl();
                    }
                    if (!StringUtils.isNullOrEmpty(urls)) {
                        checkItemVo.setImageUrls(urls.substring(1));
                    }
                    checkItemVo.setUpdateTime(breakActivity.getUpdateTime());
                    checkItemVoList.add(checkItemVo);
                }
            }
        }
        // 午班
        // 午餐
        Lunch lunch = this.lunchMapper.selectOne((new QueryWrapper<Lunch>()).eq("leader_duty_id", leaderDuty.getId()));
        if (lunch != null) {
            for (int i = 0; i < 2; i++) {
                CheckItemVo checkItemVo = new CheckItemVo();
                checkItemVo.setShift("午班");
                checkItemVo.setStartTime(lunch.getStartTime());
                checkItemVo.setEndTime(lunch.getEndTime());
                checkItemVo.setCheckItem("午餐情况");
                if (i == 0) {
                    checkItemVo.setCheckSubItem("是否正常");
                    checkItemVo.setCheckResult(lunch.getDiningOrder().getName());
                }
                if (i == 1) {
                    checkItemVo.setCheckSubItem("是否陪餐");
                    checkItemVo.setCheckResult(lunch.getIsAccompanyMeal().getName());
                }
                String urls = "";
                for (LunchImages image : lunch.getLunchImagesList()) {
                    urls = urls + ',' + image.getUrl();
                }
                if (!StringUtils.isNullOrEmpty(urls)) {
                    checkItemVo.setImageUrls(urls.substring(1));
                }
                checkItemVo.setUpdateTime(lunch.getUpdateTime());
                checkItemVoList.add(checkItemVo);
            }
        }
        // 教学区秩序
        TeachingArea teachingArea = this.teachingAreaMapper.selectOne((new QueryWrapper<TeachingArea>()).eq("leader_duty_id", leaderDuty.getId()));
        if (teachingArea != null) {
            for (int i = 0; i < 3; i++) {
                CheckItemVo checkItemVo = new CheckItemVo();
                checkItemVo.setShift("午班");
                checkItemVo.setStartTime(teachingArea.getStartTime());
                checkItemVo.setEndTime(teachingArea.getEndTime());
                checkItemVo.setCheckItem("教学区秩序");
                if (i == 0) {
                    checkItemVo.setCheckSubItem("高一年级");
                    checkItemVo.setCheckResult(teachingArea.getTeachingAreaOrder1().getName());
                }
                if (i == 1) {
                    checkItemVo.setCheckSubItem("高二年级");
                    checkItemVo.setCheckResult(teachingArea.getTeachingAreaOrder2().getName());
                }
                if (i == 2) {
                    checkItemVo.setCheckSubItem("高三年级");
                    checkItemVo.setCheckResult(teachingArea.getTeachingAreaOrder3().getName());
                }
                String urls = "";
                for (TeachingAreaImages image : teachingArea.getTeachingAreaImagesList()) {
                    if (i == 0 && image.getImageClassify().equals(GradeEnum.ONE)) {
                        urls = urls + ',' + image.getUrl();
                    } else if (i == 1 && image.getImageClassify().equals(GradeEnum.TWO)) {
                        urls = urls + ',' + image.getUrl();
                    } else if (i == 2 && image.getImageClassify().equals(GradeEnum.THREE)) {
                        urls = urls + ',' + image.getUrl();
                    }
                }
                if (!StringUtils.isNullOrEmpty(urls)) {
                    checkItemVo.setImageUrls(urls.substring(1));
                }
                checkItemVo.setUpdateTime(teachingArea.getUpdateTime());
                checkItemVoList.add(checkItemVo);
            }
        }
        // 运动区秩序
        NoonSportArea noonSportArea = this.noonSportAreaMapper.selectOne((new QueryWrapper<NoonSportArea>()).eq("leader_duty_id", leaderDuty.getId()));
        if (noonSportArea != null) {
            CheckItemVo checkItemVo = new CheckItemVo();
            checkItemVo.setShift("午班");
            checkItemVo.setStartTime(noonSportArea.getStartTime());
            checkItemVo.setEndTime(noonSportArea.getEndTime());
            checkItemVo.setCheckItem("运动区秩序");
            checkItemVo.setCheckSubItem("是否正常");
            checkItemVo.setCheckResult(noonSportArea.getSportAreaOrder().getName());
            String urls = "";
            for (NoonSportAreaImages image : noonSportArea.getNoonSportAreaImagesList()) {
                urls = urls + ',' + image.getUrl();
            }
            if (!StringUtils.isNullOrEmpty(urls)) {
                checkItemVo.setImageUrls(urls.substring(1));
            }
            checkItemVo.setUpdateTime(noonSportArea.getUpdateTime());
            checkItemVoList.add(checkItemVo);
        }
        // 晚班
        // 晚餐
        Dinner dinner = this.dinnerMapper.selectOne((new QueryWrapper<Dinner>()).eq("leader_duty_id", leaderDuty.getId()));
        if (dinner != null) {
            for (int i = 0; i < 2; i++) {
                CheckItemVo checkItemVo = new CheckItemVo();
                checkItemVo.setShift("晚班");
                checkItemVo.setStartTime(dinner.getStartTime());
                checkItemVo.setEndTime(dinner.getEndTime());
                checkItemVo.setCheckItem("晚餐情况");
                if (i == 0) {
                    checkItemVo.setCheckSubItem("是否正常");
                    checkItemVo.setCheckResult(dinner.getDiningOrder().getName());
                }
                if (i == 1) {
                    checkItemVo.setCheckSubItem("是否陪餐");
                    checkItemVo.setCheckResult(dinner.getIsAccompanyMeal().getName());
                }
                String urls = "";
                for (DinnerImages image : dinner.getDinnerImagesList()) {
                    urls = urls + ',' + image.getUrl();
                }
                if (!StringUtils.isNullOrEmpty(urls)) {
                    checkItemVo.setImageUrls(urls.substring(1));
                }
                checkItemVo.setUpdateTime(dinner.getUpdateTime());
                checkItemVoList.add(checkItemVo);
            }
        }
        // 走读生提前出门
        GoOut goOut = this.goOutMapper.selectOne((new QueryWrapper<GoOut>()).eq("leader_duty_id", leaderDuty.getId()));
        if (goOut != null) {
            CheckItemVo checkItemVo = new CheckItemVo();
            checkItemVo.setShift("晚班");
            checkItemVo.setStartTime(goOut.getStartTime());
            checkItemVo.setEndTime(goOut.getEndTime());
            checkItemVo.setCheckItem("走读生提前出门情况");
            checkItemVo.setCheckSubItem("是否正常");
            checkItemVo.setCheckResult(goOut.getGoOutOrder().getName());
            String urls = "";
            for (GoOutImages image : goOut.getGoOutImagesList()) {
                urls = urls + ',' + image.getUrl();
            }
            if (!StringUtils.isNullOrEmpty(urls)) {
                checkItemVo.setImageUrls(urls.substring(1));
            }
            checkItemVo.setUpdateTime(goOut.getUpdateTime());
            checkItemVoList.add(checkItemVo);
        }
        // 运动区秩序
        NightSportArea nightSportArea = this.nightSportAreaMapper.selectOne((new QueryWrapper<NightSportArea>()).eq("leader_duty_id", leaderDuty.getId()));
        if (nightSportArea != null) {
            CheckItemVo checkItemVo = new CheckItemVo();
            checkItemVo.setShift("晚班");
            checkItemVo.setStartTime(nightSportArea.getStartTime());
            checkItemVo.setEndTime(nightSportArea.getEndTime());
            checkItemVo.setCheckItem("运动区秩序");
            checkItemVo.setCheckSubItem("是否正常");
            checkItemVo.setCheckResult(nightSportArea.getSportAreaOrder().getName());
            String urls = "";
            for (NightSportAreaImages image : nightSportArea.getNightSportAreaImagesList()) {
                urls = urls + ',' + image.getUrl();
            }
            if (!StringUtils.isNullOrEmpty(urls)) {
                checkItemVo.setImageUrls(urls.substring(1));
            }
            checkItemVo.setUpdateTime(nightSportArea.getUpdateTime());
            checkItemVoList.add(checkItemVo);
        }
        // 偶发事件
        List<Incident> incidentList = this.incidentMapper.selectList((new QueryWrapper<Incident>()).eq("daily_routine_id",southGate!=null ?  southGate.getId(): -1).eq("classify", ClassifyEnum.DAY_SOUTH_GATE.toString()));
        incidentList.addAll(this.incidentMapper.selectList((new QueryWrapper<Incident>()).eq("daily_routine_id", breakfast != null ? breakfast.getId(): -1).eq("classify", ClassifyEnum.DAY_BREAKFAST.toString())));
        incidentList.addAll(this.incidentMapper.selectList((new QueryWrapper<Incident>()).eq("daily_routine_id", morningReading != null ? morningReading.getId(): -1).eq("classify", ClassifyEnum.DAY_MORNING_READING_1.toString())));
        incidentList.addAll(this.incidentMapper.selectList((new QueryWrapper<Incident>()).eq("daily_routine_id", morningReading != null ? morningReading.getId(): -1).eq("classify", ClassifyEnum.DAY_MORNING_READING_2.toString())));
        incidentList.addAll(this.incidentMapper.selectList((new QueryWrapper<Incident>()).eq("daily_routine_id", morningReading != null ? morningReading.getId(): -1).eq("classify", ClassifyEnum.DAY_MORNING_READING_3.toString())));
        incidentList.addAll(this.incidentMapper.selectList((new QueryWrapper<Incident>()).eq("daily_routine_id", breakActivity!= null ?breakActivity.getId():-1).eq("classify", ClassifyEnum.DAY_BREAK_ACTIVITY.toString())));
        incidentList.addAll(this.incidentMapper.selectList((new QueryWrapper<Incident>()).eq("daily_routine_id", lunch!=null?lunch.getId():-1).eq("classify", ClassifyEnum.DAY_LUNCH.toString())));
        incidentList.addAll(this.incidentMapper.selectList((new QueryWrapper<Incident>()).eq("daily_routine_id", teachingArea!=null?teachingArea.getId():-1).eq("classify", ClassifyEnum.DAY_TEACHING_AREA_1.toString())));
        incidentList.addAll(this.incidentMapper.selectList((new QueryWrapper<Incident>()).eq("daily_routine_id", teachingArea!=null?teachingArea.getId():-1).eq("classify", ClassifyEnum.DAY_TEACHING_AREA_2.toString())));
        incidentList.addAll(this.incidentMapper.selectList((new QueryWrapper<Incident>()).eq("daily_routine_id", teachingArea!=null?teachingArea.getId():-1).eq("classify", ClassifyEnum.DAY_TEACHING_AREA_3.toString())));
        incidentList.addAll(this.incidentMapper.selectList((new QueryWrapper<Incident>()).eq("daily_routine_id", noonSportArea!=null?noonSportArea.getId():-1).eq("classify", ClassifyEnum.DAY_NOON_SPORT_AREA.toString())));
        incidentList.addAll(this.incidentMapper.selectList((new QueryWrapper<Incident>()).eq("daily_routine_id", dinner!=null?dinner.getId():-1).eq("classify", ClassifyEnum.DAY_DINNER.toString())));
        incidentList.addAll(this.incidentMapper.selectList((new QueryWrapper<Incident>()).eq("daily_routine_id", goOut!=null?goOut.getId():-1).eq("classify", ClassifyEnum.DAY_GO_OUT.toString())));
        incidentList.addAll(this.incidentMapper.selectList((new QueryWrapper<Incident>()).eq("daily_routine_id", nightSportArea!=null?nightSportArea.getId():-1).eq("classify", ClassifyEnum.DAY_NIGHT_SPORT_AREA.toString())));
        incidentList.addAll(this.incidentMapper.selectList((new QueryWrapper<Incident>()).eq("daily_routine_id", leaderDuty!=null?leaderDuty.getId():-1).eq("classify", ClassifyEnum.DAY_OTHER_1.toString())));
        List<CheckItemVo> incidentVoList = incidentList.stream().map(data -> {
            CheckItemVo checkItemVo = new CheckItemVo();
            checkItemVo.setShift("偶发事件");
            if (data.getClassify() == ClassifyEnum.DAY_SOUTH_GATE)
                checkItemVo.setCheckItem("南大门准备情况");
            else if (data.getClassify() == ClassifyEnum.DAY_BREAKFAST)
                checkItemVo.setCheckItem("早餐情况");
            else if (data.getClassify() == ClassifyEnum.DAY_MORNING_READING_1)
                checkItemVo.setCheckItem("早读情况");
            else if (data.getClassify() == ClassifyEnum.DAY_MORNING_READING_2)
                checkItemVo.setCheckItem("早读情况");
            else if (data.getClassify() == ClassifyEnum.DAY_MORNING_READING_3)
                checkItemVo.setCheckItem("早读情况");
            else if (data.getClassify() == ClassifyEnum.DAY_BREAK_ACTIVITY)
                checkItemVo.setCheckItem("大课间活动");
            else if (data.getClassify() == ClassifyEnum.DAY_LUNCH)
                checkItemVo.setCheckItem("午餐情况");
            else if (data.getClassify() == ClassifyEnum.DAY_TEACHING_AREA_1)
                checkItemVo.setCheckItem("教学区秩序");
            else if (data.getClassify() == ClassifyEnum.DAY_TEACHING_AREA_2)
                checkItemVo.setCheckItem("教学区秩序");
            else if (data.getClassify() == ClassifyEnum.DAY_TEACHING_AREA_3)
                checkItemVo.setCheckItem("教学区秩序");
            else if (data.getClassify() == ClassifyEnum.DAY_NOON_SPORT_AREA)
                checkItemVo.setCheckItem("运动区秩序");
            else if (data.getClassify() == ClassifyEnum.DAY_DINNER)
                checkItemVo.setCheckItem("晚餐情况");
            else if (data.getClassify() == ClassifyEnum.DAY_GO_OUT)
                checkItemVo.setCheckItem("走读生提前出门情况");
            else if (data.getClassify() == ClassifyEnum.DAY_NIGHT_SPORT_AREA)
                checkItemVo.setCheckItem("运动区秩序");
            else if (data.getClassify() == ClassifyEnum.DAY_OTHER_1)
                checkItemVo.setCheckItem("其他");
            checkItemVo.setCheckResult(data.getContent());
            checkItemVo.setUpdateTime(data.getUpdateTime());
            return checkItemVo;
        }).collect(Collectors.toList());
        if (incidentVoList.size() != 0) {
            checkItemVoList.addAll(incidentVoList);
        }
        // 意见与建议
        List<Comment> commentList = this.commentMapper.selectList((new QueryWrapper<Comment>()).eq("daily_routine_id", southGate!=null?southGate.getId():-1).eq("classify", ClassifyEnum.DAY_SOUTH_GATE.toString()));
        commentList.addAll(this.commentMapper.selectList((new QueryWrapper<Comment>()).eq("daily_routine_id", breakfast!=null?breakfast.getId():-1).eq("classify", ClassifyEnum.DAY_BREAKFAST.toString())));
        commentList.addAll(this.commentMapper.selectList((new QueryWrapper<Comment>()).eq("daily_routine_id", morningReading!=null?morningReading.getId():-1).eq("classify", ClassifyEnum.DAY_MORNING_READING_1.toString())));
        commentList.addAll(this.commentMapper.selectList((new QueryWrapper<Comment>()).eq("daily_routine_id", morningReading!=null?morningReading.getId():-1).eq("classify", ClassifyEnum.DAY_MORNING_READING_2.toString())));
        commentList.addAll(this.commentMapper.selectList((new QueryWrapper<Comment>()).eq("daily_routine_id", morningReading!=null?morningReading.getId():-1).eq("classify", ClassifyEnum.DAY_MORNING_READING_3.toString())));
        commentList.addAll(this.commentMapper.selectList((new QueryWrapper<Comment>()).eq("daily_routine_id", breakActivity!=null?breakActivity.getId():-1).eq("classify", ClassifyEnum.DAY_BREAK_ACTIVITY.toString())));
        commentList.addAll(this.commentMapper.selectList((new QueryWrapper<Comment>()).eq("daily_routine_id", lunch!=null?lunch.getId():-1).eq("classify", ClassifyEnum.DAY_LUNCH.toString())));
        commentList.addAll(this.commentMapper.selectList((new QueryWrapper<Comment>()).eq("daily_routine_id", teachingArea!=null?teachingArea.getId():-1).eq("classify", ClassifyEnum.DAY_TEACHING_AREA_1.toString())));
        commentList.addAll(this.commentMapper.selectList((new QueryWrapper<Comment>()).eq("daily_routine_id", teachingArea!=null?teachingArea.getId():-1).eq("classify", ClassifyEnum.DAY_TEACHING_AREA_2.toString())));
        commentList.addAll(this.commentMapper.selectList((new QueryWrapper<Comment>()).eq("daily_routine_id", teachingArea!=null?teachingArea.getId():-1).eq("classify", ClassifyEnum.DAY_TEACHING_AREA_3.toString())));
        commentList.addAll(this.commentMapper.selectList((new QueryWrapper<Comment>()).eq("daily_routine_id", noonSportArea!=null?noonSportArea.getId():-1).eq("classify", ClassifyEnum.DAY_NOON_SPORT_AREA.toString())));
        commentList.addAll(this.commentMapper.selectList((new QueryWrapper<Comment>()).eq("daily_routine_id", dinner!=null?dinner.getId():-1).eq("classify", ClassifyEnum.DAY_DINNER.toString())));
        commentList.addAll(this.commentMapper.selectList((new QueryWrapper<Comment>()).eq("daily_routine_id", goOut!=null?goOut.getId():-1).eq("classify", ClassifyEnum.DAY_GO_OUT.toString())));
        commentList.addAll(this.commentMapper.selectList((new QueryWrapper<Comment>()).eq("daily_routine_id", nightSportArea!=null?nightSportArea.getId():-1).eq("classify", ClassifyEnum.DAY_NIGHT_SPORT_AREA.toString())));
        commentList.addAll(this.commentMapper.selectList((new QueryWrapper<Comment>()).eq("daily_routine_id", leaderDuty!=null?leaderDuty.getId():-1).eq("classify", ClassifyEnum.DAY_OTHER_1.toString())));
        List<CheckItemVo> commentVoList = commentList.stream().map(data -> {
            CheckItemVo checkItemVo = new CheckItemVo();
            checkItemVo.setShift("意见与建议");
            if (data.getClassify() == ClassifyEnum.DAY_SOUTH_GATE)
                checkItemVo.setCheckItem("南大门准备情况");
            else if (data.getClassify() == ClassifyEnum.DAY_BREAKFAST)
                checkItemVo.setCheckItem("早餐情况");
            else if (data.getClassify() == ClassifyEnum.DAY_MORNING_READING_1)
                checkItemVo.setCheckItem("早读情况");
            else if (data.getClassify() == ClassifyEnum.DAY_MORNING_READING_2)
                checkItemVo.setCheckItem("早读情况");
            else if (data.getClassify() == ClassifyEnum.DAY_MORNING_READING_3)
                checkItemVo.setCheckItem("早读情况");
            else if (data.getClassify() == ClassifyEnum.DAY_BREAK_ACTIVITY)
                checkItemVo.setCheckItem("大课间活动");
            else if (data.getClassify() == ClassifyEnum.DAY_LUNCH)
                checkItemVo.setCheckItem("午餐情况");
            else if (data.getClassify() == ClassifyEnum.DAY_TEACHING_AREA_1)
                checkItemVo.setCheckItem("教学区秩序");
            else if (data.getClassify() == ClassifyEnum.DAY_TEACHING_AREA_2)
                checkItemVo.setCheckItem("教学区秩序");
            else if (data.getClassify() == ClassifyEnum.DAY_TEACHING_AREA_3)
                checkItemVo.setCheckItem("教学区秩序");
            else if (data.getClassify() == ClassifyEnum.DAY_NOON_SPORT_AREA)
                checkItemVo.setCheckItem("运动区秩序");
            else if (data.getClassify() == ClassifyEnum.DAY_DINNER)
                checkItemVo.setCheckItem("晚餐情况");
            else if (data.getClassify() == ClassifyEnum.DAY_GO_OUT)
                checkItemVo.setCheckItem("走读生提前出门情况");
            else if (data.getClassify() == ClassifyEnum.DAY_NIGHT_SPORT_AREA)
                checkItemVo.setCheckItem("运动区秩序");
            else if (data.getClassify() == ClassifyEnum.DAY_OTHER_1)
                checkItemVo.setCheckItem("其他");
            checkItemVo.setCheckResult(data.getContent());
            checkItemVo.setUpdateTime(data.getUpdateTime());
            return checkItemVo;
        }).collect(Collectors.toList());
        if (commentVoList.size() != 0) {
            checkItemVoList.addAll(commentVoList);
        }
        for (int i = 1; i <= checkItemVoList.size(); i++) {
            checkItemVoList.get(i - 1).setId(i);
        }
        if (checkItemVoList.size() != 0) {
            routineInfoVo.setCheckItemList(checkItemVoList);
        }
        return routineInfoVo;
    }

    @Override
    public List<CheckItemVo> getRoutineInfoWarning(String dutyDate,YesNoEnum warning,YesNoEnum today) throws ParseException{
        if (StringUtils.isNullOrEmpty(dutyDate)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.add(calendar.DAY_OF_MONTH, -1);
            dutyDate = sdf.format(calendar.getTime());
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse(dutyDate));
        if(YesNoEnum.NO.equals(today)){
            calendar.add(calendar.DAY_OF_MONTH, -1);
        }
        String startTime = sdf.format(calendar.getTime());
        QueryWrapper<LeaderDuty> queryWrapper = new QueryWrapper<LeaderDuty>();
        queryWrapper.eq("duty_type", "ROUTINE");
        queryWrapper.ge("start_time", java.sql.Timestamp.valueOf(startTime + " 00:00:00"));
        queryWrapper.le("start_time", java.sql.Timestamp.valueOf(dutyDate + " 23:59:59"));
        List<LeaderDuty> leaderDutyList = this.baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(leaderDutyList)) return null;

        List<CheckItemVo> checkItemVoList = new ArrayList<>();

        for (LeaderDuty leaderDuty : leaderDutyList){
            if(leaderDuty.getLeader() == null){
                leaderDuty.setLeader(new Staff());
            }
            // 检查项目

            // 早班
            // 南大门
            SouthGate southGate = this.southGateMapper.selectOne((new QueryWrapper<SouthGate>()).eq("leader_duty_id", leaderDuty.getId()));
            if (southGate != null ) {
                for (int i = 0; i < 3; i++) {
                    CheckItemVo checkItemVo = new CheckItemVo();
                    checkItemVo.setShift("早班");
                    checkItemVo.setStartTime(southGate.getStartTime());
                    checkItemVo.setEndTime(southGate.getEndTime());
                    checkItemVo.setCheckItem("南大门准备情况");
                    checkItemVo.setLeaderName(leaderDuty.getLeader().getName());
                    String urls = "";
                    for (SouthGateImages image : southGate.getSouthGateImagesList()) {
                        if (i == 0 && image.getImageClassify().equals(SouthGateImageClassifyEnum.DOCTOR)) {
                            urls = urls + ',' + image.getUrl();
                        } else if (i == 1 && image.getImageClassify().equals(SouthGateImageClassifyEnum.GUARD)) {
                            urls = urls + ',' + image.getUrl();
                        } else if (i == 2 && image.getImageClassify().equals(SouthGateImageClassifyEnum.THERMOMETER)) {
                            urls = urls + ',' + image.getUrl();
                        }
                    }
                    if (!StringUtils.isNullOrEmpty(urls)) {
                        checkItemVo.setImageUrls(urls.substring(1));
                    }
                    checkItemVo.setUpdateTime(southGate.getUpdateTime());
                    if (i == 0) {
                        if(YesNoEnum.NO.equals(warning) && YesNoEnum.NO.equals(southGate.getDoctorInPlace())){
                            checkItemVo.setCheckSubItem("校医就位");
                            checkItemVo.setCheckResult(southGate.getDoctorInPlace().getName());
                            checkItemVo.setComment("校医未就位");
                            checkItemVoList.add(checkItemVo);
                        }else if(YesNoEnum.YES.equals(warning)){
                            if(YesNoEnum.NO.equals(southGate.getDoctorInPlace())){
                                checkItemVo.setCheckSubItem("校医就位");
                                checkItemVo.setCheckResult(southGate.getDoctorInPlace().getName());
                                checkItemVo.setComment("校医未就位");
                                checkItemVoList.add(checkItemVo);
                            }else if(YesNoEnum.YES.equals(southGate.getDoctorInPlace())){
                                checkItemVo.setCheckSubItem("校医就位");
                                checkItemVo.setCheckResult(southGate.getDoctorInPlace().getName());
                                checkItemVo.setComment("校医已就位");
                                checkItemVoList.add(checkItemVo);
                            }
                        }
                    }
                    if (i == 1) {
                        if(YesNoEnum.NO.equals(warning) && YesNoEnum.NO.equals(southGate.getGuardInPlace())){
                            checkItemVo.setCheckSubItem("保安就位");
                            checkItemVo.setCheckResult(southGate.getGuardInPlace().getName());
                            checkItemVo.setComment(southGate.getGuardDescription());
                            checkItemVoList.add(checkItemVo);
                        }else if(YesNoEnum.YES.equals(warning)){
                            if(YesNoEnum.NO.equals(southGate.getGuardInPlace())){
                                checkItemVo.setCheckSubItem("保安就位");
                                checkItemVo.setCheckResult(southGate.getGuardInPlace().getName());
                                checkItemVo.setComment(southGate.getGuardDescription());
                                checkItemVoList.add(checkItemVo);
                            }else if(YesNoEnum.YES.equals(southGate.getGuardInPlace())){
                                checkItemVo.setCheckSubItem("保安就位");
                                checkItemVo.setCheckResult(southGate.getGuardInPlace().getName());
                                checkItemVo.setComment("保安已就位");
                                checkItemVoList.add(checkItemVo);
                            }
                        }
                    }
                    if (i == 2 && YesNoEnum.NO.equals(southGate.getThermometerInPlace())) {
                        if(YesNoEnum.NO.equals(warning) && YesNoEnum.NO.equals(southGate.getThermometerInPlace())){
                            checkItemVo.setCheckSubItem("远红外测温仪");
                            checkItemVo.setCheckResult(southGate.getThermometerInPlace().getName());
                            checkItemVo.setComment("远红外测温仪未就位");
                            checkItemVoList.add(checkItemVo);
                        }else if(YesNoEnum.YES.equals(warning)){
                            if(YesNoEnum.NO.equals(southGate.getThermometerInPlace())){
                                checkItemVo.setCheckSubItem("远红外测温仪");
                                checkItemVo.setCheckResult(southGate.getThermometerInPlace().getName());
                                checkItemVo.setComment("远红外测温仪未就位");
                                checkItemVoList.add(checkItemVo);
                            }else if(YesNoEnum.YES.equals(southGate.getThermometerInPlace())){
                                checkItemVo.setCheckSubItem("远红外测温仪");
                                checkItemVo.setCheckResult(southGate.getThermometerInPlace().getName());
                                checkItemVo.setComment("远红外测温仪已就位");
                                checkItemVoList.add(checkItemVo);
                            }
                        }
                    }
                }
            }
            // 早餐
            Breakfast breakfast = this.breakfastMapper.selectOne((new QueryWrapper<Breakfast>()).eq("leader_duty_id", leaderDuty.getId()));
            if (breakfast != null) {
                if(YesNoEnum.YES.equals(warning)){
                    CheckItemVo checkItemVo = new CheckItemVo();
                    checkItemVo.setShift("早班");
                    checkItemVo.setStartTime(breakfast.getStartTime());
                    checkItemVo.setEndTime(breakfast.getEndTime());
                    checkItemVo.setCheckItem("早餐情况");
                    checkItemVo.setLeaderName(leaderDuty.getLeader().getName());
                    String urls = "";
                    for (BreakfastImages image : breakfast.getBreakfastImagesList()) {
                        urls = urls + ',' + image.getUrl();
                    }
                    if (!StringUtils.isNullOrEmpty(urls)) {
                        checkItemVo.setImageUrls(urls.substring(1));
                    }
                    checkItemVo.setUpdateTime(breakfast.getUpdateTime());
                    checkItemVo.setCheckSubItem("是否正常");
                    checkItemVo.setCheckResult(breakfast.getDiningOrder().getName());
                    checkItemVo.setCheckSubItem("是否陪餐");
                    checkItemVo.setCheckResult(breakfast.getIsAccompanyMeal().getName());
                    checkItemVoList.add(checkItemVo);
                }else{
                    if(DayOrderEnum.IMPROVE.equals(breakfast.getDiningOrder())){
                        CheckItemVo checkItemVo = new CheckItemVo();
                        checkItemVo.setShift("早班");
                        checkItemVo.setStartTime(breakfast.getStartTime());
                        checkItemVo.setEndTime(breakfast.getEndTime());
                        checkItemVo.setCheckItem("早餐情况");
                        checkItemVo.setLeaderName(leaderDuty.getLeader().getName());
                        String urls = "";
                        for (BreakfastImages image : breakfast.getBreakfastImagesList()) {
                            urls = urls + ',' + image.getUrl();
                        }
                        if (!StringUtils.isNullOrEmpty(urls)) {
                            checkItemVo.setImageUrls(urls.substring(1));
                        }
                        checkItemVo.setUpdateTime(breakfast.getUpdateTime());
                        checkItemVo.setCheckSubItem("是否正常");
                        checkItemVo.setCheckResult(breakfast.getDiningOrder().getName());
                        checkItemVo.setCheckSubItem("是否陪餐");
                        checkItemVo.setComment(breakfast.getCommentList().stream().map(commentDto -> commentDto.getContent()).collect(Collectors.joining(";")));
                        checkItemVo.setCheckResult(breakfast.getIsAccompanyMeal().getName());
                        checkItemVoList.add(checkItemVo);
                    }
                }
            }
            // 早读
            MorningReading morningReading = this.morningReadingMapper.selectOne((new QueryWrapper<MorningReading>()).eq("leader_duty_id", leaderDuty.getId()));
            if (morningReading != null) {
                for (int i = 0; i < 3; i++) {
                    CheckItemVo checkItemVo = new CheckItemVo();
                    checkItemVo.setShift("早班");
                    checkItemVo.setStartTime(morningReading.getStartTime());
                    checkItemVo.setEndTime(morningReading.getEndTime());
                    checkItemVo.setCheckItem("早读");
                    checkItemVo.setLeaderName(leaderDuty.getLeader().getName());
                    String urls = "";
                    for (MorningReadingImages image : morningReading.getMorningReadingImagesList()) {
                        if (i == 0 && image.getImageClassify().equals(GradeEnum.ONE)) {
                            urls = urls + ',' + image.getUrl();
                        } else if (i == 1 && image.getImageClassify().equals(GradeEnum.TWO)) {
                            urls = urls + ',' + image.getUrl();
                        } else if (i == 2 && image.getImageClassify().equals(GradeEnum.THREE)) {
                            urls = urls + ',' + image.getUrl();
                        }
                    }
                    if (!StringUtils.isNullOrEmpty(urls)) {
                        checkItemVo.setImageUrls(urls.substring(1));
                    }
                    checkItemVo.setUpdateTime(morningReading.getUpdateTime());

                    if (i == 0) {
                        if(YesNoEnum.YES.equals(warning)){
                            checkItemVo.setCheckItem("高一年级早读");
                            checkItemVo.setCheckSubItem("高一年级");
                            checkItemVo.setCheckResult(morningReading.getReadingOrder1().getName());
                            String comment = breakfast.getCommentList().stream().filter(commentDto -> ClassifyEnum.DAY_MORNING_READING_2.equals(commentDto.getClassify()))
                                    .map(commentDto -> commentDto.getContent()).collect(Collectors.joining(";"));
                            checkItemVo.setComment(comment);
                            checkItemVoList.add(checkItemVo);
                        }else{
                            if(DayOrderEnum.IMPROVE.equals(morningReading.getReadingOrder1())){
                                checkItemVo.setCheckSubItem("高一年级");
                                checkItemVo.setCheckItem("高一年级早读");
                                checkItemVo.setCheckResult(morningReading.getReadingOrder1().getName());
                                String comment = breakfast.getCommentList().stream().filter(commentDto -> ClassifyEnum.DAY_MORNING_READING_2.equals(commentDto.getClassify()))
                                        .map(commentDto -> commentDto.getContent()).collect(Collectors.joining(";"));
                                checkItemVo.setComment(comment);
                                checkItemVoList.add(checkItemVo);
                            }
                        }
                    }
                    if (i == 1) {
                        if(YesNoEnum.YES.equals(warning)){
                            checkItemVo.setCheckItem("高二年级早读");
                            checkItemVo.setCheckSubItem("高二年级");
                            checkItemVo.setCheckResult(morningReading.getReadingOrder2().getName());
                            String comment = breakfast.getCommentList().stream().filter(commentDto -> ClassifyEnum.DAY_MORNING_READING_1.equals(commentDto.getClassify()))
                                    .map(commentDto -> commentDto.getContent()).collect(Collectors.joining(";"));
                            checkItemVo.setComment(comment);
                            checkItemVoList.add(checkItemVo);
                        }else{
                            if(DayOrderEnum.IMPROVE.equals(morningReading.getReadingOrder2())){
                                checkItemVo.setCheckSubItem("高二年级");
                                checkItemVo.setCheckItem("高二年级早读");
                                checkItemVo.setCheckResult(morningReading.getReadingOrder2().getName());
                                String comment = breakfast.getCommentList().stream().filter(commentDto -> ClassifyEnum.DAY_MORNING_READING_1.equals(commentDto.getClassify()))
                                        .map(commentDto -> commentDto.getContent()).collect(Collectors.joining(";"));
                                checkItemVo.setComment(comment);
                                checkItemVoList.add(checkItemVo);
                            }
                        }
                    }
                    if (i == 2 ) {
                        if(YesNoEnum.YES.equals(warning)){
                            checkItemVo.setCheckItem("高三年级早读");
                            checkItemVo.setCheckSubItem("高三年级");
                            checkItemVo.setCheckResult(morningReading.getReadingOrder3().getName());
                            String comment = breakfast.getCommentList().stream().filter(commentDto -> ClassifyEnum.DAY_MORNING_READING_3.equals(commentDto.getClassify()))
                                    .map(commentDto -> commentDto.getContent()).collect(Collectors.joining(";"));
                            checkItemVo.setComment(comment);
                            checkItemVoList.add(checkItemVo);
                        }else{
                            if(DayOrderEnum.IMPROVE.equals(morningReading.getReadingOrder3())){
                                checkItemVo.setCheckSubItem("高三年级");
                                checkItemVo.setCheckItem("高三年级早读");
                                checkItemVo.setCheckResult(morningReading.getReadingOrder3().getName());
                                String comment = breakfast.getCommentList().stream().filter(commentDto -> ClassifyEnum.DAY_MORNING_READING_3.equals(commentDto.getClassify()))
                                        .map(commentDto -> commentDto.getContent()).collect(Collectors.joining(";"));
                                checkItemVo.setComment(comment);
                                checkItemVoList.add(checkItemVo);
                            }
                        }
                    }
                }
            }
            // 大课间活动
            BreakActivity breakActivity = this.breakActivityMapper.selectOne((new QueryWrapper<BreakActivity>()).eq("leader_duty_id", leaderDuty.getId()));
            if (breakActivity != null) {
                if(YesNoEnum.YES.equals(warning)){
                    CheckItemVo checkItemVo = new CheckItemVo();
                    checkItemVo.setShift("早班");
                    checkItemVo.setStartTime(breakActivity.getStartTime());
                    checkItemVo.setEndTime(breakActivity.getEndTime());
                    checkItemVo.setCheckItem("大课间活动");
                    checkItemVo.setLeaderName(leaderDuty.getLeader().getName());
                    checkItemVo.setCheckSubItem(breakActivity.getActivityGrade());
                    checkItemVo.setCheckResult(breakActivity.getActivityOrder().getName());
                    checkItemVo.setComment(breakActivity.getCommentList().stream().map(commentDto -> commentDto.getContent()).collect(Collectors.joining(";")));
                    checkItemVo.setUpdateTime(breakActivity.getUpdateTime());
                    checkItemVoList.add(checkItemVo);
                }else{
                    if(breakActivity.getActivityOrder() == DayOrderEnum.IMPROVE){
                        CheckItemVo checkItemVo = new CheckItemVo();
                        checkItemVo.setShift("早班");
                        checkItemVo.setStartTime(breakActivity.getStartTime());
                        checkItemVo.setEndTime(breakActivity.getEndTime());
                        checkItemVo.setCheckItem("大课间活动");
                        checkItemVo.setLeaderName(leaderDuty.getLeader().getName());
                        checkItemVo.setCheckSubItem(breakActivity.getActivityGrade());
                        checkItemVo.setCheckResult(breakActivity.getActivityOrder().getName());
                        checkItemVo.setComment(breakActivity.getCommentList().stream().map(commentDto -> commentDto.getContent()).collect(Collectors.joining(";")));
                        checkItemVo.setUpdateTime(breakActivity.getUpdateTime());
                        checkItemVoList.add(checkItemVo);
                    }
                }
            }
            // 午班
            // 午餐
            Lunch lunch = this.lunchMapper.selectOne((new QueryWrapper<Lunch>()).eq("leader_duty_id", leaderDuty.getId()));
            if (lunch != null ) {
                if(YesNoEnum.YES.equals(warning)){
                    CheckItemVo checkItemVo = new CheckItemVo();
                    checkItemVo.setShift("午班");
                    checkItemVo.setStartTime(lunch.getStartTime());
                    checkItemVo.setEndTime(lunch.getEndTime());
                    checkItemVo.setCheckItem("午餐情况");
                    checkItemVo.setCheckSubItem("是否正常");
                    checkItemVo.setLeaderName(leaderDuty.getLeader().getName());
                    checkItemVo.setCheckResult(lunch.getDiningOrder().getName());
                    String urls = "";
                    for (LunchImages image : lunch.getLunchImagesList()) {
                        urls = urls + ',' + image.getUrl();
                    }
                    if (!StringUtils.isNullOrEmpty(urls)) {
                        checkItemVo.setImageUrls(urls.substring(1));
                    }
                    checkItemVo.setUpdateTime(lunch.getUpdateTime());
                    checkItemVo.setComment(lunch.getCommentList().stream().map(commentDto -> commentDto.getContent()).collect(Collectors.joining(";")));
                    checkItemVoList.add(checkItemVo);
                }else{
                    if(DayOrderEnum.IMPROVE.equals(lunch.getDiningOrder())){
                        CheckItemVo checkItemVo = new CheckItemVo();
                        checkItemVo.setShift("午班");
                        checkItemVo.setStartTime(lunch.getStartTime());
                        checkItemVo.setEndTime(lunch.getEndTime());
                        checkItemVo.setCheckItem("午餐情况");
                        checkItemVo.setCheckSubItem("是否正常");
                        checkItemVo.setLeaderName(leaderDuty.getLeader().getName());
                        checkItemVo.setCheckResult(lunch.getDiningOrder().getName());
                        String urls = "";
                        for (LunchImages image : lunch.getLunchImagesList()) {
                            urls = urls + ',' + image.getUrl();
                        }
                        if (!StringUtils.isNullOrEmpty(urls)) {
                            checkItemVo.setImageUrls(urls.substring(1));
                        }
                        checkItemVo.setUpdateTime(lunch.getUpdateTime());
                        checkItemVo.setComment(lunch.getCommentList().stream().map(commentDto -> commentDto.getContent()).collect(Collectors.joining(";")));
                        checkItemVoList.add(checkItemVo);
                    }
                }

            }
            // 教学区秩序
            TeachingArea teachingArea = this.teachingAreaMapper.selectOne((new QueryWrapper<TeachingArea>()).eq("leader_duty_id", leaderDuty.getId()));
            if (teachingArea != null) {
                for (int i = 0; i < 3; i++) {
                    CheckItemVo checkItemVo = new CheckItemVo();
                    checkItemVo.setShift("午班");
                    checkItemVo.setStartTime(teachingArea.getStartTime());
                    checkItemVo.setEndTime(teachingArea.getEndTime());
                    checkItemVo.setCheckItem("教学区秩序");
                    checkItemVo.setLeaderName(leaderDuty.getLeader().getName());
                    String urls = "";
                    for (TeachingAreaImages image : teachingArea.getTeachingAreaImagesList()) {
                        if (i == 0 && image.getImageClassify().equals(GradeEnum.ONE)) {
                            urls = urls + ',' + image.getUrl();
                        } else if (i == 1 && image.getImageClassify().equals(GradeEnum.TWO)) {
                            urls = urls + ',' + image.getUrl();
                        } else if (i == 2 && image.getImageClassify().equals(GradeEnum.THREE)) {
                            urls = urls + ',' + image.getUrl();
                        }
                    }
                    if (!StringUtils.isNullOrEmpty(urls)) {
                        checkItemVo.setImageUrls(urls.substring(1));
                    }
                    checkItemVo.setUpdateTime(teachingArea.getUpdateTime());
                    if (i == 0 ) {
                        if(YesNoEnum.YES.equals(warning)){
                            checkItemVo.setCheckItem("高一年级教学区秩序");
                            checkItemVo.setCheckSubItem("高一年级");
                            checkItemVo.setCheckResult(teachingArea.getTeachingAreaOrder1().getName());
                            String comment = teachingArea.getCommentList().stream().filter(commentDto -> ClassifyEnum.DAY_TEACHING_AREA_1.equals(commentDto.getClassify()))
                                    .map(commentDto -> commentDto.getContent()).collect(Collectors.joining(";"));
                            checkItemVo.setComment(comment);
                            checkItemVoList.add(checkItemVo);
                        }else{
                            if(DayOrderEnum.IMPROVE.equals(teachingArea.getTeachingAreaOrder1())){
                                checkItemVo.setCheckSubItem("高一年级");
                                checkItemVo.setCheckItem("高一年级教学区秩序");
                                checkItemVo.setCheckResult(teachingArea.getTeachingAreaOrder1().getName());
                                String comment = teachingArea.getCommentList().stream().filter(commentDto -> ClassifyEnum.DAY_TEACHING_AREA_1.equals(commentDto.getClassify()))
                                        .map(commentDto -> commentDto.getContent()).collect(Collectors.joining(";"));
                                checkItemVo.setComment(comment);
                                checkItemVoList.add(checkItemVo);
                            }
                        }

                    }
                    if (i == 1 ) {
                        if(YesNoEnum.YES.equals(warning)){
                            checkItemVo.setCheckItem("高二年级教学区秩序");
                            checkItemVo.setCheckSubItem("高二年级");
                            checkItemVo.setCheckResult(teachingArea.getTeachingAreaOrder2().getName());
                            String comment = teachingArea.getCommentList().stream().filter(commentDto -> ClassifyEnum.DAY_TEACHING_AREA_2.equals(commentDto.getClassify()))
                                    .map(commentDto -> commentDto.getContent()).collect(Collectors.joining(";"));
                            checkItemVo.setComment(comment);
                            checkItemVoList.add(checkItemVo);
                        }else{
                            if(DayOrderEnum.IMPROVE.equals(teachingArea.getTeachingAreaOrder2())){
                                checkItemVo.setCheckSubItem("高二年级");
                                checkItemVo.setCheckItem("高二年级教学区秩序");
                                checkItemVo.setCheckResult(teachingArea.getTeachingAreaOrder2().getName());
                                String comment = teachingArea.getCommentList().stream().filter(commentDto -> ClassifyEnum.DAY_TEACHING_AREA_2.equals(commentDto.getClassify()))
                                        .map(commentDto -> commentDto.getContent()).collect(Collectors.joining(";"));
                                checkItemVo.setComment(comment);
                                checkItemVoList.add(checkItemVo);
                            }
                        }
                    }
                    if (i == 2) {
                        if(YesNoEnum.YES.equals(warning)){
                            checkItemVo.setCheckItem("高三年级教学区秩序");
                            checkItemVo.setCheckSubItem("高三年级");
                            checkItemVo.setCheckResult(teachingArea.getTeachingAreaOrder3().getName());
                            String comment = teachingArea.getCommentList().stream().filter(commentDto -> ClassifyEnum.DAY_TEACHING_AREA_3.equals(commentDto.getClassify()))
                                    .map(commentDto -> commentDto.getContent()).collect(Collectors.joining(";"));
                            checkItemVo.setComment(comment);
                            checkItemVoList.add(checkItemVo);
                        }else{
                            if(DayOrderEnum.IMPROVE.equals(teachingArea.getTeachingAreaOrder3())){
                                checkItemVo.setCheckSubItem("高三年级");
                                checkItemVo.setCheckItem("高三年级教学区秩序");
                                checkItemVo.setCheckResult(teachingArea.getTeachingAreaOrder3().getName());
                                String comment = teachingArea.getCommentList().stream().filter(commentDto -> ClassifyEnum.DAY_TEACHING_AREA_3.equals(commentDto.getClassify()))
                                        .map(commentDto -> commentDto.getContent()).collect(Collectors.joining(";"));
                                checkItemVo.setComment(comment);
                                checkItemVoList.add(checkItemVo);
                            }
                        }
                    }
                }
            }
            // 运动区秩序
            NoonSportArea noonSportArea = this.noonSportAreaMapper.selectOne((new QueryWrapper<NoonSportArea>()).eq("leader_duty_id", leaderDuty.getId()));
            if (noonSportArea != null) {
                if(YesNoEnum.YES.equals(warning)){
                    CheckItemVo checkItemVo = new CheckItemVo();
                    checkItemVo.setShift("午班");
                    checkItemVo.setStartTime(noonSportArea.getStartTime());
                    checkItemVo.setEndTime(noonSportArea.getEndTime());
                    checkItemVo.setCheckItem("运动区秩序");
                    checkItemVo.setCheckSubItem("是否正常");
                    checkItemVo.setLeaderName(leaderDuty.getLeader().getName());
                    checkItemVo.setCheckResult(noonSportArea.getSportAreaOrder().getName());
                    String urls = "";
                    for (NoonSportAreaImages image : noonSportArea.getNoonSportAreaImagesList()) {
                        urls = urls + ',' + image.getUrl();
                    }
                    if (!StringUtils.isNullOrEmpty(urls)) {
                        checkItemVo.setImageUrls(urls.substring(1));
                    }
                    checkItemVo.setComment(noonSportArea.getIncidentList().stream().map(incident -> incident.getContent()).collect(Collectors.joining(";")));
                    checkItemVo.setUpdateTime(noonSportArea.getUpdateTime());
                    checkItemVoList.add(checkItemVo);
                }else{
                    if(DayOrderEnum.IMPROVE.equals(noonSportArea.getSportAreaOrder())){
                        CheckItemVo checkItemVo = new CheckItemVo();
                        checkItemVo.setShift("午班");
                        checkItemVo.setStartTime(noonSportArea.getStartTime());
                        checkItemVo.setEndTime(noonSportArea.getEndTime());
                        checkItemVo.setCheckItem("运动区秩序");
                        checkItemVo.setCheckSubItem("是否正常");
                        checkItemVo.setLeaderName(leaderDuty.getLeader().getName());
                        checkItemVo.setCheckResult(noonSportArea.getSportAreaOrder().getName());
                        String urls = "";
                        for (NoonSportAreaImages image : noonSportArea.getNoonSportAreaImagesList()) {
                            urls = urls + ',' + image.getUrl();
                        }
                        if (!StringUtils.isNullOrEmpty(urls)) {
                            checkItemVo.setImageUrls(urls.substring(1));
                        }
                        checkItemVo.setComment(noonSportArea.getIncidentList().stream().map(incident -> incident.getContent()).collect(Collectors.joining(";")));
                        checkItemVo.setUpdateTime(noonSportArea.getUpdateTime());
                        checkItemVoList.add(checkItemVo);
                    }
                }
            }
            // 晚班
            // 晚餐
            Dinner dinner = this.dinnerMapper.selectOne((new QueryWrapper<Dinner>()).eq("leader_duty_id", leaderDuty.getId()));
            if (dinner != null ) {
                if(YesNoEnum.YES.equals(warning)){
                    CheckItemVo checkItemVo = new CheckItemVo();
                    checkItemVo.setShift("晚班");
                    checkItemVo.setStartTime(dinner.getStartTime());
                    checkItemVo.setEndTime(dinner.getEndTime());
                    checkItemVo.setCheckItem("晚餐情况");
                    checkItemVo.setCheckSubItem("是否正常");
                    checkItemVo.setCheckResult(dinner.getDiningOrder().getName());
                    checkItemVo.setCheckSubItem("是否陪餐");
                    checkItemVo.setLeaderName(leaderDuty.getLeader().getName());
                    checkItemVo.setCheckResult(dinner.getIsAccompanyMeal().getName());
                    String urls = "";
                    for (DinnerImages image : dinner.getDinnerImagesList()) {
                        urls = urls + ',' + image.getUrl();
                    }
                    if (!StringUtils.isNullOrEmpty(urls)) {
                        checkItemVo.setImageUrls(urls.substring(1));
                    }
                    checkItemVo.setComment(dinner.getCommentList().stream().map(commentDto -> commentDto.getContent()).collect(Collectors.joining(";")));
                    checkItemVo.setUpdateTime(dinner.getUpdateTime());
                    checkItemVoList.add(checkItemVo);
                }else{
                    if(DayOrderEnum.IMPROVE.equals(dinner.getDiningOrder())){
                        CheckItemVo checkItemVo = new CheckItemVo();
                        checkItemVo.setShift("晚班");
                        checkItemVo.setStartTime(dinner.getStartTime());
                        checkItemVo.setEndTime(dinner.getEndTime());
                        checkItemVo.setCheckItem("晚餐情况");
                        checkItemVo.setCheckSubItem("是否正常");
                        checkItemVo.setCheckResult(dinner.getDiningOrder().getName());
                        checkItemVo.setCheckSubItem("是否陪餐");
                        checkItemVo.setCheckResult(dinner.getIsAccompanyMeal().getName());
                        String urls = "";
                        checkItemVo.setLeaderName(leaderDuty.getLeader().getName());
                        for (DinnerImages image : dinner.getDinnerImagesList()) {
                            urls = urls + ',' + image.getUrl();
                        }
                        if (!StringUtils.isNullOrEmpty(urls)) {
                            checkItemVo.setImageUrls(urls.substring(1));
                        }
                        checkItemVo.setComment(dinner.getCommentList().stream().map(commentDto -> commentDto.getContent()).collect(Collectors.joining(";")));
                        checkItemVo.setUpdateTime(dinner.getUpdateTime());
                        checkItemVoList.add(checkItemVo);
                    }
                }
            }
            // 走读生提前出门
            GoOut goOut = this.goOutMapper.selectOne((new QueryWrapper<GoOut>()).eq("leader_duty_id", leaderDuty.getId()));
            if (goOut != null) {
                if(YesNoEnum.YES.equals(warning)){
                    CheckItemVo checkItemVo = new CheckItemVo();
                    checkItemVo.setShift("晚班");
                    checkItemVo.setStartTime(goOut.getStartTime());
                    checkItemVo.setEndTime(goOut.getEndTime());
                    checkItemVo.setCheckItem("走读生提前出门情况");
                    checkItemVo.setCheckSubItem("是否正常");
                    checkItemVo.setCheckResult(goOut.getGoOutOrder().getName());
                    String urls = "";
                    checkItemVo.setLeaderName(leaderDuty.getLeader().getName());
                    for (GoOutImages image : goOut.getGoOutImagesList()) {
                        urls = urls + ',' + image.getUrl();
                    }
                    if (!StringUtils.isNullOrEmpty(urls)) {
                        checkItemVo.setImageUrls(urls.substring(1));
                    }
                    checkItemVo.setUpdateTime(goOut.getUpdateTime());
                    checkItemVo.setComment(goOut.getIncidentList().stream().map(incident -> incident.getContent()).collect(Collectors.joining(";")));
                    checkItemVoList.add(checkItemVo);
                }else{
                    if(DayOrderEnum.IMPROVE.equals(goOut.getGoOutOrder())){
                        CheckItemVo checkItemVo = new CheckItemVo();
                        checkItemVo.setShift("晚班");
                        checkItemVo.setStartTime(goOut.getStartTime());
                        checkItemVo.setEndTime(goOut.getEndTime());
                        checkItemVo.setCheckItem("走读生提前出门情况");
                        checkItemVo.setCheckSubItem("是否正常");
                        checkItemVo.setCheckResult(goOut.getGoOutOrder().getName());
                        String urls = "";
                        checkItemVo.setLeaderName(leaderDuty.getLeader().getName());
                        for (GoOutImages image : goOut.getGoOutImagesList()) {
                            urls = urls + ',' + image.getUrl();
                        }
                        if (!StringUtils.isNullOrEmpty(urls)) {
                            checkItemVo.setImageUrls(urls.substring(1));
                        }
                        checkItemVo.setUpdateTime(goOut.getUpdateTime());
                        checkItemVo.setComment(goOut.getIncidentList().stream().map(incident -> incident.getContent()).collect(Collectors.joining(";")));
                        checkItemVoList.add(checkItemVo);
                    }
                }

            }
            // 运动区秩序
            NightSportArea nightSportArea = this.nightSportAreaMapper.selectOne((new QueryWrapper<NightSportArea>()).eq("leader_duty_id", leaderDuty.getId()));
            if (nightSportArea != null ) {
                if(YesNoEnum.YES.equals(warning)){
                    CheckItemVo checkItemVo = new CheckItemVo();
                    checkItemVo.setShift("晚班");
                    checkItemVo.setStartTime(nightSportArea.getStartTime());
                    checkItemVo.setEndTime(nightSportArea.getEndTime());
                    checkItemVo.setCheckItem("运动区秩序");
                    checkItemVo.setCheckSubItem("是否正常");
                    checkItemVo.setLeaderName(leaderDuty.getLeader().getName());
                    checkItemVo.setCheckResult(nightSportArea.getSportAreaOrder().getName());
                    String urls = "";
                    for (NightSportAreaImages image : nightSportArea.getNightSportAreaImagesList()) {
                        urls = urls + ',' + image.getUrl();
                    }
                    if (!StringUtils.isNullOrEmpty(urls)) {
                        checkItemVo.setImageUrls(urls.substring(1));
                    }
                    checkItemVo.setUpdateTime(nightSportArea.getUpdateTime());
                    checkItemVo.setComment(nightSportArea.getIncidentList().stream().map(incident -> incident.getContent()).collect(Collectors.joining(";")));
                    checkItemVoList.add(checkItemVo);
                }else{
                    if(DayOrderEnum.IMPROVE.equals(nightSportArea.getSportAreaOrder())){
                        CheckItemVo checkItemVo = new CheckItemVo();
                        checkItemVo.setShift("晚班");
                        checkItemVo.setStartTime(nightSportArea.getStartTime());
                        checkItemVo.setEndTime(nightSportArea.getEndTime());
                        checkItemVo.setCheckItem("运动区秩序");
                        checkItemVo.setCheckSubItem("是否正常");
                        checkItemVo.setLeaderName(leaderDuty.getLeader().getName());
                        checkItemVo.setCheckResult(nightSportArea.getSportAreaOrder().getName());
                        String urls = "";
                        for (NightSportAreaImages image : nightSportArea.getNightSportAreaImagesList()) {
                            urls = urls + ',' + image.getUrl();
                        }
                        if (!StringUtils.isNullOrEmpty(urls)) {
                            checkItemVo.setImageUrls(urls.substring(1));
                        }
                        checkItemVo.setUpdateTime(nightSportArea.getUpdateTime());
                        checkItemVo.setComment(nightSportArea.getIncidentList().stream().map(incident -> incident.getContent()).collect(Collectors.joining(";")));
                        checkItemVoList.add(checkItemVo);
                    }
                }
            }
        }
        Long academicYearSemesterId = ((User) SecurityUtils.getSubject().getPrincipal()).getAcademicYearSemester().getId();
        for (int i = 0; i < 3; i++) {
            Long gradeId = 1L;
            String gradeName = "高一年级";
            if(i == 1){
                 gradeId = 2L;
                 gradeName = "高二年级";
            }else if(i == 2){
                 gradeId = 3L;
                 gradeName = "高三年级";
            }
            Map<String,Object> map = dailyAttendanceMapper.isAbnormal(DateUtils.parse(dutyDate+" 00:00:00"),gradeId,academicYearSemesterId);
            if(map.containsKey("illNum") && map.containsKey("thingsNum") && map.containsKey("today")){
                Integer illNum = Integer.parseInt(map.get("illNum").toString());
                Integer thingsNum = Integer.parseInt(map.get("thingsNum").toString());
                Integer illTodayNum = Integer.parseInt(map.get("today").toString().split(",")[0]);
                Integer thingsTodayNum = Integer.parseInt(map.get("today").toString().split(",")[1]);
                if((illTodayNum - (illNum - illTodayNum)/7) >= 5){
                    CheckItemVo checkItemVo = new CheckItemVo();
                    checkItemVo.setShift("白班");
                    checkItemVo.setStartTime(new Date());
                    checkItemVo.setEndTime(new Date());
                    checkItemVo.setCheckItem("病假");
                    checkItemVo.setComment(gradeName+illTodayNum+"人");
                    checkItemVo.setCheckSubItem("是否正常");
                    checkItemVo.setCheckResult("异常");
                    checkItemVoList.add(checkItemVo);
                }
                if((thingsTodayNum - (thingsNum - thingsTodayNum)/7) >= 5){
                    CheckItemVo checkItemVo = new CheckItemVo();
                    checkItemVo.setShift("白班");
                    checkItemVo.setStartTime(new Date());
                    checkItemVo.setEndTime(new Date());
                    checkItemVo.setCheckItem("事假");
                    checkItemVo.setComment(gradeName+thingsTodayNum+"人");
                    checkItemVo.setCheckSubItem("是否正常");
                    checkItemVo.setCheckResult("异常");
                    checkItemVoList.add(checkItemVo);
                }
            }
        }
        return checkItemVoList;
    }

    @Override
    public NightStudyInfoVo getNightStudyInfo(Long staffId, String dutyDate) {
        if (StringUtils.isNullOrEmpty(dutyDate)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.add(calendar.DAY_OF_MONTH, -1);
            dutyDate = sdf.format(calendar.getTime());
        }
        NightStudyInfoVo nightStudyInfoVo = new NightStudyInfoVo();
        QueryWrapper<LeaderDuty> queryWrapper = new QueryWrapper<LeaderDuty>();
        queryWrapper.eq("duty_type", "NIGHT_STUDY");
        if (staffId != null)
            queryWrapper.eq("leader_id", staffId);
        queryWrapper.ge("start_time", java.sql.Timestamp.valueOf(dutyDate + " 00:00:00"));
        queryWrapper.le("start_time", java.sql.Timestamp.valueOf(dutyDate + " 23:59:59"));
        List<LeaderDuty> leaderDutys = this.list(queryWrapper);
        if (CollectionUtils.isEmpty(leaderDutys)) return null;
        nightStudyInfoVo.setLeaderId(leaderDutys.get(0).getLeaderId());
        nightStudyInfoVo.setLeader(leaderDutys.get(0).getLeader());
        nightStudyInfoVo.setSchoolyard(leaderDutys.get(0).getSchoolyard());
        nightStudyInfoVo.setDutyDate(java.sql.Date.valueOf(dutyDate));
        // 获得年级列表
        List<Grade> gradeList = this.gradeMapper.selectList(new QueryWrapper<Grade>().eq("schoolyard_id", 1).orderByAsc("id"));
        List<GradeNightStudyInfoVo> gradeNightStudyInfoVoList = new ArrayList<>();
        for(Grade grade : gradeList) {
            GradeNightStudyInfoVo gradeNightStudyInfoVo = new GradeNightStudyInfoVo();
            gradeNightStudyInfoVo.setGradeId(grade.getId());
            gradeNightStudyInfoVo.setGrade(grade);
            // 生成所有行
            Map<String, Object> row1 = new HashMap<>();
            row1.put("id", 1);
            row1.put("stage", "第一阶段");
            row1.put("name", "应到人数");
            Map<String, Object> row2 = new HashMap<>();
            row2.put("id", 2);
            row2.put("stage", "第一阶段");
            row2.put("name", "实到人数");
            Map<String, Object> row3 = new HashMap<>();
            row3.put("id", 3);
            row3.put("stage", "第一阶段");
            row3.put("name", "值班老师");
            Map<String, Object> row4 = new HashMap<>();
            row4.put("id", 4);
            row4.put("stage", "第一阶段");
            row4.put("name", "表现分");
            Map<String, Object> row5 = new HashMap<>();
            row5.put("id", 5);
            row5.put("stage", "第一阶段");
            row5.put("name", "班级扣分情况说明");
            Map<String, Object> row6 = new HashMap<>();
            row6.put("id", 6);
            row6.put("stage", "第二阶段");
            row6.put("name", "应到人数");
            Map<String, Object> row7 = new HashMap<>();
            row7.put("id", 7);
            row7.put("stage", "第二阶段");
            row7.put("name", "实到人数");
            Map<String, Object> row8 = new HashMap<>();
            row8.put("id", 8);
            row8.put("stage", "第二阶段");
            row8.put("name", "值班老师");
            Map<String, Object> row9 = new HashMap<>();
            row9.put("id", 9);
            row9.put("stage", "第二阶段");
            row9.put("name", "表现分");
            Map<String, Object> row10 = new HashMap<>();
            row10.put("id", 10);
            row10.put("stage", "第二阶段");
            row10.put("name", "班级扣分情况说明");
            // 获得班级列表
            AcademicYearSemester academicYearSemester = academicYearSemesterService.getYearSemesterByDate(dutyDate);
            QueryWrapper<Clazz> clazzQueryWrapper = new QueryWrapper<Clazz>();
            clazzQueryWrapper.eq("academic_year_semester_id", academicYearSemester.getId());
            clazzQueryWrapper.eq("grade_id", grade.getId());
            List<Clazz> clazzList = this.clazzMapper.selectList(clazzQueryWrapper);
            clazzList.sort(Comparator.comparing(item -> Integer.parseInt(item.getName().replace("班", ""))));
            // 班级得分情况
            List<NightStudyDutyClazzVo> nightStudyDutyClazzVoList = nightStudyDutyClazzMapper.getDutyClazzList(leaderDutys.get(0).getId());
            for (int i = 1; i < leaderDutys.size(); i++) {
                List<NightStudyDutyClazzVo> other = nightStudyDutyClazzMapper.getDutyClazzList(leaderDutys.get(i).getId());
                nightStudyDutyClazzVoList.addAll(other);
            }
            for(int i=0;i<clazzList.size();i++){
                Clazz clazz = clazzList.get(i);
                List<NightStudyDutyClazzVo> currentClazzInfoList = nightStudyDutyClazzVoList.stream()
                        .sorted(Comparator.comparing(NightStudyDutyClazzVo::getStartTime))
                        .filter(item -> item.getClazzId().equals(clazz.getId()))
                        .collect(Collectors.toList());
                if (currentClazzInfoList.size()>0) {
                    NightStudyDutyClazzVo currentClazzInfo = currentClazzInfoList.get(0);
                    row1.put("C" + (i + 1), currentClazzInfo.getShouldStudentCount());
                    row2.put("C" + (i + 1), currentClazzInfo.getActualStudentCount());
                    row3.put("C" + (i + 1), currentClazzInfo.getTeacher());
                    row4.put("C" + (i + 1), currentClazzInfo.getScore());
                    row5.put("C" + (i + 1), currentClazzInfo.getNightStudyDutyClazzDeductionList());
                }
                if (currentClazzInfoList.size()>1) {
                    NightStudyDutyClazzVo currentClazzInfo = currentClazzInfoList.get(1);
                    row6.put("C" + (i + 1), currentClazzInfo.getShouldStudentCount());
                    row7.put("C" + (i + 1), currentClazzInfo.getActualStudentCount());
                    row8.put("C" + (i + 1), currentClazzInfo.getTeacher());
                    row9.put("C" + (i + 1), currentClazzInfo.getScore());
                    row10.put("C" + (i + 1), currentClazzInfo.getNightStudyDutyClazzDeductionList());
                }
                if (currentClazzInfoList.size() == 0) {
                    row1.put("C" + (i + 1), 0);
                    row2.put("C" + (i + 1), 0);
                    row3.put("C" + (i + 1), "");
                    row4.put("C" + (i + 1), 0);
                    row5.put("C" + (i + 1), new ArrayList<>());
                    row6.put("C" + (i + 1), 0);
                    row7.put("C" + (i + 1), 0);
                    row8.put("C" + (i + 1), "");
                    row9.put("C" + (i + 1), 0);
                    row10.put("C" + (i + 1), new ArrayList<>());
                }
            }
            List<Map<String, Object>> clazzInfo = new ArrayList<>();
            clazzInfo.add(row1);
            clazzInfo.add(row2);
            clazzInfo.add(row3);
            clazzInfo.add(row4);
            clazzInfo.add(row5);
            clazzInfo.add(row6);
            clazzInfo.add(row7);
            clazzInfo.add(row8);
            clazzInfo.add(row9);
            clazzInfo.add(row10);
            gradeNightStudyInfoVo.setClazzList(clazzList);
            gradeNightStudyInfoVo.setClazzInfoList(clazzInfo);
            gradeNightStudyInfoVoList.add(gradeNightStudyInfoVo);
        }
        nightStudyInfoVo.setGradeList(gradeNightStudyInfoVoList);
        return nightStudyInfoVo;
    }

    @Override
    @Transactional
    public Integer updateLeader(Long id) {
        User user = (User)SecurityUtils.getSubject().getPrincipal();
        Staff staff = staffMapper.selectById(user.getStaffId());
        LeaderDuty leaderDuty = this.getById(id);
        if(leaderDuty.getLeaderId().equals(staff.getId())){
            throw new ApiCode.ApiException(-5,"您无法替自己带班！");
        }

        LeaderDutySubstitute leaderDutySubstitute = new LeaderDutySubstitute();
        leaderDutySubstitute.setDutyType(leaderDuty.getDutyType());
        leaderDutySubstitute.setLeaderId(staff.getId());
        leaderDutySubstitute.setLeaderDutyId(leaderDuty.getId());
        leaderDutySubstitute.setLeaderOldId(leaderDuty.getLeaderId());
        leaderDutySubstitute.setDefault().validate(true);
        leaderDutySubstituteMapper.insert(leaderDutySubstitute);

        TeacherDuty teacherDuty = this.teacherDutyService.getOne(Wrappers.<TeacherDuty>lambdaQuery()
                .eq(TeacherDuty::getSchoolyardId, leaderDuty.getSchoolyardId())
                .eq(TeacherDuty::getDutyType, TeacherDutyTypeEnum.TOTAL_DUTY)
                .apply("to_days(start_time)" + "=" + "to_days({0})", leaderDuty.getStartTime()));
        // 不处理为空的情况
        if (teacherDuty != null) {
            teacherDuty.setTeacherId(staff.getId());
            this.teacherDutyService.updateById(teacherDuty);
        }

        return this.baseMapper.update(new LeaderDuty(),Wrappers.<LeaderDuty>lambdaUpdate()
                .set(LeaderDuty::getLeaderId,staff.getId())
                .eq(LeaderDuty::getId,leaderDuty.getId())
        );
    }

    @Override
    public String importLeaderDuty(Long schoolyardId, String fileUrl) {
        if (StringUtils.isNullOrEmpty(fileUrl))
            throw new ApiCode.ApiException(-1, "没有上传文件！");
        String[] items = fileUrl.split("/");
        File file = new File(uploadPath + File.separator + items[items.length - 2] + File.separator + items[items.length - 1]);

        //查询老师
        QueryWrapper<Staff> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete", YesNoEnum.NO);
        List<Staff> staffList = this.staffMapper.selectList(queryWrapper);
        Map<String, List<Staff>> staffMap = staffList.stream().collect(Collectors.groupingBy(Staff::getName));
        if (!file.exists())
            throw new ApiCode.ApiException(-1, "文件不存在！");
        try {
            // 读取 Excel
            XSSFWorkbook book = new XSSFWorkbook(file);
            XSSFSheet sheet = book.getSheetAt(0);
            //  file.delete();
            //获得总行数
            int rowNum = sheet.getPhysicalNumberOfRows();
            // 获得总列数
            int columnNum = sheet.getRow(0).getPhysicalNumberOfCells();


            String day = sheet.getRow(0).getCell(1).toString().replace("（", "(").replace("）", ")");
            String night = sheet.getRow(0).getCell(2).toString().replace("（", "(").replace("）", ")");

            if (!(day.contains("(") && day.contains(")"))) {
                throw new ApiCode.ApiException(-5, "白班开始结束时间必传！");
            }
            if (!(night.contains("(") && night.contains(")"))) {
                throw new ApiCode.ApiException(-5, "晚自习开始结束时间必传！");
            }
            String dayStartTime = day.split("\\(")[1].split("\\)")[0].split("-")[0].trim();
            String dayEndTime = day.split("\\(")[1].split("\\)")[0].split("-")[1].trim();
            String nightOneStartTime = night.split("\\(")[1].split("\\)")[0].split("-")[0];
            String nightOneEndTime = night.split("\\(")[1].split("\\)")[0].split("-")[1];
            String nightTwoStartTime = night.split("\\(")[2].split("\\)")[0].split("-")[0];
            String nightTwoEndTime = night.split("\\(")[2].split("\\)")[0].split("-")[1];

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            StringBuilder sb = new StringBuilder();
            for (int rowIndex = 1; rowIndex < rowNum; rowIndex = rowIndex + 1) {
                String dateCell = CellUtils.getCellValue(sheet.getRow(rowIndex).getCell(0), "yyyy-MM-dd");
                if (DateUtils.parse(dateCell, "yyyy-MM-dd").getTime() < DateUtils.parse(DateUtils.format(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd").getTime()) {
                    sb.append("第").append(rowIndex + 1).append("行: ").append("日期早于当前日期").append("\r\n");
                    continue;
                }

                StringBuilder child = new StringBuilder();
                LeaderDuty dayLeaderDuty = new LeaderDuty();
                dayLeaderDuty.setSchoolyardId(schoolyardId);
                dayLeaderDuty.setStartTime(sdf.parse(dateCell + " " + dayStartTime));
                dayLeaderDuty.setEndTime(sdf.parse(dateCell + " " + dayEndTime));
                dayLeaderDuty.setDutyType(LeaderDutyTypeEnum.ROUTINE);
                XSSFCell cell = sheet.getRow(rowIndex).getCell(1);
                String cellValue = "";
                if (cell.getCellType().equals(CellType.STRING))
                    cellValue = cell.getStringCellValue();
                else if (cell.getCellType().equals(CellType.NUMERIC))
                    cellValue = String.valueOf(cell.getNumericCellValue());
                String key = cellValue.trim();
                if (staffMap.containsKey(key)) {
                    dayLeaderDuty.setLeaderId(staffMap.get(key).get(0).getId());
                    dayLeaderDuty.setDefault().validate(true);
                    createOrUpdate(dayLeaderDuty);
                } else if (!StringUtils.isNullOrEmpty(key)){
                    child.append("第1列").append(",");
                }

                LeaderDuty nightLeaderDuty = new LeaderDuty();
                nightLeaderDuty.setStartTime(sdf.parse(dateCell + " " + nightOneStartTime));
                nightLeaderDuty.setEndTime(sdf.parse(dateCell + " " + nightTwoEndTime));
                nightLeaderDuty.setDutyType(LeaderDutyTypeEnum.NIGHT_STUDY);
                XSSFCell cell2 = sheet.getRow(rowIndex).getCell(2);
                String cell2Value = "";
                if (cell2.getCellType().equals(CellType.STRING))
                    cell2Value = cell2.getStringCellValue();
                else if (cell2.getCellType().equals(CellType.NUMERIC))
                    cell2Value = String.valueOf(cell2.getNumericCellValue());
                key = cell2Value.trim();
                if (staffMap.containsKey(key)) {
                    nightLeaderDuty.setSchoolyardId(schoolyardId);
                    nightLeaderDuty.setLeaderId(staffMap.get(key).get(0).getId());
                    nightLeaderDuty.setDefault().validate(true);
                    NightStudyDuty nightStudyDuty = null;
                    List<NightStudyDuty> nightStudyDuties = new ArrayList<>();
                    for (int i = 0; i < 2; i++) {
                        nightStudyDuty = new NightStudyDuty();
                        nightStudyDuty.setHasContingency(YesNoEnum.NO);
                        if (i == 0) {
                            nightStudyDuty.setStartTime(sdf.parse(dateCell + " " + nightOneStartTime));
                            nightStudyDuty.setEndTime(sdf.parse(dateCell + " " + nightOneEndTime));
                        } else {
                            nightStudyDuty.setStartTime(sdf.parse(dateCell + " " + nightTwoStartTime));
                            nightStudyDuty.setEndTime(sdf.parse(dateCell + " " + nightTwoEndTime));
                        }
                        nightStudyDuties.add(nightStudyDuty);
                    }
                    nightLeaderDuty.setNightStudyDutyList(nightStudyDuties);
                    createOrUpdate(nightLeaderDuty);
                } else if (!StringUtils.isNullOrEmpty(key)) {
                    child.append("第2列").append(",");
                }
                int len;
                if ((len = child.length()) > 0) {
                    child.deleteCharAt(len - 1);
                    child.append("老师名称有误");
                    sb.append("第").append(rowIndex + 1).append("行: ").append(child).append("\r\n");
                }
            }
            if (sb.length() != 0) {
                throw new ApiCode.ApiException(-1, sb.toString());
            }
            return "导入成功";
        } catch (IOException | InvalidFormatException e) {
            throw new ApiCode.ApiException(-1, "导入数据失败！");
        } catch (ParseException p) {
            throw new ApiCode.ApiException(-1, "时间解析失败");
        }
    }

    private void createOrUpdate(LeaderDuty leaderDuty){
        LeaderDuty leaderDuty1 = this.baseMapper.getByTime(leaderDuty.getStartTime(),leaderDuty.getDutyType().toString(),leaderDuty.getSchoolyardId());
        if(leaderDuty1 != null){
            this.nightStudyDutyMapper.delete(new QueryWrapper<NightStudyDuty>()
                    .eq("leader_duty_id",leaderDuty1.getId())
            );
            leaderDuty1.setSchoolyardId(leaderDuty.getSchoolyardId());
            leaderDuty1.setStartTime(leaderDuty.getStartTime());
            leaderDuty1.setEndTime(leaderDuty.getEndTime());
            leaderDuty1.setLeaderId(leaderDuty.getLeaderId());
            this.baseMapper.updateById(leaderDuty1);
            leaderDuty.setId(leaderDuty1.getId());
        }else{
            this.baseMapper.insert(leaderDuty);
        }
        if(CollectionUtils.isNotEmpty(leaderDuty.getNightStudyDutyList())){
            leaderDuty.getNightStudyDutyList().stream().forEach(nightStudyDuty -> {
                nightStudyDuty.setLeaderDutyId(leaderDuty.getId());
                nightStudyDuty.setDefault().validate(true);
            });
            nightStudyDutyMapper.batchInsert(leaderDuty.getNightStudyDutyList());
        }
    }

    @Override
    public Integer cancelUpdateLeader(Long leaderDutyId, Long leaderId) {
        User user = (User)SecurityUtils.getSubject().getPrincipal();
        LeaderDuty leaderDuty = this.getById(leaderDutyId);
        if(leaderDuty.getLeaderId() != user.getStaffId()){
            throw new ApiCode.ApiException(-5,"该用户不是当前值班人员，无法取消！");
        }
        TeacherDuty teacherDuty = this.teacherDutyService.getOne(Wrappers.<TeacherDuty>lambdaQuery()
                .eq(TeacherDuty::getSchoolyardId, leaderDuty.getSchoolyardId())
                .eq(TeacherDuty::getDutyType, TeacherDutyTypeEnum.TOTAL_DUTY)
                .apply("to_days(start_time)" + "=" + "to_days({0})", leaderDuty.getStartTime()));
        // 不处理为空的情况
        if (teacherDuty != null) {
            teacherDuty.setTeacherId(leaderId);
            this.teacherDutyService.updateById(teacherDuty);
        }

        return this.baseMapper.update(new LeaderDuty(),Wrappers.<LeaderDuty>lambdaUpdate()
                .set(LeaderDuty::getLeaderId,leaderId)
                .eq(LeaderDuty::getId,leaderDuty.getId())
        );
    }

    @Override
    public List<Staff> cancelLeaderChoosePeople(Long leaderDutyId) {
        User user = (User)SecurityUtils.getSubject().getPrincipal();
        LeaderDutySubstitute leaderDutySubstitute = leaderDutySubstituteMapper.selectOne(Wrappers.<LeaderDutySubstitute>lambdaQuery()
                .eq(LeaderDutySubstitute::getLeaderDutyId,leaderDutyId)
                .eq(LeaderDutySubstitute::getLeaderId,user.getStaffId())
                .orderByDesc(LeaderDutySubstitute::getId)
        );
        List<LeaderDutySubstitute> leaderDutySubstitutes = leaderDutySubstituteMapper.selectList(Wrappers.<LeaderDutySubstitute>lambdaQuery()
                .eq(LeaderDutySubstitute::getLeaderDutyId,leaderDutyId)
                .le(LeaderDutySubstitute::getId,leaderDutySubstitute.getId())
        );
        List<Long> ids = new ArrayList<>();
        leaderDutySubstitutes.stream().forEach(item -> {
            if(!item.getLeaderOldId().equals(user.getStaffId())){
                ids.add(item.getLeaderOldId());
            }
        });
        return this.staffMapper.selectBatchIds(ids);
    }

    @Override
    public Map<String,Object> getDayRoutineComment(Date time) {

        Map<String,Object> map = new HashMap<>();


        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Staff staff = staffMapper.selectById(user.getStaffId());

        DayRoutineDto dayRoutineDto = this.baseMapper.dayRoutine(time, RoutineEnum.DAY.toString(), staff.getId(), LeaderDutyTypeEnum.ROUTINE.toString());


        if (dayRoutineDto == null) return new HashMap<>();

        if(CollectionUtils.isNotEmpty(dayRoutineDto.getIncidentList())){
            dayRoutineDto.getIncidentList().addAll((List<Incident>) DayRoutineDto.parse(dayRoutineDto).get("incident"));
        }else{
            dayRoutineDto.setIncidentList((List<Incident>) DayRoutineDto.parse(dayRoutineDto).get("incident"));
        }
        List<Incident> incidentList = dayRoutineDto.getIncidentList();

        if(CollectionUtils.isNotEmpty(dayRoutineDto.getComment())){
            dayRoutineDto.getComment().addAll((List<CommentDto>) DayRoutineDto.parse(dayRoutineDto).get("comment"));
        }else{
            dayRoutineDto.setComment((List<CommentDto>) DayRoutineDto.parse(dayRoutineDto).get("comment"));
        }
        List<CommentDto> commentDtoList = dayRoutineDto.getComment();
        commentDtoList.stream().forEach(commentDto -> {
            List<FunctionDepartment> office = commentDto.getDepartmentList().stream().filter(department -> department.getParentId() == 1).collect(Collectors.toList());
            List<FunctionDepartment> grade = commentDto.getDepartmentList().stream().filter(department -> department.getParentId() == 2).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(office)){
                office = new ArrayList<>();
                FunctionDepartment functionDepartment = new FunctionDepartment();
                functionDepartment.setName("教务处");
                office.add(functionDepartment);
            }else{
                FunctionDepartment functionDepartment = new FunctionDepartment();
                functionDepartment.setName("教务处");
                office.add(functionDepartment);
            }
            commentDto.setOfficeDepartmentList(office);
            commentDto.setGradeDepartmentList(grade);
        });

        map.put("comment",commentDtoList);
        map.put("incidentList",incidentList);
        return map;
    }
    @Override
    @Transactional
    public Integer dayRoutineClickFinish(String type, Long leaderDutyId) {
        LeaderDuty leaderDuty = this.baseMapper.selectById(leaderDutyId);
        if(leaderDuty == null )
            throw new ApiCode.ApiException(-5,"领导任务未查询到");
        if("morning".equals(type)){
            Integer southGateCount = southGateMapper.selectCount(Wrappers.<SouthGate>lambdaQuery()
                    .eq(SouthGate::getLeaderDutyId,leaderDutyId)
            );
            if(Objects.equals(0,southGateCount)){
                SouthGate southGate = new SouthGate();
                southGate.setLeaderDutyId(leaderDutyId);
                southGate.setSchoolyardId(leaderDuty.getSchoolyardId());
                southGate.setStartTime(DateUtils.parse("06:15",leaderDuty.getStartTime()));
                southGate.setEndTime(DateUtils.parse("06:25",leaderDuty.getStartTime()));
                southGate.setCheckTime(new Date());
                southGate.setDefault().validate(true);
                southGateMapper.insert(southGate);
            }
            Integer breakfastCount= breakfastMapper.selectCount(Wrappers.<Breakfast>lambdaQuery()
                    .eq(Breakfast::getLeaderDutyId,leaderDutyId)
            );
            if(Objects.equals(0,breakfastCount)){
                Breakfast breakfast = new Breakfast();
                breakfast.setLeaderDutyId(leaderDutyId);
                breakfast.setSchoolyardId(leaderDuty.getSchoolyardId());
                breakfast.setStartTime(DateUtils.parse("06:25",leaderDuty.getStartTime()));
                breakfast.setEndTime(DateUtils.parse("06:40",leaderDuty.getStartTime()));
                breakfast.setCheckTime(new Date());
                breakfast.setDefault().validate(true);
                breakfastMapper.insert(breakfast);
            }
            Integer breakActivityCount= breakActivityMapper.selectCount(Wrappers.<BreakActivity>lambdaQuery()
                    .eq(BreakActivity::getLeaderDutyId,leaderDutyId)
            );
            if(Objects.equals(0,breakActivityCount)){
                BreakActivity breakActivity = new BreakActivity();
                breakActivity.setLeaderDutyId(leaderDutyId);
                breakActivity.setSchoolyardId(leaderDuty.getSchoolyardId());
                breakActivity.setActivityGrade("ONE,TWO,THREE");
                breakActivity.setActivityType(ActivityTypeEnum.EXERCISE);
                breakActivity.setStartTime(DateUtils.parse("08:50",leaderDuty.getStartTime()));
                breakActivity.setEndTime(DateUtils.parse("09:30",leaderDuty.getStartTime()));
                breakActivity.setCheckTime(new Date());
                breakActivity.setDefault().validate(true);
                breakActivityMapper.insert(breakActivity);
            }
            Integer morningReadingCount= morningReadingMapper.selectCount(Wrappers.<MorningReading>lambdaQuery()
                    .eq(MorningReading::getLeaderDutyId,leaderDutyId)
            );
            if(Objects.equals(0,morningReadingCount)){
                MorningReading morningReading = new MorningReading();
                morningReading.setLeaderDutyId(leaderDutyId);
                morningReading.setSchoolyardId(leaderDuty.getSchoolyardId());
                morningReading.setStartTime(DateUtils.parse("06:40",leaderDuty.getStartTime()));
                morningReading.setEndTime(DateUtils.parse("07:20",leaderDuty.getStartTime()));
                morningReading.setCheckTime(new Date());
                morningReading.setDefault().validate(true);
                morningReadingMapper.insert(morningReading);
            }
            return 1;
        }else if("afternoon".equals(type)){
            Integer lunchCount = lunchMapper.selectCount(Wrappers.<Lunch>lambdaQuery()
                    .eq(Lunch::getLeaderDutyId,leaderDutyId)
            );
            if(Objects.equals(0,lunchCount)){
                Lunch lunch = new Lunch();
                lunch.setLeaderDutyId(leaderDutyId);
                lunch.setSchoolyardId(leaderDuty.getSchoolyardId());
                lunch.setStartTime(DateUtils.parse("11:30",leaderDuty.getStartTime()));
                lunch.setEndTime(DateUtils.parse("12:10",leaderDuty.getStartTime()));
                lunch.setCheckTime(new Date());
                lunch.setDefault().validate(true);
                lunchMapper.insert(lunch);
            }
            Integer teachingAreaCount= teachingAreaMapper.selectCount(Wrappers.<TeachingArea>lambdaQuery()
                    .eq(TeachingArea::getLeaderDutyId,leaderDutyId)
            );
            if(Objects.equals(0,teachingAreaCount)){
                TeachingArea teachingArea = new TeachingArea();
                teachingArea.setLeaderDutyId(leaderDutyId);
                teachingArea.setSchoolyardId(leaderDuty.getSchoolyardId());
                teachingArea.setStartTime(DateUtils.parse("12:10",leaderDuty.getStartTime()));
                teachingArea.setEndTime(DateUtils.parse("13:30",leaderDuty.getStartTime()));
                teachingArea.setCheckTime(new Date());
                teachingArea.setDefault().validate(true);
                teachingAreaMapper.insert(teachingArea);
            }
            Integer noonSportAreaCount= noonSportAreaMapper.selectCount(Wrappers.<NoonSportArea>lambdaQuery()
                    .eq(NoonSportArea::getLeaderDutyId,leaderDutyId)
            );
            if(Objects.equals(0,noonSportAreaCount)){
                NoonSportArea noonSportArea = new NoonSportArea();
                noonSportArea.setLeaderDutyId(leaderDutyId);
                noonSportArea.setSchoolyardId(leaderDuty.getSchoolyardId());
                noonSportArea.setStartTime(DateUtils.parse("12:10",leaderDuty.getStartTime()));
                noonSportArea.setEndTime(DateUtils.parse("13:30",leaderDuty.getStartTime()));
                noonSportArea.setCheckTime(new Date());
                noonSportArea.setDefault().validate(true);
                noonSportAreaMapper.insert(noonSportArea);
            }
            return 1;
        }else if("evening".equals(type)){
            Integer dinnerCount = dinnerMapper.selectCount(Wrappers.<Dinner>lambdaQuery()
                    .eq(Dinner::getLeaderDutyId,leaderDutyId)
            );
            if(Objects.equals(0,dinnerCount)){
                Dinner dinner = new Dinner();
                dinner.setLeaderDutyId(leaderDutyId);
                dinner.setSchoolyardId(leaderDuty.getSchoolyardId());
                dinner.setStartTime(DateUtils.parse("17:30",leaderDuty.getStartTime()));
                dinner.setEndTime(DateUtils.parse("18:20",leaderDuty.getStartTime()));
                dinner.setCheckTime(new Date());
                dinner.setDefault().validate(true);
                dinnerMapper.insert(dinner);
            }
            Integer goOutCount= goOutMapper.selectCount(Wrappers.<GoOut>lambdaQuery()
                    .eq(GoOut::getLeaderDutyId,leaderDutyId)
            );
            if(Objects.equals(0,goOutCount)){
                GoOut goOut = new GoOut();
                goOut.setLeaderDutyId(leaderDutyId);
                goOut.setSchoolyardId(leaderDuty.getSchoolyardId());
                goOut.setStartTime(DateUtils.parse("17:30",leaderDuty.getStartTime()));
                goOut.setEndTime(DateUtils.parse("18:20",leaderDuty.getStartTime()));
                goOut.setCheckTime(new Date());
                goOut.setDefault().validate(true);
                goOutMapper.insert(goOut);
            }
            Integer nightSportAreaCount= nightSportAreaMapper.selectCount(Wrappers.<NightSportArea>lambdaQuery()
                    .eq(NightSportArea::getLeaderDutyId,leaderDutyId)
            );
            if(Objects.equals(0,nightSportAreaCount)){
                NightSportArea nightSportArea = new NightSportArea();
                nightSportArea.setLeaderDutyId(leaderDutyId);
                nightSportArea.setSchoolyardId(leaderDuty.getSchoolyardId());
                nightSportArea.setStartTime(DateUtils.parse("17:30",leaderDuty.getStartTime()));
                nightSportArea.setEndTime(DateUtils.parse("18:20",leaderDuty.getStartTime()));
                nightSportArea.setCheckTime(new Date());
                nightSportArea.setDefault().validate(true);
                nightSportAreaMapper.insert(nightSportArea);
            }
            return 1;
        }else {
            throw new ApiCode.ApiException(-5,"请传入准确类型");
        }
    }
}

