/**
* 项目：中华中学管理平台
* 模型分组：消息管理
* 模型名称：消息任务表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
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
import com.zhzx.server.enums.SendMethodEnum;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.domain.MessageTemplate;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`mes_message_task`")
@ApiModel(value = "MessageTask", description = "消息任务表")
public class MessageTask extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * ID系统自动生成
     */
    @ApiModelProperty(value = "ID系统自动生成")
    @TableField(exist = false)
    private List<MessageTaskReceiver> messageTaskReceiverList;
    /**
     * 任务模板id
     */
    @TableField(value = "message_template_id")
    @ApiModelProperty(value = "任务模板id")
    private Long messageTemplateId;
    /**
     * 任务模板id
     */
    @ApiModelProperty(value = "任务模板id")
    @TableField(exist = false)
    private MessageTemplate messageTemplate;
    /**
     * 名称
     */
    @TableField(value = "name")
    @ApiModelProperty(value = "名称")
    private String name;
    /**
     * 标题
     */
    @TableField(value = "title")
    @ApiModelProperty(value = "标题")
    private String title;
    /**
     * 内容
     */
    @TableField(value = "content", select = false)
    @ApiModelProperty(value = "内容", required = true)
    private String content;
    /**
     * 发送策略
     */
    @TableField(value = "cron")
    @ApiModelProperty(value = "发送策略", required = true)
    private String cron;
    /**
     * 发送策略json
     */
    @TableField(value = "cron_json")
    @ApiModelProperty(value = "发送策略json", required = true)
    private String cronJson;
    /**
     * 发送方式
     */
    @TableField(value = "send_method")
    @ApiModelProperty(value = "发送方式", required = true)
    private SendMethodEnum sendMethod;
    /**
     * 
     */
    @TableField(value = "start_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "", required = true)
    private java.util.Date startTime;
    /**
     * 
     */
    @TableField(value = "end_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "", required = true)
    private java.util.Date endTime;
    /**
     * 修改人,sys_user_id
     */
    @TableField(value = "editor_id")
    @ApiModelProperty(value = "修改人,sys_user_id", required = true)
    private Long editorId;
    /**
     * 修改人名称
     */
    @TableField(value = "editor_name")
    @ApiModelProperty(value = "修改人名称", required = true)
    private String editorName;
    /**
     * 是否发送
     */
    @TableField(value = "send_type")
    @ApiModelProperty(value = "是否发送", required = true)
    private YesNoEnum sendType;
    /**
     * 是否需要填写
     */
    @TableField(value = "need_write")
    @ApiModelProperty(value = "是否需要填写", required = true)
    private YesNoEnum needWrite;
    /**
     * 重发时间间隔
     */
    @TableField(value = "repeat_send")
    @ApiModelProperty(value = "重发时间间隔", required = true)
    private Long repeatSend;
    /**
     * 
     */
    @TableField(value = "created_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "", required = true)
    private java.util.Date createdTime;
    /**
     * 
     */
    @TableField(value = "updated_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "", required = true)
    private java.util.Date updatedTime;

    /**
     * 设置默认值
     */
    public MessageTask setDefault() {
        if (this.getSendMethod() == null) {
            this.setSendMethod(SendMethodEnum.WX);
        }
        if (this.getSendType() == null) {
            this.setSendType(YesNoEnum.NO);
        }
        if (this.getNeedWrite() == null) {
            this.setNeedWrite(YesNoEnum.NO);
        }
        if (this.getRepeatSend() == null) {
            this.setRepeatSend(24L);
        }
        if (this.getCreatedTime() == null) {
            this.setCreatedTime(new java.util.Date());
        }
        if (this.getUpdatedTime() == null) {
            this.setUpdatedTime(new java.util.Date());
        }
        return this;
    }

    /**
     * 数据验证
     */
    public Boolean validate(Boolean throwException) {
        if (this.getContent() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "content不能为空！");
            return false;
        }
        if (this.getCron() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "cron不能为空！");
            return false;
        }
        if (this.getCronJson() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "cron_json不能为空！");
            return false;
        }
        if (this.getSendMethod() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "send_method不能为空！");
            return false;
        }
        if (this.getStartTime() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "start_time不能为空！");
            return false;
        }
        if (this.getEndTime() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "end_time不能为空！");
            return false;
        }
        if (this.getEditorId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "editor_id不能为空！");
            return false;
        }
        if (this.getEditorName() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "editor_name不能为空！");
            return false;
        }
        if (this.getSendType() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "send_type不能为空！");
            return false;
        }
        if (this.getNeedWrite() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "need_write不能为空！");
            return false;
        }
        if (this.getRepeatSend() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "repeat_send不能为空！");
            return false;
        }
        if (this.getCreatedTime() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "created_time不能为空！");
            return false;
        }
        if (this.getUpdatedTime() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "updated_time不能为空！");
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
