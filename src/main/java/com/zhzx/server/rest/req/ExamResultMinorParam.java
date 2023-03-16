/**
 * 项目：中华中学管理平台
 * 模型分组：成绩管理
 * 模型名称：副科考试结果表
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
    public QueryWrapper<ExamResultMinor> toQueryWrapper() {
        QueryWrapper<ExamResultMinor> wrapper = Wrappers.<ExamResultMinor>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getExamId() != null, "exam_id", this.getExamId());
        wrapper.eq(this.getStudentId() != null, "student_id", this.getStudentId());
        wrapper.eq(this.getClazzId() != null, "clazz_id", this.getClazzId());
        wrapper.eq(this.getMusicScore() != null, "music_score", this.getMusicScore());
        if (this.getMusicLevel() != null) {
            if (this.getMusicLevel().startsWith("%") && this.getMusicLevel().endsWith("%")) {
                wrapper.like("music_level", this.getMusicLevel().substring(1, this.getMusicLevel().length() - 1));
            } else if (this.getMusicLevel().startsWith("%") && !this.getMusicLevel().endsWith("%")) {
                wrapper.likeLeft("music_level", this.getMusicLevel().substring(1));
            } else if (this.getMusicLevel().endsWith("%")) {
                wrapper.likeRight("music_level", this.getMusicLevel().substring(0, this.getMusicLevel().length() - 1));
            } else {
                wrapper.eq("music_level", this.getMusicLevel());
            }
        }
        wrapper.eq(this.getSportsScore() != null, "sports_score", this.getSportsScore());
        if (this.getSportsLevel() != null) {
            if (this.getSportsLevel().startsWith("%") && this.getSportsLevel().endsWith("%")) {
                wrapper.like("sports_level", this.getSportsLevel().substring(1, this.getSportsLevel().length() - 1));
            } else if (this.getSportsLevel().startsWith("%") && !this.getSportsLevel().endsWith("%")) {
                wrapper.likeLeft("sports_level", this.getSportsLevel().substring(1));
            } else if (this.getSportsLevel().endsWith("%")) {
                wrapper.likeRight("sports_level", this.getSportsLevel().substring(0, this.getSportsLevel().length() - 1));
            } else {
                wrapper.eq("sports_level", this.getSportsLevel());
            }
        }
        wrapper.eq(this.getPaintingScore() != null, "painting_score", this.getPaintingScore());
        if (this.getPaintingLevel() != null) {
            if (this.getPaintingLevel().startsWith("%") && this.getPaintingLevel().endsWith("%")) {
                wrapper.like("painting_level", this.getPaintingLevel().substring(1, this.getPaintingLevel().length() - 1));
            } else if (this.getPaintingLevel().startsWith("%") && !this.getPaintingLevel().endsWith("%")) {
                wrapper.likeLeft("painting_level", this.getPaintingLevel().substring(1));
            } else if (this.getPaintingLevel().endsWith("%")) {
                wrapper.likeRight("painting_level", this.getPaintingLevel().substring(0, this.getPaintingLevel().length() - 1));
            } else {
                wrapper.eq("painting_level", this.getPaintingLevel());
            }
        }
        wrapper.eq(this.getPsychologyScore() != null, "psychology_score", this.getPsychologyScore());
        if (this.getPsychologyLevel() != null) {
            if (this.getPsychologyLevel().startsWith("%") && this.getPsychologyLevel().endsWith("%")) {
                wrapper.like("psychology_level", this.getPsychologyLevel().substring(1, this.getPsychologyLevel().length() - 1));
            } else if (this.getPsychologyLevel().startsWith("%") && !this.getPsychologyLevel().endsWith("%")) {
                wrapper.likeLeft("psychology_level", this.getPsychologyLevel().substring(1));
            } else if (this.getPsychologyLevel().endsWith("%")) {
                wrapper.likeRight("psychology_level", this.getPsychologyLevel().substring(0, this.getPsychologyLevel().length() - 1));
            } else {
                wrapper.eq("psychology_level", this.getPsychologyLevel());
            }
        }
        wrapper.eq(this.getGeneralTechnologyScore() != null, "general_technology_score", this.getGeneralTechnologyScore());
        if (this.getGeneralTechnologyLevel() != null) {
            if (this.getGeneralTechnologyLevel().startsWith("%") && this.getGeneralTechnologyLevel().endsWith("%")) {
                wrapper.like("general_technology_level", this.getGeneralTechnologyLevel().substring(1, this.getGeneralTechnologyLevel().length() - 1));
            } else if (this.getGeneralTechnologyLevel().startsWith("%") && !this.getGeneralTechnologyLevel().endsWith("%")) {
                wrapper.likeLeft("general_technology_level", this.getGeneralTechnologyLevel().substring(1));
            } else if (this.getGeneralTechnologyLevel().endsWith("%")) {
                wrapper.likeRight("general_technology_level", this.getGeneralTechnologyLevel().substring(0, this.getGeneralTechnologyLevel().length() - 1));
            } else {
                wrapper.eq("general_technology_level", this.getGeneralTechnologyLevel());
            }
        }
        wrapper.eq(this.getInformationTechnologyScore() != null, "information_technology_score", this.getInformationTechnologyScore());
        if (this.getInformationTechnologyLevel() != null) {
            if (this.getInformationTechnologyLevel().startsWith("%") && this.getInformationTechnologyLevel().endsWith("%")) {
                wrapper.like("information_technology_level", this.getInformationTechnologyLevel().substring(1, this.getInformationTechnologyLevel().length() - 1));
            } else if (this.getInformationTechnologyLevel().startsWith("%") && !this.getInformationTechnologyLevel().endsWith("%")) {
                wrapper.likeLeft("information_technology_level", this.getInformationTechnologyLevel().substring(1));
            } else if (this.getInformationTechnologyLevel().endsWith("%")) {
                wrapper.likeRight("information_technology_level", this.getInformationTechnologyLevel().substring(0, this.getInformationTechnologyLevel().length() - 1));
            } else {
                wrapper.eq("information_technology_level", this.getInformationTechnologyLevel());
            }
        }
        if (this.getOther() != null) {
            if (this.getOther().startsWith("%") && this.getOther().endsWith("%")) {
                wrapper.like("other", this.getOther().substring(1, this.getOther().length() - 1));
            } else if (this.getOther().startsWith("%") && !this.getOther().endsWith("%")) {
                wrapper.likeLeft("other", this.getOther().substring(1));
            } else if (this.getOther().endsWith("%")) {
                wrapper.likeRight("other", this.getOther().substring(0, this.getOther().length() - 1));
            } else {
                wrapper.eq("other", this.getOther());
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
