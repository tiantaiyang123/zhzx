/**
* 项目：中华中学管理平台
* 模型分组：系统管理
* 模型名称：老师分类表
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
import com.zhzx.server.enums.YesNoEnum;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`sys_staff_category`")
@ApiModel(value = "StaffCategory", description = "老师分类表")
public class StaffCategory extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * 父类id
     */
    @TableField(value = "parent_id")
    @ApiModelProperty(value = "父类id", required = true)
    private Long parentId;
    /**
     * 名称
     */
    @TableField(value = "name")
    @ApiModelProperty(value = "名称", required = true)
    private String name;
    /**
     * 描述
     */
    @TableField(value = "remark")
    @ApiModelProperty(value = "描述")
    private String remark;
    /**
     * 是否允许创建下级
     */
    @TableField(value = "create_subordinate")
    @ApiModelProperty(value = "是否允许创建下级", required = true)
    private YesNoEnum createSubordinate;
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
    public StaffCategory setDefault() {
        if (this.getCreateSubordinate() == null) {
            this.setCreateSubordinate(YesNoEnum.YES);
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
        if (this.getParentId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "parent_id不能为空！");
            return false;
        }
        if (this.getName() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "name不能为空！");
            return false;
        }
        if (this.getCreateSubordinate() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "create_subordinate不能为空！");
            return false;
        }
        if (this.getCreateTime() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "create_time不能为空！");
            return false;
        }
        if (this.getUpdateTime() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "update_time不能为空！");
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
