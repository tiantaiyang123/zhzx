/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：年级表
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
import com.zhzx.server.domain.Schoolyard;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`sys_grade`")
@ApiModel(value = "Grade", description = "年级表")
public class Grade extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * 校区ID sys_schoolyard.id
     */
    @TableField(value = "schoolyard_id")
    @ApiModelProperty(value = "校区ID sys_schoolyard.id", required = true)
    private Long schoolyardId;
    /**
     * 校区ID sys_schoolyard.id
     */
    @ApiModelProperty(value = "校区ID sys_schoolyard.id")
    @TableField(exist = false)
    private Schoolyard schoolyard;
    /**
     * 名称
     */
    @TableField(value = "name")
    @ApiModelProperty(value = "名称", required = true)
    private String name;
    /**
     * 年级组长 多选 用[,]分割 sys_staff.name
     */
    @TableField(value = "grade_leader")
    @ApiModelProperty(value = "年级组长 多选 用[,]分割 sys_staff.name")
    private String gradeLeader;
    /**
     * 教研组长 多选 用[,]分割 sys_staff.name
     */
    @TableField(value = "team_leader")
    @ApiModelProperty(value = "教研组长 多选 用[,]分割 sys_staff.name")
    private String teamLeader;
    /**
     * 学业等级A等比例
     */
    @TableField(value = "academy_ratio_a")
    @ApiModelProperty(value = "学业等级A等比例", required = true)
    private Long academyRatioA;
    /**
     * 学业等级B等比例
     */
    @TableField(value = "academy_ratio_b")
    @ApiModelProperty(value = "学业等级B等比例", required = true)
    private Long academyRatioB;
    /**
     * 学业等级C等比例
     */
    @TableField(value = "academy_ratio_c")
    @ApiModelProperty(value = "学业等级C等比例", required = true)
    private Long academyRatioC;
    /**
     * 学业等级D等比例
     */
    @TableField(value = "academy_ratio_d")
    @ApiModelProperty(value = "学业等级D等比例", required = true)
    private Long academyRatioD;
    /**
     * 学业等级E等比例
     */
    @TableField(value = "academy_ratio_e")
    @ApiModelProperty(value = "学业等级E等比例", required = true)
    private Long academyRatioE;
    /**
     * 
     */
    @TableField(value = "create_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "", required = true)
    private java.util.Date createTime;
    /**
     * 
     */
    @TableField(value = "update_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "", required = true)
    private java.util.Date updateTime;

    /**
     * 设置默认值
     */
    public Grade setDefault() {
        if (this.getSchoolyardId() == null) {
            this.setSchoolyardId(0L);
        }
        if (this.getAcademyRatioA() == null) {
            this.setAcademyRatioA(0L);
        }
        if (this.getAcademyRatioB() == null) {
            this.setAcademyRatioB(0L);
        }
        if (this.getAcademyRatioC() == null) {
            this.setAcademyRatioC(0L);
        }
        if (this.getAcademyRatioD() == null) {
            this.setAcademyRatioD(0L);
        }
        if (this.getAcademyRatioE() == null) {
            this.setAcademyRatioE(0L);
        }
        if (this.getCreateTime() == null) {
            this.setCreateTime(new java.util.Date());
        }
        if (this.getUpdateTime() == null) {
            this.setUpdateTime(new java.util.Date());
        }
        return this;
    }

    /**
     * 数据验证
     */
    public Boolean validate(Boolean throwException) {
        if (this.getSchoolyardId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "校区ID sys_schoolyard.id不能为空！");
            return false;
        }
        if (this.getName() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "名称不能为空！");
            return false;
        }
        if (this.getAcademyRatioA() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "学业等级A等比例不能为空！");
            return false;
        }
        if (this.getAcademyRatioB() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "学业等级B等比例不能为空！");
            return false;
        }
        if (this.getAcademyRatioC() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "学业等级C等比例不能为空！");
            return false;
        }
        if (this.getAcademyRatioD() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "学业等级D等比例不能为空！");
            return false;
        }
        if (this.getAcademyRatioE() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "学业等级E等比例不能为空！");
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
