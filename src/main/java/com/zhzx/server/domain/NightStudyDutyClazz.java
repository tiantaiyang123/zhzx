/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：晚自习行政值班班级情况表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.rest.res.ApiCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`day_night_study_duty_clazz`")
@ApiModel(value = "NightStudyDutyClazz", description = "晚自习行政值班班级情况表")
public class NightStudyDutyClazz extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * 晚自习行政值班ID day_night_study_duty.id
     */
    @TableField(value = "night_study_duty_id")
    @ApiModelProperty(value = "晚自习行政值班ID day_night_study_duty.id", required = true)
    private Long nightStudyDutyId;
    /**
     * 班级ID sys_clazz.id
     */
    @TableField(value = "clazz_id")
    @ApiModelProperty(value = "班级ID sys_clazz.id", required = true)
    private Long clazzId;
    /**
     * 值班老师
     */
    @TableField(value = "teacher")
    @ApiModelProperty(value = "值班老师", required = true)
    private String teacher;
    /**
     * 应到学生数
     */
    @TableField(value = "should_student_count")
    @ApiModelProperty(value = "应到学生数", required = true)
    private Integer shouldStudentCount;
    /**
     * 实到学生数
     */
    @TableField(value = "actual_student_count")
    @ApiModelProperty(value = "实到学生数", required = true)
    private Integer actualStudentCount;
    /**
     * 得分
     */
    @TableField(value = "score")
    @ApiModelProperty(value = "得分", required = true)
    private Integer score;
    /**
     * 值班情况YES正常NO缺席
     */
    @TableField(value = "duty_situation")
    @ApiModelProperty(value = "值班情况", required = true)
    private YesNoEnum dutySituation;

    @TableField(exist = false)
    private String clazzName;

    @TableField(exist = false)
    private String gradeName;

    @TableField(exist = false)
    private Long gradeId;

    @TableField(exist = false)
    private Integer allStudentCount;

    /**
     * 设置默认值
     */
    public NightStudyDutyClazz setDefault() {
        if (this.getShouldStudentCount() == null) {
            this.setShouldStudentCount(0);
        }
        if (this.getActualStudentCount() == null) {
            this.setActualStudentCount(0);
        }
        if (this.getScore() == null) {
            this.setScore(95);
        }
        if (this.getDutySituation() == null) {
            this.setDutySituation(YesNoEnum.YES);
        }
        return this;
    }

    /**
     * 数据验证
     */
    public Boolean validate(Boolean throwException) {
        if (this.getNightStudyDutyId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "晚自习行政值班ID day_night_study_duty.id不能为空！");
            return false;
        }
        if (this.getClazzId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "班级ID sys_clazz.id不能为空！");
            return false;
        }
        if (this.getTeacher() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "值班老师不能为空！");
            return false;
        }
        if (this.getShouldStudentCount() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "应到学生数不能为空！");
            return false;
        }
        if (this.getActualStudentCount() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "实到学生数不能为空！");
            return false;
        }
        if (this.getScore() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "得分不能为空！");
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
