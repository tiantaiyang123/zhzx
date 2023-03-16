/**
 * 项目：中华中学管理平台
 * 模型分组：成绩管理
 * 模型名称：副科考试结果表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
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

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`dat_exam_result_minor`")
@ApiModel(value = "ExamResultMinor", description = "副科考试结果表")
public class ExamResultMinor extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * dat_exam.id
     */
    @TableField(value = "exam_id")
    @ApiModelProperty(value = "dat_exam.id", required = true)
    private Long examId;
    /**
     * sys_student.id
     */
    @TableField(value = "student_id")
    @ApiModelProperty(value = "sys_student.id", required = true)
    private Long studentId;
    /**
     * sys_clazz.id
     */
    @TableField(value = "clazz_id")
    @ApiModelProperty(value = "sys_clazz.id", required = true)
    private Long clazzId;
    /**
     * 音乐分数
     */
    @TableField(value = "music_score")
    @ApiModelProperty(value = "音乐分数")
    private java.math.BigDecimal musicScore;
    /**
     * 音乐等级
     */
    @TableField(value = "music_level")
    @ApiModelProperty(value = "音乐等级")
    private String musicLevel;
    /**
     * 体育分数
     */
    @TableField(value = "sports_score")
    @ApiModelProperty(value = "体育分数")
    private java.math.BigDecimal sportsScore;
    /**
     * 体育等级
     */
    @TableField(value = "sports_level")
    @ApiModelProperty(value = "体育等级")
    private String sportsLevel;
    /**
     * 美术分数
     */
    @TableField(value = "painting_score")
    @ApiModelProperty(value = "美术分数")
    private java.math.BigDecimal paintingScore;
    /**
     * 美术等级
     */
    @TableField(value = "painting_level")
    @ApiModelProperty(value = "美术等级")
    private String paintingLevel;
    /**
     * 心理健康分数
     */
    @TableField(value = "psychology_score")
    @ApiModelProperty(value = "心理健康分数")
    private java.math.BigDecimal psychologyScore;
    /**
     * 心理健康等级
     */
    @TableField(value = "psychology_level")
    @ApiModelProperty(value = "心理健康等级")
    private String psychologyLevel;
    /**
     * 通用技术分数
     */
    @TableField(value = "general_technology_score")
    @ApiModelProperty(value = "通用技术分数")
    private java.math.BigDecimal generalTechnologyScore;
    /**
     * 通用技术等级
     */
    @TableField(value = "general_technology_level")
    @ApiModelProperty(value = "通用技术等级")
    private String generalTechnologyLevel;
    /**
     * 信息技术分数
     */
    @TableField(value = "information_technology_score")
    @ApiModelProperty(value = "信息技术分数")
    private java.math.BigDecimal informationTechnologyScore;
    /**
     * 信息技术等级
     */
    @TableField(value = "information_technology_level")
    @ApiModelProperty(value = "信息技术等级")
    private String informationTechnologyLevel;
    /**
     * 其他成绩信息
     */
    @TableField(value = "other")
    @ApiModelProperty(value = "其他成绩信息")
    private String other;
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
    public ExamResultMinor setDefault() {
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
        if (this.getExamId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "dat_exam.id不能为空！");
            return false;
        }
        if (this.getStudentId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "sys_student.id不能为空！");
            return false;
        }
        if (this.getClazzId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "sys_clazz.id不能为空！");
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
