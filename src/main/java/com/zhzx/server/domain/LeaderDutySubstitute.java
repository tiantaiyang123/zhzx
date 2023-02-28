/**
 * 项目：中华中学管理平台
 * 模型分组：一日常规管理
 * 模型名称：领导值班代班表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.domain;

import com.zhzx.server.rest.res.ApiCode;
import lombok.*;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.zhzx.server.enums.LeaderDutyTypeEnum;
import com.zhzx.server.enums.YesNoEnum;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`day_leader_duty_substitute`")
@ApiModel(value = "LeaderDutySubstitute", description = "领导值班代班表")
public class LeaderDutySubstitute extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * 领导值班类型
     */
    @TableField(value = "duty_type")
    @ApiModelProperty(value = "领导值班类型", required = true)
    private LeaderDutyTypeEnum dutyType;
    /**
     * 领导值班ID day_leader_duty.id
     */
    @TableField(value = "leader_duty_id")
    @ApiModelProperty(value = "领导值班ID day_leader_duty.id", required = true)
    private Long leaderDutyId;
    /**
     * 原值班领导ID sys_staff.id
     */
    @TableField(value = "leader_old_id")
    @ApiModelProperty(value = "原值班领导ID sys_staff.id", required = true)
    private Long leaderOldId;
    /**
     * 代班领导ID sys_staff.id
     */
    @TableField(value = "leader_id")
    @ApiModelProperty(value = "代班领导ID sys_staff.id", required = true)
    private Long leaderId;
    /**
     * 是否同意 初始值为 null
     */
    @TableField(value = "is_agree")
    @ApiModelProperty(value = "是否同意 初始值为 null")
    private YesNoEnum isAgree;

    /**
     * 设置默认值
     */
    public LeaderDutySubstitute setDefault() {
        if (this.getDutyType() == null) {
            this.setDutyType(LeaderDutyTypeEnum.ROUTINE);
        }
        if (this.getLeaderOldId() == null) {
            this.setLeaderOldId(0L);
        }
        return this;
    }

    /**
     * 数据验证
     */
    public Boolean validate(Boolean throwException) {
        if (this.getDutyType() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "领导值班类型不能为空！");
            return false;
        }
        if (this.getLeaderDutyId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "领导值班ID day_leader_duty.id不能为空！");
            return false;
        }
        if (this.getLeaderOldId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "原值班领导ID sys_staff.id不能为空！");
            return false;
        }
        if (this.getLeaderId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "代班领导ID sys_staff.id不能为空！");
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
