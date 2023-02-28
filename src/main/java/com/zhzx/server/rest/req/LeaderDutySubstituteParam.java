/**
 * 项目：中华中学管理平台
 * 模型分组：一日常规管理
 * 模型名称：领导值班代班表
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

import com.zhzx.server.enums.LeaderDutyTypeEnum;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.domain.LeaderDutySubstitute;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "LeaderDutySubstituteParam", description = "领导值班代班表参数")
public class LeaderDutySubstituteParam implements Serializable {
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
     * 领导值班类型
     */
    @ApiModelProperty(value = "领导值班类型")
    private LeaderDutyTypeEnum dutyType;
    /**
     * 领导值班类型 IN值List
     */
    @ApiModelProperty(value = "领导值班类型 IN值List")
    private List<String> dutyTypeList;
    /**
     * 领导值班ID day_leader_duty.id
     */
    @ApiModelProperty(value = "领导值班ID day_leader_duty.id")
    private Long leaderDutyId;
    /**
     * 原值班领导ID sys_staff.id
     */
    @ApiModelProperty(value = "原值班领导ID sys_staff.id")
    private Long leaderOldId;
    /**
     * 代班领导ID sys_staff.id
     */
    @ApiModelProperty(value = "代班领导ID sys_staff.id")
    private Long leaderId;
    /**
     * 是否同意 初始值为 null
     */
    @ApiModelProperty(value = "是否同意 初始值为 null")
    private YesNoEnum isAgree;
    /**
     * 是否同意 初始值为 null IN值List
     */
    @ApiModelProperty(value = "是否同意 初始值为 null IN值List")
    private List<String> isAgreeList;


    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<LeaderDutySubstitute> toQueryWrapper() {
        QueryWrapper<LeaderDutySubstitute> wrapper = Wrappers.<LeaderDutySubstitute>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getDutyType() != null, "duty_type", this.getDutyType());
        wrapper.in(this.getDutyTypeList() != null && this.getDutyTypeList().size() > 0, "duty_type", this.getDutyTypeList());
        wrapper.eq(this.getLeaderDutyId() != null, "leader_duty_id", this.getLeaderDutyId());
        wrapper.eq(this.getLeaderOldId() != null, "leader_old_id", this.getLeaderOldId());
        wrapper.eq(this.getLeaderId() != null, "leader_id", this.getLeaderId());
        wrapper.eq(this.getIsAgree() != null, "is_agree", this.getIsAgree());
        wrapper.in(this.getIsAgreeList() != null && this.getIsAgreeList().size() > 0, "is_agree", this.getIsAgreeList());
        return wrapper;
    }

}
