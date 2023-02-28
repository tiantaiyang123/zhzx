/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest;


import com.zhzx.server.dto.DayRoutineDto;
import com.zhzx.server.dto.NightDutyClassDto;
import com.zhzx.server.enums.LeaderDutyTypeEnum;
import com.zhzx.server.enums.RoutineEnum;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.LeaderDutyService;
import com.zhzx.server.service.NightStudyDutyService;
import com.zhzx.server.service.TeacherDutyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@Api(tags = "DayRoutineController", description = "一日常规管理")
@RequestMapping("/v1/day/routine")
public class DayRoutineController {
    @Resource
    private LeaderDutyService leaderDutyService;
    @Resource
    private TeacherDutyService teacherDutyService;
    @Resource
    private NightStudyDutyService nightStudyDutyService;

    /**
     * 通过当前登录人员id
     *\
     * @return 一日常规数据
     */
    @GetMapping("/dayRoutine")
    @ApiOperation("通过当前登录人员,查询常规数据")
    public ApiResponse<DayRoutineDto> selectLeaderDayDuty(@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam("time") Date time,
                                             @RequestParam(value = "type",defaultValue = "DAY") RoutineEnum type) {

        return ApiResponse.ok(leaderDutyService.dayRoutine(time,type));
    }


    /**
     * 通过当前登录人员id
     *
     * @return 查询晚班数据
     */
    @GetMapping("/nightRoutine/leader")
    @ApiOperation("查询领导晚班数据")
    public ApiResponse<Map<String,Object>> selectNightStudyDuty(@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam("time") Date time,
                                             @RequestParam(value = "type",defaultValue = "DAY") RoutineEnum type) {
        Map<String,Object> map = nightStudyDutyService.nightStudyDuty(time,type);
        return ApiResponse.ok(map);
    }

    /**
     *
     * @return 查询领导晚班班级老师数据
     */
    @GetMapping("/nightRoutine/clazz/teacher")
    @ApiOperation("查询领导晚班班级老师数据")
    public ApiResponse<Map<String,Object>> selectNightStudyDutyClazzTeacher(@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam(value = "time",required = false) Date time) {

        Map<String,Object> map = nightStudyDutyService.selectNightStudyDutyClazzTeacher(time);
        return ApiResponse.ok(map);
    }
    /**
     *
     * @return 查询领导晚班班级老师数据
     */
    @GetMapping("/nightRoutine/clazz/confirm")
    @ApiOperation("查询领导晚班班级确认情况")
    public ApiResponse<Map<String,Object>> selectNightStudyDutyClazzConfirm(@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam(value = "time",required = false) Date time) {

        Map<String,Object> map = nightStudyDutyService.selectNightStudyDutyClazzConfirm(time);
        return ApiResponse.ok(map);
    }

    /**
     * 通过当前登录人员id
     *
     * @return 查询晚班数据
     */
    @GetMapping("/nightRoutine/teacher")
    @ApiOperation("查询值班老师晚班数据")
    public ApiResponse<Object> selectTeacherNightStudyDuty(@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam("time") Date time,
                                                           @RequestParam(value = "type",defaultValue = "DAY") RoutineEnum type) {

        Map<Object,Object> teacherDutyDtoMap = teacherDutyService.nightRoutine(time,type);
        return ApiResponse.ok(teacherDutyDtoMap);
    }

    /**
     * 通过当前登录人员id
     *
     * @return 查询晚班数据
     */
    @PostMapping("/nightRoutine/teacher/confirm")
    @ApiOperation("值班老师确认按钮")
    public ApiResponse<Integer> nightStudyConfirm(@RequestBody NightDutyClassDto nightDutyClassDto) {
        Integer count = teacherDutyService.nightStudyConfirm(nightDutyClassDto);
        return ApiResponse.ok(count);
    }

    /**
     * 通过当前登录人员id
     *
     * @return 查询晚班数据
     */
    @GetMapping("/get/dayRoutine/commentAndIncident")
    @ApiOperation("获取comment/incident数据")
    public ApiResponse<Map<String,Object>> getDayRoutineComment(@RequestParam LeaderDutyTypeEnum dutyType,
                                                                @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam Date time ) {
        if(LeaderDutyTypeEnum.ROUTINE.equals(dutyType)){
            return ApiResponse.ok(leaderDutyService.getDayRoutineComment(time));
        }else if(LeaderDutyTypeEnum.NIGHT_STUDY.equals(dutyType)){
            return ApiResponse.ok(nightStudyDutyService.getNightRoutineComment(time));
        }
        return ApiResponse.ok(new HashMap<>());
    }

    /**
     *
     * @return 白班一键确定
     */
    @PostMapping("/dayRoutine/click/finish")
    @ApiOperation("白班一键确定")
    public ApiResponse<Map<String,Object>> dayRoutineClickFinish(@RequestParam("type") String type,
                                                                 @RequestParam("leaderDutyId") Long leaderDutyId) {
        return ApiResponse.ok(leaderDutyService.dayRoutineClickFinish(type,leaderDutyId));
    }
}
