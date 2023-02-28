/**
 * 项目：中华中学管理平台
 * 模型分组：一日常规管理
 * 模型名称：教师值班班级表
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
import com.zhzx.server.domain.TeacherDutyClazz;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "教师值班班级表参数", description = "")
public class TeacherDutyClazzParam implements Serializable {
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
     * 教师值班ID day_teacher_duty.id
     */
    @ApiModelProperty(value = "教师值班ID day_teacher_duty.id")
    private Long teacherDutyId;
    /**
     * 班级ID sys_clazz.id
     */
    @ApiModelProperty(value = "班级ID sys_clazz.id")
    private Long clazzId;
    /**
     * 是否确认
     */
    @ApiModelProperty(value = "是否确认")
    private YesNoEnum isComfirm;
    /**
     * 是否确认 IN值List
     */
    @ApiModelProperty(value = "是否确认 IN值List")
    private List<String> isComfirmList;
    /**
     * 值班领导是否确认
     */
    @ApiModelProperty(value = "值班领导是否确认")
    private YesNoEnum isLeaderComfirm;
    /**
     * 值班领导是否确认 IN值List
     */
    @ApiModelProperty(value = "值班领导是否确认 IN值List")
    private List<String> isLeaderComfirmList;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<TeacherDutyClazz> toQueryWrapper() {
        QueryWrapper<TeacherDutyClazz> wrapper = Wrappers.<TeacherDutyClazz>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getTeacherDutyId() != null, "teacher_duty_id", this.getTeacherDutyId());
        wrapper.eq(this.getClazzId() != null, "clazz_id", this.getClazzId());
        wrapper.eq(this.getIsComfirm() != null, "is_comfirm", this.getIsComfirm());
        wrapper.in(this.getIsComfirmList() != null && this.getIsComfirmList().size() > 0, "is_comfirm", this.getIsComfirmList());
        wrapper.eq(this.getIsLeaderComfirm() != null, "is_leader_comfirm", this.getIsLeaderComfirm());
        wrapper.in(this.getIsLeaderComfirmList() != null && this.getIsLeaderComfirmList().size() > 0, "is_leader_comfirm", this.getIsLeaderComfirmList());
        return wrapper;
    }

}
