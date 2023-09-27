/**
 * 项目：中华中学管理平台
 * 模型分组：一日常规管理
 * 模型名称：假期校园概况表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zhzx.server.rest.res.ApiCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`day_campus_overview_holiday`")
@ApiModel(value = "CampusOverviewHoliday", description = "假期校园概况表")
public class CampusOverviewHoliday extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * 校区ID sys_schoolyard.id
     */
    @TableField(value = "schoolyard_id")
    @ApiModelProperty(value = "校区ID sys_schoolyard.id", required = true)
    private Long schoolyardId;
    /**
     * 领导值班ID day_leader_duty.id
     */
    @TableField(value = "leader_duty_id")
    @ApiModelProperty(value = "领导值班ID day_leader_duty.id", required = true)
    private Long leaderDutyId;
    /**
     * 校园基本情况
     */
    @TableField(value = "basic_info")
    @ApiModelProperty(value = "校园基本情况", required = true)
    private String basicInfo;
    /**
     * 来电来访
     */
    @TableField(value = "phone_and_visit_info")
    @ApiModelProperty(value = "来电来访")
    private String phoneAndVisitInfo;
    /**
     * 偶发事件及处置情况
     */
    @TableField(value = "contingency_and_handle_info")
    @ApiModelProperty(value = "偶发事件及处置情况")
    private String contingencyAndHandleInfo;
    /**
     * 问题与建议
     */
    @TableField(value = "problem_and_advice_info")
    @ApiModelProperty(value = "问题与建议")
    private String problemAndAdviceInfo;
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
    public CampusOverviewHoliday setDefault() {
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
        if (this.getSchoolyardId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "校区ID sys_schoolyard.id不能为空！");
            return false;
        }
        if (this.getLeaderDutyId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "领导值班ID day_leader_duty.id不能为空！");
            return false;
        }
        if (this.getBasicInfo() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "校园基本情况不能为空！");
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
