/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：手机app角色应用范围表
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
@TableName("`sys_application_role_app`")
@ApiModel(value = "ApplicationRoleApp", description = "手机app角色应用范围表")
public class ApplicationRoleApp extends BaseDomain {
    /**
     * Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "Id", required = true)
    private Long id;
    /**
     * 角色Id
     */
    @TableField(value = "role_id")
    @ApiModelProperty(value = "角色Id", required = true)
    private Long roleId;
    /**
     * 手机应用Id
     */
    @TableField(value = "application_app_id")
    @ApiModelProperty(value = "手机应用Id", required = true)
    private Long applicationAppId;

    /**
     * 设置默认值
     */
    public ApplicationRoleApp setDefault() {
        if (this.getRoleId() == null) {
            this.setRoleId(0L);
        }
        if (this.getApplicationAppId() == null) {
            this.setApplicationAppId(0L);
        }
        return this;
    }

    /**
     * 数据验证
     */
    public Boolean validate(Boolean throwException) {
        if (this.getRoleId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "角色Id不能为空！");
            return false;
        }
        if (this.getApplicationAppId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "手机应用Id不能为空！");
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
