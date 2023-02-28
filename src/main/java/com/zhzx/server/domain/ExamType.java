/**
* 项目：中华中学管理平台
* 模型分组：成绩管理
* 模型名称：考试分类表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
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
@TableName("`dat_exam_type`")
@ApiModel(value = "ExamType", description = "考试分类表")
public class ExamType extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * 分类名称
     */
    @TableField(value = "name")
    @ApiModelProperty(value = "分类名称", required = true)
    private String name;
    /**
     * 权重
     */
    @TableField(value = "weight")
    @ApiModelProperty(value = "权重", required = true)
    private Integer weight;

    /**
     * 设置默认值
     */
    public ExamType setDefault() {
        if (this.getWeight() == null) {
            this.setWeight(0);
        }
        return this;
    }

    /**
     * 数据验证
     */
    public Boolean validate(Boolean throwException) {
        if (this.getName() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "name不能为空！");
            return false;
        }
        if (this.getWeight() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "weight不能为空！");
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
