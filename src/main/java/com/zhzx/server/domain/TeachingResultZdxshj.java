/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：教学管理
 * 模型名称：指导学生获奖表
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
@TableName("`tmg_teaching_result_zdxshj`")
@ApiModel(value = "TeachingResultZdxshj", description = "指导学生获奖表")
public class TeachingResultZdxshj extends BaseDomain {
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
     * 获奖情况
     */
    @TableField(value = "hjqk")
    @ApiModelProperty(value = "获奖情况", required = true)
    private String hjqk;
    /**
     * 组织单位
     */
    @TableField(value = "zzdw")
    @ApiModelProperty(value = "组织单位", required = true)
    private String zzdw;
    /**
     * 比赛名称 和tmg_teaching_result.name一致
     */
    @TableField(value = "bsmc")
    @ApiModelProperty(value = "比赛名称 和tmg_teaching_result.name一致", required = true)
    private String bsmc;
    /**
     * 学生姓名
     */
    @TableField(value = "xsxm")
    @ApiModelProperty(value = "学生姓名", required = true)
    private String xsxm;
    /**
     * 学生班级
     */
    @TableField(value = "xsbj")
    @ApiModelProperty(value = "学生班级", required = true)
    private String xsbj;

    /**
     * 设置默认值
     */
    public TeachingResultZdxshj setDefault() {
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
            if (throwException) throw new ApiCode.ApiException(-1, "获奖情况不能为空！");
            return false;
        }
        if (this.getZzdw() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "组织单位不能为空！");
            return false;
        }
        if (this.getBsmc() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "比赛名称 和tmg_teaching_result.name一致不能为空！");
            return false;
        }
        if (this.getXsxm() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "学生姓名不能为空！");
            return false;
        }
        if (this.getXsbj() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "学生班级不能为空！");
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
