package com.zhzx.server.vo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhzx.server.domain.TeachingResult;
import com.zhzx.server.enums.SemesterEnum;
import com.zhzx.server.enums.TeachingResultLevelEnum;
import com.zhzx.server.enums.TeachingResultStateEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

@Data
public class TeachingResultVo {
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
     * 成果时间(年-月)
     */
    @ApiModelProperty(value = "成果时间(年-月)")
    private String resultDate;
    /**
     * 学年
     */
    @ApiModelProperty(value = "学年")
    private String year;
    /**
     * 学期
     */
    @ApiModelProperty(value = "学期")
    private SemesterEnum semester;
    /**
     * 学期 IN值List
     */
    @ApiModelProperty(value = "学期 IN值List")
    private List<String> semesterList;
    /**
     * 级别
     */
    @ApiModelProperty(value = "级别")
    private TeachingResultLevelEnum level;
    /**
     * 级别 IN值List
     */
    @ApiModelProperty(value = "级别 IN值List")
    private List<String> levelList;
    /**
     * 成果名称
     */
    @ApiModelProperty(value = "成果名称")
    private String name;
    /**
     * 审核人ID sys_stuff.id
     */
    @ApiModelProperty(value = "审核人ID sys_stuff.id")
    private Long reviewerId;
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private TeachingResultStateEnum state;
    /**
     * 状态 IN值List
     */
    @ApiModelProperty(value = "状态 IN值List")
    private List<String> stateList;
    /**
     * 原因
     */
    @ApiModelProperty(value = "原因")
    private String reason;
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
    public QueryWrapper<TeachingResult> toQueryWrapper() {
        QueryWrapper<TeachingResult> wrapper = Wrappers.<TeachingResult>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        if (this.getResultDate() != null) {
            if (this.getResultDate().startsWith("%") && this.getResultDate().endsWith("%")) {
                wrapper.like("result_date", this.getResultDate().substring(1, this.getResultDate().length() - 1));
            } else if (this.getResultDate().startsWith("%") && !this.getResultDate().endsWith("%")) {
                wrapper.likeLeft("result_date", this.getResultDate().substring(1));
            } else if (this.getResultDate().endsWith("%")) {
                wrapper.likeRight("result_date", this.getResultDate().substring(0, this.getResultDate().length() - 1));
            } else {
                wrapper.eq("result_date", this.getResultDate());
            }
        }
        if (this.getYear() != null) {
            if (this.getYear().startsWith("%") && this.getYear().endsWith("%")) {
                wrapper.like("year", this.getYear().substring(1, this.getYear().length() - 1));
            } else if (this.getYear().startsWith("%") && !this.getYear().endsWith("%")) {
                wrapper.likeLeft("year", this.getYear().substring(1));
            } else if (this.getYear().endsWith("%")) {
                wrapper.likeRight("year", this.getYear().substring(0, this.getYear().length() - 1));
            } else {
                wrapper.eq("year", this.getYear());
            }
        }
        wrapper.eq(this.getSemester() != null, "semester", this.getSemester());
        wrapper.in(this.getSemesterList() != null && this.getSemesterList().size() > 0, "semester", this.getSemesterList());
        wrapper.eq(this.getLevel() != null, "level", this.getLevel());
        wrapper.in(this.getLevelList() != null && this.getLevelList().size() > 0, "level", this.getLevelList());
        if (this.getName() != null) {
            if (this.getName().startsWith("%") && this.getName().endsWith("%")) {
                wrapper.like("name", this.getName().substring(1, this.getName().length() - 1));
            } else if (this.getName().startsWith("%") && !this.getName().endsWith("%")) {
                wrapper.likeLeft("name", this.getName().substring(1));
            } else if (this.getName().endsWith("%")) {
                wrapper.likeRight("name", this.getName().substring(0, this.getName().length() - 1));
            } else {
                wrapper.eq("name", this.getName());
            }
        }
        wrapper.eq(this.getReviewerId() != null, "reviewer_id", this.getReviewerId());
        wrapper.eq(this.getState() != null, "state", this.getState());
        wrapper.in(this.getStateList() != null && this.getStateList().size() > 0, "state", this.getStateList());
        if (this.getReason() != null) {
            if (this.getReason().startsWith("%") && this.getReason().endsWith("%")) {
                wrapper.like("reason", this.getReason().substring(1, this.getReason().length() - 1));
            } else if (this.getReason().startsWith("%") && !this.getReason().endsWith("%")) {
                wrapper.likeLeft("reason", this.getReason().substring(1));
            } else if (this.getReason().endsWith("%")) {
                wrapper.likeRight("reason", this.getReason().substring(0, this.getReason().length() - 1));
            } else {
                wrapper.eq("reason", this.getReason());
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
