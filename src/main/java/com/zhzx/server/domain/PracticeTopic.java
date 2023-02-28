/**
* 项目：中华中学管理平台
* 模型分组：成绩管理
* 模型名称：小题得分情况表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.domain;

import java.io.Serializable;

import com.zhzx.server.rest.res.ApiCode;
import lombok.*;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zhzx.server.domain.Practice;
import com.zhzx.server.domain.Clazz;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`dat_practice_topic`")
@ApiModel(value = "PracticeTopic", description = "小题得分情况表")
public class PracticeTopic extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * 练习ID dat_practice.id
     */
    @TableField(value = "practice_id")
    @ApiModelProperty(value = "练习ID dat_practice.id", required = true)
    private Long practiceId;
    /**
     * 练习ID dat_practice.id
     */
    @ApiModelProperty(value = "练习ID dat_practice.id")
    @TableField(exist = false)
    private Practice practice;
    /**
     * 班级ID sys_clazz.id
     */
    @TableField(value = "clazz_id")
    @ApiModelProperty(value = "班级ID sys_clazz.id", required = true)
    private Long clazzId;
    /**
     * 班级ID sys_clazz.id
     */
    @ApiModelProperty(value = "班级ID sys_clazz.id")
    @TableField(exist = false)
    private Clazz clazz;
    /**
     * 题目
     */
    @TableField(value = "topic_name")
    @ApiModelProperty(value = "题目")
    private String topicName;
    /**
     * 题号
     */
    @TableField(value = "topic_number")
    @ApiModelProperty(value = "题号", required = true)
    private Integer topicNumber;
    /**
     * 题型
     */
    @TableField(value = "topic_type")
    @ApiModelProperty(value = "题型", required = true)
    private String topicType;
    /**
     * 分值
     */
    @TableField(value = "score")
    @ApiModelProperty(value = "分值", required = true)
    private java.math.BigDecimal score;
    /**
     * 难度
     */
    @TableField(value = "difficulty")
    @ApiModelProperty(value = "难度", required = true)
    private java.math.BigDecimal difficulty;
    /**
     * 区分度
     */
    @TableField(value = "distinguish")
    @ApiModelProperty(value = "区分度", required = true)
    private java.math.BigDecimal distinguish;
    /**
     * 年级均分
     */
    @TableField(value = "grade_average")
    @ApiModelProperty(value = "年级均分", required = true)
    private java.math.BigDecimal gradeAverage;
    /**
     * 年级得分率
     */
    @TableField(value = "grade_rate")
    @ApiModelProperty(value = "年级得分率", required = true)
    private java.math.BigDecimal gradeRate;
    /**
     * 均分
     */
    @TableField(value = "average")
    @ApiModelProperty(value = "均分", required = true)
    private java.math.BigDecimal average;
    /**
     * 得分率
     */
    @TableField(value = "rate")
    @ApiModelProperty(value = "得分率", required = true)
    private java.math.BigDecimal rate;
    /**
     * 排名
     */
    @TableField(value = "rank")
    @ApiModelProperty(value = "排名", required = true)
    private Integer rank;
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

    /**
     * 设置默认值
     */
    public PracticeTopic setDefault() {
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
        if (this.getPracticeId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "practice_id不能为空！");
            return false;
        }
        if (this.getClazzId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "clazz_id不能为空！");
            return false;
        }
        if (this.getTopicNumber() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "topic_number不能为空！");
            return false;
        }
        if (this.getTopicType() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "topic_type不能为空！");
            return false;
        }
        if (this.getScore() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "score不能为空！");
            return false;
        }
        if (this.getDifficulty() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "difficulty不能为空！");
            return false;
        }
        if (this.getDistinguish() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "distinguish不能为空！");
            return false;
        }
        if (this.getGradeAverage() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "grade_average不能为空！");
            return false;
        }
        if (this.getGradeRate() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "grade_rate不能为空！");
            return false;
        }
        if (this.getAverage() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "average不能为空！");
            return false;
        }
        if (this.getRate() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "rate不能为空！");
            return false;
        }
        if (this.getRank() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "rank不能为空！");
            return false;
        }
        if (this.getCreateTime() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "create_time不能为空！");
            return false;
        }
        if (this.getUpdateTime() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "update_time不能为空！");
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
