/**
 * 项目：中华中学管理平台
 * 模型分组：成绩管理
 * 模型名称：考试发布表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zhzx.server.enums.ExamPrintEnum;
import com.zhzx.server.rest.res.ApiCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`dat_exam_publish`")
@ApiModel(value = "ExamPublish", description = "考试发布表")
public class ExamPublish extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * ID系统自动生成
     */
    @ApiModelProperty(value = "ID系统自动生成")
    @TableField(exist = false)
    private List<ExamPublishRelation> examPublishRelationList;
    /**
     * ID系统自动生成
     */
    @ApiModelProperty(value = "ID系统自动生成")
    @TableField(exist = false)
    private Grade grade;
    /**
     * 年级ID sys_grade.id
     */
    @TableField(value = "grade_id")
    @ApiModelProperty(value = "年级ID sys_grade.id", required = true)
    private Long gradeId;
    /**
     * 发布日期
     */
    @TableField(value = "publish_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "发布日期", required = true)
    private java.util.Date publishTime;
    /**
     * 备注
     */
    @TableField(value = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 填报人
     */
    @TableField(value = "editor")
    @ApiModelProperty(value = "填报人", required = true)
    private String editor;
    /**
     * 是否发布
     */
    @TableField(value = "is_publish")
    @ApiModelProperty(value = "是否发布", required = true)
    private ExamPrintEnum isPublish;
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
    public ExamPublish setDefault() {
        if (this.getIsPublish() == null) {
            this.setIsPublish(ExamPrintEnum.NO);
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
        if (this.getGradeId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "年级ID sys_grade.id不能为空！");
            return false;
        }
        if (this.getPublishTime() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "发布日期不能为空！");
            return false;
        }
        if (this.getEditor() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "填报人不能为空！");
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
