/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.Clazz;
import com.zhzx.server.domain.Exam;
import com.zhzx.server.rest.req.ExamParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.ExamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Api(tags = "ExamController", description = "考试表管理")
@RequestMapping("/v1/data/exam")
public class ExamController {
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
    public ApiResponse<Exam> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.examService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<Exam> add(@RequestBody Exam entity) {
        entity.setDefault().validate(true);
        this.examService.save(entity);
        return ApiResponse.ok(this.examService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<Exam> update(@RequestBody Exam entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.examService.updateAllFieldsById(entity);
        } else {
            this.examService.updateById(entity);
        }
        return ApiResponse.ok(this.examService.getById(entity.getId()));
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
        return ApiResponse.ok(this.examService.removeById(id));
    }

    /**
     * 批量新增
     *
     * @param entityList 要新增的对象
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-save")
    @ApiOperation("批量新增")
    public ApiResponse<Boolean> batchSave(@RequestBody List<Exam> entityList) {
        entityList.forEach(entity -> {
            entity.setDefault().validate(true);
        });
        return ApiResponse.ok(this.examService.saveBatch(entityList));
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
    public ApiResponse<Boolean> batchUpdate(ExamParam param, @RequestBody Exam entity) {
        return ApiResponse.ok(this.examService.update(entity, param.toQueryWrapper()));
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
        return ApiResponse.ok(this.examService.removeByIds(idList));
    }

    /**
     * 分页查询
     *
     * @param param 查询参数
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return int
     */
    @GetMapping("/search-detail")
    @ApiOperation("分页查询(带分科信息, 分科排名页面单独用)")
    public ApiResponse<IPage<Map<String, Object>>> selectByPageDetail(
            ExamParam param,
            @RequestParam(value = "orderByClause", defaultValue = "exam_end_date desc") String orderByClause,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        IPage<Exam> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.examService.selectByPageDetail(page, orderByClause, param));
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
    public ApiResponse<IPage<Exam>> selectByPage(
        ExamParam param,
        @RequestParam(value = "orderByClause", defaultValue = "exam_end_date desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<Exam> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<Exam> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.examService.page(page, wrapper));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Long> count(ExamParam param) {
        QueryWrapper<Exam> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.examService.count(wrapper));
    }

    /**
     * 获得考试列表
     */
    @GetMapping("/list-all")
    @ApiOperation("获得考试列表")
    public ApiResponse<List<Clazz>> getListBySchoolyardYear(
            @RequestParam(value = "schoolyardId", required = false) Long schoolyardId,
            @RequestParam(value = "academicYear", required = false) String academicYear,
            @RequestParam(value = "gradeId", required = false) Long gradeId) {
        return ApiResponse.ok(this.examService.getList(schoolyardId, academicYear, gradeId));
    }

    @GetMapping("/list-by-grade")
    @ApiOperation("获得本年级及之前所有考试")
    public ApiResponse<List<Exam>> getListByGrade(
            @RequestParam(value = "schoolyardId", required = false) Long schoolyardId,
            @RequestParam(value = "gradeId", required = false) Long gradeId) {
        return ApiResponse.ok(this.examService.getListByGrade(schoolyardId, gradeId));
    }

    @GetMapping("/publish")
    @ApiOperation("发布考试")
    public ApiResponse<Object> subscribe(
            @RequestParam(value = "examId", required = false) Long examId) {
        this.examService.publish(examId);
        return ApiResponse.ok("成功");
    }

}
