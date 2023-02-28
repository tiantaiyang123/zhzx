/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import javax.annotation.Resource;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.StudentParent;
import com.zhzx.server.enums.RelationEnum;
import com.zhzx.server.service.WxSendMessageService;
import com.zhzx.server.vo.SchoolWeek;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.springframework.web.bind.annotation.*;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.domain.AcademicYearSemester;
import com.zhzx.server.rest.req.AcademicYearSemesterParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.AcademicYearSemesterService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Api(tags = "AcademicYearSemesterController", description = "学年学期表管理")
@RequestMapping("/v1/system/academic-year-semester")
public class AcademicYearSemesterController {
    @Resource
    private AcademicYearSemesterService academicYearSemesterService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<AcademicYearSemester> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.academicYearSemesterService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<AcademicYearSemester> add(@RequestBody AcademicYearSemester entity) {
        entity.setDefault().validate(true);
        this.academicYearSemesterService.save(entity);
        return ApiResponse.ok(this.academicYearSemesterService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<AcademicYearSemester> update(@RequestBody AcademicYearSemester entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.academicYearSemesterService.updateAllFieldsById(entity);
        } else {
            this.academicYearSemesterService.updateById(entity);
        }
        return ApiResponse.ok(this.academicYearSemesterService.getById(entity.getId()));
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
        return ApiResponse.ok(this.academicYearSemesterService.removeById(id));
    }

    /**
     * 批量新增
     *
     * @param entityList 要新增的对象
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-save")
    @ApiOperation("批量新增")
    public ApiResponse<Boolean> batchSave(@RequestBody List<AcademicYearSemester> entityList) {
        entityList.forEach(entity -> {
            entity.setDefault().validate(true);
        });
        return ApiResponse.ok(this.academicYearSemesterService.saveBatch(entityList));
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
    public ApiResponse<Boolean> batchUpdate(AcademicYearSemesterParam param, @RequestBody AcademicYearSemester entity) {
        return ApiResponse.ok(this.academicYearSemesterService.update(entity, param.toQueryWrapper()));
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
        return ApiResponse.ok(this.academicYearSemesterService.removeByIds(idList));
    }

    /**
     * 分页查询
     *
     * @param param    查询参数
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @return int
     */
    @GetMapping("/search")
    @ApiOperation("分页查询")
    public ApiResponse<IPage<AcademicYearSemester>> selectByPage(
            AcademicYearSemesterParam param,
            @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<AcademicYearSemester> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<AcademicYearSemester> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.academicYearSemesterService.page(page, wrapper));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Long> count(AcademicYearSemesterParam param) {
        QueryWrapper<AcademicYearSemester> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.academicYearSemesterService.count(wrapper));
    }

    @GetMapping("/weeks")
    @ApiOperation("获得学期周数")
    public ApiResponse<List<SchoolWeek>> getWeeks(@RequestParam(value = "academicYearSemesterId", required = false) Long academicYearSemesterId) {
        return ApiResponse.ok(this.academicYearSemesterService.getWeeks(academicYearSemesterId));
    }

    @GetMapping("/set-current-year-semester/{id}")
    @ApiOperation("设置当前学期")
    public ApiResponse<Integer> setCurrentYearSemester(@PathVariable("id") Long id) {
        this.academicYearSemesterService.setCurrentYearSemester(id);
        return ApiResponse.ok(null);
    }
}