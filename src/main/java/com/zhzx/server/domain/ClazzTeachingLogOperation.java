/**
 * 项目：中华中学管理平台
 * 模型分组：学生管理
 * 模型名称：班级教学日志作业量反馈表
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
import com.zhzx.server.enums.OperationDurationEnum;
import com.zhzx.server.domain.Subject;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`std_clazz_teaching_log_operation`")
@ApiModel(value = "ClazzTeachingLogOperation", description = "班级教学日志作业量反馈表")
public class ClazzTeachingLogOperation extends BaseDomain {
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
     * 时长
     */
    @TableField(value = "duration")
    @ApiModelProperty(value = "时长")
    private OperationDurationEnum duration;
    /**
     * 作业日期
     */
    @TableField(value = "operation_date")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "作业日期", required = true)
    private java.util.Date operationDate;
    /**
     * 作业耗时(分钟)
     */
    @TableField(value = "operation_time")
    @ApiModelProperty(value = "作业耗时(分钟)", required = true)
    private Integer operationTime;

    /**
     * 设置默认值
     */
    public ClazzTeachingLogOperation setDefault() {
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
        if (this.getOperationDate() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "作业日期不能为空！");
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
