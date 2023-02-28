/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：教学管理
 * 模型名称：个人竞赛获奖表
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
import com.zhzx.server.domain.TeachingResultGrjshj;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "个人竞赛获奖表参数", description = "")
public class TeachingResultGrjshjParam implements Serializable {
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
     * 组织单位
     */
    @ApiModelProperty(value = "组织单位")
    private String zzdw;
    /**
     * 获奖情况
     */
    @ApiModelProperty(value = "获奖情况")
    private String hjqk;
    /**
     * 比赛名称 和tmg_teaching_result.name一致 可以从sys_label.classify == GRJS_BSMC选择，也可以录入
     */
    @ApiModelProperty(value = "比赛名称 和tmg_teaching_result.name一致 可以从sys_label.classify == GRJS_BSMC选择，也可以录入")
    private String bsmc;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<TeachingResultGrjshj> toQueryWrapper() {
        QueryWrapper<TeachingResultGrjshj> wrapper = Wrappers.<TeachingResultGrjshj>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getTeachingResultId() != null, "teaching_result_id", this.getTeachingResultId());
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
        if (this.getBsmc() != null) {
            if (this.getBsmc().startsWith("%") && this.getBsmc().endsWith("%")) {
                wrapper.like("bsmc", this.getBsmc().substring(1, this.getBsmc().length() - 1));
            } else if (this.getBsmc().startsWith("%") && !this.getBsmc().endsWith("%")) {
                wrapper.likeLeft("bsmc", this.getBsmc().substring(1));
            } else if (this.getBsmc().endsWith("%")) {
                wrapper.likeRight("bsmc", this.getBsmc().substring(0, this.getBsmc().length() - 1));
            } else {
                wrapper.eq("bsmc", this.getBsmc());
            }
        }
        return wrapper;
    }

}
