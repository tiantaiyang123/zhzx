/**
* 项目：中华中学管理平台
* 模型分组：系统管理
* 模型名称：老师分类关联表
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
import com.zhzx.server.domain.Staff;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`sys_staff_category_relation`")
@ApiModel(value = "StaffCategoryRelation", description = "老师分类关联表")
public class StaffCategoryRelation extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * 分类id
     */
    @TableField(value = "staff_category_id")
    @ApiModelProperty(value = "分类id", required = true)
    private Long staffCategoryId;
    /**
     * teahcerID
     */
    @TableField(value = "staff_id")
    @ApiModelProperty(value = "teahcerID", required = true)
    private Long staffId;
    /**
     * teahcerID
     */
    @ApiModelProperty(value = "teahcerID")
    @TableField(exist = false)
    private Staff staff;
    /**
     * 名称
     */
    @TableField(value = "parent_category")
    @ApiModelProperty(value = "名称", required = true)
    private String parentCategory;

    /**
     * 设置默认值
     */
    public StaffCategoryRelation setDefault() {
        return this;
    }

    /**
     * 数据验证
     */
    public Boolean validate(Boolean throwException) {
        if (this.getStaffCategoryId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "staff_category_id不能为空！");
            return false;
        }
        if (this.getStaffId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "staff_id不能为空！");
            return false;
        }
        if (this.getParentCategory() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "parent_category不能为空！");
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
