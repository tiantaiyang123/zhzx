/**
* 项目：中华中学管理平台
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.Message;
import com.zhzx.server.domain.MessageTask;
import com.zhzx.server.dto.MessageTaskDto;
import com.zhzx.server.rest.req.MessageTaskParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.MessageService;
import com.zhzx.server.service.MessageTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import sun.misc.resources.Messages;

import javax.annotation.Resource;
import java.util.Arrays;

@Slf4j
@RestController
@Api(tags = "MessageTaskController", description = "消息任务表管理")
@RequestMapping("/v1/message/message-task")
public class MessageTaskController {
    @Resource
    private MessageTaskService messageTaskService;

    @Resource
    private MessageService messageService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<MessageTask> selectByPrimaryKey(@PathVariable("id") Long id) {
        MessageTask  messageTask = this.messageTaskService.getById(id);
        MessageTaskDto messageTaskDto = new MessageTaskDto();
        BeanUtils.copyProperties(messageTask,messageTaskDto);
        messageTaskDto.setReceiverType(messageTask.getMessageTaskReceiverList().get(0).getReceiverType());
        return ApiResponse.ok(messageTaskDto);
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<MessageTask> add(@RequestBody MessageTask entity) {
        entity.setDefault().validate(true);
        this.messageTaskService.save(entity);
        return ApiResponse.ok(this.messageTaskService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<MessageTask> update(@RequestBody MessageTask entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.messageTaskService.updateAllFieldsById(entity);
        } else {
            this.messageTaskService.updateById(entity);
            //更新message信息
            this.messageService.updateIsSendOnMessage(entity);
        }
        return ApiResponse.ok(this.messageTaskService.getById(entity.getId()));
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
        return ApiResponse.ok(this.messageTaskService.delete(id));
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
    public ApiResponse<Page<MessageTaskDto>> selectByPage(
        MessageTaskParam param,
        @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<MessageTask> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        Page<MessageTaskDto> authorityPage = new Page<>(pageNum, pageSize);
        this.messageTaskService.getPage(authorityPage, wrapper);
        authorityPage.getRecords().stream().forEach(item->item.setReceiverType(item.getMessageTaskReceiverList().get(0).getReceiverType()));
        return ApiResponse.ok(authorityPage);
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Integer> count(MessageTaskParam param) {
        QueryWrapper<MessageTask> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.messageTaskService.count(wrapper));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/create/with/receiver")
    @ApiOperation("新增包含发送人")
    public ApiResponse<MessageTask> createWithReceiver(@RequestBody MessageTaskDto entity) {

        this.messageTaskService.create(entity);
        return ApiResponse.ok(entity);
    }

    /**
     * 修改包含发送人
     *
     * @param entity 修改包含发送人
     * @return 修改包含发送人
     */
    @PostMapping("/update/with/receiver")
    @ApiOperation("修改包含发送人")
    public ApiResponse<MessageTask> updateWithReceiver(@RequestBody MessageTaskDto entity) {

        this.messageTaskService.update(entity);
        return ApiResponse.ok(entity);
    }
}
