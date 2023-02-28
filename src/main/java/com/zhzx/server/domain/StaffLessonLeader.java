/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：备课组长表
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
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.domain.AcademicYearSemester;
import com.zhzx.server.domain.Grade;
import com.zhzx.server.domain.Staff;
import com.zhzx.server.domain.Subject;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`sys_staff_lesson_leader`")
@ApiModel(value = "StaffLessonLeader", description = "备课组长表")
public class StaffLessonLeader extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * 学年学期 sys_academic_year_semester.id
     */
    @TableField(value = "academic_year_semester_id")
    @ApiModelProperty(value = "学年学期 sys_academic_year_semester.id", required = true)
    private Long academicYearSemesterId;
    /**
     * 学年学期 sys_academic_year_semester.id
     */
    @ApiModelProperty(value = "学年学期 sys_academic_year_semester.id")
    @TableField(exist = false)
    private AcademicYearSemester academicYearSemester;
    /**
     * 年级 sys_grade.id
     */
    @TableField(value = "grade_id")
    @ApiModelProperty(value = "年级 sys_grade.id", required = true)
    private Long gradeId;
    /**
     * 年级 sys_grade.id
     */
    @ApiModelProperty(value = "年级 sys_grade.id")
    @TableField(exist = false)
    private Grade grade;
    /**
     * 教师ID sys_staff.id
     */
    @TableField(value = "staff_id")
    @ApiModelProperty(value = "教师ID sys_staff.id", required = true)
    private Long staffId;
    /**
     * 教师ID sys_staff.id
     */
    @ApiModelProperty(value = "教师ID sys_staff.id")
    @TableField(exist = false)
    private Staff staff;
    /**
     * 科目ID sys_subject.id
     */
    @TableField(value = "subject_id")
    @ApiModelProperty(value = "科目ID sys_subject.id", required = true)
    private Long subjectId;
    /**
     * 科目ID sys_subject.id
     */
    @ApiModelProperty(value = "科目ID sys_subject.id")
    @TableField(exist = false)
    private Subject subject;
    /**
     * 是否当值
     */
    @TableField(value = "is_current")
    @ApiModelProperty(value = "是否当值", required = true)
    private YesNoEnum isCurrent;

    /**
     * 设置默认值
     */
    public StaffLessonLeader setDefault() {
        return this;
    }

    /**
     * 数据验证
     */
    public Boolean validate(Boolean throwException) {
        if (this.getAcademicYearSemesterId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "学年学期 sys_academic_year_semester.id不能为空！");
            return false;
        }
        if (this.getGradeId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "年级 sys_grade.id不能为空！");
            return false;
        }
        if (this.getStaffId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "教师ID sys_staff.id不能为空！");
            return false;
        }
        if (this.getSubjectId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "科目ID sys_subject.id不能为空！");
            return false;
        }
        if (this.getIsCurrent() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "是否当值不能为空！");
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
