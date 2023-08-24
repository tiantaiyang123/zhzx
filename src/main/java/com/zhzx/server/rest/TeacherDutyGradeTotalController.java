/**
 * 项目：中华中学流程自动化管理平台
 *
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
 */

package com.zhzx.server.rest;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.NightStudyDuty;
import com.zhzx.server.domain.Staff;
import com.zhzx.server.dto.TeacherDutyGradeTotalDto;
import com.zhzx.server.dto.TeacherDutyGradeTotalSubstitueDto;
import com.zhzx.server.enums.LeaderDutyTypeEnum;
import com.zhzx.server.enums.RoutineEnum;
import com.zhzx.server.rest.req.TeacherDutySubstituteParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.TeacherDutyGradeTotalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Api(tags = "TeacherDutyGradeTotalController", description = "教师值班表管理")
@RequestMapping("/v1/day/teacher-duty-grade-total")
public class TeacherDutyGradeTotalController {
    @Resource
    private TeacherDutyGradeTotalService teacherDutyGradeTotalService;

    /**
     * 获取对应领导值班
     * @Author: yu
     * @Date: 2022/8/11 10:43
     */
    @GetMapping("/get/correspond-leader-study-duty")
    @ApiOperation("获取对应领导值班")
    public ApiResponse<NightStudyDuty> getCorrespondLeaderNightStudyDuty(
            @RequestParam(value = "schoolyardId") Long schoolyardId,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam("time") Date time) {
        return ApiResponse.ok(this.teacherDutyGradeTotalService.getCorrespondLeaderNightStudyDuty(schoolyardId, time));
    }

    /**
     * 年级总值班晚班 班级数据
     */
    @GetMapping("/nightRoutine")
    @ApiOperation("年级总值班晚班 班级数据")
    public ApiResponse<Map<String, Object>> nightRoutine(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam("time") Date time,
                                                         @RequestParam(value = "type", defaultValue = "DAY") RoutineEnum type) {
        return ApiResponse.ok(this.teacherDutyGradeTotalService.nightRoutine(time, type));
    }

    @GetMapping("/nightRoutine/commentAndIncident")
    @ApiOperation("年级总值班晚班 偶发事件意见建议")
    public ApiResponse<Map<String, Object>> nightRoutineCommentAndIncident(@RequestParam LeaderDutyTypeEnum dutyType,
                                                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam Date time) {
        return ApiResponse.ok(this.teacherDutyGradeTotalService.nightRoutineCommentAndIncident(dutyType, time));
    }

    /**
     * 年级总值班带班
     */
    @GetMapping("/update/teacher/duty")
    @ApiOperation("年级总值班带班")
    public ApiResponse<Object> updateTeacherDuty(@RequestParam(value = "id") Long id) {
        return ApiResponse.ok(this.teacherDutyGradeTotalService.updateTeacherDuty(id));
    }

    /**
     * 年级总值班带班 可选班次列表
     */
    @GetMapping("/update/teacher/duty/list")
    @ApiOperation("年级总值班带班 可选班次列表")
    public ApiResponse<List<TeacherDutyGradeTotalDto>> updateTeacherDutyList(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam(value = "timeFrom", required = false) Date timeFrom,
                                                                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam(value = "timeTo", required = false) Date timeTo) {
        return ApiResponse.ok(this.teacherDutyGradeTotalService.updateTeacherDutyList(timeFrom, timeTo));
    }

    /**
     * 年级总值班取消带班
     */
    @PostMapping("/cancel/teacher/duty")
    @ApiOperation("年级总值班取消带班")
    public ApiResponse<Object> cancelTeacherDuty(@RequestParam(value = "teacherDutyId") Long teacherDutyId,
                                                  @RequestParam(value = "teacherId") Long teacherId) {
        return ApiResponse.ok(this.teacherDutyGradeTotalService.cancelTeacherDuty(teacherDutyId, teacherId));
    }

    /**
     * 年级总值班取消带班 可选老师列表
     */
    @GetMapping("/cancel/teacher/list")
    @ApiOperation("年级总值班取消带班 可选老师列表")
    public ApiResponse<List<Staff>> cancelTeacherList(@RequestParam Long teacherDutyId) {
        return ApiResponse.ok(this.teacherDutyGradeTotalService.cancelTeacherList(teacherDutyId));
    }

    @GetMapping("/search/log")
    @ApiOperation("查询代值记录")
    public ApiResponse<IPage<TeacherDutyGradeTotalSubstitueDto>> searchMyLogPage(
            TeacherDutySubstituteParam param,
            @RequestParam(value = "bool", required = false) Boolean bool,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return ApiResponse.ok(this.teacherDutyGradeTotalService.searchMyLogPage(new Page<>(pageNum, pageSize), param, bool));
    }

}
