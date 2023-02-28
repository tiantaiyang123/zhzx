package com.zhzx.server.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ExamGoalSubStyleVo {

    @ApiModelProperty(value = "前景色", required = true)
    private String color;

    @ApiModelProperty(value = "背景色", required = true)
    private String background;

    @ApiModelProperty(value = "加粗", required = true)
    private String fontWeight;
}
