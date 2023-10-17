/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：晚自习行政值班表
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
import com.zhzx.server.dto.LeaderNightStudyDutyDto;
import com.zhzx.server.dto.NightDutyClassDto;
import com.zhzx.server.dto.TeacherDutyDto;
import com.zhzx.server.enums.*;
import com.zhzx.server.repository.*;
import com.zhzx.server.repository.base.NightStudyDutyBaseMapper;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.GradeService;
import com.zhzx.server.service.NightStudyDutyService;
import com.zhzx.server.service.TeacherDutyService;
import com.zhzx.server.vo.ClazzVo;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NightStudyDutyServiceImpl extends ServiceImpl<NightStudyDutyMapper, NightStudyDuty> implements NightStudyDutyService {
    @Value("${web.upload-path}")
    private String uploadPath;
    @Resource
    private StaffMapper staffMapper;
    @Resource
    private NightStudyMapper nightStudyMapper;
    @Resource
    private GradeService gradeService;
    @Resource
    private IncidentMapper incidentMapper;
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private TeacherDutyService teacherDutyService;
    @Resource
    private FunctionDepartmentMapper functionDepartmentMapper;
    @Resource
    private NightStudyDutyClazzMapper nightStudyDutyClazzMapper;
    @Resource
    private NightStudyAttendanceMapper  nightStudyAttendanceMapper;
    @Resource
    private NightStudyAttendanceSubMapper nightStudyAttendanceSubMapper;
    @Resource
    private CourseTimeMapper courseTimeMapper;
    @Override
    public int updateAllFieldsById(NightStudyDuty entity) {
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
    public boolean saveBatch(Collection<NightStudyDuty> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(NightStudyDutyBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    @Override
    @Transactional
    public  Map<String,Object> nightStudyDuty(Date time, RoutineEnum type) {
        Map<String,Object> map = new HashMap<>();

        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Staff staff = staffMapper.selectById(user.getStaffId());
        List<LeaderNightStudyDutyDto> leaderNightStudyDutyDtoList = this.baseMapper.nightRoutine(time,type.toString(),staff.getId());
        if(CollectionUtils.isNotEmpty(leaderNightStudyDutyDtoList) ){
            Long schoolyardId = leaderNightStudyDutyDtoList.get(0).getSchoolyard().getId();
            map.put("schoolyardId",schoolyardId);
            //两个晚自习
            if(leaderNightStudyDutyDtoList.size() != 2) throw new ApiCode.ApiException(-5,"阶段不匹配，请确认！");
            List<ClazzVo> stageOne = gradeService.getGradeList(time,TeacherDutyTypeEnum.STAGE_ONE);
            stageOne = stageOne.stream().filter(item -> item.getSchoolyardId().equals(schoolyardId)).collect(Collectors.toList());
            parseStage(stageOne,leaderNightStudyDutyDtoList.get(0),TeacherDutyTypeEnum.STAGE_ONE);
            List<ClazzVo> stageTwo = gradeService.getGradeList(time,TeacherDutyTypeEnum.STAGE_TWO);
            stageTwo = stageTwo.stream().filter(item -> item.getSchoolyardId().equals(schoolyardId)).collect(Collectors.toList());
            parseStage(stageTwo,leaderNightStudyDutyDtoList.get(1),TeacherDutyTypeEnum.STAGE_TWO);
            List<Long> ids =  leaderNightStudyDutyDtoList.stream().map(studyDuty->studyDuty.getId()).collect(Collectors.toList());

            List<Incident> incidentList = incidentMapper.selectList(Wrappers.<Incident>lambdaQuery()
                    .eq(Incident::getClassify, ClassifyEnum.DAY_NIGHT_STUDY_DUTY)
                    .in(Incident::getDailyRoutineId,ids)
                    .or().eq(Incident::getClassify, ClassifyEnum.DAY_OTHER_2)
                    .in(Incident::getDailyRoutineId,ids)
            );
            List<Comment> commentList = commentMapper.selectList(Wrappers.<Comment>lambdaQuery()
                    .eq(Comment::getClassify, ClassifyEnum.DAY_NIGHT_STUDY_DUTY)
                    .in(Comment::getDailyRoutineId, ids)
                    .or().eq(Comment::getClassify, ClassifyEnum.DAY_OTHER_2)
                    .in(Comment::getDailyRoutineId,ids)
            );
            map.put("comment",commentList);
            map.put("incidentList",incidentList);
            map.put("nightStudyDutyId", leaderNightStudyDutyDtoList.get(0).getId());
            // 插入所查年级的最晚结束时间
            List<Long> gradeIdList = stageTwo.stream().map(ClazzVo::getGradeId).distinct().collect(Collectors.toList());
            List<Map<String, Object>> courseTime = this.courseTimeMapper.selectMaps(
                    new QueryWrapper<CourseTime>()
                            .select("max(end_time) as endTime")
                            .eq("sort_order", 12)
                            .in("grade_id", gradeIdList)
            );
            map.put("endTime", courseTime.get(0).get("endTime"));
        }else {
            // 插入最晚结束时间
            List<Map<String, Object>> courseTime = this.courseTimeMapper.selectMaps(
                    new QueryWrapper<CourseTime>().select("max(end_time) as endTime").eq("sort_order", 12)
            );
            map.put("endTime", courseTime.get(0).get("endTime"));
            return map;
        }
        leaderNightStudyDutyDtoList.stream().forEach(leaderNightStudyDutyDto -> {
            Map<String,List<NightDutyClassDto>> stringListMap = leaderNightStudyDutyDto.getNightDutyClassDtoList().stream().collect(Collectors.groupingBy(NightDutyClassDto::getGradeName));
            List<Map<String,Object>> mapList = new ArrayList<>();
            stringListMap.keySet().stream().forEach(key->{
                Map<String,Object> stringListMap1 = new HashMap<>();
                stringListMap1.put("class",stringListMap.get(key));
                stringListMap1.put("gradeName",key);
                stringListMap1.put("gradeId",stringListMap.get(key).get(0).getGradeId());
                mapList.add(stringListMap1);
            });
            Collections.sort(mapList, new Comparator<Map<String, Object>>() {
                        @Override
                        public int compare(Map<String, Object> o1, Map<String, Object> o2) {
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
                        }
                    });
            leaderNightStudyDutyDto.setMap(mapList);
        });
        map.put("classList",leaderNightStudyDutyDtoList);
        TeacherDutyDto teacherDutyDto = teacherDutyService.getGradeTeacherDuty(time);
        map.put("totalDutyTeacher",teacherDutyDto.getTotalDutyTeacher());
        map.put("gradeOneTeacher",teacherDutyDto.getGradeOneTeacher());
        map.put("gradeTwoTeacher",teacherDutyDto.getGradeTwoTeacher());
        map.put("gradeThreeTeacher",teacherDutyDto.getGradeThreeTeacher());

        List<FunctionDepartment> functionDepartments = functionDepartmentMapper.selectList(Wrappers.<FunctionDepartment>lambdaQuery()
                .eq(FunctionDepartment::getParentId,1).or().eq(FunctionDepartment::getParentId,2)
                .orderByAsc(FunctionDepartment::getSortOrder)
        );
        List<FunctionDepartment> officeDepartment = functionDepartments.stream().filter(functionDepartment -> functionDepartment.getParentId().equals(1L)).collect(Collectors.toList());
        List<FunctionDepartment> gradeDepartment = functionDepartments.stream().filter(functionDepartment -> functionDepartment.getParentId().equals(2L)).collect(Collectors.toList());

        Map<String,List<FunctionDepartment>> departmentMap = new HashMap<>();
        departmentMap.put( "office",officeDepartment);
        departmentMap.put( "grade",gradeDepartment);

        map.put("departmentMap",departmentMap);

        return map;
    }

    private List<NightDutyClassDto> parseStage(List<ClazzVo> stageOne,LeaderNightStudyDutyDto leaderNightStudyDutyDto,TeacherDutyTypeEnum stage){
        //查询所有班级，没有就加入初始展示数据
        List<NightStudyDutyClazz> nightStudyDutyClazzes = new ArrayList<>();
        List<NightDutyClassDto> nightDutyClassDtos = leaderNightStudyDutyDto.getNightDutyClassDtoList();
        Map<Long,List<ClazzVo>> clazzMap = stageOne.stream().collect(Collectors.groupingBy(ClazzVo::getId));
        nightDutyClassDtos = nightDutyClassDtos.stream().filter(nightDutyClassDto -> clazzMap.containsKey(nightDutyClassDto.getClazzId())).collect(Collectors.toList());

        for (ClazzVo clazzVo:stageOne) {
            if(CollectionUtils.isNotEmpty(nightDutyClassDtos)){
               List<NightDutyClassDto> classDtos =  nightDutyClassDtos.stream().filter(nightDutyClassDto ->
                        clazzVo.getId().equals(nightDutyClassDto.getClazzId())
                ).collect(Collectors.toList());
               if(classDtos == null || classDtos.size()  <= 0){
                   NightStudyAttendance nightStudyAttendance = nightStudyAttendanceMapper.getOneByTimeAndClazzId(clazzVo.getId(),leaderNightStudyDutyDto.getStartTime(),stage.toString());
                   NightStudyDutyClazz nightStudyDutyClazz = new NightStudyDutyClazz();
                   nightStudyDutyClazz.setNightStudyDutyId(leaderNightStudyDutyDto.getId());
                   nightStudyDutyClazz.setClazzId(clazzVo.getId());
                   nightStudyDutyClazz.setAllStudentCount(clazzVo.getStudentNum());
                   nightStudyDutyClazz.setTeacher(clazzVo.getTeacherDutyName() == null ? " ":clazzVo.getTeacherDutyName());
                   if(nightStudyAttendance != null){
                       nightStudyDutyClazz.setShouldStudentCount(nightStudyAttendance.getShouldNum());
                       nightStudyDutyClazz.setActualStudentCount(nightStudyAttendance.getActualNum());
                   }else{
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
               }else{
                   classDtos.stream().forEach(item->{
                       if(clazzVo.getTeacherLeaderConfirm() != null){
                           item.setIsLeaderConfirm(clazzVo.getTeacherLeaderConfirm().split(",")[0].trim().equals("YES") ? YesNoEnum.YES:YesNoEnum.NO);
                       }
                   });
               }
            }else{
                NightStudyAttendance nightStudyAttendance = nightStudyAttendanceMapper.getOneByTimeAndClazzId(clazzVo.getId(),leaderNightStudyDutyDto.getStartTime(),stage.toString());
                NightStudyDutyClazz nightStudyDutyClazz = new NightStudyDutyClazz();
                nightStudyDutyClazz.setNightStudyDutyId(leaderNightStudyDutyDto.getId());
                nightStudyDutyClazz.setAllStudentCount(clazzVo.getStudentNum());
                nightStudyDutyClazz.setClazzId(clazzVo.getId());
                nightStudyDutyClazz.setTeacher(clazzVo.getTeacherDutyName() == null ? " ":clazzVo.getTeacherDutyName());
                if(nightStudyAttendance != null){
                    nightStudyDutyClazz.setShouldStudentCount(nightStudyAttendance.getShouldNum());
                    nightStudyDutyClazz.setActualStudentCount(nightStudyAttendance.getActualNum());
                }else{
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
        if(CollectionUtils.isNotEmpty(nightStudyDutyClazzes)){
            nightStudyDutyClazzMapper.batchInsertWithId(nightStudyDutyClazzes);
            for (NightStudyDutyClazz nightStudyDutyClazz:nightStudyDutyClazzes) {
                NightDutyClassDto nightDutyClassDto = new NightDutyClassDto();
                BeanUtils.copyProperties(nightStudyDutyClazz,nightDutyClassDto);
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

    @Override
    public Map<String, Object> selectNightStudyDutyClazzTeacher(Date time) {
        if(time == null){
            time = new Date();
        }
        Map<String, Object> map = new HashMap<>();

        List<ClazzVo> stageOne = gradeService.getGradeList(time,TeacherDutyTypeEnum.STAGE_ONE);
        stageOne = stageOne.stream().filter(item->item.getTeacherLeaderConfirm() != null && item.getTeacherDutyName() != null).collect(Collectors.toList());
        Map<Object,Object> stageOneMap = new HashMap<>();
        stageOne.stream().forEach(item->{
            Map<String,Object> map1 = new HashMap<>();
            map1.put("name",item.getTeacherDutyName());
            map1.put("confirm",item.getTeacherLeaderConfirm().split(",")[1]);
            stageOneMap.put(item.getId(),map1);
        });

        Map<Object,Object> stageTwoMap = new HashMap<>();
        List<ClazzVo> stageTwo = gradeService.getGradeList(time,TeacherDutyTypeEnum.STAGE_TWO);
        stageTwo = stageTwo.stream().filter(item->item.getTeacherLeaderConfirm() != null && item.getTeacherDutyName() != null).collect(Collectors.toList());
        stageTwo.stream().forEach(item->{
            Map<String,Object> map1 = new HashMap<>();
            map1.put("name",item.getTeacherDutyName());
            map1.put("confirm",item.getTeacherLeaderConfirm().split(",")[1]);
            stageTwoMap.put(item.getId(),map1);
        });
        map.put("STAGE_ONE",stageOneMap);
        map.put("STAGE_TWO",stageTwoMap);
        return map;
    }

    @Override
    public Map<String, Object> selectNightStudyDutyClazzConfirm(Date time) {
        if(time == null){
            time = new Date();
        }
        Map<String, Object> map = new HashMap<>();

        List<ClazzVo> stageOne = gradeService.getGradeList(time,TeacherDutyTypeEnum.STAGE_ONE);
        stageOne = stageOne.stream().filter(item->item.getTeacherLeaderConfirm() != null && item.getTeacherDutyName() != null).collect(Collectors.toList());
        Map<Object,Object> stageOneMap = new HashMap<>();
        stageOne.stream().forEach(item->{
            stageOneMap.put(item.getId(),item.getTeacherLeaderConfirm().split(",")[0]);
        });

        List<ClazzVo> stageTwo = gradeService.getGradeList(time,TeacherDutyTypeEnum.STAGE_TWO);
        stageTwo = stageTwo.stream().filter(item->item.getTeacherLeaderConfirm() != null && item.getTeacherDutyName() != null).collect(Collectors.toList());
        Map<Object,Object> stageTwoMap = new HashMap<>();
        stageTwo.stream().forEach(item->{
            stageTwoMap.put(item.getId(),item.getTeacherLeaderConfirm().split(",")[0]);
        });
        map.put("STAGE_ONE",stageOneMap);
        map.put("STAGE_TWO",stageTwoMap);
        return map;
    }

    @Override
    public Map<String, Object> getNightRoutineComment(Date time) {
        Map<String, Object> map = new HashMap<>();
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Staff staff = staffMapper.selectById(user.getStaffId());
        List<LeaderNightStudyDutyDto> leaderNightStudyDutyDtoList = this.baseMapper.nightRoutine(time,"DAY",staff.getId());
        if(CollectionUtils.isNotEmpty(leaderNightStudyDutyDtoList) ){
            List<Long> ids =  leaderNightStudyDutyDtoList.stream().map(studyDuty->studyDuty.getId()).collect(Collectors.toList());
            List<Incident> incidentList = incidentMapper.selectList(Wrappers.<Incident>lambdaQuery()
                    .eq(Incident::getClassify, ClassifyEnum.DAY_NIGHT_STUDY_DUTY)
                    .in(Incident::getDailyRoutineId,ids)
                    .or().eq(Incident::getClassify, ClassifyEnum.DAY_OTHER_2)
                    .in(Incident::getDailyRoutineId,ids)
            );
            List<Comment> commentList = commentMapper.selectList(Wrappers.<Comment>lambdaQuery()
                    .eq(Comment::getClassify, ClassifyEnum.DAY_NIGHT_STUDY_DUTY)
                    .in(Comment::getDailyRoutineId, ids)
                    .or().eq(Comment::getClassify, ClassifyEnum.DAY_OTHER_2)
                    .in(Comment::getDailyRoutineId,ids)
            );
            map.put("comment",commentList);
            map.put("incidentList",incidentList);
            return map;
        }else {
            return new HashMap<>();
        }
    }

    @Override
    public List<LeaderNightStudyDutyDto> getDetail(Date time, Long clazzId){
        return this.baseMapper.getDetail(time,clazzId);
    }
}
