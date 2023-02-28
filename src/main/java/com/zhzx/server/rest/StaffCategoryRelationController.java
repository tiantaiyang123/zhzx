/**
* 项目：中华中学管理平台
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.rest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
import com.zhzx.server.domain.StaffCategoryRelation;
import com.zhzx.server.rest.req.StaffCategoryRelationParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.StaffCategoryRelationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Api(tags = "StaffCategoryRelationController", description = "老师分类关联表管理")
@RequestMapping("/v1/system/staff-category-relation")
public class StaffCategoryRelationController {
    @Resource
    private StaffCategoryRelationService staffCategoryRelationService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<StaffCategoryRelation> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.staffCategoryRelationService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<StaffCategoryRelation> add(@RequestBody StaffCategoryRelation entity) {
        entity.setDefault().validate(true);
        this.staffCategoryRelationService.save(entity);
        return ApiResponse.ok(this.staffCategoryRelationService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<StaffCategoryRelation> update(@RequestBody StaffCategoryRelation entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.staffCategoryRelationService.updateAllFieldsById(entity);
        } else {
            this.staffCategoryRelationService.updateById(entity);
        }
        return ApiResponse.ok(this.staffCategoryRelationService.getById(entity.getId()));
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
        return ApiResponse.ok(this.staffCategoryRelationService.removeById(id));
    }

    /**
     * 删除
     *
     * @param staffCategoryRelations 要删除的对象id
     * @return int
     */
    @PostMapping("/remove/batch")
    @ApiOperation("批量删除")
    public ApiResponse<Integer> removeBatch(@RequestBody List<StaffCategoryRelation> staffCategoryRelations) {
        return ApiResponse.ok(this.staffCategoryRelationService.remove(Wrappers.<StaffCategoryRelation>lambdaQuery()
                .in(StaffCategoryRelation::getId,staffCategoryRelations.stream().map(item->item.getId()).collect(Collectors.toList()))
        ));
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
    public ApiResponse<IPage<StaffCategoryRelation>> selectByPage(
        StaffCategoryRelationParam param,
        @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<StaffCategoryRelation> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<StaffCategoryRelation> authorityPage = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.staffCategoryRelationService.page(authorityPage, wrapper));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Long> count(StaffCategoryRelationParam param) {
        QueryWrapper<StaffCategoryRelation> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.staffCategoryRelationService.count(wrapper));
    }


    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/batch/insert")
    @ApiOperation("批量新增/修改")
    public ApiResponse<StaffCategoryRelation> batchInsert(@RequestBody List<StaffCategoryRelation> entity) {
        return ApiResponse.ok(this.staffCategoryRelationService.batchInsertOrUpdate(entity));
    }
}
