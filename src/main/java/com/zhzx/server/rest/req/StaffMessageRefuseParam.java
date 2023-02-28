/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：老师信息接收管理表
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
import com.zhzx.server.domain.StaffMessageRefuse;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "老师信息接收管理表参数", description = "")
public class StaffMessageRefuseParam implements Serializable {
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
     * staff_id
     */
    @ApiModelProperty(value = "staff_id")
    private Long staffId;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;
    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String remark;
    /**
     * 是否接收此类消息
     */
    @ApiModelProperty(value = "是否接收此类消息")
    private YesNoEnum status;
    /**
     * 是否接收此类消息 IN值List
     */
    @ApiModelProperty(value = "是否接收此类消息 IN值List")
    private List<String> statusList;
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
    public QueryWrapper<StaffMessageRefuse> toQueryWrapper() {
        QueryWrapper<StaffMessageRefuse> wrapper = Wrappers.<StaffMessageRefuse>query();
        wrapper.eq(this.getId() != null, "id", this.getId());
        wrapper.in(this.getIdList() != null && this.getIdList().size() > 0, "id", this.getIdList());
        wrapper.eq(this.getStaffId() != null, "staff_id", this.getStaffId());
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
        wrapper.eq(this.getStatus() != null, "status", this.getStatus());
        wrapper.in(this.getStatusList() != null && this.getStatusList().size() > 0, "status", this.getStatusList());
        wrapper.eq(this.getCreateTime() != null, "create_time", this.getCreateTime());
        wrapper.ge(this.getCreateTimeFrom() != null, "create_time", this.getCreateTimeFrom());
        wrapper.lt(this.getCreateTimeTo() != null, "create_time", this.getCreateTimeTo());
        wrapper.eq(this.getUpdateTime() != null, "update_time", this.getUpdateTime());
        wrapper.ge(this.getUpdateTimeFrom() != null, "update_time", this.getUpdateTimeFrom());
        wrapper.lt(this.getUpdateTimeTo() != null, "update_time", this.getUpdateTimeTo());
        return wrapper;
    }

}
