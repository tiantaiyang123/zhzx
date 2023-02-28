/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：班级表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest.req;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhzx.server.domain.Clazz;
import com.zhzx.server.enums.ClazzNatureEnum;
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
@ApiModel(value = "班级表参数", description = "")
public class ClazzParam implements Serializable {
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
     * 校区 sys_schoolyard.id
     */
    @ApiModelProperty(value = "年级 sys_schoolyard.id")
    private Long schoolyardId;
    /**
     * 学年学期 sys_academic_year_semester.id
     */
    @ApiModelProperty(value = "学年学期 sys_academic_year_semester.id")
    private Long academicYearSemesterId;
    /**
     * 年级 sys_grade.id
     */
    @ApiModelProperty(value = "年级 sys_grade.id")
    private Long gradeId;
    /**
     * 名称 sys_label.classify=CLAZZ
     */
    @ApiModelProperty(value = "名称 sys_label.classify=CLAZZ")
    private String name;
    /**
     * 学生人数
     */
    @ApiModelProperty(value = "学生人数")
    private Integer studentCount;
    /**
     * 入学年份
     */
    @ApiModelProperty(value = "入学年份")
    private Integer startYear;
    /**
     * 班主任 sys_staff.name
     */
    @ApiModelProperty(value = "班主任 sys_staff.name")
    private String headTeacher;
    /**
     * 班级性质
     */
    @ApiModelProperty(value = "班级性质")
    private ClazzNatureEnum clazzNature;
    /**
     * 班级性质 IN值List
     */
    @ApiModelProperty(value = "班级性质 IN值List")
    private List<String> clazzNatureList;
    /**
     * 班级分科 多选 用[,]分割 sys_subject.id
     */
    @ApiModelProperty(value = "班级分科 多选 用[,]分割 sys_subject.id")
    private String clazzDivision;
    /**
     * 学科水平科目 多选 用[,]分割 sys_subject.id
     */
    @ApiModelProperty(value = "学科水平科目 多选 用[,]分割 sys_subject.id")
    private String subjectLevel;
    /**
     * 班级层次
     */
    @ApiModelProperty(value = "班级层次")
    private String clazzLevel;
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
    public QueryWrapper<Clazz> toQueryWrapper() {
        QueryWrapper<Clazz> wrapper = Wrappers.<Clazz>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getAcademicYearSemesterId() != null, "academic_year_semester_id", this.getAcademicYearSemesterId());
        wrapper.eq(this.getGradeId() != null, "grade_id", this.getGradeId());
        wrapper.eq(this.getSchoolyardId() != null, "schoolyard_id", this.getSchoolyardId());
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
        wrapper.eq(this.getStudentCount() != null, "student_count", this.getStudentCount());
        wrapper.eq(this.getStartYear() != null, "start_year", this.getStartYear());
        if (this.getHeadTeacher() != null) {
            if (this.getHeadTeacher().startsWith("%") && this.getHeadTeacher().endsWith("%")) {
                wrapper.like("head_teacher", this.getHeadTeacher().substring(1, this.getHeadTeacher().length() - 1));
            } else if (this.getHeadTeacher().startsWith("%") && !this.getHeadTeacher().endsWith("%")) {
                wrapper.likeLeft("head_teacher", this.getHeadTeacher().substring(1));
            } else if (this.getHeadTeacher().endsWith("%")) {
                wrapper.likeRight("head_teacher", this.getHeadTeacher().substring(0, this.getHeadTeacher().length() - 1));
            } else {
                wrapper.eq("head_teacher", this.getHeadTeacher());
            }
        }
        wrapper.eq(this.getClazzNature() != null, "clazz_nature", this.getClazzNature());
        wrapper.in(this.getClazzNatureList() != null && this.getClazzNatureList().size() > 0, "clazz_nature", this.getClazzNatureList());
        if (this.getClazzDivision() != null) {
            if (this.getClazzDivision().startsWith("%") && this.getClazzDivision().endsWith("%")) {
                wrapper.like("clazz_division", this.getClazzDivision().substring(1, this.getClazzDivision().length() - 1));
            } else if (this.getClazzDivision().startsWith("%") && !this.getClazzDivision().endsWith("%")) {
                wrapper.likeLeft("clazz_division", this.getClazzDivision().substring(1));
            } else if (this.getClazzDivision().endsWith("%")) {
                wrapper.likeRight("clazz_division", this.getClazzDivision().substring(0, this.getClazzDivision().length() - 1));
            } else {
                wrapper.eq("clazz_division", this.getClazzDivision());
            }
        }
        if (this.getSubjectLevel() != null) {
            if (this.getSubjectLevel().startsWith("%") && this.getSubjectLevel().endsWith("%")) {
                wrapper.like("subject_level", this.getSubjectLevel().substring(1, this.getSubjectLevel().length() - 1));
            } else if (this.getSubjectLevel().startsWith("%") && !this.getSubjectLevel().endsWith("%")) {
                wrapper.likeLeft("subject_level", this.getSubjectLevel().substring(1));
            } else if (this.getSubjectLevel().endsWith("%")) {
                wrapper.likeRight("subject_level", this.getSubjectLevel().substring(0, this.getSubjectLevel().length() - 1));
            } else {
                wrapper.eq("subject_level", this.getSubjectLevel());
            }
        }
        if (this.getClazzLevel() != null) {
            if (this.getClazzLevel().startsWith("%") && this.getClazzLevel().endsWith("%")) {
                wrapper.like("clazz_level", this.getClazzLevel().substring(1, this.getClazzLevel().length() - 1));
            } else if (this.getClazzLevel().startsWith("%") && !this.getClazzLevel().endsWith("%")) {
                wrapper.likeLeft("clazz_level", this.getClazzLevel().substring(1));
            } else if (this.getClazzLevel().endsWith("%")) {
                wrapper.likeRight("clazz_level", this.getClazzLevel().substring(0, this.getClazzLevel().length() - 1));
            } else {
                wrapper.eq("clazz_level", this.getClazzLevel());
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
