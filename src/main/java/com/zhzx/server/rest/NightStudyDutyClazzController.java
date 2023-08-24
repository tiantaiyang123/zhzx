/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.NightStudyDutyClazz;
import com.zhzx.server.dto.NightDutyClassDto;
import com.zhzx.server.enums.TeacherDutyTypeEnum;
import com.zhzx.server.rest.req.NightStudyDutyClazzParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.NightStudyDutyClazzService;
import com.zhzx.server.service.NightStudyDutyService;
import com.zhzx.server.service.TeacherDutyService;
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
import java.util.*;

@Slf4j
@RestController
@Api(tags = "NightStudyDutyClazzController", description = "晚自习行政值班班级情况表管理")
@RequestMapping("/v1/day/night-study-duty-clazz")
public class NightStudyDutyClazzController {
    @Resource
    private NightStudyDutyClazzService nightStudyDutyClazzService;
    @Resource
    private NightStudyDutyService nightStudyDutyService;
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
    public ApiResponse<NightStudyDutyClazz> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.nightStudyDutyClazzService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<NightStudyDutyClazz> add(@RequestBody NightStudyDutyClazz entity) {
        entity.setDefault().validate(true);
        this.nightStudyDutyClazzService.save(entity);
        return ApiResponse.ok(this.nightStudyDutyClazzService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<NightStudyDutyClazz> update(@RequestBody NightStudyDutyClazz entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.nightStudyDutyClazzService.updateAllFieldsById(entity);
        } else {
            this.nightStudyDutyClazzService.updateById(entity);
        }
        return ApiResponse.ok(this.nightStudyDutyClazzService.getById(entity.getId()));
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
        return ApiResponse.ok(this.nightStudyDutyClazzService.removeById(id));
    }

    /**
     * 批量新增
     *
     * @param entityList 要新增的对象
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-save")
    @ApiOperation("批量新增")
    public ApiResponse<Boolean> batchSave(@RequestBody List<NightStudyDutyClazz> entityList) {
        entityList.forEach(entity -> {
            entity.setDefault().validate(true);
        });
        return ApiResponse.ok(this.nightStudyDutyClazzService.saveBatch(entityList));
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
    public ApiResponse<Boolean> batchUpdate(NightStudyDutyClazzParam param, @RequestBody NightStudyDutyClazz entity) {
        return ApiResponse.ok(this.nightStudyDutyClazzService.update(entity, param.toQueryWrapper()));
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
        return ApiResponse.ok(this.nightStudyDutyClazzService.removeByIds(idList));
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
    public ApiResponse<IPage<NightStudyDutyClazz>> selectByPage(
        NightStudyDutyClazzParam param,
        @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<NightStudyDutyClazz> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<NightStudyDutyClazz> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.nightStudyDutyClazzService.page(page, wrapper));
    }

    @GetMapping("/refresh")
    @ApiOperation("pad定时刷新人数")
    public ApiResponse<Object> refresh() {
        return ApiResponse.ok(this.nightStudyDutyClazzService.refresh());
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Integer> count(NightStudyDutyClazzParam param) {
        QueryWrapper<NightStudyDutyClazz> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.nightStudyDutyClazzService.count(wrapper));
    }

    /**
     * count查询
     *
     * @return int
     */
    @PostMapping("/create/or/update")
    @ApiOperation("创建或者修改")
    public ApiResponse<NightDutyClassDto> createOrUpdate(@RequestBody NightDutyClassDto nightDutyClassDto) {

        return ApiResponse.ok(this.nightStudyDutyClazzService.createOrUpdate(nightDutyClassDto));
    }

    @GetMapping("/back/search")
    @ApiOperation("晚自习分页查询")
    public ApiResponse<Map<String, Object>> selectByPage(@RequestParam(value = "schoolyardId", required = false) Long schoolyardId,
                                           @RequestParam(value = "name",required = false) String name,
                                           @RequestParam(value = "clazzName",required = false) String clazzName,
                                           @RequestParam(value = "gradeId",required = false) Long gradeId,
                                           @RequestParam(value = "clazzId",required = false) Long clazzId,
                                           @RequestParam(value = "dutyType",defaultValue = "STAGE_ONE") TeacherDutyTypeEnum dutyType,
                                           @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam(value = "startTime", required = false) Date startTime,
                                           @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam(value = "endTime",required = false) Date endTime,
                                           @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                           @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
//        IPage leaderPage = new Page<>(pageNum, pageSize);
        // 嵌套映射会让默认分页查询出问题 这里单独查总数
        IPage teacherPage = new Page<>(pageNum, pageSize, false);
        Map<String,Object> map = new HashMap<>();
        IPage teacher = this.teacherDutyService.pageDetail(teacherPage,dutyType,name,clazzName,startTime,endTime,gradeId,clazzId, schoolyardId);
//        IPage leader = this.nightStudyDutyClazzService.pageDetail(leaderPage,dutyType,name,clazzName,startTime,endTime,gradeId,clazzId, schoolyardId);
        map.put("teacher",teacher);
//        map.put("leader",leader);
        return ApiResponse.ok(map);
    }

    @GetMapping("/back/search/export-excel")
    @ApiOperation("晚自习分页查询(导出excel)")
    @SneakyThrows
    public void tableInfoClazzExportExcel(@RequestParam(value = "schoolyardId", required = false) Long schoolyardId,
                                          @RequestParam(value = "name",required = false) String name,
                                          @RequestParam(value = "clazzName",required = false) String clazzName,
                                          @RequestParam(value = "gradeId",required = false) Long gradeId,
                                          @RequestParam(value = "clazzId",required = false) Long clazzId,
                                          @RequestParam(value = "dutyType",defaultValue = "STAGE_ONE") TeacherDutyTypeEnum dutyType,
                                          @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam(value = "startTime", required = false) Date startTime,
                                          @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam(value = "endTime",required = false) Date endTime,
                                          HttpServletResponse response, HttpServletRequest request) {
        XSSFWorkbook book = this.nightStudyDutyClazzService.exportExcel(dutyType,name,clazzName,startTime,endTime,gradeId,clazzId, schoolyardId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = "晚自习值班情况" + sdf.format(new Date()) + ".xlsx";
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
     * 领导晚班确认
     *
     * @return int
     */
    @PostMapping("/leader/confirm")
    @ApiOperation("领导晚班确认")
    public ApiResponse<Integer> leaderConfirm(@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")@RequestParam Date time,
                                           @RequestParam Long clazzId,
                                           @RequestParam TeacherDutyTypeEnum dutyType) {

        return ApiResponse.ok(this.nightStudyDutyClazzService.leaderConfirm(time,clazzId,dutyType));
    }
}
