/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：教学管理
 * 模型名称：优课获奖表
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
import com.zhzx.server.domain.TeachingResultYkhj;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "优课获奖表参数", description = "")
public class TeachingResultYkhjParam implements Serializable {
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
     * 课题名称 和tmg_teaching_result.name一致
     */
    @ApiModelProperty(value = "课题名称 和tmg_teaching_result.name一致")
    private String ktmc;
    /**
     * 比赛名称 可以从sys_label.classify == YKHJ_BSMC选择，也可以录入
     */
    @ApiModelProperty(value = "比赛名称 可以从sys_label.classify == YKHJ_BSMC选择，也可以录入")
    private String bsmc;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<TeachingResultYkhj> toQueryWrapper() {
        QueryWrapper<TeachingResultYkhj> wrapper = Wrappers.<TeachingResultYkhj>query();
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
