/**
 * 项目：中华中学管理平台
 * 模型分组：学生管理
 * 模型名称：晚自习考勤表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest.req;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhzx.server.domain.NightStudyAttendance;
import com.zhzx.server.domain.NightStudyAttendanceSub;
import com.zhzx.server.enums.StudentNightDutyTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.List;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "NightStudyAttendanceParam", description = "晚自习考勤表参数")
public class NightStudyAttendanceParam implements Serializable {
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
     * 学年学期ID sys_academic_year_semester.id
     */
    @ApiModelProperty(value = "学年学期ID sys_academic_year_semester.id")
    private Long academicYearSemesterId;
    /**
     * 周数
     */
    @ApiModelProperty(value = "周数")
    private Integer week;
    /**
     * 日期
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "日期")
    private java.util.Date registerDate;
    /**
     * 日期 下限值(大于等于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "日期 下限值(大于等于)")
    private java.util.Date registerDateFrom;
    /**
     * 日期 上限值(小于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "日期 上限值(小于)")
    private java.util.Date registerDateTo;
    /**
     * 班级ID sys_clazz.id
     */
    @ApiModelProperty(value = "班级ID sys_clazz.id")
    private Long clazzId;
    /**
     * 学生ID sys_student.id
     */
    @ApiModelProperty(value = "学生ID sys_student.id")
    private Long studentId;
    /**
     * 自习阶段
     */
    @ApiModelProperty(value = "自习阶段")
    private StudentNightDutyTypeEnum stage;
    /**
     * 自习阶段 IN值List
     */
    @ApiModelProperty(value = "自习阶段 IN值List")
    private List<String> stageList;
    /**
     * 应到人数
     */
    @ApiModelProperty(value = "应到人数")
    private Integer shouldNum;
    /**
     * 实到人数
     */
    @ApiModelProperty(value = "实到人数")
    private Integer actualNum;
    /**
     * 分类 sys_label.classify=WZXKQ
     */
    @ApiModelProperty(value = "分类 sys_label.classify=WZXKQ")
    private String classify;
    /**
     * 原因
     */
    @ApiModelProperty(value = "原因")
    private String reason;
    /**
     * 值日班长总结
     */
    @ApiModelProperty(value = "值日班长总结")
    private String summarize;
    /**
     * 值日班长签名
     */
    @ApiModelProperty(value = "值日班长签名")
    private String sign;
    /**
     * 
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "")
    private java.util.Date createTime;
    /**
     *  下限值(大于等于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = " 下限值(大于等于)")
    private java.util.Date createTimeFrom;
    /**
     *  上限值(小于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = " 上限值(小于)")
    private java.util.Date createTimeTo;
    /**
     * 
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "")
    private java.util.Date updateTime;
    /**
     *  下限值(大于等于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = " 下限值(大于等于)")
    private java.util.Date updateTimeFrom;
    /**
     *  上限值(小于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = " 上限值(小于)")
    private java.util.Date updateTimeTo;


    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<NightStudyAttendance> toQueryWrapper() {
        QueryWrapper<NightStudyAttendance> wrapper = Wrappers.<NightStudyAttendance>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getAcademicYearSemesterId() != null, "academic_year_semester_id", this.getAcademicYearSemesterId());
        wrapper.eq(this.getWeek() != null, "week", this.getWeek());
        wrapper.eq(this.getRegisterDate() != null, "register_date", this.getRegisterDate());
        wrapper.ge(this.getRegisterDateFrom() != null, "register_date", this.getRegisterDateFrom());
        wrapper.lt(this.getRegisterDateTo() != null, "register_date", this.getRegisterDateTo());
        wrapper.eq(this.getClazzId() != null, "clazz_id", this.getClazzId());
        wrapper.eq(this.getStudentId() != null, "student_id", this.getStudentId());
        wrapper.eq(this.getStage() != null, "stage", this.getStage());
        wrapper.in(this.getStageList() != null && this.getStageList().size() > 0, "stage", this.getStageList());
        wrapper.eq(this.getShouldNum() != null, "should_num", this.getShouldNum());
        wrapper.eq(this.getActualNum() != null, "actual_num", this.getActualNum());
        if (this.getClassify() != null) {
            if (this.getClassify().startsWith("%") && this.getClassify().endsWith("%")) {
                wrapper.like("classify", this.getClassify().substring(1, this.getClassify().length() - 1));
            } else if (this.getClassify().startsWith("%") && !this.getClassify().endsWith("%")) {
                wrapper.likeLeft("classify", this.getClassify().substring(1));
            } else if (this.getClassify().endsWith("%")) {
                wrapper.likeRight("classify", this.getClassify().substring(0, this.getClassify().length() - 1));
            } else {
                wrapper.eq("classify", this.getClassify());
            }
        }
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
        if (this.getSummarize() != null) {
            if (this.getSummarize().startsWith("%") && this.getSummarize().endsWith("%")) {
                wrapper.like("summarize", this.getSummarize().substring(1, this.getSummarize().length() - 1));
            } else if (this.getSummarize().startsWith("%") && !this.getSummarize().endsWith("%")) {
                wrapper.likeLeft("summarize", this.getSummarize().substring(1));
            } else if (this.getSummarize().endsWith("%")) {
                wrapper.likeRight("summarize", this.getSummarize().substring(0, this.getSummarize().length() - 1));
            } else {
                wrapper.eq("summarize", this.getSummarize());
            }
        }
        if (this.getSign() != null) {
            if (this.getSign().startsWith("%") && this.getSign().endsWith("%")) {
                wrapper.like("sign", this.getSign().substring(1, this.getSign().length() - 1));
            } else if (this.getSign().startsWith("%") && !this.getSign().endsWith("%")) {
                wrapper.likeLeft("sign", this.getSign().substring(1));
            } else if (this.getSign().endsWith("%")) {
                wrapper.likeRight("sign", this.getSign().substring(0, this.getSign().length() - 1));
            } else {
                wrapper.eq("sign", this.getSign());
            }
        }
        wrapper.eq(this.getCreateTime() != null, "create_time", this.getCreateTime());
        wrapper.ge(this.getCreateTimeFrom() != null, "create_time", this.getCreateTimeFrom());
        wrapper.lt(this.getCreateTimeTo() != null, "create_time", this.getCreateTimeTo());
        wrapper.eq(this.getUpdateTime() != null, "update_time", this.getUpdateTime());
        wrapper.ge(this.getUpdateTimeFrom() != null, "update_time", this.getUpdateTimeFrom());
        wrapper.lt(this.getUpdateTimeTo() != null, "update_time", this.getUpdateTimeTo());
        return wrapper;
    }

    public QueryWrapper<NightStudyAttendanceSub> toQueryWrapper1() {
        QueryWrapper<NightStudyAttendanceSub> wrapper = Wrappers.<NightStudyAttendanceSub>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getAcademicYearSemesterId() != null, "academic_year_semester_id", this.getAcademicYearSemesterId());
        wrapper.eq(this.getWeek() != null, "week", this.getWeek());
        wrapper.eq(this.getRegisterDate() != null, "register_date", this.getRegisterDate());
        wrapper.ge(this.getRegisterDateFrom() != null, "register_date", this.getRegisterDateFrom());
        wrapper.lt(this.getRegisterDateTo() != null, "register_date", this.getRegisterDateTo());
        wrapper.eq(this.getClazzId() != null, "clazz_id", this.getClazzId());
        wrapper.eq(this.getStage() != null, "stage", this.getStage());
        wrapper.in(this.getStageList() != null && this.getStageList().size() > 0, "stage", this.getStageList());
        wrapper.eq(this.getShouldNum() != null, "should_num", this.getShouldNum());
        wrapper.eq(this.getActualNum() != null, "actual_num", this.getActualNum());
        if (this.getClassify() != null) {
            if (this.getClassify().startsWith("%") && this.getClassify().endsWith("%")) {
                wrapper.like("classify", this.getClassify().substring(1, this.getClassify().length() - 1));
            } else if (this.getClassify().startsWith("%") && !this.getClassify().endsWith("%")) {
                wrapper.likeLeft("classify", this.getClassify().substring(1));
            } else if (this.getClassify().endsWith("%")) {
                wrapper.likeRight("classify", this.getClassify().substring(0, this.getClassify().length() - 1));
            } else {
                wrapper.eq("classify", this.getClassify());
            }
        }
        if (this.getSummarize() != null) {
            if (this.getSummarize().startsWith("%") && this.getSummarize().endsWith("%")) {
                wrapper.like("summarize", this.getSummarize().substring(1, this.getSummarize().length() - 1));
            } else if (this.getSummarize().startsWith("%") && !this.getSummarize().endsWith("%")) {
                wrapper.likeLeft("summarize", this.getSummarize().substring(1));
            } else if (this.getSummarize().endsWith("%")) {
                wrapper.likeRight("summarize", this.getSummarize().substring(0, this.getSummarize().length() - 1));
            } else {
                wrapper.eq("summarize", this.getSummarize());
            }
        }
        if (this.getSign() != null) {
            if (this.getSign().startsWith("%") && this.getSign().endsWith("%")) {
                wrapper.like("sign", this.getSign().substring(1, this.getSign().length() - 1));
            } else if (this.getSign().startsWith("%") && !this.getSign().endsWith("%")) {
                wrapper.likeLeft("sign", this.getSign().substring(1));
            } else if (this.getSign().endsWith("%")) {
                wrapper.likeRight("sign", this.getSign().substring(0, this.getSign().length() - 1));
            } else {
                wrapper.eq("sign", this.getSign());
            }
        }
        wrapper.eq(this.getCreateTime() != null, "create_time", this.getCreateTime());
        wrapper.ge(this.getCreateTimeFrom() != null, "create_time", this.getCreateTimeFrom());
        wrapper.lt(this.getCreateTimeTo() != null, "create_time", this.getCreateTimeTo());
        wrapper.eq(this.getUpdateTime() != null, "update_time", this.getUpdateTime());
        wrapper.ge(this.getUpdateTimeFrom() != null, "update_time", this.getUpdateTimeFrom());
        wrapper.lt(this.getUpdateTimeTo() != null, "update_time", this.getUpdateTimeTo());
        return wrapper;
    }

}
