package com.zhzx.server.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.zhzx.server.domain.*;
import com.zhzx.server.enums.CommentStateEnum;
import com.zhzx.server.enums.YesNoEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by A2 on 2022/1/11.
 */
@Data
public class CommentTaskDto extends CommentTask{

    @TableField(exist = false)
    @ApiModelProperty(value = "填写人")
    private String infoFillPeople;

    @TableField(exist = false)
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @TableField(exist = false)
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @TableField(exist = false)
    @ApiModelProperty(value = "意见内容")
    private String commentContent;

    @TableField(exist = false)
    @ApiModelProperty(value = "是否需要校长室批示")
    private YesNoEnum needInstruction;

    @TableField(exist = false)
    @ApiModelProperty(value = "校长室批示")
    private String instructions;

    @TableField(exist = false)
    @ApiModelProperty(value = "校长批示状态")
    private CommentStateEnum processState;

    @TableField(exist = false)
    @ApiModelProperty(value = "部门名称")
    private String functionDepartmentName;

    @TableField(exist = false)
    @ApiModelProperty(value = "部门名称")
    private String classify;

    @TableField(exist = false)
    @ApiModelProperty(value = "图片")
    private List<CommentImages> commentImagesList;

    private String schoolyardName;
}
