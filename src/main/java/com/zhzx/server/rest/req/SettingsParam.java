/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：系统配置表
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
import com.zhzx.server.domain.Settings;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "系统配置表参数", description = "")
public class SettingsParam implements Serializable {
    /**
     * ID系统自动生成
     */
    @ApiModelProperty(value = "ID系统自动生成")
    private Long id;
    /**
     * ID系统自动生成 IN值List
     */
    @ApiModelProperty(value = "ID系统自动生成 IN值List")
    private List<Long> idList;
    /**
     * 编码
     */
    @ApiModelProperty(value = "编码")
    private String code;
    /**
     * 说明
     */
    @ApiModelProperty(value = "说明")
    private String remark;
    /**
     * 参数json
     */
    @ApiModelProperty(value = "参数json")
    private String params;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<Settings> toQueryWrapper() {
        QueryWrapper<Settings> wrapper = Wrappers.<Settings>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        if (this.getCode() != null) {
            if (this.getCode().startsWith("%") && this.getCode().endsWith("%")) {
                wrapper.like("code", this.getCode().substring(1, this.getCode().length() - 1));
            } else if (this.getCode().startsWith("%") && !this.getCode().endsWith("%")) {
                wrapper.likeLeft("code", this.getCode().substring(1));
            } else if (this.getCode().endsWith("%")) {
                wrapper.likeRight("code", this.getCode().substring(0, this.getCode().length() - 1));
            } else {
                wrapper.eq("code", this.getCode());
            }
        }
        if (this.getRemark() != null) {
            if (this.getRemark().startsWith("%") && this.getRemark().endsWith("%")) {
                wrapper.like("remark", this.getRemark().substring(1, this.getRemark().length() - 1));
            } else if (this.getRemark().startsWith("%") && !this.getRemark().endsWith("%")) {
                wrapper.likeLeft("remark", this.getRemark().substring(1));
            } else if (this.getRemark().endsWith("%")) {
                wrapper.likeRight("remark", this.getRemark().substring(0, this.getRemark().length() - 1));
            } else {
                wrapper.eq("remark", this.getRemark());
            }
        }
        if (this.getParams() != null) {
            if (this.getParams().startsWith("%") && this.getParams().endsWith("%")) {
                wrapper.like("params", this.getParams().substring(1, this.getParams().length() - 1));
            } else if (this.getParams().startsWith("%") && !this.getParams().endsWith("%")) {
                wrapper.likeLeft("params", this.getParams().substring(1));
            } else if (this.getParams().endsWith("%")) {
                wrapper.likeRight("params", this.getParams().substring(0, this.getParams().length() - 1));
            } else {
                wrapper.eq("params", this.getParams());
            }
        }
        return wrapper;
    }

}
