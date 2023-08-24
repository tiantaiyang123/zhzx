/**
 * 项目：中华中学管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.NightStudyAttendance;
import com.zhzx.server.domain.NightStudyAttendanceSub;
import com.zhzx.server.dto.NightDutyClassDto;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.rest.req.NightStudyAttendanceParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.NightStudyAttendanceService;
import com.zhzx.server.service.NightStudyAttendanceSubService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@Api(tags = "NightStudyAttendanceController", description = "晚自习考勤表管理")
@RequestMapping("/v1/student/night-study-attendance")
public class NightStudyAttendanceController {
    @Resource
    private NightStudyAttendanceService nightStudyAttendanceService;
    @Resource
    private NightStudyAttendanceSubService nightStudyAttendanceSubService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<NightStudyAttendance> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.nightStudyAttendanceService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<NightStudyAttendance> add(@RequestBody NightStudyAttendance entity) {
        entity.setDefault().validate(true);
        this.nightStudyAttendanceService.save(entity);
        return ApiResponse.ok(this.nightStudyAttendanceService.getById(entity.getId()));
    }

    @PostMapping("/save-multi-student")
    @ApiOperation("新增(多学生)")
    public ApiResponse<Object> saveMultiStudent(@RequestBody NightStudyAttendance entity,
                                                @RequestParam(value = "studentIds") String studentIds) {
        entity.setStudentId(0L);
        entity.setDefault().validate(true);
        return ApiResponse.ok(this.nightStudyAttendanceService.saveMultiStudent(entity, studentIds));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<NightStudyAttendance> update(@RequestBody NightStudyAttendance entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.nightStudyAttendanceService.updateAllFieldsById(entity);
        } else {
            this.nightStudyAttendanceService.updateById(entity);
        }
        return ApiResponse.ok(this.nightStudyAttendanceService.getById(entity.getId()));
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
        return ApiResponse.ok(this.nightStudyAttendanceService.removeById(id));
    }

    /**
     * 批量新增
     *
     * @param entityList 要新增的对象
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-save")
    @ApiOperation("批量新增")
    public ApiResponse<Boolean> batchSave(@RequestBody List<NightStudyAttendance> entityList) {
        entityList.forEach(entity -> {
            entity.setDefault().validate(true);
        });
        return ApiResponse.ok(this.nightStudyAttendanceService.saveBatch(entityList));
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
    public ApiResponse<Boolean> batchUpdate(NightStudyAttendanceParam param, @RequestBody NightStudyAttendance entity) {
        return ApiResponse.ok(this.nightStudyAttendanceService.update(entity, param.toQueryWrapper()));
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
        return ApiResponse.ok(this.nightStudyAttendanceService.removeByIds(idList));
    }

    /**
     * 分页查询
     *
     * @param param    查询参数
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @return int
     */
    @GetMapping("/search")
    @ApiOperation("分页查询")
    public ApiResponse<IPage<NightStudyAttendance>> selectByPage(
            NightStudyAttendanceParam param,
            @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<NightStudyAttendance> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<NightStudyAttendance> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.nightStudyAttendanceService.page(page, wrapper));
    }

    @GetMapping("/search/info")
    @ApiOperation("分页查询(含字表)")
    public ApiResponse<Page<NightStudyAttendance>> selectByPageInfo(
            NightStudyAttendanceParam param,
            @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<NightStudyAttendance> wrapper = param.toQueryWrapper();
        Integer count1 = this.nightStudyAttendanceService.count(wrapper);
        QueryWrapper<NightStudyAttendanceSub> wrapper1 = param.toQueryWrapper1();
        wrapper1.eq("is_full_attendence", YesNoEnum.YES);
        Integer count2 = this.nightStudyAttendanceSubService.count(wrapper1);
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        Page<NightStudyAttendance> page = new Page<>(pageNum, pageSize, count1 + count2, false);
        return ApiResponse.ok(this.nightStudyAttendanceService.pageDetail(page, wrapper));
    }

    /**
     * 单个查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/one")
    @ApiOperation("单个查询")
    public ApiResponse<NightStudyAttendance> selectOne(NightStudyAttendanceParam param) {
        QueryWrapper<NightStudyAttendance> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.nightStudyAttendanceService.getOne(wrapper, false));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Integer> count(NightStudyAttendanceParam param) {
        QueryWrapper<NightStudyAttendance> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.nightStudyAttendanceService.count(wrapper));
    }

    /**
     * 分页查询晚自习考勤汇总
     */
    @GetMapping("/search-night-study-attendance")
    @ApiOperation("分页查询晚自习考勤汇总")
    public ApiResponse<IPage<NightStudyAttendance>> searchByPage(
            @RequestParam(value = "schoolyardId", required = false) Long schoolyardId,
            @RequestParam(value = "academicYearSemesterId", required = false) Long academicYearSemesterId,
            @RequestParam(value = "gradeId", required = false) Long gradeId,
            @RequestParam(value = "clazzId", required = false) Long clazzId,
            @RequestParam(value = "week", required = false) Integer week,
            @RequestParam(value = "registerDateFrom", required = false) String registerDateFrom,
            @RequestParam(value = "registerDateTo", required = false) String registerDateTo,
            @RequestParam(value = "orderByClause", defaultValue = "id desc") String orderByClause,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        IPage<NightStudyAttendance> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.nightStudyAttendanceService.searchNightStudyAttendance(page, schoolyardId, academicYearSemesterId, gradeId, clazzId, week, registerDateFrom, registerDateTo, orderByClause));
    }

    @PostMapping("/full-attendance")
    @ApiOperation("全勤")
    public ApiResponse<Object> fullAttendance(@RequestBody NightDutyClassDto nightDutyClassDto,
                                              @RequestParam(value = "week") Integer week) {
        return ApiResponse.ok(this.nightStudyAttendanceService.fullAttendance(nightDutyClassDto, week));
    }
}
