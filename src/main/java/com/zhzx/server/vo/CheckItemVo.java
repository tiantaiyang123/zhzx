package com.zhzx.server.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.zhzx.server.domain.BaseDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class CheckItemVo extends BaseDomain {
    /**
     * ID
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "ID", required = true)
    private Integer id;
    /**
     * 班次
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "班次", required = true)
    private String shift;

    /**
     * 开始时间
     */
    @TableField(exist = false)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间", required = true)
    private java.util.Date startTime;

    /**
     * 结束时间
     */
    @TableField(exist = false)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间", required = true)
    private java.util.Date endTime;

    /**
     * 检查项目
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "检查项目", required = true)
    private String checkItem;

    /**
     * 检查项目
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "检查子项目", required = true)
    private String checkSubItem;

    /**
     * 检查结果
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "检查结果", required = true)
    private String checkResult;

    /**
     * 图片
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "图片", required = true)
    private String imageUrls;

    /**
     * 图片
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "意见与建议", required = true)
    private String comment;

    /**
     * 更新时间
     */
    @TableField(exist = false)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间", required = true)
    private java.util.Date updateTime;

    /**
     * 更新时间
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "领导")
    private String leaderName;
}
