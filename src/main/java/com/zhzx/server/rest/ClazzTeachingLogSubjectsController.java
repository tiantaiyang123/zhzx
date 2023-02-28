/**
 * 项目：中华中学管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.ClazzTeachingLogSubjects;
import com.zhzx.server.rest.req.ClazzTeachingLogSubjectsParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.ClazzTeachingLogSubjectsService;
import com.zhzx.server.vo.ClazzTeachingLogSubjectsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@Api(tags = "ClazzTeachingLogSubjectsController", description = "班级教学日志科目表管理")
@RequestMapping("/v1/student/clazz-teaching-log-subjects")
public class ClazzTeachingLogSubjectsController {
    @Resource
    private ClazzTeachingLogSubjectsService clazzTeachingLogSubjectsService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<ClazzTeachingLogSubjects> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.clazzTeachingLogSubjectsService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<ClazzTeachingLogSubjects> add(@RequestBody ClazzTeachingLogSubjects entity) {
        entity.setDefault().validate(true);
        this.clazzTeachingLogSubjectsService.save(entity);
        return ApiResponse.ok(this.clazzTeachingLogSubjectsService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<ClazzTeachingLogSubjects> update(@RequestBody ClazzTeachingLogSubjects entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.clazzTeachingLogSubjectsService.updateAllFieldsById(entity);
        } else {
            this.clazzTeachingLogSubjectsService.updateById(entity);
        }
        return ApiResponse.ok(this.clazzTeachingLogSubjectsService.getById(entity.getId()));
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
        return ApiResponse.ok(this.clazzTeachingLogSubjectsService.removeById(id));
    }

    /**
     * 批量新增
     *
     * @param entityList 要新增的对象
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-save")
    @ApiOperation("批量新增")
    public ApiResponse<Boolean> batchSave(@RequestBody List<ClazzTeachingLogSubjects> entityList) {
        entityList.forEach(entity -> {
            entity.setDefault().validate(true);
        });
        return ApiResponse.ok(this.clazzTeachingLogSubjectsService.saveBatch(entityList));
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
    public ApiResponse<Boolean> batchUpdate(ClazzTeachingLogSubjectsParam param, @RequestBody ClazzTeachingLogSubjects entity) {
        return ApiResponse.ok(this.clazzTeachingLogSubjectsService.update(entity, param.toQueryWrapper()));
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
        return ApiResponse.ok(this.clazzTeachingLogSubjectsService.removeByIds(idList));
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
    public ApiResponse<IPage<ClazzTeachingLogSubjects>> selectByPage(
            ClazzTeachingLogSubjectsParam param,
            @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<ClazzTeachingLogSubjects> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<ClazzTeachingLogSubjects> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.clazzTeachingLogSubjectsService.page(page, wrapper));
    }

    /**
     * 单个查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/one")
    @ApiOperation("单个查询")
    public ApiResponse<ClazzTeachingLogSubjects> selectOne(ClazzTeachingLogSubjectsParam param) {
        QueryWrapper<ClazzTeachingLogSubjects> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.clazzTeachingLogSubjectsService.getOne(wrapper, false));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Long> count(ClazzTeachingLogSubjectsParam param) {
        QueryWrapper<ClazzTeachingLogSubjects> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.clazzTeachingLogSubjectsService.count(wrapper));
    }

    /**
     * 分页查询审核列表
     *
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @return int
     */
    @GetMapping("/list-audit")
    @ApiOperation("分页查询审核列表")
    public ApiResponse<IPage<ClazzTeachingLogSubjectsVo>> listAuditPage(
            @RequestParam(value = "schoolyardId", required = false) Long schoolyardId,
            @RequestParam(value = "academicYearSemesterId", required = false) Long academicYearSemesterId,
            @RequestParam(value = "gradeId", required = false) Long gradeId,
            @RequestParam(value = "clazzId", required = false) Long clazzId,
            @RequestParam(value = "week", required = false) Integer week,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        IPage<ClazzTeachingLogSubjectsVo> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.clazzTeachingLogSubjectsService.listAuditPage(page, schoolyardId, academicYearSemesterId, gradeId, clazzId, week, state));
    }

    /**
     * 审核批量同意
     * @return int
     */
    @PostMapping("/batch-audit")
    @ApiOperation("审核批量同意")
    public ApiResponse<IPage<ClazzTeachingLogSubjectsVo>> batchAudit(@RequestBody List<Long> idList) {
        this.clazzTeachingLogSubjectsService.batchAudit(idList);
        return ApiResponse.ok(null);
    }

    @PostMapping("/student-sure")
    @ApiOperation("学生一键确定")
    public ApiResponse<Integer> studentSure(@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam(value = "startTime", required = false) Date startTime) {
        return ApiResponse.ok(this.clazzTeachingLogSubjectsService.studentSure(startTime));
    }

    @GetMapping("/export-excel")
    @ApiOperation("导出教学日志")
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
        XSSFWorkbook book = this.clazzTeachingLogSubjectsService.exportExcel(schoolyardId, academicYearSemesterId, gradeId, clazzId, week, startTime, endTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = "教学日志统计" + sdf.format(new Date()) + ".xlsx";
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
