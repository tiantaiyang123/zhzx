/**
 * 项目：中华中学管理平台
 * 模型分组：成绩管理
 * 模型名称：副科考试结果表
 *
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
import com.zhzx.server.domain.ExamResultMinor;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "副科考试结果表参数", description = "")
public class ExamResultMinorParam implements Serializable {
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
     * sys_student.id
     */
    @ApiModelProperty(value = "sys_student.id")
    private Long studentId;
    /**
     * sys_clazz.id
     */
    @ApiModelProperty(value = "sys_clazz.id")
    private Long clazzId;
    /**
     * 音乐分数
     */
    @ApiModelProperty(value = "音乐分数")
    private java.math.BigDecimal musicScore;
    /**
     * 音乐等级
     */
    @ApiModelProperty(value = "音乐等级")
    private String musicLevel;
    /**
     * 体育分数
     */
    @ApiModelProperty(value = "体育分数")
    private java.math.BigDecimal sportsScore;
    /**
     * 体育等级
     */
    @ApiModelProperty(value = "体育等级")
    private String sportsLevel;
    /**
     * 美术分数
     */
    @ApiModelProperty(value = "美术分数")
    private java.math.BigDecimal paintingScore;
    /**
     * 美术等级
     */
    @ApiModelProperty(value = "美术等级")
    private String paintingLevel;
    /**
     * 心理健康分数
     */
    @ApiModelProperty(value = "心理健康分数")
    private java.math.BigDecimal psychologyScore;
    /**
     * 心理健康等级
     */
    @ApiModelProperty(value = "心理健康等级")
    private String psychologyLevel;
    /**
     * 通用技术分数
     */
    @ApiModelProperty(value = "通用技术分数")
    private java.math.BigDecimal generalTechnologyScore;
    /**
     * 通用技术等级
     */
    @ApiModelProperty(value = "通用技术等级")
    private String generalTechnologyLevel;
    /**
     * 信息技术分数
     */
    @ApiModelProperty(value = "信息技术分数")
    private java.math.BigDecimal informationTechnologyScore;
    /**
     * 信息技术等级
     */
    @ApiModelProperty(value = "信息技术等级")
    private String informationTechnologyLevel;
    /**
     * 其他成绩信息
     */
    @ApiModelProperty(value = "其他成绩信息")
    private String other;
    /**
     *
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "")
    private java.util.Date createTime;
    /**
     *  下限值(大于等于)
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = " 下限值(大于等于)")
    private java.util.Date createTimeFrom;
    /**
     *  上限值(小于)
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = " 上限值(小于)")
    private java.util.Date createTimeTo;
    /**
     *
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "")
    private java.util.Date updateTime;
    /**
     *  下限值(大于等于)
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = " 下限值(大于等于)")
    private java.util.Date updateTimeFrom;
    /**
     *  上限值(小于)
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = " 上限值(小于)")
    private java.util.Date updateTimeTo;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<ExamResultMinor> toQueryWrapper() {
        QueryWrapper<ExamResultMinor> wrapper = Wrappers.<ExamResultMinor>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getExamId() != null, "exam_id", this.getExamId());
        wrapper.eq(this.getStudentId() != null, "student_id", this.getStudentId());
        wrapper.eq(this.getClazzId() != null, "clazz_id", this.getClazzId());
        wrapper.eq(this.getMusicScore() != null, "music_score", this.getMusicScore());
        wrapper.eq(this.getMusicLevel() != null, "music_level", this.getMusicLevel());
        wrapper.eq(this.getSportsScore() != null, "sports_score", this.getSportsScore());
        wrapper.eq(this.getSportsLevel() != null, "sports_level", this.getSportsLevel());
        wrapper.eq(this.getPaintingScore() != null, "painting_score", this.getPaintingScore());
        wrapper.eq(this.getPaintingLevel() != null, "painting_level", this.getPaintingLevel());
        wrapper.eq(this.getPsychologyScore() != null, "psychology_score", this.getPsychologyScore());
        wrapper.eq(this.getPsychologyLevel() != null, "psychology_level", this.getPsychologyLevel());
        wrapper.eq(this.getGeneralTechnologyScore() != null, "general_technology_score", this.getGeneralTechnologyScore());
        wrapper.eq(this.getGeneralTechnologyLevel() != null, "general_technology_level", this.getGeneralTechnologyLevel());
        wrapper.eq(this.getInformationTechnologyScore() != null, "information_technology_score", this.getInformationTechnologyScore());
        wrapper.eq(this.getInformationTechnologyLevel() != null, "information_technology_level", this.getInformationTechnologyLevel());
        wrapper.eq(this.getCreateTime() != null, "create_time", this.getCreateTime());
        wrapper.ge(this.getCreateTimeFrom() != null, "create_time", this.getCreateTimeFrom());
        wrapper.lt(this.getCreateTimeTo() != null, "create_time", this.getCreateTimeTo());
        wrapper.eq(this.getUpdateTime() != null, "update_time", this.getUpdateTime());
        wrapper.ge(this.getUpdateTimeFrom() != null, "update_time", this.getUpdateTimeFrom());
        wrapper.lt(this.getUpdateTimeTo() != null, "update_time", this.getUpdateTimeTo());
        return wrapper;
    }

}
