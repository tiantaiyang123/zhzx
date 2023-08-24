/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest;

import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.Comment;
import com.zhzx.server.domain.CommentTask;
import com.zhzx.server.domain.Message;
import com.zhzx.server.dto.CommentProcessDto;
import com.zhzx.server.dto.CommentTaskDto;
import com.zhzx.server.enums.CommentStateEnum;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.rest.req.CommentTaskParam;
import com.zhzx.server.service.CommentService;
import com.zhzx.server.service.CommentTaskService;
import com.zhzx.server.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import net.hasor.web.annotation.Post;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.domain.CommentProcess;
import com.zhzx.server.rest.req.CommentProcessParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.CommentProcessService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Api(tags = "CommentProcessController", description = "意见与建议推送表管理")
@RequestMapping("/v1/day/comment-process")
public class CommentProcessController {
    @Resource
    private CommentProcessService commentProcessService;
    @Resource
    private CommentService commentService;
    @Resource
    private MessageService messageService;
    @Resource
    private CommentTaskService commentTaskService;
    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<CommentProcess> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.commentProcessService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<CommentProcess> add(@RequestBody CommentProcess entity) {
        entity.setDefault().validate(true);
        this.commentProcessService.save(entity);
        return ApiResponse.ok(this.commentProcessService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<CommentProcess> update(@RequestBody CommentProcess entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.commentProcessService.updateAllFieldsById(entity);
        } else {
            this.commentProcessService.updateById(entity);
        }
        return ApiResponse.ok(this.commentProcessService.getById(entity.getId()));
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
        return ApiResponse.ok(this.commentProcessService.removeById(id));
    }

    /**
     * 批量新增
     *
     * @param entityList 要新增的对象
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-save")
    @ApiOperation("批量新增")
    public ApiResponse<Boolean> batchSave(@RequestBody List<CommentProcess> entityList) {
        entityList.forEach(entity -> {
            entity.setDefault().validate(true);
        });
        return ApiResponse.ok(this.commentProcessService.saveBatch(entityList));
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
    public ApiResponse<Boolean> batchUpdate(CommentProcessParam param, @RequestBody CommentProcess entity) {
        return ApiResponse.ok(this.commentProcessService.update(entity, param.toQueryWrapper()));
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
        return ApiResponse.ok(this.commentProcessService.removeByIds(idList));
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
    public ApiResponse<IPage<CommentProcess>> selectByPage(
        CommentProcessParam param,
        @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<CommentProcess> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<CommentProcess> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.commentProcessService.page(page, wrapper));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Integer> count(CommentProcessParam param) {
        QueryWrapper<CommentProcess> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.commentProcessService.count(wrapper));
    }

    /**
     * 分页查询
     *
     * @param param 查询参数
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return int
     */
    @GetMapping("/back/search/headmaster")
    @ApiOperation("校长室分页查询")
    public ApiResponse<IPage<CommentProcessDto>> backHeadMasterByPage(
            CommentProcessParam param,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<CommentProcess> wrapper = new QueryWrapper<>();
        wrapper.ge(param.getCreateTimeFrom() != null, "t.create_time", param.getCreateTimeFrom());
        wrapper.lt(param.getCreateTimeTo() != null, "t.create_time", param.getCreateTimeTo());
        wrapper.eq("dcp.need_instruction", YesNoEnum.YES);
        IPage page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.commentProcessService.headmasterPageDetail(page, wrapper));
    }

    /**
     * 校长室查询详情
     */
    @GetMapping("/headmaster/detail")
    @ApiOperation("校长室查询详情")
    public ApiResponse<CommentProcess> headmasterDetail(@RequestParam Long commentProcessId) {
        messageService.update(Wrappers.<Message>lambdaUpdate()
                .set(Message::getIsRead,YesNoEnum.YES)
                .eq(Message::getName,"commentProcess_"+commentProcessId)
        );
        return ApiResponse.ok(this.commentProcessService.headmasterDetail(commentProcessId));
    }

    /**
     * 校长室查询详情
     */
    @PostMapping("/headmaster/verify")
    @ApiOperation("校长室批示")
    public ApiResponse<Integer> headmasterVerify(@RequestBody CommentProcess commentProcess) {
        return ApiResponse.ok(commentProcessService.headmasterVerify(commentProcess));
    }
}
