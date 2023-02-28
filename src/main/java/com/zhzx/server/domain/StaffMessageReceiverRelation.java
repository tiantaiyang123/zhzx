/**
* 项目：中华中学管理平台
* 模型分组：系统管理
* 模型名称：老师微信发送人关联表
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
import com.zhzx.server.enums.ReceiverEnum;
import com.zhzx.server.domain.Staff;
import com.zhzx.server.domain.Student;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`sys_staff_message_receiver_relation`")
@ApiModel(value = "StaffMessageReceiverRelation", description = "老师微信发送人关联表")
public class StaffMessageReceiverRelation extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * sys_staff_message.id
     */
    @TableField(value = "staff_message_id")
    @ApiModelProperty(value = "sys_staff_message.id", required = true)
    private Long staffMessageId;
    /**
     * 职工id
     */
    @TableField(value = "staff_id")
    @ApiModelProperty(value = "职工id")
    private Long staffId;
    /**
     * 职工id
     */
    @ApiModelProperty(value = "职工id")
    @TableField(exist = false)
    private Staff staff;
    /**
     * 学生id
     */
    @TableField(value = "student_id")
    @ApiModelProperty(value = "学生id")
    private Long studentId;
    /**
     * 学生id
     */
    @ApiModelProperty(value = "学生id")
    @TableField(exist = false)
    private Student student;
    /**
     * 接收人id,学生或者职工id
     */
    @TableField(value = "receiver_id")
    @ApiModelProperty(value = "接收人id,学生或者职工id", required = true)
    private Long receiverId;
    /**
     * 类型
     */
    @TableField(value = "receiver_type")
    @ApiModelProperty(value = "类型", required = true)
    private ReceiverEnum receiverType;
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
    public StaffMessageReceiverRelation setDefault() {
        if (this.getReceiverType() == null) {
            this.setReceiverType(ReceiverEnum.STUDENT);
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
        if (this.getStaffMessageId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "staff_message_id不能为空！");
            return false;
        }
        if (this.getReceiverId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "receiver_id不能为空！");
            return false;
        }
        if (this.getReceiverType() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "receiver_type不能为空！");
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
