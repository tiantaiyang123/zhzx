package com.zhzx.server.bus;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CacheFlushMessage {
    private String key;
    private String keyPrefix;
    private Long expires;
    private Object object;
    private String opType;
}
