/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.Comment;
import com.zhzx.server.dto.CommentDto;
import com.zhzx.server.rest.req.CommentParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Api(tags = "CommentController", description = "意见与建议表管理")
@RequestMapping("/v1/day/comment")
public class CommentController {
    @Resource
    private CommentService commentService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<Comment> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.commentService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<Comment> add(@RequestBody Comment entity) {
        entity.setDefault().validate(true);
        this.commentService.save(entity);
        return ApiResponse.ok(this.commentService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<Comment> update(@RequestBody Comment entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.commentService.updateAllFieldsById(entity);
        } else {
            this.commentService.updateById(entity);
        }
        return ApiResponse.ok(this.commentService.getById(entity.getId()));
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
        return ApiResponse.ok(this.commentService.removeById(id));
    }

    /**
     * 批量新增
     *
     * @param entityList 要新增的对象
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-save")
    @ApiOperation("批量新增")
    public ApiResponse<Boolean> batchSave(@RequestBody List<Comment> entityList) {
        entityList.forEach(entity -> {
            entity.setDefault().validate(true);
        });
        return ApiResponse.ok(this.commentService.saveBatch(entityList));
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
    public ApiResponse<Boolean> batchUpdate(CommentParam param, @RequestBody Comment entity) {
        return ApiResponse.ok(this.commentService.update(entity, param.toQueryWrapper()));
    }

    /**
     * 批量删除
     *
     * @param idList 要删除的对象id
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-delete")
    @ApiOperation("批量删除")
    public ApiResponse<Integer> batchDelete(@RequestBody List<Long> idList) {
        return ApiResponse.ok(this.commentService.removeByIds(idList));
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
    public ApiResponse<IPage<Comment>> selectByPage(
        CommentParam param,
        @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<Comment> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<Comment> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.commentService.page(page, wrapper));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Integer> count(CommentParam param) {
        QueryWrapper<Comment> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.commentService.count(wrapper));
    }

    /**
     * count查询
     *
     * @param entity 查询参数
     * @return int
     */
    @PostMapping("/create/comment")
    @ApiOperation("创建或修改")
    public ApiResponse<CommentDto> createComment(@RequestBody CommentDto entity) {
        return ApiResponse.ok(this.commentService.create(entity));
    }

    /**
     * count查询
     *
     * @param entity 查询参数
     * @return int
     */
    @PostMapping("/push/comment")
    @ApiOperation("推送")
    public ApiResponse<CommentDto> push(@RequestBody CommentDto entity) {
        return ApiResponse.ok(this.commentService.push(entity));
    }


    /**
     * 分页查询
     *
     * @param param 查询参数
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return int
     */
    @GetMapping("/back/search/dean")
    @ApiOperation("教务处分页查询")
    public ApiResponse<IPage> backDeanByPage(
            CommentParam param,
            @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<Comment> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.commentService.deanPageDetail(page, wrapper));
    }

    /**
     * count查询
     *
     * @return int
     */
    @GetMapping("/search/comment/by/nightStudyId")
    @ApiOperation("查询")
    public ApiResponse<Map<String, Object>> searchByNightStudyId(@RequestParam Long nightStudyId) {
        return ApiResponse.ok(this.commentService.searchByNightStudyId(nightStudyId));
    }
}
