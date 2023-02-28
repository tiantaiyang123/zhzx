package com.zhzx.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhzx.server.domain.*;
import com.zhzx.server.dto.*;
import com.zhzx.server.enums.*;
import com.zhzx.server.repository.*;
import com.zhzx.server.rest.req.TeacherDutySubstituteParam;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.GradeService;
import com.zhzx.server.service.TeacherDutyGradeTotalService;
import com.zhzx.server.service.TeacherDutyService;
import com.zhzx.server.util.DateUtils;
import com.zhzx.server.vo.ClazzVo;
import lombok.SneakyThrows;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TeacherDutyGradeTotalServiceImpl implements TeacherDutyGradeTotalService {
    @Resource
    private NightStudyDutyMapper nightStudyDutyMapper;
    @Resource
    private NightStudyDutyClazzMapper nightStudyDutyClazzMapper;
    @Resource
    private NightStudyAttendanceMapper nightStudyAttendanceMapper;
    @Resource
    private NightStudyAttendanceSubMapper nightStudyAttendanceSubMapper;
    @Resource
    private IncidentMapper incidentMapper;
    @Resource
    private StaffMapper staffMapper;
    @Resource
    private LeaderDutyMapper leaderDutyMapper;
    @Resource
    private TeacherDutyMapper teacherDutyMapper;
    @Resource
    private TeacherDutyClazzMapper teacherDutyClazzMapper;
    @Resource
    private TeacherDutySubstituteMapper teacherDutySubstituteMapper;
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private FunctionDepartmentMapper functionDepartmentMapper;
    @Resource
    private GradeService gradeService;
    @Resource
    private TeacherDutyService teacherDutyService;
    @Resource
    private NightStudyMapper nightStudyMapper;

    @Override
    public NightStudyDuty getCorrespondLeaderNightStudyDuty(Long schoolyardId, Date time) {
        return this.nightStudyDutyMapper.getCorrespondLeaderNightStudyDuty(schoolyardId, time);
    }

    @SneakyThrows
//    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    private NightStudyDuty insertOrDefault(Long schoolyardId, Date time, Long staffId) {
        NightStudyDuty nightStudyDutyStageOne = this.getCorrespondLeaderNightStudyDuty(schoolyardId, time);
        if (nightStudyDutyStageOne == null) {
            // 默认安排本人作为领导值班 如果本人在另外校区有晚班 则插徐飞
            LeaderDuty leaderDutyOther = this.leaderDutyMapper.selectOne(new QueryWrapper<LeaderDuty>()
                    .eq("leader_id", staffId)
                    .eq("duty_type", "NIGHT_STUDY")
                    .ne("schoolyard_id", schoolyardId)
                    .apply("to_days(start_time)" + "=" + "to_days({0})", time));
            if (leaderDutyOther != null) staffId = 1L;
            LeaderDuty leaderDuty = new LeaderDuty();
            leaderDuty.setSchoolyardId(schoolyardId);
            leaderDuty.setDutyType(LeaderDutyTypeEnum.NIGHT_STUDY);
            leaderDuty.setLeaderId(staffId);
            leaderDuty.setStartTime(DateUtils.parse("19:30", time));
            leaderDuty.setEndTime(DateUtils.parse("22:00", time));
            leaderDuty.setDefault().validate(true);
            this.leaderDutyMapper.insert(leaderDuty);

            List<NightStudyDuty> nightStudyDutyList = new ArrayList<>();
            NightStudyDuty nightStudyDuty1 = new NightStudyDuty();
            nightStudyDuty1.setLeaderDutyId(leaderDuty.getId());
            nightStudyDuty1.setHasContingency(YesNoEnum.NO);
            nightStudyDuty1.setStartTime(leaderDuty.getStartTime());
            nightStudyDuty1.setEndTime(DateUtils.parse("20:40", time));
            nightStudyDuty1.setDefault().validate(true);
            nightStudyDutyList.add(nightStudyDuty1);

            NightStudyDuty nightStudyDuty2 = new NightStudyDuty();
            nightStudyDuty2.setLeaderDutyId(leaderDuty.getId());
            nightStudyDuty2.setHasContingency(YesNoEnum.NO);
            nightStudyDuty2.setStartTime(nightStudyDuty1.getEndTime());
            nightStudyDuty2.setEndTime(leaderDuty.getEndTime());
            nightStudyDuty2.setDefault().validate(true);
            nightStudyDutyList.add(nightStudyDuty2);

            this.nightStudyDutyMapper.batchInsert(nightStudyDutyList);

            nightStudyDutyStageOne = new NightStudyDuty();
            nightStudyDutyStageOne.setLeaderDuty(leaderDuty);
        }
        return nightStudyDutyStageOne;
    }

    @Override
    public Map<String, Object> nightRoutine(Date time, RoutineEnum type) {
        Map<String, Object> map = new HashMap<>();
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        TeacherDutyGradeTotalDto teacherDutyGradeTotalDto = this.getInfo(time, user.getStaffId());
        if (teacherDutyGradeTotalDto != null && TeacherDutyModeEnum.NORMAL.equals(teacherDutyGradeTotalDto.getDutyMode())) {
            return map;
        }
        if (teacherDutyGradeTotalDto == null
                || CollectionUtils.isEmpty(teacherDutyGradeTotalDto.getTeacherDutyGradeTotalClazzList())) {
            return map;
        }

        Long schoolyardId = teacherDutyGradeTotalDto.getSchoolyardId();
        NightStudyDuty nightStudyDutyStageOne = this.insertOrDefault(schoolyardId, time, user.getStaffId());
        List<Long> gradeIdList = teacherDutyGradeTotalDto.getTeacherDutyGradeTotalClazzList()
                .stream()
                .map(item -> item.getClazz().getGradeId()).distinct().collect(Collectors.toList());
        LeaderDuty leaderDuty = nightStudyDutyStageOne.getLeaderDuty();
        List<LeaderNightStudyDutyDto> leaderNightStudyDutyDtoList = this.nightStudyDutyMapper.nightRoutine(time, type.toString(), leaderDuty.getLeaderId());
        if (CollectionUtils.isNotEmpty(leaderNightStudyDutyDtoList)) {
            //两个晚自习
            if (leaderNightStudyDutyDtoList.size() != 2) throw new ApiCode.ApiException(-5, "阶段不匹配，请确认！");
            List<ClazzVo> stageOne = gradeService.getGradeList(time, TeacherDutyTypeEnum.STAGE_ONE);
            stageOne = stageOne.stream().filter(item -> item.getSchoolyardId().equals(schoolyardId)).collect(Collectors.toList());
            parseStage(stageOne, leaderNightStudyDutyDtoList.get(0), TeacherDutyTypeEnum.STAGE_ONE, gradeIdList);
            List<ClazzVo> stageTwo = gradeService.getGradeList(time, TeacherDutyTypeEnum.STAGE_TWO);
            stageTwo = stageTwo.stream().filter(item -> item.getSchoolyardId().equals(schoolyardId)).collect(Collectors.toList());
            parseStage(stageTwo, leaderNightStudyDutyDtoList.get(1), TeacherDutyTypeEnum.STAGE_TWO, gradeIdList);
            List<Long> ids = leaderNightStudyDutyDtoList.stream().map(NightStudyDuty::getId).collect(Collectors.toList());

            List<Incident> incidentList = incidentMapper.selectList(Wrappers.<Incident>lambdaQuery()
                    .eq(Incident::getClassify, ClassifyEnum.DAY_NIGHT_STUDY_DUTY)
                    .in(Incident::getDailyRoutineId, ids)
                    .or().eq(Incident::getClassify, ClassifyEnum.DAY_OTHER_2)
                    .in(Incident::getDailyRoutineId, ids)
            );
            List<Comment> commentList = commentMapper.selectList(Wrappers.<Comment>lambdaQuery()
                    .eq(Comment::getClassify, ClassifyEnum.DAY_NIGHT_STUDY_DUTY)
                    .in(Comment::getDailyRoutineId, ids)
                    .or().eq(Comment::getClassify, ClassifyEnum.DAY_OTHER_2)
                    .in(Comment::getDailyRoutineId, ids)
            );
            map.put("comment", commentList);
            map.put("incidentList", incidentList);
            map.put("nightStudyDutyId", leaderNightStudyDutyDtoList.get(0).getId());
        } else {
            return new HashMap<>();
        }
        leaderNightStudyDutyDtoList.forEach(leaderNightStudyDutyDto -> {
            Map<String, List<NightDutyClassDto>> stringListMap = leaderNightStudyDutyDto.getNightDutyClassDtoList().stream().collect(Collectors.groupingBy(NightDutyClassDto::getGradeName));
            List<Map<String, Object>> mapList = new ArrayList<>();
            stringListMap.keySet().forEach(key -> {
                Map<String, Object> stringListMap1 = new HashMap<>();
                stringListMap1.put("class", stringListMap.get(key));
                stringListMap1.put("gradeName", key);
                stringListMap1.put("gradeId", stringListMap.get(key).get(0).getGradeId());
                mapList.add(stringListMap1);
            });
            mapList.sort((o1, o2) -> {
                if (!o1.isEmpty() && !o2.isEmpty()) {
                    if (Integer.parseInt(o1.get("gradeId") + "") > Integer.parseInt(o2.get("gradeId") + "")) {
                        return 1;
                    }
                    if (o1.get("gradeId") == o2.get("gradeId")) {
                        return 0;
                    }
                    return -1;
                } else {
                    return 0;
                }
            });
            leaderNightStudyDutyDto.setMap(mapList);
        });
        map.put("classList", leaderNightStudyDutyDtoList);
        map.put("schoolyardId", schoolyardId);
        TeacherDutyDto teacherDutyDto = teacherDutyService.getGradeTeacherDuty(time);
        map.put("totalDutyTeacher", teacherDutyDto.getTotalDutyTeacher());
        map.put("gradeOneTeacher", teacherDutyDto.getGradeOneTeacher());
        map.put("gradeTwoTeacher", teacherDutyDto.getGradeTwoTeacher());
        map.put("gradeThreeTeacher", teacherDutyDto.getGradeThreeTeacher());

        List<FunctionDepartment> functionDepartments = functionDepartmentMapper.selectList(Wrappers.<FunctionDepartment>lambdaQuery()
                .eq(FunctionDepartment::getParentId, 1).or().eq(FunctionDepartment::getParentId, 2)
                .orderByAsc(FunctionDepartment::getSortOrder)
        );
        List<FunctionDepartment> officeDepartment = functionDepartments.stream().filter(functionDepartment -> functionDepartment.getParentId().equals(1L)).collect(Collectors.toList());
        List<FunctionDepartment> gradeDepartment = functionDepartments.stream().filter(functionDepartment -> functionDepartment.getParentId().equals(2L)).collect(Collectors.toList());

        Map<String, List<FunctionDepartment>> departmentMap = new HashMap<>();
        departmentMap.put("office", officeDepartment);
        departmentMap.put("grade", gradeDepartment);

        map.put("departmentMap", departmentMap);
        return map;
    }

    private List<NightDutyClassDto> parseStage(List<ClazzVo> stageOne, LeaderNightStudyDutyDto leaderNightStudyDutyDto, TeacherDutyTypeEnum stage, List<Long> gradeIdList) {
        //查询所有班级，没有就加入初始展示数据
        List<NightStudyDutyClazz> nightStudyDutyClazzes = new ArrayList<>();
        stageOne = stageOne.stream().filter(item -> gradeIdList.contains(item.getGradeId())).collect(Collectors.toList());
        List<NightDutyClassDto> nightDutyClassDtos = leaderNightStudyDutyDto.getNightDutyClassDtoList();
        Map<Long, List<ClazzVo>> clazzMap = stageOne.stream().collect(Collectors.groupingBy(ClazzVo::getId));
        nightDutyClassDtos = nightDutyClassDtos.stream().filter(nightDutyClassDto -> clazzMap.containsKey(nightDutyClassDto.getClazzId())).collect(Collectors.toList());

        for (ClazzVo clazzVo : stageOne) {
            if (CollectionUtils.isNotEmpty(nightDutyClassDtos)) {
                List<NightDutyClassDto> classDtos = nightDutyClassDtos.stream().filter(nightDutyClassDto ->
                        clazzVo.getId().equals(nightDutyClassDto.getClazzId())
                ).collect(Collectors.toList());
                if (classDtos.size() <= 0) {
                    NightStudyAttendance nightStudyAttendance = nightStudyAttendanceMapper.getOneByTimeAndClazzId(clazzVo.getId(), leaderNightStudyDutyDto.getStartTime(), stage.toString());
                    NightStudyDutyClazz nightStudyDutyClazz = new NightStudyDutyClazz();
                    nightStudyDutyClazz.setNightStudyDutyId(leaderNightStudyDutyDto.getId());
                    nightStudyDutyClazz.setAllStudentCount(clazzVo.getStudentNum());
                    nightStudyDutyClazz.setClazzId(clazzVo.getId());
                    nightStudyDutyClazz.setTeacher(clazzVo.getTeacherDutyName() == null ? " " : clazzVo.getTeacherDutyName());
                    if (nightStudyAttendance != null) {
                        nightStudyDutyClazz.setShouldStudentCount(nightStudyAttendance.getShouldNum());
                        nightStudyDutyClazz.setActualStudentCount(nightStudyAttendance.getActualNum());
                    } else {
                        NightStudyAttendanceSub nightStudyAttendanceSub;
                        if(Objects.equals(stage,TeacherDutyTypeEnum.STAGE_ONE)){
                            nightStudyAttendanceSub = this.nightStudyAttendanceSubMapper.selectOne(Wrappers.<NightStudyAttendanceSub>lambdaQuery()
                                    .eq(NightStudyAttendanceSub::getClazzId, clazzVo.getId())
                                    .eq(NightStudyAttendanceSub::getStage, StudentNightDutyTypeEnum.STAGE_ONE)
                                    .eq(NightStudyAttendanceSub::getIsFullAttendence, YesNoEnum.YES)
                                    .apply("to_days(register_date)" + "=" + "to_days({0})", leaderNightStudyDutyDto.getStartTime()));
                            if (nightStudyAttendanceSub == null) {
                                // 老师是否填写
                                NightStudy nightStudy = this.nightStudyMapper.getNightStudyLeader(clazzVo.getId(), leaderNightStudyDutyDto.getStartTime(), "STAGE_ONE");
                                if (nightStudy != null && nightStudy.getActualStudentCount() != 0 && nightStudy.getShouldStudentCount() != 0) {
                                    nightStudyDutyClazz.setShouldStudentCount(nightStudy.getShouldStudentCount());
                                    nightStudyDutyClazz.setActualStudentCount(nightStudy.getActualStudentCount());
                                } else {
                                    nightStudyDutyClazz.setShouldStudentCount(clazzVo.getNightStageOneNum() == null ? 0 : clazzVo.getNightStageOneNum().intValue());
                                    nightStudyDutyClazz.setActualStudentCount(0);
                                }
                            } else {
                                nightStudyDutyClazz.setShouldStudentCount(nightStudyAttendanceSub.getShouldNum());
                                nightStudyDutyClazz.setActualStudentCount(nightStudyAttendanceSub.getActualNum());
                            }
                        }else if(Objects.equals(stage,TeacherDutyTypeEnum.STAGE_TWO)){
                            nightStudyAttendanceSub = this.nightStudyAttendanceSubMapper.selectOne(Wrappers.<NightStudyAttendanceSub>lambdaQuery()
                                    .eq(NightStudyAttendanceSub::getClazzId, clazzVo.getId())
                                    .eq(NightStudyAttendanceSub::getStage, StudentNightDutyTypeEnum.STAGE_TWO)
                                    .eq(NightStudyAttendanceSub::getIsFullAttendence, YesNoEnum.YES)
                                    .apply("to_days(register_date)" + "=" + "to_days({0})", leaderNightStudyDutyDto.getStartTime()));
                            if (nightStudyAttendanceSub == null) {
                                // 老师是否填写
                                NightStudy nightStudy = this.nightStudyMapper.getNightStudyLeader(clazzVo.getId(), leaderNightStudyDutyDto.getStartTime(), "STAGE_TWO");
                                if (nightStudy != null && nightStudy.getActualStudentCount() != 0 && nightStudy.getShouldStudentCount() != 0) {
                                    nightStudyDutyClazz.setShouldStudentCount(nightStudy.getShouldStudentCount());
                                    nightStudyDutyClazz.setActualStudentCount(nightStudy.getActualStudentCount());
                                } else {
                                    nightStudyDutyClazz.setShouldStudentCount(clazzVo.getNightStageTwoNum() == null ? 0 : clazzVo.getNightStageTwoNum().intValue());
                                    nightStudyDutyClazz.setActualStudentCount(0);
                                }
                            } else {
                                nightStudyDutyClazz.setShouldStudentCount(nightStudyAttendanceSub.getShouldNum());
                                nightStudyDutyClazz.setActualStudentCount(nightStudyAttendanceSub.getActualNum());
                            }
                        }
                    }
                    nightStudyDutyClazz.setScore(100);
                    nightStudyDutyClazz.setClazzName(clazzVo.getName());
                    nightStudyDutyClazz.setGradeName(clazzVo.getGradeName());
                    nightStudyDutyClazz.setGradeId(clazzVo.getGradeId());
                    nightStudyDutyClazz.setDefault().validate(true);
                    nightStudyDutyClazzes.add(nightStudyDutyClazz);
                } else {
                    classDtos.forEach(item -> {
                        if (clazzVo.getTeacherLeaderConfirm() != null) {
                            item.setIsLeaderConfirm(clazzVo.getTeacherLeaderConfirm().split(",")[0].trim().equals("YES") ? YesNoEnum.YES : YesNoEnum.NO);
                        }
                    });
                }
            } else {
                NightStudyAttendance nightStudyAttendance = nightStudyAttendanceMapper.getOneByTimeAndClazzId(clazzVo.getId(), leaderNightStudyDutyDto.getStartTime(), stage.toString());
                NightStudyDutyClazz nightStudyDutyClazz = new NightStudyDutyClazz();
                nightStudyDutyClazz.setNightStudyDutyId(leaderNightStudyDutyDto.getId());
                nightStudyDutyClazz.setAllStudentCount(clazzVo.getStudentNum());
                nightStudyDutyClazz.setClazzId(clazzVo.getId());
                nightStudyDutyClazz.setTeacher(clazzVo.getTeacherDutyName() == null ? " " : clazzVo.getTeacherDutyName());
                if (nightStudyAttendance != null) {
                    nightStudyDutyClazz.setShouldStudentCount(nightStudyAttendance.getShouldNum());
                    nightStudyDutyClazz.setActualStudentCount(nightStudyAttendance.getActualNum());
                } else {
                    NightStudyAttendanceSub nightStudyAttendanceSub;
                    if(Objects.equals(stage,TeacherDutyTypeEnum.STAGE_ONE)){
                        nightStudyAttendanceSub = this.nightStudyAttendanceSubMapper.selectOne(Wrappers.<NightStudyAttendanceSub>lambdaQuery()
                                .eq(NightStudyAttendanceSub::getClazzId, clazzVo.getId())
                                .eq(NightStudyAttendanceSub::getStage, StudentNightDutyTypeEnum.STAGE_ONE)
                                .eq(NightStudyAttendanceSub::getIsFullAttendence, YesNoEnum.YES)
                                .apply("to_days(register_date)" + "=" + "to_days({0})", leaderNightStudyDutyDto.getStartTime()));
                        if (nightStudyAttendanceSub == null) {
                            // 老师是否填写
                            NightStudy nightStudy = this.nightStudyMapper.getNightStudyLeader(clazzVo.getId(), leaderNightStudyDutyDto.getStartTime(), "STAGE_ONE");
                            if (nightStudy != null && nightStudy.getActualStudentCount() != 0 && nightStudy.getShouldStudentCount() != 0) {
                                nightStudyDutyClazz.setShouldStudentCount(nightStudy.getShouldStudentCount());
                                nightStudyDutyClazz.setActualStudentCount(nightStudy.getActualStudentCount());
                            } else {
                                nightStudyDutyClazz.setShouldStudentCount(clazzVo.getNightStageOneNum() == null ? 0 : clazzVo.getNightStageOneNum().intValue());
                                nightStudyDutyClazz.setActualStudentCount(0);
                            }
                        } else {
                            nightStudyDutyClazz.setShouldStudentCount(nightStudyAttendanceSub.getShouldNum());
                            nightStudyDutyClazz.setActualStudentCount(nightStudyAttendanceSub.getActualNum());
                        }
                    }else if(Objects.equals(stage,TeacherDutyTypeEnum.STAGE_TWO)){
                        nightStudyAttendanceSub = this.nightStudyAttendanceSubMapper.selectOne(Wrappers.<NightStudyAttendanceSub>lambdaQuery()
                                .eq(NightStudyAttendanceSub::getClazzId, clazzVo.getId())
                                .eq(NightStudyAttendanceSub::getStage, StudentNightDutyTypeEnum.STAGE_TWO)
                                .eq(NightStudyAttendanceSub::getIsFullAttendence, YesNoEnum.YES)
                                .apply("to_days(register_date)" + "=" + "to_days({0})", leaderNightStudyDutyDto.getStartTime()));
                        if (nightStudyAttendanceSub == null) {
                            // 老师是否填写
                            NightStudy nightStudy = this.nightStudyMapper.getNightStudyLeader(clazzVo.getId(), leaderNightStudyDutyDto.getStartTime(), "STAGE_TWO");
                            if (nightStudy != null && nightStudy.getActualStudentCount() != 0 && nightStudy.getShouldStudentCount() != 0) {
                                nightStudyDutyClazz.setShouldStudentCount(nightStudy.getShouldStudentCount());
                                nightStudyDutyClazz.setActualStudentCount(nightStudy.getActualStudentCount());
                            } else {
                                nightStudyDutyClazz.setShouldStudentCount(clazzVo.getNightStageTwoNum() == null ? 0 : clazzVo.getNightStageTwoNum().intValue());
                                nightStudyDutyClazz.setActualStudentCount(0);
                            }
                        } else {
                            nightStudyDutyClazz.setShouldStudentCount(nightStudyAttendanceSub.getShouldNum());
                            nightStudyDutyClazz.setActualStudentCount(nightStudyAttendanceSub.getActualNum());
                        }
                    }
                }
                nightStudyDutyClazz.setScore(100);
                nightStudyDutyClazz.setClazzName(clazzVo.getName());
                nightStudyDutyClazz.setGradeName(clazzVo.getGradeName());
                nightStudyDutyClazz.setGradeId(clazzVo.getGradeId());
                nightStudyDutyClazz.setDefault().validate(true);
                nightStudyDutyClazzes.add(nightStudyDutyClazz);
            }
        }
        if (CollectionUtils.isNotEmpty(nightStudyDutyClazzes)) {
            this.nightStudyDutyClazzMapper.batchInsertWithId(nightStudyDutyClazzes);
            for (NightStudyDutyClazz nightStudyDutyClazz : nightStudyDutyClazzes) {
                NightDutyClassDto nightDutyClassDto = new NightDutyClassDto();
                BeanUtils.copyProperties(nightStudyDutyClazz, nightDutyClassDto);
                nightDutyClassDto.setNightStudyDutyClazzId(nightStudyDutyClazz.getId());
                nightDutyClassDto.setTeacherName(nightStudyDutyClazz.getTeacher());
                nightDutyClassDto.setClazzName(nightStudyDutyClazz.getClazzName());
                nightDutyClassDto.setGradeName(nightStudyDutyClazz.getGradeName());
                nightDutyClassDto.setGradeId(nightStudyDutyClazz.getGradeId());
                nightDutyClassDto.setIsLeaderConfirm(YesNoEnum.NO);
                nightDutyClassDtos.add(nightDutyClassDto);
            }
        }
        leaderNightStudyDutyDto.setNightDutyClassDtoList(nightDutyClassDtos);
        return nightDutyClassDtos;
    }

    private TeacherDutyGradeTotalDto getInfo(Date time, Long staffId) {
        return this.teacherDutyMapper.getTeacherDutyGradeTotalDto(time, TeacherDutyTypeEnum.GRADE_TOTAL_DUTY.toString(), staffId);
    }

    @Override
    @Transactional
    public Object updateTeacherDuty(Long id) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Staff staff = user.getStaff();
        TeacherDuty teacherDuty = this.teacherDutyMapper.selectById(id);
        if (teacherDuty == null || teacherDuty.getTeacherId().equals(staff.getId())) {
            throw new ApiCode.ApiException(-5, "未查询到教师值班或您无法替自己带班");
        }
        if (!TeacherDutyModeEnum.HOLIDAY.equals(teacherDuty.getDutyMode())) {
            throw new ApiCode.ApiException(-5, "非假期模式 无法带班");
        }
        if (!TeacherDutyTypeEnum.GRADE_TOTAL_DUTY.equals(teacherDuty.getDutyType())) {
            throw new ApiCode.ApiException(-5, "只能带值年级总值班");
        }

        TeacherDuty teacherDuty1 = this.teacherDutyMapper.getByClazz(staff.getId(), teacherDuty.getStartTime(), TeacherDutyTypeEnum.GRADE_TOTAL_DUTY.toString());
        if (teacherDuty1 == null) {
            teacherDuty1 = new TeacherDuty();
            teacherDuty1.setDutyMode(TeacherDutyModeEnum.HOLIDAY);
            teacherDuty1.setTeacherId(staff.getId());
            teacherDuty1.setStartTime(teacherDuty.getStartTime());
            teacherDuty1.setEndTime(teacherDuty.getEndTime());
            teacherDuty1.setDutyType(teacherDuty.getDutyType());
            teacherDuty1.setDefault().validate(true);
            this.teacherDutyMapper.insert(teacherDuty1);
        } else {
            List<TeacherDutyClazz> teacherDutyClazzes = teacherDutyClazzMapper.selectList(Wrappers.<TeacherDutyClazz>lambdaQuery()
                    .eq(TeacherDutyClazz::getTeacherDutyId, teacherDuty1.getId()));
            if (CollectionUtils.isNotEmpty(teacherDutyClazzes)) {
                throw new ApiCode.ApiException(-5, "您当天已有值班，无法带班！");
            }
        }
        teacherDutyClazzMapper.update(new TeacherDutyClazz(), Wrappers.<TeacherDutyClazz>lambdaUpdate()
                .set(TeacherDutyClazz::getTeacherDutyId, teacherDuty1.getId())
                .eq(TeacherDutyClazz::getTeacherDutyId, teacherDuty.getId())
        );
        teacherDutySubstituteMapper.update(new TeacherDutySubstitute(), Wrappers.<TeacherDutySubstitute>lambdaUpdate()
                .set(TeacherDutySubstitute::getTeacherDutyId, teacherDuty1.getId())
                .eq(TeacherDutySubstitute::getTeacherDutyId, teacherDuty.getId())
        );
        TeacherDutySubstitute teacherDutySubstitute = new TeacherDutySubstitute();
        teacherDutySubstitute.setTeacherDutyId(teacherDuty1.getId());
        teacherDutySubstitute.setTeacherId(staff.getId());
        teacherDutySubstitute.setTeacherOldId(teacherDuty.getTeacherId());
        return teacherDutySubstituteMapper.insert(teacherDutySubstitute);
    }

    @Override
    @Transactional
    public Object cancelTeacherDuty(Long teacherDutyId, Long teacherId) {
        TeacherDuty teacherDuty = this.teacherDutyMapper.selectById(teacherDutyId);

        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Staff staff = user.getStaff();

        TeacherDutyGradeTotalDto teacherDutyGradeTotalDto = this.getInfo(teacherDuty.getStartTime(), staff.getId());
        if (teacherDutyGradeTotalDto == null || CollectionUtils.isEmpty(teacherDutyGradeTotalDto.getTeacherDutyGradeTotalClazzList())) {
            throw new ApiCode.ApiException(-5, "无值班不能取消！");
        }

        TeacherDuty teacherDuty1 = this.teacherDutyMapper.getByClazz(teacherId, teacherDuty.getStartTime(), TeacherDutyTypeEnum.GRADE_TOTAL_DUTY.toString());
        if (teacherDuty1 == null) {
            teacherDuty1 = new TeacherDuty();
            teacherDuty1.setTeacherId(staff.getId());
            teacherDuty1.setDutyMode(TeacherDutyModeEnum.HOLIDAY);
            teacherDuty1.setStartTime(teacherDutyGradeTotalDto.getStartTime());
            teacherDuty1.setEndTime(teacherDutyGradeTotalDto.getEndTime());
            teacherDuty1.setDutyType(teacherDutyGradeTotalDto.getDutyType());
            teacherDuty1.setDefault().validate(true);
            this.teacherDutyMapper.insert(teacherDuty1);
        }
        this.teacherDutySubstituteMapper.delete(Wrappers
                .<TeacherDutySubstitute>lambdaQuery()
                .eq(TeacherDutySubstitute::getTeacherDutyId, teacherDutyId)
                .eq(TeacherDutySubstitute::getTeacherId, staff.getId()));
        return teacherDutyClazzMapper.update(new TeacherDutyClazz(), Wrappers.<TeacherDutyClazz>lambdaUpdate()
                .set(TeacherDutyClazz::getTeacherDutyId, teacherDuty1.getId())
                .eq(TeacherDutyClazz::getTeacherDutyId, teacherDutyId));
    }

    @Override
    public List<Staff> cancelTeacherList(Long teacherDutyId) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        TeacherDutySubstitute teacherDutySubstitute = teacherDutySubstituteMapper.selectOne(Wrappers.<TeacherDutySubstitute>lambdaQuery()
                .eq(TeacherDutySubstitute::getTeacherDutyId, teacherDutyId)
                .eq(TeacherDutySubstitute::getTeacherId, user.getStaffId())
                .orderByDesc(TeacherDutySubstitute::getId));
        List<TeacherDutySubstitute> teacherDutySubstitutes = teacherDutySubstituteMapper.selectList(Wrappers.<TeacherDutySubstitute>lambdaQuery()
                .eq(TeacherDutySubstitute::getTeacherDutyId, teacherDutyId)
                .le(TeacherDutySubstitute::getId, teacherDutySubstitute.getId()));
        List<Long> ids = new ArrayList<>();
        teacherDutySubstitutes.forEach(item -> {
            if (!item.getTeacherOldId().equals(user.getStaffId())) {
                ids.add(item.getTeacherOldId());
            }
        });
        return this.staffMapper.selectBatchIds(ids);
    }

    @Override
    public List<TeacherDutyGradeTotalDto> updateTeacherDutyList(Date timeFrom, Date timeTo) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        List<TeacherDutyGradeTotalDto> teacherDutyGradeTotalDtoList = this.teacherDutyMapper.getTeacherDutyGradeTotalDtoList(timeFrom, timeTo, TeacherDutyTypeEnum.GRADE_TOTAL_DUTY.toString());
        teacherDutyGradeTotalDtoList = teacherDutyGradeTotalDtoList.stream().filter(item ->
                !CollectionUtils.isEmpty(item.getTeacherDutyGradeTotalClazzList())
                        && !item.getTeacherId().equals(user.getStaff().getId())
                        && TeacherDutyModeEnum.HOLIDAY.equals(item.getDutyMode())
        ).sorted(Comparator.comparing(TeacherDuty::getStartTime)).collect(Collectors.toList());
        teacherDutyGradeTotalDtoList.forEach(item -> {
            List<TeacherDutyClazz> list = new ArrayList<>();
            list.add(item.getTeacherDutyGradeTotalClazzList().get(0));
            item.setTeacherDutyGradeTotalClazzList(list);
        });
        return teacherDutyGradeTotalDtoList;
    }

    @Override
    public Map<String, Object> nightRoutineCommentAndIncident(LeaderDutyTypeEnum dutyType, Date time) {
        Map<String, Object> map = new HashMap<>();
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        TeacherDutyGradeTotalDto teacherDutyGradeTotalDto = this.getInfo(time, user.getStaffId());
        if (teacherDutyGradeTotalDto == null
                || teacherDutyGradeTotalDto.getTeacherDutyGradeTotalClazzList() == null) {
            return map;
        }
        NightStudyDuty nightStudyDutyStageOne = this.getCorrespondLeaderNightStudyDuty(teacherDutyGradeTotalDto.getSchoolyardId(), time);
        if (nightStudyDutyStageOne != null) {
            LeaderDuty leaderDuty = nightStudyDutyStageOne.getLeaderDuty();
            List<LeaderNightStudyDutyDto> leaderNightStudyDutyDtoList = this.nightStudyDutyMapper.nightRoutine(time, "DAY", leaderDuty.getLeaderId());
            if (CollectionUtils.isNotEmpty(leaderNightStudyDutyDtoList)) {
                List<Long> ids = leaderNightStudyDutyDtoList.stream().map(studyDuty -> studyDuty.getId()).collect(Collectors.toList());
                List<Incident> incidentList = incidentMapper.selectList(Wrappers.<Incident>lambdaQuery()
                        .eq(Incident::getClassify, ClassifyEnum.DAY_NIGHT_STUDY_DUTY)
                        .in(Incident::getDailyRoutineId, ids)
                        .or().eq(Incident::getClassify, ClassifyEnum.DAY_OTHER_2)
                        .in(Incident::getDailyRoutineId, ids)
                );
                List<CommentDto> commentList = commentMapper.selectListDto(Wrappers.<Comment>lambdaQuery()
                        .eq(Comment::getClassify, ClassifyEnum.DAY_NIGHT_STUDY_DUTY)
                        .in(Comment::getDailyRoutineId, ids)
                        .or().eq(Comment::getClassify, ClassifyEnum.DAY_OTHER_2)
                        .in(Comment::getDailyRoutineId, ids)
                );
                commentList.forEach(commentDto -> {
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

                map.put("comment", commentList);
                map.put("incidentList", incidentList);
            }
        }
        return map;
    }

    @Override
    public IPage<TeacherDutyGradeTotalSubstitueDto> searchMyLogPage(IPage<TeacherDutyGradeTotalSubstitueDto> page, TeacherDutySubstituteParam param, Boolean bool) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Staff staff = staffMapper.selectById(user.getStaffId());
        if (bool == null) {
            param.setTeacherOldId(staff.getId());
            param.setTeacherId(staff.getId());
        } else if (bool) {
            param.setTeacherId(staff.getId());
        } else {
            param.setTeacherOldId(staff.getId());
        }
        List<TeacherDutyGradeTotalSubstitueDto> teacherDutySubstituteDtoList = this.teacherDutySubstituteMapper.searchMyLogPageGradeTotal(page, param);
        teacherDutySubstituteDtoList = teacherDutySubstituteDtoList
                .stream()
                .peek(item -> item.setContent(item.getTeacherDutyGradeTotalClazzList().stream().map(item1 -> item1.getClazz().getGrade().getName()).collect(Collectors.joining(", "))))
                .collect(Collectors.toList());
        page.setRecords(teacherDutySubstituteDtoList);
        return page;
    }
}
