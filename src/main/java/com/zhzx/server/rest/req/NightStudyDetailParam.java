/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：晚自习明细表
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
import com.zhzx.server.enums.TeacherDutyInfoDetailClassifyEnum;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.domain.NightStudyDetail;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "晚自习明细表参数", description = "")
public class NightStudyDetailParam implements Serializable {
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
     * 晚自习ID day_night_study.id
     */
    @ApiModelProperty(value = "晚自习ID day_night_study.id")
    private Long nightStudyId;
    /**
     * 分类
     */
    @ApiModelProperty(value = "分类")
    private TeacherDutyInfoDetailClassifyEnum classify;
    /**
     * 分类 IN值List
     */
    @ApiModelProperty(value = "分类 IN值List")
    private List<String> classifyList;
    /**
     * 学生ID sys_student.id
     */
    @ApiModelProperty(value = "学生ID sys_student.id")
    private Long studentId;
    /**
     * 原因
     */
    @ApiModelProperty(value = "原因")
    private String reason;
    /**
     * 是否通知班主任
     */
    @ApiModelProperty(value = "是否通知班主任")
    private YesNoEnum isNotice;
    /**
     * 是否通知班主任 IN值List
     */
    @ApiModelProperty(value = "是否通知班主任 IN值List")
    private List<String> isNoticeList;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<NightStudyDetail> toQueryWrapper() {
        QueryWrapper<NightStudyDetail> wrapper = Wrappers.<NightStudyDetail>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getNightStudyId() != null, "night_study_id", this.getNightStudyId());
        wrapper.eq(this.getClassify() != null, "classify", this.getClassify());
        wrapper.in(this.getClassifyList() != null && this.getClassifyList().size() > 0, "classify", this.getClassifyList());
        wrapper.eq(this.getStudentId() != null, "student_id", this.getStudentId());
        if (this.getReason() != null) {
            if (this.getReason().startsWith("%") && this.getReason().endsWith("%")) {
                wrapper.like("reason", this.getReason().substring(1, this.getReason().length() - 1));
            } else if (this.getReason().startsWith("%") && !this.getReason().endsWith("%")) {
                wrapper.likeLeft("reason", this.getReason().substring(1));
            } else if (this.getReason().endsWith("%")) {
                wrapper.likeRight("reason", this.getReason().substring(0, this.getReason().length() - 1));
            } else {
                wrapper.eq("reason", this.getReason());
            }
        }
        wrapper.eq(this.getIsNotice() != null, "is_notice", this.getIsNotice());
        wrapper.in(this.getIsNoticeList() != null && this.getIsNoticeList().size() > 0, "is_notice", this.getIsNoticeList());
        return wrapper;
    }

}
