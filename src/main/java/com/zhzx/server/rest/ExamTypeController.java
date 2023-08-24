/**
* 项目：中华中学管理平台
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.ExamType;
import com.zhzx.server.rest.req.ExamTypeParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.ExamTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;

@Slf4j
@RestController
@Api(tags = "ExamTypeController", description = "考试分类表管理")
@RequestMapping("/v1/data/exam-type")
public class ExamTypeController {
    @Resource
    private ExamTypeService examTypeService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<ExamType> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.examTypeService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<ExamType> add(@RequestBody ExamType entity) {
        entity.setDefault().validate(true);
        this.examTypeService.save(entity);
        return ApiResponse.ok(this.examTypeService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<ExamType> update(@RequestBody ExamType entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.examTypeService.updateAllFieldsById(entity);
        } else {
            this.examTypeService.updateById(entity);
        }
        return ApiResponse.ok(this.examTypeService.getById(entity.getId()));
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
        return ApiResponse.ok(this.examTypeService.removeById(id));
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
    public ApiResponse<IPage<ExamType>> selectByPage(
        ExamTypeParam param,
        @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<ExamType> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<ExamType> authorityPage = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.examTypeService.page(authorityPage, wrapper));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Integer> count(ExamTypeParam param) {
        QueryWrapper<ExamType> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.examTypeService.count(wrapper));
    }

}
