/**
 * 项目：中华中学管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.DailyAttendance;
import com.zhzx.server.domain.DailyAttendanceSub;
import com.zhzx.server.rest.req.DailyAttendanceParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.DailyAttendanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
@Api(tags = "DailyAttendanceController", description = "日常考勤表管理")
@RequestMapping("/v1/student/daily-attendance")
public class DailyAttendanceController {
    @Resource
    private DailyAttendanceService dailyAttendanceService;

    @PostMapping("/app-save")
    @ApiOperation("app端缺勤田宝")
    public ApiResponse<DailyAttendanceSub> appSave(@RequestBody DailyAttendance entity) {
        return ApiResponse.ok(this.dailyAttendanceService.appSave(entity));
    }

    @DeleteMapping("/app-delete/{id}")
    @ApiOperation("app端缺勤删除")
    public ApiResponse<Integer> detail(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.dailyAttendanceService.appDelete(id));
    }

    @GetMapping("/export-excel")
    @ApiOperation("白班考勤统计")
    @SneakyThrows
    public void exportExcel(@RequestParam(value = "schoolyardId", required = false) Long schoolyardId,
                            @RequestParam(value = "academicYearSemesterId", required = false) Long academicYearSemesterId,
                            @RequestParam(value = "gradeId", required = false) Long gradeId,
                            @RequestParam(value = "clazzId", required = false) Long clazzId,
                            @RequestParam(value = "week", required = false) Integer week,
                            @RequestParam(value = "registerDateFrom", required = false) String registerDateFrom,
                            @RequestParam(value = "registerDateTo", required = false) String registerDateTo,
                            HttpServletResponse response, HttpServletRequest request) {
        XSSFWorkbook book = this.dailyAttendanceService.exportExcel(schoolyardId, academicYearSemesterId, gradeId, clazzId, week, registerDateFrom, registerDateTo);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = "白班考勤统计" + sdf.format(new Date()) + ".xlsx";
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

    @GetMapping("/common-query")
    @ApiOperation("分页查询日常考勤汇总")
    public ApiResponse<IPage<DailyAttendanceSub>> commonQuery(
            @RequestParam(value = "schoolyardId", required = false) Long schoolyardId,
            @RequestParam(value = "academicYearSemesterId", required = false) Long academicYearSemesterId,
            @RequestParam(value = "gradeId", required = false) Long gradeId,
            @RequestParam(value = "clazzId", required = false) Long clazzId,
            @RequestParam(value = "week", required = false) Integer week,
            @RequestParam(value = "registerDateFrom", required = false) String registerDateFrom,
            @RequestParam(value = "registerDateTo", required = false) String registerDateTo,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        IPage<DailyAttendanceSub> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.dailyAttendanceService.commonQuery(page, schoolyardId, academicYearSemesterId, gradeId, clazzId, week, registerDateFrom, registerDateTo));
    }

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<DailyAttendance> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.dailyAttendanceService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<DailyAttendance> add(@RequestBody DailyAttendance entity) {
        entity.setDefault().validate(true);
        this.dailyAttendanceService.save(entity);
        return ApiResponse.ok(this.dailyAttendanceService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<DailyAttendance> update(@RequestBody DailyAttendance entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.dailyAttendanceService.updateAllFieldsById(entity);
        } else {
            this.dailyAttendanceService.updateById(entity);
        }
        return ApiResponse.ok(this.dailyAttendanceService.getById(entity.getId()));
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
        return ApiResponse.ok(this.dailyAttendanceService.removeById(id));
    }

    /**
     * 批量新增
     *
     * @param entityList 要新增的对象
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-save")
    @ApiOperation("批量新增")
    public ApiResponse<Boolean> batchSave(@RequestBody List<DailyAttendance> entityList) {
        entityList.forEach(entity -> {
            entity.setDefault().validate(true);
        });
        return ApiResponse.ok(this.dailyAttendanceService.saveBatch(entityList));
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
    public ApiResponse<Boolean> batchUpdate(DailyAttendanceParam param, @RequestBody DailyAttendance entity) {
        return ApiResponse.ok(this.dailyAttendanceService.update(entity, param.toQueryWrapper()));
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
        return ApiResponse.ok(this.dailyAttendanceService.removeByIds(idList));
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
    public ApiResponse<IPage<DailyAttendance>> selectByPage(
        DailyAttendanceParam param,
        @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<DailyAttendance> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<DailyAttendance> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.dailyAttendanceService.page(page, wrapper));
    }

    /**
     * 单个查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/one")
    @ApiOperation("单个查询")
    public ApiResponse<DailyAttendance> selectOne(DailyAttendanceParam param) {
        QueryWrapper<DailyAttendance> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.dailyAttendanceService.getOne(wrapper, false));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Integer> count(DailyAttendanceParam param) {
        QueryWrapper<DailyAttendance> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.dailyAttendanceService.count(wrapper));
    }

    /**
     * 分页查询日常考勤汇总
     */
    @GetMapping("/search-daily-attendance")
    @ApiOperation("分页查询日常考勤汇总")
    public ApiResponse<IPage<DailyAttendance>> searchByPage(
            @RequestParam(value = "schoolyardId", required = false) Long schoolyardId,
            @RequestParam(value = "academicYearSemesterId", required = false) Long academicYearSemesterId,
            @RequestParam(value = "gradeId", required = false) Long gradeId,
            @RequestParam(value = "clazzId", required = false) Long clazzId,
            @RequestParam(value = "week", required = false) Integer week,
            @RequestParam(value = "registerDateFrom", required = false) String registerDateFrom,
            @RequestParam(value = "registerDateTo", required = false) String registerDateTo,
            @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        IPage<DailyAttendance> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.dailyAttendanceService.searchDailyAttendance(page, schoolyardId, academicYearSemesterId, gradeId, clazzId, week, registerDateFrom, registerDateTo, orderByClause));
    }
}
