/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：教学管理
 * 模型名称：参编教材表
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
import com.zhzx.server.domain.TeachingResultCbzc;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "参编教材表参数", description = "")
public class TeachingResultCbzcParam implements Serializable {
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
     * 著作名称 和tmg_teaching_result.name一致
     */
    @ApiModelProperty(value = "著作名称 和tmg_teaching_result.name一致")
    private String zzmc;
    /**
     * 组织单位
     */
    @ApiModelProperty(value = "组织单位")
    private String zzdw;
    /**
     * ISBN书号
     */
    @ApiModelProperty(value = "ISBN书号")
    private String isbnsh;
    /**
     * 出版社
     */
    @ApiModelProperty(value = "出版社")
    private String cbs;
    /**
     * 完成字数及比例
     */
    @ApiModelProperty(value = "完成字数及比例")
    private String wczsjbl;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<TeachingResultCbzc> toQueryWrapper() {
        QueryWrapper<TeachingResultCbzc> wrapper = Wrappers.<TeachingResultCbzc>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getTeachingResultId() != null, "teaching_result_id", this.getTeachingResultId());
        if (this.getZzmc() != null) {
            if (this.getZzmc().startsWith("%") && this.getZzmc().endsWith("%")) {
                wrapper.like("zzmc", this.getZzmc().substring(1, this.getZzmc().length() - 1));
            } else if (this.getZzmc().startsWith("%") && !this.getZzmc().endsWith("%")) {
                wrapper.likeLeft("zzmc", this.getZzmc().substring(1));
            } else if (this.getZzmc().endsWith("%")) {
                wrapper.likeRight("zzmc", this.getZzmc().substring(0, this.getZzmc().length() - 1));
            } else {
                wrapper.eq("zzmc", this.getZzmc());
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
        if (this.getIsbnsh() != null) {
            if (this.getIsbnsh().startsWith("%") && this.getIsbnsh().endsWith("%")) {
                wrapper.like("isbnsh", this.getIsbnsh().substring(1, this.getIsbnsh().length() - 1));
            } else if (this.getIsbnsh().startsWith("%") && !this.getIsbnsh().endsWith("%")) {
                wrapper.likeLeft("isbnsh", this.getIsbnsh().substring(1));
            } else if (this.getIsbnsh().endsWith("%")) {
                wrapper.likeRight("isbnsh", this.getIsbnsh().substring(0, this.getIsbnsh().length() - 1));
            } else {
                wrapper.eq("isbnsh", this.getIsbnsh());
            }
        }
        if (this.getCbs() != null) {
            if (this.getCbs().startsWith("%") && this.getCbs().endsWith("%")) {
                wrapper.like("cbs", this.getCbs().substring(1, this.getCbs().length() - 1));
            } else if (this.getCbs().startsWith("%") && !this.getCbs().endsWith("%")) {
                wrapper.likeLeft("cbs", this.getCbs().substring(1));
            } else if (this.getCbs().endsWith("%")) {
                wrapper.likeRight("cbs", this.getCbs().substring(0, this.getCbs().length() - 1));
            } else {
                wrapper.eq("cbs", this.getCbs());
            }
        }
        if (this.getWczsjbl() != null) {
            if (this.getWczsjbl().startsWith("%") && this.getWczsjbl().endsWith("%")) {
                wrapper.like("wczsjbl", this.getWczsjbl().substring(1, this.getWczsjbl().length() - 1));
            } else if (this.getWczsjbl().startsWith("%") && !this.getWczsjbl().endsWith("%")) {
                wrapper.likeLeft("wczsjbl", this.getWczsjbl().substring(1));
            } else if (this.getWczsjbl().endsWith("%")) {
                wrapper.likeRight("wczsjbl", this.getWczsjbl().substring(0, this.getWczsjbl().length() - 1));
            } else {
                wrapper.eq("wczsjbl", this.getWczsjbl());
            }
        }
        return wrapper;
    }

}
