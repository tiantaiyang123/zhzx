/**
 * 项目：中华中学管理平台
 * 模型分组：教学管理
 * 模型名称：微课获奖表
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
import com.zhzx.server.domain.TeachingResultWkhj;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "微课获奖表参数", description = "")
public class TeachingResultWkhjParam implements Serializable {
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
     * 微课名称 和tmg_teaching_result.name一致
     */
    @ApiModelProperty(value = "微课名称 和tmg_teaching_result.name一致")
    private String wkmc;
    /**
     * 活动名称
     */
    @ApiModelProperty(value = "活动名称")
    private String hdmc;
    /**
     * 获奖名次
     */
    @ApiModelProperty(value = "获奖名次")
    private String hjmc;
    /**
     * 组织单位
     */
    @ApiModelProperty(value = "组织单位")
    private String zzdw;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<TeachingResultWkhj> toQueryWrapper() {
        QueryWrapper<TeachingResultWkhj> wrapper = Wrappers.<TeachingResultWkhj>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getTeachingResultId() != null, "teaching_result_id", this.getTeachingResultId());
        if (this.getWkmc() != null) {
            if (this.getWkmc().startsWith("%") && this.getWkmc().endsWith("%")) {
                wrapper.like("wkmc", this.getWkmc().substring(1, this.getWkmc().length() - 1));
            } else if (this.getWkmc().startsWith("%") && !this.getWkmc().endsWith("%")) {
                wrapper.likeLeft("wkmc", this.getWkmc().substring(1));
            } else if (this.getWkmc().endsWith("%")) {
                wrapper.likeRight("wkmc", this.getWkmc().substring(0, this.getWkmc().length() - 1));
            } else {
                wrapper.eq("wkmc", this.getWkmc());
            }
        }
        if (this.getHdmc() != null) {
            if (this.getHdmc().startsWith("%") && this.getHdmc().endsWith("%")) {
                wrapper.like("hdmc", this.getHdmc().substring(1, this.getHdmc().length() - 1));
            } else if (this.getHdmc().startsWith("%") && !this.getHdmc().endsWith("%")) {
                wrapper.likeLeft("hdmc", this.getHdmc().substring(1));
            } else if (this.getHdmc().endsWith("%")) {
                wrapper.likeRight("hdmc", this.getHdmc().substring(0, this.getHdmc().length() - 1));
            } else {
                wrapper.eq("hdmc", this.getHdmc());
            }
        }
        if (this.getHjmc() != null) {
            if (this.getHjmc().startsWith("%") && this.getHjmc().endsWith("%")) {
                wrapper.like("hjmc", this.getHjmc().substring(1, this.getHjmc().length() - 1));
            } else if (this.getHjmc().startsWith("%") && !this.getHjmc().endsWith("%")) {
                wrapper.likeLeft("hjmc", this.getHjmc().substring(1));
            } else if (this.getHjmc().endsWith("%")) {
                wrapper.likeRight("hjmc", this.getHjmc().substring(0, this.getHjmc().length() - 1));
            } else {
                wrapper.eq("hjmc", this.getHjmc());
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
