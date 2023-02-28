/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.ExamGoal;
import com.zhzx.server.rest.req.ExamGoalParam;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.ExamGoalService;
import com.zhzx.server.vo.ExamGoalMutateVo;
import com.zhzx.server.vo.ExamGoalVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Api(tags = "ExamGoalController", description = "考试目标表管理")
@RequestMapping("/v1/data/exam-goal")
public class ExamGoalController {
    @Resource
    private ExamGoalService examGoalService;

    @GetMapping("/station/goal")
    @ApiOperation("工作台(目标)")
    public ApiResponse<List<Map<String, Object>>> goalStation(@RequestParam(value = "examId", required = false) Long examId,
                                                              @RequestParam(value = "goalId", required = false) Long goalId,
                                                              @RequestParam(value = "clazzId", required = false, defaultValue = "0") Long clazzId) {
        return ApiResponse.ok(this.examGoalService.goalStation(examId, goalId, clazzId));
    }

    @GetMapping("/station/goal-school")
    @ApiOperation("工作台(全校目标)")
    public ApiResponse<Map<String, Object>> goalStationSchool(@RequestParam(value = "examId", required = false) Long examId,
                                                              @RequestParam(value = "academicYearSemesterId", required = false) Long academicYearSemesterId,
                                                              @RequestParam(value = "gradeId", required = false) Long gradeId) {
        return ApiResponse.ok(this.examGoalService.goalStationSchool(examId, academicYearSemesterId, gradeId));
    }

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<ExamGoal> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.examGoalService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<ExamGoal> add(@RequestBody ExamGoal entity) {
        entity.setDefault().validate(true);
        String[] arr = entity.getName().split(",");
        List<String> list = Arrays.stream(arr).distinct().collect(Collectors.toList());
        if (list.size() != arr.length)
            return ApiResponse.fail(-1, "名称重复");
        int count = this.examGoalService.count(Wrappers.<ExamGoal>lambdaQuery()
                .eq(ExamGoal::getExamId, entity.getExamId())
                .in(ExamGoal::getName, list));
        if (count > 0)
            return ApiResponse.fail(-1, "名称重复");
        List<ExamGoal> examGoalList = list.stream().map(item -> {
            ExamGoal examGoal = new ExamGoal();
            try {
                BeanUtils.copyProperties(examGoal, entity);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new ApiCode.ApiException(-1, "bean copy error");
            }
            examGoal.setName(item);
            return examGoal;
        }).collect(Collectors.toList());
        this.examGoalService.saveBatch(examGoalList);
        return ApiResponse.ok(entity);
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<ExamGoal> update(@RequestBody ExamGoal entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        entity.setDefault();
        if (updateAllFields) {
            this.examGoalService.updateAllFieldsById(entity);
        } else {
            this.examGoalService.updateById(entity);
        }
        return ApiResponse.ok(this.examGoalService.getById(entity.getId()));
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
        return ApiResponse.ok(this.examGoalService.removeAll(id));
    }

    /**
     * 批量新增
     *
     * @param entityList 要新增的对象
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-save")
    @ApiOperation("批量新增")
    public ApiResponse<Boolean> batchSave(@RequestBody List<ExamGoal> entityList) {
        entityList.forEach(entity -> {
            entity.setDefault().validate(true);
        });
        return ApiResponse.ok(this.examGoalService.saveBatch(entityList));
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
    public ApiResponse<Boolean> batchUpdate(ExamGoalParam param, @RequestBody ExamGoal entity) {
        return ApiResponse.ok(this.examGoalService.update(entity, param.toQueryWrapper()));
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
        return ApiResponse.ok(this.examGoalService.removeByIds(idList));
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
    public ApiResponse<IPage<ExamGoal>> selectByPage(
        ExamGoalParam param,
        @RequestParam(value = "gradeId", required = false) Long gradeId,
        @RequestParam(value = "academicYearSemesterId", required = false) Long academicYearSemesterId,
        @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<ExamGoal> wrapper = param.toQueryWrapper();
        String suffix = "";
        if (gradeId != null) {
            suffix += " and grade_id=" + gradeId;
        }
        if (academicYearSemesterId != null) {
            suffix += " and academic_year_semester_id=" + academicYearSemesterId;
        }
        wrapper.inSql("exam_id", "select id from dat_exam where 1=1" + suffix);
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<ExamGoal> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.examGoalService.page(page, wrapper));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Long> count(ExamGoalParam param) {
        QueryWrapper<ExamGoal> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.examGoalService.count(wrapper));
    }

    /**
     * 添加或者更新总目标
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/addUpdateTotal")
    @ApiOperation("新增目标前置")
    public ApiResponse<List<ExamGoal>> addUpdateTotal(@RequestBody List<ExamGoal> entity) {
        return ApiResponse.ok(this.examGoalService.addUpdateTotal(entity.get(0).getExamId(), entity));
    }

    /**
     * addUpdateEach 添加或更新各班目标
     *
     * @param  entity 要新增或更新的对象
     * @return 新增的对象
     */
    @PostMapping("/addUpdateEach")
    @ApiOperation("新增目标班级")
    public ApiResponse<ExamGoal> addUpdateEach(@RequestBody ExamGoalVo entity) {
        return ApiResponse.ok(this.examGoalService.addUpdateEach(entity));
    }

    @PostMapping("/update-total-goal-score-cache")
    @ApiOperation("修改总分目标分数")
    public ApiResponse<Integer> updateTotalGoalScoreCache(@RequestBody ExamGoalMutateVo examGoalMutateVo) {
        return ApiResponse.ok(this.examGoalService.updateTotalGoalScoreCache(examGoalMutateVo));
    }

    @GetMapping("/import-excel")
    @ApiOperation("导入班级表")
    public ApiResponse<Boolean> importExcel(
            @RequestParam(value = "examId") Long examId,
            @RequestParam(value = "fileUrl") String fileUrl) {
        this.examGoalService.importExcel(examId, fileUrl);
        return ApiResponse.ok(null);
    }



}
