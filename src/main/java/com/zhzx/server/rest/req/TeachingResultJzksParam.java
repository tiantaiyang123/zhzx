/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：教学管理
 * 模型名称：讲座开设表
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
import com.zhzx.server.domain.TeachingResultJzks;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "讲座开设表参数", description = "")
public class TeachingResultJzksParam implements Serializable {
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
     * 讲座名称 和tmg_teaching_result.name一致
     */
    @ApiModelProperty(value = "讲座名称 和tmg_teaching_result.name一致")
    private String jzmc;
    /**
     * 组织单位
     */
    @ApiModelProperty(value = "组织单位")
    private String zzdw;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<TeachingResultJzks> toQueryWrapper() {
        QueryWrapper<TeachingResultJzks> wrapper = Wrappers.<TeachingResultJzks>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getTeachingResultId() != null, "teaching_result_id", this.getTeachingResultId());
        if (this.getJzmc() != null) {
            if (this.getJzmc().startsWith("%") && this.getJzmc().endsWith("%")) {
                wrapper.like("jzmc", this.getJzmc().substring(1, this.getJzmc().length() - 1));
            } else if (this.getJzmc().startsWith("%") && !this.getJzmc().endsWith("%")) {
                wrapper.likeLeft("jzmc", this.getJzmc().substring(1));
            } else if (this.getJzmc().endsWith("%")) {
                wrapper.likeRight("jzmc", this.getJzmc().substring(0, this.getJzmc().length() - 1));
            } else {
                wrapper.eq("jzmc", this.getJzmc());
            }
        }
        if (this.getZzdw() != null) {
            if (this.getZzdw().startsWith("%") && this.getZzdw().endsWith("%")) {
                wrapper.like("zzdw", this.getZzdw().substring(1, this.getZzdw().length() - 1));
            } else if (this.getZzdw().startsWith("%") && !this.getZzdw().endsWith("%")) {
                wrapper.likeLeft("zzdw", this.getZzdw().substring(1));
            } else if (this.getZzdw().endsWith("%")) {
                wrapper.likeRight("zzdw", this.getZzdw().substring(0, this.getZzdw().length() - 1));
            } else {
                wrapper.eq("zzdw", this.getZzdw());
            }
        }
        return wrapper;
    }

}
