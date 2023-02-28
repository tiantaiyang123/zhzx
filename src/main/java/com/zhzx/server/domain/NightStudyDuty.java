/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：晚自习行政值班表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.domain;

import java.io.Serializable;

import com.zhzx.server.rest.res.ApiCode;
import lombok.*;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zhzx.server.enums.YesNoEnum;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`day_night_study_duty`")
@ApiModel(value = "NightStudyDuty", description = "晚自习行政值班表")
public class NightStudyDuty extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * 领导值班ID day_leader_duty.id
     */
    @TableField(value = "leader_duty_id")
    @ApiModelProperty(value = "领导值班ID day_leader_duty.id", required = true)
    private Long leaderDutyId;
    /**
     * 领导值班ID day_leader_duty
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "领导值班ID day_leader_duty.id")
    private LeaderDuty leaderDuty;
    /**
     * 开始时间
     */
    @TableField(value = "start_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间", required = true)
    private java.util.Date startTime;
    /**
     * 结束时间
     */
    @TableField(value = "end_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间", required = true)
    private java.util.Date endTime;
    /**
     * 是否有偶发事件
     */
    @TableField(value = "has_contingency")
    @ApiModelProperty(value = "是否有偶发事件", required = true)
    private YesNoEnum hasContingency;
    /**
     * 
     */
    @TableField(value = "create_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "", required = true)
    private java.util.Date createTime;
    /**
     * 
     */
    @TableField(value = "update_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "", required = true)
    private java.util.Date updateTime;

    /**
     * 设置默认值
     */
    public NightStudyDuty setDefault() {
        if (this.getHasContingency() == null) {
            this.setHasContingency(YesNoEnum.NO);
        }
        if (this.getCreateTime() == null) {
            this.setCreateTime(new java.util.Date());
        }
        if (this.getUpdateTime() == null) {
            this.setUpdateTime(new java.util.Date());
        }
        return this;
    }

    /**
     * 数据验证
     */
    public Boolean validate(Boolean throwException) {
        if (this.getLeaderDutyId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "领导值班ID day_leader_duty.id不能为空！");
            return false;
        }
        if (this.getStartTime() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "开始时间不能为空！");
            return false;
        }
        if (this.getEndTime() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "结束时间不能为空！");
            return false;
        }
        if (this.getHasContingency() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "是否有偶发事件不能为空！");
            return false;
        }
        return true;
    }

    /**
     * 数据验证
     */
    public Boolean validate() {
        return this.validate(false);
    }

}
