package com.zhzx.server.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.zhzx.server.domain.Clazz;
import com.zhzx.server.domain.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.shiro.subject.Subject;

import java.util.List;

/**
 * @Author: 王志斌
 * @Date: 2021/12/29 上午10:25
 */
@Data
public class UserVo{

    /**
     * 用户信息
     */
    @ApiModelProperty(value = "用户信息")
    @TableField(exist = false)
    private User UserInfo;
    /**
     * 权限列表
     */
    @ApiModelProperty(value = "权限列表")
    @TableField(exist = false)
    private List<AuthorityVo> authorityList;

//    /**
//     * 班级列表
//     */
//    @ApiModelProperty(value = "班级列表")
//    @TableField(exist = false)
//    private List<Clazz> clazzList;

    /**
     * 学科列表
     */
    @ApiModelProperty(value = "学科列表")
    @TableField(exist = false)
    private List<Subject> subjectList;
}
