/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：教学管理
 * 模型名称：班级荣誉表
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
import com.zhzx.server.domain.TeachingResultBjry;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "班级荣誉表参数", description = "")
public class TeachingResultBjryParam implements Serializable {
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
     * 获奖名称
     */
    @ApiModelProperty(value = "获奖名称")
    private String hjmc;
    /**
     * 班级ID sys_clazz.id
     */
    @ApiModelProperty(value = "班级ID sys_clazz.id")
    private Long clazzId;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<TeachingResultBjry> toQueryWrapper() {
        QueryWrapper<TeachingResultBjry> wrapper = Wrappers.<TeachingResultBjry>query();
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
        wrapper.eq(this.getClazzId() != null, "clazz_id", this.getClazzId());
        return wrapper;
    }

}
