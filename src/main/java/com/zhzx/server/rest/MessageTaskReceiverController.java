/**
* 项目：中华中学管理平台
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.MessageTaskReceiver;
import com.zhzx.server.rest.req.MessageTaskReceiverParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.MessageTaskReceiverService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;

@Slf4j
@RestController
@Api(tags = "MessageTaskReceiverController", description = "消息发送人员表管理")
@RequestMapping("/v1/message/message-task-receiver")
public class MessageTaskReceiverController {
    @Resource
    private MessageTaskReceiverService messageTaskReceiverService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<MessageTaskReceiver> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.messageTaskReceiverService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<MessageTaskReceiver> add(@RequestBody MessageTaskReceiver entity) {
        entity.setDefault().validate(true);
        this.messageTaskReceiverService.save(entity);
        return ApiResponse.ok(this.messageTaskReceiverService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<MessageTaskReceiver> update(@RequestBody MessageTaskReceiver entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.messageTaskReceiverService.updateAllFieldsById(entity);
        } else {
            this.messageTaskReceiverService.updateById(entity);
        }
        return ApiResponse.ok(this.messageTaskReceiverService.getById(entity.getId()));
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
        return ApiResponse.ok(this.messageTaskReceiverService.removeById(id));
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
    public ApiResponse<IPage<MessageTaskReceiver>> selectByPage(
        MessageTaskReceiverParam param,
        @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<MessageTaskReceiver> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<MessageTaskReceiver> authorityPage = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.messageTaskReceiverService.page(authorityPage, wrapper));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Integer> count(MessageTaskReceiverParam param) {
        QueryWrapper<MessageTaskReceiver> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.messageTaskReceiverService.count(wrapper));
    }

}
