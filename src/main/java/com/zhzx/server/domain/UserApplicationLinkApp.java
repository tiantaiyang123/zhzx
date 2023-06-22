/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：手机app用户应用跳转表
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
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`sys_user_application_link_app`")
@ApiModel(value = "UserApplicationLinkApp", description = "手机app用户应用跳转表")
public class UserApplicationLinkApp extends BaseDomain {
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
     * 小程序编码
     */
    @TableField(value = "xcx_code")
    @ApiModelProperty(value = "小程序编码", required = true)
    private String xcxCode;
    /**
     * 跳转路径
     */
    @TableField(value = "link_path")
    @ApiModelProperty(value = "跳转路径", required = true)
    private String linkPath;
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
    public UserApplicationLinkApp setDefault() {
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
        if (this.getUserId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "用户Id不能为空！");
            return false;
        }
        if (this.getXcxCode() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "小程序编码不能为空！");
            return false;
        }
        if (this.getLinkPath() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "跳转路径不能为空！");
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
