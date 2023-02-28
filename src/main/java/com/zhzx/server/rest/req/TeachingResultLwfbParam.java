/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：教学管理
 * 模型名称：论文发表表
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
import com.zhzx.server.domain.TeachingResultLwfb;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "论文发表表参数", description = "")
public class TeachingResultLwfbParam implements Serializable {
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
     * 教学成果ID tmg_teaching_result.id
     */
    @ApiModelProperty(value = "教学成果ID tmg_teaching_result.id")
    private Long teachingResultId;
    /**
     * 论文标题 和tmg_teaching_result.name一致
     */
    @ApiModelProperty(value = "论文标题 和tmg_teaching_result.name一致")
    private String lwbt;
    /**
     * ISSN刊号
     */
    @ApiModelProperty(value = "ISSN刊号")
    private String issnkh;
    /**
     * ISSN书号
     */
    @ApiModelProperty(value = "ISSN书号")
    private String issnsh;
    /**
     * CN刊号
     */
    @ApiModelProperty(value = "CN刊号")
    private String cnkh;
    /**
     * 期刊名称
     */
    @ApiModelProperty(value = "期刊名称")
    private String qkmc;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<TeachingResultLwfb> toQueryWrapper() {
        QueryWrapper<TeachingResultLwfb> wrapper = Wrappers.<TeachingResultLwfb>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getTeachingResultId() != null, "teaching_result_id", this.getTeachingResultId());
        if (this.getLwbt() != null) {
            if (this.getLwbt().startsWith("%") && this.getLwbt().endsWith("%")) {
                wrapper.like("lwbt", this.getLwbt().substring(1, this.getLwbt().length() - 1));
            } else if (this.getLwbt().startsWith("%") && !this.getLwbt().endsWith("%")) {
                wrapper.likeLeft("lwbt", this.getLwbt().substring(1));
            } else if (this.getLwbt().endsWith("%")) {
                wrapper.likeRight("lwbt", this.getLwbt().substring(0, this.getLwbt().length() - 1));
            } else {
                wrapper.eq("lwbt", this.getLwbt());
            }
        }
        if (this.getIssnkh() != null) {
            if (this.getIssnkh().startsWith("%") && this.getIssnkh().endsWith("%")) {
                wrapper.like("issnkh", this.getIssnkh().substring(1, this.getIssnkh().length() - 1));
            } else if (this.getIssnkh().startsWith("%") && !this.getIssnkh().endsWith("%")) {
                wrapper.likeLeft("issnkh", this.getIssnkh().substring(1));
            } else if (this.getIssnkh().endsWith("%")) {
                wrapper.likeRight("issnkh", this.getIssnkh().substring(0, this.getIssnkh().length() - 1));
            } else {
                wrapper.eq("issnkh", this.getIssnkh());
            }
        }
        if (this.getIssnsh() != null) {
            if (this.getIssnsh().startsWith("%") && this.getIssnsh().endsWith("%")) {
                wrapper.like("issnsh", this.getIssnsh().substring(1, this.getIssnsh().length() - 1));
            } else if (this.getIssnsh().startsWith("%") && !this.getIssnsh().endsWith("%")) {
                wrapper.likeLeft("issnsh", this.getIssnsh().substring(1));
            } else if (this.getIssnsh().endsWith("%")) {
                wrapper.likeRight("issnsh", this.getIssnsh().substring(0, this.getIssnsh().length() - 1));
            } else {
                wrapper.eq("issnsh", this.getIssnsh());
            }
        }
        if (this.getCnkh() != null) {
            if (this.getCnkh().startsWith("%") && this.getCnkh().endsWith("%")) {
                wrapper.like("cnkh", this.getCnkh().substring(1, this.getCnkh().length() - 1));
            } else if (this.getCnkh().startsWith("%") && !this.getCnkh().endsWith("%")) {
                wrapper.likeLeft("cnkh", this.getCnkh().substring(1));
            } else if (this.getCnkh().endsWith("%")) {
                wrapper.likeRight("cnkh", this.getCnkh().substring(0, this.getCnkh().length() - 1));
            } else {
                wrapper.eq("cnkh", this.getCnkh());
            }
        }
        if (this.getQkmc() != null) {
            if (this.getQkmc().startsWith("%") && this.getQkmc().endsWith("%")) {
                wrapper.like("qkmc", this.getQkmc().substring(1, this.getQkmc().length() - 1));
            } else if (this.getQkmc().startsWith("%") && !this.getQkmc().endsWith("%")) {
                wrapper.likeLeft("qkmc", this.getQkmc().substring(1));
            } else if (this.getQkmc().endsWith("%")) {
                wrapper.likeRight("qkmc", this.getQkmc().substring(0, this.getQkmc().length() - 1));
            } else {
                wrapper.eq("qkmc", this.getQkmc());
            }
        }
        return wrapper;
    }

}
