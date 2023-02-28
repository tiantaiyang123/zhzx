/**
* 项目：中华中学管理平台
* 模型分组：消息管理
* 模型名称：消息模板表
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
import com.zhzx.server.domain.MessageTemplate;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "消息模板表参数", description = "")
public class MessageTemplateParam implements Serializable {
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
     * 消息模板名称
     */
    @ApiModelProperty(value = "消息模板名称")
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
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
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
    public QueryWrapper<MessageTemplate> toQueryWrapper() {
        QueryWrapper<MessageTemplate> wrapper = Wrappers.<MessageTemplate>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
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
        wrapper.eq(this.getCreatedTime() != null, "created_time", this.getCreatedTime());
        wrapper.ge(this.getCreatedTimeFrom() != null, "created_time", this.getCreatedTimeFrom());
        wrapper.lt(this.getCreatedTimeTo() != null, "created_time", this.getCreatedTimeTo());
        wrapper.eq(this.getUpdatedTime() != null, "updated_time", this.getUpdatedTime());
        wrapper.ge(this.getUpdatedTimeFrom() != null, "updated_time", this.getUpdatedTimeFrom());
        wrapper.lt(this.getUpdatedTimeTo() != null, "updated_time", this.getUpdatedTimeTo());
        return wrapper;
    }

}
