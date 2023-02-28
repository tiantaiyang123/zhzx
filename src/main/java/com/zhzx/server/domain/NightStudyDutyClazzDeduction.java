/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：晚自习行政值班班级扣分表
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
@TableName("`day_night_study_duty_clazz_deduction`")
@ApiModel(value = "NightStudyDutyClazzDeduction", description = "晚自习行政值班班级扣分表")
public class NightStudyDutyClazzDeduction extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * 晚自习行政值班班级情况ID day_night_study_duty_clazz.id
     */
    @TableField(value = "night_study_duty_clazz_id")
    @ApiModelProperty(value = "晚自习行政值班班级情况ID day_night_study_duty_clazz.id", required = true)
    private Long nightStudyDutyClazzId;
    /**
     * 情况说明
     */
    @TableField(value = "Description")
    @ApiModelProperty(value = "情况说明", required = true)
    private String Description;

    /**
     * 设置默认值
     */
    public NightStudyDutyClazzDeduction setDefault() {
        return this;
    }

    /**
     * 数据验证
     */
    public Boolean validate(Boolean throwException) {
        if (this.getNightStudyDutyClazzId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "晚自习行政值班班级情况ID day_night_study_duty_clazz.id不能为空！");
            return false;
        }
        if (this.getDescription() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "情况说明不能为空！");
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
