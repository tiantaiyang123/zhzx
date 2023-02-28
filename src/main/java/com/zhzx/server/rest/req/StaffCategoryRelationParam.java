/**
* 项目：中华中学管理平台
* 模型分组：系统管理
* 模型名称：老师分类关联表
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
import com.zhzx.server.domain.StaffCategoryRelation;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "老师分类关联表参数", description = "")
public class StaffCategoryRelationParam implements Serializable {
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
     * 分类id
     */
    @ApiModelProperty(value = "分类id")
    private Long staffCategoryId;
    /**
     * teahcerID
     */
    @ApiModelProperty(value = "teahcerID")
    private Long staffId;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String parentCategory;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<StaffCategoryRelation> toQueryWrapper() {
        QueryWrapper<StaffCategoryRelation> wrapper = Wrappers.<StaffCategoryRelation>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getStaffCategoryId() != null, "staff_category_id", this.getStaffCategoryId());
        wrapper.eq(this.getStaffId() != null, "staff_id", this.getStaffId());
        if (this.getParentCategory() != null) {
            if (this.getParentCategory().startsWith("%") && this.getParentCategory().endsWith("%")) {
                wrapper.like("parent_category", this.getParentCategory().substring(1, this.getParentCategory().length() - 1));
            } else if (this.getParentCategory().startsWith("%") && !this.getParentCategory().endsWith("%")) {
                wrapper.likeLeft("parent_category", this.getParentCategory().substring(1));
            } else if (this.getParentCategory().endsWith("%")) {
                wrapper.likeRight("parent_category", this.getParentCategory().substring(0, this.getParentCategory().length() - 1));
            } else {
                wrapper.eq("parent_category", this.getParentCategory());
            }
        }
        return wrapper;
    }

}
