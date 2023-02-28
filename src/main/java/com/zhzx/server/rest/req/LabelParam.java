/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：标签表
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
import com.zhzx.server.domain.Label;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "标签表参数", description = "")
public class LabelParam implements Serializable {
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
     * 分类
     */
    @ApiModelProperty(value = "分类")
    private String classify;
    /**
     * 标签
     */
    @ApiModelProperty(value = "标签")
    private String name;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<Label> toQueryWrapper() {
        QueryWrapper<Label> wrapper = Wrappers.<Label>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        if (this.getClassify() != null) {
            if (this.getClassify().startsWith("%") && this.getClassify().endsWith("%")) {
                wrapper.like("classify", this.getClassify().substring(1, this.getClassify().length() - 1));
            } else if (this.getClassify().startsWith("%") && !this.getClassify().endsWith("%")) {
                wrapper.likeLeft("classify", this.getClassify().substring(1));
            } else if (this.getClassify().endsWith("%")) {
                wrapper.likeRight("classify", this.getClassify().substring(0, this.getClassify().length() - 1));
            } else {
                wrapper.eq("classify", this.getClassify());
            }
        }
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
        return wrapper;
    }

}
