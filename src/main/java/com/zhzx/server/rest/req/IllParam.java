/**
 * 项目：中华中学管理平台
 * 模型分组：学生管理
 * 模型名称：因病缺课表
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
import com.zhzx.server.domain.Ill;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "因病缺课表参数", description = "")
public class IllParam implements Serializable {
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
     * 缺课天数
     */
    @ApiModelProperty(value = "缺课天数")
    private Integer days;
    /**
     * 因病缺课主要症状
     */
    @ApiModelProperty(value = "因病缺课主要症状")
    private String symptom;
    /**
     * 因病缺课疾病名称
     */
    @ApiModelProperty(value = "因病缺课疾病名称")
    private String illName;
    /**
     * 就医情况
     */
    @ApiModelProperty(value = "就医情况")
    private String treatment;
    /**
     * 非因病缺课原因
     */
    @ApiModelProperty(value = "非因病缺课原因")
    private String reason;
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
    public QueryWrapper<Ill> toQueryWrapper() {
        QueryWrapper<Ill> wrapper = Wrappers.<Ill>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getAcademicYearSemesterId() != null, "academic_year_semester_id", this.getAcademicYearSemesterId());
        wrapper.eq(this.getWeek() != null, "week", this.getWeek());
        wrapper.eq(this.getRegisterDate() != null, "register_date", this.getRegisterDate());
        wrapper.ge(this.getRegisterDateFrom() != null, "register_date", this.getRegisterDateFrom());
        wrapper.lt(this.getRegisterDateTo() != null, "register_date", this.getRegisterDateTo());
        wrapper.eq(this.getClazzId() != null, "clazz_id", this.getClazzId());
        wrapper.eq(this.getStudentId() != null, "student_id", this.getStudentId());
        wrapper.eq(this.getDays() != null, "days", this.getDays());
        if (this.getSymptom() != null) {
            if (this.getSymptom().startsWith("%") && this.getSymptom().endsWith("%")) {
                wrapper.like("symptom", this.getSymptom().substring(1, this.getSymptom().length() - 1));
            } else if (this.getSymptom().startsWith("%") && !this.getSymptom().endsWith("%")) {
                wrapper.likeLeft("symptom", this.getSymptom().substring(1));
            } else if (this.getSymptom().endsWith("%")) {
                wrapper.likeRight("symptom", this.getSymptom().substring(0, this.getSymptom().length() - 1));
            } else {
                wrapper.eq("symptom", this.getSymptom());
            }
        }
        if (this.getIllName() != null) {
            if (this.getIllName().startsWith("%") && this.getIllName().endsWith("%")) {
                wrapper.like("ill_name", this.getIllName().substring(1, this.getIllName().length() - 1));
            } else if (this.getIllName().startsWith("%") && !this.getIllName().endsWith("%")) {
                wrapper.likeLeft("ill_name", this.getIllName().substring(1));
            } else if (this.getIllName().endsWith("%")) {
                wrapper.likeRight("ill_name", this.getIllName().substring(0, this.getIllName().length() - 1));
            } else {
                wrapper.eq("ill_name", this.getIllName());
            }
        }
        if (this.getTreatment() != null) {
            if (this.getTreatment().startsWith("%") && this.getTreatment().endsWith("%")) {
                wrapper.like("treatment", this.getTreatment().substring(1, this.getTreatment().length() - 1));
            } else if (this.getTreatment().startsWith("%") && !this.getTreatment().endsWith("%")) {
                wrapper.likeLeft("treatment", this.getTreatment().substring(1));
            } else if (this.getTreatment().endsWith("%")) {
                wrapper.likeRight("treatment", this.getTreatment().substring(0, this.getTreatment().length() - 1));
            } else {
                wrapper.eq("treatment", this.getTreatment());
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
        wrapper.eq(this.getCreateTime() != null, "create_time", this.getCreateTime());
        wrapper.ge(this.getCreateTimeFrom() != null, "create_time", this.getCreateTimeFrom());
        wrapper.lt(this.getCreateTimeTo() != null, "create_time", this.getCreateTimeTo());
        wrapper.eq(this.getUpdateTime() != null, "update_time", this.getUpdateTime());
        wrapper.ge(this.getUpdateTimeFrom() != null, "update_time", this.getUpdateTimeFrom());
        wrapper.lt(this.getUpdateTimeTo() != null, "update_time", this.getUpdateTimeTo());
        return wrapper;
    }

}
