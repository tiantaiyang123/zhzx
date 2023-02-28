/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：日志表
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
import com.zhzx.server.domain.Log;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "日志表参数", description = "")
public class LogParam implements Serializable {
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
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String code;
    /**
     * 值
     */
    @ApiModelProperty(value = "值")
    private String value;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 操作人ID
     */
    @ApiModelProperty(value = "操作人ID")
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
    public QueryWrapper<Log> toQueryWrapper() {
        QueryWrapper<Log> wrapper = Wrappers.<Log>query();
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
        if (this.getValue() != null) {
            if (this.getValue().startsWith("%") && this.getValue().endsWith("%")) {
                wrapper.like("value", this.getValue().substring(1, this.getValue().length() - 1));
            } else if (this.getValue().startsWith("%") && !this.getValue().endsWith("%")) {
                wrapper.likeLeft("value", this.getValue().substring(1));
            } else if (this.getValue().endsWith("%")) {
                wrapper.likeRight("value", this.getValue().substring(0, this.getValue().length() - 1));
            } else {
                wrapper.eq("value", this.getValue());
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
