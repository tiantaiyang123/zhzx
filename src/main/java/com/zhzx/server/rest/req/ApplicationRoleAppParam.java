/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：手机app角色应用范围表
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
import com.zhzx.server.domain.ApplicationRoleApp;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "手机app角色应用范围表参数", description = "")
public class ApplicationRoleAppParam implements Serializable {
    /**
     * Id
     */
    @ApiModelProperty(value = "Id")
    private Long id;
    /**
     * Id IN值List
     */
    @ApiModelProperty(value = "Id IN值List")
    private List<Long> idList;
    /**
     * 角色Id
     */
    @ApiModelProperty(value = "角色Id")
    private Long roleId;
    /**
     * 手机应用Id
     */
    @ApiModelProperty(value = "手机应用Id")
    private Long applicationAppId;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<ApplicationRoleApp> toQueryWrapper() {
        QueryWrapper<ApplicationRoleApp> wrapper = Wrappers.<ApplicationRoleApp>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getRoleId() != null, "role_id", this.getRoleId());
        wrapper.eq(this.getApplicationAppId() != null, "application_app_id", this.getApplicationAppId());
        return wrapper;
    }

}
