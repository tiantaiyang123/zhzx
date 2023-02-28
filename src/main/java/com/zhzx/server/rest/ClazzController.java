/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.Clazz;
import com.zhzx.server.enums.TeacherDutyTypeEnum;
import com.zhzx.server.rest.req.ClazzParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.ClazzService;
import com.zhzx.server.vo.ClazzVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@Api(tags = "ClazzController", description = "班级表管理")
@RequestMapping("/v1/system/clazz")
public class ClazzController {
    @Resource
    private ClazzService clazzService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<Clazz> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.clazzService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<Clazz> add(@RequestBody Clazz entity) {
        entity.setDefault().validate(true);
        this.clazzService.save(entity);
        return ApiResponse.ok(this.clazzService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<Clazz> update(@RequestBody Clazz entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.clazzService.updateAllFieldsById(entity);
        } else {
            this.clazzService.updateById(entity);
        }
        return ApiResponse.ok(this.clazzService.getById(entity.getId()));
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
        return ApiResponse.ok(this.clazzService.removeById(id));
    }

    /**
     * 批量新增
     *
     * @param entityList 要新增的对象
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-save")
    @ApiOperation("批量新增")
    public ApiResponse<Boolean> batchSave(@RequestBody List<Clazz> entityList) {
        entityList.forEach(entity -> {
            entity.setDefault().validate(true);
        });
        return ApiResponse.ok(this.clazzService.saveBatch(entityList));
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
    public ApiResponse<Boolean> batchUpdate(ClazzParam param, @RequestBody Clazz entity) {
        return ApiResponse.ok(this.clazzService.update(entity, param.toQueryWrapper()));
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
        return ApiResponse.ok(this.clazzService.removeByIds(idList));
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
    public ApiResponse<IPage<Clazz>> selectByPage(
        ClazzParam param,
        @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<Clazz> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<Clazz> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.clazzService.page(page, wrapper));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Long> count(ClazzParam param) {
        QueryWrapper<Clazz> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.clazzService.count(wrapper));
    }

    /**
     * 获得班级列表
     */
//    @GetMapping("/list-all")
//    @ApiOperation("获得班级列表")
//    public ApiResponse<List<Clazz>> getListBySchoolyardYear(
//            @RequestParam(value = "schoolyardId", required = false) Long schoolyardId,
//            @RequestParam(value = "academicYearSemesterId", required = false) Long academicYearSemesterId,
//            @RequestParam(value = "gradeId", required = false) Long gradeId) {
//        return ApiResponse.ok(this.clazzService.getList(schoolyardId, academicYearSemesterId, gradeId));
//    }

    /**
     * 分页查询
     *
     * @param param 查询参数
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return int
     */
    @GetMapping("/search-detail")
    @ApiOperation("分页查询(包含科目)")
    public ApiResponse<IPage<ClazzVo>> searchDetail(
            ClazzParam param,
            @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<Clazz> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<ClazzVo> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.clazzService.pageDetail(page, wrapper));
    }

    /**
     * 获得班级列表
     */
    @GetMapping("/list/teacherDutyClazz")
    @ApiOperation("获得今日值班班级列表（修改值班教师）")
    public ApiResponse<List<ClazzVo>> getListTeacherDutyClazz(@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam Date date,
                                                            @RequestParam TeacherDutyTypeEnum dutyType) {
        return ApiResponse.ok(this.clazzService.getListTeacherDutyClazz(date,dutyType));
    }


    @GetMapping("/import-excel")
    @ApiOperation("导入班级表")
    public ApiResponse<Boolean> importExcel(
            @RequestParam(value = "schoolyardId") Long schoolyardId,
            @RequestParam(value = "academicYearSemesterId") Long academicYearSemesterId,
            @RequestParam(value = "fileUrl") String fileUrl) {
        this.clazzService.importExcel(schoolyardId, academicYearSemesterId, fileUrl);
        return ApiResponse.ok(null);
    }
}
