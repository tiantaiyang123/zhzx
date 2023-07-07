/**
 * 项目：中华中学管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.WxXcxMessage;
import com.zhzx.server.dto.MessageCombineDto;
import com.zhzx.server.rest.req.WxXcxMessageParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.WxXcxMessageService;
import com.zhzx.server.vo.MessageCombineVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;

@Slf4j
@RestController
@Api(tags = "WxXcxMessageController", description = "微信小程序通知表管理")
@RequestMapping("/v1/tir/wx-xcx-message")
public class WxXcxMessageController {
    @Resource
    private WxXcxMessageService wxXcxMessageService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<WxXcxMessage> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.wxXcxMessageService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<WxXcxMessage> add(@RequestBody WxXcxMessage entity) {
        entity.setDefault().validate(true);
        this.wxXcxMessageService.save(entity);
        return ApiResponse.ok(this.wxXcxMessageService.getById(entity.getId()));
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
    public ApiResponse<IPage<WxXcxMessage>> selectByPage(
        WxXcxMessageParam param,
        @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<WxXcxMessage> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<WxXcxMessage> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.wxXcxMessageService.page(page, wrapper));
    }

    @GetMapping("/search-app")
    @ApiOperation("分页查询")
    public ApiResponse<IPage<MessageCombineDto>> selectApp(
            MessageCombineVo messageCombineVo,
            @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        return ApiResponse.ok(this.wxXcxMessageService.pageApp(orderByClause, pageNum, pageSize, messageCombineVo));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Long> count(WxXcxMessageParam param) {
        QueryWrapper<WxXcxMessage> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.wxXcxMessageService.count(wrapper));
    }

}
