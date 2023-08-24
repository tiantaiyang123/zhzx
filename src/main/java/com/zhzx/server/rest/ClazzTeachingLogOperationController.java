/**
 * 项目：中华中学管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Arrays;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.ClazzTeachingLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import lombok.SneakyThrows;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.domain.ClazzTeachingLogOperation;
import com.zhzx.server.rest.req.ClazzTeachingLogOperationParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.ClazzTeachingLogOperationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Api(tags = "ClazzTeachingLogOperationController", description = "班级教学日志作业量反馈表管理")
@RequestMapping("/v1/student/clazz-teaching-log-operation")
public class ClazzTeachingLogOperationController {
    @Resource
    private ClazzTeachingLogOperationService clazzTeachingLogOperationService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<ClazzTeachingLogOperation> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.clazzTeachingLogOperationService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<ClazzTeachingLogOperation> add(@RequestBody ClazzTeachingLogOperation entity) {
        entity.setDefault().validate(true);
        this.clazzTeachingLogOperationService.save(entity);
        return ApiResponse.ok(this.clazzTeachingLogOperationService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<ClazzTeachingLogOperation> update(@RequestBody ClazzTeachingLogOperation entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.clazzTeachingLogOperationService.updateAllFieldsById(entity);
        } else {
            this.clazzTeachingLogOperationService.updateById(entity);
        }
        return ApiResponse.ok(this.clazzTeachingLogOperationService.getById(entity.getId()));
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
        return ApiResponse.ok(this.clazzTeachingLogOperationService.removeById(id));
    }

    /**
     * 批量新增
     *
     * @param entityList 要新增的对象
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-save")
    @ApiOperation("批量新增")
    public ApiResponse<Boolean> batchSave(@RequestBody List<ClazzTeachingLogOperation> entityList) {
        entityList.forEach(entity -> {
            entity.setDefault().validate(true);
        });
        return ApiResponse.ok(this.clazzTeachingLogOperationService.saveBatch(entityList));
    }

    /**
     * 更新对象列表
     *
     * @param entityList 要更新的对象列表
     * @return boolean 成功或失败
     */
    @PostMapping("/update-list")
    @ApiOperation("更新对象列表")
    public ApiResponse<Boolean> updateList(@RequestBody List<ClazzTeachingLogOperation> entityList) {
        return ApiResponse.ok(this.clazzTeachingLogOperationService.updateList(entityList));
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
    public ApiResponse<Boolean> batchUpdate(ClazzTeachingLogOperationParam param, @RequestBody ClazzTeachingLogOperation entity) {
        return ApiResponse.ok(this.clazzTeachingLogOperationService.update(entity, param.toQueryWrapper()));
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
        return ApiResponse.ok(this.clazzTeachingLogOperationService.removeByIds(idList));
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
    public ApiResponse<IPage<ClazzTeachingLogOperation>> selectByPage(
        ClazzTeachingLogOperationParam param,
        @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<ClazzTeachingLogOperation> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<ClazzTeachingLogOperation> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.clazzTeachingLogOperationService.page(page, wrapper));
    }

    /**
     * 单个查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/one")
    @ApiOperation("单个查询")
    public ApiResponse<ClazzTeachingLogOperation> selectOne(ClazzTeachingLogOperationParam param) {
        QueryWrapper<ClazzTeachingLogOperation> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.clazzTeachingLogOperationService.getOne(wrapper, false));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Integer> count(ClazzTeachingLogOperationParam param) {
        QueryWrapper<ClazzTeachingLogOperation> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.clazzTeachingLogOperationService.count(wrapper));
    }

    @GetMapping("/export-excel")
    @ApiOperation("导出平均作业量")
    @SneakyThrows
    public void exportExcel(
            @RequestParam(value = "schoolyardId", required = false) Long schoolyardId,
            @RequestParam(value = "academicYearSemesterId", required = false) Long academicYearSemesterId,
            @RequestParam(value = "gradeId", required = false) Long gradeId,
            @RequestParam(value = "clazzId", required = false) Long clazzId,
            @RequestParam(value = "week", required = false) Integer week,
            @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam(value = "startTime", required = false) Date startTime,
            @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam(value = "endTime",required = false) Date endTime,
            HttpServletResponse response, HttpServletRequest request
    ) {
        XSSFWorkbook book = this.clazzTeachingLogOperationService.exportExcel(schoolyardId, academicYearSemesterId, gradeId, clazzId, week, startTime, endTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = "作业量统计" + sdf.format(new Date()) + ".xlsx";
        //获取浏览器使用的编码
        String encoding = request.getCharacterEncoding();
        if (encoding != null && encoding.length() > 0) {
            fileName = URLEncoder.encode(fileName, encoding);
        } else {
            //默认编码是utf-8
            fileName = URLEncoder.encode(fileName, "UTF-8");
        }
        response.reset();
        response.setCharacterEncoding(encoding);
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        OutputStream out = response.getOutputStream();
        book.write(out);
        out.flush();
        out.close();
        book.close();
    }

}
