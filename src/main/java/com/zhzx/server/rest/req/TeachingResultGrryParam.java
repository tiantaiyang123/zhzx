/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：教学管理
 * 模型名称：个人荣誉表
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
import com.zhzx.server.domain.TeachingResultGrry;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "个人荣誉表参数", description = "")
public class TeachingResultGrryParam implements Serializable {
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
     * 荣誉称号 和tmg_teaching_result.name一致
     */
    @ApiModelProperty(value = "荣誉称号 和tmg_teaching_result.name一致")
    private String rych;
    /**
     * 发证单位 可以从sys_label.classify == FZDW选择，也可以录入
     */
    @ApiModelProperty(value = "发证单位 可以从sys_label.classify == FZDW选择，也可以录入")
    private String fzdw;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<TeachingResultGrry> toQueryWrapper() {
        QueryWrapper<TeachingResultGrry> wrapper = Wrappers.<TeachingResultGrry>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getTeachingResultId() != null, "teaching_result_id", this.getTeachingResultId());
        if (this.getRych() != null) {
            if (this.getRych().startsWith("%") && this.getRych().endsWith("%")) {
                wrapper.like("rych", this.getRych().substring(1, this.getRych().length() - 1));
            } else if (this.getRych().startsWith("%") && !this.getRych().endsWith("%")) {
                wrapper.likeLeft("rych", this.getRych().substring(1));
            } else if (this.getRych().endsWith("%")) {
                wrapper.likeRight("rych", this.getRych().substring(0, this.getRych().length() - 1));
            } else {
                wrapper.eq("rych", this.getRych());
            }
        }
        if (this.getFzdw() != null) {
            if (this.getFzdw().startsWith("%") && this.getFzdw().endsWith("%")) {
                wrapper.like("fzdw", this.getFzdw().substring(1, this.getFzdw().length() - 1));
            } else if (this.getFzdw().startsWith("%") && !this.getFzdw().endsWith("%")) {
                wrapper.likeLeft("fzdw", this.getFzdw().substring(1));
            } else if (this.getFzdw().endsWith("%")) {
                wrapper.likeRight("fzdw", this.getFzdw().substring(0, this.getFzdw().length() - 1));
            } else {
                wrapper.eq("fzdw", this.getFzdw());
            }
        }
        return wrapper;
    }

}
