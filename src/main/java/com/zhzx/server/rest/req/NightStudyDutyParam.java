/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：晚自习行政值班表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest.req;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.domain.NightStudyDuty;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "晚自习行政值班表参数", description = "")
public class NightStudyDutyParam implements Serializable {
    /**
     * ID系统自动生成
     */
    @ApiModelProperty(value = "ID系统自动生成")
    private Long id;
    /**
     * ID系统自动生成 IN值List
     */
    @ApiModelProperty(value = "ID系统自动生成 IN值List")
    private List<Long> idList;
    /**
     * 领导值班ID day_leader_duty.id
     */
    @ApiModelProperty(value = "领导值班ID day_leader_duty.id")
    private Long leaderDutyId;
    /**
     * 开始时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间")
    private java.util.Date startTime;
    /**
     * 开始时间 下限值(大于等于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间 下限值(大于等于)")
    private java.util.Date startTimeFrom;
    /**
     * 开始时间 上限值(小于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间 上限值(小于)")
    private java.util.Date startTimeTo;
    /**
     * 结束时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间")
    private java.util.Date endTime;
    /**
     * 结束时间 下限值(大于等于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间 下限值(大于等于)")
    private java.util.Date endTimeFrom;
    /**
     * 结束时间 上限值(小于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间 上限值(小于)")
    private java.util.Date endTimeTo;
    /**
     * 是否有偶发事件
     */
    @ApiModelProperty(value = "是否有偶发事件")
    private YesNoEnum hasContingency;
    /**
     * 是否有偶发事件 IN值List
     */
    @ApiModelProperty(value = "是否有偶发事件 IN值List")
    private List<String> hasContingencyList;
    /**
     * 
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "")
    private java.util.Date createTime;
    /**
     *  下限值(大于等于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = " 下限值(大于等于)")
    private java.util.Date createTimeFrom;
    /**
     *  上限值(小于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = " 上限值(小于)")
    private java.util.Date createTimeTo;
    /**
     * 
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "")
    private java.util.Date updateTime;
    /**
     *  下限值(大于等于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = " 下限值(大于等于)")
    private java.util.Date updateTimeFrom;
    /**
     *  上限值(小于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = " 上限值(小于)")
    private java.util.Date updateTimeTo;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<NightStudyDuty> toQueryWrapper() {
        QueryWrapper<NightStudyDuty> wrapper = Wrappers.<NightStudyDuty>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getLeaderDutyId() != null, "leader_duty_id", this.getLeaderDutyId());
        wrapper.eq(this.getStartTime() != null, "start_time", this.getStartTime());
        wrapper.ge(this.getStartTimeFrom() != null, "start_time", this.getStartTimeFrom());
        wrapper.lt(this.getStartTimeTo() != null, "start_time", this.getStartTimeTo());
        wrapper.eq(this.getEndTime() != null, "end_time", this.getEndTime());
        wrapper.ge(this.getEndTimeFrom() != null, "end_time", this.getEndTimeFrom());
        wrapper.lt(this.getEndTimeTo() != null, "end_time", this.getEndTimeTo());
        wrapper.eq(this.getHasContingency() != null, "has_contingency", this.getHasContingency());
        wrapper.in(this.getHasContingencyList() != null && this.getHasContingencyList().size() > 0, "has_contingency", this.getHasContingencyList());
        wrapper.eq(this.getCreateTime() != null, "create_time", this.getCreateTime());
        wrapper.ge(this.getCreateTimeFrom() != null, "create_time", this.getCreateTimeFrom());
        wrapper.lt(this.getCreateTimeTo() != null, "create_time", this.getCreateTimeTo());
        wrapper.eq(this.getUpdateTime() != null, "update_time", this.getUpdateTime());
        wrapper.ge(this.getUpdateTimeFrom() != null, "update_time", this.getUpdateTimeFrom());
        wrapper.lt(this.getUpdateTimeTo() != null, "update_time", this.getUpdateTimeTo());
        return wrapper;
    }

}
