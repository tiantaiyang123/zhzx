/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：职能部门表(用于一日常规意见与建议)
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
import com.zhzx.server.domain.FunctionDepartment;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "职能部门表(用于一日常规意见与建议)参数", description = "")
public class FunctionDepartmentParam implements Serializable {
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
     * 序号
     */
    @ApiModelProperty(value = "序号")
    private Long sortOrder;
    /**
     * 父级ID sys_function_department.id
     */
    @ApiModelProperty(value = "父级ID sys_function_department.id")
    private Long parentId;
    /**
     * 负责人ID sys_user.id
     */
    @ApiModelProperty(value = "负责人ID sys_user.id")
    private Long principalId;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<FunctionDepartment> toQueryWrapper() {
        QueryWrapper<FunctionDepartment> wrapper = Wrappers.<FunctionDepartment>query();
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
        wrapper.eq(this.getSortOrder() != null, "sort_order", this.getSortOrder());
        wrapper.eq(this.getParentId() != null, "parent_id", this.getParentId());
        wrapper.eq(this.getPrincipalId() != null, "principal_id", this.getPrincipalId());
        return wrapper;
    }

}
