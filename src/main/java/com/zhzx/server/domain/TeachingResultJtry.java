/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：教学管理
 * 模型名称：集体荣誉表
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
@TableName("`tmg_teaching_result_jtry`")
@ApiModel(value = "TeachingResultJtry", description = "集体荣誉表")
public class TeachingResultJtry extends BaseDomain {
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
     * 荣誉称号
     */
    @TableField(value = "rych")
    @ApiModelProperty(value = "荣誉称号", required = true)
    private String rych;
    /**
     * 获奖名称 和tmg_teaching_result.name一致
     */
    @TableField(value = "hjmc")
    @ApiModelProperty(value = "获奖名称 和tmg_teaching_result.name一致", required = true)
    private String hjmc;
    /**
     * 发证单位 可以从sys_label.classify == FZDW选择，也可以录入
     */
    @TableField(value = "fzdw")
    @ApiModelProperty(value = "发证单位 可以从sys_label.classify == FZDW选择，也可以录入", required = true)
    private String fzdw;

    /**
     * 设置默认值
     */
    public TeachingResultJtry setDefault() {
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
        if (this.getRych() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "荣誉称号不能为空！");
            return false;
        }
        if (this.getHjmc() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "获奖名称 和tmg_teaching_result.name一致不能为空！");
            return false;
        }
        if (this.getFzdw() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "发证单位 可以从sys_label.classify == FZDW选择，也可以录入不能为空！");
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
