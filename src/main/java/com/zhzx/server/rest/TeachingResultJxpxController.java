/**
 * 项目：中华中学流程自动化管理平台
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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.springframework.web.bind.annotation.*;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.domain.TeachingResultJxpx;
import com.zhzx.server.rest.req.TeachingResultJxpxParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.TeachingResultJxpxService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Api(tags = "TeachingResultJxpxController", description = "进修培训表管理")
@RequestMapping("/v1/teaching/teaching-result-jxpx")
public class TeachingResultJxpxController {
    @Resource
    private TeachingResultJxpxService teachingResultJxpxService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<TeachingResultJxpx> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.teachingResultJxpxService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<TeachingResultJxpx> add(@RequestBody TeachingResultJxpx entity) {
        entity.setDefault().validate(true);
        this.teachingResultJxpxService.save(entity);
        return ApiResponse.ok(this.teachingResultJxpxService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<TeachingResultJxpx> update(@RequestBody TeachingResultJxpx entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.teachingResultJxpxService.updateAllFieldsById(entity);
        } else {
            this.teachingResultJxpxService.updateById(entity);
        }
        return ApiResponse.ok(this.teachingResultJxpxService.getById(entity.getId()));
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
        return ApiResponse.ok(this.teachingResultJxpxService.removeById(id));
    }

    /**
     * 批量新增
     *
     * @param entityList 要新增的对象
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-save")
    @ApiOperation("批量新增")
    public ApiResponse<Boolean> batchSave(@RequestBody List<TeachingResultJxpx> entityList) {
        entityList.forEach(entity -> {
            entity.setDefault().validate(true);
        });
        return ApiResponse.ok(this.teachingResultJxpxService.saveBatch(entityList));
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
    public ApiResponse<Boolean> batchUpdate(TeachingResultJxpxParam param, @RequestBody TeachingResultJxpx entity) {
        return ApiResponse.ok(this.teachingResultJxpxService.update(entity, param.toQueryWrapper()));
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
        return ApiResponse.ok(this.teachingResultJxpxService.removeByIds(idList));
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
    public ApiResponse<IPage<TeachingResultJxpx>> selectByPage(
        TeachingResultJxpxParam param,
        @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<TeachingResultJxpx> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<TeachingResultJxpx> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.teachingResultJxpxService.page(page, wrapper));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Integer> count(TeachingResultJxpxParam param) {
        QueryWrapper<TeachingResultJxpx> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.teachingResultJxpxService.count(wrapper));
    }

}
