/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：任课老师表
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
import com.zhzx.server.domain.Staff;
import com.zhzx.server.domain.Clazz;
import com.zhzx.server.domain.Subject;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`sys_staff_lesson_teacher`")
@ApiModel(value = "StaffLessonTeacher", description = "任课老师表")
public class StaffLessonTeacher extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
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
     * 班级ID sys_clazz.id
     */
    @TableField(value = "clazz_id")
    @ApiModelProperty(value = "班级ID sys_clazz.id", required = true)
    private Long clazzId;
    /**
     * 班级ID sys_clazz.id
     */
    @ApiModelProperty(value = "班级ID sys_clazz.id")
    @TableField(exist = false)
    private Clazz clazz;
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
    public StaffLessonTeacher setDefault() {
        return this;
    }

    /**
     * 数据验证
     */
    public Boolean validate(Boolean throwException) {
        if (this.getStaffId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "教师ID sys_staff.id不能为空！");
            return false;
        }
        if (this.getClazzId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "班级ID sys_clazz.id不能为空！");
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
