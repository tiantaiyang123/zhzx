package com.zhzx.server.config;

import com.google.common.eventbus.AsyncEventBus;
import com.zhzx.server.bus.CacheFlushListener;
import com.zhzx.server.rest.res.ApiCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Configuration
@AutoConfigureAfter({ ThreadPoolConfig.class })
public class EventBusConfiguration {

    @Bean
    public AsyncEventBus createEventBus(ThreadPoolExecutor threadPoolExecutor) {
        AsyncEventBus asyncEventBus = new AsyncEventBus(threadPoolExecutor, (throwable, subscriberExceptionContext) -> {
            throw new ApiCode.ApiException(-1, "bus异常： " + throwable.getMessage());
        });
        asyncEventBus.register(new CacheFlushListener());
        log.info("-----EVENT BUS 初始化完毕-----");
        return asyncEventBus;
    }
}
