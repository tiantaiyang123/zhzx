/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：访问日志表表
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
import com.zhzx.server.domain.VisitLog;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "访问日志表表参数", description = "")
public class VisitLogParam implements Serializable {
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
     * 项目名称
     */
    @ApiModelProperty(value = "项目名称")
    private String projectName;
    /**
     * 次数
     */
    @ApiModelProperty(value = "次数")
    private Long num;
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
     * 最后一次访问时间时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "最后一次访问时间时间")
    private java.util.Date visitTime;
    /**
     * 最后一次访问时间时间 下限值(大于等于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "最后一次访问时间时间 下限值(大于等于)")
    private java.util.Date visitTimeFrom;
    /**
     * 最后一次访问时间时间 上限值(小于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "最后一次访问时间时间 上限值(小于)")
    private java.util.Date visitTimeTo;
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
    public QueryWrapper<VisitLog> toQueryWrapper() {
        QueryWrapper<VisitLog> wrapper = Wrappers.<VisitLog>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        if (this.getProjectName() != null) {
            if (this.getProjectName().startsWith("%") && this.getProjectName().endsWith("%")) {
                wrapper.like("project_name", this.getProjectName().substring(1, this.getProjectName().length() - 1));
            } else if (this.getProjectName().startsWith("%") && !this.getProjectName().endsWith("%")) {
                wrapper.likeLeft("project_name", this.getProjectName().substring(1));
            } else if (this.getProjectName().endsWith("%")) {
                wrapper.likeRight("project_name", this.getProjectName().substring(0, this.getProjectName().length() - 1));
            } else {
                wrapper.eq("project_name", this.getProjectName());
            }
        }
        wrapper.eq(this.getNum() != null, "num", this.getNum());
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
        wrapper.eq(this.getVisitTime() != null, "visit_time", this.getVisitTime());
        wrapper.ge(this.getVisitTimeFrom() != null, "visit_time", this.getVisitTimeFrom());
        wrapper.lt(this.getVisitTimeTo() != null, "visit_time", this.getVisitTimeTo());
        wrapper.eq(this.getCreateTime() != null, "create_time", this.getCreateTime());
        wrapper.ge(this.getCreateTimeFrom() != null, "create_time", this.getCreateTimeFrom());
        wrapper.lt(this.getCreateTimeTo() != null, "create_time", this.getCreateTimeTo());
        wrapper.eq(this.getUpdateTime() != null, "update_time", this.getUpdateTime());
        wrapper.ge(this.getUpdateTimeFrom() != null, "update_time", this.getUpdateTimeFrom());
        wrapper.lt(this.getUpdateTimeTo() != null, "update_time", this.getUpdateTimeTo());
        return wrapper;
    }

}
