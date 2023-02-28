/**
* 项目：中华中学管理平台
* 模型分组：成绩管理
* 模型名称：考试发布表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.rest.req;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhzx.server.domain.ExamPublish;
import com.zhzx.server.enums.ExamPrintEnum;
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
@ApiModel(value = "考试发布表参数", description = "")
public class ExamPublishParam implements Serializable {
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
     * 年级ID sys_grade.id
     */
    @ApiModelProperty(value = "年级ID sys_grade.id")
    private Long gradeId;
    /**
     * 发布日期
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "发布日期")
    private java.util.Date publishTime;
    /**
     * 发布日期 下限值(大于等于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "发布日期 下限值(大于等于)")
    private java.util.Date publishTimeFrom;
    /**
     * 发布日期 上限值(小于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "发布日期 上限值(小于)")
    private java.util.Date publishTimeTo;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 填报人
     */
    @ApiModelProperty(value = "填报人")
    private String editor;
    /**
     * 是否发布
     */
    @ApiModelProperty(value = "是否发布")
    private ExamPrintEnum isPublish;
    /**
     * 是否发布 IN值List
     */
    @ApiModelProperty(value = "是否发布 IN值List")
    private List<String> isPublishList;
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
    public QueryWrapper<ExamPublish> toQueryWrapper() {
        QueryWrapper<ExamPublish> wrapper = Wrappers.<ExamPublish>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getGradeId() != null, "grade_id", this.getGradeId());
        wrapper.eq(this.getPublishTime() != null, "publish_time", this.getPublishTime());
        wrapper.ge(this.getPublishTimeFrom() != null, "publish_time", this.getPublishTimeFrom());
        wrapper.lt(this.getPublishTimeTo() != null, "publish_time", this.getPublishTimeTo());
        if (this.getRemark() != null) {
            if (this.getRemark().startsWith("%") && this.getRemark().endsWith("%")) {
                wrapper.like("remark", this.getRemark().substring(1, this.getRemark().length() - 1));
            } else if (this.getRemark().startsWith("%") && !this.getRemark().endsWith("%")) {
                wrapper.likeLeft("remark", this.getRemark().substring(1));
            } else if (this.getRemark().endsWith("%")) {
                wrapper.likeRight("remark", this.getRemark().substring(0, this.getRemark().length() - 1));
            } else {
                wrapper.eq("remark", this.getRemark());
            }
        }
        if (this.getEditor() != null) {
            if (this.getEditor().startsWith("%") && this.getEditor().endsWith("%")) {
                wrapper.like("editor", this.getEditor().substring(1, this.getEditor().length() - 1));
            } else if (this.getEditor().startsWith("%") && !this.getEditor().endsWith("%")) {
                wrapper.likeLeft("editor", this.getEditor().substring(1));
            } else if (this.getEditor().endsWith("%")) {
                wrapper.likeRight("editor", this.getEditor().substring(0, this.getEditor().length() - 1));
            } else {
                wrapper.eq("editor", this.getEditor());
            }
        }
        wrapper.eq(this.getIsPublish() != null, "is_publish", this.getIsPublish());
        wrapper.in(this.getIsPublishList() != null && this.getIsPublishList().size() > 0, "is_publish", this.getIsPublishList());
        wrapper.eq(this.getCreateTime() != null, "create_time", this.getCreateTime());
        wrapper.ge(this.getCreateTimeFrom() != null, "create_time", this.getCreateTimeFrom());
        wrapper.lt(this.getCreateTimeTo() != null, "create_time", this.getCreateTimeTo());
        wrapper.eq(this.getUpdateTime() != null, "update_time", this.getUpdateTime());
        wrapper.ge(this.getUpdateTimeFrom() != null, "update_time", this.getUpdateTimeFrom());
        wrapper.lt(this.getUpdateTimeTo() != null, "update_time", this.getUpdateTimeTo());
        return wrapper;
    }

}
