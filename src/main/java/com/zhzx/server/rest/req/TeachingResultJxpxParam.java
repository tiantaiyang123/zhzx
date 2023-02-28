/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：教学管理
 * 模型名称：进修培训表
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
import com.zhzx.server.domain.TeachingResultJxpx;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "进修培训表参数", description = "")
public class TeachingResultJxpxParam implements Serializable {
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
     * 培训名称 和tmg_teaching_result.name一致
     */
    @ApiModelProperty(value = "培训名称 和tmg_teaching_result.name一致")
    private String pxmc;
    /**
     * 组织单位
     */
    @ApiModelProperty(value = "组织单位")
    private String zzdw;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<TeachingResultJxpx> toQueryWrapper() {
        QueryWrapper<TeachingResultJxpx> wrapper = Wrappers.<TeachingResultJxpx>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getTeachingResultId() != null, "teaching_result_id", this.getTeachingResultId());
        if (this.getPxmc() != null) {
            if (this.getPxmc().startsWith("%") && this.getPxmc().endsWith("%")) {
                wrapper.like("pxmc", this.getPxmc().substring(1, this.getPxmc().length() - 1));
            } else if (this.getPxmc().startsWith("%") && !this.getPxmc().endsWith("%")) {
                wrapper.likeLeft("pxmc", this.getPxmc().substring(1));
            } else if (this.getPxmc().endsWith("%")) {
                wrapper.likeRight("pxmc", this.getPxmc().substring(0, this.getPxmc().length() - 1));
            } else {
                wrapper.eq("pxmc", this.getPxmc());
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
