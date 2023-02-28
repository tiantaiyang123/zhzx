/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：早读情况表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest.req;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhzx.server.domain.MorningReading;
import com.zhzx.server.enums.DayOrderEnum;
import com.zhzx.server.enums.EvaluationLevelEnum;
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
@ApiModel(value = "早读情况表参数", description = "")
public class MorningReadingParam implements Serializable {
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
     * 高一年级早读秩序
     */
    @ApiModelProperty(value = "高一年级早读秩序")
    private DayOrderEnum readingOrder1;
    /**
     * 高一年级早读秩序 IN值List
     */
    @ApiModelProperty(value = "高一年级早读秩序 IN值List")
    private List<String> readingOrder1List;
    /**
     * 高一年级评价等级
     */
    @ApiModelProperty(value = "高一年级评价等级")
    private EvaluationLevelEnum evaluationLevel1;
    /**
     * 高一年级评价等级 IN值List
     */
    @ApiModelProperty(value = "高一年级评价等级 IN值List")
    private List<String> evaluationLevel1List;
    /**
     * 高二年级早读秩序
     */
    @ApiModelProperty(value = "高二年级早读秩序")
    private DayOrderEnum readingOrder2;
    /**
     * 高二年级早读秩序 IN值List
     */
    @ApiModelProperty(value = "高二年级早读秩序 IN值List")
    private List<String> readingOrder2List;
    /**
     * 高二年级评价等级
     */
    @ApiModelProperty(value = "高二年级评价等级")
    private EvaluationLevelEnum evaluationLevel2;
    /**
     * 高二年级评价等级 IN值List
     */
    @ApiModelProperty(value = "高二年级评价等级 IN值List")
    private List<String> evaluationLevel2List;
    /**
     * 高三年级早读秩序
     */
    @ApiModelProperty(value = "高三年级早读秩序")
    private DayOrderEnum readingOrder3;
    /**
     * 高三年级早读秩序 IN值List
     */
    @ApiModelProperty(value = "高三年级早读秩序 IN值List")
    private List<String> readingOrder3List;
    /**
     * 高三年级评价等级
     */
    @ApiModelProperty(value = "高三年级评价等级")
    private EvaluationLevelEnum evaluationLevel3;
    /**
     * 高三年级评价等级 IN值List
     */
    @ApiModelProperty(value = "高三年级评价等级 IN值List")
    private List<String> evaluationLevel3List;
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
    public QueryWrapper<MorningReading> toQueryWrapper() {
        QueryWrapper<MorningReading> wrapper = Wrappers.<MorningReading>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getLeaderDutyId() != null, "leader_duty_id", this.getLeaderDutyId());
        wrapper.eq(this.getReadingOrder1() != null, "reading_order1", this.getReadingOrder1());
        wrapper.in(this.getReadingOrder1List() != null && this.getReadingOrder1List().size() > 0, "reading_order1", this.getReadingOrder1List());
        wrapper.eq(this.getEvaluationLevel1() != null, "evaluation_level1", this.getEvaluationLevel1());
        wrapper.in(this.getEvaluationLevel1List() != null && this.getEvaluationLevel1List().size() > 0, "evaluation_level1", this.getEvaluationLevel1List());
        wrapper.eq(this.getReadingOrder2() != null, "reading_order2", this.getReadingOrder2());
        wrapper.in(this.getReadingOrder2List() != null && this.getReadingOrder2List().size() > 0, "reading_order2", this.getReadingOrder2List());
        wrapper.eq(this.getEvaluationLevel2() != null, "evaluation_level2", this.getEvaluationLevel2());
        wrapper.in(this.getEvaluationLevel2List() != null && this.getEvaluationLevel2List().size() > 0, "evaluation_level2", this.getEvaluationLevel2List());
        wrapper.eq(this.getReadingOrder3() != null, "reading_order3", this.getReadingOrder3());
        wrapper.in(this.getReadingOrder3List() != null && this.getReadingOrder3List().size() > 0, "reading_order3", this.getReadingOrder3List());
        wrapper.eq(this.getEvaluationLevel3() != null, "evaluation_level3", this.getEvaluationLevel3());
        wrapper.in(this.getEvaluationLevel3List() != null && this.getEvaluationLevel3List().size() > 0, "evaluation_level3", this.getEvaluationLevel3List());
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
