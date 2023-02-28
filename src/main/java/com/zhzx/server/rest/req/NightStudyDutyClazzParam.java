/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：晚自习行政值班班级情况表
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
import com.zhzx.server.domain.NightStudyDutyClazz;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "晚自习行政值班班级情况表参数", description = "")
public class NightStudyDutyClazzParam implements Serializable {
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
     * 晚自习行政值班ID day_night_study_duty.id
     */
    @ApiModelProperty(value = "晚自习行政值班ID day_night_study_duty.id")
    private Long nightStudyDutyId;
    /**
     * 班级ID sys_clazz.id
     */
    @ApiModelProperty(value = "班级ID sys_clazz.id")
    private Long clazzId;
    /**
     * 值班老师
     */
    @ApiModelProperty(value = "值班老师")
    private String teacher;
    /**
     * 应到学生数
     */
    @ApiModelProperty(value = "应到学生数")
    private Integer shouldStudentCount;
    /**
     * 实到学生数
     */
    @ApiModelProperty(value = "实到学生数")
    private Integer actualStudentCount;
    /**
     * 得分
     */
    @ApiModelProperty(value = "得分")
    private Integer score;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<NightStudyDutyClazz> toQueryWrapper() {
        QueryWrapper<NightStudyDutyClazz> wrapper = Wrappers.<NightStudyDutyClazz>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getNightStudyDutyId() != null, "night_study_duty_id", this.getNightStudyDutyId());
        wrapper.eq(this.getClazzId() != null, "clazz_id", this.getClazzId());
        if (this.getTeacher() != null) {
            if (this.getTeacher().startsWith("%") && this.getTeacher().endsWith("%")) {
                wrapper.like("teacher", this.getTeacher().substring(1, this.getTeacher().length() - 1));
            } else if (this.getTeacher().startsWith("%") && !this.getTeacher().endsWith("%")) {
                wrapper.likeLeft("teacher", this.getTeacher().substring(1));
            } else if (this.getTeacher().endsWith("%")) {
                wrapper.likeRight("teacher", this.getTeacher().substring(0, this.getTeacher().length() - 1));
            } else {
                wrapper.eq("teacher", this.getTeacher());
            }
        }
        wrapper.eq(this.getShouldStudentCount() != null, "should_student_count", this.getShouldStudentCount());
        wrapper.eq(this.getActualStudentCount() != null, "actual_student_count", this.getActualStudentCount());
        wrapper.eq(this.getScore() != null, "score", this.getScore());
        return wrapper;
    }

}
