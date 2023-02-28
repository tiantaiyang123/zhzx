/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：意见与建议处理表
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
import com.zhzx.server.domain.CommentTask;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "意见与建议处理表参数", description = "")
public class CommentTaskParam implements Serializable {
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
     * 意见与建议推送ID day_comment_process.id
     */
    @ApiModelProperty(value = "意见与建议推送ID day_comment_process.id")
    private Long commentProcessId;
    /**
     * 序号
     */
    @ApiModelProperty(value = "序号")
    private Integer sortOrder;
    /**
     * 职能部门ID sys_function_department.id
     */
    @ApiModelProperty(value = "职能部门ID sys_function_department.id")
    private Long functionDepartmentId;
    /**
     * 职能部门负责人
     */
    @ApiModelProperty(value = "职能部门负责人")
    private String principal;
    /**
     * 处理结果
     */
    @ApiModelProperty(value = "处理结果")
    private String content;
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
    public QueryWrapper<CommentTask> toQueryWrapper() {
        QueryWrapper<CommentTask> wrapper = Wrappers.<CommentTask>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getCommentProcessId() != null, "comment_process_id", this.getCommentProcessId());
        wrapper.eq(this.getSortOrder() != null, "sort_order", this.getSortOrder());
        wrapper.eq(this.getFunctionDepartmentId() != null, "function_department_id", this.getFunctionDepartmentId());
        if (this.getPrincipal() != null) {
            if (this.getPrincipal().startsWith("%") && this.getPrincipal().endsWith("%")) {
                wrapper.like("principal", this.getPrincipal().substring(1, this.getPrincipal().length() - 1));
            } else if (this.getPrincipal().startsWith("%") && !this.getPrincipal().endsWith("%")) {
                wrapper.likeLeft("principal", this.getPrincipal().substring(1));
            } else if (this.getPrincipal().endsWith("%")) {
                wrapper.likeRight("principal", this.getPrincipal().substring(0, this.getPrincipal().length() - 1));
            } else {
                wrapper.eq("principal", this.getPrincipal());
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
        wrapper.eq(this.getCreateTime() != null, "create_time", this.getCreateTime());
        wrapper.ge(this.getCreateTimeFrom() != null, "create_time", this.getCreateTimeFrom());
        wrapper.lt(this.getCreateTimeTo() != null, "create_time", this.getCreateTimeTo());
        wrapper.eq(this.getUpdateTime() != null, "update_time", this.getUpdateTime());
        wrapper.ge(this.getUpdateTimeFrom() != null, "update_time", this.getUpdateTimeFrom());
        wrapper.lt(this.getUpdateTimeTo() != null, "update_time", this.getUpdateTimeTo());
        return wrapper;
    }

}
