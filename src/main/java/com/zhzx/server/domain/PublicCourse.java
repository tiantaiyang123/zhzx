/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：公开课表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
 */

package com.zhzx.server.domain;


import com.zhzx.server.rest.res.ApiCode;
import lombok.*;

import com.baomidou.mybatisplus.annotation.*;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`sys_public_course`")
@ApiModel(value = "PublicCourse", description = "公开课表")
public class PublicCourse extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * 学年学期ID sys_academic_year_semester.id
     */
    @TableField(value = "academic_year_semester_id")
    @ApiModelProperty(value = "学年学期ID sys_academic_year_semester.id", required = true)
    private Long academicYearSemesterId;
    /**
     * 学年学期ID sys_academic_year_semester.id
     */
    @ApiModelProperty(value = "学年学期ID sys_academic_year_semester.id")
    @TableField(exist = false)
    private AcademicYearSemester academicYearSemester;
    /**
     * 校区 sys_schoolyard.id
     */
    @TableField(value = "schoolyard_id")
    @ApiModelProperty(value = "校区 sys_schoolyard.id", required = true)
    private Long schoolyardId;
    /**
     * 校区 sys_schoolyard.id
     */
    @ApiModelProperty(value = "校区 sys_schoolyard.id")
    @TableField(exist = false)
    private Schoolyard schoolyard;
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
     * 课程开始时间
     */
    @TableField(value = "start_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "课程开始时间", required = true)
    private java.util.Date startTime;
    /**
     * 节次
     */
    @TableField(value = "sort_order")
    @ApiModelProperty(value = "节次", required = true)
    private String sortOrder;
    /**
     * 课程名称
     */
    @TableField(value = "course_name")
    @ApiModelProperty(value = "课程名称", required = true)
    private String courseName;
    /**
     * 班级ID 根据逗号分割
     */
    @TableField(value = "clazz_id")
    @ApiModelProperty(value = "班级ID 根据逗号分割")
    private String clazzId;
    /**
     * 班级
     */
    @TableField(value = "clazz_name")
    @ApiModelProperty(value = "班级")
    private String clazzName;
    /**
     * 老师ID sys_staff.id
     */
    @TableField(value = "teacher_id")
    @ApiModelProperty(value = "老师ID sys_staff.id", required = true)
    private Long teacherId;
    /**
     * 老师ID sys_staff.id
     */
    @ApiModelProperty(value = "老师ID sys_staff.id")
    @TableField(exist = false)
    private Staff teacher;
    /**
     * 老师
     */
    @TableField(value = "teacher_name")
    @ApiModelProperty(value = "老师")
    private String teacherName;
    /**
     * 课程组别名称
     */
    @TableField(value = "subject_name")
    @ApiModelProperty(value = "课程组别名称")
    private String subjectName;
    /**
     * 地址
     */
    @TableField(value = "address")
    @ApiModelProperty(value = "地址", required = true)
    private String address;
    /**
     * 类别
     */
    @TableField(value = "classify")
    @ApiModelProperty(value = "类别")
    private String classify;
    /**
     * 操作人ID
     */
    @TableField(value = "editor_id")
    @ApiModelProperty(value = "操作人ID", required = true)
    private Long editorId;
    /**
     * 操作人
     */
    @TableField(value = "editor_name")
    @ApiModelProperty(value = "操作人", required = true)
    private String editorName;
    /**
     * 
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "", required = true)
    private java.util.Date createTime;
    /**
     * 
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "", required = true)
    private java.util.Date updateTime;

    /**
     * 设置默认值
     */
    public PublicCourse setDefault() {
        if (this.getSortOrder() == null) {
            this.setSortOrder("0");
        }
        if (this.getEditorId() == null) {
            this.setEditorId(1L);
        }
        if (this.getEditorName() == null) {
            this.setEditorName("admin");
        }
        if (this.getCreateTime() == null) {
            this.setCreateTime(new java.util.Date());
        }
        if (this.getUpdateTime() == null) {
            this.setUpdateTime(new java.util.Date());
        }
        return this;
    }

    /**
     * 数据验证
     */
    public Boolean validate(Boolean throwException) {
        if (this.getAcademicYearSemesterId() == null) {
            if (throwException) {
                throw new ApiCode.ApiException(-1, "学年学期ID sys_academic_year_semester.id不能为空！");
            }
            return false;
        }
        if (this.getSchoolyardId() == null) {
            if (throwException) {
                throw new ApiCode.ApiException(-1, "校区 sys_schoolyard.id不能为空！");
            }
            return false;
        }
        if (this.getGradeId() == null) {
            if (throwException) {
                throw new ApiCode.ApiException(-1, "年级ID sys_grade.id不能为空！");
            }
            return false;
        }
        if (this.getStartTime() == null) {
            if (throwException) {
                throw new ApiCode.ApiException(-1, "课程开始时间不能为空！");
            }
            return false;
        }
        if (this.getSortOrder() == null) {
            if (throwException) {
                throw new ApiCode.ApiException(-1, "节次不能为空！");
            }
            return false;
        }
        if (this.getCourseName() == null) {
            if (throwException) {
                throw new ApiCode.ApiException(-1, "课程名称不能为空！");
            }
            return false;
        }
        if (this.getTeacherId() == null) {
            if (throwException) {
                throw new ApiCode.ApiException(-1, "老师ID sys_staff.id不能为空！");
            }
            return false;
        }
        if (this.getAddress() == null) {
            if (throwException) {
                throw new ApiCode.ApiException(-1, "地址不能为空！");
            }
            return false;
        }
        if (this.getEditorId() == null) {
            if (throwException) {
                throw new ApiCode.ApiException(-1, "操作人ID不能为空！");
            }
            return false;
        }
        if (this.getEditorName() == null) {
            if (throwException) {
                throw new ApiCode.ApiException(-1, "操作人不能为空！");
            }
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
