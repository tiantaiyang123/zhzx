/**
 * 项目：中华中学管理平台
 * 模型分组：一日常规管理
 * 模型名称：偶发事件表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zhzx.server.enums.ClassifyEnum;
import com.zhzx.server.rest.res.ApiCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

@Data
@Builder(builderMethodName = "newBuilder")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("`day_incident`")
@ApiModel(value = "Incident", description = "偶发事件表")
public class Incident extends BaseDomain {
    /**
     * ID系统自动生成
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID系统自动生成", required = true)
    private Long id;
    /**
     * 年级 sys_grade.id
     */
    @TableField(value = "schoolyard_id")
    @ApiModelProperty(value = "校区 sys_schoolyard.id", required = true)
    private Long schoolyardId;
    @TableField(exist = false)
    @ApiModelProperty(value = "校区 sys_schoolyard.id", required = true)
    private Schoolyard schoolyard;
    /**
     * ID系统自动生成
     */
    @ApiModelProperty(value = "ID系统自动生成")
    @TableField(exist = false)
    private List<IncidentImages> incidentImagesList;
    /**
     * 一日常规分类
     */
    @TableField(value = "classify")
    @ApiModelProperty(value = "一日常规分类", required = true)
    private ClassifyEnum classify;
    /**
     * 项目名称
     */
    @TableField(value = "item_name")
    @ApiModelProperty(value = "项目名称", required = true)
    private String itemName;
    /**
     * 一日常规ID (classify为数据表名)
     */
    @TableField(value = "daily_routine_id")
    @ApiModelProperty(value = "一日常规ID (classify为数据表名)", required = true)
    private Long dailyRoutineId;
    /**
     * 内容 可以从sys_label.classify == YRCG_OFSJ选择，也可以录入
     */
    @TableField(value = "content")
    @ApiModelProperty(value = "内容 可以从sys_label.classify == YRCG_OFSJ选择，也可以录入", required = true)
    private String content;
    /**
     * 
     */
    @TableField(value = "create_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "", required = true)
    private java.util.Date createTime;
    /**
     * 
     */
    @TableField(value = "update_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "", required = true)
    private java.util.Date updateTime;

    @TableField(exist = false)
    private String leaderName;

    /**
     * 设置默认值
     */
    public Incident setDefault() {
        if (this.getCreateTime() == null) {
            this.setCreateTime(new java.util.Date());
        }
        if (this.getUpdateTime() == null) {
            this.setUpdateTime(new java.util.Date());
        }
        return this;
    }

    /**
     * 数据验证
     */
    public Boolean validate(Boolean throwException) {
        if (this.getClassify() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "一日常规分类不能为空！");
            return false;
        }
        if (this.getSchoolyardId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "校区 sys_schoolyard.id不能为空！");
            return false;
        }
        if (this.getItemName() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "项目名称不能为空！");
            return false;
        }
        if (this.getDailyRoutineId() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "一日常规ID (classify为数据表名)不能为空！");
            return false;
        }
        if (this.getContent() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "内容 可以从sys_label.classify == YRCG_OFSJ选择，也可以录入不能为空！");
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
