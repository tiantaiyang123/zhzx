/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：晚自习行政值班年级图片表
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
import com.zhzx.server.domain.NightStudyDutyGradeImages;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "晚自习行政值班年级图片表参数", description = "")
public class NightStudyDutyGradeImagesParam implements Serializable {
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
     * 图片链接
     */
    @ApiModelProperty(value = "图片链接")
    private String url;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<NightStudyDutyGradeImages> toQueryWrapper() {
        QueryWrapper<NightStudyDutyGradeImages> wrapper = Wrappers.<NightStudyDutyGradeImages>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getNightStudyDutyId() != null, "night_study_duty_id", this.getNightStudyDutyId());
        wrapper.eq(this.getGradeId() != null, "grade_id", this.getGradeId());
        if (this.getUrl() != null) {
            if (this.getUrl().startsWith("%") && this.getUrl().endsWith("%")) {
                wrapper.like("url", this.getUrl().substring(1, this.getUrl().length() - 1));
            } else if (this.getUrl().startsWith("%") && !this.getUrl().endsWith("%")) {
                wrapper.likeLeft("url", this.getUrl().substring(1));
            } else if (this.getUrl().endsWith("%")) {
                wrapper.likeRight("url", this.getUrl().substring(0, this.getUrl().length() - 1));
            } else {
                wrapper.eq("url", this.getUrl());
            }
        }
        return wrapper;
    }

}
