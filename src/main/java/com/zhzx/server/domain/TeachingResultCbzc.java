/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：教学管理
 * 模型名称：参编教材表
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
@TableName("`tmg_teaching_result_cbzc`")
@ApiModel(value = "TeachingResultCbzc", description = "参编教材表")
public class TeachingResultCbzc extends BaseDomain {
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
     * 著作名称 和tmg_teaching_result.name一致
     */
    @TableField(value = "zzmc")
    @ApiModelProperty(value = "著作名称 和tmg_teaching_result.name一致", required = true)
    private String zzmc;
    /**
     * 组织单位
     */
    @TableField(value = "zzdw")
    @ApiModelProperty(value = "组织单位", required = true)
    private String zzdw;
    /**
     * ISBN书号
     */
    @TableField(value = "isbnsh")
    @ApiModelProperty(value = "ISBN书号", required = true)
    private String isbnsh;
    /**
     * 出版社
     */
    @TableField(value = "cbs")
    @ApiModelProperty(value = "出版社", required = true)
    private String cbs;
    /**
     * 完成字数及比例
     */
    @TableField(value = "wczsjbl")
    @ApiModelProperty(value = "完成字数及比例", required = true)
    private String wczsjbl;

    /**
     * 设置默认值
     */
    public TeachingResultCbzc setDefault() {
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
        if (this.getZzmc() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "著作名称 和tmg_teaching_result.name一致不能为空！");
            return false;
        }
        if (this.getZzdw() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "组织单位不能为空！");
            return false;
        }
        if (this.getIsbnsh() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "ISBN书号不能为空！");
            return false;
        }
        if (this.getCbs() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "出版社不能为空！");
            return false;
        }
        if (this.getWczsjbl() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "完成字数及比例不能为空！");
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
