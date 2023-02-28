/**
 * 项目：中华中学管理平台
 * 模型分组：成绩管理
 * 模型名称：考试目标表
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
import com.zhzx.server.enums.SettingEnum;
import com.zhzx.server.enums.TargetEnum;
import com.zhzx.server.enums.GoalEnum;
import com.zhzx.server.domain.ExamGoal;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "考试目标表参数", description = "")
public class ExamGoalParam implements Serializable {
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
     * dat_exam.id
     */
    @ApiModelProperty(value = "dat_exam.id")
    private Long examId;
    /**
     * 目标名称
     */
    @ApiModelProperty(value = "目标名称")
    private String name;
    /**
     * 设置方式
     */
    @ApiModelProperty(value = "设置方式")
    private SettingEnum settingType;
    /**
     * 设置方式 IN值List
     */
    @ApiModelProperty(value = "设置方式 IN值List")
    private List<String> settingTypeList;
    /**
     * 目标类型
     */
    @ApiModelProperty(value = "目标类型")
    private TargetEnum targetType;
    /**
     * 目标类型 IN值List
     */
    @ApiModelProperty(value = "目标类型 IN值List")
    private List<String> targetTypeList;
    /**
     * 目标科目类型
     */
    @ApiModelProperty(value = "目标科目类型")
    private GoalEnum subjectType;
    /**
     * 目标科目类型 IN值List
     */
    @ApiModelProperty(value = "目标科目类型 IN值List")
    private List<String> subjectTypeList;
    /**
     * 总分目标值
     */
    @ApiModelProperty(value = "总分目标值")
    private Integer goalValue;
    /**
     * 单科目标值
     */
    @ApiModelProperty(value = "单科目标值")
    private Integer subjectValue;
    /**
     * 手动输入的各班级目标参考，用json格式
     */
    @ApiModelProperty(value = "手动输入的各班级目标参考，用json格式")
    private String goalReference;
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
    public QueryWrapper<ExamGoal> toQueryWrapper() {
        QueryWrapper<ExamGoal> wrapper = Wrappers.<ExamGoal>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getExamId() != null, "exam_id", this.getExamId());
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
        wrapper.eq(this.getSettingType() != null, "setting_type", this.getSettingType());
        wrapper.in(this.getSettingTypeList() != null && this.getSettingTypeList().size() > 0, "setting_type", this.getSettingTypeList());
        wrapper.eq(this.getTargetType() != null, "target_type", this.getTargetType());
        wrapper.in(this.getTargetTypeList() != null && this.getTargetTypeList().size() > 0, "target_type", this.getTargetTypeList());
        wrapper.eq(this.getSubjectType() != null, "subject_type", this.getSubjectType());
        wrapper.in(this.getSubjectTypeList() != null && this.getSubjectTypeList().size() > 0, "subject_type", this.getSubjectTypeList());
        wrapper.eq(this.getGoalValue() != null, "goal_value", this.getGoalValue());
        wrapper.eq(this.getSubjectValue() != null, "subject_value", this.getSubjectValue());
        if (this.getGoalReference() != null) {
            if (this.getGoalReference().startsWith("%") && this.getGoalReference().endsWith("%")) {
                wrapper.like("goal_reference", this.getGoalReference().substring(1, this.getGoalReference().length() - 1));
            } else if (this.getGoalReference().startsWith("%") && !this.getGoalReference().endsWith("%")) {
                wrapper.likeLeft("goal_reference", this.getGoalReference().substring(1));
            } else if (this.getGoalReference().endsWith("%")) {
                wrapper.likeRight("goal_reference", this.getGoalReference().substring(0, this.getGoalReference().length() - 1));
            } else {
                wrapper.eq("goal_reference", this.getGoalReference());
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
