package com.zhzx.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 定时任务配置
 * Created by macro on 2019/4/8.
 */
@Configuration
@EnableScheduling
public class SpringTaskConfig {
    @Bean
    public TaskScheduler taskScheduler(){
        ThreadPoolTaskScheduler taskExecutor = new ThreadPoolTaskScheduler();
        taskExecutor.setPoolSize(1024);
        return taskExecutor;
    }
}
