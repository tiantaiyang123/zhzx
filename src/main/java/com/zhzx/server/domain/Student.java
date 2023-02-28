/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：学生表
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
import com.zhzx.server.enums.StudentTypeEnum;
import com.zhzx.server.enums.GenderEnum;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`sys_student`")
@ApiModel(value = "Student", description = "学生表")
public class Student extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * 学号
     */
    @TableField(value = "student_number")
    @ApiModelProperty(value = "学号", required = true)
    private String studentNumber;
    /**
     * 身份证号
     */
    @TableField(value = "id_number")
    @ApiModelProperty(value = "身份证号", required = true)
    private String idNumber;
    /**
     * 编号
     */
    @TableField(value = "order_number")
    @ApiModelProperty(value = "编号", required = true)
    private String orderNumber;
    /**
     * 一卡通号
     */
    @TableField(value = "card_number")
    @ApiModelProperty(value = "一卡通号", required = true)
    private String cardNumber;
    /**
     * 姓名
     */
    @TableField(value = "name")
    @ApiModelProperty(value = "姓名", required = true)
    private String name;
    /**
     * 学生类型
     */
    @TableField(value = "student_type")
    @ApiModelProperty(value = "学生类型", required = true)
    private StudentTypeEnum studentType;
    /**
     * 性别
     */
    @TableField(value = "gender")
    @ApiModelProperty(value = "性别", required = true)
    private GenderEnum gender;
    /**
     * 民族
     */
    @TableField(value = "nationality")
    @ApiModelProperty(value = "民族", required = true)
    private String nationality;
    /**
     * 入学方式
     */
    @TableField(value = "admission_way")
    @ApiModelProperty(value = "入学方式", required = true)
    private String admissionWay;
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
    public Student setDefault() {
        if (this.getStudentType() == null) {
            this.setStudentType(StudentTypeEnum.DAY);
        }
        if (this.getGender() == null) {
            this.setGender(GenderEnum.M);
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
        if (this.getStudentNumber() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "学号不能为空！");
            return false;
        }
        if (this.getIdNumber() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "身份证号不能为空！");
            return false;
        }
        if (this.getOrderNumber() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "编号不能为空！");
            return false;
        }
        if (this.getCardNumber() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "一卡通号不能为空！");
            return false;
        }
        if (this.getName() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "姓名不能为空！");
            return false;
        }
        if (this.getStudentType() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "学生类型不能为空！");
            return false;
        }
        if (this.getGender() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "性别不能为空！");
            return false;
        }
        if (this.getNationality() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "民族不能为空！");
            return false;
        }
        if (this.getAdmissionWay() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "入学方式不能为空！");
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
