/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：科目表
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
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.domain.Subject;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "科目表参数", description = "")
public class SubjectParam implements Serializable {
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
     * 科目名称
     */
    @ApiModelProperty(value = "科目名称")
    private String name;
    /**
     * 是否为赋分科目
     */
    @ApiModelProperty(value = "是否为赋分科目")
    private YesNoEnum hasWeight;
    /**
     * 是否为赋分科目 IN值List
     */
    @ApiModelProperty(value = "是否为赋分科目 IN值List")
    private List<String> hasWeightList;
    /**
     * 是否为主要科目
     */
    @ApiModelProperty(value = "是否为主要科目")
    private YesNoEnum isMain;
    /**
     * 是否为主要科目 IN值List
     */
    @ApiModelProperty(value = "是否为主要科目 IN值List")
    private List<String> isMainList;
    /**
     * 总分
     */
    @ApiModelProperty(value = "总分")
    private Long maxScore;
    /**
     * 学科别名
     */
    @ApiModelProperty(value = "学科别名")
    private String subjectAlias;
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
    public QueryWrapper<Subject> toQueryWrapper() {
        QueryWrapper<Subject> wrapper = Wrappers.<Subject>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        if (this.getName() != null) {
            if (this.getName().startsWith("%") && this.getName().endsWith("%")) {
                wrapper.like("name", this.getName().substring(1, this.getName().length() - 1));
            } else if (this.getName().startsWith("%") && !this.getName().endsWith("%")) {
                wrapper.likeLeft("name", this.getName().substring(1));
            } else if (this.getName().endsWith("%")) {
                wrapper.likeRight("name", this.getName().substring(0, this.getName().length() - 1));
            } else {
                wrapper.eq("name", this.getName());
            }
        }
        wrapper.eq(this.getHasWeight() != null, "has_weight", this.getHasWeight());
        wrapper.in(this.getHasWeightList() != null && this.getHasWeightList().size() > 0, "has_weight", this.getHasWeightList());
        wrapper.eq(this.getIsMain() != null, "is_main", this.getIsMain());
        wrapper.in(this.getIsMainList() != null && this.getIsMainList().size() > 0, "is_main", this.getIsMainList());
        wrapper.eq(this.getMaxScore() != null, "max_score", this.getMaxScore());
        if (this.getSubjectAlias() != null) {
            if (this.getSubjectAlias().startsWith("%") && this.getSubjectAlias().endsWith("%")) {
                wrapper.like("subject_alias", this.getSubjectAlias().substring(1, this.getSubjectAlias().length() - 1));
            } else if (this.getSubjectAlias().startsWith("%") && !this.getSubjectAlias().endsWith("%")) {
                wrapper.likeLeft("subject_alias", this.getSubjectAlias().substring(1));
            } else if (this.getSubjectAlias().endsWith("%")) {
                wrapper.likeRight("subject_alias", this.getSubjectAlias().substring(0, this.getSubjectAlias().length() - 1));
            } else {
                wrapper.eq("subject_alias", this.getSubjectAlias());
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
