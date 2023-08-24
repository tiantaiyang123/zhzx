/**
 * 项目：中华中学管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.StaffMessageRefuse;
import com.zhzx.server.rest.req.StaffMessageRefuseParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.StaffMessageRefuseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@Api(tags = "StaffMessageRefuseController", description = "老师信息接收管理表管理")
@RequestMapping("/v1/system/staff-message-refuse")
public class StaffMessageRefuseController {
    @Resource
    private StaffMessageRefuseService staffMessageRefuseService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<StaffMessageRefuse> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.staffMessageRefuseService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<StaffMessageRefuse> add(@RequestBody StaffMessageRefuse entity) {
        StaffMessageRefuse staffMessageRefuse = this.staffMessageRefuseService.getOne(Wrappers.<StaffMessageRefuse>lambdaQuery()
                .eq(StaffMessageRefuse::getName,entity.getName())
                .eq(StaffMessageRefuse::getStaffId,entity.getStaffId())
        );
        if(staffMessageRefuse != null){
            return ApiResponse.fail(-5,"请勿重复进行");
        }
        entity.setDefault().validate(true);
        this.staffMessageRefuseService.save(entity);
        return ApiResponse.ok(this.staffMessageRefuseService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<StaffMessageRefuse> update(@RequestBody StaffMessageRefuse entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.staffMessageRefuseService.updateAllFieldsById(entity);
        } else {
            this.staffMessageRefuseService.updateById(entity);
        }
        return ApiResponse.ok(this.staffMessageRefuseService.getById(entity.getId()));
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
        return ApiResponse.ok(this.staffMessageRefuseService.removeById(id));
    }

    /**
     * 批量新增
     *
     * @param entityList 要新增的对象
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-save")
    @ApiOperation("批量新增")
    public ApiResponse<Boolean> batchSave(@RequestBody List<StaffMessageRefuse> entityList) {
        entityList.forEach(entity -> {
            entity.setDefault().validate(true);
        });
        return ApiResponse.ok(this.staffMessageRefuseService.saveBatch(entityList));
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
    public ApiResponse<Boolean> batchUpdate(StaffMessageRefuseParam param, @RequestBody StaffMessageRefuse entity) {
        return ApiResponse.ok(this.staffMessageRefuseService.update(entity, param.toQueryWrapper()));
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
        return ApiResponse.ok(this.staffMessageRefuseService.removeByIds(idList));
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
    public ApiResponse<IPage<StaffMessageRefuse>> selectByPage(
        StaffMessageRefuseParam param,
        @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<StaffMessageRefuse> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<StaffMessageRefuse> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.staffMessageRefuseService.page(page, wrapper));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Integer> count(StaffMessageRefuseParam param) {
        QueryWrapper<StaffMessageRefuse> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.staffMessageRefuseService.count(wrapper));
    }

    @PostMapping("/receive/message")
    @ApiOperation("接收此类消息")
    public ApiResponse<Boolean> delete(@RequestBody StaffMessageRefuse entity) {

        Boolean success = this.staffMessageRefuseService.remove(Wrappers.<StaffMessageRefuse>lambdaQuery()
                .eq(StaffMessageRefuse::getStaffId,entity.getStaffId())
                .eq(StaffMessageRefuse::getName,entity.getName())
        );
        if(success){
            return ApiResponse.ok(success);
        }
        return ApiResponse.fail(-5,"删除失败！");
    }

}
