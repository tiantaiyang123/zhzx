package com.zhzx.server.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ExamGoalSubTotalVo {
    @ApiModelProperty(value = "分段信息")
    private List<ExamGoalSubVo> examGoalSubVos;

    @ApiModelProperty(value = "班级列表")
    private List<Long> clazzIds;

    @ApiModelProperty(value = "01---分段设置02---工作台")
    private String type;

    @ApiModelProperty(value = "分段性质")
    private String clazzNature;

    @ApiModelProperty(value = "分段科目")
    private Long subjectId;

    @ApiModelProperty(value = "分段考试")
    private Long examId;
}
