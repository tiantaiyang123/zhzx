/**
 * 项目：中华中学管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.Exam;
import com.zhzx.server.domain.ExamScoreReport;
import com.zhzx.server.domain.User;
import com.zhzx.server.rest.req.ExamScoreReportParam;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.ExamScoreReportService;
import com.zhzx.server.service.ExamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@Api(tags = "ExamScoreReportController", description = "成绩单表管理")
@RequestMapping("/v1/data/exam-score-report")
public class ExamScoreReportController {
    @Resource
    private ExamScoreReportService examScoreReportService;

    @Resource
    private ExamService examService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<ExamScoreReport> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.examScoreReportService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<ExamScoreReport> add(@RequestBody ExamScoreReport entity) {
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        entity.setEditorId(user.getId());
        entity.setEditorName(user.getUsername());
        entity.setDefault().validate(true);
        this.examScoreReportService.save(entity);
        return ApiResponse.ok(this.examScoreReportService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<ExamScoreReport> update(@RequestBody ExamScoreReport entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.examScoreReportService.updateAllFieldsById(entity);
        } else {
            this.examScoreReportService.updateById(entity);
        }
        return ApiResponse.ok(this.examScoreReportService.getById(entity.getId()));
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
        return ApiResponse.ok(this.examScoreReportService.removeById(id));
    }

    /**
     * 批量新增
     *
     * @param entityList 要新增的对象
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-save")
    @ApiOperation("批量新增")
    public ApiResponse<Boolean> batchSave(@RequestBody List<ExamScoreReport> entityList) {
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        entityList.forEach(entity -> {
            entity.setEditorId(user.getId());
            entity.setEditorName(user.getUsername());
            entity.setDefault().validate(true);
        });
        return ApiResponse.ok(this.examScoreReportService.saveBatch(entityList));
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
    public ApiResponse<Boolean> batchUpdate(ExamScoreReportParam param, @RequestBody ExamScoreReport entity) {
        return ApiResponse.ok(this.examScoreReportService.update(entity, param.toQueryWrapper()));
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
        return ApiResponse.ok(this.examScoreReportService.removeByIds(idList));
    }

    /**
     * 分页查询
     *
     * @param param 查询参数
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return int
     */
    @GetMapping("/search")
    @ApiOperation("分页查询")
    public ApiResponse<IPage<ExamScoreReport>> selectByPage(
        ExamScoreReportParam param,
        @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<ExamScoreReport> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<ExamScoreReport> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.examScoreReportService.page(page, wrapper));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Long> count(ExamScoreReportParam param) {
        QueryWrapper<ExamScoreReport> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.examScoreReportService.count(wrapper));
    }

    @GetMapping("/import-excel")
    @ApiOperation("导入平时成绩")
    public ApiResponse<Object> importExcel(
            @RequestParam(value = "fileUrl") String fileUrl,
            @RequestParam(value = "academicYearSemesterId") Long academicYearSemesterId,
            @RequestParam(value = "subjectId") Long subjectId,
            @RequestParam(value = "clazzId") Long clazzId
    ) {
        this.examScoreReportService.importExcel(fileUrl, academicYearSemesterId, subjectId, clazzId);
        return ApiResponse.ok(null);
    }

    @PostMapping("/batch-create-or-update")
    @ApiOperation("批量新增或者编辑")
    public ApiResponse<List<ExamScoreReport>> batchCreateOrUpdate(@RequestBody List<ExamScoreReport> entityList) {
        return ApiResponse.ok(this.examScoreReportService.batchCreateOrUpdate(entityList));
    }

    @GetMapping("/search-exist-or-default")
    @ApiOperation("批量新增或者编辑前置接口 查询默认值")
    public ApiResponse<List<ExamScoreReport>> searchExistOrDefault(@RequestParam(value = "subjectId", required = false) Long subjectId,
                                                                   @RequestParam(value = "academicYearSemesterId", required = false) Long academicYearSemesterId,
                                                                   @RequestParam(value = "clazzId", required = false) Long clazzId) {
        return ApiResponse.ok(this.examScoreReportService.searchExistOrDefault(subjectId, clazzId, academicYearSemesterId));
    }

    @GetMapping("/calculate")
    @ApiOperation("计算总评成绩")
    public ApiResponse<Object> calculate(
            @RequestParam(value = "schoolYardId") Long schoolYardId,
            @RequestParam(value = "academicYearSemesterId") Long academicYearSemesterId,
            @RequestParam(value = "gradeId") Long gradeId,
            @RequestParam(value = "subjectId") Long subjectId,
            @RequestParam(value = "clazzId") Long clazzId
    ) {
        List<Exam> examList = this.examService.list(Wrappers.<Exam>lambdaQuery()
                .eq(Exam::getAcademicYearSemesterId, academicYearSemesterId)
                .eq(Exam::getSchoolyardId, schoolYardId)
                .eq(Exam::getGradeId, gradeId));
        if (CollectionUtils.isEmpty(examList)) {
            throw new ApiCode.ApiException(-1, "本年级无考试");
        }
        Long examIdMiddle = null, examIdEnd = null;
        for (Exam exam : examList) {
            if (exam.getExamType().getName().equals("期中") && examIdMiddle == null) {
                examIdMiddle = exam.getId();
                continue;
            }
            if (exam.getExamType().getName().equals("期末") && examIdEnd == null) {
                examIdEnd = exam.getId();
            }
        }
        if (examIdMiddle == null || examIdEnd == null) {
            throw new ApiCode.ApiException(-1, "缺少期中或期末考试");
        }
        this.examScoreReportService.calculate(subjectId, clazzId, examIdMiddle, examIdEnd);
        return ApiResponse.ok(null);
    }

}
