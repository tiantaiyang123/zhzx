/**
* 项目：中华中学管理平台
* 模型分组：系统管理
* 模型名称：宿舍学生表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
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
import com.zhzx.server.domain.StudentDormitory;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "宿舍学生表参数", description = "")
public class StudentDormitoryParam implements Serializable {
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
     * 学生id
     */
    @ApiModelProperty(value = "学生id")
    private Long studentId;
    /**
     * 宿舍id
     */
    @ApiModelProperty(value = "宿舍id")
    private Long dormitoryId;
    /**
     * 宿舍床位
     */
    @ApiModelProperty(value = "宿舍床位")
    private String bed;
    /**
     * 是否启用
     */
    @ApiModelProperty(value = "是否启用")
    private YesNoEnum isDefault;
    /**
     * 是否启用 IN值List
     */
    @ApiModelProperty(value = "是否启用 IN值List")
    private List<String> isDefaultList;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
    /**
     * 创建时间 下限值(大于等于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间 下限值(大于等于)")
    private java.util.Date createTimeFrom;
    /**
     * 创建时间 上限值(小于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间 上限值(小于)")
    private java.util.Date createTimeTo;
    /**
     * 更新时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
    /**
     * 更新时间 下限值(大于等于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间 下限值(大于等于)")
    private java.util.Date updateTimeFrom;
    /**
     * 更新时间 上限值(小于)
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间 上限值(小于)")
    private java.util.Date updateTimeTo;

    /**
     * 将查询参数转化为查询Wrapper
     */
    public QueryWrapper<StudentDormitory> toQueryWrapper() {
        QueryWrapper<StudentDormitory> wrapper = Wrappers.<StudentDormitory>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getStudentId() != null, "student_id", this.getStudentId());
        wrapper.eq(this.getDormitoryId() != null, "dormitory_id", this.getDormitoryId());
        if (this.getBed() != null) {
            if (this.getBed().startsWith("%") && this.getBed().endsWith("%")) {
                wrapper.like("bed", this.getBed().substring(1, this.getBed().length() - 1));
            } else if (this.getBed().startsWith("%") && !this.getBed().endsWith("%")) {
                wrapper.likeLeft("bed", this.getBed().substring(1));
            } else if (this.getBed().endsWith("%")) {
                wrapper.likeRight("bed", this.getBed().substring(0, this.getBed().length() - 1));
            } else {
                wrapper.eq("bed", this.getBed());
            }
        }
        wrapper.eq(this.getIsDefault() != null, "is_default", this.getIsDefault());
        wrapper.in(this.getIsDefaultList() != null && this.getIsDefaultList().size() > 0, "is_default", this.getIsDefaultList());
        wrapper.eq(this.getCreateTime() != null, "create_time", this.getCreateTime());
        wrapper.ge(this.getCreateTimeFrom() != null, "create_time", this.getCreateTimeFrom());
        wrapper.lt(this.getCreateTimeTo() != null, "create_time", this.getCreateTimeTo());
        wrapper.eq(this.getUpdateTime() != null, "update_time", this.getUpdateTime());
        wrapper.ge(this.getUpdateTimeFrom() != null, "update_time", this.getUpdateTimeFrom());
        wrapper.lt(this.getUpdateTimeTo() != null, "update_time", this.getUpdateTimeTo());
        return wrapper;
    }

}
