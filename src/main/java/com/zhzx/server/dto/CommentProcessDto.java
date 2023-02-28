package com.zhzx.server.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.zhzx.server.domain.CommentImages;
import com.zhzx.server.domain.CommentProcess;
import com.zhzx.server.enums.ClassifyEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by A2 on 2022/1/11.
 */
@Data
public class CommentProcessDto extends CommentProcess{

    @TableField(exist = false)
    @ApiModelProperty(value = "填写人")
    private String infoFillPeople;

    @TableField(exist = false)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @TableField(exist = false)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @TableField(exist = false)
    @ApiModelProperty(value = "意见内容")
    private String commentContent;

    @TableField(exist = false)
    private List<CommentTaskDto> commentTaskDtoList;

    @TableField(exist = false)
    private String gradeDepartment;

    @TableField(exist = false)
    private String officeDepartment;

    @TableField(exist = false)
    private Map<Long,CommentTaskDto> taskDtoMap;

    @TableField(exist = false)
    private ClassifyEnum classify;

    @TableField(exist = false)
    private Map<Object,Object> columnMap;

    @TableField(exist = false)
    private List<CommentImages> commentImagesList;

    private String schoolyardName;

}
