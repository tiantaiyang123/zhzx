/**
* 项目：中华中学管理平台
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.Practice;
import com.zhzx.server.rest.req.PracticeParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.PracticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;

@Slf4j
@RestController
@Api(tags = "PracticeController", description = "班级练习表管理")
@RequestMapping("/v1/data/practice")
public class PracticeController {
    @Resource
    private PracticeService practiceService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<Practice> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.practiceService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<Practice> add(@RequestBody Practice entity) {
        entity.setDefault().validate(true);
        this.practiceService.save(entity);
        return ApiResponse.ok(this.practiceService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<Practice> update(@RequestBody Practice entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.practiceService.updateAllFieldsById(entity);
        } else {
            this.practiceService.updateById(entity);
        }
        return ApiResponse.ok(this.practiceService.getById(entity.getId()));
    }

    /**
     * 删除
     *
     * @param id 要删除的对象id
     * @return int
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除")
    public ApiResponse<Boolean> update(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.practiceService.removeById(id));
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
    public ApiResponse<IPage<Practice>> selectByPage(
        PracticeParam param,
        @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<Practice> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<Practice> authorityPage = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.practiceService.page(authorityPage, wrapper));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Integer> count(PracticeParam param) {
        QueryWrapper<Practice> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.practiceService.count(wrapper));
    }

}
