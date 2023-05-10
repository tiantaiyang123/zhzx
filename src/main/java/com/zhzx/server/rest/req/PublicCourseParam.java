/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：公开课表
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

import com.zhzx.server.domain.PublicCourse;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "PublicCourseParam", description = "公开课表参数")
public class PublicCourseParam implements Serializable {
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
     * 校区 sys_schoolyard.id
     */
    @ApiModelProperty(value = "校区 sys_schoolyard.id")
    private Long schoolyardId;
    /**
     * 年级ID sys_grade.id
     */
    @ApiModelProperty(value = "年级ID sys_grade.id")
    private Long gradeId;
    /**
     * 课程开始时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "课程开始时间")
    private java.util.Date startTime;
    /**
     * 课程开始时间 下限值(大于等于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "课程开始时间 下限值(大于等于)")
    private java.util.Date startTimeFrom;
    /**
     * 课程开始时间 上限值(小于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "课程开始时间 上限值(小于)")
    private java.util.Date startTimeTo;
    /**
     * 节次
     */
    @ApiModelProperty(value = "节次")
    private String sortOrder;
    /**
     * 课程名称
     */
    @ApiModelProperty(value = "课程名称")
    private String courseName;
    /**
     * 班级ID 根据逗号分割
     */
    @ApiModelProperty(value = "班级ID 根据逗号分割")
    private String clazzId;
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
     * 课程组别名称
     */
    @ApiModelProperty(value = "课程组别名称")
    private String subjectName;
    /**
     * 地址
     */
    @ApiModelProperty(value = "地址")
    private String address;
    /**
     * 类别
     */
    @ApiModelProperty(value = "类别")
    private String classify;
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
    public QueryWrapper<PublicCourse> toQueryWrapper() {
        return this.toQueryWrapper("");
    }

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<PublicCourse> toQueryWrapper(String columnPrefix) {
        QueryWrapper<PublicCourse> wrapper = Wrappers.<PublicCourse>query();
        wrapper.eq(this.getId() != null, columnPrefix + "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, columnPrefix + "id", this.getIdList());
        wrapper.eq(this.getAcademicYearSemesterId() != null, columnPrefix + "academic_year_semester_id", this.getAcademicYearSemesterId());
        wrapper.eq(this.getSchoolyardId() != null, columnPrefix + "schoolyard_id", this.getSchoolyardId());
        wrapper.eq(this.getGradeId() != null, columnPrefix + "grade_id", this.getGradeId());
        wrapper.eq(this.getStartTime() != null, columnPrefix + "start_time", this.getStartTime());
        wrapper.ge(this.getStartTimeFrom() != null, columnPrefix + "start_time", this.getStartTimeFrom());
        wrapper.lt(this.getStartTimeTo() != null, columnPrefix + "start_time", this.getStartTimeTo());
        if (this.getSortOrder() != null) {
            if (this.getSortOrder().startsWith("%") && this.getSortOrder().endsWith("%")) {
                wrapper.like(columnPrefix + "sort_order", this.getSortOrder().substring(1, this.getSortOrder().length() - 1));
            } else if (this.getSortOrder().startsWith("%") && !this.getSortOrder().endsWith("%")) {
                wrapper.likeLeft(columnPrefix + "sort_order", this.getSortOrder().substring(1));
            } else if (this.getSortOrder().endsWith("%")) {
                wrapper.likeRight(columnPrefix + "sort_order", this.getSortOrder().substring(0, this.getSortOrder().length() - 1));
            } else {
                wrapper.eq(columnPrefix + "sort_order", this.getSortOrder());
            }
        }
        if (this.getCourseName() != null) {
            if (this.getCourseName().startsWith("%") && this.getCourseName().endsWith("%")) {
                wrapper.like(columnPrefix + "course_name", this.getCourseName().substring(1, this.getCourseName().length() - 1));
            } else if (this.getCourseName().startsWith("%") && !this.getCourseName().endsWith("%")) {
                wrapper.likeLeft(columnPrefix + "course_name", this.getCourseName().substring(1));
            } else if (this.getCourseName().endsWith("%")) {
                wrapper.likeRight(columnPrefix + "course_name", this.getCourseName().substring(0, this.getCourseName().length() - 1));
            } else {
                wrapper.eq(columnPrefix + "course_name", this.getCourseName());
            }
        }
        if (this.getClazzId() != null) {
            if (this.getClazzId().startsWith("%") && this.getClazzId().endsWith("%")) {
                wrapper.like(columnPrefix + "clazz_id", this.getClazzId().substring(1, this.getClazzId().length() - 1));
            } else if (this.getClazzId().startsWith("%") && !this.getClazzId().endsWith("%")) {
                wrapper.likeLeft(columnPrefix + "clazz_id", this.getClazzId().substring(1));
            } else if (this.getClazzId().endsWith("%")) {
                wrapper.likeRight(columnPrefix + "clazz_id", this.getClazzId().substring(0, this.getClazzId().length() - 1));
            } else {
                wrapper.eq(columnPrefix + "clazz_id", this.getClazzId());
            }
        }
        if (this.getClazzName() != null) {
            if (this.getClazzName().startsWith("%") && this.getClazzName().endsWith("%")) {
                wrapper.like(columnPrefix + "clazz_name", this.getClazzName().substring(1, this.getClazzName().length() - 1));
            } else if (this.getClazzName().startsWith("%") && !this.getClazzName().endsWith("%")) {
                wrapper.likeLeft(columnPrefix + "clazz_name", this.getClazzName().substring(1));
            } else if (this.getClazzName().endsWith("%")) {
                wrapper.likeRight(columnPrefix + "clazz_name", this.getClazzName().substring(0, this.getClazzName().length() - 1));
            } else {
                wrapper.eq(columnPrefix + "clazz_name", this.getClazzName());
            }
        }
        wrapper.eq(this.getTeacherId() != null, columnPrefix + "teacher_id", this.getTeacherId());
        if (this.getTeacherName() != null) {
            if (this.getTeacherName().startsWith("%") && this.getTeacherName().endsWith("%")) {
                wrapper.like(columnPrefix + "teacher_name", this.getTeacherName().substring(1, this.getTeacherName().length() - 1));
            } else if (this.getTeacherName().startsWith("%") && !this.getTeacherName().endsWith("%")) {
                wrapper.likeLeft(columnPrefix + "teacher_name", this.getTeacherName().substring(1));
            } else if (this.getTeacherName().endsWith("%")) {
                wrapper.likeRight(columnPrefix + "teacher_name", this.getTeacherName().substring(0, this.getTeacherName().length() - 1));
            } else {
                wrapper.eq(columnPrefix + "teacher_name", this.getTeacherName());
            }
        }
        if (this.getSubjectName() != null) {
            if (this.getSubjectName().startsWith("%") && this.getSubjectName().endsWith("%")) {
                wrapper.like(columnPrefix + "subject_name", this.getSubjectName().substring(1, this.getSubjectName().length() - 1));
            } else if (this.getSubjectName().startsWith("%") && !this.getSubjectName().endsWith("%")) {
                wrapper.likeLeft(columnPrefix + "subject_name", this.getSubjectName().substring(1));
            } else if (this.getSubjectName().endsWith("%")) {
                wrapper.likeRight(columnPrefix + "subject_name", this.getSubjectName().substring(0, this.getSubjectName().length() - 1));
            } else {
                wrapper.eq(columnPrefix + "subject_name", this.getSubjectName());
            }
        }
        if (this.getAddress() != null) {
            if (this.getAddress().startsWith("%") && this.getAddress().endsWith("%")) {
                wrapper.like(columnPrefix + "address", this.getAddress().substring(1, this.getAddress().length() - 1));
            } else if (this.getAddress().startsWith("%") && !this.getAddress().endsWith("%")) {
                wrapper.likeLeft(columnPrefix + "address", this.getAddress().substring(1));
            } else if (this.getAddress().endsWith("%")) {
                wrapper.likeRight(columnPrefix + "address", this.getAddress().substring(0, this.getAddress().length() - 1));
            } else {
                wrapper.eq(columnPrefix + "address", this.getAddress());
            }
        }
        if (this.getClassify() != null) {
            if (this.getClassify().startsWith("%") && this.getClassify().endsWith("%")) {
                wrapper.like(columnPrefix + "classify", this.getClassify().substring(1, this.getClassify().length() - 1));
            } else if (this.getClassify().startsWith("%") && !this.getClassify().endsWith("%")) {
                wrapper.likeLeft(columnPrefix + "classify", this.getClassify().substring(1));
            } else if (this.getClassify().endsWith("%")) {
                wrapper.likeRight(columnPrefix + "classify", this.getClassify().substring(0, this.getClassify().length() - 1));
            } else {
                wrapper.eq(columnPrefix + "classify", this.getClassify());
            }
        }
        wrapper.eq(this.getEditorId() != null, columnPrefix + "editor_id", this.getEditorId());
        if (this.getEditorName() != null) {
            if (this.getEditorName().startsWith("%") && this.getEditorName().endsWith("%")) {
                wrapper.like(columnPrefix + "editor_name", this.getEditorName().substring(1, this.getEditorName().length() - 1));
            } else if (this.getEditorName().startsWith("%") && !this.getEditorName().endsWith("%")) {
                wrapper.likeLeft(columnPrefix + "editor_name", this.getEditorName().substring(1));
            } else if (this.getEditorName().endsWith("%")) {
                wrapper.likeRight(columnPrefix + "editor_name", this.getEditorName().substring(0, this.getEditorName().length() - 1));
            } else {
                wrapper.eq(columnPrefix + "editor_name", this.getEditorName());
            }
        }
        wrapper.eq(this.getCreateTime() != null, columnPrefix + "create_time", this.getCreateTime());
        wrapper.ge(this.getCreateTimeFrom() != null, columnPrefix + "create_time", this.getCreateTimeFrom());
        wrapper.lt(this.getCreateTimeTo() != null, columnPrefix + "create_time", this.getCreateTimeTo());
        wrapper.eq(this.getUpdateTime() != null, columnPrefix + "update_time", this.getUpdateTime());
        wrapper.ge(this.getUpdateTimeFrom() != null, columnPrefix + "update_time", this.getUpdateTimeFrom());
        wrapper.lt(this.getUpdateTimeTo() != null, columnPrefix + "update_time", this.getUpdateTimeTo());
        return wrapper;
    }

}
