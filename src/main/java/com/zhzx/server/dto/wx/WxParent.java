package com.zhzx.server.dto.wx;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * Created by A2 on 2022/3/15.
 */
@Data
@ApiModel(value = "WxParent", description = "WxParent")
public class WxParent {
    private String parent_userid;

    private String relation;

    private String mobile;

    private Integer is_subscribe;
}
