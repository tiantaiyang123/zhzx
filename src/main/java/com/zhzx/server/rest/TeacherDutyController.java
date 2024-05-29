/**
 * 项目：中华中学流程自动化管理平台
 *
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
 */

package com.zhzx.server.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.Staff;
import com.zhzx.server.domain.TeacherDuty;
import com.zhzx.server.dto.NightDutyClassDto;
import com.zhzx.server.dto.TeacherServerFormDto;
import com.zhzx.server.enums.TeacherDutyTypeEnum;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.rest.req.TeacherDutyParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.TeacherDutyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Api(tags = "TeacherDutyController", description = "教师值班表管理")
@RequestMapping("/v1/day/teacher-duty")
public class TeacherDutyController {
    @Resource
    private TeacherDutyService teacherDutyService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<TeacherDuty> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.teacherDutyService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<TeacherDuty> add(@RequestBody TeacherDuty entity) {
        entity.setDefault().validate(true);
        this.teacherDutyService.save(entity);
        return ApiResponse.ok(this.teacherDutyService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<TeacherDuty> update(@RequestBody TeacherDuty entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.teacherDutyService.updateAllFieldsById(entity);
        } else {
            this.teacherDutyService.updateById(entity);
        }
        return ApiResponse.ok(this.teacherDutyService.getById(entity.getId()));
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
        return ApiResponse.ok(this.teacherDutyService.removeById(id));
    }

    /**
     * 批量新增
     *
     * @param entityList 要新增的对象
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-save")
    @ApiOperation("批量新增")
    public ApiResponse<Boolean> batchSave(@RequestBody List<TeacherDuty> entityList) {
        entityList.forEach(entity -> {
            entity.setDefault().validate(true);
        });
        return ApiResponse.ok(this.teacherDutyService.saveBatch(entityList));
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
    public ApiResponse<Boolean> batchUpdate(TeacherDutyParam param, @RequestBody TeacherDuty entity) {
        return ApiResponse.ok(this.teacherDutyService.update(entity, param.toQueryWrapper()));
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
        return ApiResponse.ok(this.teacherDutyService.removeByIds(idList));
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
    public ApiResponse<IPage<TeacherDuty>> selectByPage(
            TeacherDutyParam param,
            @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<TeacherDuty> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<TeacherDuty> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.teacherDutyService.page(page, wrapper));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Integer> count(TeacherDutyParam param) {
        QueryWrapper<TeacherDuty> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.teacherDutyService.count(wrapper));
    }

    /**
     * 查询值班表
     *
     * @return int
     */
    @GetMapping("/get/teacherDuty/form")
    @ApiOperation("查询教师值班表")
    public ApiResponse<Page<Map<String, Object>>> getTeacherDutyForm(@RequestParam(value = "schoolyardId", required = false) Long schoolyardId,
                                                                     @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                                     @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam(value = "timeFrom", required = false) Date timeFrom,
                                                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam(value = "timeTo", required = false) Date timeTo,
                                                                     @RequestParam(value = "teacherDutyName", required = false) String teacherDutyName,
                                                                     @RequestParam(value = "gradeId", required = false) Long gradeId,
                                                                     @RequestParam(value = "fromApp", required = false, defaultValue = "NO") YesNoEnum fromApp) {

        return ApiResponse.ok(this.teacherDutyService.getTeacherDutyFormV2(pageNum, pageSize, timeFrom, timeTo, teacherDutyName, gradeId, schoolyardId, fromApp));
    }

    @GetMapping("/export-excel")
    @ApiOperation("导出教师值班表")
    public void exportExcel(HttpServletResponse response, HttpServletRequest request,
                            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam(value = "timeFrom", required = false) Date timeFrom,
                            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam(value = "timeTo", required = false) Date timeTo,
                            @RequestParam(value = "gradeId", required = false) Long gradeId,
                            @RequestParam(value = "schoolyardId", required = false) Long schoolyardId) throws Exception {
        XSSFWorkbook book = this.teacherDutyService.exportExcel(schoolyardId, gradeId, timeFrom, timeTo);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = "值班表" + sdf.format(new Date()) + ".xlsx";
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

    /**
     * 查询值班表
     *
     * @return int
     */
    @PostMapping("/update/dutyMode")
    @ApiOperation("修改当天值班模式")
    public ApiResponse<Integer> updateDutyMode(@RequestBody TeacherServerFormDto teacherServerFormDto) {

        return ApiResponse.ok(this.teacherDutyService.updateDutyMode(teacherServerFormDto));
    }

    @GetMapping("/get/teacherDuty/picture")
    @ApiOperation("查询教师值班表(图片)")
    public void getTeacherDutyPicture(HttpServletResponse response, HttpServletRequest request,
                                      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam(value = "timeFrom", required = false) Date timeFrom,
                                      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam(value = "timeTo", required = false) Date timeTo,
                                      @RequestParam(value = "gradeId", required = false) Long gradeId) throws Exception {
        BufferedImage image = this.teacherDutyService.getImage(timeFrom, timeTo, gradeId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = "值班表(" + sdf.format(new Date()) + ").jpg";
        //获取浏览器使用的编码
        String encoding = request.getCharacterEncoding();
        if (encoding != null && encoding.length() > 0) {
            fileName = URLEncoder.encode(fileName, encoding);
        } else {
            //默认编码是utf-8
            fileName = URLEncoder.encode(fileName, "UTF-8");
        }
        response.setCharacterEncoding(encoding);
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Access-Control-Expose-Headers", "Content-disposition");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        OutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
        out.flush();
        out.close();
    }

    /**
     * 值班老师导入
     *
     * @return int
     */
    @GetMapping("/import/teacher/duty")
    @ApiOperation("值班老师导入")
    public ApiResponse<String> importTeacherDuty(@RequestParam(value = "schoolyardId") Long schoolyardId,
                                                 @RequestParam(value = "academicYearSemesterId") Long academicYearSemesterId,
                                                 @RequestParam(value = "gradeId") Long gradeId,
                                                 @RequestParam(value = "fileUrl") String fileUrl) {
        return ApiResponse.ok(this.teacherDutyService.importTeacherDuty(schoolyardId, academicYearSemesterId, gradeId, fileUrl));
    }

    /**
     * 值班老师帮替值班 ---- app端老师帮人代班
     */
    @PostMapping("/update/teacher/duty")
    @ApiOperation("值班老师帮替值班")
    public ApiResponse<Integer> updateTeacherDuty(@RequestBody NightDutyClassDto nightDutyClassDto) {
        return ApiResponse.ok(this.teacherDutyService.updateTeacherDuty(nightDutyClassDto));
    }

    /**
     * 值班老师取消带班
     */
    @PostMapping("/cancel/teacher/duty")
    @ApiOperation("值班老师取消带班")
    public ApiResponse<Integer> cancelTeacherDuty(@RequestBody NightDutyClassDto nightDutyClassDto) {
        return ApiResponse.ok(this.teacherDutyService.cancelTeacherDuty(nightDutyClassDto));
    }

    /**
     * 值班老师取消带班
     */
    @GetMapping("/cancel/teacher/list")
    @ApiOperation("值班老师取消值班可选老师列表")
    public ApiResponse<List<Staff>> cancelTeacherList(@RequestParam Long teacherDutyId) {
        return ApiResponse.ok(this.teacherDutyService.cancelTeacherList(teacherDutyId));
    }

    /**
     * 查询值班表
     *
     * @return int
     */
    @GetMapping("/get/teacherDuty/byStaffId")
    @ApiOperation("根据教师查询值班")
    public ApiResponse<List<TeacherServerFormDto>> getTeacherDutyByStaffId(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam(value = "timeFrom", required = false) Date timeFrom,
                                                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam(value = "timeTo", required = false) Date timeTo) {

        return ApiResponse.ok(this.teacherDutyService.getTeacherDutyByStaffId(timeFrom, timeTo));
    }

    @GetMapping("/work-amount/export-excel")
    @ApiOperation("导出工作量")
    @SneakyThrows
    public void exportExcel(
            @RequestParam(value = "schoolyardId", required = false) Long schoolyardId,
            @RequestParam(value = "gradeId", required = false) Long gradeId,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam(value = "startTime", required = false) Date startTime,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam(value = "endTime", required = false) Date endTime,
            HttpServletResponse response, HttpServletRequest request
    ) {
        XSSFWorkbook book = this.teacherDutyService.exportExcelWorkAmount(schoolyardId, gradeId, startTime, endTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = "晚自习工作量统计" + sdf.format(new Date()) + ".xlsx";
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

    /**
     * 返回查询到的原来值班的信息
     */
    @GetMapping("/old-teacher-message")
    @ApiOperation("原先值班的教师返回")
    public ApiResponse<String> getTeacherDuty(@RequestParam(value = "schoolyardId", required = false) Long schoolyardId,
                                              @RequestParam(value = "gradeId", required = false) Long gradeId,
                                              @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam(value = "time", required = false) Date time,
                                              @RequestParam(value = "classId", required = false) Long classId,
                                              @RequestParam(value = "stage", required = false) TeacherDutyTypeEnum stage) {
        String oneTeacher = this.teacherDutyService.searchOneTeacher(schoolyardId, gradeId, time, classId, stage);
        return ApiResponse.ok(oneTeacher);
    }

    /**
     * 更新值班的信息
     *
     * @return
     */
    @PostMapping("/update/teacher-message")
    @ApiOperation("变更值班老师的")
    public ApiResponse<Integer> updateTeacherDutyByName(@RequestBody NightDutyClassDto nightDutyClassDto) {
        Integer integer = this.teacherDutyService.updateOneTeacher(nightDutyClassDto);
        return ApiResponse.ok(integer);
    }


}
