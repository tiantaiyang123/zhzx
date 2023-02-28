/**
* 项目：中华中学管理平台
* 模型分组：学生管理
* 模型名称：晚自习考勤班级概况表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zhzx.server.enums.StudentNightDutyTypeEnum;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.rest.res.ApiCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`std_night_study_attendance_sub`")
@ApiModel(value = "NightStudyAttendanceSub", description = "晚自习考勤班级概况表")
public class NightStudyAttendanceSub extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
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
     * 周数
     */
    @TableField(value = "week")
    @ApiModelProperty(value = "周数", required = true)
    private Integer week;
    /**
     * 日期
     */
    @TableField(value = "register_date")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "日期", required = true)
    private java.util.Date registerDate;
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
     * 自习阶段
     */
    @TableField(value = "stage")
    @ApiModelProperty(value = "自习阶段", required = true)
    private StudentNightDutyTypeEnum stage;
    /**
     * 应到人数
     */
    @TableField(value = "should_num")
    @ApiModelProperty(value = "应到人数", required = true)
    private Integer shouldNum;
    /**
     * 实到人数
     */
    @TableField(value = "actual_num")
    @ApiModelProperty(value = "实到人数", required = true)
    private Integer actualNum;
    /**
     * 分类 sys_label.classify=WZXKQ
     */
    @TableField(value = "classify")
    @ApiModelProperty(value = "分类 sys_label.classify=WZXKQ", required = true)
    private String classify;
    /**
     * 值日班长总结
     */
    @TableField(value = "summarize")
    @ApiModelProperty(value = "值日班长总结")
    private String summarize;
    /**
     * 值日班长签名
     */
    @TableField(value = "sign")
    @ApiModelProperty(value = "值日班长签名", required = true)
    private String sign;
    /**
     * 是否全勤 [YES.是 NO.否]
     */
    @TableField(value = "is_full_attendence")
    @ApiModelProperty(value = "是否全勤 [YES.是 NO.否]", required = true)
    private YesNoEnum isFullAttendence;
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

    @TableField(exist = false)
    private List<NightStudyAttendance> nightStudyAttendanceList;
    @TableField(exist = false)
    private java.util.Date dayTime;

    /**
     * 设置默认值
     */
    public NightStudyAttendanceSub setDefault() {
        if (this.getShouldNum() == null) {
            this.setShouldNum(0);
        }
        if (this.getActualNum() == null) {
            this.setActualNum(0);
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
        if (this.getAcademicYearSemesterId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "academic_year_semester_id不能为空！");
            return false;
        }
        if (this.getWeek() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "week不能为空！");
            return false;
        }
        if (this.getRegisterDate() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "register_date不能为空！");
            return false;
        }
        if (this.getClazzId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "clazz_id不能为空！");
            return false;
        }
        if (this.getStage() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "stage不能为空！");
            return false;
        }
        if (this.getShouldNum() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "should_num不能为空！");
            return false;
        }
        if (this.getActualNum() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "actual_num不能为空！");
            return false;
        }
        if (this.getClassify() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "classify不能为空！");
            return false;
        }
        if (this.getSign() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "sign不能为空！");
            return false;
        }
        if (this.getIsFullAttendence() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "is_full_attendence不能为空！");
            return false;
        }
        if (this.getCreateTime() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "create_time不能为空！");
            return false;
        }
        if (this.getUpdateTime() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "update_time不能为空！");
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
