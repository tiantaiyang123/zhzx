/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：晚自习表
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
@TableName("`day_night_study`")
@ApiModel(value = "NightStudy", description = "晚自习表")
public class NightStudy extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * 教师值班班级ID day_teacher_duty_clazz.id
     */
    @TableField(value = "teacher_duty_clazz_id")
    @ApiModelProperty(value = "教师值班班级ID day_teacher_duty_clazz.id", required = true)
    private Long teacherDutyClazzId;
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
     * 设置默认值
     */
    public NightStudy setDefault() {
        return this;
    }

    /**
     * 数据验证
     */
    public Boolean validate(Boolean throwException) {
        if (this.getTeacherDutyClazzId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "教师值班班级ID day_teacher_duty_clazz.id不能为空！");
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
        return true;
    }

    /**
     * 数据验证
     */
    public Boolean validate() {
        return this.validate(false);
    }

}
