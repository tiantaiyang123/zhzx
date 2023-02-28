package com.zhzx.server.rest;

import com.zhzx.server.dto.exam.ExamGoalDto;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.rest.req.ExamEdgeSubParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.ScoreAnalyseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by A2 on 2022/2/24.
 */
@Slf4j
@RestController
@Api(tags = "ScoreAnalyseController", description = "成绩分析")
@RequestMapping("/v1/score-analyse")
public class ScoreAnalyseController {
    @Resource
    private ScoreAnalyseService scoreAnalyseService;

    @PostMapping("/radio-score")
    @ApiOperation("成绩雷达图")
    public ApiResponse<Object> radioScore(
            @RequestBody List<Map<String, Object>> examResultSimpleDtoList
    ) {
        // RequestBody中的Map对应ExamClazzAnalyseClazzAndStudentDto
        return ApiResponse.ok(this.scoreAnalyseService.radioScore(examResultSimpleDtoList));
    }

    @GetMapping("/edge")
    @ApiOperation("临界生柱状图")
    public ApiResponse<Object> edge(
            ExamEdgeSubParam param,
            @RequestParam(value = "selfCustom", defaultValue = "NO") YesNoEnum selfCustom,
            @RequestParam(value = "clazzIds", required = false) List<Long> clazzIds
    ) {
        return ApiResponse.ok(this.scoreAnalyseService.edge(param, selfCustom, clazzIds));
    }

    @GetMapping("/edge-detail")
    @ApiOperation("临界生详情")
    public ApiResponse<Object> edgeDetail(
            ExamEdgeSubParam param,
            @RequestParam(value = "clazzIds", required = false) List<Long> clazzIds
    ) {
        return ApiResponse.ok(this.scoreAnalyseService.edgeDetail(param, clazzIds));
    }

    @GetMapping("/partition/line-chart")
    @ApiOperation("分段折线图")
    public ApiResponse<Object> partitionLineChart(@RequestParam(value = "examId", required = false) Long examId,
                                                  @RequestParam(value = "subjectId", required = false) Long subjectId,
                                                  @RequestParam(value = "clazzIds", required = false) List<Long> clazzIds,
                                                  @RequestParam(value = "tolerance", required = false, defaultValue = "10") Integer tolerance) {
        return ApiResponse.ok(this.scoreAnalyseService.partitionLineChart(examId, subjectId, clazzIds, tolerance));
    }

    @GetMapping("/score/bar-chart")
    @ApiOperation("柱状图")
    public ApiResponse<Object> scoreBarChart(@RequestParam(value = "examId", required = false) Long examId,
                                             @RequestParam(value = "clazzIds", required = false) List<Long> clazzIds) {
        return ApiResponse.ok(this.scoreAnalyseService.scoreBarChart(examId, clazzIds));
    }

    @GetMapping("/person/bar-chart")
    @ApiOperation("柱状图(个人)")
    public ApiResponse<Object> personBarChart(@RequestParam(value = "examId", required = false) Long examId,
                                              @RequestParam(value = "studentId", required = false) Long studentId) {
        return ApiResponse.ok(this.scoreAnalyseService.personBarChart(examId, studentId));
    }

    @GetMapping("/head-info-clazz")
    @ApiOperation("头信息(班级)")
    public ApiResponse<Object> headInfoClazz(@RequestParam(value = "examId", required = false) Long examId,
                                             @RequestParam(value = "clazzId") Long clazzId) {
        return ApiResponse.ok(this.scoreAnalyseService.headInfoClazz(examId, clazzId));
    }

    @GetMapping("/table-info-clazz")
    @ApiOperation("班级成绩表")
    public ApiResponse<Object> tableInfoClazz(@RequestParam(value = "examId", required = false) Long examId,
                                              @RequestParam(value = "clazzId", required = false) Long clazzId,
                                              @RequestParam(value = "orderByClause", defaultValue = "totalWeightedScore desc") String orderByClause) {
        return ApiResponse.ok(this.scoreAnalyseService.tableInfoClazz(examId, clazzId, orderByClause));
    }

    @GetMapping("/table-info-clazz/export-excel")
    @ApiOperation("班级成绩表(导出excel)")
    @SneakyThrows
    public void tableInfoClazzExportExcel(@RequestParam(value = "examId") Long examId,
                                          @RequestParam(value = "clazzId") Long clazzId,
                                          @RequestParam(value = "needGoal") String needGoal,
                                          @RequestParam(value = "numberOrScore") String numberOrScore,
                                          HttpServletResponse response, HttpServletRequest request) {
        XSSFWorkbook book = this.scoreAnalyseService.tableInfoClazzExportExcel(examId, clazzId, needGoal, numberOrScore);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = "学生成绩表(成绩分析)" + sdf.format(new Date()) + ".xlsx";
        //获取浏览器使用的编码
        String encoding = request.getCharacterEncoding();
        if (encoding != null && encoding.length() > 0) {
            fileName = URLEncoder.encode(fileName, encoding);
        } else {
            //默认编码是utf-8
            fileName = URLEncoder.encode(fileName, "UTF-8");
        }
        response.reset();
        response.setCharacterEncoding(encoding);
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        OutputStream out = response.getOutputStream();
        book.write(out);
        out.flush();
        out.close();
        book.close();
    }

    @GetMapping("/head-info-grade")
    @ApiOperation("头信息(年级)")
    public ApiResponse<Object> headInfoGrade(@RequestParam(value = "examId", required = false) Long examId,
                                             @RequestParam(value = "gradeId", required = false) Long gradeId) {
        return ApiResponse.ok(this.scoreAnalyseService.headInfoGrade(examId, gradeId));
    }

    @GetMapping("/subject/table-compare")
    @ApiOperation("学科成绩表格对比")
    public ApiResponse<Object> subjectTableCompare(@RequestParam(value = "examId", required = false) Long examId,
                                                   @RequestParam(value = "subjectId", required = false) Long subjectId,
                                                   @RequestParam(value = "clazzIds", required = false) List<Long> clazzIds) {
        return ApiResponse.ok(this.scoreAnalyseService.subjectTableCompare(examId, subjectId, clazzIds));
    }

    @GetMapping("/subject/table-compare/export-excel")
    @ApiOperation("学科成绩表格对比(导出excel)")
    @SneakyThrows
    public void subjectTableCompareExportExcel(@RequestParam(value = "examId", required = false) Long examId,
                                                              @RequestParam(value = "subjectId", required = false) Long subjectId,
                                                              @RequestParam(value = "clazzIds", required = false) List<Long> clazzIds,
                                                              HttpServletResponse response, HttpServletRequest request) {
        XSSFWorkbook book = this.scoreAnalyseService.subjectTableCompareExportExcel(examId, subjectId, clazzIds);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = "学科成绩对比表(" + sdf.format(new Date()) + ").xlsx";
        //获取浏览器使用的编码
        String encoding = request.getCharacterEncoding();
        if (encoding != null && encoding.length() > 0) {
            fileName = URLEncoder.encode(fileName, encoding);
        } else {
            //默认编码是utf-8
            fileName = URLEncoder.encode(fileName, "UTF-8");
        }
        response.reset();
        response.setCharacterEncoding(encoding);
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        OutputStream out = response.getOutputStream();
        book.write(out);
        out.flush();
        out.close();
        book.close();
    }

    @GetMapping("/goal")
    @ApiOperation("目标")
    public ApiResponse<Object> goal(@RequestParam(value = "examId", required = false) Long examId,
                                    @RequestParam(value = "clazzIds", required = false) List<Long> clazzIds) {
        return ApiResponse.ok(this.scoreAnalyseService.goal(examId, clazzIds));
    }

    @GetMapping("/goal/list")
    @ApiOperation("目标列表")
    public ApiResponse<List<ExamGoalDto>> goalList(@RequestParam(value = "examId", required = false) Long examId) {
        return ApiResponse.ok(this.scoreAnalyseService.goalList(examId));
    }

    @GetMapping("/goal/export-excel")
    @ApiOperation("目标(导出excel)")
    @SneakyThrows
    public void goalExportExcel(@RequestParam(value = "examId") Long examId,
                                @RequestParam(value = "subjectId") Long subjectId,
                                @RequestParam(value = "clazzIds", required = false) List<Long> clazzIds,
                                HttpServletResponse response, HttpServletRequest request) {
        XSSFWorkbook book = this.scoreAnalyseService.goalExportExcel(examId, subjectId, clazzIds);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = "目标(成绩分析)" + sdf.format(new Date()) + ".xlsx";
        //获取浏览器使用的编码
        String encoding = request.getCharacterEncoding();
        if (encoding != null && encoding.length() > 0) {
            fileName = URLEncoder.encode(fileName, encoding);
        } else {
            //默认编码是utf-8
            fileName = URLEncoder.encode(fileName, "UTF-8");
        }
        response.reset();
        response.setCharacterEncoding(encoding);
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        OutputStream out = response.getOutputStream();
        book.write(out);
        out.flush();
        out.close();
        book.close();
    }

    @GetMapping("/goal-detail")
    @ApiOperation("目标达标详情(暂时不用)")
    public ApiResponse<Object> goalDetail(@RequestParam(value = "examId") Long examId,
                                          @RequestParam(value = "clazzId") Long clazzId,
                                          @RequestParam(value = "subjectId") Long subjectId) {
        return ApiResponse.ok(this.scoreAnalyseService.goalDetail(examId, clazzId, subjectId));
    }
}
