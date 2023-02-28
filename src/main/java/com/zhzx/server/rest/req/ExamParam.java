/**
 * 项目：中华中学管理平台
 * 模型分组：成绩管理
 * 模型名称：考试表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest.req;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhzx.server.domain.Exam;
import com.zhzx.server.enums.YesNoEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.List;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "考试表参数", description = "")
public class ExamParam implements Serializable {
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
     * 校区ID sys_schoolyard.id
     */
    @ApiModelProperty(value = "校区ID sys_schoolyard.id")
    private Long schoolyardId;
    /**
     * 学年学期ID sys_academic_year_semester.id
     */
    @ApiModelProperty(value = "学年学期ID sys_academic_year_semester.id")
    private Long academicYearSemesterId;
    /**
     * 学年学期ID sys_academic_year_semester.id
     */
    @ApiModelProperty(value = "学年学期列表")
    private List<Long> academicYearSemesterIdList;
    /**
     * 年级ID sys_grade.id
     */
    @ApiModelProperty(value = "年级ID sys_grade.id")
    private Long gradeId;
    /**
     * 目标模板ID dat_exam_goal_template.id
     */
    @ApiModelProperty(value = "目标模板ID dat_exam_goal_template.id")
    private Long examGoalTemplateId;
    /**
     * 考试名称
     */
    @ApiModelProperty(value = "考试名称")
    private String name;
    /**
     * 考试开始日期
     */
    @ApiModelProperty(value = "考试开始日期")
    private String examStartDate;
    /**
     * 考试结束日期
     */
    @ApiModelProperty(value = "考试结束日期")
    private String examEndDate;
    /**
     * 学业等级更新日期，对应grade表同名字段
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "学业等级更新日期，对应grade表同名字段")
    private java.util.Date academyRatioUpdateTime;
    /**
     * 学业等级更新日期，对应grade表同名字段 下限值(大于等于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "学业等级更新日期，对应grade表同名字段 下限值(大于等于)")
    private java.util.Date academyRatioUpdateTimeFrom;
    /**
     * 学业等级更新日期，对应grade表同名字段 上限值(小于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "学业等级更新日期，对应grade表同名字段 上限值(小于)")
    private java.util.Date academyRatioUpdateTimeTo;
    /**
     * 是否发布
     */
    @ApiModelProperty(value = "是否发布")
    private YesNoEnum isPublish;

    @ApiModelProperty(value = "是否教师可见", required = true)
    private YesNoEnum isTeacherSeen;

    /**
     * 是否发布 IN值List
     */
    @ApiModelProperty(value = "是否发布 IN值List")
    private List<String> isPublishList;
    /**
     * 
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "")
    private java.util.Date createTime;
    /**
     *  下限值(大于等于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = " 下限值(大于等于)")
    private java.util.Date createTimeFrom;
    /**
     *  上限值(小于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = " 上限值(小于)")
    private java.util.Date createTimeTo;
    /**
     * 
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "")
    private java.util.Date updateTime;
    /**
     *  下限值(大于等于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = " 下限值(大于等于)")
    private java.util.Date updateTimeFrom;
    /**
     *  上限值(小于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = " 上限值(小于)")
    private java.util.Date updateTimeTo;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<Exam> toQueryWrapper() {
        QueryWrapper<Exam> wrapper = Wrappers.<Exam>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getSchoolyardId() != null, "schoolyard_id", this.getSchoolyardId());
        wrapper.eq(this.getAcademicYearSemesterId() != null, "academic_year_semester_id", this.getAcademicYearSemesterId());
        wrapper.eq(this.getGradeId() != null, "grade_id", this.getGradeId());
        wrapper.eq(this.getExamGoalTemplateId() != null, "exam_goal_template_id", this.getExamGoalTemplateId());
        wrapper.in(this.getAcademicYearSemesterIdList() != null, "academic_year_semester_id", academicYearSemesterIdList);
        if (this.getName() != null) {
            if (this.getName().startsWith("%") && this.getName().endsWith("%")) {
                wrapper.like("name", this.getName().substring(1, this.getName().length() - 1));
            } else if (this.getName().startsWith("%") && !this.getName().endsWith("%")) {
                wrapper.likeLeft("name", this.getName().substring(1));
            } else if (this.getName().endsWith("%")) {
                wrapper.likeRight("name", this.getName().substring(0, this.getName().length() - 1));
            } else {
                wrapper.eq("name", this.getName());
            }
        }
        wrapper.eq(this.getExamStartDate() != null, "exam_start_date", this.getExamStartDate());
        wrapper.eq(this.getExamEndDate() != null, "exam_end_date", this.getExamEndDate());
        wrapper.eq(this.getAcademyRatioUpdateTime() != null, "academy_ratio_update_time", this.getAcademyRatioUpdateTime());
        wrapper.ge(this.getAcademyRatioUpdateTimeFrom() != null, "academy_ratio_update_time", this.getAcademyRatioUpdateTimeFrom());
        wrapper.lt(this.getAcademyRatioUpdateTimeTo() != null, "academy_ratio_update_time", this.getAcademyRatioUpdateTimeTo());
        wrapper.eq(this.getIsPublish() != null, "is_publish", this.getIsPublish());
        wrapper.in(this.getIsPublishList() != null && this.getIsPublishList().size() > 0, "is_publish", this.getIsPublishList());
        wrapper.eq(this.getIsTeacherSeen() != null, "is_teacher_seen", this.getIsTeacherSeen());
        wrapper.eq(this.getCreateTime() != null, "create_time", this.getCreateTime());
        wrapper.ge(this.getCreateTimeFrom() != null, "create_time", this.getCreateTimeFrom());
        wrapper.lt(this.getCreateTimeTo() != null, "create_time", this.getCreateTimeTo());
        wrapper.eq(this.getUpdateTime() != null, "update_time", this.getUpdateTime());
        wrapper.ge(this.getUpdateTimeFrom() != null, "update_time", this.getUpdateTimeFrom());
        wrapper.lt(this.getUpdateTimeTo() != null, "update_time", this.getUpdateTimeTo());
        return wrapper;
    }

}
