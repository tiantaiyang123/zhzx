/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：部门表
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
@TableName("`sys_department`")
@ApiModel(value = "Department", description = "部门表")
public class Department extends BaseDomain {
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
     * 部门负责人 多选 用[/]分割 sys_staff.name
     */
    @TableField(value = "principal")
    @ApiModelProperty(value = "部门负责人 多选 用[/]分割 sys_staff.name", required = true)
    private String principal;
    /**
     * 父级ID sys_department.id
     */
    @TableField(value = "parent_id")
    @ApiModelProperty(value = "父级ID sys_department.id", required = true)
    private Long parentId;
    /**
     * 序号
     */
    @TableField(value = "sort_order")
    @ApiModelProperty(value = "序号", required = true)
    private Long sortOrder;
    /**
     * 
     */
    @TableField(value = "create_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "", required = true)
    private java.util.Date createTime;
    /**
     * 
     */
    @TableField(value = "update_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "", required = true)
    private java.util.Date updateTime;

    /**
     * 设置默认值
     */
    public Department setDefault() {
        if (this.getParentId() == null) {
            this.setParentId(0L);
        }
        if (this.getSortOrder() == null) {
            this.setSortOrder(0L);
        }
        if (this.getCreateTime() == null) {
            this.setCreateTime(new java.util.Date());
        }
        if (this.getUpdateTime() == null) {
            this.setUpdateTime(new java.util.Date());
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
        if (this.getPrincipal() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "部门负责人 多选 用[/]分割 sys_staff.name不能为空！");
            return false;
        }
        if (this.getParentId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "父级ID sys_department.id不能为空！");
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
        return true;
    }

    /**
     * 数据验证
     */
    public Boolean validate() {
        return this.validate(false);
    }

}
