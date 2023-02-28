/**
* 项目：中华中学管理平台
* 模型分组：系统管理
* 模型名称：学生家长表
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
import com.zhzx.server.enums.RelationEnum;
import com.zhzx.server.enums.MessageParentEnum;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`sys_student_parent`")
@ApiModel(value = "StudentParent", description = "学生家长表")
public class StudentParent extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * 学生ID sys_student.id
     */
    @TableField(value = "student_id")
    @ApiModelProperty(value = "学生ID sys_student.id", required = true)
    private Long studentId;
    /**
     * 姓名
     */
    @TableField(value = "name")
    @ApiModelProperty(value = "姓名", required = true)
    private String name;
    /**
     * 手机号
     */
    @TableField(value = "phone")
    @ApiModelProperty(value = "手机号", required = true)
    private String phone;
    /**
     * 关系
     */
    @TableField(value = "relation")
    @ApiModelProperty(value = "关系", required = true)
    private RelationEnum relation;
    /**
     * 微信家长id
     */
    @TableField(value = "wx_parent_id")
    @ApiModelProperty(value = "微信家长id", required = true)
    private String wxParentId;
    /**
     * 是否在微信创建家长联系人发送消息
     */
    @TableField(value = "type")
    @ApiModelProperty(value = "是否在微信创建家长联系人发送消息", required = true)
    private MessageParentEnum type;

    /**
     * 设置默认值
     */
    public StudentParent setDefault() {
        if (this.getRelation() == null) {
            this.setRelation(RelationEnum.F);
        }
        if (this.getType() == null) {
            this.setType(MessageParentEnum.NO_CREATE);
        }
        return this;
    }

    /**
     * 数据验证
     */
    public Boolean validate(Boolean throwException) {
        if (this.getStudentId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "student_id不能为空！");
            return false;
        }
        if (this.getName() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "name不能为空！");
            return false;
        }
        if (this.getPhone() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "phone不能为空！");
            return false;
        }
        if (this.getRelation() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "relation不能为空！");
            return false;
        }
        if (this.getType() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "type不能为空！");
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
