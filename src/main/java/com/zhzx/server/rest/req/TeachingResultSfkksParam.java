/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：教学管理
 * 模型名称：示范课、研究课开设表
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
import com.zhzx.server.domain.TeachingResultSfkks;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "示范课、研究课开设表参数", description = "")
public class TeachingResultSfkksParam implements Serializable {
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
     * 课题名称 和tmg_teaching_result.name一致
     */
    @ApiModelProperty(value = "课题名称 和tmg_teaching_result.name一致")
    private String ktmc;
    /**
     * 组织单位
     */
    @ApiModelProperty(value = "组织单位")
    private String zzdw;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<TeachingResultSfkks> toQueryWrapper() {
        QueryWrapper<TeachingResultSfkks> wrapper = Wrappers.<TeachingResultSfkks>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getTeachingResultId() != null, "teaching_result_id", this.getTeachingResultId());
        if (this.getKtmc() != null) {
            if (this.getKtmc().startsWith("%") && this.getKtmc().endsWith("%")) {
                wrapper.like("ktmc", this.getKtmc().substring(1, this.getKtmc().length() - 1));
            } else if (this.getKtmc().startsWith("%") && !this.getKtmc().endsWith("%")) {
                wrapper.likeLeft("ktmc", this.getKtmc().substring(1));
            } else if (this.getKtmc().endsWith("%")) {
                wrapper.likeRight("ktmc", this.getKtmc().substring(0, this.getKtmc().length() - 1));
            } else {
                wrapper.eq("ktmc", this.getKtmc());
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
