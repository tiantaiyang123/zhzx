/**
 * 项目：中华中学管理平台
 * 模型分组：一日常规管理
 * 模型名称：意见与建议表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zhzx.server.enums.ClassifyEnum;
import com.zhzx.server.enums.CommentStateEnum;
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
@TableName("`day_comment`")
@ApiModel(value = "Comment", description = "意见与建议表")
public class Comment extends BaseDomain {
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
    private List<CommentImages> commentImagesList;
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
     * 开始时间
     */
    @TableField(value = "start_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间", required = true)
    private java.util.Date startTime;
    /**
     * 结束时间
     */
    @TableField(value = "end_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间", required = true)
    private java.util.Date endTime;
    /**
     * 填报人
     */
    @TableField(value = "editor")
    @ApiModelProperty(value = "填报人", required = true)
    private String editor;
    /**
     * 意见与建议 可以从sys_label.classify == YRCG_YJJY选择，也可以录入
     */
    @TableField(value = "content")
    @ApiModelProperty(value = "意见与建议 可以从sys_label.classify == YRCG_YJJY选择，也可以录入", required = true)
    private String content;
    /**
     * 状态
     */
    @TableField(value = "state")
    @ApiModelProperty(value = "状态", required = true)
    private CommentStateEnum state;
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

    /**
     * 设置默认值
     */
    public Comment setDefault() {
        if (this.getState() == null) {
            this.setState(CommentStateEnum.TO_BE_PUSHED);
        }
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
        if (this.getStartTime() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "开始时间不能为空！");
            return false;
        }
        if (this.getEndTime() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "结束时间不能为空！");
            return false;
        }
        if (this.getEditor() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "填报人不能为空！");
            return false;
        }
        if (this.getContent() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "意见与建议 可以从sys_label.classify == YRCG_YJJY选择，也可以录入不能为空！");
            return false;
        }
        if (this.getState() == null) {
            if (throwException) throw new ApiCode.ApiException(-1, "状态不能为空！");
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
