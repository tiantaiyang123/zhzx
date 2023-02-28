/**
 * 项目：中华中学管理平台
 * 模型分组：成绩管理
 * 模型名称：考试结果表
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
import com.zhzx.server.domain.ExamResult;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "考试结果表参数", description = "")
public class ExamResultParam implements Serializable {
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
     * 原始分总分
     */
    @ApiModelProperty(value = "原始分总分")
    private java.math.BigDecimal totalScore;
    /**
     * 原始分赋分
     */
    @ApiModelProperty(value = "原始分赋分")
    private java.math.BigDecimal totalWeightedScore;
    /**
     * 语文成绩
     */
    @ApiModelProperty(value = "语文成绩")
    private java.math.BigDecimal chineseScore;
    /**
     * 数学成绩
     */
    @ApiModelProperty(value = "数学成绩")
    private java.math.BigDecimal mathScore;
    /**
     * 英语成绩
     */
    @ApiModelProperty(value = "英语成绩")
    private java.math.BigDecimal englishScore;
    /**
     * 物理成绩
     */
    @ApiModelProperty(value = "物理成绩")
    private java.math.BigDecimal physicsScore;
    /**
     * 化学原始分
     */
    @ApiModelProperty(value = "化学原始分")
    private java.math.BigDecimal chemistryScore;
    /**
     * 化学赋分
     */
    @ApiModelProperty(value = "化学赋分")
    private java.math.BigDecimal chemistryWeightedScore;
    /**
     * 生物原始分
     */
    @ApiModelProperty(value = "生物原始分")
    private java.math.BigDecimal biologyScore;
    /**
     * 生物赋分
     */
    @ApiModelProperty(value = "生物赋分")
    private java.math.BigDecimal biologyWeightedScore;
    /**
     * 历史成绩
     */
    @ApiModelProperty(value = "历史成绩")
    private java.math.BigDecimal historyScore;
    /**
     * 政治原始分
     */
    @ApiModelProperty(value = "政治原始分")
    private java.math.BigDecimal politicsScore;
    /**
     * 政治赋分
     */
    @ApiModelProperty(value = "政治赋分")
    private java.math.BigDecimal politicsWeightedScore;
    /**
     * 地理原始分
     */
    @ApiModelProperty(value = "地理原始分")
    private java.math.BigDecimal geographyScore;
    /**
     * 地理赋分
     */
    @ApiModelProperty(value = "地理赋分")
    private java.math.BigDecimal geographyWeightedScore;
    /**
     * 班级排名
     */
    @ApiModelProperty(value = "班级排名")
    private Integer clazzRank;
    /**
     * 年级排名
     */
    @ApiModelProperty(value = "年级排名")
    private Integer gradeRank;
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
    public QueryWrapper<ExamResult> toQueryWrapper() {
        QueryWrapper<ExamResult> wrapper = Wrappers.<ExamResult>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getExamId() != null, "exam_id", this.getExamId());
        wrapper.eq(this.getStudentId() != null, "student_id", this.getStudentId());
        wrapper.eq(this.getClazzId() != null, "clazz_id", this.getClazzId());
        wrapper.eq(this.getTotalScore() != null, "total_score", this.getTotalScore());
        wrapper.eq(this.getTotalWeightedScore() != null, "total_weighted_score", this.getTotalWeightedScore());
        wrapper.eq(this.getChineseScore() != null, "chinese_score", this.getChineseScore());
        wrapper.eq(this.getMathScore() != null, "math_score", this.getMathScore());
        wrapper.eq(this.getEnglishScore() != null, "english_score", this.getEnglishScore());
        wrapper.eq(this.getPhysicsScore() != null, "physics_score", this.getPhysicsScore());
        wrapper.eq(this.getChemistryScore() != null, "chemistry_score", this.getChemistryScore());
        wrapper.eq(this.getChemistryWeightedScore() != null, "chemistry_weighted_score", this.getChemistryWeightedScore());
        wrapper.eq(this.getBiologyScore() != null, "biology_score", this.getBiologyScore());
        wrapper.eq(this.getBiologyWeightedScore() != null, "biology_weighted_score", this.getBiologyWeightedScore());
        wrapper.eq(this.getHistoryScore() != null, "history_score", this.getHistoryScore());
        wrapper.eq(this.getPoliticsScore() != null, "politics_score", this.getPoliticsScore());
        wrapper.eq(this.getPoliticsWeightedScore() != null, "politics_weighted_score", this.getPoliticsWeightedScore());
        wrapper.eq(this.getGeographyScore() != null, "geography_score", this.getGeographyScore());
        wrapper.eq(this.getGeographyWeightedScore() != null, "geography_weighted_score", this.getGeographyWeightedScore());
        wrapper.eq(this.getClazzRank() != null, "clazz_rank", this.getClazzRank());
        wrapper.eq(this.getGradeRank() != null, "grade_rank", this.getGradeRank());
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
