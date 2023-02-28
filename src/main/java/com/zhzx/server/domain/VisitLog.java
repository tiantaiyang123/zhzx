/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：访问日志表表
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

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`sys_visit_log`")
@ApiModel(value = "VisitLog", description = "访问日志表表")
public class VisitLog extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * 项目名称
     */
    @TableField(value = "project_name")
    @ApiModelProperty(value = "项目名称", required = true)
    private String projectName;
    /**
     * 次数
     */
    @TableField(value = "num")
    @ApiModelProperty(value = "次数", required = true)
    private Long num;
    /**
     * 备注
     */
    @TableField(value = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 操作人ID
     */
    @TableField(value = "editor_id")
    @ApiModelProperty(value = "操作人ID", required = true)
    private Long editorId;
    /**
     * 操作人
     */
    @TableField(value = "editor_name")
    @ApiModelProperty(value = "操作人")
    private String editorName;
    /**
     * 最后一次访问时间时间
     */
    @TableField(value = "visit_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "最后一次访问时间时间", required = true)
    private java.util.Date visitTime;
    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间", required = true)
    private java.util.Date createTime;
    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间", required = true)
    private java.util.Date updateTime;

    /**
     * 设置默认值
     */
    public VisitLog setDefault() {
        if (this.getNum() == null) {
            this.setNum(1L);
        }
        if (this.getEditorId() == null) {
            this.setEditorId(1L);
        }
        if (this.getEditorName() == null) {
            this.setEditorName("admin");
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
        if (this.getProjectName() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "项目名称不能为空！");
            return false;
        }
        if (this.getNum() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "次数不能为空！");
            return false;
        }
        if (this.getEditorId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "操作人ID不能为空！");
            return false;
        }
        if (this.getVisitTime() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "最后一次访问时间时间不能为空！");
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
