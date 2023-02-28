/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：教学管理
 * 模型名称：课题研究表
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
import com.zhzx.server.enums.KtyjCyfsEnum;
import com.zhzx.server.enums.KtyjYjztEnum;
import com.zhzx.server.domain.TeachingResultKtyj;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "课题研究表参数", description = "")
public class TeachingResultKtyjParam implements Serializable {
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
     * 课题编号
     */
    @ApiModelProperty(value = "课题编号")
    private String ktbh;
    /**
     * 参与方式
     */
    @ApiModelProperty(value = "参与方式")
    private KtyjCyfsEnum cyfs;
    /**
     * 参与方式 IN值List
     */
    @ApiModelProperty(value = "参与方式 IN值List")
    private List<String> cyfsList;
    /**
     * 研究状态
     */
    @ApiModelProperty(value = "研究状态")
    private KtyjYjztEnum yjzt;
    /**
     * 研究状态 IN值List
     */
    @ApiModelProperty(value = "研究状态 IN值List")
    private List<String> yjztList;
    /**
     * 组织单位
     */
    @ApiModelProperty(value = "组织单位")
    private String zzdw;
    /**
     * 开题时间(年-月)
     */
    @ApiModelProperty(value = "开题时间(年-月)")
    private String ktsj;
    /**
     * 结题时间(年-月)
     */
    @ApiModelProperty(value = "结题时间(年-月)")
    private String jtsj;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<TeachingResultKtyj> toQueryWrapper() {
        QueryWrapper<TeachingResultKtyj> wrapper = Wrappers.<TeachingResultKtyj>query();
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
        if (this.getKtbh() != null) {
            if (this.getKtbh().startsWith("%") && this.getKtbh().endsWith("%")) {
                wrapper.like("ktbh", this.getKtbh().substring(1, this.getKtbh().length() - 1));
            } else if (this.getKtbh().startsWith("%") && !this.getKtbh().endsWith("%")) {
                wrapper.likeLeft("ktbh", this.getKtbh().substring(1));
            } else if (this.getKtbh().endsWith("%")) {
                wrapper.likeRight("ktbh", this.getKtbh().substring(0, this.getKtbh().length() - 1));
            } else {
                wrapper.eq("ktbh", this.getKtbh());
            }
        }
        wrapper.eq(this.getCyfs() != null, "cyfs", this.getCyfs());
        wrapper.in(this.getCyfsList() != null && this.getCyfsList().size() > 0, "cyfs", this.getCyfsList());
        wrapper.eq(this.getYjzt() != null, "yjzt", this.getYjzt());
        wrapper.in(this.getYjztList() != null && this.getYjztList().size() > 0, "yjzt", this.getYjztList());
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
        if (this.getKtsj() != null) {
            if (this.getKtsj().startsWith("%") && this.getKtsj().endsWith("%")) {
                wrapper.like("ktsj", this.getKtsj().substring(1, this.getKtsj().length() - 1));
            } else if (this.getKtsj().startsWith("%") && !this.getKtsj().endsWith("%")) {
                wrapper.likeLeft("ktsj", this.getKtsj().substring(1));
            } else if (this.getKtsj().endsWith("%")) {
                wrapper.likeRight("ktsj", this.getKtsj().substring(0, this.getKtsj().length() - 1));
            } else {
                wrapper.eq("ktsj", this.getKtsj());
            }
        }
        if (this.getJtsj() != null) {
            if (this.getJtsj().startsWith("%") && this.getJtsj().endsWith("%")) {
                wrapper.like("jtsj", this.getJtsj().substring(1, this.getJtsj().length() - 1));
            } else if (this.getJtsj().startsWith("%") && !this.getJtsj().endsWith("%")) {
                wrapper.likeLeft("jtsj", this.getJtsj().substring(1));
            } else if (this.getJtsj().endsWith("%")) {
                wrapper.likeRight("jtsj", this.getJtsj().substring(0, this.getJtsj().length() - 1));
            } else {
                wrapper.eq("jtsj", this.getJtsj());
            }
        }
        return wrapper;
    }

}
