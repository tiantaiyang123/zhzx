/**
* 项目：中华中学管理平台
* 模型分组：系统管理
* 模型名称：宿舍表
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
@TableName("`sys_dormitory`")
@ApiModel(value = "Dormitory", description = "宿舍表")
public class Dormitory extends BaseDomain {
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
     * 楼栋
     */
    @TableField(value = "building")
    @ApiModelProperty(value = "楼栋", required = true)
    private String building;
    /**
     * 楼层
     */
    @TableField(value = "floor")
    @ApiModelProperty(value = "楼层", required = true)
    private String floor;
    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间", required = true)
    private java.util.Date createTime;
    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间", required = true)
    private java.util.Date updateTime;

    /**
     * 设置默认值
     */
    public Dormitory setDefault() {
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
            if (throwException) throw new ApiCode.ApiException(-1, "name不能为空！");
            return false;
        }
        if (this.getBuilding() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "building不能为空！");
            return false;
        }
        if (this.getFloor() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "floor不能为空！");
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
