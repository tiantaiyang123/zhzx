/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest;

import java.util.Date;
import java.util.List;
import java.util.Arrays;

import javax.annotation.Resource;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.enums.TeacherDutyTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.domain.Staff;
import com.zhzx.server.rest.req.StaffParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.StaffService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Api(tags = "StaffController", description = "教职工表管理")
@RequestMapping("/v1/system/staff")
public class StaffController {
    @Resource
    private StaffService staffService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<Staff> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.staffService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<Staff> add(@RequestBody Staff entity) {
        return ApiResponse.ok(staffService.saveStaff(entity));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<Staff> update(@RequestBody Staff entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        return ApiResponse.ok(this.staffService.updateStaff(entity, updateAllFields));
    }

    /**
     * 禁用
     *
     * @param id 要删除的对象id
     * @return int
     */
    @GetMapping("/update-delete/{id}")
    @ApiOperation("禁用")
    public ApiResponse<Staff> updateDelete(@PathVariable("id") Long id) {
        this.staffService.updateIsDelete(id);
        return ApiResponse.ok(null);
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
        this.staffService.delete(id);
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
    public ApiResponse<Boolean> batchSave(@RequestBody List<Staff> entityList) {
        entityList.forEach(entity -> {
            entity.setDefault().validate(true);
        });
        return ApiResponse.ok(this.staffService.saveBatch(entityList));
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
    public ApiResponse<Boolean> batchUpdate(StaffParam param, @RequestBody Staff entity) {
        return ApiResponse.ok(this.staffService.update(entity, param.toQueryWrapper()));
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
        return ApiResponse.ok(this.staffService.removeByIds(idList));
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
    public ApiResponse<IPage<Staff>> selectByPage(
        StaffParam param,
        @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<Staff> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<Staff> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.staffService.page(page, wrapper));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Integer> count(StaffParam param) {
        QueryWrapper<Staff> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.staffService.count(wrapper));
    }

    @GetMapping("/import-excel")
    @ApiOperation("导入教职工表")
    public ApiResponse<Boolean> importExcel(@RequestParam(value = "fileUrl") String fileUrl) {
        this.staffService.importExcel(fileUrl);
        return ApiResponse.ok(null);
    }

    @GetMapping("/get/list/no/duty")
    @ApiOperation("查询今天没有值班的老师")
    public ApiResponse<List<Staff>> getListNoDuty(@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam Date date,
                                              @RequestParam TeacherDutyTypeEnum dutyType) {

        return ApiResponse.ok(this.staffService.getListNoDuty(date,dutyType));
    }
}
