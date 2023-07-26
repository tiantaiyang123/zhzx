package com.zhzx.server.vo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhzx.server.domain.TeachingResult;
import com.zhzx.server.rest.req.TeachingResultParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.util.List;

@Data
@ApiModel(value = "教学成果表参数", description = "")
public class TeachingResultParamVo extends TeachingResultParam {

    @ApiModelProperty(value = "分类父级ID IN值List")
    private List<Long> classifyParentIdList;

    @ApiModelProperty(value = "分类ID IN值List")
    private List<Long> classifyIdList;

    @ApiModelProperty(value = "老师ID IN值List")
    private List<Long> teacherIdList;

    @ApiModelProperty(value = "部门 IN值List")
    private List<String> departmentList;

    @ApiModelProperty(value = "科目Id")
    private Long subjectId;
    /**
     * 成果时间下限值(大于等于)
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "成果时间下限值(大于等于)")
    private java.util.Date resultDateFrom;
    /**
     * 成果时间上限值(小于)
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "成果时间上限值(小于)")
    private java.util.Date resultDateTo;

    @Override
    public QueryWrapper<TeachingResult> toQueryWrapper() {
        QueryWrapper<TeachingResult> queryWrapper = super.toQueryWrapper();
        if (this.getClassifyIdList() == null || this.getClassifyIdList().size() == 0) {
            if (this.getClassifyParentIdList() != null && this.getClassifyParentIdList().size() > 0) {
                String parentIds = StringUtils.join(this.getClassifyParentIdList(), ",");
                queryWrapper.inSql("result_classify_id", "select id from sys_teaching_result_classify where parent_id in (" + parentIds + ")");
            }
        }
        queryWrapper.in(this.getClassifyIdList() != null && this.getClassifyIdList().size() > 0, "result_classify_id", this.getClassifyIdList());
        queryWrapper.in(this.getTeacherIdList() != null && this.getTeacherIdList().size() > 0, "teacher_id", this.getTeacherIdList());
        queryWrapper.inSql(this.getDepartmentList() != null, "teacher_id", "select id from sys_staff where department in ('" + StringUtils.join(departmentList, "','") + "')");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        queryWrapper.ge(this.getResultDateFrom() != null, "result_date", this.getResultDateFrom() != null ? sdf.format(this.getResultDateFrom()) : null);
        queryWrapper.le(this.getResultDateTo() != null, "result_date", this.getResultDateTo() != null ? sdf.format(this.getResultDateTo()) : null);
        return queryWrapper;
    }
}
