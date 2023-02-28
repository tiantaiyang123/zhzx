/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：教学管理
 * 模型名称：指导学生获奖表
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
import com.zhzx.server.domain.TeachingResultZdxshj;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "指导学生获奖表参数", description = "")
public class TeachingResultZdxshjParam implements Serializable {
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
     * 比赛名称 和tmg_teaching_result.name一致
     */
    @ApiModelProperty(value = "比赛名称 和tmg_teaching_result.name一致")
    private String bsmc;
    /**
     * 学生姓名
     */
    @ApiModelProperty(value = "学生姓名")
    private String xsxm;
    /**
     * 学生班级
     */
    @ApiModelProperty(value = "学生班级")
    private String xsbj;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<TeachingResultZdxshj> toQueryWrapper() {
        QueryWrapper<TeachingResultZdxshj> wrapper = Wrappers.<TeachingResultZdxshj>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getTeachingResultId() != null, "teaching_result_id", this.getTeachingResultId());
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
        if (this.getXsxm() != null) {
            if (this.getXsxm().startsWith("%") && this.getXsxm().endsWith("%")) {
                wrapper.like("xsxm", this.getXsxm().substring(1, this.getXsxm().length() - 1));
            } else if (this.getXsxm().startsWith("%") && !this.getXsxm().endsWith("%")) {
                wrapper.likeLeft("xsxm", this.getXsxm().substring(1));
            } else if (this.getXsxm().endsWith("%")) {
                wrapper.likeRight("xsxm", this.getXsxm().substring(0, this.getXsxm().length() - 1));
            } else {
                wrapper.eq("xsxm", this.getXsxm());
            }
        }
        if (this.getXsbj() != null) {
            if (this.getXsbj().startsWith("%") && this.getXsbj().endsWith("%")) {
                wrapper.like("xsbj", this.getXsbj().substring(1, this.getXsbj().length() - 1));
            } else if (this.getXsbj().startsWith("%") && !this.getXsbj().endsWith("%")) {
                wrapper.likeLeft("xsbj", this.getXsbj().substring(1));
            } else if (this.getXsbj().endsWith("%")) {
                wrapper.likeRight("xsbj", this.getXsbj().substring(0, this.getXsbj().length() - 1));
            } else {
                wrapper.eq("xsbj", this.getXsbj());
            }
        }
        return wrapper;
    }

}
