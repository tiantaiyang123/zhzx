/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：教学管理
 * 模型名称：论文评比获奖表
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
import com.zhzx.server.domain.TeachingResultLwpbhj;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "论文评比获奖表参数", description = "")
public class TeachingResultLwpbhjParam implements Serializable {
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
     * 获奖情况
     */
    @ApiModelProperty(value = "获奖情况")
    private String hjqk;
    /**
     * 组织单位
     */
    @ApiModelProperty(value = "组织单位")
    private String zzdw;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<TeachingResultLwpbhj> toQueryWrapper() {
        QueryWrapper<TeachingResultLwpbhj> wrapper = Wrappers.<TeachingResultLwpbhj>query();
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
        if (this.getHjqk() != null) {
            if (this.getHjqk().startsWith("%") && this.getHjqk().endsWith("%")) {
                wrapper.like("hjqk", this.getHjqk().substring(1, this.getHjqk().length() - 1));
            } else if (this.getHjqk().startsWith("%") && !this.getHjqk().endsWith("%")) {
                wrapper.likeLeft("hjqk", this.getHjqk().substring(1));
            } else if (this.getHjqk().endsWith("%")) {
                wrapper.likeRight("hjqk", this.getHjqk().substring(0, this.getHjqk().length() - 1));
            } else {
                wrapper.eq("hjqk", this.getHjqk());
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
