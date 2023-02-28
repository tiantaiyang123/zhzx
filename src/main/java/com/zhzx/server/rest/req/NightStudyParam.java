/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：晚自习表
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
import com.zhzx.server.domain.NightStudy;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "晚自习表参数", description = "")
public class NightStudyParam implements Serializable {
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
     * 教师值班班级ID day_teacher_duty_clazz.id
     */
    @ApiModelProperty(value = "教师值班班级ID day_teacher_duty_clazz.id")
    private Long teacherDutyClazzId;
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
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<NightStudy> toQueryWrapper() {
        QueryWrapper<NightStudy> wrapper = Wrappers.<NightStudy>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getTeacherDutyClazzId() != null, "teacher_duty_clazz_id", this.getTeacherDutyClazzId());
        wrapper.eq(this.getShouldStudentCount() != null, "should_student_count", this.getShouldStudentCount());
        wrapper.eq(this.getActualStudentCount() != null, "actual_student_count", this.getActualStudentCount());
        return wrapper;
    }

}
