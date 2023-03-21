/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.Clazz;
import com.zhzx.server.domain.Exam;
import com.zhzx.server.domain.ExamResult;
import com.zhzx.server.domain.ExamScoreReport;
import com.zhzx.server.dto.exam.*;
import com.zhzx.server.enums.ClazzNatureEnum;
import com.zhzx.server.enums.GenderEnum;
import com.zhzx.server.enums.StudentTypeEnum;
import com.zhzx.server.rest.req.ExamResultParam;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.*;
import com.zhzx.server.util.StringUtils;
import com.zhzx.server.vo.ExamVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Api(tags = "ExamResultController", description = "考试结果表管理")
@RequestMapping("/v1/data/exam-result")
public class ExamResultController {
    @Resource
    private ExamResultService examResultService;

    @Resource
    private ExamService examService;

    @Resource
    private ClazzService clazzService;

    @Resource
    private ExamGoalService examGoalService;

    @Resource
    private ExamGoalClazzService examGoalClazzService;

    @Resource
    private SubjectService subjectService;

    @Resource
    private GradeService gradeService;

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @GetMapping("/subject-join-count")
    @ApiOperation("获取考试参考人数")
    public ApiResponse<Map<Long, Object>> subjectJoinCount(@RequestParam(value = "examIds", required = false) List<Long> examIds,
                                                             @RequestParam(value = "clazzNature", required = false) ClazzNatureEnum clazzNatureEnum) {
        return ApiResponse.ok(this.examResultService.subjectJoinCount(examIds, clazzNatureEnum));
    }

    @GetMapping("/station/personal-list")
    @ApiOperation("工作台(个人榜单)")
    public ApiResponse<List<ExamResultSimpleDto>> personalListStation(@RequestParam(value = "examId", required = false) Long examId,
                                                                      @RequestParam(value = "clazzId", required = false) Long clazzId) {
        return ApiResponse.ok(this.examResultService.personalListStation(examId, clazzId));
    }

    @GetMapping("/station/score-list")
    @ApiOperation("工作台(成绩榜单)")
    public ApiResponse<List<ExamGradeAnalyseClazzSituationDto>> scoreListStation(@RequestParam(value = "examId", required = false) Long examId,
                                                                                 @RequestParam(value = "subjectId", required = false) Long subjectId) {
        return ApiResponse.ok(this.examResultService.scoreListStation(examId, subjectId));
    }

    @GetMapping("/station/clazz-trend")
    @ApiOperation("工作台(班级排名趋势)")
    public ApiResponse<Map<String, Object>> clazzTrendStation(@RequestParam(value = "gradeId", required = false) Long gradeId) {
        return ApiResponse.ok(this.examResultService.clazzTrendStation(gradeId));
    }

    @GetMapping("/grade-analyse-subject")
    @ApiOperation("年级成绩分析(学科情况)")
    public ApiResponse<Map<String, Object>> gradeAnalyseSubject(
            @RequestParam(value = "examId") Long examId,
            @RequestParam(value = "clazzNature", required = false, defaultValue = "") String clazzNature,
            @RequestParam(value = "subjectName", defaultValue = "") String subjectName,
            @RequestParam(value = "highest", defaultValue = "100") Integer highest,
            @RequestParam(value = "lowest", defaultValue = "60") Integer lowest,
            @RequestParam(value = "tolerance", defaultValue = "10") Integer tolerance
    ) {
        return ApiResponse.ok(this.examResultService.gradeAnalyseSubject(examId, clazzNature, subjectName, highest, lowest, tolerance));
    }

    @GetMapping("/grade-analyse-clazz")
    @ApiOperation("年级成绩分析(班级情况均分)")
    public ApiResponse<Map<String, Object>> gradeAnalyseClazz(@RequestParam(value = "examId") Long examId) {
        return ApiResponse.ok(this.examResultService.gradeAnalyseClazz(examId));
    }

    @GetMapping("/grade-analyse-goal")
    @ApiOperation("年级成绩分析(目标情况)")
    public ApiResponse<List<Map<String, Object>>> gradeAnalyseGoal(@RequestParam(value = "examId", required = false) Long examId,
                                                                   @RequestParam(value = "goalId", required = false) Long goalId,
                                                                   @RequestParam(value = "clazzId", required = false, defaultValue = "0") Long clazzId) {
        Map<String, Object> res = new HashMap<>();
        if (examId != null) {
            List<ExamGoalDto> examGoalDtoList = this.examGoalService.getDefault(examId, goalId);
            if (CollectionUtils.isNotEmpty(examGoalDtoList)) {
                CompletableFuture<Void> c1 = CompletableFuture.runAsync(() -> {
                    Map<String, Object> clazzGoalTotal = this.examGoalService.getGoalTotal(clazzId, examId, examGoalDtoList);
                    res.put("goalTotalColumns", clazzGoalTotal.get("goalTotalColumns"));
                    res.put("goalTotalList", clazzGoalTotal.get("goalTotalList"));
                    res.put("columnsTypeList", clazzGoalTotal.get("columnsTypeList"));
                }, threadPoolExecutor);
                CompletableFuture<Void> c2 = CompletableFuture.runAsync(() -> {
                    Map<String, Object> clazzGoalSubject = this.examGoalService.getGoalSubject(clazzId, examId, examGoalDtoList);
                    res.put("goalSubjectColumns", clazzGoalSubject.get("goalSubjectColumns"));
                    res.put("goalSubjectMap", clazzGoalSubject.get("goalSubjectMap"));
                }, threadPoolExecutor);
                try {
                    CompletableFuture.allOf(c1, c2).get();
                } catch (Exception e) {
                    log.error("查询目标失败", e.getMessage());
                    return ApiResponse.fail(-1, e.getMessage());
                }
            }
        }
        return ApiResponse.ok(res);
    }

    @GetMapping("/clazz-analyse")
    @ApiOperation("班级成绩分析")
    public ApiResponse<Map<String, Object>> clazzAnalyse(
            @RequestParam(value = "examId", required = false) Long examId,
            @RequestParam(value = "clazzId", required = false) Long clazzId,
            @RequestParam(value = "studentId", required = false) Long studentId
    ) {
        Map<String, Object> map;
        try {
            map = this.examResultService.clazzAnalyse(examId, clazzId, studentId);
        } catch (Exception e) {
            throw new ApiCode.ApiException(-1, e.getMessage());
        }
        return ApiResponse.ok(map);
    }

    @GetMapping("/student/analyse")
    @ApiOperation("学生成绩单")
    public ApiResponse<Map<String, Object>> studentAnalyse(
            @RequestParam(value = "schoolyardId", required = false) Long schoolyardId,
            @RequestParam(value = "type", required = false, defaultValue = "01") String type,
            @RequestParam(value = "academicYearSemesterId", required = false) Long academicYearSemesterId,
            @RequestParam(value = "clazzId", required = false) Long clazzId,
            @RequestParam(value = "studentId", required = false) Long studentId
    ) {
        Map<String, Object> map = this.examResultService.studentAnalyse(schoolyardId, academicYearSemesterId, clazzId, studentId, type);
        return ApiResponse.ok(map);
    }

    @GetMapping("/student-analyse")
    @ApiOperation("学生成绩单(1)")
    public ApiResponse<Map<String, Object>> studentAnalysePager(
            @RequestParam(value = "type", required = false, defaultValue = "01") String type,
            @RequestParam(value = "schoolyardId", required = false) Long schoolyardId,
            @RequestParam(value = "examPublishId", required = false) Long examPublishId,
            @RequestParam(value = "studentId", required = false) Long studentId
    ) {
        Map<String, Object> map = this.examResultService.studentAnalysePager(schoolyardId, examPublishId, studentId, type);
        return ApiResponse.ok(map);
    }

    @GetMapping("getExamBySemmsterAndYard")
    @ApiOperation("通过学年校区获取考试列表(不用)")
    public ApiResponse<List<ExamVo>> getExamBySemmsterAndYard(
            @RequestParam(value = "schoolyardId") Integer schoolyardId,
            @RequestParam(value = "academicYear") String academicYear
    ) {
        return ApiResponse.ok(this.examService.getExamBySemmsterAndYard(new HashMap<String, Object>() {
            {
                put("schoolyardId", schoolyardId);
                put("academicYear", academicYear);
            }
        }));
    }

    @GetMapping("calculate")
    @ApiOperation("计算年级赋分和学业等级")
    public ApiResponse<Boolean> calculate(@RequestParam(value = "examId") Long examId,
                                          @RequestParam(value = "name") String name,
                                          @RequestParam(value = "includeWeighted", required = false, defaultValue = "01") String includeWeighted) {
        return ApiResponse.ok(this.examResultService.calculate(examId, name, includeWeighted));
    }

    @GetMapping("score-pager")
    @ApiOperation("成绩单")
    public ApiResponse<List<ExamPagerDto>> examResultPager(
            @RequestParam(value = "examId", required = false) Long examId,
            @RequestParam(value = "clazzId", required = false) Long clazzId,
            @RequestParam(value = "gender", required = false, defaultValue = "") GenderEnum gender,
            @RequestParam(value = "studentType", required = false, defaultValue = "") StudentTypeEnum studentType,
            @RequestParam(value = "pagerType", required = false, defaultValue = "") ClazzNatureEnum pagerType,
            @RequestParam(value = "orderByClause") String orderByClause,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        IPage<ExamPagerDto> page = new Page<>(pageNum, pageSize);
        Map<String, Object> map = new HashMap<String, Object>() {
            {
                put("examId", examId);
                put("clazzId", clazzId);
                put("gender", gender);
                put("studentType", studentType);
                put("pagerType", pagerType);
                put("orderByClause", StringUtils.isNullOrEmpty(orderByClause) ? "a.clazzId" : "a.clazzId," + orderByClause);
            }
        };
        return ApiResponse.ok(this.examResultService.queryExamResultPager(page, map, examId));
    }

    @GetMapping("/resultEnter")
    @ApiOperation("成绩录入(不用)")
    public ApiResponse<IPage<ExamResult>> resultEnter(
            ExamResultParam param,
            @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<ExamResult> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<ExamResult> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.examResultService.queryExamResultByPage(page, wrapper));
    }

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<ExamResult> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.examResultService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<ExamResult> add(@RequestBody ExamResult entity) {
        BigDecimal partScore = entity.getChineseScore().add(entity.getMathScore())
                .add(entity.getEnglishScore()).add(entity.getPhysicsScore())
                .add(entity.getHistoryScore());
        if (entity.getTotalScore() == null) {
            entity.setTotalScore(partScore.add(entity.getChemistryScore()).add(entity.getBiologyScore())
                    .add(entity.getPoliticsScore()).add(entity.getGeographyScore()));
        }
        if (entity.getTotalWeightedScore() == null) {
            entity.setTotalWeightedScore(entity.getChemistryWeightedScore().add(entity.getBiologyWeightedScore())
                    .add(entity.getPoliticsWeightedScore()).add(entity.getGeographyWeightedScore()));
        }
        entity.setDefault().validate(true);
        this.examResultService.save(entity);
        return ApiResponse.ok(this.examResultService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<ExamResult> update(@RequestBody ExamResult entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        BigDecimal partScore = entity.getChineseScore().add(entity.getMathScore())
                .add(entity.getEnglishScore()).add(entity.getPhysicsScore())
                .add(entity.getHistoryScore());
        entity.setTotalScore(partScore.add(entity.getChemistryScore()).add(entity.getBiologyScore())
                    .add(entity.getPoliticsScore()).add(entity.getGeographyScore()));
        entity.setTotalWeightedScore(entity.getChemistryWeightedScore().add(entity.getBiologyWeightedScore())
                    .add(entity.getPoliticsWeightedScore()).add(entity.getGeographyWeightedScore()));
        if (updateAllFields) {
            this.examResultService.updateAllFieldsById(entity);
        } else {
            this.examResultService.updateById(entity);
        }
        return ApiResponse.ok(this.examResultService.getById(entity.getId()));
    }

    /**
     * 删除
     *
     * @param id 要删除的对象id
     * @return int
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除")
    public ApiResponse<Integer> delete(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.examResultService.removeById(id));
    }

    /**
     * 批量新增
     *
     * @param entityList 要新增的对象
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-save")
    @ApiOperation("批量新增")
    public ApiResponse<Boolean> batchSave(@RequestBody List<ExamResult> entityList) {
        entityList.forEach(entity -> {
            entity.setDefault().validate(true);
        });
        return ApiResponse.ok(this.examResultService.saveBatch(entityList));
    }

    /**
     * 批量更新
     *
     * @param param  更新条件
     * @param entity 要更新的对象
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-update")
    @ApiOperation("批量更新")
    public ApiResponse<Boolean> batchUpdate(ExamResultParam param, @RequestBody ExamResult entity) {
        return ApiResponse.ok(this.examResultService.update(entity, param.toQueryWrapper()));
    }

    /**
     * 批量删除
     *
     * @param idList 要删除的对象id
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-delete")
    @ApiOperation("批量删除")
    public ApiResponse<Boolean> batchDelete(@RequestBody List<Long> idList) {
        return ApiResponse.ok(this.examResultService.removeByIds(idList));
    }

    /**
     * 分页查询
     *
     * @param param    查询参数
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @return int
     */
    @GetMapping("/search")
    @ApiOperation("分页查询")
    public ApiResponse<IPage<ExamResult>> selectByPage(
            ExamResultParam param,
            @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<ExamResult> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<ExamResult> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.examResultService.page(page, wrapper));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Long> count(ExamResultParam param) {
        QueryWrapper<ExamResult> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.examResultService.count(wrapper));
    }

    /**
     * 分页查询成绩
     */
    @GetMapping("/search-exam-result")
    @ApiOperation("分页查询成绩")
    public ApiResponse<IPage<ExamResult>> searchByPage(
            @RequestParam(value = "schoolyardId", required = false) Long schoolyardId,
            @RequestParam(value = "academicYearSemesterId", required = false) Long academicYearSemesterId,
            @RequestParam(value = "gradeId", required = false) Long gradeId,
            @RequestParam(value = "examId", required = false) Long examId,
            @RequestParam(value = "clazzId", required = false) Long clazzId,
            @RequestParam(value = "studentId", required = false) Long studentId,
            @RequestParam(value = "studentName", required = false) String studentName,
            @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        IPage<ExamResult> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.examResultService.searchExamResult(page, schoolyardId, academicYearSemesterId, gradeId, examId, clazzId, studentId, studentName, orderByClause));
    }

    /**
     * 分页查询成绩
     */
    @GetMapping("/search-exam-result/exist-or-default")
    @ApiOperation("批量新增或者编辑前置接口 查询默认值")
    public ApiResponse<List<ExamResult>> searchByPageExistOrDefault(
            @RequestParam(value = "schoolyardId", required = false) Long schoolyardId,
            @RequestParam(value = "academicYearSemesterId", required = false) Long academicYearSemesterId,
            @RequestParam(value = "gradeId", required = false) Long gradeId,
            @RequestParam(value = "examId", required = false) Long examId,
            @RequestParam(value = "clazzId", required = false) Long clazzId,
            @RequestParam(value = "studentId", required = false) Long studentId,
            @RequestParam(value = "studentName", required = false) String studentName,
            @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause
    ) {
        return ApiResponse.ok(this.examResultService.searchByPageExistOrDefault(schoolyardId, academicYearSemesterId, gradeId, examId, clazzId, studentId, studentName, orderByClause));
    }

    @PostMapping("/batch-create-or-update")
    @ApiOperation("批量新增或者编辑")
    public ApiResponse<List<ExamResult>> batchCreateOrUpdate(@RequestBody List<ExamResult> entityList) {
        return ApiResponse.ok(this.examResultService.batchCreateOrUpdate(entityList));
    }

    /**
     * 分页查询成绩
     */
    @GetMapping("/card/search-exam-result")
    @ApiOperation("分页查询成绩")
    public ApiResponse<IPage<ExamResult>> searchByPageCard(
            @RequestParam(value = "examPublishId", required = false) Long examPublishId,
            @RequestParam(value = "studentId", required = false) Long studentId,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        IPage<ExamResult> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.examResultService.searchExamResultCard(page, examPublishId, studentId));
    }

    /**
     * 分页查询成绩
     */
    @GetMapping("/student/search-exam-result")
    @ApiOperation("学生成绩单列表")
    public ApiResponse<IPage<Map<String, Object>>> searchByPageStudent(
            @RequestParam(value = "schoolyardId", required = false) Long schoolyardId,
            @RequestParam(value = "academicYearSemesterId", required = false) Long academicYearSemesterId,
            @RequestParam(value = "gradeId", required = false) Long gradeId,
            @RequestParam(value = "examId", required = false) Long examId,
            @RequestParam(value = "clazzId", required = false) Long clazzId,
            @RequestParam(value = "studentId", required = false) Long studentId,
            @RequestParam(value = "studentName", required = false) String studentName,
            @RequestParam(value = "orderByClause", defaultValue = "studentNumber") String orderByClause,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        IPage<ExamResult> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.examResultService.searchExamResultStudent(page, schoolyardId, academicYearSemesterId, gradeId, examId, clazzId, studentId, studentName, orderByClause));
    }

    @GetMapping("/student-search-exam-result")
    @ApiOperation("学生成绩单列表(1)")
    public ApiResponse<IPage<Map<String, Object>>> studentScorePager(
            @RequestParam(value = "schoolyardId", required = false) Long schoolyardId,
            @RequestParam(value = "gradeId", required = false) Long gradeId,
            @RequestParam(value = "clazzIds", required = false) List<Long> clazzIds,
            @RequestParam(value = "examPublishId", required = false) Long examPublishId,
            @RequestParam(value = "studentName", required = false) String studentName,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        IPage<ExamResult> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.examResultService.studentScorePager(page, schoolyardId, gradeId, clazzIds, examPublishId, studentName));
    }

    /**
     * 分页查询成绩
     */
    @GetMapping("/clazz/search-exam-result")
    @ApiOperation("分页查询成绩")
    public ApiResponse<IPage<Map<String, Object>>> searchByPageClazz(
            @RequestParam(value = "examId", required = false) Long examId,
            @RequestParam(value = "clazzId", required = false) List<Long> clazzId,
            @RequestParam(value = "studentName", required = false) String studentName,
            @RequestParam(value = "orderByClause", defaultValue = "studentNumber") String orderByClause,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        IPage<ExamResult> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.examResultService.searchExamResultClazz(page, examId, clazzId, orderByClause, studentName));
    }

    @GetMapping("/grade/search-exam-result")
    @ApiOperation("分页查询成绩")
    public ApiResponse<IPage<Map<String, Object>>> searchByPageGrade(
            @RequestParam(value = "clazzNature", required = false, defaultValue = "OTHER") String clazzNature,
            @RequestParam(value = "clazzIds", required = false) List<Long> clazzIds,
            @RequestParam(value = "examId", required = false) Long examId,
            @RequestParam(value = "studentName", required = false) String studentName,
            @RequestParam(value = "orderByClause", defaultValue = "totalWeightedScore desc") String orderByClause,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "limitNum", defaultValue = "100") Integer limitNum
    ) {
        IPage<ExamResult> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.examResultService.searchExamResultGrade(page, clazzNature, examId, orderByClause, limitNum, studentName, clazzIds));
    }

    @GetMapping("/import-excel")
    @ApiOperation("导入成绩表")
    public ApiResponse<Boolean> importExcel(@RequestParam(value = "examId") Long examId,
                                            @RequestParam(value = "fileUrl") String fileUrl) {
        this.examResultService.importExcel(examId, fileUrl);
        return ApiResponse.ok(null);
    }

    @GetMapping("/export-excel")
    @ApiOperation("导出成绩表")
    public void exportExcel(HttpServletResponse response, HttpServletRequest request,
                            @RequestParam(value = "type", required = false, defaultValue = "01") String type,
                            @RequestParam(value = "examId") Long examId,
                            @RequestParam(value = "clazzId") Long clazzId) throws IOException, InvalidFormatException {
        Exam exam = this.examService.getById(examId);
        XSSFWorkbook book = this.examResultService.exportExcel(type, clazzId, exam);
        OutputStream output = response.getOutputStream();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = exam.getGrade().getName() + "-" + exam.getName() + "-成绩表(" + sdf.format(new Date()) + ").xlsx";
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

    @GetMapping("/score-pager/export-excel")
    @ApiOperation("导出成绩单")
    public void exportExcelPager(HttpServletResponse response, HttpServletRequest request,
                                    @RequestParam(value = "examId") Long examId,
                                    @RequestParam(value = "columnList", required = false) List<String> columnList,
                                    @RequestParam(value = "orderByClause", required = false) String orderByClause,
                                    @RequestParam(value = "clazzId", required = false) Long clazzId,
                                    @RequestParam(value = "gender", required = false, defaultValue = "") GenderEnum gender,
                                    @RequestParam(value = "studentType", required = false, defaultValue = "") StudentTypeEnum studentType,
                                    @RequestParam(value = "pagerType", required = false, defaultValue = "") ClazzNatureEnum pagerType) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>() {
            {
                put("examId", examId);
                put("clazzId", clazzId);
                put("gender", gender);
                put("studentType", studentType);
                put("pagerType", pagerType);
                put("orderByClause", StringUtils.isNullOrEmpty(orderByClause) ? "a.clazzId" : "a.clazzId," + orderByClause);
            }
        };
        Exam exam = this.examService.getById(examId);
        if (exam == null) {
            throw new ApiCode.ApiException(-1, "考试不存在！");
        }
        XSSFWorkbook book = this.examResultService.exportExcelPager(columnList, examId, map);
        OutputStream output = response.getOutputStream();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = exam.getGrade().getName() + "-" + exam.getName() + "-成绩表(" + sdf.format(new Date()) + ").xlsx";
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

}
