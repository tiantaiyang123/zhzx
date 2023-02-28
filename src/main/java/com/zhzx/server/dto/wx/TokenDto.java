package com.zhzx.server.dto.wx;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * Created by A2 on 2022/3/15.
 */
@Data
@ApiModel(value = "TokenDto", description = "token 缓存对象")
public class TokenDto {
    private String token;

    private long currentTime;

    private long expireTime;

    private Integer errorNum;
}
