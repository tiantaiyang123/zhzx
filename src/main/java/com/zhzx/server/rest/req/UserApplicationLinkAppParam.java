/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：手机app用户应用跳转表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest.req;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhzx.server.domain.UserApplicationLinkApp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.List;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "手机app用户应用跳转表参数", description = "")
public class UserApplicationLinkAppParam implements Serializable {
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
     * 用户Id
     */
    @ApiModelProperty(value = "用户Id")
    private Long userId;
    /**
     * 小程序编码
     */
    @ApiModelProperty(value = "小程序编码")
    private String xcxCode;
    /**
     * 跳转路径
     */
    @ApiModelProperty(value = "跳转路径")
    private String linkPath;
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
    public QueryWrapper<UserApplicationLinkApp> toQueryWrapper() {
        QueryWrapper<UserApplicationLinkApp> wrapper = Wrappers.<UserApplicationLinkApp>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getUserId() != null, "user_id", this.getUserId());
        if (this.getXcxCode() != null) {
            if (this.getXcxCode().startsWith("%") && this.getXcxCode().endsWith("%")) {
                wrapper.like("xcx_code", this.getXcxCode().substring(1, this.getXcxCode().length() - 1));
            } else if (this.getXcxCode().startsWith("%") && !this.getXcxCode().endsWith("%")) {
                wrapper.likeLeft("xcx_code", this.getXcxCode().substring(1));
            } else if (this.getXcxCode().endsWith("%")) {
                wrapper.likeRight("xcx_code", this.getXcxCode().substring(0, this.getXcxCode().length() - 1));
            } else {
                wrapper.eq("xcx_code", this.getXcxCode());
            }
        }
        if (this.getLinkPath() != null) {
            if (this.getLinkPath().startsWith("%") && this.getLinkPath().endsWith("%")) {
                wrapper.like("link_path", this.getLinkPath().substring(1, this.getLinkPath().length() - 1));
            } else if (this.getLinkPath().startsWith("%") && !this.getLinkPath().endsWith("%")) {
                wrapper.likeLeft("link_path", this.getLinkPath().substring(1));
            } else if (this.getLinkPath().endsWith("%")) {
                wrapper.likeRight("link_path", this.getLinkPath().substring(0, this.getLinkPath().length() - 1));
            } else {
                wrapper.eq("link_path", this.getLinkPath());
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
