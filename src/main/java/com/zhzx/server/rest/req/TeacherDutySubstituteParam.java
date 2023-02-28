/**
 * 项目：中华中学管理平台
 * 模型分组：一日常规管理
 * 模型名称：教师值班代班表
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
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.domain.TeacherDutySubstitute;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "教师值班代班表参数", description = "")
public class TeacherDutySubstituteParam implements Serializable {
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
     * 教师值班ID day_teacher_duty.id
     */
    @ApiModelProperty(value = "教师值班ID day_teacher_duty.id")
    private Long teacherDutyId;
    /**
     * 原值班教师ID sys_staff.id
     */
    @ApiModelProperty(value = "原值班教师ID sys_staff.id")
    private Long teacherOldId;
    /**
     * 代班教师ID sys_staff.id
     */
    @ApiModelProperty(value = "代班教师ID sys_staff.id")
    private Long teacherId;
    /**
     * 是否同意 初始值为 null
     */
    @ApiModelProperty(value = "是否同意 初始值为 null")
    private YesNoEnum isAgree;
    /**
     * 是否同意 初始值为 null IN值List
     */
    @ApiModelProperty(value = "是否同意 初始值为 null IN值List")
    private List<String> isAgreeList;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<TeacherDutySubstitute> toQueryWrapper() {
        QueryWrapper<TeacherDutySubstitute> wrapper = Wrappers.<TeacherDutySubstitute>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getTeacherDutyId() != null, "teacher_duty_id", this.getTeacherDutyId());
        wrapper.eq(this.getTeacherOldId() != null, "teacher_old_id", this.getTeacherOldId());
        wrapper.eq(this.getTeacherId() != null, "teacher_id", this.getTeacherId());
        wrapper.eq(this.getIsAgree() != null, "is_agree", this.getIsAgree());
        wrapper.in(this.getIsAgreeList() != null && this.getIsAgreeList().size() > 0, "is_agree", this.getIsAgreeList());
        return wrapper;
    }

}
