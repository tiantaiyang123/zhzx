/**
 * 项目：中华中学管理平台
 * 模型分组：一日常规管理
 * 模型名称：教师值班代班表
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
import com.zhzx.server.enums.YesNoEnum;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`day_teacher_duty_substitute`")
@ApiModel(value = "TeacherDutySubstitute", description = "教师值班代班表")
public class TeacherDutySubstitute extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * 教师值班ID day_teacher_duty.id
     */
    @TableField(value = "teacher_duty_id")
    @ApiModelProperty(value = "教师值班ID day_teacher_duty.id", required = true)
    private Long teacherDutyId;
    /**
     * 原值班教师ID sys_staff.id
     */
    @TableField(value = "teacher_old_id")
    @ApiModelProperty(value = "原值班教师ID sys_staff.id", required = true)
    private Long teacherOldId;
    /**
     * 代班教师ID sys_staff.id
     */
    @TableField(value = "teacher_id")
    @ApiModelProperty(value = "代班教师ID sys_staff.id", required = true)
    private Long teacherId;
    /**
     * 是否同意 初始值为 null
     */
    @TableField(value = "is_agree")
    @ApiModelProperty(value = "是否同意 初始值为 null")
    private YesNoEnum isAgree;

    /**
     * 设置默认值
     */
    public TeacherDutySubstitute setDefault() {
        if (this.getTeacherOldId() == null) {
            this.setTeacherOldId(0L);
        }
        return this;
    }

    /**
     * 数据验证
     */
    public Boolean validate(Boolean throwException) {
        if (this.getTeacherDutyId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "教师值班ID day_teacher_duty.id不能为空！");
            return false;
        }
        if (this.getTeacherOldId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "原值班教师ID sys_staff.id不能为空！");
            return false;
        }
        if (this.getTeacherId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "代班教师ID sys_staff.id不能为空！");
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
