/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：教学管理
 * 模型名称：课题研究表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.domain;

import java.io.Serializable;

import com.zhzx.server.rest.res.ApiCode;
import lombok.*;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zhzx.server.enums.KtyjCyfsEnum;
import com.zhzx.server.enums.KtyjYjztEnum;
import com.zhzx.server.domain.TeachingResult;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`tmg_teaching_result_ktyj`")
@ApiModel(value = "TeachingResultKtyj", description = "课题研究表")
public class TeachingResultKtyj extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * 教学成果ID tmg_teaching_result.id
     */
    @TableField(value = "teaching_result_id")
    @ApiModelProperty(value = "教学成果ID tmg_teaching_result.id", required = true)
    private Long teachingResultId;
    /**
     * 教学成果ID tmg_teaching_result.id
     */
    @ApiModelProperty(value = "教学成果ID tmg_teaching_result.id")
    @TableField(exist = false)
    private TeachingResult teachingResult;
    /**
     * 课题名称 和tmg_teaching_result.name一致
     */
    @TableField(value = "ktmc")
    @ApiModelProperty(value = "课题名称 和tmg_teaching_result.name一致", required = true)
    private String ktmc;
    /**
     * 课题编号
     */
    @TableField(value = "ktbh")
    @ApiModelProperty(value = "课题编号", required = true)
    private String ktbh;
    /**
     * 参与方式
     */
    @TableField(value = "cyfs")
    @ApiModelProperty(value = "参与方式", required = true)
    private KtyjCyfsEnum cyfs;
    /**
     * 研究状态
     */
    @TableField(value = "yjzt")
    @ApiModelProperty(value = "研究状态", required = true)
    private KtyjYjztEnum yjzt;
    /**
     * 组织单位
     */
    @TableField(value = "zzdw")
    @ApiModelProperty(value = "组织单位", required = true)
    private String zzdw;
    /**
     * 开题时间(年-月)
     */
    @TableField(value = "ktsj")
    @ApiModelProperty(value = "开题时间(年-月)", required = true)
    private String ktsj;
    /**
     * 结题时间(年-月)
     */
    @TableField(value = "jtsj")
    @ApiModelProperty(value = "结题时间(年-月)", required = true)
    private String jtsj;

    /**
     * 设置默认值
     */
    public TeachingResultKtyj setDefault() {
        if (this.getCyfs() == null) {
            this.setCyfs(KtyjCyfsEnum.CY);
        }
        if (this.getYjzt() == null) {
            this.setYjzt(KtyjYjztEnum.YJZ);
        }
        return this;
    }

    /**
     * 数据验证
     */
    public Boolean validate(Boolean throwException) {
        if (this.getTeachingResultId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "教学成果ID tmg_teaching_result.id不能为空！");
            return false;
        }
        if (this.getKtmc() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "课题名称 和tmg_teaching_result.name一致不能为空！");
            return false;
        }
        if (this.getKtbh() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "课题编号不能为空！");
            return false;
        }
        if (this.getCyfs() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "参与方式不能为空！");
            return false;
        }
        if (this.getYjzt() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "研究状态不能为空！");
            return false;
        }
        if (this.getZzdw() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "组织单位不能为空！");
            return false;
        }
        if (this.getKtsj() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "开题时间(年-月)不能为空！");
            return false;
        }
        if (this.getJtsj() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "结题时间(年-月)不能为空！");
            return false;
        }
        return true;
    }

    /**
     * 数据验证
     */
    public Boolean validate() {
        return this.validate(false);
    }

}
