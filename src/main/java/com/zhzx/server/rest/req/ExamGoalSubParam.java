/**
* 项目：中华中学管理平台
* 模型分组：成绩管理
* 模型名称：考试分段表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
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
import com.zhzx.server.enums.GoalEnum;
import com.zhzx.server.enums.ExamCompareEnum;
import com.zhzx.server.domain.ExamGoalSub;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "考试分段表参数", description = "")
public class ExamGoalSubParam implements Serializable {
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
     * 学科id,原始分分段90001，赋分分段100001
     */
    @ApiModelProperty(value = "学科id,原始分分段90001，赋分分段100001")
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
     * 分段逻辑
     */
    @ApiModelProperty(value = "分段逻辑")
    private ExamCompareEnum type;
    /**
     * 分段逻辑 IN值List
     */
    @ApiModelProperty(value = "分段逻辑 IN值List")
    private List<String> typeList;
    /**
     * 目标值
     */
    @ApiModelProperty(value = "目标值")
    private Integer goalValue;
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
    public QueryWrapper<ExamGoalSub> toQueryWrapper() {
        QueryWrapper<ExamGoalSub> wrapper = Wrappers.<ExamGoalSub>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getExamId() != null, "exam_id", this.getExamId());
        wrapper.eq(this.getSubjectId() != null, "subject_id", this.getSubjectId());
        wrapper.eq(this.getSubjectType() != null, "subject_type", this.getSubjectType());
        wrapper.in(this.getSubjectTypeList() != null && this.getSubjectTypeList().size() > 0, "subject_type", this.getSubjectTypeList());
        wrapper.eq(this.getType() != null, "type", this.getType());
        wrapper.in(this.getTypeList() != null && this.getTypeList().size() > 0, "type", this.getTypeList());
        wrapper.eq(this.getGoalValue() != null, "goal_value", this.getGoalValue());
        wrapper.eq(this.getCreateTime() != null, "create_time", this.getCreateTime());
        wrapper.ge(this.getCreateTimeFrom() != null, "create_time", this.getCreateTimeFrom());
        wrapper.lt(this.getCreateTimeTo() != null, "create_time", this.getCreateTimeTo());
        wrapper.eq(this.getUpdateTime() != null, "update_time", this.getUpdateTime());
        wrapper.ge(this.getUpdateTimeFrom() != null, "update_time", this.getUpdateTimeFrom());
        wrapper.lt(this.getUpdateTimeTo() != null, "update_time", this.getUpdateTimeTo());
        return wrapper;
    }

}
