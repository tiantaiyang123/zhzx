/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：课程时间表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest.req;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;
import com.zhzx.server.domain.CourseTime;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "课程时间表参数", description = "")
public class CourseTimeParam implements Serializable {
    /**
     * ID系统自动生成
     */
    @ApiModelProperty(value = "ID系统自动生成")
    private Long id;
    /**
     * ID系统自动生成 IN值List
     */
    @ApiModelProperty(value = "ID系统自动生成 IN值List")
    private List<Long> idList;
    /**
     * 年级ID sys_grade.id
     */
    @ApiModelProperty(value = "年级ID sys_grade.id")
    private Long gradeId;
    /**
     * 节次
     */
    @ApiModelProperty(value = "节次")
    private Integer sortOrder;
    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private String startTime;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private String endTime;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<CourseTime> toQueryWrapper() {
        QueryWrapper<CourseTime> wrapper = Wrappers.<CourseTime>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getGradeId() != null, "grade_id", this.getGradeId());
        wrapper.eq(this.getSortOrder() != null, "sort_order", this.getSortOrder());
        if (this.getStartTime() != null) {
            if (this.getStartTime().startsWith("%") && this.getStartTime().endsWith("%")) {
                wrapper.like("start_time", this.getStartTime().substring(1, this.getStartTime().length() - 1));
            } else if (this.getStartTime().startsWith("%") && !this.getStartTime().endsWith("%")) {
                wrapper.likeLeft("start_time", this.getStartTime().substring(1));
            } else if (this.getStartTime().endsWith("%")) {
                wrapper.likeRight("start_time", this.getStartTime().substring(0, this.getStartTime().length() - 1));
            } else {
                wrapper.eq("start_time", this.getStartTime());
            }
        }
        if (this.getEndTime() != null) {
            if (this.getEndTime().startsWith("%") && this.getEndTime().endsWith("%")) {
                wrapper.like("end_time", this.getEndTime().substring(1, this.getEndTime().length() - 1));
            } else if (this.getEndTime().startsWith("%") && !this.getEndTime().endsWith("%")) {
                wrapper.likeLeft("end_time", this.getEndTime().substring(1));
            } else if (this.getEndTime().endsWith("%")) {
                wrapper.likeRight("end_time", this.getEndTime().substring(0, this.getEndTime().length() - 1));
            } else {
                wrapper.eq("end_time", this.getEndTime());
            }
        }
        return wrapper;
    }

}
