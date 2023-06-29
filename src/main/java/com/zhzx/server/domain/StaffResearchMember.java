/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：教研组组员表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.rest.res.ApiCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`sys_staff_research_member`")
@ApiModel(value = "StaffResearchMember", description = "教研组组员表")
public class StaffResearchMember extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * 教师ID sys_staff.id
     */
    @TableField(value = "staff_id")
    @ApiModelProperty(value = "教师ID sys_staff.id", required = true)
    private Long staffId;
    /**
     * 教师ID sys_staff.id
     */
    @ApiModelProperty(value = "教师ID sys_staff.id")
    @TableField(exist = false)
    private Staff staff;
    /**
     * 科目ID sys_subject.id
     */
    @TableField(value = "subject_id")
    @ApiModelProperty(value = "科目ID sys_subject.id", required = true)
    private Long subjectId;
    /**
     * 科目ID sys_subject.id
     */
    @ApiModelProperty(value = "科目ID sys_subject.id")
    @TableField(exist = false)
    private Subject subject;
    /**
     * 是否当值
     */
    @TableField(value = "is_current")
    @ApiModelProperty(value = "是否当值", required = true)
    private YesNoEnum isCurrent;
    /**
     * 是否组长
     */
    @TableField(value = "is_leader")
    @ApiModelProperty(value = "是否组长", required = true)
    private YesNoEnum isLeader;

    /**
     * 设置默认值
     */
    public StaffResearchMember setDefault() {
        return this;
    }

    /**
     * 数据验证
     */
    public Boolean validate(Boolean throwException) {
        if (this.getStaffId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "教师ID sys_staff.id不能为空！");
            return false;
        }
        if (this.getSubjectId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "科目ID sys_subject.id不能为空！");
            return false;
        }
        if (this.getIsCurrent() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "是否当值不能为空！");
            return false;
        }
        if (this.getIsLeader() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "是否组长不能为空！");
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
