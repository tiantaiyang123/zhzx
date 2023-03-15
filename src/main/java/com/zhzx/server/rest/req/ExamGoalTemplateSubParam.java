/**
 * 项目：中华中学管理平台
 * 模型分组：成绩管理
 * 模型名称：目标模板详情表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest.req;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhzx.server.domain.ExamGoalTemplateSub;
import com.zhzx.server.enums.GoalEnum;
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
@ApiModel(value = "目标模板详情表参数", description = "")
public class ExamGoalTemplateSubParam implements Serializable {
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
     * 目标模板ID dat_exam_goal_template.id
     */
    @ApiModelProperty(value = "目标模板ID dat_exam_goal_template.id")
    private Long examGoalTemplateId;
    /**
     * 班级ID
     */
    @ApiModelProperty(value = "班级ID")
    private Long clazzId;
    /**
     * 目标名称
     */
    @ApiModelProperty(value = "目标名称")
    private String goalName;
    /**
     * 总分目标值
     */
    @ApiModelProperty(value = "总分目标值")
    private Integer goalValue;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sortOrder;
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
    public QueryWrapper<ExamGoalTemplateSub> toQueryWrapper() {
        QueryWrapper<ExamGoalTemplateSub> wrapper = Wrappers.<ExamGoalTemplateSub>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getExamGoalTemplateId() != null, "exam_goal_template_id", this.getExamGoalTemplateId());
        wrapper.eq(this.getClazzId() != null, "clazz_id", this.getClazzId());
        if (this.getGoalName() != null) {
            if (this.getGoalName().startsWith("%") && this.getGoalName().endsWith("%")) {
                wrapper.like("goal_name", this.getGoalName().substring(1, this.getGoalName().length() - 1));
            } else if (this.getGoalName().startsWith("%") && !this.getGoalName().endsWith("%")) {
                wrapper.likeLeft("goal_name", this.getGoalName().substring(1));
            } else if (this.getGoalName().endsWith("%")) {
                wrapper.likeRight("goal_name", this.getGoalName().substring(0, this.getGoalName().length() - 1));
            } else {
                wrapper.eq("goal_name", this.getGoalName());
            }
        }
        wrapper.eq(this.getGoalValue() != null, "goal_value", this.getGoalValue());
        wrapper.eq(this.getSortOrder() != null, "sort_order", this.getSortOrder());
        wrapper.eq(this.getCreateTime() != null, "create_time", this.getCreateTime());
        wrapper.ge(this.getCreateTimeFrom() != null, "create_time", this.getCreateTimeFrom());
        wrapper.lt(this.getCreateTimeTo() != null, "create_time", this.getCreateTimeTo());
        wrapper.eq(this.getUpdateTime() != null, "update_time", this.getUpdateTime());
        wrapper.ge(this.getUpdateTimeFrom() != null, "update_time", this.getUpdateTimeFrom());
        wrapper.lt(this.getUpdateTimeTo() != null, "update_time", this.getUpdateTimeTo());
        return wrapper;
    }

}
