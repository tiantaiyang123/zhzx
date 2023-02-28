/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：教学管理
 * 模型名称：指导青年教师表
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
@TableName("`tmg_teaching_result_zdqnjs`")
@ApiModel(value = "TeachingResultZdqnjs", description = "指导青年教师表")
public class TeachingResultZdqnjs extends BaseDomain {
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
     * 获奖情况 和tmg_teaching_result.name一致
     */
    @TableField(value = "hjqk")
    @ApiModelProperty(value = "获奖情况 和tmg_teaching_result.name一致", required = true)
    private String hjqk;
    /**
     * 开始时间(年-月)
     */
    @TableField(value = "kssj")
    @ApiModelProperty(value = "开始时间(年-月)", required = true)
    private String kssj;
    /**
     * 结束时间(年-月)
     */
    @TableField(value = "jssj")
    @ApiModelProperty(value = "结束时间(年-月)", required = true)
    private String jssj;
    /**
     * 教师姓名
     */
    @TableField(value = "jsmc")
    @ApiModelProperty(value = "教师姓名", required = true)
    private String jsmc;

    /**
     * 设置默认值
     */
    public TeachingResultZdqnjs setDefault() {
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
        if (this.getHjqk() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "获奖情况 和tmg_teaching_result.name一致不能为空！");
            return false;
        }
        if (this.getKssj() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "开始时间(年-月)不能为空！");
            return false;
        }
        if (this.getJssj() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "结束时间(年-月)不能为空！");
            return false;
        }
        if (this.getJsmc() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "教师姓名不能为空！");
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
