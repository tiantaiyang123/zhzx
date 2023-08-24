/**
 * 项目：中华中学管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest;

import java.util.List;
import java.util.Arrays;

import javax.annotation.Resource;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.Ill;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.springframework.web.bind.annotation.*;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.domain.ClazzTeachingLog;
import com.zhzx.server.rest.req.ClazzTeachingLogParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.ClazzTeachingLogService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Api(tags = "ClazzTeachingLogController", description = "班级教学日志表管理")
@RequestMapping("/v1/student/clazz-teaching-log")
public class ClazzTeachingLogController {
    @Resource
    private ClazzTeachingLogService clazzTeachingLogService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<ClazzTeachingLog> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.clazzTeachingLogService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<ClazzTeachingLog> add(@RequestBody ClazzTeachingLog entity) {
        entity.setDefault().validate(true);
        this.clazzTeachingLogService.save(entity);
        return ApiResponse.ok(this.clazzTeachingLogService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<ClazzTeachingLog> update(@RequestBody ClazzTeachingLog entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.clazzTeachingLogService.updateAllFieldsById(entity);
        } else {
            this.clazzTeachingLogService.updateById(entity);
        }
        return ApiResponse.ok(this.clazzTeachingLogService.getById(entity.getId()));
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
        return ApiResponse.ok(this.clazzTeachingLogService.removeById(id));
    }

    /**
     * 批量新增
     *
     * @param entityList 要新增的对象
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-save")
    @ApiOperation("批量新增")
    public ApiResponse<Boolean> batchSave(@RequestBody List<ClazzTeachingLog> entityList) {
        entityList.forEach(entity -> {
            entity.setDefault().validate(true);
        });
        return ApiResponse.ok(this.clazzTeachingLogService.saveBatch(entityList));
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
    public ApiResponse<Boolean> batchUpdate(ClazzTeachingLogParam param, @RequestBody ClazzTeachingLog entity) {
        return ApiResponse.ok(this.clazzTeachingLogService.update(entity, param.toQueryWrapper()));
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
        return ApiResponse.ok(this.clazzTeachingLogService.removeByIds(idList));
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
    public ApiResponse<IPage<ClazzTeachingLog>> selectByPage(
        ClazzTeachingLogParam param,
        @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<ClazzTeachingLog> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<ClazzTeachingLog> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.clazzTeachingLogService.page(page, wrapper));
    }

    /**
     * 单个查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/one")
    @ApiOperation("单个查询")
    public ApiResponse<ClazzTeachingLog> selectOne(ClazzTeachingLogParam param) {
        QueryWrapper<ClazzTeachingLog> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.clazzTeachingLogService.getOne(wrapper, false));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Integer> count(ClazzTeachingLogParam param) {
        QueryWrapper<ClazzTeachingLog> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.clazzTeachingLogService.count(wrapper));
    }

    /**
     * 班级教学日志填报
     */
    @GetMapping("/clazz-teaching-log-report")
    @ApiOperation("班级教学日志填报")
    public ApiResponse<ClazzTeachingLog> getClazzTeachingLog(
            @RequestParam(value = "clazzId") Long clazzId,
            @RequestParam(value = "registerDate") String registerDate) {
        return ApiResponse.ok(this.clazzTeachingLogService.getClazzTeachingLog(clazzId, registerDate));
    }


    /**
     * 按周查询班级教学日志
     */
    @GetMapping("/clazz-teaching-log-week")
    @ApiOperation("按周查询班级教学日志")
    public ApiResponse<List<ClazzTeachingLog>> listWeekLog(
            @RequestParam(value = "schoolyardId", required = false) Long schoolyardId,
            @RequestParam(value = "academicYearSemesterId", required = false) Long academicYearSemesterId,
            @RequestParam(value = "gradeId", required = false) Long gradeId,
            @RequestParam(value = "clazzId", required = false) Long clazzId,
            @RequestParam(value = "week", required = false) Integer week
    ) {
        return ApiResponse.ok(this.clazzTeachingLogService.listWeekLog(schoolyardId, academicYearSemesterId, gradeId, clazzId, week));
    }
}
