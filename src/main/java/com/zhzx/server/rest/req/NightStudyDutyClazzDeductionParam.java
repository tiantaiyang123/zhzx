/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：晚自习行政值班班级扣分表
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
import com.zhzx.server.domain.NightStudyDutyClazzDeduction;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "晚自习行政值班班级扣分表参数", description = "")
public class NightStudyDutyClazzDeductionParam implements Serializable {
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
     * 晚自习行政值班班级情况ID day_night_study_duty_clazz.id
     */
    @ApiModelProperty(value = "晚自习行政值班班级情况ID day_night_study_duty_clazz.id")
    private Long nightStudyDutyClazzId;
    /**
     * 情况说明
     */
    @ApiModelProperty(value = "情况说明")
    private String Description;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<NightStudyDutyClazzDeduction> toQueryWrapper() {
        QueryWrapper<NightStudyDutyClazzDeduction> wrapper = Wrappers.<NightStudyDutyClazzDeduction>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getNightStudyDutyClazzId() != null, "night_study_duty_clazz_id", this.getNightStudyDutyClazzId());
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
