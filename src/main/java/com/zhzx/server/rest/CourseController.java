/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.Course;
import com.zhzx.server.domain.Settings;
import com.zhzx.server.domain.User;
import com.zhzx.server.rest.req.CourseParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.CourseService;
import com.zhzx.server.service.SettingsService;
import com.zhzx.server.util.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.hasor.dataway.dal.HeaderData;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Api(tags = "CourseController", description = "课程表管理")
@RequestMapping("/v1/system/course")
public class CourseController {
    @Resource
    private CourseService courseService;
    @Resource
    private SettingsService settingsService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<Course> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.courseService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<Course> add(@RequestBody Course entity) {
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        entity.setEditorId(user.getId());
        entity.setEditorName(user.getUsername());
        entity.setDefault().validate(true);
        this.courseService.save(entity);
        return ApiResponse.ok(this.courseService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<Course> update(@RequestBody Course entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.courseService.updateAllFieldsById(entity);
        } else {
            this.courseService.updateById(entity);
        }
        return ApiResponse.ok(this.courseService.getById(entity.getId()));
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
        return ApiResponse.ok(this.courseService.removeById(id));
    }

    /**
     * 批量新增
     *
     * @param entityList 要新增的对象
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-save")
    @ApiOperation("批量新增")
    public ApiResponse<Boolean> batchSave(@RequestBody List<Course> entityList) {
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        entityList.forEach(entity -> {
            entity.setEditorId(user.getId());
            entity.setEditorName(user.getUsername());
            entity.setDefault().validate(true);
        });
        return ApiResponse.ok(this.courseService.saveBatch(entityList));
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
    public ApiResponse<Boolean> batchUpdate(CourseParam param, @RequestBody Course entity) {
        return ApiResponse.ok(this.courseService.update(entity, param.toQueryWrapper()));
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
        return ApiResponse.ok(this.courseService.removeByIds(idList));
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
    public ApiResponse<IPage<Course>> selectByPage(
        CourseParam param,
        @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<Course> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<Course> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.courseService.page(page, wrapper));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Long> count(CourseParam param) {
        QueryWrapper<Course> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.courseService.count(wrapper));
    }

    @GetMapping("/list-all")
    @ApiOperation("获得年级课程表")
    public ApiResponse<List<Map<String, String>>> getList(
            @RequestParam(value = "academicYearSemesterId", required = false) Long academicYearSemesterId,
            @RequestParam(value = "gradeId", required = false) Long gradeId,
            @RequestParam(value = "clazzId", required = false) Long clazzId) {
        Settings settings = settingsService.getOne(Wrappers.<Settings>lambdaQuery()
                .like(Settings::getCode, "COURSE\\_" + academicYearSemesterId + "\\_" + gradeId + "\\_" + clazzId+"\\_")
        );
        if (settings == null)
            return ApiResponse.ok(new ArrayList<>());
        return ApiResponse.ok(JSON.parseArray(settings.getParams()));
    }

    @GetMapping("/list-picture")
    @ApiOperation("获得年级课程表(图片)")
    public void getListPicture(
            HttpServletResponse response, HttpServletRequest request,
            @RequestParam(value = "academicYearSemesterId", required = false) Long academicYearSemesterId,
            @RequestParam(value = "gradeId", required = false) Long gradeId,
            @RequestParam(value = "clazzId", required = false) Long clazzId) throws IOException, Exception  {
        BufferedImage image = this.courseService.getImage(academicYearSemesterId, gradeId,clazzId);
        if (image != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String fileName = "课程表(" + sdf.format(new Date()) + ").jpg";
            //获取浏览器使用的编码
            String encoding = request.getCharacterEncoding();
            if (encoding != null && encoding.length() > 0) {
                fileName = URLEncoder.encode(fileName, encoding);
            } else {
                //默认编码是utf-8
                fileName = URLEncoder.encode(fileName, "UTF-8");
            }
//            response.reset();
            String headers = response.getHeaderNames().stream().map(item -> item.concat(": ").concat(response.getHeader(item))).collect(Collectors.joining(","));
            log.info(">>>>>>>>>>>>>>>当前响应头 {}", headers);
            response.setCharacterEncoding(encoding);
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.addHeader("Access-Control-Expose-Headers", "Content-disposition");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            headers = response.getHeaderNames().stream().map(item -> item.concat(": ").concat(response.getHeader(item))).collect(Collectors.joining(","));
            log.info(">>>>>>>>>>>>>>>修改后响应头 {}", headers);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "jpg", out);
            out.flush();
            out.close();
        }
    }

    @GetMapping("/import-excel")
    @ApiOperation("导入课程表")
    public ApiResponse<Boolean> importExcel(
            @RequestParam(value = "academicYearSemesterId") Long academicYearSemesterId,
            @RequestParam(value = "gradeId") Long gradeId,
            @RequestParam(value = "fileUrl") String fileUrl) {
        this.courseService.importExcel(academicYearSemesterId, gradeId, fileUrl);
        return ApiResponse.ok(null);
    }

    @GetMapping("/get/staff/teacher/course")
    @ApiOperation("获取当前登录老师课程安排")
    public ApiResponse<Map<String,Object>> getStaffTeacherCourse(@RequestParam(required = false)Long staffId) {
        return ApiResponse.ok(this.courseService.getStaffTeacherCourse(staffId));
    }

    @GetMapping("/get/staff/teacher/clazz")
    @ApiOperation("获取当前登录老师课程班级")
    public ApiResponse<Map<String,Object>> getStaffTeacherClazz(@RequestParam(required = false)Long staffId) {
        return ApiResponse.ok(courseService.getStaffTeacherClazz(staffId));
    }

    @GetMapping("/get/subject/course")
    @ApiOperation("获取备课组课表")
    public ApiResponse<Map<String,Object>> getSubjectCourse(@RequestParam(value = "academicYearSemesterId",required = false) Long academicYearSemesterId,
                                                                @RequestParam(value = "gradeId") Long gradeId,
                                                                @RequestParam(value = "subjectId") Long subjectId) {
        return ApiResponse.ok(courseService.getListSubject(academicYearSemesterId,gradeId,subjectId));
    }
}
