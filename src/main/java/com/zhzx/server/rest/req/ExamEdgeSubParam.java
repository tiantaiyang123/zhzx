/**
 * 项目：中华中学管理平台
 * 模型分组：成绩管理
 * 模型名称：考试临界生分数段设置表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest.req;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhzx.server.domain.ExamEdgeSub;
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
@ApiModel(value = "考试临界生分数段设置表参数", description = "")
public class ExamEdgeSubParam implements Serializable {
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
     * 基准分数
     */
    @ApiModelProperty(value = "基准分数")
    private Integer baseValue;
    /**
     * 浮动分数
     */
    @ApiModelProperty(value = "浮动分数")
    private Integer floatValue;
    /**
     * 上限分数
     */
    @ApiModelProperty(value = "上限分数")
    private Integer upperValue;
    /**
     * 下限分数
     */
    @ApiModelProperty(value = "下限分数")
    private Integer lowerValue;
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
    /***
     * 二位bit
     */
    private String state;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<ExamEdgeSub> toQueryWrapper() {
        QueryWrapper<ExamEdgeSub> wrapper = Wrappers.<ExamEdgeSub>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getExamId() != null, "exam_id", this.getExamId());
        wrapper.eq(this.getSubjectId() != null, "subject_id", this.getSubjectId());
        wrapper.eq(this.getBaseValue() != null, "base_value", this.getBaseValue());
        wrapper.eq(this.getFloatValue() != null, "float_value", this.getFloatValue());
        wrapper.eq(this.getUpperValue() != null, "upper_value", this.getUpperValue());
        wrapper.eq(this.getLowerValue() != null, "lower_value", this.getLowerValue());
        wrapper.eq(this.getCreateTime() != null, "create_time", this.getCreateTime());
        wrapper.ge(this.getCreateTimeFrom() != null, "create_time", this.getCreateTimeFrom());
        wrapper.lt(this.getCreateTimeTo() != null, "create_time", this.getCreateTimeTo());
        wrapper.eq(this.getUpdateTime() != null, "update_time", this.getUpdateTime());
        wrapper.ge(this.getUpdateTimeFrom() != null, "update_time", this.getUpdateTimeFrom());
        wrapper.lt(this.getUpdateTimeTo() != null, "update_time", this.getUpdateTimeTo());
        return wrapper;
    }

}
