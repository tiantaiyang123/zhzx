/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：教学管理
 * 模型名称：教学成果参评申报历史表
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
import com.zhzx.server.domain.TeachingResultDeclarationClassify;
import com.zhzx.server.domain.TeachingResultDeclarationClassify;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`tmg_teaching_result_history`")
@ApiModel(value = "TeachingResultHistory", description = "教学成果参评申报历史表")
public class TeachingResultHistory extends BaseDomain {
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
     * 申报类别ID1 sys_teaching_result_declaration_classify.id
     */
    @TableField(value = "classify_id1")
    @ApiModelProperty(value = "申报类别ID1 sys_teaching_result_declaration_classify.id", required = true)
    private Long classifyId1;
    /**
     * 申报类别ID1 sys_teaching_result_declaration_classify.id
     */
    @ApiModelProperty(value = "申报类别ID1 sys_teaching_result_declaration_classify.id")
    @TableField(exist = false)
    private TeachingResultDeclarationClassify classify1;
    /**
     * 申报类别ID2 sys_teaching_result_declaration_classify.id
     */
    @TableField(value = "classify_id2")
    @ApiModelProperty(value = "申报类别ID2 sys_teaching_result_declaration_classify.id", required = true)
    private Long classifyId2;
    /**
     * 申报类别ID2 sys_teaching_result_declaration_classify.id
     */
    @ApiModelProperty(value = "申报类别ID2 sys_teaching_result_declaration_classify.id")
    @TableField(exist = false)
    private TeachingResultDeclarationClassify classify2;

    /**
     * 设置默认值
     */
    public TeachingResultHistory setDefault() {
        if (this.getClassifyId1() == null) {
            this.setClassifyId1(0L);
        }
        if (this.getClassifyId2() == null) {
            this.setClassifyId2(0L);
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
        if (this.getClassifyId1() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "申报类别ID1 sys_teaching_result_declaration_classify.id不能为空！");
            return false;
        }
        if (this.getClassifyId2() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "申报类别ID2 sys_teaching_result_declaration_classify.id不能为空！");
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
