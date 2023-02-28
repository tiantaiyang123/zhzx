/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest;

import java.io.IOException;
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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;
import com.zhzx.server.domain.Grade;
import com.zhzx.server.rest.req.GradeParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.GradeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Api(tags = "GradeController", description = "年级表管理")
@RequestMapping("/v1/system/grade")
public class GradeController {
    @Resource
    private GradeService gradeService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<Grade> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.gradeService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<Grade> add(@RequestBody Grade entity) {
        entity.setDefault().validate(true);
        this.gradeService.save(entity);
        return ApiResponse.ok(this.gradeService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<Grade> update(@RequestBody Grade entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.gradeService.updateAllFieldsById(entity);
        } else {
            this.gradeService.updateById(entity);
        }
        return ApiResponse.ok(this.gradeService.getById(entity.getId()));
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
        return ApiResponse.ok(this.gradeService.removeById(id));
    }

    /**
     * 批量新增
     *
     * @param entityList 要新增的对象
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-save")
    @ApiOperation("批量新增")
    public ApiResponse<Boolean> batchSave(@RequestBody List<Grade> entityList) {
        entityList.forEach(entity -> {
            entity.setDefault().validate(true);
        });
        return ApiResponse.ok(this.gradeService.saveBatch(entityList));
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
    public ApiResponse<Boolean> batchUpdate(GradeParam param, @RequestBody Grade entity) {
        return ApiResponse.ok(this.gradeService.update(entity, param.toQueryWrapper()));
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
        return ApiResponse.ok(this.gradeService.removeByIds(idList));
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
    public ApiResponse<IPage<Grade>> selectByPage(
        GradeParam param,
        @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<Grade> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<Grade> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.gradeService.page(page, wrapper));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Long> count(GradeParam param) {
        QueryWrapper<Grade> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.gradeService.count(wrapper));
    }

//    /**
//     * 获得当前学年年级
//     *
//     * @return int
//     */
//    @GetMapping("/current-grades")
//    @ApiOperation("获得当前学年年级")
//    public ApiResponse<List<Grade>> getCurrentGrades() {
//        return ApiResponse.ok(this.gradeService.getCurrentGrades());
//    }
//
//    /**
//     * 根据学期获得年级
//     */
//    @GetMapping("/semester-grades")
//    @ApiOperation("获得当前学年年级")
//    public ApiResponse<List<Grade>> getSemesterGrades(@RequestParam(value = "academicYearSemesterId") Long academicYearSemesterId) {
//        return ApiResponse.ok(this.gradeService.getSemesterGrades(academicYearSemesterId));
//    }


//    @GetMapping("/import-excel")
//    @ApiOperation("导入年级表")
//    public ApiResponse<Boolean> importExcel(@RequestParam(value = "fileUrl") String fileUrl) {
//        this.gradeService.importExcel(fileUrl);
//        return ApiResponse.ok(null);
//    }
//
//    @GetMapping("/export-excel")
//    @ApiOperation("导出年级表")
//    public void exportExcel(HttpServletResponse response, HttpServletRequest request) throws IOException, InvalidFormatException {
//        XSSFWorkbook book = this.gradeService.exportExcel();
//        OutputStream output = response.getOutputStream();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//        String fileName = "年级列表(" + sdf.format(new Date()) + ").xlsx";
//        //获取浏览器使用的编码
//        String encoding = request.getCharacterEncoding();
//        if (encoding != null && encoding.length() > 0) {
//            fileName = URLEncoder.encode(fileName, encoding);
//        } else {
//            //默认编码是utf-8
//            fileName = URLEncoder.encode(fileName, "UTF-8");
//        }
//        response.reset();
//        response.setCharacterEncoding(encoding);
//        response.setContentType("application/octet-stream;charset=UTF-8");
//        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
//        OutputStream out = response.getOutputStream();
//        book.write(out);
//        out.flush();
//        out.close();
//        book.close();
//    }
}
