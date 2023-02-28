/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：早读情况表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zhzx.server.dto.CommentDto;
import com.zhzx.server.enums.DayOrderEnum;
import com.zhzx.server.enums.EvaluationLevelEnum;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.vo.CheckItemVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`day_morning_reading`")
@ApiModel(value = "MorningReading", description = "早读情况表")
public class MorningReading extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * 年级 sys_grade.id
     */
    @TableField(value = "schoolyard_id")
    @ApiModelProperty(value = "校区 sys_schoolyard.id", required = true)
    private Long schoolyardId;
    @TableField(exist = false)
    @ApiModelProperty(value = "校区 sys_schoolyard.id", required = true)
    private Schoolyard schoolyard;
    /**
     * ID系统自动生成
     */
    @ApiModelProperty(value = "ID系统自动生成")
    @TableField(exist = false)
    private List<MorningReadingImages> morningReadingImagesList;
    /**
     * 领导值班ID day_leader_duty.id
     */
    @TableField(value = "leader_duty_id")
    @ApiModelProperty(value = "领导值班ID day_leader_duty.id", required = true)
    private Long leaderDutyId;
    /**
     * 高一年级早读秩序
     */
    @TableField(value = "reading_order1")
    @ApiModelProperty(value = "高一年级早读秩序", required = true)
    private DayOrderEnum readingOrder1;
    /**
     * 高一年级评价等级
     */
    @TableField(value = "evaluation_level1")
    @ApiModelProperty(value = "高一年级评价等级", required = true)
    private EvaluationLevelEnum evaluationLevel1;
    /**
     * 高二年级早读秩序
     */
    @TableField(value = "reading_order2")
    @ApiModelProperty(value = "高二年级早读秩序", required = true)
    private DayOrderEnum readingOrder2;
    /**
     * 高二年级评价等级
     */
    @TableField(value = "evaluation_level2")
    @ApiModelProperty(value = "高二年级评价等级", required = true)
    private EvaluationLevelEnum evaluationLevel2;
    /**
     * 高三年级早读秩序
     */
    @TableField(value = "reading_order3")
    @ApiModelProperty(value = "高三年级早读秩序", required = true)
    private DayOrderEnum readingOrder3;
    /**
     * 高三年级评价等级
     */
    @TableField(value = "evaluation_level3")
    @ApiModelProperty(value = "高三年级评价等级", required = true)
    private EvaluationLevelEnum evaluationLevel3;
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
     * 检查时间
     */
    @TableField(value = "check_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "检查时间", required = true)
    private java.util.Date checkTime;
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

    @TableField(exist = false)
    @ApiModelProperty(value = "偶发情况")
    private List<Incident> incidentList;

    @TableField(exist = false)
    @ApiModelProperty(value = "意见与建议")
    private List<CommentDto> commentList;

    @TableField(exist = false)
    private List<CheckItemVo> checkItemVoList;

    @TableField(exist = false)
    private String leaderName;

    /**
     * 设置默认值
     */
    public MorningReading setDefault() {
        if (this.getReadingOrder1() == null) {
            this.setReadingOrder1(DayOrderEnum.NORMAL);
        }
        if (this.getEvaluationLevel1() == null) {
            this.setEvaluationLevel1(EvaluationLevelEnum.A);
        }
        if (this.getReadingOrder2() == null) {
            this.setReadingOrder2(DayOrderEnum.NORMAL);
        }
        if (this.getEvaluationLevel2() == null) {
            this.setEvaluationLevel2(EvaluationLevelEnum.A);
        }
        if (this.getReadingOrder3() == null) {
            this.setReadingOrder3(DayOrderEnum.NORMAL);
        }
        if (this.getEvaluationLevel3() == null) {
            this.setEvaluationLevel3(EvaluationLevelEnum.A);
        }
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
        if (this.getSchoolyardId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "校区 sys_schoolyard.id不能为空！");
            return false;
        }
        if (this.getReadingOrder1() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "高一年级早读秩序不能为空！");
            return false;
        }
        if (this.getEvaluationLevel1() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "高一年级评价等级不能为空！");
            return false;
        }
        if (this.getReadingOrder2() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "高二年级早读秩序不能为空！");
            return false;
        }
        if (this.getEvaluationLevel2() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "高二年级评价等级不能为空！");
            return false;
        }
        if (this.getReadingOrder3() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "高三年级早读秩序不能为空！");
            return false;
        }
        if (this.getEvaluationLevel3() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "高三年级评价等级不能为空！");
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
        if (this.getCheckTime() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "检查时间不能为空！");
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
