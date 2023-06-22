/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：手机app应用配置表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
 */

package com.zhzx.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zhzx.server.enums.AuthorityTypeEnum;
import com.zhzx.server.rest.res.ApiCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`sys_application_app`")
@ApiModel(value = "ApplicationApp", description = "手机app应用配置表")
public class ApplicationApp extends BaseDomain {
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
     * 编码
     */
    @TableField(value = "code")
    @ApiModelProperty(value = "编码", required = true)
    private String code;
    /**
     * 跳转路径
     */
    @TableField(value = "path")
    @ApiModelProperty(value = "跳转路径")
    private String path;
    /**
     * 小程序编码
     */
    @TableField(value = "xcx_code")
    @ApiModelProperty(value = "小程序编码")
    private String xcxCode;
    /**
     * 是否隐藏
     */
    @TableField(value = "hidden")
    @ApiModelProperty(value = "是否隐藏", required = true)
    private String hidden;
    /**
     * 是否隐藏子菜单
     */
    @TableField(value = "hide_children")
    @ApiModelProperty(value = "是否隐藏子菜单", required = true)
    private String hideChildren;
    /**
     * 图标
     */
    @TableField(value = "icon")
    @ApiModelProperty(value = "图标")
    private String icon;
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
    public ApplicationApp setDefault() {
        if (this.getParentId() == null) {
            this.setParentId(0L);
        }
        if (this.getType() == null) {
            this.setType(AuthorityTypeEnum.MENU);
        }
        if (this.getHidden() == null) {
            this.setHidden(null);
        }
        if (this.getHideChildren() == null) {
            this.setHideChildren(null);
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
        if (this.getCode() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "编码不能为空！");
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
