/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.TeachingResultAttachment;
import com.zhzx.server.rest.req.TeachingResultAttachmentParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.TeachingResultAttachmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@Api(tags = "TeachingResultAttachmentController", description = "教学成果附件表管理")
@RequestMapping("/v1/teaching/teaching-result-attachment")
public class TeachingResultAttachmentController {
    @Resource
    private TeachingResultAttachmentService teachingResultAttachmentService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<TeachingResultAttachment> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.teachingResultAttachmentService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<TeachingResultAttachment> add(@RequestBody TeachingResultAttachment entity) {
        entity.setDefault().validate(true);
        this.teachingResultAttachmentService.save(entity);
        return ApiResponse.ok(this.teachingResultAttachmentService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<TeachingResultAttachment> update(@RequestBody TeachingResultAttachment entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.teachingResultAttachmentService.updateAllFieldsById(entity);
        } else {
            this.teachingResultAttachmentService.updateById(entity);
        }
        return ApiResponse.ok(this.teachingResultAttachmentService.getById(entity.getId()));
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
        return ApiResponse.ok(this.teachingResultAttachmentService.removeById(id));
    }

    /**
     * 批量新增
     *
     * @param entityList 要新增的对象
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-save")
    @ApiOperation("批量新增")
    public ApiResponse<Boolean> batchSave(@RequestBody List<TeachingResultAttachment> entityList) {
        entityList.forEach(entity -> {
            entity.setDefault().validate(true);
        });
        return ApiResponse.ok(this.teachingResultAttachmentService.saveBatch(entityList));
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
    public ApiResponse<Boolean> batchUpdate(TeachingResultAttachmentParam param, @RequestBody TeachingResultAttachment entity) {
        return ApiResponse.ok(this.teachingResultAttachmentService.update(entity, param.toQueryWrapper()));
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
        return ApiResponse.ok(this.teachingResultAttachmentService.removeByIds(idList));
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
    public ApiResponse<IPage<TeachingResultAttachment>> selectByPage(
        TeachingResultAttachmentParam param,
        @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<TeachingResultAttachment> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<TeachingResultAttachment> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.teachingResultAttachmentService.page(page, wrapper));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Integer> count(TeachingResultAttachmentParam param) {
        QueryWrapper<TeachingResultAttachment> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.teachingResultAttachmentService.count(wrapper));
    }

    @PostMapping("/transfer")
    @ApiOperation("文件转移")
    public ApiResponse<Object> transfer() {
        this.teachingResultAttachmentService.transfer();
        return ApiResponse.ok(null);
    }

}
