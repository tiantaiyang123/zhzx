/**
* 项目：中华中学管理平台
* 模型分组：成绩管理
* 模型名称：考试赋分表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.rest.req;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhzx.server.domain.ExamGoalNatural;
import com.zhzx.server.enums.ExamNaturalSettingEnum;
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
@ApiModel(value = "考试赋分表参数", description = "")
public class ExamGoalNaturalParam implements Serializable {
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
     * 学科id
     */
    @ApiModelProperty(value = "学科id")
    private Long subjectId;
    /**
     * 模板名称
     */
    @ApiModelProperty(value = "模板名称")
    private String name;
    /**
     * 赋分类型
     */
    @ApiModelProperty(value = "赋分类型")
    private ExamNaturalSettingEnum settingType;
    /**
     * 学业等级A等比例
     */
    @ApiModelProperty(value = "学业等级A等比例")
    private Long academyRatioA;
    /**
     * 学业等级B等比例
     */
    @ApiModelProperty(value = "学业等级B等比例")
    private Long academyRatioB;
    /**
     * 学业等级C等比例
     */
    @ApiModelProperty(value = "学业等级C等比例")
    private Long academyRatioC;
    /**
     * 学业等级D等比例
     */
    @ApiModelProperty(value = "学业等级D等比例")
    private Long academyRatioD;
    /**
     * 学业等级E等比例
     */
    @ApiModelProperty(value = "学业等级E等比例")
    private Long academyRatioE;
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
    public QueryWrapper<ExamGoalNatural> toQueryWrapper() {
        QueryWrapper<ExamGoalNatural> wrapper = Wrappers.<ExamGoalNatural>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getExamId() != null, "exam_id", this.getExamId());
        wrapper.eq(this.getSubjectId() != null, "subject_id", this.getSubjectId());
        if (this.getName() != null) {
            if (this.getName().startsWith("%") && this.getName().endsWith("%")) {
                wrapper.like("name", this.getName().substring(1, this.getName().length() - 1));
            } else if (this.getName().startsWith("%") && !this.getName().endsWith("%")) {
                wrapper.likeLeft("name", this.getName().substring(1));
            } else if (this.getName().endsWith("%")) {
                wrapper.likeRight("name", this.getName().substring(0, this.getName().length() - 1));
            } else {
                wrapper.eq("name", this.getName());
            }
        }
        wrapper.eq(this.getSettingType() != null, "setting_type", this.getSettingType());
        wrapper.eq(this.getAcademyRatioA() != null, "academy_ratio_a", this.getAcademyRatioA());
        wrapper.eq(this.getAcademyRatioB() != null, "academy_ratio_b", this.getAcademyRatioB());
        wrapper.eq(this.getAcademyRatioC() != null, "academy_ratio_c", this.getAcademyRatioC());
        wrapper.eq(this.getAcademyRatioD() != null, "academy_ratio_d", this.getAcademyRatioD());
        wrapper.eq(this.getAcademyRatioE() != null, "academy_ratio_e", this.getAcademyRatioE());
        wrapper.eq(this.getCreateTime() != null, "create_time", this.getCreateTime());
        wrapper.ge(this.getCreateTimeFrom() != null, "create_time", this.getCreateTimeFrom());
        wrapper.lt(this.getCreateTimeTo() != null, "create_time", this.getCreateTimeTo());
        wrapper.eq(this.getUpdateTime() != null, "update_time", this.getUpdateTime());
        wrapper.ge(this.getUpdateTimeFrom() != null, "update_time", this.getUpdateTimeFrom());
        wrapper.lt(this.getUpdateTimeTo() != null, "update_time", this.getUpdateTimeTo());
        return wrapper;
    }

}
