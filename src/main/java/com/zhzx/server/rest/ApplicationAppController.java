/**
 * 项目：中华中学管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest;

import java.util.List;
import java.util.Arrays;

import javax.annotation.Resource;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.dto.ApplicationAppDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.springframework.web.bind.annotation.*;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.domain.ApplicationApp;
import com.zhzx.server.rest.req.ApplicationAppParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.ApplicationAppService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import com.zhzx.server.domain.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Api(tags = "ApplicationAppController", description = "手机app应用配置表管理")
@RequestMapping("/v1/system/application-app")
public class ApplicationAppController {
    @Resource
    private ApplicationAppService applicationAppService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<ApplicationApp> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.applicationAppService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<ApplicationApp> add(@RequestBody ApplicationApp entity) {
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        entity.setEditorId(user.getId());
        entity.setEditorName(user.getUsername());
        entity.setDefault().validate(true);
        this.applicationAppService.save(entity);
        return ApiResponse.ok(this.applicationAppService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<ApplicationApp> update(@RequestBody ApplicationApp entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.applicationAppService.updateAllFieldsById(entity);
        } else {
            this.applicationAppService.updateById(entity);
        }
        return ApiResponse.ok(this.applicationAppService.getById(entity.getId()));
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
        return ApiResponse.ok(this.applicationAppService.removeById(id));
    }

    /**
     * 批量新增
     *
     * @param entityList 要新增的对象
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-save")
    @ApiOperation("批量新增")
    public ApiResponse<Boolean> batchSave(@RequestBody List<ApplicationApp> entityList) {
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        entityList.forEach(entity -> {
            entity.setEditorId(user.getId());
            entity.setEditorName(user.getUsername());
            entity.setDefault().validate(true);
        });
        return ApiResponse.ok(this.applicationAppService.saveBatch(entityList));
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
    public ApiResponse<Boolean> batchUpdate(ApplicationAppParam param, @RequestBody ApplicationApp entity) {
        return ApiResponse.ok(this.applicationAppService.update(entity, param.toQueryWrapper()));
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
        return ApiResponse.ok(this.applicationAppService.removeByIds(idList));
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
    public ApiResponse<IPage<ApplicationApp>> selectByPage(
        ApplicationAppParam param,
        @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<ApplicationApp> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<ApplicationApp> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.applicationAppService.page(page, wrapper));
    }

    @GetMapping("/search-by-role")
    @ApiOperation("按角色查询所有的app应用")
    public ApiResponse<List<ApplicationAppDto>> searchByRole() {
        return ApiResponse.ok(this.applicationAppService.searchByRole());
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Long> count(ApplicationAppParam param) {
        QueryWrapper<ApplicationApp> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.applicationAppService.count(wrapper));
    }

}
