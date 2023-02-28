/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：宿舍学生表
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
import com.zhzx.server.domain.Dormitory;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`sys_student_dormitory`")
@ApiModel(value = "StudentDormitory", description = "宿舍学生表")
public class StudentDormitory extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * 学生id
     */
    @TableField(value = "student_id")
    @ApiModelProperty(value = "学生id", required = true)
    private Long studentId;
    /**
     * 宿舍id
     */
    @TableField(value = "dormitory_id")
    @ApiModelProperty(value = "宿舍id", required = true)
    private Long dormitoryId;
    /**
     * 宿舍id
     */
    @ApiModelProperty(value = "宿舍id")
    @TableField(exist = false)
    private Dormitory dormitory;
    /**
     * 宿舍床位
     */
    @TableField(value = "bed")
    @ApiModelProperty(value = "宿舍床位", required = true)
    private String bed;
    /**
     * 是否启用
     */
    @TableField(value = "is_default")
    @ApiModelProperty(value = "是否启用", required = true)
    private YesNoEnum isDefault;
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
    public StudentDormitory setDefault() {
        if (this.getIsDefault() == null) {
            this.setIsDefault(YesNoEnum.YES);
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
        if (this.getStudentId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "学生id不能为空！");
            return false;
        }
        if (this.getDormitoryId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "宿舍id不能为空！");
            return false;
        }
        if (this.getBed() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "宿舍床位不能为空！");
            return false;
        }
        if (this.getIsDefault() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "是否启用不能为空！");
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
