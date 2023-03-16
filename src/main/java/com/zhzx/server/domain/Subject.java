/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：科目表
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
import com.zhzx.server.enums.YesNoEnum;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`sys_subject`")
@ApiModel(value = "Subject", description = "科目表")
public class Subject extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * 科目名称
     */
    @TableField(value = "name")
    @ApiModelProperty(value = "科目名称", required = true)
    private String name;
    /**
     * 是否为赋分科目
     */
    @TableField(value = "has_weight")
    @ApiModelProperty(value = "是否为赋分科目", required = true)
    private YesNoEnum hasWeight;
    /**
     * 是否为主要科目
     */
    @TableField(value = "is_main")
    @ApiModelProperty(value = "是否为主要科目", required = true)
    private YesNoEnum isMain;
    /**
     * 是否录入成绩
     */
    @TableField(value = "is_record_score")
    @ApiModelProperty(value = "是否录入成绩", required = true)
    private YesNoEnum isRecordScore;
    /**
     * 总分
     */
    @TableField(value = "max_score")
    @ApiModelProperty(value = "总分", required = true)
    private Long maxScore;
    /**
     * 学科别名
     */
    @TableField(value = "subject_alias")
    @ApiModelProperty(value = "学科别名", required = true)
    private String subjectAlias;
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
    public Subject setDefault() {
        if (this.getHasWeight() == null) {
            this.setHasWeight(YesNoEnum.NO);
        }
        if (this.getIsMain() == null) {
            this.setIsMain(YesNoEnum.YES);
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
        if (this.getName() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "科目名称不能为空！");
            return false;
        }
        if (this.getHasWeight() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "是否为赋分科目不能为空！");
            return false;
        }
        if (this.getIsMain() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "是否为主要科目不能为空！");
            return false;
        }
        if (this.getMaxScore() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "总分不能为空！");
            return false;
        }
        if (this.getSubjectAlias() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "学科别名不能为空！");
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
