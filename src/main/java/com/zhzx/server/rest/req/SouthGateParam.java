/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：南大门准备情况表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest.req;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhzx.server.domain.SouthGate;
import com.zhzx.server.enums.YesNoEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.List;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "南大门准备情况表参数", description = "")
public class SouthGateParam implements Serializable {
    /**
     * ID系统自动生成
     */
    @ApiModelProperty(value = "ID系统自动生成")
    private Long id;
    /**
     * 年级 sys_grade.id
     */
    @ApiModelProperty(value = "校区 sys_schoolyard.id", required = true)
    private Long schoolyardId;
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
     * 校医是否就位
     */
    @ApiModelProperty(value = "校医是否就位")
    private YesNoEnum doctorInPlace;
    /**
     * 校医是否就位 IN值List
     */
    @ApiModelProperty(value = "校医是否就位 IN值List")
    private List<String> doctorInPlaceList;
    /**
     * 情况说明
     */
    @ApiModelProperty(value = "情况说明")
    private String doctorDescription;
    /**
     * 保安是否就位
     */
    @ApiModelProperty(value = "保安是否就位")
    private YesNoEnum guardInPlace;
    /**
     * 保安是否就位 IN值List
     */
    @ApiModelProperty(value = "保安是否就位 IN值List")
    private List<String> guardInPlaceList;
    /**
     * 情况说明
     */
    @ApiModelProperty(value = "情况说明")
    private String guardDescription;
    /**
     * 远红外测温仪是否正常
     */
    @ApiModelProperty(value = "远红外测温仪是否正常")
    private YesNoEnum thermometerInPlace;
    /**
     * 远红外测温仪是否正常 IN值List
     */
    @ApiModelProperty(value = "远红外测温仪是否正常 IN值List")
    private List<String> thermometerInPlaceList;
    /**
     * 情况说明
     */
    @ApiModelProperty(value = "情况说明")
    private String thermometerDescription;
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
     * 检查时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "检查时间")
    private java.util.Date checkTime;
    /**
     * 检查时间 下限值(大于等于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "检查时间 下限值(大于等于)")
    private java.util.Date checkTimeFrom;
    /**
     * 检查时间 上限值(小于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "检查时间 上限值(小于)")
    private java.util.Date checkTimeTo;
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
    public QueryWrapper<SouthGate> toQueryWrapper() {
        QueryWrapper<SouthGate> wrapper = Wrappers.<SouthGate>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getLeaderDutyId() != null, "leader_duty_id", this.getLeaderDutyId());
        wrapper.eq(this.getDoctorInPlace() != null, "doctor_in_place", this.getDoctorInPlace());
        wrapper.in(this.getDoctorInPlaceList() != null && this.getDoctorInPlaceList().size() > 0, "doctor_in_place", this.getDoctorInPlaceList());
        if (this.getDoctorDescription() != null) {
            if (this.getDoctorDescription().startsWith("%") && this.getDoctorDescription().endsWith("%")) {
                wrapper.like("doctor_description", this.getDoctorDescription().substring(1, this.getDoctorDescription().length() - 1));
            } else if (this.getDoctorDescription().startsWith("%") && !this.getDoctorDescription().endsWith("%")) {
                wrapper.likeLeft("doctor_description", this.getDoctorDescription().substring(1));
            } else if (this.getDoctorDescription().endsWith("%")) {
                wrapper.likeRight("doctor_description", this.getDoctorDescription().substring(0, this.getDoctorDescription().length() - 1));
            } else {
                wrapper.eq("doctor_description", this.getDoctorDescription());
            }
        }
        wrapper.eq(this.getGuardInPlace() != null, "guard_in_place", this.getGuardInPlace());
        wrapper.in(this.getGuardInPlaceList() != null && this.getGuardInPlaceList().size() > 0, "guard_in_place", this.getGuardInPlaceList());
        if (this.getGuardDescription() != null) {
            if (this.getGuardDescription().startsWith("%") && this.getGuardDescription().endsWith("%")) {
                wrapper.like("guard_description", this.getGuardDescription().substring(1, this.getGuardDescription().length() - 1));
            } else if (this.getGuardDescription().startsWith("%") && !this.getGuardDescription().endsWith("%")) {
                wrapper.likeLeft("guard_description", this.getGuardDescription().substring(1));
            } else if (this.getGuardDescription().endsWith("%")) {
                wrapper.likeRight("guard_description", this.getGuardDescription().substring(0, this.getGuardDescription().length() - 1));
            } else {
                wrapper.eq("guard_description", this.getGuardDescription());
            }
        }
        wrapper.eq(this.getThermometerInPlace() != null, "thermometer_in_place", this.getThermometerInPlace());
        wrapper.in(this.getThermometerInPlaceList() != null && this.getThermometerInPlaceList().size() > 0, "thermometer_in_place", this.getThermometerInPlaceList());
        if (this.getThermometerDescription() != null) {
            if (this.getThermometerDescription().startsWith("%") && this.getThermometerDescription().endsWith("%")) {
                wrapper.like("thermometer_description", this.getThermometerDescription().substring(1, this.getThermometerDescription().length() - 1));
            } else if (this.getThermometerDescription().startsWith("%") && !this.getThermometerDescription().endsWith("%")) {
                wrapper.likeLeft("thermometer_description", this.getThermometerDescription().substring(1));
            } else if (this.getThermometerDescription().endsWith("%")) {
                wrapper.likeRight("thermometer_description", this.getThermometerDescription().substring(0, this.getThermometerDescription().length() - 1));
            } else {
                wrapper.eq("thermometer_description", this.getThermometerDescription());
            }
        }
        wrapper.eq(this.getStartTime() != null, "start_time", this.getStartTime());
        wrapper.ge(this.getStartTimeFrom() != null, "start_time", this.getStartTimeFrom());
        wrapper.lt(this.getStartTimeTo() != null, "start_time", this.getStartTimeTo());
        wrapper.eq(this.getEndTime() != null, "end_time", this.getEndTime());
        wrapper.ge(this.getEndTimeFrom() != null, "end_time", this.getEndTimeFrom());
        wrapper.lt(this.getEndTimeTo() != null, "end_time", this.getEndTimeTo());
        wrapper.eq(this.getCheckTime() != null, "check_time", this.getCheckTime());
        wrapper.ge(this.getCheckTimeFrom() != null, "check_time", this.getCheckTimeFrom());
        wrapper.lt(this.getCheckTimeTo() != null, "check_time", this.getCheckTimeTo());
        wrapper.eq(this.getHasContingency() != null, "has_contingency", this.getHasContingency());
        wrapper.in(this.getHasContingencyList() != null && this.getHasContingencyList().size() > 0, "has_contingency", this.getHasContingencyList());
        wrapper.eq(this.getCreateTime() != null, "create_time", this.getCreateTime());
        wrapper.ge(this.getCreateTimeFrom() != null, "create_time", this.getCreateTimeFrom());
        wrapper.lt(this.getCreateTimeTo() != null, "create_time", this.getCreateTimeTo());
        wrapper.eq(this.getUpdateTime() != null, "update_time", this.getUpdateTime());
        wrapper.ge(this.getUpdateTimeFrom() != null, "update_time", this.getUpdateTimeFrom());
        wrapper.lt(this.getUpdateTimeTo() != null, "update_time", this.getUpdateTimeTo());
        wrapper.eq(this.getSchoolyardId() != null, "schoolyard_id", this.getSchoolyardId());
        return wrapper;
    }

}
