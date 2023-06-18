/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：手机app用户首页应用表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zhzx.server.rest.res.ApiCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`sys_user_application_prefer_app`")
@ApiModel(value = "UserApplicationPreferApp", description = "手机app用户首页应用表")
public class UserApplicationPreferApp extends BaseDomain {
    /**
     * Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "Id", required = true)
    private Long id;
    /**
     * 用户Id
     */
    @TableField(value = "user_id")
    @ApiModelProperty(value = "用户Id", required = true)
    private Long userId;
    /**
     * 手机应用Id
     */
    @TableField(value = "application_app_id")
    @ApiModelProperty(value = "手机应用Id", required = true)
    private Long applicationAppId;

    /**
     * 手机应用
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "手机应用")
    private ApplicationApp applicationApp;

    /**
     * 序号
     */
    @TableField(value = "sort_order")
    @ApiModelProperty(value = "序号", required = true)
    private Long sortOrder;

    /**
     * 设置默认值
     */
    public UserApplicationPreferApp setDefault() {
        if (this.getUserId() == null) {
            this.setUserId(0L);
        }
        if (this.getApplicationAppId() == null) {
            this.setApplicationAppId(0L);
        }
        if (this.getSortOrder() == null) {
            this.setSortOrder(0L);
        }
        return this;
    }

    /**
     * 数据验证
     */
    public Boolean validate(Boolean throwException) {
        if (this.getUserId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "用户Id不能为空！");
            return false;
        }
        if (this.getApplicationAppId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "手机应用Id不能为空！");
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
        return true;
    }

    /**
     * 数据验证
     */
    public Boolean validate() {
        return this.validate(false);
    }

}
