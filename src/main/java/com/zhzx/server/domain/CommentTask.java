/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：意见与建议处理表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.domain;

import java.io.Serializable;

import com.zhzx.server.rest.res.ApiCode;
import lombok.*;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`day_comment_task`")
@ApiModel(value = "CommentTask", description = "意见与建议处理表")
public class CommentTask extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * 意见与建议推送ID day_comment_process.id
     */
    @TableField(value = "comment_process_id")
    @ApiModelProperty(value = "意见与建议推送ID day_comment_process.id", required = true)
    private Long commentProcessId;
    /**
     * 序号
     */
    @TableField(value = "sort_order")
    @ApiModelProperty(value = "序号", required = true)
    private Integer sortOrder;
    /**
     * 职能部门ID sys_function_department.id
     */
    @TableField(value = "function_department_id")
    @ApiModelProperty(value = "职能部门ID sys_function_department.id", required = true)
    private Long functionDepartmentId;
    /**
     * 职能部门负责人
     */
    @TableField(value = "principal")
    @ApiModelProperty(value = "职能部门负责人", required = true)
    private String principal;
    /**
     * 职能部门负责人
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "职能部门负责人")
    private Long userId;
    /**
     * 处理结果
     */
    @TableField(value = "content")
    @ApiModelProperty(value = "处理结果")
    private String content;
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
    public CommentTask setDefault() {
        if (this.getSortOrder() == null) {
            this.setSortOrder(0);
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
        if (this.getCommentProcessId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "意见与建议推送ID day_comment_process.id不能为空！");
            return false;
        }
        if (this.getSortOrder() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "序号不能为空！");
            return false;
        }
        if (this.getFunctionDepartmentId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "职能部门ID sys_function_department.id不能为空！");
            return false;
        }
        if (this.getPrincipal() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "职能部门负责人不能为空！");
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
