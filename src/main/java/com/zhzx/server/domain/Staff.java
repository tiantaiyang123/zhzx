/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：教职工表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zhzx.server.enums.*;
import com.zhzx.server.rest.res.ApiCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`sys_staff`")
@ApiModel(value = "Staff", description = "教职工表")
public class Staff extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * ID系统自动生成
     */
    @ApiModelProperty(value = "ID系统自动生成")
    @TableField(exist = false)
    private List<StaffSubject> staffSubjectList;
    /**
     * ID系统自动生成
     */
    @ApiModelProperty(value = "ID系统自动生成")
    @TableField(exist = false)
    private List<StaffClazzAdviser> staffClazzAdviserList;
    /**
     * ID系统自动生成
     */
    @ApiModelProperty(value = "ID系统自动生成")
    @TableField(exist = false)
    private List<StaffLessonTeacher> staffLessonTeacherList;
    /**
     * ID系统自动生成
     */
    @ApiModelProperty(value = "ID系统自动生成")
    @TableField(exist = false)
    private List<StaffResearchLeader> staffResearchLeaderList;
    /**
     * ID系统自动生成
     */
    @ApiModelProperty(value = "ID系统自动生成")
    @TableField(exist = false)
    private List<StaffLessonLeader> staffLessonLeaderList;
    /**
     * ID系统自动生成
     */
    @ApiModelProperty(value = "ID系统自动生成")
    @TableField(exist = false)
    private List<StaffGradeLeader> staffGradeLeaderList;
    /**
     * 部门 多选 用[,]分割 sys_department.name
     */
    @TableField(value = "department")
    @ApiModelProperty(value = "部门 多选 用[,]分割 sys_department.name", required = true)
    private String department;
    /**
     * 职工号
     */
    @TableField(value = "employee_number")
    @ApiModelProperty(value = "职工号", required = true)
    private String employeeNumber;
    /**
     * 姓名
     */
    @TableField(value = "name")
    @ApiModelProperty(value = "姓名", required = true)
    private String name;
    /**
     * 性别
     */
    @TableField(value = "gender")
    @ApiModelProperty(value = "性别", required = true)
    private GenderEnum gender;

    /**
     * 性别
     */
    @TableField(value = "wx_username")
    @ApiModelProperty(value = "企业微信账号", required = true)
    private String wxUserName;

    /**
     * 手机号
     */
    @TableField(value = "phone")
    @ApiModelProperty(value = "手机号", required = true)
    private String phone;
    /**
     * 学历
     */
    @TableField(value = "education")
    @ApiModelProperty(value = "学历", required = true)
    private String education;
    /**
     * 职称
     */
    @TableField(value = "title")
    @ApiModelProperty(value = "职称", required = true)
    private String title;
    /**
     * 一卡通号
     */
    @TableField(value = "card_number")
    @ApiModelProperty(value = "一卡通号", required = true)
    private String cardNumber;
    /**
     * 身份证号
     */
    @TableField(value = "id_number")
    @ApiModelProperty(value = "身份证号", required = true)
    private String idNumber;
    /**
     * 编制情况
     */
    @TableField(value = "compilation_situation")
    @ApiModelProperty(value = "编制情况", required = true)
    private CompilationSituationEnum compilationSituation;
    /**
     * 人事情况
     */
    @TableField(value = "personnel_situation")
    @ApiModelProperty(value = "人事情况", required = true)
    private PersonnelSituationEnum personnelSituation;
    /**
     * 是否删除
     */
    @TableField(value = "is_delete")
    @ApiModelProperty(value = "是否删除", required = true)
    private YesNoEnum isDelete;
    /**
     * 职能
     */
    @TableField(value = "`function`")
    @ApiModelProperty(value = "职能", required = true)
    private FunctionEnum function;
    /**
     * 荣誉
     */
    @TableField(value = "honor")
    @ApiModelProperty(value = "荣誉")
    private String honor;
    /**
     * 职能
     */
    @TableField(value = "wx_username")
    @ApiModelProperty(value = "微信账号")
    private String wxUsername;
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
     * 是否跨校区
     */
    @ApiModelProperty(value = "是否跨校区")
    @TableField(exist = false)
    private YesNoEnum unionSchoolyard;

    /**
     * 设置默认值
     */
    public Staff setDefault() {
        if (this.getGender() == null) {
            this.setGender(GenderEnum.M);
        }
        if (this.getCompilationSituation() == null) {
            this.setCompilationSituation(CompilationSituationEnum.ORGANIZATION);
        }
        if (this.getPersonnelSituation() == null) {
            this.setPersonnelSituation(PersonnelSituationEnum.TEACHER);
        }
        if (this.getIsDelete() == null) {
            this.setIsDelete(YesNoEnum.NO);
        }
        if (this.getFunction() == null) {
            this.setFunction(FunctionEnum.OTHER);
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
        if (this.getDepartment() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "部门 多选 用[,]分割 sys_department.name不能为空！");
            return false;
        }
        if (this.getEmployeeNumber() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "职工号不能为空！");
            return false;
        }
        if (this.getName() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "姓名不能为空！");
            return false;
        }
        if (this.getGender() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "性别不能为空！");
            return false;
        }
        if (this.getPhone() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "手机号不能为空！");
            return false;
        }
        if (this.getEducation() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "学历不能为空！");
            return false;
        }
        if (this.getTitle() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "职称不能为空！");
            return false;
        }
        if (this.getCardNumber() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "一卡通号不能为空！");
            return false;
        }
        if (this.getIdNumber() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "身份证号不能为空！");
            return false;
        }
        if (this.getCompilationSituation() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "编制情况不能为空！");
            return false;
        }
        if (this.getPersonnelSituation() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "人事情况不能为空！");
            return false;
        }
        if (this.getIsDelete() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "是否删除不能为空！");
            return false;
        }
        if (this.getFunction() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "职能不能为空！");
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
