/**
 * 项目：中华中学管理平台
 * 模型分组：学生管理
 * 模型名称：班级教学日志作业量反馈表
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
import com.zhzx.server.enums.OperationDurationEnum;
import com.zhzx.server.domain.ClazzTeachingLogOperation;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "班级教学日志作业量反馈表参数", description = "")
public class ClazzTeachingLogOperationParam implements Serializable {
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
     * 科目ID sys_subject.id
     */
    @ApiModelProperty(value = "科目ID sys_subject.id")
    private Long subjectId;
    /**
     * 时长
     */
    @ApiModelProperty(value = "时长")
    private OperationDurationEnum duration;
    /**
     * 时长 IN值List
     */
    @ApiModelProperty(value = "时长 IN值List")
    private List<String> durationList;
    /**
     * 作业日期
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "作业日期")
    private java.util.Date operationDate;
    /**
     * 作业日期 下限值(大于等于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "作业日期 下限值(大于等于)")
    private java.util.Date operationDateFrom;
    /**
     * 作业日期 上限值(小于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "作业日期 上限值(小于)")
    private java.util.Date operationDateTo;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<ClazzTeachingLogOperation> toQueryWrapper() {
        QueryWrapper<ClazzTeachingLogOperation> wrapper = Wrappers.<ClazzTeachingLogOperation>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getClazzTeachingLogId() != null, "clazz_teaching_log_id", this.getClazzTeachingLogId());
        wrapper.eq(this.getSubjectId() != null, "subject_id", this.getSubjectId());
        wrapper.eq(this.getDuration() != null, "duration", this.getDuration());
        wrapper.in(this.getDurationList() != null && this.getDurationList().size() > 0, "duration", this.getDurationList());
        wrapper.eq(this.getOperationDate() != null, "operation_date", this.getOperationDate());
        wrapper.ge(this.getOperationDateFrom() != null, "operation_date", this.getOperationDateFrom());
        wrapper.lt(this.getOperationDateTo() != null, "operation_date", this.getOperationDateTo());
        return wrapper;
    }

}
