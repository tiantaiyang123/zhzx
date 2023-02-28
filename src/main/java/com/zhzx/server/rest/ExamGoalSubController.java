/**
* 项目：中华中学管理平台
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.ExamGoalSub;
import com.zhzx.server.rest.req.ExamGoalSubParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.ExamGoalSubService;
import com.zhzx.server.vo.ExamGoalSubTotalVo;
import com.zhzx.server.vo.ExamGoalSubVo;
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
import java.util.Map;

@Slf4j
@RestController
@Api(tags = "ExamGoalSubController", description = "考试分段表管理")
@RequestMapping("/v1/data/exam-goal-sub")
public class ExamGoalSubController {
    @Resource
    private ExamGoalSubService examGoalSubService;

    @GetMapping("/average")
    @ApiOperation("均分统计")
    public ApiResponse<Map<String, Object>> gradeAnalyseClazz(@RequestParam(value = "examId", required = false) Long examId) {
        return ApiResponse.ok(this.examGoalSubService.clazzAverage(examId));
    }

    @GetMapping("/average/export-excel")
    @ApiOperation("均分统计(导出excel)")
    @SneakyThrows
    public void gradeAnalyseClazzExportExcel(
            @RequestParam(value = "examId", required = false) Long examId,
            HttpServletResponse response, HttpServletRequest request) {
        XSSFWorkbook book = this.examGoalSubService.clazzAverageExportExcel(examId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = "班级成绩对比表(" + sdf.format(new Date()) + ").xlsx";
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

    @PostMapping("/partition")
    @ApiOperation("分段统计")
    public ApiResponse<Map<String, Object>> partition(
            @RequestBody ExamGoalSubTotalVo examGoalSubTotalVo
    ) {
        return ApiResponse.ok(this.examGoalSubService.partition(examGoalSubTotalVo));
    }

    @GetMapping("/partition/export-excel")
    @ApiOperation("分段统计(导出excel)")
    @SneakyThrows
    public void partitionExportExcel(
            ExamGoalSubTotalVo examGoalSubTotalVo,
            HttpServletResponse response, HttpServletRequest request
    ) {
        XSSFWorkbook book = this.examGoalSubService.partitionExportExcel(examGoalSubTotalVo);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = "成绩分段表(" + sdf.format(new Date()) + ").xlsx";
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

    @PostMapping("/save-all")
    @ApiOperation("保存(不用)")
    public ApiResponse<List<ExamGoalSub>> saveAll(@RequestBody List<ExamGoalSubVo> examGoalSubVos,
                                                  @RequestParam(value = "examId") Long examId,
                                                  @RequestParam(value = "subjectId") Long subjectId,
                                                  @RequestParam(value = "clazzNature") String clazzNature) {
        return ApiResponse.ok(this.examGoalSubService.saveAll(examGoalSubVos, examId, subjectId, clazzNature));
    }

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<ExamGoalSub> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.examGoalSubService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<ExamGoalSub> add(@RequestBody ExamGoalSub entity) {
        entity.setDefault().validate(true);
        this.examGoalSubService.save(entity);
        return ApiResponse.ok(this.examGoalSubService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<ExamGoalSub> update(@RequestBody ExamGoalSub entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.examGoalSubService.updateAllFieldsById(entity);
        } else {
            this.examGoalSubService.updateById(entity);
        }
        return ApiResponse.ok(this.examGoalSubService.getById(entity.getId()));
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
        return ApiResponse.ok(this.examGoalSubService.removeById(id));
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
    public ApiResponse<IPage<ExamGoalSub>> selectByPage(
        ExamGoalSubParam param,
        @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<ExamGoalSub> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<ExamGoalSub> authorityPage = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.examGoalSubService.page(authorityPage, wrapper));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Long> count(ExamGoalSubParam param) {
        QueryWrapper<ExamGoalSub> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.examGoalSubService.count(wrapper));
    }

}
