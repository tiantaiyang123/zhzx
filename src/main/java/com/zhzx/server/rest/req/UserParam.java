/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：用户表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest.req;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhzx.server.domain.User;
import com.zhzx.server.enums.YesNoEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "用户表参数", description = "")
public class UserParam implements Serializable {
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
     * ROLEID IN值List
     */
    @ApiModelProperty(value = "ROLEID IN值List")
    private List<Long> roleIdList;
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;
    /**
     * 登录编号
     */
    @ApiModelProperty(value = "登录编号")
    private String loginNumber;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;
    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String realName;
    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String avatar;
    /**
     * 教职工ID sys_staff.id
     */
    @ApiModelProperty(value = "教职工ID sys_staff.id")
    private Long staffId;
    /**
     * 学生ID sys_student.id
     */
    @ApiModelProperty(value = "学生ID sys_student.id")
    private Long studentId;
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
    public QueryWrapper<User> toQueryWrapper() {
        QueryWrapper<User> wrapper = Wrappers.<User>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        if (this.getRoleIdList() != null && this.getRoleIdList().size() > 0) {
            wrapper.inSql(
                    "id",
                    "select user_id from sys_user_role where role_id in" + this.getRoleIdList().stream().map(String::valueOf).collect(Collectors.joining(",", " (", ") ")) + "group by user_id having count(1) >= " + this.getRoleIdList().size());
        }
        if (this.getUsername() != null) {
            if (this.getUsername().startsWith("%") && this.getUsername().endsWith("%")) {
                wrapper.like("username", this.getUsername().substring(1, this.getUsername().length() - 1));
            } else if (this.getUsername().startsWith("%") && !this.getUsername().endsWith("%")) {
                wrapper.likeLeft("username", this.getUsername().substring(1));
            } else if (this.getUsername().endsWith("%")) {
                wrapper.likeRight("username", this.getUsername().substring(0, this.getUsername().length() - 1));
            } else {
                wrapper.eq("username", this.getUsername());
            }
        }
        if (this.getLoginNumber() != null) {
            if (this.getLoginNumber().startsWith("%") && this.getLoginNumber().endsWith("%")) {
                wrapper.like("login_number", this.getLoginNumber().substring(1, this.getLoginNumber().length() - 1));
            } else if (this.getLoginNumber().startsWith("%") && !this.getLoginNumber().endsWith("%")) {
                wrapper.likeLeft("login_number", this.getLoginNumber().substring(1));
            } else if (this.getLoginNumber().endsWith("%")) {
                wrapper.likeRight("login_number", this.getLoginNumber().substring(0, this.getLoginNumber().length() - 1));
            } else {
                wrapper.eq("login_number", this.getLoginNumber());
            }
        }
        if (this.getPassword() != null) {
            if (this.getPassword().startsWith("%") && this.getPassword().endsWith("%")) {
                wrapper.like("password", this.getPassword().substring(1, this.getPassword().length() - 1));
            } else if (this.getPassword().startsWith("%") && !this.getPassword().endsWith("%")) {
                wrapper.likeLeft("password", this.getPassword().substring(1));
            } else if (this.getPassword().endsWith("%")) {
                wrapper.likeRight("password", this.getPassword().substring(0, this.getPassword().length() - 1));
            } else {
                wrapper.eq("password", this.getPassword());
            }
        }
        if (this.getRealName() != null) {
            if (this.getRealName().startsWith("%") && this.getRealName().endsWith("%")) {
                wrapper.like("real_name", this.getRealName().substring(1, this.getRealName().length() - 1));
            } else if (this.getRealName().startsWith("%") && !this.getRealName().endsWith("%")) {
                wrapper.likeLeft("real_name", this.getRealName().substring(1));
            } else if (this.getRealName().endsWith("%")) {
                wrapper.likeRight("real_name", this.getRealName().substring(0, this.getRealName().length() - 1));
            } else {
                wrapper.eq("real_name", this.getRealName());
            }
        }
        if (this.getAvatar() != null) {
            if (this.getAvatar().startsWith("%") && this.getAvatar().endsWith("%")) {
                wrapper.like("avatar", this.getAvatar().substring(1, this.getAvatar().length() - 1));
            } else if (this.getAvatar().startsWith("%") && !this.getAvatar().endsWith("%")) {
                wrapper.likeLeft("avatar", this.getAvatar().substring(1));
            } else if (this.getAvatar().endsWith("%")) {
                wrapper.likeRight("avatar", this.getAvatar().substring(0, this.getAvatar().length() - 1));
            } else {
                wrapper.eq("avatar", this.getAvatar());
            }
        }
        wrapper.eq(this.getStaffId() != null, "staff_id", this.getStaffId());
        wrapper.eq(this.getStudentId() != null, "student_id", this.getStudentId());
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
