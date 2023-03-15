/**
 * 项目：中华中学管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.ExamGoalTemplate;
import com.zhzx.server.domain.ExamGoalTemplateSub;
import com.zhzx.server.rest.req.ExamGoalTemplateSubParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.ExamGoalTemplateSubService;
import com.zhzx.server.vo.ExamGoalTemplateVo;
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
@Api(tags = "ExamGoalTemplateSubController", description = "目标模板详情表管理")
@RequestMapping("/v1/data/exam-goal-template-sub")
public class ExamGoalTemplateSubController {
    @Resource
    private ExamGoalTemplateSubService examGoalTemplateSubService;

    @GetMapping("/search-detail")
    @ApiOperation("通过主键查询")
    public ApiResponse<Map<String, Object>> searchDetail(@RequestParam(value = "academicYearSemesterId", required = false) Long academicYearSemesterId,
                                                         @RequestParam(value = "gradeId", required = false) Long gradeId,
                                                         @RequestParam(value = "examGoalTemplateId", required = false) Long examGoalTemplateId) {
        return ApiResponse.ok(this.examGoalTemplateSubService.searchDetail(academicYearSemesterId, gradeId, examGoalTemplateId));
    }

    @GetMapping("/it")
    @ApiOperation("按模板插入考试目标")
    public ApiResponse<Map<String, Object>> it(@RequestParam(value = "examId", defaultValue = "false") Long examId,
                                                         @RequestParam(value = "examGoalTemplateId", defaultValue = "false") Long examGoalTemplateId) {
        this.examGoalTemplateSubService.insertExamGoals(examGoalTemplateId, examId);
        return ApiResponse.ok(null);
    }

    @DeleteMapping("/delete-all/{examGoalTemplateId}")
    @ApiOperation("删除")
    public ApiResponse<Object> deleteAll(@PathVariable("examGoalTemplateId") Long examGoalTemplateId) {
        this.examGoalTemplateSubService.deleteAll(examGoalTemplateId);
        return ApiResponse.ok(null);
    }

    @PostMapping("/create-or-update")
    @ApiOperation("新增或修改")
    public ApiResponse<List<ExamGoalTemplateSub>> createOrUpdate(@RequestBody ExamGoalTemplateVo examGoalTemplateVo) {
        return ApiResponse.ok(this.examGoalTemplateSubService.createOrUpdate(examGoalTemplateVo));
    }

    @GetMapping("/export-excel")
    @ApiOperation("下载")
    @SneakyThrows
    public void tableInfoClazzExportExcel(@RequestParam(value = "id") Long id,
                                          @RequestParam(value = "examGoalTemplateName") String examGoalTemplateName,
                                          HttpServletResponse response, HttpServletRequest request) {
        XSSFWorkbook book = this.examGoalTemplateSubService.exportExcel(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = examGoalTemplateName + sdf.format(new Date()) + ".xlsx";
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
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<ExamGoalTemplateSub> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.examGoalTemplateSubService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<ExamGoalTemplateSub> add(@RequestBody ExamGoalTemplateSub entity) {
        entity.setDefault().validate(true);
        this.examGoalTemplateSubService.save(entity);
        return ApiResponse.ok(this.examGoalTemplateSubService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<ExamGoalTemplateSub> update(@RequestBody ExamGoalTemplateSub entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.examGoalTemplateSubService.updateAllFieldsById(entity);
        } else {
            this.examGoalTemplateSubService.updateById(entity);
        }
        return ApiResponse.ok(this.examGoalTemplateSubService.getById(entity.getId()));
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
        return ApiResponse.ok(this.examGoalTemplateSubService.removeById(id));
    }

    /**
     * 批量新增
     *
     * @param entityList 要新增的对象
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-save")
    @ApiOperation("批量新增")
    public ApiResponse<Boolean> batchSave(@RequestBody List<ExamGoalTemplateSub> entityList) {
        entityList.forEach(entity -> {
            entity.setDefault().validate(true);
        });
        return ApiResponse.ok(this.examGoalTemplateSubService.saveBatch(entityList));
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
    public ApiResponse<Boolean> batchUpdate(ExamGoalTemplateSubParam param, @RequestBody ExamGoalTemplateSub entity) {
        return ApiResponse.ok(this.examGoalTemplateSubService.update(entity, param.toQueryWrapper()));
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
        return ApiResponse.ok(this.examGoalTemplateSubService.removeByIds(idList));
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
    public ApiResponse<IPage<ExamGoalTemplateSub>> selectByPage(
        ExamGoalTemplateSubParam param,
        @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<ExamGoalTemplateSub> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<ExamGoalTemplateSub> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.examGoalTemplateSubService.page(page, wrapper));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Long> count(ExamGoalTemplateSubParam param) {
        QueryWrapper<ExamGoalTemplateSub> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.examGoalTemplateSubService.count(wrapper));
    }

}
