/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：意见与建议推送表
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
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.enums.CommentProcessSourceEnum;
import com.zhzx.server.enums.CommentStateEnum;
import com.zhzx.server.domain.CommentProcess;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "意见与建议推送表参数", description = "")
public class CommentProcessParam implements Serializable {
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
     * 意见与建议ID day_comment.id
     */
    @ApiModelProperty(value = "意见与建议ID day_comment.id")
    private Long commentId;
    /**
     * 是否需要校长室批示
     */
    @ApiModelProperty(value = "是否需要校长室批示")
    private YesNoEnum needInstruction;
    /**
     * 是否需要校长室批示 IN值List
     */
    @ApiModelProperty(value = "是否需要校长室批示 IN值List")
    private List<String> needInstructionList;
    /**
     * 校长室批示
     */
    @ApiModelProperty(value = "校长室批示")
    private String instructions;
    /**
     * 批示人
     */
    @ApiModelProperty(value = "批示人")
    private String headmaster;
    /**
     * 来源
     */
    @ApiModelProperty(value = "来源")
    private CommentProcessSourceEnum source;
    /**
     * 来源 IN值List
     */
    @ApiModelProperty(value = "来源 IN值List")
    private List<String> sourceList;
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private CommentStateEnum state;
    /**
     * 状态 IN值List
     */
    @ApiModelProperty(value = "状态 IN值List")
    private List<String> stateList;
    /**
     * 
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "")
    private java.util.Date createTime;
    /**
     *  下限值(大于等于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = " 下限值(大于等于)")
    private java.util.Date createTimeFrom;
    /**
     *  上限值(小于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = " 上限值(小于)")
    private java.util.Date createTimeTo;
    /**
     * 
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "")
    private java.util.Date updateTime;
    /**
     *  下限值(大于等于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = " 下限值(大于等于)")
    private java.util.Date updateTimeFrom;
    /**
     *  上限值(小于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = " 上限值(小于)")
    private java.util.Date updateTimeTo;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<CommentProcess> toQueryWrapper() {
        QueryWrapper<CommentProcess> wrapper = Wrappers.<CommentProcess>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getCommentId() != null, "comment_id", this.getCommentId());
        wrapper.eq(this.getNeedInstruction() != null, "need_instruction", this.getNeedInstruction());
        wrapper.in(this.getNeedInstructionList() != null && this.getNeedInstructionList().size() > 0, "need_instruction", this.getNeedInstructionList());
        if (this.getInstructions() != null) {
            if (this.getInstructions().startsWith("%") && this.getInstructions().endsWith("%")) {
                wrapper.like("instructions", this.getInstructions().substring(1, this.getInstructions().length() - 1));
            } else if (this.getInstructions().startsWith("%") && !this.getInstructions().endsWith("%")) {
                wrapper.likeLeft("instructions", this.getInstructions().substring(1));
            } else if (this.getInstructions().endsWith("%")) {
                wrapper.likeRight("instructions", this.getInstructions().substring(0, this.getInstructions().length() - 1));
            } else {
                wrapper.eq("instructions", this.getInstructions());
            }
        }
        if (this.getHeadmaster() != null) {
            if (this.getHeadmaster().startsWith("%") && this.getHeadmaster().endsWith("%")) {
                wrapper.like("headmaster", this.getHeadmaster().substring(1, this.getHeadmaster().length() - 1));
            } else if (this.getHeadmaster().startsWith("%") && !this.getHeadmaster().endsWith("%")) {
                wrapper.likeLeft("headmaster", this.getHeadmaster().substring(1));
            } else if (this.getHeadmaster().endsWith("%")) {
                wrapper.likeRight("headmaster", this.getHeadmaster().substring(0, this.getHeadmaster().length() - 1));
            } else {
                wrapper.eq("headmaster", this.getHeadmaster());
            }
        }
        wrapper.eq(this.getSource() != null, "source", this.getSource());
        wrapper.in(this.getSourceList() != null && this.getSourceList().size() > 0, "source", this.getSourceList());
        wrapper.eq(this.getState() != null, "state", this.getState());
        wrapper.in(this.getStateList() != null && this.getStateList().size() > 0, "state", this.getStateList());
        wrapper.eq(this.getCreateTime() != null, "create_time", this.getCreateTime());
        wrapper.ge(this.getCreateTimeFrom() != null, "create_time", this.getCreateTimeFrom());
        wrapper.lt(this.getCreateTimeTo() != null, "create_time", this.getCreateTimeTo());
        wrapper.eq(this.getUpdateTime() != null, "update_time", this.getUpdateTime());
        wrapper.ge(this.getUpdateTimeFrom() != null, "update_time", this.getUpdateTimeFrom());
        wrapper.lt(this.getUpdateTimeTo() != null, "update_time", this.getUpdateTimeTo());
        return wrapper;
    }

}
