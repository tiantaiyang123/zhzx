/**
* 项目：中华中学管理平台
* 模型分组：消息管理
* 模型名称：消息任务表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
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
import com.zhzx.server.enums.SendMethodEnum;
import com.zhzx.server.domain.MessageTask;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "消息任务表参数", description = "")
public class MessageTaskParam implements Serializable {
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
     * 任务模板id
     */
    @ApiModelProperty(value = "任务模板id")
    private Long messageTemplateId;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;
    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;
    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String content;
    /**
     * 发送策略
     */
    @ApiModelProperty(value = "发送策略")
    private String cron;
    /**
     * 发送策略json
     */
    @ApiModelProperty(value = "发送策略json")
    private String cronJson;
    /**
     * 发送方式
     */
    @ApiModelProperty(value = "发送方式")
    private SendMethodEnum sendMethod;
    /**
     * 发送方式 IN值List
     */
    @ApiModelProperty(value = "发送方式 IN值List")
    private List<String> sendMethodList;
    /**
     * 
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "")
    private java.util.Date startTime;
    /**
     *  下限值(大于等于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = " 下限值(大于等于)")
    private java.util.Date startTimeFrom;
    /**
     *  上限值(小于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = " 上限值(小于)")
    private java.util.Date startTimeTo;
    /**
     * 
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "")
    private java.util.Date endTime;
    /**
     *  下限值(大于等于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = " 下限值(大于等于)")
    private java.util.Date endTimeFrom;
    /**
     *  上限值(小于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = " 上限值(小于)")
    private java.util.Date endTimeTo;
    /**
     * 修改人,sys_user_id
     */
    @ApiModelProperty(value = "修改人,sys_user_id")
    private Long editorId;
    /**
     * 修改人名称
     */
    @ApiModelProperty(value = "修改人名称")
    private String editorName;
    /**
     * 
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "")
    private java.util.Date createdTime;
    /**
     *  下限值(大于等于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = " 下限值(大于等于)")
    private java.util.Date createdTimeFrom;
    /**
     *  上限值(小于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = " 上限值(小于)")
    private java.util.Date createdTimeTo;
    /**
     * 
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "")
    private java.util.Date updatedTime;
    /**
     *  下限值(大于等于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = " 下限值(大于等于)")
    private java.util.Date updatedTimeFrom;
    /**
     *  上限值(小于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = " 上限值(小于)")
    private java.util.Date updatedTimeTo;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<MessageTask> toQueryWrapper() {
        QueryWrapper<MessageTask> wrapper = Wrappers.<MessageTask>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getMessageTemplateId() != null, "message_template_id", this.getMessageTemplateId());
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
        if (this.getContent() != null) {
            if (this.getContent().startsWith("%") && this.getContent().endsWith("%")) {
                wrapper.like("content", this.getContent().substring(1, this.getContent().length() - 1));
            } else if (this.getContent().startsWith("%") && !this.getContent().endsWith("%")) {
                wrapper.likeLeft("content", this.getContent().substring(1));
            } else if (this.getContent().endsWith("%")) {
                wrapper.likeRight("content", this.getContent().substring(0, this.getContent().length() - 1));
            } else {
                wrapper.eq("content", this.getContent());
            }
        }
        if (this.getCron() != null) {
            if (this.getCron().startsWith("%") && this.getCron().endsWith("%")) {
                wrapper.like("cron", this.getCron().substring(1, this.getCron().length() - 1));
            } else if (this.getCron().startsWith("%") && !this.getCron().endsWith("%")) {
                wrapper.likeLeft("cron", this.getCron().substring(1));
            } else if (this.getCron().endsWith("%")) {
                wrapper.likeRight("cron", this.getCron().substring(0, this.getCron().length() - 1));
            } else {
                wrapper.eq("cron", this.getCron());
            }
        }
        if (this.getCronJson() != null) {
            if (this.getCronJson().startsWith("%") && this.getCronJson().endsWith("%")) {
                wrapper.like("cron_json", this.getCronJson().substring(1, this.getCronJson().length() - 1));
            } else if (this.getCronJson().startsWith("%") && !this.getCronJson().endsWith("%")) {
                wrapper.likeLeft("cron_json", this.getCronJson().substring(1));
            } else if (this.getCronJson().endsWith("%")) {
                wrapper.likeRight("cron_json", this.getCronJson().substring(0, this.getCronJson().length() - 1));
            } else {
                wrapper.eq("cron_json", this.getCronJson());
            }
        }
        wrapper.eq(this.getSendMethod() != null, "send_method", this.getSendMethod());
        wrapper.in(this.getSendMethodList() != null && this.getSendMethodList().size() > 0, "send_method", this.getSendMethodList());
        wrapper.eq(this.getStartTime() != null, "start_time", this.getStartTime());
        wrapper.ge(this.getStartTimeFrom() != null, "start_time", this.getStartTimeFrom());
        wrapper.lt(this.getStartTimeTo() != null, "start_time", this.getStartTimeTo());
        wrapper.eq(this.getEndTime() != null, "end_time", this.getEndTime());
        wrapper.ge(this.getEndTimeFrom() != null, "end_time", this.getEndTimeFrom());
        wrapper.lt(this.getEndTimeTo() != null, "end_time", this.getEndTimeTo());
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
        wrapper.eq(this.getCreatedTime() != null, "created_time", this.getCreatedTime());
        wrapper.ge(this.getCreatedTimeFrom() != null, "created_time", this.getCreatedTimeFrom());
        wrapper.lt(this.getCreatedTimeTo() != null, "created_time", this.getCreatedTimeTo());
        wrapper.eq(this.getUpdatedTime() != null, "updated_time", this.getUpdatedTime());
        wrapper.ge(this.getUpdatedTimeFrom() != null, "updated_time", this.getUpdatedTimeFrom());
        wrapper.lt(this.getUpdatedTimeTo() != null, "updated_time", this.getUpdatedTimeTo());
        return wrapper;
    }

}
