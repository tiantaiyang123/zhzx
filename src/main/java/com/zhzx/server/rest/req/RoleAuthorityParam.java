/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：角色权限对应表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest.req;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;
import com.zhzx.server.domain.RoleAuthority;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "角色权限对应表参数", description = "")
public class RoleAuthorityParam implements Serializable {
    /**
     * ID系统自动生成
     */
    @ApiModelProperty(value = "ID系统自动生成")
    private Long id;
    /**
     * ID系统自动生成 IN值List
     */
    @ApiModelProperty(value = "ID系统自动生成 IN值List")
    private List<Long> idList;
    /**
     * sys_role.id
     */
    @ApiModelProperty(value = "sys_role.id")
    private Long roleId;
    /**
     * sys_authority.id
     */
    @ApiModelProperty(value = "sys_authority.id")
    private Long authorityId;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<RoleAuthority> toQueryWrapper() {
        QueryWrapper<RoleAuthority> wrapper = Wrappers.<RoleAuthority>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getRoleId() != null, "role_id", this.getRoleId());
        wrapper.eq(this.getAuthorityId() != null, "authority_id", this.getAuthorityId());
        return wrapper;
    }

}
