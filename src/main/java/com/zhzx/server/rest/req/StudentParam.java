/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：学生表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest.req;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;
import com.zhzx.server.enums.StudentTypeEnum;
import com.zhzx.server.enums.GenderEnum;
import com.zhzx.server.domain.Student;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "学生表参数", description = "")
public class StudentParam implements Serializable {
    /**
     * ID系统自动生成
     */
    @ApiModelProperty(value = "ID系统自动生成")
    private Long id;
    /**
     * ID系统自动生成 IN值List
     */
    @ApiModelProperty(value = "ID系统自动生成 IN值List")
    private List<Long> idList;
    /**
     * 学号
     */
    @ApiModelProperty(value = "学号")
    private String studentNumber;
    /**
     * 身份证号
     */
    @ApiModelProperty(value = "身份证号")
    private String idNumber;
    /**
     * 编号
     */
    @ApiModelProperty(value = "编号")
    private String orderNumber;
    /**
     * 一卡通号
     */
    @ApiModelProperty(value = "一卡通号")
    private String cardNumber;
    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String name;
    /**
     * 学生类型
     */
    @ApiModelProperty(value = "学生类型")
    private StudentTypeEnum studentType;
    /**
     * 学生类型 IN值List
     */
    @ApiModelProperty(value = "学生类型 IN值List")
    private List<String> studentTypeList;
    /**
     * 性别
     */
    @ApiModelProperty(value = "性别")
    private GenderEnum gender;
    /**
     * 性别 IN值List
     */
    @ApiModelProperty(value = "性别 IN值List")
    private List<String> genderList;
    /**
     * 民族
     */
    @ApiModelProperty(value = "民族")
    private String nationality;
    /**
     * 入学方式
     */
    @ApiModelProperty(value = "入学方式")
    private String admissionWay;
    /**
     * 
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "")
    private java.util.Date createTime;
    /**
     *  下限值(大于等于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = " 下限值(大于等于)")
    private java.util.Date createTimeFrom;
    /**
     *  上限值(小于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = " 上限值(小于)")
    private java.util.Date createTimeTo;
    /**
     * 
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "")
    private java.util.Date updateTime;
    /**
     *  下限值(大于等于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = " 下限值(大于等于)")
    private java.util.Date updateTimeFrom;
    /**
     *  上限值(小于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = " 上限值(小于)")
    private java.util.Date updateTimeTo;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<Student> toQueryWrapper() {
        QueryWrapper<Student> wrapper = Wrappers.<Student>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        if (this.getStudentNumber() != null) {
            if (this.getStudentNumber().startsWith("%") && this.getStudentNumber().endsWith("%")) {
                wrapper.like("student_number", this.getStudentNumber().substring(1, this.getStudentNumber().length() - 1));
            } else if (this.getStudentNumber().startsWith("%") && !this.getStudentNumber().endsWith("%")) {
                wrapper.likeLeft("student_number", this.getStudentNumber().substring(1));
            } else if (this.getStudentNumber().endsWith("%")) {
                wrapper.likeRight("student_number", this.getStudentNumber().substring(0, this.getStudentNumber().length() - 1));
            } else {
                wrapper.eq("student_number", this.getStudentNumber());
            }
        }
        if (this.getIdNumber() != null) {
            if (this.getIdNumber().startsWith("%") && this.getIdNumber().endsWith("%")) {
                wrapper.like("id_number", this.getIdNumber().substring(1, this.getIdNumber().length() - 1));
            } else if (this.getIdNumber().startsWith("%") && !this.getIdNumber().endsWith("%")) {
                wrapper.likeLeft("id_number", this.getIdNumber().substring(1));
            } else if (this.getIdNumber().endsWith("%")) {
                wrapper.likeRight("id_number", this.getIdNumber().substring(0, this.getIdNumber().length() - 1));
            } else {
                wrapper.eq("id_number", this.getIdNumber());
            }
        }
        if (this.getOrderNumber() != null) {
            if (this.getOrderNumber().startsWith("%") && this.getOrderNumber().endsWith("%")) {
                wrapper.like("order_number", this.getOrderNumber().substring(1, this.getOrderNumber().length() - 1));
            } else if (this.getOrderNumber().startsWith("%") && !this.getOrderNumber().endsWith("%")) {
                wrapper.likeLeft("order_number", this.getOrderNumber().substring(1));
            } else if (this.getOrderNumber().endsWith("%")) {
                wrapper.likeRight("order_number", this.getOrderNumber().substring(0, this.getOrderNumber().length() - 1));
            } else {
                wrapper.eq("order_number", this.getOrderNumber());
            }
        }
        if (this.getCardNumber() != null) {
            if (this.getCardNumber().startsWith("%") && this.getCardNumber().endsWith("%")) {
                wrapper.like("card_number", this.getCardNumber().substring(1, this.getCardNumber().length() - 1));
            } else if (this.getCardNumber().startsWith("%") && !this.getCardNumber().endsWith("%")) {
                wrapper.likeLeft("card_number", this.getCardNumber().substring(1));
            } else if (this.getCardNumber().endsWith("%")) {
                wrapper.likeRight("card_number", this.getCardNumber().substring(0, this.getCardNumber().length() - 1));
            } else {
                wrapper.eq("card_number", this.getCardNumber());
            }
        }
        if (this.getName() != null) {
            if (this.getName().startsWith("%") && this.getName().endsWith("%")) {
                wrapper.like("name", this.getName().substring(1, this.getName().length() - 1));
            } else if (this.getName().startsWith("%") && !this.getName().endsWith("%")) {
                wrapper.likeLeft("name", this.getName().substring(1));
            } else if (this.getName().endsWith("%")) {
                wrapper.likeRight("name", this.getName().substring(0, this.getName().length() - 1));
            } else {
                wrapper.eq("name", this.getName());
            }
        }
        wrapper.eq(this.getStudentType() != null, "student_type", this.getStudentType());
        wrapper.in(this.getStudentTypeList() != null && this.getStudentTypeList().size() > 0, "student_type", this.getStudentTypeList());
        wrapper.eq(this.getGender() != null, "gender", this.getGender());
        wrapper.in(this.getGenderList() != null && this.getGenderList().size() > 0, "gender", this.getGenderList());
        if (this.getNationality() != null) {
            if (this.getNationality().startsWith("%") && this.getNationality().endsWith("%")) {
                wrapper.like("nationality", this.getNationality().substring(1, this.getNationality().length() - 1));
            } else if (this.getNationality().startsWith("%") && !this.getNationality().endsWith("%")) {
                wrapper.likeLeft("nationality", this.getNationality().substring(1));
            } else if (this.getNationality().endsWith("%")) {
                wrapper.likeRight("nationality", this.getNationality().substring(0, this.getNationality().length() - 1));
            } else {
                wrapper.eq("nationality", this.getNationality());
            }
        }
        if (this.getAdmissionWay() != null) {
            if (this.getAdmissionWay().startsWith("%") && this.getAdmissionWay().endsWith("%")) {
                wrapper.like("admission_way", this.getAdmissionWay().substring(1, this.getAdmissionWay().length() - 1));
            } else if (this.getAdmissionWay().startsWith("%") && !this.getAdmissionWay().endsWith("%")) {
                wrapper.likeLeft("admission_way", this.getAdmissionWay().substring(1));
            } else if (this.getAdmissionWay().endsWith("%")) {
                wrapper.likeRight("admission_way", this.getAdmissionWay().substring(0, this.getAdmissionWay().length() - 1));
            } else {
                wrapper.eq("admission_way", this.getAdmissionWay());
            }
        }
        wrapper.eq(this.getCreateTime() != null, "create_time", this.getCreateTime());
        wrapper.ge(this.getCreateTimeFrom() != null, "create_time", this.getCreateTimeFrom());
        wrapper.lt(this.getCreateTimeTo() != null, "create_time", this.getCreateTimeTo());
        wrapper.eq(this.getUpdateTime() != null, "update_time", this.getUpdateTime());
        wrapper.ge(this.getUpdateTimeFrom() != null, "update_time", this.getUpdateTimeFrom());
        wrapper.lt(this.getUpdateTimeTo() != null, "update_time", this.getUpdateTimeTo());
        return wrapper;
    }

}
