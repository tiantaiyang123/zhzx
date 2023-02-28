/**
* 项目：中华中学管理平台
* 模型分组：成绩管理
* 模型名称：考试赋分表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zhzx.server.enums.ExamNaturalSettingEnum;
import com.zhzx.server.rest.res.ApiCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`dat_exam_goal_natural`")
@ApiModel(value = "ExamGoalNatural", description = "考试赋分表")
public class ExamGoalNatural extends BaseDomain {
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
     * dat_exam.id
     */
    @ApiModelProperty(value = "dat_exam.id")
    @TableField(exist = false)
    private Exam exam;
    /**
     * 学科id
     */
    @TableField(value = "subject_id")
    @ApiModelProperty(value = "学科id", required = true)
    private Long subjectId;
    /**
     * 年级ID sys_grade.id
     */
    @ApiModelProperty(value = "年级ID sys_grade.id")
    @TableField(exist = false)
    private Subject subject;
    /**
     * 模板名称
     */
    @TableField(value = "name")
    @ApiModelProperty(value = "模板名称", required = true)
    private String name;
    /**
     * 赋分类型
     */
    @TableField(value = "setting_type")
    @ApiModelProperty(value = "赋分类型", required = true)
    private ExamNaturalSettingEnum settingType;
    /**
     * 学业等级A等比例
     */
    @TableField(value = "academy_ratio_a")
    @ApiModelProperty(value = "学业等级A等比例", required = true)
    private Long academyRatioA;
    /**
     * 学业等级B等比例
     */
    @TableField(value = "academy_ratio_b")
    @ApiModelProperty(value = "学业等级B等比例", required = true)
    private Long academyRatioB;
    /**
     * 学业等级C等比例
     */
    @TableField(value = "academy_ratio_c")
    @ApiModelProperty(value = "学业等级C等比例", required = true)
    private Long academyRatioC;
    /**
     * 学业等级D等比例
     */
    @TableField(value = "academy_ratio_d")
    @ApiModelProperty(value = "学业等级D等比例", required = true)
    private Long academyRatioD;
    /**
     * 学业等级E等比例
     */
    @TableField(value = "academy_ratio_e")
    @ApiModelProperty(value = "学业等级E等比例", required = true)
    private Long academyRatioE;
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
    public ExamGoalNatural setDefault() {
        if (this.getAcademyRatioA() == null) {
            this.setAcademyRatioA(0L);
        }
        if (this.getAcademyRatioB() == null) {
            this.setAcademyRatioB(0L);
        }
        if (this.getAcademyRatioC() == null) {
            this.setAcademyRatioC(0L);
        }
        if (this.getAcademyRatioD() == null) {
            this.setAcademyRatioD(0L);
        }
        if (this.getAcademyRatioE() == null) {
            this.setAcademyRatioE(0L);
        }
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
            if (throwException) throw new ApiCode.ApiException(-1, "exam_id不能为空！");
            return false;
        }
        if (this.getSubjectId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "subject_id不能为空！");
            return false;
        }
        if (this.getName() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "name不能为空！");
            return false;
        }
        if (this.getSettingType() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "setting_type不能为空！");
            return false;
        }
        if (this.getAcademyRatioA() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "academy_ratio_a不能为空！");
            return false;
        }
        if (this.getAcademyRatioB() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "academy_ratio_b不能为空！");
            return false;
        }
        if (this.getAcademyRatioC() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "academy_ratio_c不能为空！");
            return false;
        }
        if (this.getAcademyRatioD() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "academy_ratio_d不能为空！");
            return false;
        }
        if (this.getAcademyRatioE() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "academy_ratio_e不能为空！");
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
