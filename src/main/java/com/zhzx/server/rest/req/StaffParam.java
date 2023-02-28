/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：教职工表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest.req;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhzx.server.enums.*;
import lombok.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;
import com.zhzx.server.domain.Staff;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "教职工表参数", description = "")
public class StaffParam implements Serializable {
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
     * 部门 多选 用[,]分割 sys_department.name
     */
    @ApiModelProperty(value = "部门 多选 用[,]分割 sys_department.name")
    private String department;
    /**
     * 职工号
     */
    @ApiModelProperty(value = "职工号")
    private String employeeNumber;
    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String name;
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
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String phone;
    /**
     * 学历
     */
    @ApiModelProperty(value = "学历")
    private String education;
    /**
     * 职称
     */
    @ApiModelProperty(value = "职称")
    private String title;
    /**
     * 一卡通号
     */
    @ApiModelProperty(value = "一卡通号")
    private String cardNumber;
    /**
     * 身份证号
     */
    @ApiModelProperty(value = "身份证号")
    private String idNumber;
    /**
     * 编制情况
     */
    @ApiModelProperty(value = "编制情况")
    private CompilationSituationEnum compilationSituation;
    /**
     * 编制情况 IN值List
     */
    @ApiModelProperty(value = "编制情况 IN值List")
    private List<String> compilationSituationList;
    /**
     * 人事情况
     */
    @ApiModelProperty(value = "人事情况")
    private PersonnelSituationEnum personnelSituation;
    /**
     * 人事情况 IN值List
     */
    @ApiModelProperty(value = "人事情况 IN值List")
    private List<String> personnelSituationList;
    /**
     * 职能
     */
    @ApiModelProperty(value = "职能")
    private FunctionEnum function;
    /**
     * 职能 IN值List
     */
    @ApiModelProperty(value = "职能 IN值List")
    private List<String> functionList;
    /**
     * 是否删除
     */
    @ApiModelProperty(value = "是否删除")
    private YesNoEnum isDelete;
    /**
     * 是否删除 IN值List
     */
    @ApiModelProperty(value = "是否删除 IN值List")
    private List<String> isDeleteList;
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
    public QueryWrapper<Staff> toQueryWrapper() {
        QueryWrapper<Staff> wrapper = Wrappers.<Staff>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        if (this.getDepartment() != null) {
            if (this.getDepartment().startsWith("%") && this.getDepartment().endsWith("%")) {
                wrapper.like("department", this.getDepartment().substring(1, this.getDepartment().length() - 1));
            } else if (this.getDepartment().startsWith("%") && !this.getDepartment().endsWith("%")) {
                wrapper.likeLeft("department", this.getDepartment().substring(1));
            } else if (this.getDepartment().endsWith("%")) {
                wrapper.likeRight("department", this.getDepartment().substring(0, this.getDepartment().length() - 1));
            } else {
                wrapper.eq("department", this.getDepartment());
            }
        }
        if (this.getEmployeeNumber() != null) {
            if (this.getEmployeeNumber().startsWith("%") && this.getEmployeeNumber().endsWith("%")) {
                wrapper.like("employee_number", this.getEmployeeNumber().substring(1, this.getEmployeeNumber().length() - 1));
            } else if (this.getEmployeeNumber().startsWith("%") && !this.getEmployeeNumber().endsWith("%")) {
                wrapper.likeLeft("employee_number", this.getEmployeeNumber().substring(1));
            } else if (this.getEmployeeNumber().endsWith("%")) {
                wrapper.likeRight("employee_number", this.getEmployeeNumber().substring(0, this.getEmployeeNumber().length() - 1));
            } else {
                wrapper.eq("employee_number", this.getEmployeeNumber());
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
        wrapper.eq(this.getGender() != null, "gender", this.getGender());
        wrapper.in(this.getGenderList() != null && this.getGenderList().size() > 0, "gender", this.getGenderList());
        if (this.getPhone() != null) {
            if (this.getPhone().startsWith("%") && this.getPhone().endsWith("%")) {
                wrapper.like("phone", this.getPhone().substring(1, this.getPhone().length() - 1));
            } else if (this.getPhone().startsWith("%") && !this.getPhone().endsWith("%")) {
                wrapper.likeLeft("phone", this.getPhone().substring(1));
            } else if (this.getPhone().endsWith("%")) {
                wrapper.likeRight("phone", this.getPhone().substring(0, this.getPhone().length() - 1));
            } else {
                wrapper.eq("phone", this.getPhone());
            }
        }
        if (this.getEducation() != null) {
            if (this.getEducation().startsWith("%") && this.getEducation().endsWith("%")) {
                wrapper.like("education", this.getEducation().substring(1, this.getEducation().length() - 1));
            } else if (this.getEducation().startsWith("%") && !this.getEducation().endsWith("%")) {
                wrapper.likeLeft("education", this.getEducation().substring(1));
            } else if (this.getEducation().endsWith("%")) {
                wrapper.likeRight("education", this.getEducation().substring(0, this.getEducation().length() - 1));
            } else {
                wrapper.eq("education", this.getEducation());
            }
        }
        if (this.getTitle() != null) {
            if (this.getTitle().startsWith("%") && this.getTitle().endsWith("%")) {
                wrapper.like("title", this.getTitle().substring(1, this.getTitle().length() - 1));
            } else if (this.getTitle().startsWith("%") && !this.getTitle().endsWith("%")) {
                wrapper.likeLeft("title", this.getTitle().substring(1));
            } else if (this.getTitle().endsWith("%")) {
                wrapper.likeRight("title", this.getTitle().substring(0, this.getTitle().length() - 1));
            } else {
                wrapper.eq("title", this.getTitle());
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
        wrapper.eq(this.getCompilationSituation() != null, "compilation_situation", this.getCompilationSituation());
        wrapper.in(this.getCompilationSituationList() != null && this.getCompilationSituationList().size() > 0, "compilation_situation", this.getCompilationSituationList());
        wrapper.eq(this.getPersonnelSituation() != null, "personnel_situation", this.getPersonnelSituation());
        wrapper.in(this.getPersonnelSituationList() != null && this.getPersonnelSituationList().size() > 0, "personnel_situation", this.getPersonnelSituationList());
        wrapper.eq(this.getFunction() != null, "function", this.getFunction());
        wrapper.in(this.getFunctionList() != null && this.getFunctionList().size() > 0, "function", this.getFunctionList());
        wrapper.eq(this.getIsDelete() != null, "is_delete", this.getIsDelete());
        wrapper.in(this.getIsDeleteList() != null && this.getIsDeleteList().size() > 0, "is_delete", this.getIsDeleteList());
        wrapper.eq(this.getCreateTime() != null, "create_time", this.getCreateTime());
        wrapper.ge(this.getCreateTimeFrom() != null, "create_time", this.getCreateTimeFrom());
        wrapper.lt(this.getCreateTimeTo() != null, "create_time", this.getCreateTimeTo());
        wrapper.eq(this.getUpdateTime() != null, "update_time", this.getUpdateTime());
        wrapper.ge(this.getUpdateTimeFrom() != null, "update_time", this.getUpdateTimeFrom());
        wrapper.lt(this.getUpdateTimeTo() != null, "update_time", this.getUpdateTimeTo());
        return wrapper;
    }

}
