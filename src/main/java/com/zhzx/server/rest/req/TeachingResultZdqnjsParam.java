/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：教学管理
 * 模型名称：指导青年教师表
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
import com.zhzx.server.domain.TeachingResultZdqnjs;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "指导青年教师表参数", description = "")
public class TeachingResultZdqnjsParam implements Serializable {
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
     * 获奖情况 和tmg_teaching_result.name一致
     */
    @ApiModelProperty(value = "获奖情况 和tmg_teaching_result.name一致")
    private String hjqk;
    /**
     * 开始时间(年-月)
     */
    @ApiModelProperty(value = "开始时间(年-月)")
    private String kssj;
    /**
     * 结束时间(年-月)
     */
    @ApiModelProperty(value = "结束时间(年-月)")
    private String jssj;
    /**
     * 教师姓名
     */
    @ApiModelProperty(value = "教师姓名")
    private String jsmc;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<TeachingResultZdqnjs> toQueryWrapper() {
        QueryWrapper<TeachingResultZdqnjs> wrapper = Wrappers.<TeachingResultZdqnjs>query();
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
        if (this.getKssj() != null) {
            if (this.getKssj().startsWith("%") && this.getKssj().endsWith("%")) {
                wrapper.like("kssj", this.getKssj().substring(1, this.getKssj().length() - 1));
            } else if (this.getKssj().startsWith("%") && !this.getKssj().endsWith("%")) {
                wrapper.likeLeft("kssj", this.getKssj().substring(1));
            } else if (this.getKssj().endsWith("%")) {
                wrapper.likeRight("kssj", this.getKssj().substring(0, this.getKssj().length() - 1));
            } else {
                wrapper.eq("kssj", this.getKssj());
            }
        }
        if (this.getJssj() != null) {
            if (this.getJssj().startsWith("%") && this.getJssj().endsWith("%")) {
                wrapper.like("jssj", this.getJssj().substring(1, this.getJssj().length() - 1));
            } else if (this.getJssj().startsWith("%") && !this.getJssj().endsWith("%")) {
                wrapper.likeLeft("jssj", this.getJssj().substring(1));
            } else if (this.getJssj().endsWith("%")) {
                wrapper.likeRight("jssj", this.getJssj().substring(0, this.getJssj().length() - 1));
            } else {
                wrapper.eq("jssj", this.getJssj());
            }
        }
        if (this.getJsmc() != null) {
            if (this.getJsmc().startsWith("%") && this.getJsmc().endsWith("%")) {
                wrapper.like("jsmc", this.getJsmc().substring(1, this.getJsmc().length() - 1));
            } else if (this.getJsmc().startsWith("%") && !this.getJsmc().endsWith("%")) {
                wrapper.likeLeft("jsmc", this.getJsmc().substring(1));
            } else if (this.getJsmc().endsWith("%")) {
                wrapper.likeRight("jsmc", this.getJsmc().substring(0, this.getJsmc().length() - 1));
            } else {
                wrapper.eq("jsmc", this.getJsmc());
            }
        }
        return wrapper;
    }

}
