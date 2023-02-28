/**
* 项目：中华中学管理平台
* 模型分组：系统管理
* 模型名称：宿舍表
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
import com.zhzx.server.domain.Dormitory;

@Data
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "宿舍表参数", description = "")
public class DormitoryParam implements Serializable {
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
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;
    /**
     * 楼栋
     */
    @ApiModelProperty(value = "楼栋")
    private String building;
    /**
     * 楼层
     */
    @ApiModelProperty(value = "楼层")
    private String floor;
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
    public QueryWrapper<Dormitory> toQueryWrapper() {
        QueryWrapper<Dormitory> wrapper = Wrappers.<Dormitory>query();
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
        if (this.getBuilding() != null) {
            if (this.getBuilding().startsWith("%") && this.getBuilding().endsWith("%")) {
                wrapper.like("building", this.getBuilding().substring(1, this.getBuilding().length() - 1));
            } else if (this.getBuilding().startsWith("%") && !this.getBuilding().endsWith("%")) {
                wrapper.likeLeft("building", this.getBuilding().substring(1));
            } else if (this.getBuilding().endsWith("%")) {
                wrapper.likeRight("building", this.getBuilding().substring(0, this.getBuilding().length() - 1));
            } else {
                wrapper.eq("building", this.getBuilding());
            }
        }
        if (this.getFloor() != null) {
            if (this.getFloor().startsWith("%") && this.getFloor().endsWith("%")) {
                wrapper.like("floor", this.getFloor().substring(1, this.getFloor().length() - 1));
            } else if (this.getFloor().startsWith("%") && !this.getFloor().endsWith("%")) {
                wrapper.likeLeft("floor", this.getFloor().substring(1));
            } else if (this.getFloor().endsWith("%")) {
                wrapper.likeRight("floor", this.getFloor().substring(0, this.getFloor().length() - 1));
            } else {
                wrapper.eq("floor", this.getFloor());
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
