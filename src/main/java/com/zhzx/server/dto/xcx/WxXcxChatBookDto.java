package com.zhzx.server.dto.xcx;

import com.zhzx.server.domain.BaseDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class WxXcxChatBookDto extends BaseDomain {
    @ApiModelProperty(value = "首字母")
    private String code;
    @ApiModelProperty(value = "人员列表")
    private List<WxXcxContactsDto> list;
}
