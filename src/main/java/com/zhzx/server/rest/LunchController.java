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
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.web.bind.annotation.*;
import com.zhzx.server.domain.Lunch;
import com.zhzx.server.rest.req.LunchParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.LunchService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Api(tags = "LunchController", description = "午餐情况表管理")
@RequestMapping("/v1/day/lunch")
public class LunchController {
    @Resource
    private LunchService lunchService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<Lunch> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.lunchService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<Lunch> add(@RequestBody Lunch entity) {
        entity.setDefault().validate(true);
        this.lunchService.save(entity);
        return ApiResponse.ok(this.lunchService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<Lunch> update(@RequestBody Lunch entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.lunchService.updateAllFieldsById(entity);
        } else {
            this.lunchService.updateById(entity);
        }
        return ApiResponse.ok(this.lunchService.getById(entity.getId()));
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
        return ApiResponse.ok(this.lunchService.removeById(id));
    }

    /**
     * 批量新增
     *
     * @param entityList 要新增的对象
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-save")
    @ApiOperation("批量新增")
    public ApiResponse<Boolean> batchSave(@RequestBody List<Lunch> entityList) {
        entityList.forEach(entity -> {
            entity.setDefault().validate(true);
        });
        return ApiResponse.ok(this.lunchService.saveBatch(entityList));
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
    public ApiResponse<Boolean> batchUpdate(LunchParam param, @RequestBody Lunch entity) {
        return ApiResponse.ok(this.lunchService.update(entity, param.toQueryWrapper()));
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
        return ApiResponse.ok(this.lunchService.removeByIds(idList));
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
    public ApiResponse<IPage<Lunch>> selectByPage(
        LunchParam param,
        @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<Lunch> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<Lunch> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.lunchService.page(page, wrapper));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Integer> count(LunchParam param) {
        QueryWrapper<Lunch> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.lunchService.count(wrapper));
    }

    /**
     * count查询
     *
     * @param entity 查询参数
     * @return int
     */
    @PostMapping("/create/or/update")
    @ApiOperation("修改设备及图片")
    public ApiResponse<Lunch> createOrUpdate(@RequestBody Lunch entity) {

        return ApiResponse.ok(this.lunchService.createOrUpdate(entity));
    }

    /**
     * 分页查询
     *
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return int
     */
    @GetMapping("/back/search")
    @ApiOperation("后台分页查询")
    public ApiResponse<IPage<Lunch>> backSelectByPage(
            LunchParam param,
            @RequestParam(value = "leaderName", required = false) String leaderName,
            @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<Lunch> wrapper = param.toQueryWrapper();
        if(StringUtils.isNotBlank(leaderName)){
            wrapper.inSql("t.leader_duty_id",
                    "select dldi.id from day_leader_duty dldi " +
                            "left join sys_staff sst on sst.id = dldi.leader_id " +
                            "where sst.name like '%"+ leaderName +"%'");
        }
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.lunchService.pageDetail(page, wrapper));
    }
}
