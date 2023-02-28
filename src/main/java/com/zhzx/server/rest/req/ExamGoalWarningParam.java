/**
 * 项目：中华中学管理平台
 * 模型分组：成绩管理
 * 模型名称：考试分数预警表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest.req;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhzx.server.domain.ExamGoalWarning;
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
@ApiModel(value = "考试分数预警表参数", description = "")
public class ExamGoalWarningParam implements Serializable {
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
     * 目标id
     */
    @ApiModelProperty(value = "目标id")
    private Long goalId;
    /**
     * 学科id,赋分90002，赋分分段100001
     */
    @ApiModelProperty(value = "学科id,赋分90002，赋分分段100001")
    private Long subjectId;
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
     * 左边界分数
     */
    @ApiModelProperty(value = "左边界分数")
    private Integer leftValue;
    /**
     * 右边界分数
     */
    @ApiModelProperty(value = "右边界分数")
    private Integer rightValue;
    /**
     * 顺序
     */
    @ApiModelProperty(value = "顺序")
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
    public QueryWrapper<ExamGoalWarning> toQueryWrapper() {
        QueryWrapper<ExamGoalWarning> wrapper = Wrappers.<ExamGoalWarning>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getExamId() != null, "exam_id", this.getExamId());
        wrapper.eq(this.getGoalId() != null, "goal_id", this.getGoalId());
        wrapper.eq(this.getSubjectId() != null, "subject_id", this.getSubjectId());
        wrapper.eq(this.getSubjectType() != null, "subject_type", this.getSubjectType());
        wrapper.in(this.getSubjectTypeList() != null && this.getSubjectTypeList().size() > 0, "subject_type", this.getSubjectTypeList());
        wrapper.eq(this.getLeftValue() != null, "left_value", this.getLeftValue());
        wrapper.eq(this.getRightValue() != null, "right_value", this.getRightValue());
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
