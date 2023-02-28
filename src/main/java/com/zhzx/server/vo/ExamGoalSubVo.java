package com.zhzx.server.vo;

import com.zhzx.server.enums.ExamCompareEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ExamGoalSubVo {

    @ApiModelProperty(value = "分数", required = true)
    private Integer score;

    @ApiModelProperty(value = "类型枚举", required = true)
    private ExamCompareEnum op;

    @ApiModelProperty(value = "列样式", required = true)
    private ExamGoalSubStyleVo style;

}
