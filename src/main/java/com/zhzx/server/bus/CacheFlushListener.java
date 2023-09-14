package com.zhzx.server.bus;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.zhzx.server.misc.SpringUtils;
import com.zhzx.server.util.RedisUtil;

public class CacheFlushListener {

    @Subscribe
    @AllowConcurrentEvents
    public void flushCache(CacheFlushMessage cacheFlushMessage) {
        RedisUtil redisUtil = SpringUtils.getBean(RedisUtil.class);

        String opType = cacheFlushMessage.getOpType();
        switch (opType) {
            case "PUT": {
                Long expires = cacheFlushMessage.getExpires();
                if (null == expires || expires <= 0) {
                    redisUtil.set(cacheFlushMessage.getKeyPrefix() + cacheFlushMessage.getKey(), cacheFlushMessage.getObject());
                } else {
                    redisUtil.set(cacheFlushMessage.getKeyPrefix() + cacheFlushMessage.getKey(), cacheFlushMessage.getObject(), expires);
                }
                return;
            }
            case "DEL": {
                redisUtil.del(cacheFlushMessage.getKeyPrefix() + cacheFlushMessage.getKey());
                return;
            }
            default:
        }
    }

}
