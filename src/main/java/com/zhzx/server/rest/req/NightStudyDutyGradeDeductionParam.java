/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：晚自习行政值班年级扣分表(不用)
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
import com.zhzx.server.domain.NightStudyDutyGradeDeduction;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "晚自习行政值班年级扣分表(不用)参数", description = "")
public class NightStudyDutyGradeDeductionParam implements Serializable {
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
     * 晚自习行政值班ID day_night_study_duty.id
     */
    @ApiModelProperty(value = "晚自习行政值班ID day_night_study_duty.id")
    private Long nightStudyDutyId;
    /**
     * 年级ID sys_grade.id
     */
    @ApiModelProperty(value = "年级ID sys_grade.id")
    private Long gradeId;
    /**
     * 情况说明
     */
    @ApiModelProperty(value = "情况说明")
    private String Description;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<NightStudyDutyGradeDeduction> toQueryWrapper() {
        QueryWrapper<NightStudyDutyGradeDeduction> wrapper = Wrappers.<NightStudyDutyGradeDeduction>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getNightStudyDutyId() != null, "night_study_duty_id", this.getNightStudyDutyId());
        wrapper.eq(this.getGradeId() != null, "grade_id", this.getGradeId());
        if (this.getDescription() != null) {
            if (this.getDescription().startsWith("%") && this.getDescription().endsWith("%")) {
                wrapper.like("Description", this.getDescription().substring(1, this.getDescription().length() - 1));
            } else if (this.getDescription().startsWith("%") && !this.getDescription().endsWith("%")) {
                wrapper.likeLeft("Description", this.getDescription().substring(1));
            } else if (this.getDescription().endsWith("%")) {
                wrapper.likeRight("Description", this.getDescription().substring(0, this.getDescription().length() - 1));
            } else {
                wrapper.eq("Description", this.getDescription());
            }
        }
        return wrapper;
    }

}
