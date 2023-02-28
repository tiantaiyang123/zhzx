/**
 * 项目：中华中学管理平台
 * 模型分组：教学管理
 * 模型名称：教学成果表
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
import com.zhzx.server.enums.SemesterEnum;
import com.zhzx.server.enums.TeachingResultLevelEnum;
import com.zhzx.server.enums.TeachingResultStateEnum;
import com.zhzx.server.domain.Staff;
import com.zhzx.server.domain.TeachingResultClassify;
import com.zhzx.server.domain.AcademicYearSemester;
import com.zhzx.server.domain.Staff;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`tmg_teaching_result`")
@ApiModel(value = "TeachingResult", description = "教学成果表")
public class TeachingResult extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * ID系统自动生成
     */
    @ApiModelProperty(value = "ID系统自动生成")
    @TableField(exist = false)
    private List<TeachingResultAttachment> teachingResultAttachmentList;
    /**
     * 教师ID sys_stuff.id
     */
    @TableField(value = "teacher_id")
    @ApiModelProperty(value = "教师ID sys_stuff.id", required = true)
    private Long teacherId;
    /**
     * 教师ID sys_stuff.id
     */
    @ApiModelProperty(value = "教师ID sys_stuff.id")
    @TableField(exist = false)
    private Staff teacher;
    /**
     * 成果分类ID sys_teaching_result_classify.id
     */
    @TableField(value = "result_classify_id")
    @ApiModelProperty(value = "成果分类ID sys_teaching_result_classify.id", required = true)
    private Long resultClassifyId;
    /**
     * 成果分类ID sys_teaching_result_classify.id
     */
    @ApiModelProperty(value = "成果分类ID sys_teaching_result_classify.id")
    @TableField(exist = false)
    private TeachingResultClassify resultClassify;
    /**
     * 成果时间(年-月)
     */
    @TableField(value = "result_date")
    @ApiModelProperty(value = "成果时间(年-月)", required = true)
    private String resultDate;
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
     * 学年
     */
    @TableField(value = "year")
    @ApiModelProperty(value = "学年", required = true)
    private String year;
    /**
     * 学期
     */
    @TableField(value = "semester")
    @ApiModelProperty(value = "学期", required = true)
    private SemesterEnum semester;
    /**
     * 级别
     */
    @TableField(value = "level")
    @ApiModelProperty(value = "级别", required = true)
    private TeachingResultLevelEnum level;
    /**
     * 成果名称
     */
    @TableField(value = "name")
    @ApiModelProperty(value = "成果名称", required = true)
    private String name;
    /**
     * 审核人ID sys_stuff.id
     */
    @TableField(value = "reviewer_id")
    @ApiModelProperty(value = "审核人ID sys_stuff.id")
    private Long reviewerId;
    /**
     * 审核人ID sys_stuff.id
     */
    @ApiModelProperty(value = "审核人ID sys_stuff.id")
    @TableField(exist = false)
    private Staff reviewer;
    /**
     * 状态
     */
    @TableField(value = "state")
    @ApiModelProperty(value = "状态", required = true)
    private TeachingResultStateEnum state;
    /**
     * 原因
     */
    @TableField(value = "reason")
    @ApiModelProperty(value = "原因")
    private String reason;
    /**
     * 操作人ID sys_user.id
     */
    @TableField(value = "editor_id")
    @ApiModelProperty(value = "操作人ID sys_user.id", required = true)
    private Long editorId;
    /**
     * 操作人 sys_user.real_name
     */
    @TableField(value = "editor_name")
    @ApiModelProperty(value = "操作人 sys_user.real_name", required = true)
    private String editorName;
    /**
     * 
     */
    @TableField(value = "create_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "", required = true)
    private java.util.Date createTime;
    /**
     * 
     */
    @TableField(value = "update_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "", required = true)
    private java.util.Date updateTime;

    /**
     * 设置默认值
     */
    public TeachingResult setDefault() {
        if (this.getYear() == null) {
            this.setYear("");
        }
        if (this.getSemester() == null) {
            this.setSemester(SemesterEnum.Q1);
        }
        if (this.getState() == null) {
            this.setState(TeachingResultStateEnum.PENDING_REVIEW);
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
        if (this.getTeacherId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "教师ID sys_stuff.id不能为空！");
            return false;
        }
        if (this.getResultClassifyId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "成果分类ID sys_teaching_result_classify.id不能为空！");
            return false;
        }
        if (this.getResultDate() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "成果时间(年-月)不能为空！");
            return false;
        }
        if (this.getAcademicYearSemesterId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "学年学期ID sys_academic_year_semester.id不能为空！");
            return false;
        }
        if (this.getYear() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "学年不能为空！");
            return false;
        }
        if (this.getSemester() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "学期不能为空！");
            return false;
        }
        if (this.getLevel() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "级别不能为空！");
            return false;
        }
        if (this.getName() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "成果名称不能为空！");
            return false;
        }
        if (this.getState() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "状态不能为空！");
            return false;
        }
        if (this.getEditorId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "操作人ID sys_user.id不能为空！");
            return false;
        }
        if (this.getEditorName() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "操作人 sys_user.real_name不能为空！");
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
