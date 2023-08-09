package com.zhzx.server.config.bean;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "xcx", ignoreUnknownFields = false)
@Configuration
@Getter
@Setter
public class TirConfiguration {
    // 小程序跳转链接过期时间
    private Long expireTime;
    // 获取小程序跳转链接认证参数
    private String authPrefix;
    // 获取小程序跳转链接地址
    private String requestUrl;
    // 获取通讯录
    private String chatBookUrl;
    // 获取校区
    private String schoolYardUrl;
    // pc免密登录获取code
    private String pcLoginCodeUrl;
    // pc免密登录获取cookie
    private String pcLoginCookieUrl;
}
