/**
 * 项目：中华中学管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.MedicalExpenseReimbursementEntries;
import com.zhzx.server.domain.User;
import com.zhzx.server.rest.req.MedicalExpenseReimbursementEntriesParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.MedicalExpenseReimbursementEntriesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@Api(tags = "MedicalExpenseReimbursementEntriesController", description = "医药费报销条目表管理")
@RequestMapping("/v1/medical/medical-expense-reimbursement-entries")
public class MedicalExpenseReimbursementEntriesController {
    @Resource
    private MedicalExpenseReimbursementEntriesService medicalExpenseReimbursementEntriesService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<MedicalExpenseReimbursementEntries> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.medicalExpenseReimbursementEntriesService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<MedicalExpenseReimbursementEntries> add(@RequestBody MedicalExpenseReimbursementEntries entity) {
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        entity.setEditorId(user.getId());
        entity.setEditorName(user.getUsername());
        entity.setDefault().validate(true);
        this.medicalExpenseReimbursementEntriesService.save(entity);
        return ApiResponse.ok(this.medicalExpenseReimbursementEntriesService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<MedicalExpenseReimbursementEntries> update(@RequestBody MedicalExpenseReimbursementEntries entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.medicalExpenseReimbursementEntriesService.updateAllFieldsById(entity);
        } else {
            this.medicalExpenseReimbursementEntriesService.updateById(entity);
        }
        return ApiResponse.ok(this.medicalExpenseReimbursementEntriesService.getById(entity.getId()));
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
        return ApiResponse.ok(this.medicalExpenseReimbursementEntriesService.removeById(id));
    }

    /**
     * 批量新增
     *
     * @param entityList 要新增的对象
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-save")
    @ApiOperation("批量新增")
    public ApiResponse<Boolean> batchSave(@RequestBody List<MedicalExpenseReimbursementEntries> entityList) {
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        entityList.forEach(entity -> {
            entity.setEditorId(user.getId());
            entity.setEditorName(user.getUsername());
            entity.setDefault().validate(true);
        });
        return ApiResponse.ok(this.medicalExpenseReimbursementEntriesService.saveBatch(entityList));
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
    public ApiResponse<Boolean> batchUpdate(MedicalExpenseReimbursementEntriesParam param, @RequestBody MedicalExpenseReimbursementEntries entity) {
        return ApiResponse.ok(this.medicalExpenseReimbursementEntriesService.update(entity, param.toQueryWrapper()));
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
        return ApiResponse.ok(this.medicalExpenseReimbursementEntriesService.removeByIds(idList));
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
    public ApiResponse<IPage<MedicalExpenseReimbursementEntries>> selectByPage(
        MedicalExpenseReimbursementEntriesParam param,
        @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<MedicalExpenseReimbursementEntries> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<MedicalExpenseReimbursementEntries> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.medicalExpenseReimbursementEntriesService.page(page, wrapper));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Integer> count(MedicalExpenseReimbursementEntriesParam param) {
        QueryWrapper<MedicalExpenseReimbursementEntries> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.medicalExpenseReimbursementEntriesService.count(wrapper));
    }

    @DeleteMapping("/remove/{year}-data")
    @ApiOperation("按年清除数据")
    public ApiResponse<Integer> removeYearData(
            @PathVariable("year") String year) {
        return ApiResponse.ok(this.medicalExpenseReimbursementEntriesService.removeYearData(year));
    }

    @GetMapping("/import-excel")
    @ApiOperation("导入公开课表")
    public ApiResponse<Boolean> importExcel(
            @RequestParam(value = "year") String year,
            @RequestParam(value = "fileUrl") String fileUrl) {
        this.medicalExpenseReimbursementEntriesService.importExcel(year, fileUrl);
        return ApiResponse.ok(null);
    }

}
