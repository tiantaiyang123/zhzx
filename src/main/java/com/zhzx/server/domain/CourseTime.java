/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：课程时间表
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
import com.zhzx.server.domain.Grade;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`sys_course_time`")
@ApiModel(value = "CourseTime", description = "课程时间表")
public class CourseTime extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * 年级ID sys_grade.id
     */
    @TableField(value = "grade_id")
    @ApiModelProperty(value = "年级ID sys_grade.id", required = true)
    private Long gradeId;
    /**
     * 年级ID sys_grade.id
     */
    @ApiModelProperty(value = "年级ID sys_grade.id")
    @TableField(exist = false)
    private Grade grade;
    /**
     * 节次
     */
    @TableField(value = "sort_order")
    @ApiModelProperty(value = "节次", required = true)
    private Integer sortOrder;
    /**
     * 开始时间
     */
    @TableField(value = "start_time")
    @ApiModelProperty(value = "开始时间", required = true)
    private String startTime;
    /**
     * 结束时间
     */
    @TableField(value = "end_time")
    @ApiModelProperty(value = "结束时间", required = true)
    private String endTime;

    /**
     * 设置默认值
     */
    public CourseTime setDefault() {
        if (this.getSortOrder() == null) {
            this.setSortOrder(0);
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
        if (this.getSortOrder() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "节次不能为空！");
            return false;
        }
        if (this.getStartTime() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "开始时间不能为空！");
            return false;
        }
        if (this.getEndTime() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "结束时间不能为空！");
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
