/**
 * 项目：中华中学管理平台
 * 模型分组：一日常规管理
 * 模型名称：假期校园概况表
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
import com.zhzx.server.domain.CampusOverviewHoliday;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "假期校园概况表参数", description = "")
public class CampusOverviewHolidayParam implements Serializable {
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
     * 校区ID sys_schoolyard.id
     */
    @ApiModelProperty(value = "校区ID sys_schoolyard.id")
    private Long schoolyardId;
    /**
     * 领导值班ID day_leader_duty.id
     */
    @ApiModelProperty(value = "领导值班ID day_leader_duty.id")
    private Long leaderDutyId;
    /**
     * 校园基本情况
     */
    @ApiModelProperty(value = "校园基本情况")
    private String basicInfo;
    /**
     * 来电来访
     */
    @ApiModelProperty(value = "来电来访")
    private String phoneAndVisitInfo;
    /**
     * 偶发事件及处置情况
     */
    @ApiModelProperty(value = "偶发事件及处置情况")
    private String contingencyAndHandleInfo;
    /**
     * 问题与建议
     */
    @ApiModelProperty(value = "问题与建议")
    private String problemAndAdviceInfo;
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
    public QueryWrapper<CampusOverviewHoliday> toQueryWrapper() {
        QueryWrapper<CampusOverviewHoliday> wrapper = Wrappers.<CampusOverviewHoliday>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getSchoolyardId() != null, "schoolyard_id", this.getSchoolyardId());
        wrapper.eq(this.getLeaderDutyId() != null, "leader_duty_id", this.getLeaderDutyId());
        if (this.getBasicInfo() != null) {
            if (this.getBasicInfo().startsWith("%") && this.getBasicInfo().endsWith("%")) {
                wrapper.like("basic_info", this.getBasicInfo().substring(1, this.getBasicInfo().length() - 1));
            } else if (this.getBasicInfo().startsWith("%") && !this.getBasicInfo().endsWith("%")) {
                wrapper.likeLeft("basic_info", this.getBasicInfo().substring(1));
            } else if (this.getBasicInfo().endsWith("%")) {
                wrapper.likeRight("basic_info", this.getBasicInfo().substring(0, this.getBasicInfo().length() - 1));
            } else {
                wrapper.eq("basic_info", this.getBasicInfo());
            }
        }
        if (this.getPhoneAndVisitInfo() != null) {
            if (this.getPhoneAndVisitInfo().startsWith("%") && this.getPhoneAndVisitInfo().endsWith("%")) {
                wrapper.like("phone_and_visit_info", this.getPhoneAndVisitInfo().substring(1, this.getPhoneAndVisitInfo().length() - 1));
            } else if (this.getPhoneAndVisitInfo().startsWith("%") && !this.getPhoneAndVisitInfo().endsWith("%")) {
                wrapper.likeLeft("phone_and_visit_info", this.getPhoneAndVisitInfo().substring(1));
            } else if (this.getPhoneAndVisitInfo().endsWith("%")) {
                wrapper.likeRight("phone_and_visit_info", this.getPhoneAndVisitInfo().substring(0, this.getPhoneAndVisitInfo().length() - 1));
            } else {
                wrapper.eq("phone_and_visit_info", this.getPhoneAndVisitInfo());
            }
        }
        if (this.getContingencyAndHandleInfo() != null) {
            if (this.getContingencyAndHandleInfo().startsWith("%") && this.getContingencyAndHandleInfo().endsWith("%")) {
                wrapper.like("contingency_and_handle_info", this.getContingencyAndHandleInfo().substring(1, this.getContingencyAndHandleInfo().length() - 1));
            } else if (this.getContingencyAndHandleInfo().startsWith("%") && !this.getContingencyAndHandleInfo().endsWith("%")) {
                wrapper.likeLeft("contingency_and_handle_info", this.getContingencyAndHandleInfo().substring(1));
            } else if (this.getContingencyAndHandleInfo().endsWith("%")) {
                wrapper.likeRight("contingency_and_handle_info", this.getContingencyAndHandleInfo().substring(0, this.getContingencyAndHandleInfo().length() - 1));
            } else {
                wrapper.eq("contingency_and_handle_info", this.getContingencyAndHandleInfo());
            }
        }
        if (this.getProblemAndAdviceInfo() != null) {
            if (this.getProblemAndAdviceInfo().startsWith("%") && this.getProblemAndAdviceInfo().endsWith("%")) {
                wrapper.like("problem_and_advice_info", this.getProblemAndAdviceInfo().substring(1, this.getProblemAndAdviceInfo().length() - 1));
            } else if (this.getProblemAndAdviceInfo().startsWith("%") && !this.getProblemAndAdviceInfo().endsWith("%")) {
                wrapper.likeLeft("problem_and_advice_info", this.getProblemAndAdviceInfo().substring(1));
            } else if (this.getProblemAndAdviceInfo().endsWith("%")) {
                wrapper.likeRight("problem_and_advice_info", this.getProblemAndAdviceInfo().substring(0, this.getProblemAndAdviceInfo().length() - 1));
            } else {
                wrapper.eq("problem_and_advice_info", this.getProblemAndAdviceInfo());
            }
        }
        wrapper.eq(this.getCreateTime() != null, "create_time", this.getCreateTime());
        wrapper.ge(this.getCreateTimeFrom() != null, "create_time", this.getCreateTimeFrom());
        wrapper.lt(this.getCreateTimeTo() != null, "create_time", this.getCreateTimeTo());
        wrapper.eq(this.getUpdateTime() != null, "update_time", this.getUpdateTime());
        wrapper.ge(this.getUpdateTimeFrom() != null, "update_time", this.getUpdateTimeFrom());
        wrapper.lt(this.getUpdateTimeTo() != null, "update_time", this.getUpdateTimeTo());
        return wrapper;
    }

}
