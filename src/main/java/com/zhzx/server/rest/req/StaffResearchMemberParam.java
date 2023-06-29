/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：教研组组员表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest.req;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhzx.server.domain.StaffResearchMember;
import com.zhzx.server.enums.YesNoEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "教研组组员表参数", description = "")
public class StaffResearchMemberParam implements Serializable {
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
     * 教师ID sys_staff.id
     */
    @ApiModelProperty(value = "教师ID sys_staff.id")
    private Long staffId;
    /**
     * 科目ID sys_subject.id
     */
    @ApiModelProperty(value = "科目ID sys_subject.id")
    private Long subjectId;
    /**
     * 是否当值
     */
    @ApiModelProperty(value = "是否当值")
    private YesNoEnum isCurrent;
    /**
     * 是否当值 IN值List
     */
    @ApiModelProperty(value = "是否当值 IN值List")
    private List<String> isCurrentList;
    /**
     * 是否组长
     */
    @ApiModelProperty(value = "是否组长")
    private YesNoEnum isLeader;
    /**
     * 是否组长 IN值List
     */
    @ApiModelProperty(value = "是否组长 IN值List")
    private List<String> isLeaderList;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<StaffResearchMember> toQueryWrapper() {
        QueryWrapper<StaffResearchMember> wrapper = Wrappers.<StaffResearchMember>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getStaffId() != null, "staff_id", this.getStaffId());
        wrapper.eq(this.getSubjectId() != null, "subject_id", this.getSubjectId());
        wrapper.eq(this.getIsCurrent() != null, "is_current", this.getIsCurrent());
        wrapper.in(this.getIsCurrentList() != null && this.getIsCurrentList().size() > 0, "is_current", this.getIsCurrentList());
        wrapper.eq(this.getIsLeader() != null, "is_leader", this.getIsLeader());
        wrapper.in(this.getIsLeaderList() != null && this.getIsLeaderList().size() > 0, "is_leader", this.getIsLeaderList());
        return wrapper;
    }

}
