/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：年级组长表
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
import com.zhzx.server.domain.StaffGradeLeader;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "年级组长表参数", description = "")
public class StaffGradeLeaderParam implements Serializable {
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
     * 教师ID sys_staff.id
     */
    @ApiModelProperty(value = "教师ID sys_staff.id")
    private Long staffId;
    /**
     * 是否当值
     */
    @ApiModelProperty(value = "是否当值")
    private YesNoEnum isCurrent;
    /**
     * 是否当值 IN值List
     */
    @ApiModelProperty(value = "是否当值 IN值List")
    private List<String> isCurrentList;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<StaffGradeLeader> toQueryWrapper() {
        QueryWrapper<StaffGradeLeader> wrapper = Wrappers.<StaffGradeLeader>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getAcademicYearSemesterId() != null, "academic_year_semester_id", this.getAcademicYearSemesterId());
        wrapper.eq(this.getGradeId() != null, "grade_id", this.getGradeId());
        wrapper.eq(this.getStaffId() != null, "staff_id", this.getStaffId());
        wrapper.eq(this.getIsCurrent() != null, "is_current", this.getIsCurrent());
        wrapper.in(this.getIsCurrentList() != null && this.getIsCurrentList().size() > 0, "is_current", this.getIsCurrentList());
        return wrapper;
    }

}
