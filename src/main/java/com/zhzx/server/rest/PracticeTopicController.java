/**
* 项目：中华中学管理平台
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.Clazz;
import com.zhzx.server.domain.PracticeTopic;
import com.zhzx.server.rest.req.PracticeTopicParam;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.PracticeTopicService;
import com.zhzx.server.service.UserService;
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
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Api(tags = "PracticeTopicController", description = "小题得分情况表管理")
@RequestMapping("/v1/data/practice-topic")
public class PracticeTopicController {
    @Resource
    private PracticeTopicService practiceTopicService;

    @Resource
    private UserService userService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<PracticeTopic> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.practiceTopicService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<PracticeTopic> add(@RequestBody PracticeTopic entity) {
        entity.setDefault().validate(true);
        this.practiceTopicService.save(entity);
        return ApiResponse.ok(this.practiceTopicService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<PracticeTopic> update(@RequestBody PracticeTopic entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.practiceTopicService.updateAllFieldsById(entity);
        } else {
            this.practiceTopicService.updateById(entity);
        }
        return ApiResponse.ok(this.practiceTopicService.getById(entity.getId()));
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
        return ApiResponse.ok(this.practiceTopicService.removeById(id));
    }

    /**
     * 分页查询
     *
     * @param param 查询参数
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return int
     */
    @GetMapping("/search/bench")
    @ApiOperation("分页查询(工作台柱状图)")
    public ApiResponse<IPage<PracticeTopic>> selectByPageBench(
            PracticeTopicParam param,
            @RequestParam(value = "academicYearSemesterId", required = false) Long academicYearSemesterId,
            @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        if (param.getPracticeId() == null || param.getTopicNumber() == null) {
            return ApiResponse.ok(new Page<PracticeTopic>());
        }
        if (CollectionUtils.isEmpty(param.getClazzIds())) {
            List<Clazz> clazzList = this.userService.mutateYear(academicYearSemesterId);
            if (CollectionUtils.isEmpty(clazzList)) {
                return ApiResponse.ok(new Page<PracticeTopic>());
            }
            param.setClazzIds(clazzList.stream().map(Clazz::getId).collect(Collectors.toList()));
        }
        QueryWrapper<PracticeTopic> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<PracticeTopic> authorityPage = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.practiceTopicService.page(authorityPage, wrapper));
    }

    @GetMapping("/search/bench-table")
    @ApiOperation("分页查询(工作台表格)")
    public ApiResponse<Map<String, Object>> selectByPageCustom(
            PracticeTopicParam param,
            @RequestParam(value = "academicYearSemesterId", required = false) Long academicYearSemesterId,
            @RequestParam(value = "orderByClause", defaultValue = "topic_number") String orderByClause,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        if (param.getPracticeId() == null) {
            return ApiResponse.ok(new HashMap<>());
        }
        List<Clazz> clazzList = this.userService.mutateYear(academicYearSemesterId);
        if (CollectionUtils.isEmpty(clazzList)) {
            return ApiResponse.ok(new HashMap<>());
        }
        if (CollectionUtils.isEmpty(param.getClazzIds())) {
            param.setClazzIds(clazzList.stream().map(Clazz::getId).collect(Collectors.toList()));
        }
        QueryWrapper<PracticeTopic> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<PracticeTopic> authorityPage = new Page<>(pageNum, pageSize);
        IPage<PracticeTopic> page = this.practiceTopicService.page(authorityPage, wrapper);
        return ApiResponse.ok(this.practiceTopicService.benchTable(page, clazzList));
    }

    @GetMapping("/bench-table/export-excel")
    @ApiOperation("小题(导出excel)")
    @SneakyThrows
    public void benchTableExportExcel(PracticeTopicParam param,
                                      @RequestParam(value = "orderByClause", defaultValue = "topic_number") String orderByClause,
                                HttpServletResponse response, HttpServletRequest request) {
        if (param.getPracticeId() == null) {
            throw new ApiCode.ApiException(-1, "");
        }
        if (CollectionUtils.isEmpty(param.getClazzIds())) {
            throw new ApiCode.ApiException(-1, "");
        }
        QueryWrapper<PracticeTopic> wrapper = param.toQueryWrapper();
        wrapper.orderByAsc(orderByClause);
        IPage<PracticeTopic> authorityPage = new Page<>(1, 9999);
        IPage<PracticeTopic> page = this.practiceTopicService.page(authorityPage, wrapper);
        XSSFWorkbook book = this.practiceTopicService.benchTableExportExcel(page);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = "小题(成绩分析)" + sdf.format(new Date()) + ".xlsx";
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
     * 分页查询
     *
     * @param param 查询参数
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return int
     */
    @GetMapping("/search")
    @ApiOperation("分页查询")
    public ApiResponse<IPage<PracticeTopic>> selectByPage(
        PracticeTopicParam param,
        @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<PracticeTopic> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<PracticeTopic> authorityPage = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.practiceTopicService.page(authorityPage, wrapper));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Long> count(PracticeTopicParam param) {
        QueryWrapper<PracticeTopic> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.practiceTopicService.count(wrapper));
    }

    @GetMapping("/import-excel")
    @ApiOperation("导入小题表")
    public ApiResponse<Boolean> importExcel(
            @RequestParam(value = "schoolyardId") Long schoolyardId,
            @RequestParam(value = "academicYearSemesterId") Long academicYearSemesterId,
            @RequestParam(value = "gradeId") Long gradeId,
            @RequestParam(value = "fileUrl") String fileUrl) {
        this.practiceTopicService.importExcel(schoolyardId, academicYearSemesterId, gradeId, fileUrl);
        return ApiResponse.ok(null);
    }

}
