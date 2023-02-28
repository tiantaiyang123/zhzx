/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：职能部门表(用于一日常规意见与建议)
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
import com.zhzx.server.domain.FunctionDepartment;
import com.zhzx.server.domain.User;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`sys_function_department`")
@ApiModel(value = "FunctionDepartment", description = "职能部门表(用于一日常规意见与建议)")
public class FunctionDepartment extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * 名称
     */
    @TableField(value = "name")
    @ApiModelProperty(value = "名称", required = true)
    private String name;
    /**
     * 序号
     */
    @TableField(value = "sort_order")
    @ApiModelProperty(value = "序号", required = true)
    private Long sortOrder;
    /**
     * 父级ID sys_function_department.id
     */
    @TableField(value = "parent_id")
    @ApiModelProperty(value = "父级ID sys_function_department.id", required = true)
    private Long parentId;
    /**
     * 父级ID sys_function_department.id
     */
    @ApiModelProperty(value = "父级ID sys_function_department.id")
    @TableField(exist = false)
    private FunctionDepartment parent;
    /**
     * 负责人ID sys_user.id
     */
    @TableField(value = "principal_id")
    @ApiModelProperty(value = "负责人ID sys_user.id", required = true)
    private Long principalId;
    /**
     * 负责人ID sys_user.id
     */
    @ApiModelProperty(value = "负责人ID sys_user.id")
    @TableField(exist = false)
    private User principal;


    @ApiModelProperty(value = "负责人ID sys_user.id")
    @TableField(exist = false)
    private List<FunctionDepartment> children;

    /**
     * 设置默认值
     */
    public FunctionDepartment setDefault() {
        if (this.getSortOrder() == null) {
            this.setSortOrder(0L);
        }
        if (this.getParentId() == null) {
            this.setParentId(0L);
        }
        if (this.getPrincipalId() == null) {
            this.setPrincipalId(0L);
        }
        return this;
    }

    /**
     * 数据验证
     */
    public Boolean validate(Boolean throwException) {
        if (this.getName() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "名称不能为空！");
            return false;
        }
        if (this.getSortOrder() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "序号不能为空！");
            return false;
        }
        Long sortOrderMax = Long.valueOf("99999");
        if (this.getSortOrder() >= sortOrderMax) {
            if (throwException) throw new ApiCode.ApiException(-1, "序号不能大于或等于99999！");
            return false;
        }
        if (this.getParentId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "父级ID sys_function_department.id不能为空！");
            return false;
        }
        if (this.getPrincipalId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "负责人ID sys_user.id不能为空！");
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
