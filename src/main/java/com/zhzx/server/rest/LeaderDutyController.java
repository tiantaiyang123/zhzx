/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.LeaderDuty;
import com.zhzx.server.domain.Staff;
import com.zhzx.server.dto.LeaderDutyDto;
import com.zhzx.server.rest.req.LeaderDutyParam;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.LeaderDutyService;
import com.zhzx.server.vo.NightStudyInfoVo;
import com.zhzx.server.vo.RoutineInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@Api(tags = "LeaderDutyController", description = "领导值班表管理")
@RequestMapping("/v1/day/leader-duty")
public class LeaderDutyController {
    @Resource
    private LeaderDutyService leaderDutyService;

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    @ApiOperation("通过主键查询")
    public ApiResponse<LeaderDuty> selectByPrimaryKey(@PathVariable("id") Long id) {
        return ApiResponse.ok(this.leaderDutyService.getById(id));
    }

    /**
     * 新增
     *
     * @param entity 要新增的对象
     * @return 新增的对象
     */
    @PostMapping("/")
    @ApiOperation("新增")
    public ApiResponse<LeaderDuty> add(@RequestBody LeaderDuty entity) {
        entity.setDefault().validate(true);
        this.leaderDutyService.save(entity);
        return ApiResponse.ok(this.leaderDutyService.getById(entity.getId()));
    }

    /**
     * 更新
     *
     * @param entity 要更新的对象
     * @return 更新后的对象
     */
    @PutMapping("/")
    @ApiOperation("更新")
    public ApiResponse<LeaderDuty> update(@RequestBody LeaderDuty entity, @RequestParam(value = "updateAllFields", defaultValue = "false") boolean updateAllFields) {
        if (updateAllFields) {
            this.leaderDutyService.updateAllFieldsById(entity);
        } else {
            this.leaderDutyService.updateById(entity);
        }
        return ApiResponse.ok(this.leaderDutyService.getById(entity.getId()));
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
        return ApiResponse.ok(this.leaderDutyService.removeById(id));
    }

    /**
     * 批量新增
     *
     * @param entityList 要新增的对象
     * @return boolean 成功或失败
     */
    @PostMapping("/batch-save")
    @ApiOperation("批量新增")
    public ApiResponse<Boolean> batchSave(@RequestBody List<LeaderDuty> entityList) {
        entityList.forEach(entity -> {
            entity.setDefault().validate(true);
        });
        return ApiResponse.ok(this.leaderDutyService.saveBatch(entityList));
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
    public ApiResponse<Boolean> batchUpdate(LeaderDutyParam param, @RequestBody LeaderDuty entity) {
        return ApiResponse.ok(this.leaderDutyService.update(entity, param.toQueryWrapper()));
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
        return ApiResponse.ok(this.leaderDutyService.removeByIds(idList));
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
    public ApiResponse<IPage<LeaderDuty>> selectByPage(
            LeaderDutyParam param,
            @RequestParam(value = "orderByClause", defaultValue = "start_time asc") String orderByClause,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        QueryWrapper<LeaderDuty> wrapper = param.toQueryWrapper();
        String[] temp = orderByClause.split("[,;]");
        Arrays.stream(temp).forEach(ob -> {
            String[] obTemp = ob.split("\\s");
            boolean isAsc = obTemp.length == 1 || obTemp[1].equalsIgnoreCase("asc");
            wrapper.orderBy(true, isAsc, obTemp[0]);
        });
        IPage<LeaderDuty> page = new Page<>(pageNum, pageSize);
        return ApiResponse.ok(this.leaderDutyService.page(page, wrapper));
    }

    /**
     * count查询
     *
     * @param param 查询参数
     * @return int
     */
    @GetMapping("/count")
    @ApiOperation("count查询")
    public ApiResponse<Integer> count(LeaderDutyParam param) {
        QueryWrapper<LeaderDuty> wrapper = param.toQueryWrapper();
        return ApiResponse.ok(this.leaderDutyService.count(wrapper));
    }

    /**
     * 查询领导值班表
     *
     * @return int
     */
    @GetMapping("/get/leaderDuty/form")
    @ApiOperation("查询领导值班表")
    public ApiResponse<Page<LeaderDutyDto>> getLeaderDutyForm(@RequestParam(value = "schoolyardId", required = false) Long schoolyardId,
                                                              @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                              @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                              @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam(value = "timeFrom", required = false) Date timeFrom,
                                                              @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam(value = "timeTo", required = false) Date timeTo,
                                                              @RequestParam(value = "leaderDutyName", required = false) String leaderDutyName,
                                                              @RequestParam(value = "phone", required = false) String phone) {

        return ApiResponse.ok(this.leaderDutyService.getLeaderDutyForm(pageNum, pageSize, timeFrom, timeTo, leaderDutyName, phone, schoolyardId));
    }

    /**
     * 领导常规值班详情
     *
     * @return int
     */
    @GetMapping("/routine-info")
    @ApiOperation("领导常规值班详情")
    public ApiResponse<RoutineInfoVo> getRoutineInfo(@RequestParam(value = "staffId", required = false) Long staffId,
                                                     @RequestParam(value = "dutyDate", required = false) String dutyDate) {
        return ApiResponse.ok(this.leaderDutyService.getRoutineInfo(staffId, dutyDate));
    }

    /**
     * 领导晚自习值班详情
     *
     * @return int
     */
    @GetMapping("/night-study-info")
    @ApiOperation("领导晚自习值班详情")
    public ApiResponse<NightStudyInfoVo> getNightStudyInfo(@RequestParam(value = "staffId", required = false) Long staffId,
                                                           @RequestParam(value = "dutyDate", required = false) String dutyDate) {
        return ApiResponse.ok(this.leaderDutyService.getNightStudyInfo(staffId, dutyDate));
    }

    /**
     * 领导晚自习值班带班
     *
     * @return int
     */
    @GetMapping("/update/leader")
    @ApiOperation("领导晚自习值班带班")
    public ApiResponse<Integer> updateLeader(@RequestParam(value = "id") Long id) {
        return ApiResponse.ok(this.leaderDutyService.updateLeader(id));
    }

    /**
     * 获取当前领导值班带班人员
     *
     * @return int
     */
    @GetMapping("/cancel/leader/choose/people")
    @ApiOperation("获取当前领导值班带班人员")
    public ApiResponse<List<Staff>> cancelLeaderChoosePeople(@RequestParam(value = "leaderDutyId") Long leaderDutyId){
        return ApiResponse.ok(this.leaderDutyService.cancelLeaderChoosePeople(leaderDutyId));
    }

    /**
     * 领导晚自习值班带班
     *
     * @return int
     */
    @PostMapping("/cancel/update/leader")
    @ApiOperation("领导取消带班")
    public ApiResponse<Integer> cancelUpdateLeader(@RequestParam(value = "leaderDutyId") Long leaderDutyId,
                                                   @RequestParam(value = "leaderId") Long leaderId){
        return ApiResponse.ok(this.leaderDutyService.cancelUpdateLeader(leaderDutyId,leaderId));
    }

    /**
     * 领导晚自习值班带班
     *
     * @return int
     */
    @GetMapping("/import/leader/duty")
    @ApiOperation("领导一日常规导入")
    public ApiResponse<String> updateLeader(
            @RequestParam(value = "schoolyardId") Long schoolyardId,
            @RequestParam(value = "fileUrl") String fileUrl) {
        return ApiResponse.ok(this.leaderDutyService.importLeaderDuty(schoolyardId, fileUrl));
    }
}
