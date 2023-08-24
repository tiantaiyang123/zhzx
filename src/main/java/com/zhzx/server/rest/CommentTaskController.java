/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.CommentTask;
import com.zhzx.server.domain.FunctionDepartment;
import com.zhzx.server.domain.User;
import com.zhzx.server.dto.CommentTaskDto;
import com.zhzx.server.enums.CommentProcessSourceEnum;
import com.zhzx.server.enums.CommentStateEnum;
import com.zhzx.server.rest.req.CommentTaskParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.CommentTaskService;
import com.zhzx.server.service.FunctionDepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Api(tags = "CommentTaskController", description = "意见与建议处理表管理")
@RequestMapping("/v1/day/comment-task")
public class CommentTaskController {
    @Resource
    private CommentTaskService commentTaskService;
    @Resource
    private FunctionDepartmentService functionDepartmentService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<CommentTask> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.commentTaskService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<CommentTask> add(@RequestBody CommentTask entity) {
        entity.setDefault().validate(true);
        this.commentTaskService.save(entity);
        return ApiResponse.ok(this.commentTaskService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<CommentTask> update(@RequestBody CommentTask entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.commentTaskService.updateAllFieldsById(entity);
        } else {
            this.commentTaskService.updateById(entity);
        }
        return ApiResponse.ok(this.commentTaskService.getById(entity.getId()));
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
        return ApiResponse.ok(this.commentTaskService.removeById(id));
    }

    /**
     * 批量新增
     *
     * @param entityList 要新增的对象
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-save")
    @ApiOperation("批量新增")
    public ApiResponse<Boolean> batchSave(@RequestBody List<CommentTask> entityList) {
        entityList.forEach(entity -> {
            entity.setDefault().validate(true);
        });
        return ApiResponse.ok(this.commentTaskService.saveBatch(entityList));
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
    public ApiResponse<Boolean> batchUpdate(CommentTaskParam param, @RequestBody CommentTask entity) {
        return ApiResponse.ok(this.commentTaskService.update(entity, param.toQueryWrapper()));
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
        return ApiResponse.ok(this.commentTaskService.removeByIds(idList));
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
    public ApiResponse<IPage<CommentTask>> selectByPage(
        CommentTaskParam param,
        @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<CommentTask> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<CommentTask> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.commentTaskService.page(page, wrapper));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Integer> count(CommentTaskParam param) {
        QueryWrapper<CommentTask> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.commentTaskService.count(wrapper));
    }

    /**
     * 分页查询
     *
     * @param param 查询参数
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return int
     */
    @GetMapping("/back/search/grade")
    @ApiOperation("部门/年级组分页查询")
    public ApiResponse<IPage<CommentTaskDto>> backSelectByPage(
            CommentTaskParam param,
            @RequestParam(value = "type",required = false) CommentProcessSourceEnum type,
            @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        List<FunctionDepartment> functionDepartment = functionDepartmentService.list(Wrappers.<FunctionDepartment>lambdaQuery()
                .eq(FunctionDepartment::getPrincipalId,user.getId())
        );
        if(CollectionUtils.isEmpty(functionDepartment)) return ApiResponse.ok(new Page<>(pageNum, pageSize));

        IPage<CommentTask> page = new Page<>(pageNum, pageSize);
        QueryWrapper<CommentTask> wrapper = new QueryWrapper<>();
        wrapper.ge(param.getCreateTimeFrom() != null, "t.create_time", param.getCreateTimeFrom());
        wrapper.lt(param.getCreateTimeTo() != null, "t.create_time", param.getCreateTimeTo());
        String ids = functionDepartment.stream().map(department->String.valueOf(department.getId())).collect(Collectors.joining(","));
        if(CommentProcessSourceEnum.GRADE.equals(type)){
            wrapper.inSql("function_department_id","select sfd.id from sys_function_department sfd where sfd.parent_id = 2 and sfd.id in ("+ ids +")");
        }else if(CommentProcessSourceEnum.OFFICE.equals(type)){
            wrapper.inSql("function_department_id","select sfd.id from sys_function_department sfd where sfd.parent_id = 1 and sfd.id in ("+ ids +")");
        }
        wrapper.in("dcp.state", new ArrayList<CommentStateEnum>(){{this.add(CommentStateEnum.PENDING);this.add(CommentStateEnum.PROCESSED);}});
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        return ApiResponse.ok(this.commentTaskService.pageDetail(page, wrapper));
    }


    /**
     * 校长室查询详情
     */
    @GetMapping("/detail")
    @ApiOperation("部门/年级组查询详情")
    public ApiResponse<CommentTaskDto> detail(@RequestParam Long commentTaskId) {
        return ApiResponse.ok(this.commentTaskService.detail(commentTaskId));
    }

    /**
     * 校长室查询详情
     */
    @PostMapping("/verify")
    @ApiOperation("部门/年级组批示")
    public ApiResponse<Integer> verify(@RequestBody CommentTaskDto commentTaskDto) {
        return ApiResponse.ok(commentTaskService.verify(commentTaskDto));
    }
}
