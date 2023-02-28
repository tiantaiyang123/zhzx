/**
* 项目：中华中学管理平台
* 模型分组：成绩管理
* 模型名称：小题得分情况表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.rest.req;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhzx.server.domain.PracticeTopic;
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
@ApiModel(value = "小题得分情况表参数", description = "")
public class PracticeTopicParam implements Serializable {
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
     * 练习ID dat_practice.id
     */
    @ApiModelProperty(value = "练习ID dat_practice.id")
    private Long practiceId;
    /**
     * 班级ID sys_clazz.id
     */
    @ApiModelProperty(value = "班级ID sys_clazz.id")
    private Long clazzId;
    /**
     * 班级ID sys_clazz.id
     */
    @ApiModelProperty(value = "班级ID列表")
    private List<Long> clazzIds;
    /**
     * 题目
     */
    @ApiModelProperty(value = "题目")
    private String topicName;
    /**
     * 题号
     */
    @ApiModelProperty(value = "题号")
    private Integer topicNumber;
    /**
     * 题型
     */
    @ApiModelProperty(value = "题型")
    private String topicType;
    /**
     * 分值
     */
    @ApiModelProperty(value = "分值")
    private java.math.BigDecimal score;
    /**
     * 难度
     */
    @ApiModelProperty(value = "难度")
    private java.math.BigDecimal difficulty;
    /**
     * 区分度
     */
    @ApiModelProperty(value = "区分度")
    private java.math.BigDecimal distinguish;
    /**
     * 年级均分
     */
    @ApiModelProperty(value = "年级均分")
    private java.math.BigDecimal gradeAverage;
    /**
     * 年级得分率
     */
    @ApiModelProperty(value = "年级得分率")
    private java.math.BigDecimal gradeRate;
    /**
     * 均分
     */
    @ApiModelProperty(value = "均分")
    private java.math.BigDecimal average;
    /**
     * 得分率
     */
    @ApiModelProperty(value = "得分率")
    private java.math.BigDecimal rate;
    /**
     * 排名
     */
    @ApiModelProperty(value = "排名")
    private Integer rank;
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
    public QueryWrapper<PracticeTopic> toQueryWrapper() {
        QueryWrapper<PracticeTopic> wrapper = Wrappers.<PracticeTopic>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.in(this.getClazzIds() != null && this.getClazzIds().size() > 0, "clazz_id", this.getClazzIds());
        wrapper.eq(this.getPracticeId() != null, "practice_id", this.getPracticeId());
        wrapper.eq(this.getClazzId() != null, "clazz_id", this.getClazzId());
        if (this.getTopicName() != null) {
            if (this.getTopicName().startsWith("%") && this.getTopicName().endsWith("%")) {
                wrapper.like("topic_name", this.getTopicName().substring(1, this.getTopicName().length() - 1));
            } else if (this.getTopicName().startsWith("%") && !this.getTopicName().endsWith("%")) {
                wrapper.likeLeft("topic_name", this.getTopicName().substring(1));
            } else if (this.getTopicName().endsWith("%")) {
                wrapper.likeRight("topic_name", this.getTopicName().substring(0, this.getTopicName().length() - 1));
            } else {
                wrapper.eq("topic_name", this.getTopicName());
            }
        }
        wrapper.eq(this.getTopicNumber() != null, "topic_number", this.getTopicNumber());
        if (this.getTopicType() != null) {
            if (this.getTopicType().startsWith("%") && this.getTopicType().endsWith("%")) {
                wrapper.like("topic_type", this.getTopicType().substring(1, this.getTopicType().length() - 1));
            } else if (this.getTopicType().startsWith("%") && !this.getTopicType().endsWith("%")) {
                wrapper.likeLeft("topic_type", this.getTopicType().substring(1));
            } else if (this.getTopicType().endsWith("%")) {
                wrapper.likeRight("topic_type", this.getTopicType().substring(0, this.getTopicType().length() - 1));
            } else {
                wrapper.eq("topic_type", this.getTopicType());
            }
        }
        wrapper.eq(this.getScore() != null, "score", this.getScore());
        wrapper.eq(this.getDifficulty() != null, "difficulty", this.getDifficulty());
        wrapper.eq(this.getDistinguish() != null, "distinguish", this.getDistinguish());
        wrapper.eq(this.getGradeAverage() != null, "grade_average", this.getGradeAverage());
        wrapper.eq(this.getGradeRate() != null, "grade_rate", this.getGradeRate());
        wrapper.eq(this.getAverage() != null, "average", this.getAverage());
        wrapper.eq(this.getRate() != null, "rate", this.getRate());
        wrapper.eq(this.getRank() != null, "rank", this.getRank());
        wrapper.eq(this.getCreateTime() != null, "create_time", this.getCreateTime());
        wrapper.ge(this.getCreateTimeFrom() != null, "create_time", this.getCreateTimeFrom());
        wrapper.lt(this.getCreateTimeTo() != null, "create_time", this.getCreateTimeTo());
        wrapper.eq(this.getUpdateTime() != null, "update_time", this.getUpdateTime());
        wrapper.ge(this.getUpdateTimeFrom() != null, "update_time", this.getUpdateTimeFrom());
        wrapper.lt(this.getUpdateTimeTo() != null, "update_time", this.getUpdateTimeTo());
        return wrapper;
    }

}
