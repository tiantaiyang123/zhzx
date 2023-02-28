package com.zhzx.server.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ConsumeRecordVo {

    @ApiModelProperty(value = "开始日期")
    private String beginDate;

    @ApiModelProperty(value = "结束日期")
    private String endDate;

    @ApiModelProperty(value = "账户")
    private Long a_Accounts;

}
