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
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.Lunch;
import com.zhzx.server.rest.req.LunchParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.springframework.web.bind.annotation.*;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.domain.MorningReading;
import com.zhzx.server.rest.req.MorningReadingParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.MorningReadingService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Api(tags = "MorningReadingController", description = "早读情况表管理")
@RequestMapping("/v1/day/morning-reading")
public class MorningReadingController {
    @Resource
    private MorningReadingService morningReadingService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<MorningReading> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.morningReadingService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<MorningReading> add(@RequestBody MorningReading entity) {
        entity.setDefault().validate(true);
        this.morningReadingService.save(entity);
        return ApiResponse.ok(this.morningReadingService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<MorningReading> update(@RequestBody MorningReading entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.morningReadingService.updateAllFieldsById(entity);
        } else {
            this.morningReadingService.updateById(entity);
        }
        return ApiResponse.ok(this.morningReadingService.getById(entity.getId()));
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
        return ApiResponse.ok(this.morningReadingService.removeById(id));
    }

    /**
     * 批量新增
     *
     * @param entityList 要新增的对象
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-save")
    @ApiOperation("批量新增")
    public ApiResponse<Boolean> batchSave(@RequestBody List<MorningReading> entityList) {
        entityList.forEach(entity -> {
            entity.setDefault().validate(true);
        });
        return ApiResponse.ok(this.morningReadingService.saveBatch(entityList));
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
    public ApiResponse<Boolean> batchUpdate(MorningReadingParam param, @RequestBody MorningReading entity) {
        return ApiResponse.ok(this.morningReadingService.update(entity, param.toQueryWrapper()));
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
        return ApiResponse.ok(this.morningReadingService.removeByIds(idList));
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
    public ApiResponse<IPage<MorningReading>> selectByPage(
        MorningReadingParam param,
        @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<MorningReading> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<MorningReading> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.morningReadingService.page(page, wrapper));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Long> count(MorningReadingParam param) {
        QueryWrapper<MorningReading> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.morningReadingService.count(wrapper));
    }

    /**
     * count查询
     *
     * @param entity 查询参数
     * @return int
     */
    @PostMapping("/create/or/update")
    @ApiOperation("修改设备及图片")
    public ApiResponse<MorningReading> createOrUpdate(@RequestBody MorningReading entity) {

        return ApiResponse.ok(this.morningReadingService.createOrUpdate(entity));
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
    public ApiResponse<IPage<MorningReading>> backSelectByPage(
            MorningReadingParam param,
            @RequestParam(value = "leaderName", required = false) String leaderName,
            @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<MorningReading> wrapper = param.toQueryWrapper();
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
        return ApiResponse.ok(this.morningReadingService.pageDetail(page, wrapper));
    }
}
