/**
* 项目：中华中学管理平台
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.NightStudyAttendanceSub;
import com.zhzx.server.rest.req.NightStudyAttendanceSubParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.NightStudyAttendanceSubService;
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

@Slf4j
@RestController
@Api(tags = "NightStudyAttendanceSubController", description = "晚自习考勤班级概况表管理")
@RequestMapping("/v1/student/night-study-attendance-sub")
public class NightStudyAttendanceSubController {
    @Resource
    private NightStudyAttendanceSubService nightStudyAttendanceSubService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<NightStudyAttendanceSub> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.nightStudyAttendanceSubService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<NightStudyAttendanceSub> add(@RequestBody NightStudyAttendanceSub entity) {
        entity.setDefault().validate(true);
        this.nightStudyAttendanceSubService.save(entity);
        return ApiResponse.ok(this.nightStudyAttendanceSubService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<NightStudyAttendanceSub> update(@RequestBody NightStudyAttendanceSub entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.nightStudyAttendanceSubService.updateAllFieldsById(entity);
        } else {
            this.nightStudyAttendanceSubService.updateById(entity);
        }
        return ApiResponse.ok(this.nightStudyAttendanceSubService.getById(entity.getId()));
    }

    /**
     * 删除
     *
     * @param id 要删除的对象id
     * @return int
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除")
    public ApiResponse<Integer> update(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.nightStudyAttendanceSubService.removeById(id));
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
    public ApiResponse<IPage<NightStudyAttendanceSub>> selectByPage(
        NightStudyAttendanceSubParam param,
        @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<NightStudyAttendanceSub> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<NightStudyAttendanceSub> authorityPage = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.nightStudyAttendanceSubService.page(authorityPage, wrapper));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Long> count(NightStudyAttendanceSubParam param) {
        QueryWrapper<NightStudyAttendanceSub> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.nightStudyAttendanceSubService.count(wrapper));
    }

    @GetMapping("/export-excel")
    @ApiOperation("导出晚自习学生填报情况")
    @SneakyThrows
    public void exportExcel(
            @RequestParam(value = "schoolyardId", required = false) Long schoolyardId,
            @RequestParam(value = "academicYearSemesterId") Long academicYearSemesterId,
            @RequestParam(value = "gradeId", required = false) Long gradeId,
            @RequestParam(value = "clazzId", required = false) Long clazzId,
            @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam(value = "startTime") Date startTime,
            @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam(value = "endTime") Date endTime,
            HttpServletResponse response, HttpServletRequest request
    ) {
        XSSFWorkbook book = this.nightStudyAttendanceSubService.exportExcel(schoolyardId, academicYearSemesterId, gradeId, clazzId, startTime, endTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = "晚自习学生填报统计" + sdf.format(new Date()) + ".xlsx";
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
