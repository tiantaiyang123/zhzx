/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.ExamGoalTemplate;
import com.zhzx.server.rest.req.ExamGoalTemplateParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.ExamGoalTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@Api(tags = "ExamGoalTemplateController", description = "目标模板表管理")
@RequestMapping("/v1/data/exam-goal-template")
public class ExamGoalTemplateController {
    @Resource
    private ExamGoalTemplateService examGoalTemplateService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<ExamGoalTemplate> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.examGoalTemplateService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<ExamGoalTemplate> add(@RequestBody ExamGoalTemplate entity) {
        entity.setDefault().validate(true);
        this.examGoalTemplateService.save(entity);
        return ApiResponse.ok(this.examGoalTemplateService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<ExamGoalTemplate> update(@RequestBody ExamGoalTemplate entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.examGoalTemplateService.updateAllFieldsById(entity);
        } else {
            this.examGoalTemplateService.updateById(entity);
        }
        return ApiResponse.ok(this.examGoalTemplateService.getById(entity.getId()));
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
        return ApiResponse.ok(this.examGoalTemplateService.removeById(id));
    }

    @DeleteMapping("/remove/{id}")
    @ApiOperation("删除")
    public ApiResponse<Object> removeAll(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.examGoalTemplateService.removeAll(id));
    }

    /**
     * 批量新增
     *
     * @param entityList 要新增的对象
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-save")
    @ApiOperation("批量新增")
    public ApiResponse<Boolean> batchSave(@RequestBody List<ExamGoalTemplate> entityList) {
        entityList.forEach(entity -> {
            entity.setDefault().validate(true);
        });
        return ApiResponse.ok(this.examGoalTemplateService.saveBatch(entityList));
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
    public ApiResponse<Boolean> batchUpdate(ExamGoalTemplateParam param, @RequestBody ExamGoalTemplate entity) {
        return ApiResponse.ok(this.examGoalTemplateService.update(entity, param.toQueryWrapper()));
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
        return ApiResponse.ok(this.examGoalTemplateService.removeByIds(idList));
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
    public ApiResponse<IPage<ExamGoalTemplate>> selectByPage(
        ExamGoalTemplateParam param,
        @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<ExamGoalTemplate> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<ExamGoalTemplate> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.examGoalTemplateService.page(page, wrapper));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Integer> count(ExamGoalTemplateParam param) {
        QueryWrapper<ExamGoalTemplate> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.examGoalTemplateService.count(wrapper));
    }

}
