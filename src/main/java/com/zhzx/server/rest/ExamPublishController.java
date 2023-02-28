/**
 * 项目：中华中学管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.ExamPublish;
import com.zhzx.server.enums.ExamPrintEnum;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.rest.req.ExamPublishParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.ExamPublishService;
import com.zhzx.server.vo.ExamPublishVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@Api(tags = "ExamPublishController", description = "考试发布表管理")
@RequestMapping("/v1/data/exam-publish")
public class ExamPublishController {
    @Resource
    private ExamPublishService examPublishService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<ExamPublish> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.examPublishService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<ExamPublish> add(@RequestBody ExamPublish entity) {
        entity.setDefault().validate(true);
        this.examPublishService.save(entity);
        return ApiResponse.ok(this.examPublishService.getById(entity.getId()));
    }

    @PostMapping("/save")
    @ApiOperation("保存")
    public ApiResponse<ExamPublish> save(@RequestBody ExamPublishVo entity) {
        return ApiResponse.ok(this.examPublishService.createUpdate(entity));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<ExamPublish> update(@RequestBody ExamPublish entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (!entity.getIsPublish().equals(ExamPrintEnum.NO)) {
            entity.setPublishTime(new Date());
        }
        if (updateAllFields) {
            this.examPublishService.updateAllFieldsById(entity);
        } else {
            this.examPublishService.updateById(entity);
        }
        return ApiResponse.ok(this.examPublishService.getById(entity.getId()));
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
        return ApiResponse.ok(this.examPublishService.removeAll(id));
    }

    /**
     * 批量新增
     *
     * @param entityList 要新增的对象
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-save")
    @ApiOperation("批量新增")
    public ApiResponse<Boolean> batchSave(@RequestBody List<ExamPublish> entityList) {
        entityList.forEach(entity -> {
            entity.setDefault().validate(true);
        });
        return ApiResponse.ok(this.examPublishService.saveBatch(entityList));
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
    public ApiResponse<Boolean> batchUpdate(ExamPublishParam param, @RequestBody ExamPublish entity) {
        return ApiResponse.ok(this.examPublishService.update(entity, param.toQueryWrapper()));
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
        return ApiResponse.ok(this.examPublishService.removeByIds(idList));
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
    public ApiResponse<IPage<ExamPublish>> selectByPage(
            ExamPublishParam param,
            @RequestParam(value = "academicYearSemesterId", required = false) Long academicYearSemesterId,
            @RequestParam(value = "containsCurrent", defaultValue = "NO") YesNoEnum containsCurrent,
            @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        Long gradeId = param.getGradeId();
        param.setGradeId(null);

        IPage<ExamPublish> page = new Page<>(pageNum, pageSize);
        if (gradeId == null) {
            QueryWrapper<ExamPublish> wrapper = param.toQueryWrapper();
            String[] temp = orderByClause.split("[,;]]");
            Arrays.stream(temp).forEach(ob -> {
                String[] obTemp = ob.split("\\s");
                boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
                wrapper.orderBy(true, isAsc, obTemp[0]);
            });
            return ApiResponse.ok(this.examPublishService.page(page, wrapper));
        }
        return ApiResponse.ok(this.examPublishService.listByGrade(page, param, containsCurrent, gradeId, academicYearSemesterId, orderByClause));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Long> count(ExamPublishParam param) {
        QueryWrapper<ExamPublish> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.examPublishService.count(wrapper));
    }

}
