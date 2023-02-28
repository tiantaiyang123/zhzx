/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：权限表
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
import com.zhzx.server.enums.AuthorityTypeEnum;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`sys_authority`")
@ApiModel(value = "Authority", description = "权限表")
public class Authority extends BaseDomain {
    /**
     * Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "Id", required = true)
    private Long id;
    /**
     * 父级Id
     */
    @TableField(value = "parent_id")
    @ApiModelProperty(value = "父级Id", required = true)
    private Long parentId;
    /**
     * 权限类型
     */
    @TableField(value = "type")
    @ApiModelProperty(value = "权限类型", required = true)
    private AuthorityTypeEnum type;
    /**
     * 名称
     */
    @TableField(value = "name")
    @ApiModelProperty(value = "名称", required = true)
    private String name;
    /**
     * 路径
     */
    @TableField(value = "path")
    @ApiModelProperty(value = "路径")
    private String path;
    /**
     * 重定向路径
     */
    @TableField(value = "redirect")
    @ApiModelProperty(value = "重定向路径")
    private String redirect;
    /**
     * 是否缓存
     */
    @TableField(value = "keep_alive")
    @ApiModelProperty(value = "是否缓存", required = true)
    private Boolean keepAlive;
    /**
     * 是否隐藏
     */
    @TableField(value = "hidden")
    @ApiModelProperty(value = "是否隐藏", required = true)
    private Boolean hidden;
    /**
     * 是否隐藏子菜单
     */
    @TableField(value = "hide_children")
    @ApiModelProperty(value = "是否隐藏子菜单", required = true)
    private Boolean hideChildren;
    /**
     * 页面组件
     */
    @TableField(value = "component")
    @ApiModelProperty(value = "页面组件")
    private String component;
    /**
     * 标题
     */
    @TableField(value = "title")
    @ApiModelProperty(value = "标题", required = true)
    private String title;
    /**
     * 图标
     */
    @TableField(value = "icon")
    @ApiModelProperty(value = "图标")
    private String icon;
    /**
     * 链接目标
     */
    @TableField(value = "target")
    @ApiModelProperty(value = "链接目标")
    private String target;
    /**
     * 是否隐藏header
     */
    @TableField(value = "hidden_header_content")
    @ApiModelProperty(value = "是否隐藏header", required = true)
    private Boolean hiddenHeaderContent;
    /**
     * 序号
     */
    @TableField(value = "sort_order")
    @ApiModelProperty(value = "序号", required = true)
    private Long sortOrder;
    /**
     * 操作人Id
     */
    @TableField(value = "editor_id")
    @ApiModelProperty(value = "操作人Id", required = true)
    private Long editorId;
    /**
     * 操作人
     */
    @TableField(value = "editor_name")
    @ApiModelProperty(value = "操作人", required = true)
    private String editorName;
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
    public Authority setDefault() {
        if (this.getParentId() == null) {
            this.setParentId(0L);
        }
        if (this.getType() == null) {
            this.setType(AuthorityTypeEnum.MENU);
        }
        if (this.getKeepAlive() == null) {
            this.setKeepAlive(null);
        }
        if (this.getHidden() == null) {
            this.setHidden(null);
        }
        if (this.getHideChildren() == null) {
            this.setHideChildren(null);
        }
        if (this.getHiddenHeaderContent() == null) {
            this.setHiddenHeaderContent(null);
        }
        if (this.getSortOrder() == null) {
            this.setSortOrder(0L);
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
        if (this.getParentId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "父级Id不能为空！");
            return false;
        }
        if (this.getType() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "权限类型不能为空！");
            return false;
        }
        if (this.getName() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "名称不能为空！");
            return false;
        }
        if (this.getKeepAlive() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "是否缓存不能为空！");
            return false;
        }
        if (this.getHidden() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "是否隐藏不能为空！");
            return false;
        }
        if (this.getHideChildren() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "是否隐藏子菜单不能为空！");
            return false;
        }
        if (this.getTitle() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "标题不能为空！");
            return false;
        }
        if (this.getHiddenHeaderContent() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "是否隐藏header不能为空！");
            return false;
        }
        if (this.getSortOrder() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "序号不能为空！");
            return false;
        }
        Long sortOrderMax = Long.valueOf("99999");
        if (this.getSortOrder() >= sortOrderMax) {
            if (throwException) throw new ApiCode.ApiException(-1, "序号不能大于或等于99999！");
            return false;
        }
        if (this.getEditorId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "操作人Id不能为空！");
            return false;
        }
        if (this.getEditorName() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "操作人不能为空！");
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
