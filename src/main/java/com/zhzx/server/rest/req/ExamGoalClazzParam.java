/**
 * 项目：中华中学管理平台
 * 模型分组：成绩管理
 * 模型名称：考试目标班级表
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
import com.zhzx.server.domain.ExamGoalClazz;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "考试目标班级表参数", description = "")
public class ExamGoalClazzParam implements Serializable {
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
     * dat_exam_goal.id
     */
    @ApiModelProperty(value = "dat_exam_goal.id")
    private Long examGoalId;
    /**
     * 班级ID sys_clazz.id
     */
    @ApiModelProperty(value = "班级ID sys_clazz.id")
    private Long clazzId;
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
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<ExamGoalClazz> toQueryWrapper() {
        QueryWrapper<ExamGoalClazz> wrapper = Wrappers.<ExamGoalClazz>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getExamGoalId() != null, "exam_goal_id", this.getExamGoalId());
        wrapper.eq(this.getClazzId() != null, "clazz_id", this.getClazzId());
        wrapper.eq(this.getGoalValue() != null, "goal_value", this.getGoalValue());
        wrapper.eq(this.getSubjectValue() != null, "subject_value", this.getSubjectValue());
        return wrapper;
    }

}
