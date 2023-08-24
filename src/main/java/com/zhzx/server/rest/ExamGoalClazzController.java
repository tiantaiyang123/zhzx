/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.ExamGoalClazz;
import com.zhzx.server.rest.req.ExamGoalClazzParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.ExamGoalClazzService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@Api(tags = "ExamGoalClazzController", description = "考试目标班级表管理")
@RequestMapping("/v1/data/exam-goal-clazz")
public class ExamGoalClazzController {
    @Resource
    private ExamGoalClazzService examGoalClazzService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<ExamGoalClazz> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.examGoalClazzService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<ExamGoalClazz> add(@RequestBody ExamGoalClazz entity) {
        return ApiResponse.ok(this.examGoalClazzService.saveExamGoalClazz(entity));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<ExamGoalClazz> update(@RequestBody ExamGoalClazz entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        return ApiResponse.ok(this.examGoalClazzService.updateExamGoalClazz(entity, updateAllFields));
    }

    /**
     * 删除
     *
     * @param id 要删除的对象id
     * @return int
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除")
    public ApiResponse<Boolean> delete(@PathVariable("id") Long id) {
        this.examGoalClazzService.deleteExamGoalClazz(id);
        return ApiResponse.ok(null);
    }

    /**
     * 批量新增
     *
     * @param entityList 要新增的对象
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-save")
    @ApiOperation("批量新增")
    public ApiResponse<Integer> batchSave(@RequestBody List<ExamGoalClazz> entityList) {
        entityList.forEach(entity -> {
            entity.setDefault().validate(true);
        });
        return ApiResponse.ok(this.examGoalClazzService.saveBatchExamGoalClazz(entityList));
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
    public ApiResponse<Boolean> batchUpdate(ExamGoalClazzParam param, @RequestBody ExamGoalClazz entity) {
        return ApiResponse.ok(this.examGoalClazzService.update(entity, param.toQueryWrapper()));
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
        return ApiResponse.ok(this.examGoalClazzService.removeByIds(idList));
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
    public ApiResponse<IPage<ExamGoalClazz>> selectByPage(
        ExamGoalClazzParam param,
        @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<ExamGoalClazz> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<ExamGoalClazz> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.examGoalClazzService.page(page, wrapper));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Integer> count(ExamGoalClazzParam param) {
        QueryWrapper<ExamGoalClazz> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.examGoalClazzService.count(wrapper));
    }

}
