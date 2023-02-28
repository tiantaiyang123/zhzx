/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：角色权限对应表
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
@TableName("`sys_role_authority`")
@ApiModel(value = "RoleAuthority", description = "角色权限对应表")
public class RoleAuthority extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * sys_role.id
     */
    @TableField(value = "role_id")
    @ApiModelProperty(value = "sys_role.id", required = true)
    private Long roleId;
    /**
     * sys_authority.id
     */
    @TableField(value = "authority_id")
    @ApiModelProperty(value = "sys_authority.id", required = true)
    private Long authorityId;

    /**
     * 设置默认值
     */
    public RoleAuthority setDefault() {
        return this;
    }

    /**
     * 数据验证
     */
    public Boolean validate(Boolean throwException) {
        if (this.getRoleId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "sys_role.id不能为空！");
            return false;
        }
        if (this.getAuthorityId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "sys_authority.id不能为空！");
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
