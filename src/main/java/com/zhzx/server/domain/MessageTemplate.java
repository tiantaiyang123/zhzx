/**
* 项目：中华中学管理平台
* 模型分组：消息管理
* 模型名称：消息模板表
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

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`mes_message_template`")
@ApiModel(value = "MessageTemplate", description = "消息模板表")
public class MessageTemplate extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * 消息模板名称
     */
    @TableField(value = "name")
    @ApiModelProperty(value = "消息模板名称")
    private String name;
    /**
     * 标题
     */
    @TableField(value = "title")
    @ApiModelProperty(value = "标题", required = true)
    private String title;
    /**
     * 内容
     */
    @TableField(value = "content", select = false)
    @ApiModelProperty(value = "内容", required = true)
    private String content;
    /**
     * 备注
     */
    @TableField(value = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 
     */
    @TableField(value = "created_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "", required = true)
    private java.util.Date createdTime;
    /**
     * 
     */
    @TableField(value = "updated_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "", required = true)
    private java.util.Date updatedTime;

    /**
     * 设置默认值
     */
    public MessageTemplate setDefault() {
        if (this.getCreatedTime() == null) {
            this.setCreatedTime(new java.util.Date());
        }
        if (this.getUpdatedTime() == null) {
            this.setUpdatedTime(new java.util.Date());
        }
        return this;
    }

    /**
     * 数据验证
     */
    public Boolean validate(Boolean throwException) {
        if (this.getTitle() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "title不能为空！");
            return false;
        }
        if (this.getContent() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "content不能为空！");
            return false;
        }
        if (this.getCreatedTime() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "created_time不能为空！");
            return false;
        }
        if (this.getUpdatedTime() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "updated_time不能为空！");
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
