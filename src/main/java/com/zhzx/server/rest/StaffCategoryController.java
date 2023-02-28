/**
* 项目：中华中学管理平台
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.rest;

import java.util.Arrays;

import javax.annotation.Resource;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.springframework.web.bind.annotation.*;
import com.zhzx.server.domain.StaffCategory;
import com.zhzx.server.rest.req.StaffCategoryParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.StaffCategoryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Api(tags = "StaffCategoryController", description = "老师分类表管理")
@RequestMapping("/v1/system/staff-category")
public class StaffCategoryController {
    @Resource
    private StaffCategoryService staffCategoryService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<StaffCategory> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.staffCategoryService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<StaffCategory> add(@RequestBody StaffCategory entity) {
        entity.setDefault().validate(true);
        this.staffCategoryService.save(entity);
        return ApiResponse.ok(this.staffCategoryService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<StaffCategory> update(@RequestBody StaffCategory entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.staffCategoryService.updateAllFieldsById(entity);
        } else {
            this.staffCategoryService.updateById(entity);
        }
        return ApiResponse.ok(this.staffCategoryService.getById(entity.getId()));
    }

    /**
     * 删除
     *
     * @param id 要删除的对象id
     * @return int
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除")
    public ApiResponse<Integer> update(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.staffCategoryService.removeById(id));
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
    public ApiResponse<IPage<StaffCategory>> selectByPage(
        StaffCategoryParam param,
        @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<StaffCategory> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<StaffCategory> authorityPage = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.staffCategoryService.page(authorityPage, wrapper));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Long> count(StaffCategoryParam param) {
        QueryWrapper<StaffCategory> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.staffCategoryService.count(wrapper));
    }
}
