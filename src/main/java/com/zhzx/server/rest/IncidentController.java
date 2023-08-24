/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.Incident;
import com.zhzx.server.rest.req.IncidentParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.IncidentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@Api(tags = "IncidentController", description = "偶发事件表管理")
@RequestMapping("/v1/day/incident")
public class IncidentController {
    @Resource
    private IncidentService incidentService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<Incident> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.incidentService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<Incident> add(@RequestBody Incident entity) {
        entity.setDefault().validate(true);
        this.incidentService.createWithPic(entity);

        return ApiResponse.ok(this.incidentService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<Incident> update(@RequestBody Incident entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.incidentService.updateAllFieldsById(entity);
        } else {
            this.incidentService.updateById(entity);
        }
        return ApiResponse.ok(this.incidentService.getById(entity.getId()));
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
        return ApiResponse.ok(this.incidentService.removeById(id));
    }

    /**
     * 批量新增
     *
     * @param entityList 要新增的对象
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-save")
    @ApiOperation("批量新增")
    public ApiResponse<Boolean> batchSave(@RequestBody List<Incident> entityList) {
        entityList.forEach(entity -> {
            entity.setDefault().validate(true);
        });
        return ApiResponse.ok(this.incidentService.saveBatch(entityList));
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
    public ApiResponse<Boolean> batchUpdate(IncidentParam param, @RequestBody Incident entity) {
        return ApiResponse.ok(this.incidentService.update(entity, param.toQueryWrapper()));
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
        return ApiResponse.ok(this.incidentService.removeByIds(idList));
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
    public ApiResponse<IPage<Incident>> selectByPage(
        IncidentParam param,
        @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<Incident> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<Incident> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.incidentService.page(page, wrapper));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Integer> count(IncidentParam param) {
        QueryWrapper<Incident> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.incidentService.count(wrapper));
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
    public ApiResponse<IPage<Incident>> backSelectByPage(
            @RequestParam(value = "schoolyardId", required = false) Long schoolyardId,
            @RequestParam(value = "leaderName", required = false) String leaderName,
            @RequestParam(value = "startTime", required = false) Date startTime,
            @RequestParam(value = "endTime", required = false) Date endTime,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        IPage page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.incidentService.pageDetail(page, leaderName,startTime,endTime, schoolyardId));
    }
}
