/**
* 项目：中华中学管理平台
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.rest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.enums.ReceiverEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import net.hasor.web.annotation.Post;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;
import com.zhzx.server.domain.StaffMessage;
import com.zhzx.server.rest.req.StaffMessageParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.StaffMessageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Api(tags = "StaffMessageController", description = "老师微信表管理")
@RequestMapping("/v1/system/staff-message")
public class StaffMessageController {
    @Resource
    private StaffMessageService staffMessageService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<StaffMessage> selectByPrimaryKey(@PathVariable("id") Long id) {
        StaffMessage staffMessage = this.staffMessageService.getById(id);
        if(CollectionUtils.isNotEmpty(staffMessage.getStaffMessageReceiverRelationList())){
            staffMessage.setTeacherList(staffMessage.getStaffMessageReceiverRelationList().stream().filter(item-> ReceiverEnum.TEACHER.equals(item.getReceiverType())).collect(Collectors.toList()));
            staffMessage.setStudentList(staffMessage.getStaffMessageReceiverRelationList().stream().filter(item-> ReceiverEnum.STUDENT.equals(item.getReceiverType())).collect(Collectors.toList()));
        }
        return ApiResponse.ok(staffMessage);
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<StaffMessage> add(@RequestBody StaffMessage entity) {
        entity.setDefault().validate(true);
        this.staffMessageService.save(entity);
        return ApiResponse.ok(this.staffMessageService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<StaffMessage> update(@RequestBody StaffMessage entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.staffMessageService.updateAllFieldsById(entity);
        } else {
            this.staffMessageService.updateById(entity);
        }
        return ApiResponse.ok(this.staffMessageService.getById(entity.getId()));
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
        return ApiResponse.ok(this.staffMessageService.removeById(id));
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
    public ApiResponse<IPage<StaffMessage>> selectByPage(
        StaffMessageParam param,
        @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<StaffMessage> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<StaffMessage> authorityPage = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.staffMessageService.page(authorityPage, wrapper));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Long> count(StaffMessageParam param) {
        QueryWrapper<StaffMessage> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.staffMessageService.count(wrapper));
    }

    /**
     *新增
     */
    @PostMapping("/create")
    @ApiOperation("create")
    public ApiResponse<Long> create(@RequestBody StaffMessage staffMessage) {

        return ApiResponse.ok(this.staffMessageService.create(staffMessage));
    }

    /**
     *修改
     */
    @PostMapping("/update")
    @ApiOperation("update")
    public ApiResponse<Long> update(@RequestBody StaffMessage staffMessage) {

        return ApiResponse.ok(this.staffMessageService.updateWithSonList(staffMessage));
    }

}
