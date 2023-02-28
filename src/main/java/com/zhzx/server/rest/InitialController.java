package com.zhzx.server.rest;

import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.InitialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@Api(tags = "InitialController", description = "数据初始化")
@RequestMapping("/v1/dat/initial")
public class InitialController {
    @Resource
    private InitialService initialService;

    @GetMapping("/import-excel/student")
    @ApiOperation("导入学生班级表(1)")
    public ApiResponse<Boolean> importExcelStudent(@RequestParam(value = "academicYearSemesterId") Long academicYearSemesterId,
                                                   @RequestParam(value = "fileUrl") String fileUrl) {
        this.initialService.importExcelStudent(academicYearSemesterId, fileUrl);
        return ApiResponse.ok(null);
    }

    @GetMapping("/import-excel/student-clazz")
    @ApiOperation("导入学生班级表(2)")
    public ApiResponse<Boolean> importExcel(@RequestParam(value = "schoolyardId") Long schoolyardId,
                                            @RequestParam(value = "gradeName", defaultValue = "高一年级") String gradeName,
                                            @RequestParam(value = "academicYearSemesterId") Long academicYearSemesterId,
                                            @RequestParam(value = "fileUrl") String fileUrl) {
        this.initialService.importExcel(schoolyardId, gradeName, academicYearSemesterId, fileUrl);
        return ApiResponse.ok(null);
    }

    @GetMapping("/import-excel/score")
    @ApiOperation("导入成绩表(3)")
    public ApiResponse<Boolean> importExcelScore(@RequestParam(value = "examId") Long examId,
                                                @RequestParam(value = "fileUrl") String fileUrl) {
        this.initialService.importExcelScore(examId, fileUrl);
        return ApiResponse.ok(null);
    }
}
