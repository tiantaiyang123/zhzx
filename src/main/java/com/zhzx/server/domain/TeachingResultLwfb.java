/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：教学管理
 * 模型名称：论文发表表
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
import com.zhzx.server.domain.TeachingResult;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`tmg_teaching_result_lwfb`")
@ApiModel(value = "TeachingResultLwfb", description = "论文发表表")
public class TeachingResultLwfb extends BaseDomain {
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
     * 论文标题 和tmg_teaching_result.name一致
     */
    @TableField(value = "lwbt")
    @ApiModelProperty(value = "论文标题 和tmg_teaching_result.name一致", required = true)
    private String lwbt;
    /**
     * ISSN刊号
     */
    @TableField(value = "issnkh")
    @ApiModelProperty(value = "ISSN刊号", required = true)
    private String issnkh;
    /**
     * ISSN书号
     */
    @TableField(value = "issnsh")
    @ApiModelProperty(value = "ISSN书号", required = true)
    private String issnsh;
    /**
     * CN刊号
     */
    @TableField(value = "cnkh")
    @ApiModelProperty(value = "CN刊号", required = true)
    private String cnkh;
    /**
     * 期刊名称
     */
    @TableField(value = "qkmc")
    @ApiModelProperty(value = "期刊名称", required = true)
    private String qkmc;

    /**
     * 设置默认值
     */
    public TeachingResultLwfb setDefault() {
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
        if (this.getLwbt() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "论文标题 和tmg_teaching_result.name一致不能为空！");
            return false;
        }
        if (this.getIssnkh() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "ISSN刊号不能为空！");
            return false;
        }
        if (this.getIssnsh() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "ISSN书号不能为空！");
            return false;
        }
        if (this.getCnkh() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "CN刊号不能为空！");
            return false;
        }
        if (this.getQkmc() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "期刊名称不能为空！");
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
