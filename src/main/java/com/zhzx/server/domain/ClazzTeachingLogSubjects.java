/**
 * 项目：中华中学管理平台
 * 模型分组：学生管理
 * 模型名称：班级教学日志科目表
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
import com.zhzx.server.enums.LogRulesEnum;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.enums.LogStateEnum;
import com.zhzx.server.domain.Subject;
import com.zhzx.server.domain.Staff;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`std_clazz_teaching_log_subjects`")
@ApiModel(value = "ClazzTeachingLogSubjects", description = "班级教学日志科目表")
public class ClazzTeachingLogSubjects extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * 班级教学日志ID std_clazz_teaching_log.id
     */
    @TableField(value = "clazz_teaching_log_id")
    @ApiModelProperty(value = "班级教学日志ID std_clazz_teaching_log.id", required = true)
    private Long clazzTeachingLogId;
    /**
     * 节次
     */
    @TableField(value = "sort_order")
    @ApiModelProperty(value = "节次", required = true)
    private Integer sortOrder;
    /**
     * 科目ID sys_subject.id
     */
    @TableField(value = "subject_id")
    @ApiModelProperty(value = "科目ID sys_subject.id")
    private Long subjectId;
    /**
     * 科目ID sys_subject.id
     */
    @ApiModelProperty(value = "科目ID sys_subject.id")
    @TableField(exist = false)
    private Subject subject;
    /**
     * 任课老师ID sys_staff.id
     */
    @TableField(value = "teacher_id")
    @ApiModelProperty(value = "任课老师ID sys_staff.id")
    private Long teacherId;
    /**
     * 任课老师ID sys_staff.id
     */
    @ApiModelProperty(value = "任课老师ID sys_staff.id")
    @TableField(exist = false)
    private Staff teacher;
    /**
     * 课堂纪律
     */
    @TableField(value = "rules")
    @ApiModelProperty(value = "课堂纪律")
    private LogRulesEnum rules;
    /**
     * 是否准时下课
     */
    @TableField(value = "is_on_time")
    @ApiModelProperty(value = "是否准时下课")
    private YesNoEnum isOnTime;
    /**
     * 状态
     */
    @TableField(value = "state")
    @ApiModelProperty(value = "状态")
    private LogStateEnum state;
    /**
     * 特殊情况
     */
    @TableField(value = "remark_subject")
    @ApiModelProperty(value = "特殊情况")
    private String remarkSubject;
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
    public ClazzTeachingLogSubjects setDefault() {
        if (this.getSortOrder() == null) {
            this.setSortOrder(0);
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
        if (this.getClazzTeachingLogId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "班级教学日志ID std_clazz_teaching_log.id不能为空！");
            return false;
        }
        if (this.getSortOrder() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "节次不能为空！");
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
