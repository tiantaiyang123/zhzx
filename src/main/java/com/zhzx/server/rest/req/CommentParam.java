/**
 * 项目：中华中学管理平台
 * 模型分组：一日常规管理
 * 模型名称：意见与建议表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest.req;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhzx.server.domain.Comment;
import com.zhzx.server.enums.ClassifyEnum;
import com.zhzx.server.enums.CommentStateEnum;
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
@ApiModel(value = "意见与建议表参数", description = "")
public class CommentParam implements Serializable {
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
     * 一日常规分类
     */
    @ApiModelProperty(value = "一日常规分类")
    private ClassifyEnum classify;
    /**
     * 一日常规分类 IN值List
     */
    @ApiModelProperty(value = "一日常规分类 IN值List")
    private List<String> classifyList;
    /**
     * 项目名称
     */
    @ApiModelProperty(value = "项目名称")
    private String itemName;
    /**
     * 一日常规ID (classify为数据表名)
     */
    @ApiModelProperty(value = "一日常规ID (classify为数据表名)")
    private Long dailyRoutineId;
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
     * 填报人
     */
    @ApiModelProperty(value = "填报人")
    private String editor;
    /**
     * 意见与建议 可以从sys_label.classify == YRCG_YJJY选择，也可以录入
     */
    @ApiModelProperty(value = "意见与建议 可以从sys_label.classify == YRCG_YJJY选择，也可以录入")
    private String content;
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private CommentStateEnum state;
    /**
     * 状态 IN值List
     */
    @ApiModelProperty(value = "状态 IN值List")
    private List<String> stateList;
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
    public QueryWrapper<Comment> toQueryWrapper() {
        QueryWrapper<Comment> wrapper = Wrappers.<Comment>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getClassify() != null, "classify", this.getClassify());
        wrapper.in(this.getClassifyList() != null && this.getClassifyList().size() > 0, "classify", this.getClassifyList());
        wrapper.eq(this.getSchoolyardId() != null, "schoolyard_id", this.getSchoolyardId());
        if (this.getItemName() != null) {
            if (this.getItemName().startsWith("%") && this.getItemName().endsWith("%")) {
                wrapper.like("item_name", this.getItemName().substring(1, this.getItemName().length() - 1));
            } else if (this.getItemName().startsWith("%") && !this.getItemName().endsWith("%")) {
                wrapper.likeLeft("item_name", this.getItemName().substring(1));
            } else if (this.getItemName().endsWith("%")) {
                wrapper.likeRight("item_name", this.getItemName().substring(0, this.getItemName().length() - 1));
            } else {
                wrapper.eq("item_name", this.getItemName());
            }
        }
        wrapper.eq(this.getDailyRoutineId() != null, "daily_routine_id", this.getDailyRoutineId());
        wrapper.eq(this.getStartTime() != null, "start_time", this.getStartTime());
        wrapper.ge(this.getStartTimeFrom() != null, "start_time", this.getStartTimeFrom());
        wrapper.lt(this.getStartTimeTo() != null, "start_time", this.getStartTimeTo());
        wrapper.eq(this.getEndTime() != null, "end_time", this.getEndTime());
        wrapper.ge(this.getEndTimeFrom() != null, "end_time", this.getEndTimeFrom());
        wrapper.lt(this.getEndTimeTo() != null, "end_time", this.getEndTimeTo());
        if (this.getEditor() != null) {
            if (this.getEditor().startsWith("%") && this.getEditor().endsWith("%")) {
                wrapper.like("editor", this.getEditor().substring(1, this.getEditor().length() - 1));
            } else if (this.getEditor().startsWith("%") && !this.getEditor().endsWith("%")) {
                wrapper.likeLeft("editor", this.getEditor().substring(1));
            } else if (this.getEditor().endsWith("%")) {
                wrapper.likeRight("editor", this.getEditor().substring(0, this.getEditor().length() - 1));
            } else {
                wrapper.eq("editor", this.getEditor());
            }
        }
        if (this.getContent() != null) {
            if (this.getContent().startsWith("%") && this.getContent().endsWith("%")) {
                wrapper.like("content", this.getContent().substring(1, this.getContent().length() - 1));
            } else if (this.getContent().startsWith("%") && !this.getContent().endsWith("%")) {
                wrapper.likeLeft("content", this.getContent().substring(1));
            } else if (this.getContent().endsWith("%")) {
                wrapper.likeRight("content", this.getContent().substring(0, this.getContent().length() - 1));
            } else {
                wrapper.eq("content", this.getContent());
            }
        }
        wrapper.eq(this.getState() != null, "state", this.getState());
        wrapper.in(this.getStateList() != null && this.getStateList().size() > 0, "state", this.getStateList());
        wrapper.eq(this.getCreateTime() != null, "create_time", this.getCreateTime());
        wrapper.ge(this.getCreateTimeFrom() != null, "create_time", this.getCreateTimeFrom());
        wrapper.lt(this.getCreateTimeTo() != null, "create_time", this.getCreateTimeTo());
        wrapper.eq(this.getUpdateTime() != null, "update_time", this.getUpdateTime());
        wrapper.ge(this.getUpdateTimeFrom() != null, "update_time", this.getUpdateTimeFrom());
        wrapper.lt(this.getUpdateTimeTo() != null, "update_time", this.getUpdateTimeTo());
        return wrapper;
    }

}
