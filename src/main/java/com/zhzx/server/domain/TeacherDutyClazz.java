/**
 * 项目：中华中学管理平台
 * 模型分组：一日常规管理
 * 模型名称：教师值班班级表
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
import com.zhzx.server.domain.Clazz;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`day_teacher_duty_clazz`")
@ApiModel(value = "TeacherDutyClazz", description = "教师值班班级表")
public class TeacherDutyClazz extends BaseDomain {
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
     * 是否确认
     */
    @TableField(value = "is_comfirm")
    @ApiModelProperty(value = "是否确认", required = true)
    private YesNoEnum isComfirm;
    /**
     * 值班领导是否确认
     */
    @TableField(value = "is_leader_comfirm")
    @ApiModelProperty(value = "值班领导是否确认", required = true)
    private YesNoEnum isLeaderComfirm;

    /**
     * 设置默认值
     */
    public TeacherDutyClazz setDefault() {
        if (this.getIsComfirm() == null) {
            this.setIsComfirm(YesNoEnum.NO);
        }
        if (this.getIsLeaderComfirm() == null) {
            this.setIsLeaderComfirm(YesNoEnum.NO);
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
        if (this.getClazzId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "班级ID sys_clazz.id不能为空！");
            return false;
        }
        if (this.getIsComfirm() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "是否确认不能为空！");
            return false;
        }
        if (this.getIsLeaderComfirm() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "值班领导是否确认不能为空！");
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
