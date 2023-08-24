/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.StaffResearchLeader;
import com.zhzx.server.domain.Subject;
import com.zhzx.server.domain.User;
import com.zhzx.server.enums.FunctionEnum;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.rest.req.SubjectParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Api(tags = "SubjectController", description = "科目表管理")
@RequestMapping("/v1/system/subject")
public class SubjectController {
    @Resource
    private SubjectService subjectService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<Subject> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.subjectService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<Subject> add(@RequestBody Subject entity) {
        entity.setDefault().validate(true);
        this.subjectService.save(entity);
        return ApiResponse.ok(this.subjectService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<Subject> update(@RequestBody Subject entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.subjectService.updateAllFieldsById(entity);
        } else {
            this.subjectService.updateById(entity);
        }
        return ApiResponse.ok(this.subjectService.getById(entity.getId()));
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
        return ApiResponse.ok(this.subjectService.removeById(id));
    }

    /**
     * 批量新增
     *
     * @param entityList 要新增的对象
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-save")
    @ApiOperation("批量新增")
    public ApiResponse<Boolean> batchSave(@RequestBody List<Subject> entityList) {
        entityList.forEach(entity -> {
            entity.setDefault().validate(true);
        });
        return ApiResponse.ok(this.subjectService.saveBatch(entityList));
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
    public ApiResponse<Boolean> batchUpdate(SubjectParam param, @RequestBody Subject entity) {
        return ApiResponse.ok(this.subjectService.update(entity, param.toQueryWrapper()));
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
        return ApiResponse.ok(this.subjectService.removeByIds(idList));
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
    public ApiResponse<IPage<Subject>> selectByPage(
        SubjectParam param,
        @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<Subject> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<Subject> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.subjectService.page(page, wrapper));
    }

    @GetMapping("/search-authority")
    @ApiOperation("查询科目(带权限)")
    public ApiResponse<List<Subject>> selectByAuthority(
            SubjectParam param,
            @RequestParam(value = "orderByClause", defaultValue = "id") String orderByClause,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (null == user.getStaff()) {
            return ApiResponse.ok(null);
        }

        QueryWrapper<Subject> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        List<Subject> subjectList = this.subjectService.page(new Page<>(pageNum, pageSize), wrapper).getRecords();
        Map<Long, Subject> subjectMap = subjectList.stream().collect(Collectors.toMap(Subject::getId, Function.identity()));

        FunctionEnum functionEnum = user.getStaff().getFunction();
        List<StaffResearchLeader> staffResearchLeaderList = user.getStaff().getStaffResearchLeaderList().stream().filter(i-> YesNoEnum.YES.equals(i.getIsCurrent())).collect(Collectors.toList());
        if (!FunctionEnum.PRINCIPAL.equals(functionEnum) && !FunctionEnum.DEAN.equals(functionEnum) && CollectionUtils.isNotEmpty(staffResearchLeaderList)) {
            List<Subject> mapList = staffResearchLeaderList.stream().map(t -> {
                Subject subject = new Subject();
                subject.setId(t.getSubjectId());
                subject.setName(subjectMap.get(t.getId()).getName());
                return subject;
            }).collect(Collectors.toList());
            return ApiResponse.ok(mapList);
        } else {
            return ApiResponse.ok(subjectList);
        }
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Integer> count(SubjectParam param) {
        QueryWrapper<Subject> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.subjectService.count(wrapper));
    }

    @GetMapping("/clazz-common")
    @ApiOperation("获取班级科目")
    public ApiResponse<List<Subject>> clazzCommon(@RequestParam(value = "clazzIds", required = false) List<Long> clazzIds,
                                                  @RequestParam(value = "type", defaultValue = "01") String type) {
        return ApiResponse.ok(this.subjectService.clazzCommon(clazzIds, type));
    }

}
