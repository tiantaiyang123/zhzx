/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：部门表
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
import com.zhzx.server.domain.Department;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "部门表参数", description = "")
public class DepartmentParam implements Serializable {
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
     * 部门负责人 多选 用[/]分割 sys_staff.name
     */
    @ApiModelProperty(value = "部门负责人 多选 用[/]分割 sys_staff.name")
    private String principal;
    /**
     * 父级ID sys_department.id
     */
    @ApiModelProperty(value = "父级ID sys_department.id")
    private Long parentId;
    /**
     * 序号
     */
    @ApiModelProperty(value = "序号")
    private Long sortOrder;
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
    public QueryWrapper<Department> toQueryWrapper() {
        QueryWrapper<Department> wrapper = Wrappers.<Department>query();
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
        wrapper.eq(this.getParentId() != null, "parent_id", this.getParentId());
        wrapper.eq(this.getSortOrder() != null, "sort_order", this.getSortOrder());
        wrapper.eq(this.getCreateTime() != null, "create_time", this.getCreateTime());
        wrapper.ge(this.getCreateTimeFrom() != null, "create_time", this.getCreateTimeFrom());
        wrapper.lt(this.getCreateTimeTo() != null, "create_time", this.getCreateTimeTo());
        wrapper.eq(this.getUpdateTime() != null, "update_time", this.getUpdateTime());
        wrapper.ge(this.getUpdateTimeFrom() != null, "update_time", this.getUpdateTimeFrom());
        wrapper.lt(this.getUpdateTimeTo() != null, "update_time", this.getUpdateTimeTo());
        return wrapper;
    }

}
