/**
 * 项目：中华中学管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.UserApplicationPreferApp;
import com.zhzx.server.rest.req.UserApplicationPreferAppParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.UserApplicationPreferAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@Api(tags = "UserApplicationPreferAppController", description = "手机app用户首页应用表管理")
@RequestMapping("/v1/system/user-application-prefer-app")
public class UserApplicationPreferAppController {
    @Resource
    private UserApplicationPreferAppService userApplicationPreferAppService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<UserApplicationPreferApp> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.userApplicationPreferAppService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<UserApplicationPreferApp> add(@RequestBody UserApplicationPreferApp entity) {
        entity.setDefault().validate(true);
        this.userApplicationPreferAppService.save(entity);
        return ApiResponse.ok(this.userApplicationPreferAppService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<UserApplicationPreferApp> update(@RequestBody UserApplicationPreferApp entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.userApplicationPreferAppService.updateAllFieldsById(entity);
        } else {
            this.userApplicationPreferAppService.updateById(entity);
        }
        return ApiResponse.ok(this.userApplicationPreferAppService.getById(entity.getId()));
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
        return ApiResponse.ok(this.userApplicationPreferAppService.removeById(id));
    }

    /**
     * 批量新增
     *
     * @param entityList 要新增的对象
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-save")
    @ApiOperation("批量新增")
    public ApiResponse<Boolean> batchSave(@RequestBody List<UserApplicationPreferApp> entityList) {
        entityList.forEach(entity -> {
            entity.setDefault().validate(true);
        });
        return ApiResponse.ok(this.userApplicationPreferAppService.saveBatch(entityList));
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
    public ApiResponse<Boolean> batchUpdate(UserApplicationPreferAppParam param, @RequestBody UserApplicationPreferApp entity) {
        return ApiResponse.ok(this.userApplicationPreferAppService.update(entity, param.toQueryWrapper()));
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
        return ApiResponse.ok(this.userApplicationPreferAppService.removeByIds(idList));
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
    public ApiResponse<IPage<UserApplicationPreferApp>> selectByPage(
        UserApplicationPreferAppParam param,
        @RequestParam(value = "orderByClause", defaultValue = "id") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<UserApplicationPreferApp> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<UserApplicationPreferApp> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.userApplicationPreferAppService.page(page, wrapper));
    }

    @PostMapping("/create-or-update")
    @ApiOperation("首页应用添加或保存")
    public ApiResponse<Integer> createOrUpdate(@RequestParam(value = "idList", required = false) List<Long> idList) {
        return ApiResponse.ok(this.userApplicationPreferAppService.createOrUpdate(idList));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Long> count(UserApplicationPreferAppParam param) {
        QueryWrapper<UserApplicationPreferApp> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.userApplicationPreferAppService.count(wrapper));
    }

}
