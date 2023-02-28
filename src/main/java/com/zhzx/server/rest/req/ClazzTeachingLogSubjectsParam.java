/**
 * 项目：中华中学管理平台
 * 模型分组：学生管理
 * 模型名称：班级教学日志科目表
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
import com.zhzx.server.enums.LogRulesEnum;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.enums.LogStateEnum;
import com.zhzx.server.domain.ClazzTeachingLogSubjects;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "班级教学日志科目表参数", description = "")
public class ClazzTeachingLogSubjectsParam implements Serializable {
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
     * 班级教学日志ID std_clazz_teaching_log.id
     */
    @ApiModelProperty(value = "班级教学日志ID std_clazz_teaching_log.id")
    private Long clazzTeachingLogId;
    /**
     * 节次
     */
    @ApiModelProperty(value = "节次")
    private Integer sortOrder;
    /**
     * 科目ID sys_subject.id
     */
    @ApiModelProperty(value = "科目ID sys_subject.id")
    private Long subjectId;
    /**
     * 任课老师ID sys_staff.id
     */
    @ApiModelProperty(value = "任课老师ID sys_staff.id")
    private Long teacherId;
    /**
     * 课堂纪律
     */
    @ApiModelProperty(value = "课堂纪律")
    private LogRulesEnum rules;
    /**
     * 课堂纪律 IN值List
     */
    @ApiModelProperty(value = "课堂纪律 IN值List")
    private List<String> rulesList;
    /**
     * 是否准时下课
     */
    @ApiModelProperty(value = "是否准时下课")
    private YesNoEnum isOnTime;
    /**
     * 是否准时下课 IN值List
     */
    @ApiModelProperty(value = "是否准时下课 IN值List")
    private List<String> isOnTimeList;
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private LogStateEnum state;
    /**
     * 状态 IN值List
     */
    @ApiModelProperty(value = "状态 IN值List")
    private List<String> stateList;
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
    public QueryWrapper<ClazzTeachingLogSubjects> toQueryWrapper() {
        QueryWrapper<ClazzTeachingLogSubjects> wrapper = Wrappers.<ClazzTeachingLogSubjects>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getClazzTeachingLogId() != null, "clazz_teaching_log_id", this.getClazzTeachingLogId());
        wrapper.eq(this.getSortOrder() != null, "sort_order", this.getSortOrder());
        wrapper.eq(this.getSubjectId() != null, "subject_id", this.getSubjectId());
        wrapper.eq(this.getTeacherId() != null, "teacher_id", this.getTeacherId());
        wrapper.eq(this.getRules() != null, "rules", this.getRules());
        wrapper.in(this.getRulesList() != null && this.getRulesList().size() > 0, "rules", this.getRulesList());
        wrapper.eq(this.getIsOnTime() != null, "is_on_time", this.getIsOnTime());
        wrapper.in(this.getIsOnTimeList() != null && this.getIsOnTimeList().size() > 0, "is_on_time", this.getIsOnTimeList());
        wrapper.eq(this.getState() != null, "state", this.getState());
        wrapper.in(this.getStateList() != null && this.getStateList().size() > 0, "state", this.getStateList());
        wrapper.eq(this.getCreateTime() != null, "create_time", this.getCreateTime());
        wrapper.ge(this.getCreateTimeFrom() != null, "create_time", this.getCreateTimeFrom());
        wrapper.lt(this.getCreateTimeTo() != null, "create_time", this.getCreateTimeTo());
        wrapper.eq(this.getUpdateTime() != null, "update_time", this.getUpdateTime());
        wrapper.ge(this.getUpdateTimeFrom() != null, "update_time", this.getUpdateTimeFrom());
        wrapper.lt(this.getUpdateTimeTo() != null, "update_time", this.getUpdateTimeTo());
        return wrapper;
    }

}
