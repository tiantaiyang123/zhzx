/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：年级表
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
import com.zhzx.server.domain.Grade;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "年级表参数", description = "")
public class GradeParam implements Serializable {
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
     * 校区ID sys_schoolyard.id
     */
    @ApiModelProperty(value = "校区ID sys_schoolyard.id")
    private Long schoolyardId;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;
    /**
     * 年级组长 多选 用[,]分割 sys_staff.name
     */
    @ApiModelProperty(value = "年级组长 多选 用[,]分割 sys_staff.name")
    private String gradeLeader;
    /**
     * 教研组长 多选 用[,]分割 sys_staff.name
     */
    @ApiModelProperty(value = "教研组长 多选 用[,]分割 sys_staff.name")
    private String teamLeader;
    /**
     * 学业等级A等比例
     */
    @ApiModelProperty(value = "学业等级A等比例")
    private Long academyRatioA;
    /**
     * 学业等级B等比例
     */
    @ApiModelProperty(value = "学业等级B等比例")
    private Long academyRatioB;
    /**
     * 学业等级C等比例
     */
    @ApiModelProperty(value = "学业等级C等比例")
    private Long academyRatioC;
    /**
     * 学业等级D等比例
     */
    @ApiModelProperty(value = "学业等级D等比例")
    private Long academyRatioD;
    /**
     * 学业等级E等比例
     */
    @ApiModelProperty(value = "学业等级E等比例")
    private Long academyRatioE;
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
    public QueryWrapper<Grade> toQueryWrapper() {
        QueryWrapper<Grade> wrapper = Wrappers.<Grade>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getSchoolyardId() != null, "schoolyard_id", this.getSchoolyardId());
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
        if (this.getGradeLeader() != null) {
            if (this.getGradeLeader().startsWith("%") && this.getGradeLeader().endsWith("%")) {
                wrapper.like("grade_leader", this.getGradeLeader().substring(1, this.getGradeLeader().length() - 1));
            } else if (this.getGradeLeader().startsWith("%") && !this.getGradeLeader().endsWith("%")) {
                wrapper.likeLeft("grade_leader", this.getGradeLeader().substring(1));
            } else if (this.getGradeLeader().endsWith("%")) {
                wrapper.likeRight("grade_leader", this.getGradeLeader().substring(0, this.getGradeLeader().length() - 1));
            } else {
                wrapper.eq("grade_leader", this.getGradeLeader());
            }
        }
        if (this.getTeamLeader() != null) {
            if (this.getTeamLeader().startsWith("%") && this.getTeamLeader().endsWith("%")) {
                wrapper.like("team_leader", this.getTeamLeader().substring(1, this.getTeamLeader().length() - 1));
            } else if (this.getTeamLeader().startsWith("%") && !this.getTeamLeader().endsWith("%")) {
                wrapper.likeLeft("team_leader", this.getTeamLeader().substring(1));
            } else if (this.getTeamLeader().endsWith("%")) {
                wrapper.likeRight("team_leader", this.getTeamLeader().substring(0, this.getTeamLeader().length() - 1));
            } else {
                wrapper.eq("team_leader", this.getTeamLeader());
            }
        }
        wrapper.eq(this.getAcademyRatioA() != null, "academy_ratio_a", this.getAcademyRatioA());
        wrapper.eq(this.getAcademyRatioB() != null, "academy_ratio_b", this.getAcademyRatioB());
        wrapper.eq(this.getAcademyRatioC() != null, "academy_ratio_c", this.getAcademyRatioC());
        wrapper.eq(this.getAcademyRatioD() != null, "academy_ratio_d", this.getAcademyRatioD());
        wrapper.eq(this.getAcademyRatioE() != null, "academy_ratio_e", this.getAcademyRatioE());
        wrapper.eq(this.getCreateTime() != null, "create_time", this.getCreateTime());
        wrapper.ge(this.getCreateTimeFrom() != null, "create_time", this.getCreateTimeFrom());
        wrapper.lt(this.getCreateTimeTo() != null, "create_time", this.getCreateTimeTo());
        wrapper.eq(this.getUpdateTime() != null, "update_time", this.getUpdateTime());
        wrapper.ge(this.getUpdateTimeFrom() != null, "update_time", this.getUpdateTimeFrom());
        wrapper.lt(this.getUpdateTimeTo() != null, "update_time", this.getUpdateTimeTo());
        return wrapper;
    }

}
