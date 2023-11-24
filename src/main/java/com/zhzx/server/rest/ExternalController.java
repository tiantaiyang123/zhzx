package com.zhzx.server.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhzx.server.domain.*;
import com.zhzx.server.dto.CookieDto;
import com.zhzx.server.dto.annotation.TirAuth;
import com.zhzx.server.dto.xcx.WxXcxMessageDto;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.rest.req.CourseParam;
import com.zhzx.server.rest.req.StaffParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.*;
import com.zhzx.server.util.DateUtils;
import com.zhzx.server.vo.StudentParamVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@RestController
@Api(tags = "ExternalController", description = "外部系统调用接口控制器")
@RequestMapping("/v1/external")
public class ExternalController {

    @Resource
    private ExternalService externalService;
    @Resource
    private WxXcxMessageService wxXcxMessageService;
    @Resource
    private CourseService courseService;
    @Resource
    private StaffService staffService;
    @Resource
    private SettingsService settingsService;
    @Resource
    private AcademicYearSemesterService academicYearSemesterService;

    @PostMapping("/sync/wx/xcx/message")
    @ApiOperation("小程序消息同步")
    @TirAuth
    public ApiResponse<Void> syncWxXcxMessage(@RequestParam(name = "code") String code,
                                                @RequestBody List<WxXcxMessageDto> wxXcxMessageDtoList) {
        wxXcxMessageService.syncWxXcxMessage(code, wxXcxMessageDtoList);
        return ApiResponse.ok(null);
    }

    @GetMapping("/get/course")
    @ApiOperation("获取课表")
    @TirAuth
    public ApiResponse<Map<String, Object>> getCourse(@RequestParam(name = "code") String code,
                                               @RequestParam(name = "isFullSync", required = false, defaultValue = "NO") YesNoEnum isFullSync,
                                               CourseParam param,
                                               @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause) {
        // 增量获取
        Settings settings = this.settingsService.getOne(
                Wrappers.<Settings>lambdaQuery()
                        .eq(Settings::getCode, "SYNC_COURSE_INFO")
        );
        if (!YesNoEnum.YES.equals(isFullSync)) {
            Date updateTime = settings == null ? null : DateUtils.parse(settings.getParams());
            param.setUpdateTimeFrom(updateTime);
        }

        Map<String, Object> map = new HashMap<>();
        AcademicYearSemester academicYearSemester = this.academicYearSemesterService.getOne(
                Wrappers.<AcademicYearSemester>lambdaQuery()
                        .eq(AcademicYearSemester::getIsDefault, YesNoEnum.YES)
        );
        List<Course> courses = new ArrayList<>();
        if (null != academicYearSemester) {
            param.setAcademicYearSemesterId(academicYearSemester.getId());
            QueryWrapper<Course> wrapper = param.toQueryWrapper();
            String[] temp = orderByClause.split("[,;]");
            Arrays.stream(temp).forEach(ob -> {
                String[] obTemp = ob.split("\\s");
                boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
                wrapper.orderBy(true, isAsc, obTemp[0]);
            });
            courses = this.externalService.listSimpleIncrCourse(settings, wrapper);
        }
        map.put("courseList", courses);
        map.put("academicYearSemester", academicYearSemester);
        return ApiResponse.ok(map);
    }

    @GetMapping("/get/teacher")
    @ApiOperation("获取教师")
    @TirAuth
    public ApiResponse<List<Staff>> getCourse(@RequestParam(name = "code") String code,
                                              StaffParam param,
                                              @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause) {
        // 全量获取
        QueryWrapper<Staff> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        return ApiResponse.ok(this.externalService.listSimpleFullStaff(wrapper));
    }

    @GetMapping("/get/student")
    @ApiOperation("获取学生")
    @TirAuth
    public ApiResponse<List<Student>> getStudent(@RequestParam(name = "code") String code,
                                                 @RequestParam(name = "isFullSync", required = false, defaultValue = "NO") YesNoEnum isFullSync,
                                                 StudentParamVo param) {
        // 增量获取
        Settings settings = this.settingsService.getOne(
                Wrappers.<Settings>lambdaQuery()
                        .eq(Settings::getCode, "SYNC_STUDENT_INFO")
        );
        if (!YesNoEnum.YES.equals(isFullSync)) {
            Date updateTime = settings == null ? null : DateUtils.parse(settings.getParams());
            param.setUpdateTimeFrom(updateTime);
        }
        return ApiResponse.ok(this.externalService.listSimpleIncrStudent(settings, param));
    }

    @GetMapping("/acquire-tir-cookie")
    @ApiOperation("获取免密登录cookie")
    public ApiResponse<CookieDto> acquireTirCookie() {
        return ApiResponse.ok(this.externalService.acquireTirCookie());
    }

}
