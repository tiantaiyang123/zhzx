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
import com.zhzx.server.dto.TeacherDutySubstituteDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import net.hasor.web.annotation.Post;
import org.springframework.web.bind.annotation.*;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.domain.TeacherDutySubstitute;
import com.zhzx.server.rest.req.TeacherDutySubstituteParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.TeacherDutySubstituteService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Api(tags = "TeacherDutySubstituteController", description = "教师值班代班表管理")
@RequestMapping("/v1/day/teacher-duty-substitute")
public class TeacherDutySubstituteController {
    @Resource
    private TeacherDutySubstituteService teacherDutySubstituteService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<TeacherDutySubstitute> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.teacherDutySubstituteService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<TeacherDutySubstitute> add(@RequestBody TeacherDutySubstitute entity) {
        entity.setDefault().validate(true);
        this.teacherDutySubstituteService.save(entity);
        return ApiResponse.ok(this.teacherDutySubstituteService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<TeacherDutySubstitute> update(@RequestBody TeacherDutySubstitute entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.teacherDutySubstituteService.updateAllFieldsById(entity);
        } else {
            this.teacherDutySubstituteService.updateById(entity);
        }
        return ApiResponse.ok(this.teacherDutySubstituteService.getById(entity.getId()));
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
        return ApiResponse.ok(this.teacherDutySubstituteService.removeById(id));
    }

    /**
     * 批量新增
     *
     * @param entityList 要新增的对象
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-save")
    @ApiOperation("批量新增")
    public ApiResponse<Boolean> batchSave(@RequestBody List<TeacherDutySubstitute> entityList) {
        entityList.forEach(entity -> {
            entity.setDefault().validate(true);
        });
        return ApiResponse.ok(this.teacherDutySubstituteService.saveBatch(entityList));
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
    public ApiResponse<Boolean> batchUpdate(TeacherDutySubstituteParam param, @RequestBody TeacherDutySubstitute entity) {
        return ApiResponse.ok(this.teacherDutySubstituteService.update(entity, param.toQueryWrapper()));
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
        return ApiResponse.ok(this.teacherDutySubstituteService.removeByIds(idList));
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
    public ApiResponse<IPage<TeacherDutySubstitute>> selectByPage(
        TeacherDutySubstituteParam param,
        @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<TeacherDutySubstitute> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<TeacherDutySubstitute> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.teacherDutySubstituteService.page(page, wrapper));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Integer> count(TeacherDutySubstituteParam param) {
        QueryWrapper<TeacherDutySubstitute> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.teacherDutySubstituteService.count(wrapper));
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
            TeacherDutySubstituteParam param,
            @RequestParam(value = "bool", required = false) Boolean bool,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        IPage<TeacherDutySubstituteDto> page = new Page<>(pageNum, pageSize);
        this.teacherDutySubstituteService.searchMyLogPage(page,param,bool);
        return ApiResponse.ok(page);
    }

    /**
     *
     *
     * @return int
     */
    @PostMapping("/agree")
    @ApiOperation("同意/拒绝代值")
    public ApiResponse<Integer> agree(@RequestBody TeacherDutySubstitute entity) {
        return ApiResponse.ok(this.teacherDutySubstituteService.agree(entity));
    }
}
