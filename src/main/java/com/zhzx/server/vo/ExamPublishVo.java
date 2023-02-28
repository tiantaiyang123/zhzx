package com.zhzx.server.vo;

import com.zhzx.server.domain.ExamPublish;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ExamPublishVo extends ExamPublish {
    @ApiModelProperty(value = "考试ID列表", required = true)
    private List<Long> examIds;
}
