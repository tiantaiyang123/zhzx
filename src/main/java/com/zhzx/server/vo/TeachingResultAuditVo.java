package com.zhzx.server.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TeachingResultAuditVo {

    @ApiModelProperty(value = "ID", required = true)
    private Long id;

    @ApiModelProperty(value = "操作类型", required = true)
    private String type;

    @ApiModelProperty(value = "拒绝原因")
    private String reason;
}
