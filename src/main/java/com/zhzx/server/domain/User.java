/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：用户表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.vo.SchoolWeek;
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
@TableName("`sys_user`")
@ApiModel(value = "User", description = "用户表")
public class User extends BaseDomain {

    @ApiModelProperty(value = "一卡通ID")
    @TableField(exist = false)
    private Long cardId;

    @ApiModelProperty(value = "登录验证码")
    @TableField(exist = false)
    private String code;

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
    private List<Role> roleList;
    /**
     * 用户名
     */
    @TableField(value = "username")
    @ApiModelProperty(value = "用户名", required = true)
    private String username;
    /**
     * 登录编号
     */
    @TableField(value = "login_number")
    @ApiModelProperty(value = "登录编号", required = true)
    private String loginNumber;
    /**
     * 密码
     */
    @TableField(value = "password")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ApiModelProperty(value = "密码")
    private String password;
    /**
     * 姓名
     */
    @TableField(value = "real_name")
    @ApiModelProperty(value = "姓名")
    private String realName;
    /**
     * 头像
     */
    @TableField(value = "avatar")
    @ApiModelProperty(value = "头像")
    private String avatar;
    /**
     * 教职工ID sys_staff.id
     */
    @TableField(value = "staff_id")
    @ApiModelProperty(value = "教职工ID sys_staff.id")
    private Long staffId;
    /**
     * 教职工ID sys_staff.id
     */
    @ApiModelProperty(value = "教职工ID sys_staff.id")
    @TableField(exist = false)
    private Staff staff;
    /**
     * 学生ID sys_student.id
     */
    @TableField(value = "student_id")
    @ApiModelProperty(value = "学生ID sys_student.id")
    private Long studentId;
    /**
     * 学生ID sys_student.id
     */
    @ApiModelProperty(value = "学生ID sys_student.id")
    @TableField(exist = false)
    private Student student;
    /**
     * 登录错误次数
     */
    @TableField(value = "login_error_cnt")
    @ApiModelProperty(value = "登录错误次数")
    private Integer loginErrorCnt;
    /**
     * 是否删除
     */
    @TableField(value = "is_delete")
    @ApiModelProperty(value = "是否删除", required = true)
    private YesNoEnum isDelete;
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
     * 班级 学生为所属班级, 教师为班主任班级
     */
    @ApiModelProperty(value = "班级 学生为所属班级, 教师为班主任班级")
    @TableField(exist = false)
    private Clazz clazz;

    /**
     * 当前学期
     */
    @ApiModelProperty(value = "当前学期")
    @TableField(exist = false)
    private AcademicYearSemester academicYearSemester;

    /**
     * 当前学期周数列表
     */
    @ApiModelProperty(value = "当前学期周数列表")
    @TableField(exist = false)
    private List<SchoolWeek> schoolWeeks;

    @TableField(exist = false)
    private List<Clazz> clazzList;

    @TableField(exist = false)
    private List<Subject> subjectList;

    @TableField(exist = false)
    private List<Exam> examList;

    /**
     * 设置默认值
     */
    public User setDefault() {
        if (this.getStaffId() == null) {
            this.setStaffId(0L);
        }
        if (this.getStudentId() == null) {
            this.setStudentId(0L);
        }
        if (this.getIsDelete() == null) {
            this.setIsDelete(YesNoEnum.NO);
        }
        if (this.getLoginErrorCnt() == null) {
            this.setLoginErrorCnt(0);
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
        if (this.getUsername() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "用户名不能为空！");
            return false;
        }
        if (this.getLoginNumber() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "登录编号不能为空！");
            return false;
        }
        if (this.getIsDelete() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "是否删除不能为空！");
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
