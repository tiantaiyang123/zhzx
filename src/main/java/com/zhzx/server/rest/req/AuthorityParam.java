/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：权限表
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
import com.zhzx.server.enums.AuthorityTypeEnum;
import com.zhzx.server.domain.Authority;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "权限表参数", description = "")
public class AuthorityParam implements Serializable {
    /**
     * Id
     */
    @ApiModelProperty(value = "Id")
    private Long id;
    /**
     * Id IN值List
     */
    @ApiModelProperty(value = "Id IN值List")
    private List<Long> idList;
    /**
     * 父级Id
     */
    @ApiModelProperty(value = "父级Id")
    private Long parentId;
    /**
     * 权限类型
     */
    @ApiModelProperty(value = "权限类型")
    private AuthorityTypeEnum type;
    /**
     * 权限类型 IN值List
     */
    @ApiModelProperty(value = "权限类型 IN值List")
    private List<String> typeList;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;
    /**
     * 路径
     */
    @ApiModelProperty(value = "路径")
    private String path;
    /**
     * 重定向路径
     */
    @ApiModelProperty(value = "重定向路径")
    private String redirect;
    /**
     * 是否缓存
     */
    @ApiModelProperty(value = "是否缓存")
    private Boolean keepAlive;
    /**
     * 是否隐藏
     */
    @ApiModelProperty(value = "是否隐藏")
    private Boolean hidden;
    /**
     * 是否隐藏子菜单
     */
    @ApiModelProperty(value = "是否隐藏子菜单")
    private Boolean hideChildren;
    /**
     * 页面组件
     */
    @ApiModelProperty(value = "页面组件")
    private String component;
    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;
    /**
     * 图标
     */
    @ApiModelProperty(value = "图标")
    private String icon;
    /**
     * 链接目标
     */
    @ApiModelProperty(value = "链接目标")
    private String target;
    /**
     * 是否隐藏header
     */
    @ApiModelProperty(value = "是否隐藏header")
    private Boolean hiddenHeaderContent;
    /**
     * 序号
     */
    @ApiModelProperty(value = "序号")
    private Long sortOrder;
    /**
     * 操作人Id
     */
    @ApiModelProperty(value = "操作人Id")
    private Long editorId;
    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private String editorName;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
    /**
     * 创建时间 下限值(大于等于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间 下限值(大于等于)")
    private java.util.Date createTimeFrom;
    /**
     * 创建时间 上限值(小于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间 上限值(小于)")
    private java.util.Date createTimeTo;
    /**
     * 更新时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
    /**
     * 更新时间 下限值(大于等于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间 下限值(大于等于)")
    private java.util.Date updateTimeFrom;
    /**
     * 更新时间 上限值(小于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间 上限值(小于)")
    private java.util.Date updateTimeTo;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<Authority> toQueryWrapper() {
        QueryWrapper<Authority> wrapper = Wrappers.<Authority>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getParentId() != null, "parent_id", this.getParentId());
        wrapper.eq(this.getType() != null, "type", this.getType());
        wrapper.in(this.getTypeList() != null && this.getTypeList().size() > 0, "type", this.getTypeList());
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
        if (this.getPath() != null) {
            if (this.getPath().startsWith("%") && this.getPath().endsWith("%")) {
                wrapper.like("path", this.getPath().substring(1, this.getPath().length() - 1));
            } else if (this.getPath().startsWith("%") && !this.getPath().endsWith("%")) {
                wrapper.likeLeft("path", this.getPath().substring(1));
            } else if (this.getPath().endsWith("%")) {
                wrapper.likeRight("path", this.getPath().substring(0, this.getPath().length() - 1));
            } else {
                wrapper.eq("path", this.getPath());
            }
        }
        if (this.getRedirect() != null) {
            if (this.getRedirect().startsWith("%") && this.getRedirect().endsWith("%")) {
                wrapper.like("redirect", this.getRedirect().substring(1, this.getRedirect().length() - 1));
            } else if (this.getRedirect().startsWith("%") && !this.getRedirect().endsWith("%")) {
                wrapper.likeLeft("redirect", this.getRedirect().substring(1));
            } else if (this.getRedirect().endsWith("%")) {
                wrapper.likeRight("redirect", this.getRedirect().substring(0, this.getRedirect().length() - 1));
            } else {
                wrapper.eq("redirect", this.getRedirect());
            }
        }
        wrapper.eq(this.getKeepAlive() != null, "keep_alive", this.getKeepAlive());
        wrapper.eq(this.getHidden() != null, "hidden", this.getHidden());
        wrapper.eq(this.getHideChildren() != null, "hide_children", this.getHideChildren());
        if (this.getComponent() != null) {
            if (this.getComponent().startsWith("%") && this.getComponent().endsWith("%")) {
                wrapper.like("component", this.getComponent().substring(1, this.getComponent().length() - 1));
            } else if (this.getComponent().startsWith("%") && !this.getComponent().endsWith("%")) {
                wrapper.likeLeft("component", this.getComponent().substring(1));
            } else if (this.getComponent().endsWith("%")) {
                wrapper.likeRight("component", this.getComponent().substring(0, this.getComponent().length() - 1));
            } else {
                wrapper.eq("component", this.getComponent());
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
        if (this.getIcon() != null) {
            if (this.getIcon().startsWith("%") && this.getIcon().endsWith("%")) {
                wrapper.like("icon", this.getIcon().substring(1, this.getIcon().length() - 1));
            } else if (this.getIcon().startsWith("%") && !this.getIcon().endsWith("%")) {
                wrapper.likeLeft("icon", this.getIcon().substring(1));
            } else if (this.getIcon().endsWith("%")) {
                wrapper.likeRight("icon", this.getIcon().substring(0, this.getIcon().length() - 1));
            } else {
                wrapper.eq("icon", this.getIcon());
            }
        }
        if (this.getTarget() != null) {
            if (this.getTarget().startsWith("%") && this.getTarget().endsWith("%")) {
                wrapper.like("target", this.getTarget().substring(1, this.getTarget().length() - 1));
            } else if (this.getTarget().startsWith("%") && !this.getTarget().endsWith("%")) {
                wrapper.likeLeft("target", this.getTarget().substring(1));
            } else if (this.getTarget().endsWith("%")) {
                wrapper.likeRight("target", this.getTarget().substring(0, this.getTarget().length() - 1));
            } else {
                wrapper.eq("target", this.getTarget());
            }
        }
        wrapper.eq(this.getHiddenHeaderContent() != null, "hidden_header_content", this.getHiddenHeaderContent());
        wrapper.eq(this.getSortOrder() != null, "sort_order", this.getSortOrder());
        wrapper.eq(this.getEditorId() != null, "editor_id", this.getEditorId());
        if (this.getEditorName() != null) {
            if (this.getEditorName().startsWith("%") && this.getEditorName().endsWith("%")) {
                wrapper.like("editor_name", this.getEditorName().substring(1, this.getEditorName().length() - 1));
            } else if (this.getEditorName().startsWith("%") && !this.getEditorName().endsWith("%")) {
                wrapper.likeLeft("editor_name", this.getEditorName().substring(1));
            } else if (this.getEditorName().endsWith("%")) {
                wrapper.likeRight("editor_name", this.getEditorName().substring(0, this.getEditorName().length() - 1));
            } else {
                wrapper.eq("editor_name", this.getEditorName());
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
