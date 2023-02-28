/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：教学成果分类表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest.req;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhzx.server.domain.TeachingResultClassify;
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
@ApiModel(value = "教学成果分类表参数", description = "")
public class TeachingResultClassifyParam implements Serializable {
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
    private String name;
    /**
     * 父级ID
     */
    @ApiModelProperty(value = "父级ID")
    private Long parentId;
    /**
     * ID系统自动生成 IN值List
     */
    @ApiModelProperty(value = "父ID列表 IN值List")
    private List<Long> parentIdList;
    /**
     * 序号
     */
    @ApiModelProperty(value = "序号")
    private Long sortOrder;
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
    public QueryWrapper<TeachingResultClassify> toQueryWrapper() {
        QueryWrapper<TeachingResultClassify> wrapper = Wrappers.<TeachingResultClassify>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.in(this.getParentIdList() != null && this.getParentIdList().size() > 0, "parent_id", this.getParentIdList());
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
        wrapper.eq(this.getParentId() != null, "parent_id", this.getParentId());
        wrapper.eq(this.getSortOrder() != null, "sort_order", this.getSortOrder());
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
