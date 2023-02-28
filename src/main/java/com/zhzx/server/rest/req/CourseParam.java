/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：课程表
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
import com.zhzx.server.domain.Course;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "课程表参数", description = "")
public class CourseParam implements Serializable {
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
     * 学年学期ID sys_academic_year_semester.id
     */
    @ApiModelProperty(value = "学年学期ID sys_academic_year_semester.id")
    private Long academicYearSemesterId;
    /**
     * 年级ID sys_grade.id
     */
    @ApiModelProperty(value = "年级ID sys_grade.id")
    private Long gradeId;
    /**
     * 星期
     */
    @ApiModelProperty(value = "星期")
    private Integer week;
    /**
     * 节次
     */
    @ApiModelProperty(value = "节次")
    private Integer sortOrder;
    /**
     * 课程名称
     */
    @ApiModelProperty(value = "课程名称")
    private String courseName;
    /**
     * 班级ID sys_clazz.id
     */
    @ApiModelProperty(value = "班级ID sys_clazz.id")
    private Long clazzId;
    /**
     * 班级
     */
    @ApiModelProperty(value = "班级")
    private String clazzName;
    /**
     * 老师ID sys_staff.id
     */
    @ApiModelProperty(value = "老师ID sys_staff.id")
    private Long teacherId;
    /**
     * 老师
     */
    @ApiModelProperty(value = "老师")
    private String teacherName;
    /**
     * 操作人ID
     */
    @ApiModelProperty(value = "操作人ID")
    private Long editorId;
    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private String editorName;
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
    public QueryWrapper<Course> toQueryWrapper() {
        QueryWrapper<Course> wrapper = Wrappers.<Course>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getAcademicYearSemesterId() != null, "academic_year_semester_id", this.getAcademicYearSemesterId());
        wrapper.eq(this.getGradeId() != null, "grade_id", this.getGradeId());
        wrapper.eq(this.getWeek() != null, "week", this.getWeek());
        wrapper.eq(this.getSortOrder() != null, "sort_order", this.getSortOrder());
        if (this.getCourseName() != null) {
            if (this.getCourseName().startsWith("%") && this.getCourseName().endsWith("%")) {
                wrapper.like("course_name", this.getCourseName().substring(1, this.getCourseName().length() - 1));
            } else if (this.getCourseName().startsWith("%") && !this.getCourseName().endsWith("%")) {
                wrapper.likeLeft("course_name", this.getCourseName().substring(1));
            } else if (this.getCourseName().endsWith("%")) {
                wrapper.likeRight("course_name", this.getCourseName().substring(0, this.getCourseName().length() - 1));
            } else {
                wrapper.eq("course_name", this.getCourseName());
            }
        }
        wrapper.eq(this.getClazzId() != null, "clazz_id", this.getClazzId());
        if (this.getClazzName() != null) {
            if (this.getClazzName().startsWith("%") && this.getClazzName().endsWith("%")) {
                wrapper.like("clazz_name", this.getClazzName().substring(1, this.getClazzName().length() - 1));
            } else if (this.getClazzName().startsWith("%") && !this.getClazzName().endsWith("%")) {
                wrapper.likeLeft("clazz_name", this.getClazzName().substring(1));
            } else if (this.getClazzName().endsWith("%")) {
                wrapper.likeRight("clazz_name", this.getClazzName().substring(0, this.getClazzName().length() - 1));
            } else {
                wrapper.eq("clazz_name", this.getClazzName());
            }
        }
        wrapper.eq(this.getTeacherId() != null, "teacher_id", this.getTeacherId());
        if (this.getTeacherName() != null) {
            if (this.getTeacherName().startsWith("%") && this.getTeacherName().endsWith("%")) {
                wrapper.like("teacher_name", this.getTeacherName().substring(1, this.getTeacherName().length() - 1));
            } else if (this.getTeacherName().startsWith("%") && !this.getTeacherName().endsWith("%")) {
                wrapper.likeLeft("teacher_name", this.getTeacherName().substring(1));
            } else if (this.getTeacherName().endsWith("%")) {
                wrapper.likeRight("teacher_name", this.getTeacherName().substring(0, this.getTeacherName().length() - 1));
            } else {
                wrapper.eq("teacher_name", this.getTeacherName());
            }
        }
        wrapper.eq(this.getEditorId() != null, "editor_id", this.getEditorId());
        if (this.getEditorName() != null) {
            if (this.getEditorName().startsWith("%") && this.getEditorName().endsWith("%")) {
                wrapper.like("editor_name", this.getEditorName().substring(1, this.getEditorName().length() - 1));
            } else if (this.getEditorName().startsWith("%") && !this.getEditorName().endsWith("%")) {
                wrapper.likeLeft("editor_name", this.getEditorName().substring(1));
            } else if (this.getEditorName().endsWith("%")) {
                wrapper.likeRight("editor_name", this.getEditorName().substring(0, this.getEditorName().length() - 1));
            } else {
                wrapper.eq("editor_name", this.getEditorName());
            }
        }
        wrapper.eq(this.getCreateTime() != null, "create_time", this.getCreateTime());
        wrapper.ge(this.getCreateTimeFrom() != null, "create_time", this.getCreateTimeFrom());
        wrapper.lt(this.getCreateTimeTo() != null, "create_time", this.getCreateTimeTo());
        wrapper.eq(this.getUpdateTime() != null, "update_time", this.getUpdateTime());
        wrapper.ge(this.getUpdateTimeFrom() != null, "update_time", this.getUpdateTimeFrom());
        wrapper.lt(this.getUpdateTimeTo() != null, "update_time", this.getUpdateTimeTo());
        return wrapper;
    }

}
