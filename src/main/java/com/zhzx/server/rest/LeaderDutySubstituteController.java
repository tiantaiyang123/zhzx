/**
 * 项目：中华中学管理平台
 * 模型分组：一日常规管理
 * 模型名称：领导值班代班表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest;

import java.util.List;
import java.util.Arrays;

import javax.annotation.Resource;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.LeaderDutySubstitute;
import com.zhzx.server.dto.TeacherDutySubstituteDto;
import com.zhzx.server.rest.req.LeaderDutySubstituteParam;
import com.zhzx.server.service.LeaderDutySubstituteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.web.bind.annotation.*;
import com.zhzx.server.rest.res.ApiResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Api(tags = "LeaderDutySubstituteController", description = "领导值班代班表管理")
@RequestMapping("/v1/day/leader-duty-substitute")
public class LeaderDutySubstituteController {

    @Resource
    private LeaderDutySubstituteService leaderDutySubstituteService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<LeaderDutySubstitute> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.leaderDutySubstituteService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<LeaderDutySubstitute> add(@RequestBody LeaderDutySubstitute entity) {
        entity.setDefault().validate(true);
        this.leaderDutySubstituteService.save(entity);
        return ApiResponse.ok(this.leaderDutySubstituteService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<LeaderDutySubstitute> update(@RequestBody LeaderDutySubstitute entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.leaderDutySubstituteService.updateAllFieldsById(entity);
        } else {
            this.leaderDutySubstituteService.updateById(entity);
        }
        return ApiResponse.ok(this.leaderDutySubstituteService.getById(entity.getId()));
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
        return ApiResponse.ok(this.leaderDutySubstituteService.removeById(id));
    }

    /**
     * 批量新增
     *
     * @param entityList 要新增的对象
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-save")
    @ApiOperation("批量新增")
    public ApiResponse<Boolean> batchSave(@RequestBody List<LeaderDutySubstitute> entityList) {
        entityList.forEach(entity -> {
            entity.setDefault().validate(true);
        });
        return ApiResponse.ok(this.leaderDutySubstituteService.saveBatch(entityList));
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
    public ApiResponse<Boolean> batchUpdate(LeaderDutySubstituteParam param, @RequestBody LeaderDutySubstitute entity) {
        return ApiResponse.ok(this.leaderDutySubstituteService.update(entity, param.toQueryWrapper()));
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
        return ApiResponse.ok(this.leaderDutySubstituteService.removeByIds(idList));
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
    public ApiResponse<IPage<LeaderDutySubstitute>> selectByPage(
            LeaderDutySubstituteParam param,
            @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<LeaderDutySubstitute> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<LeaderDutySubstitute> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.leaderDutySubstituteService.page(page, wrapper));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Long> count(LeaderDutySubstituteParam param) {
        QueryWrapper<LeaderDutySubstitute> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.leaderDutySubstituteService.count(wrapper));
    }

    /**
     *
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/search/log")
    @ApiOperation("查询代值记录")
    public ApiResponse<IPage<TeacherDutySubstituteDto>> searchMyLogPage(
            LeaderDutySubstituteParam param,
            @RequestParam(value = "bool", required = false) Boolean bool,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        IPage page = new Page<>(pageNum, pageSize);
        this.leaderDutySubstituteService.searchMyLogPage(page,param,bool);
        return ApiResponse.ok(page);
    }
}
