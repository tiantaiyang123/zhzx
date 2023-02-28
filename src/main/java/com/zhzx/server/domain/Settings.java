/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：系统配置表
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
@TableName("`sys_settings`")
@ApiModel(value = "Settings", description = "系统配置表")
public class Settings extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * 编码
     */
    @TableField(value = "code")
    @ApiModelProperty(value = "编码", required = true)
    private String code;
    /**
     * 说明
     */
    @TableField(value = "remark")
    @ApiModelProperty(value = "说明", required = true)
    private String remark;
    /**
     * 参数json
     */
    @TableField(value = "params", select = false)
    @ApiModelProperty(value = "参数json")
    private String params;

    /**
     * 设置默认值
     */
    public Settings setDefault() {
        return this;
    }

    /**
     * 数据验证
     */
    public Boolean validate(Boolean throwException) {
        if (this.getCode() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "编码不能为空！");
            return false;
        }
        if (this.getRemark() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "说明不能为空！");
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
