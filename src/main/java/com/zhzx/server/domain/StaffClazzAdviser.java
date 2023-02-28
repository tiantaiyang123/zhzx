/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：班主任表
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
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.domain.Staff;
import com.zhzx.server.domain.Clazz;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`sys_staff_clazz_adviser`")
@ApiModel(value = "StaffClazzAdviser", description = "班主任表")
public class StaffClazzAdviser extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * 教师ID sys_staff.id
     */
    @TableField(value = "staff_id")
    @ApiModelProperty(value = "教师ID sys_staff.id", required = true)
    private Long staffId;
    /**
     * 教师ID sys_staff.id
     */
    @ApiModelProperty(value = "教师ID sys_staff.id")
    @TableField(exist = false)
    private Staff staff;
    /**
     * 班级ID sys_clazz.id
     */
    @TableField(value = "clazz_id")
    @ApiModelProperty(value = "班级ID sys_clazz.id", required = true)
    private Long clazzId;
    /**
     * 班级ID sys_clazz.id
     */
    @ApiModelProperty(value = "班级ID sys_clazz.id")
    @TableField(exist = false)
    private Clazz clazz;
    /**
     * 是否当值
     */
    @TableField(value = "is_current")
    @ApiModelProperty(value = "是否当值", required = true)
    private YesNoEnum isCurrent;

    /**
     * 设置默认值
     */
    public StaffClazzAdviser setDefault() {
        return this;
    }

    /**
     * 数据验证
     */
    public Boolean validate(Boolean throwException) {
        if (this.getStaffId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "教师ID sys_staff.id不能为空！");
            return false;
        }
        if (this.getClazzId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "班级ID sys_clazz.id不能为空！");
            return false;
        }
        if (this.getIsCurrent() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "是否当值不能为空！");
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
