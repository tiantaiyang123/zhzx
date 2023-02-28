package com.zhzx.server.config;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.zhzx.server.dto.wx.TokenDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

/**
 * Created by 11345 on 2020/12/1.
 */
@Slf4j
@Configuration
public class TokenCacheConfig {

    private static LoadingCache<String, TokenDto> localcache = CacheBuilder.newBuilder()
            .maximumSize(5000)
            .build(new CacheLoader<String, TokenDto>() {
                @Override
                public TokenDto load(String key) throws Exception {
                    return new TokenDto();
                }
            });

    /*
     * 添加本地缓存
     * */
    public static void setKey(String key, TokenDto value) {
        localcache.put(key, value);
    }

    /*
    * 删除本地缓存
    * */
    public static void removeKey(String key) {
        localcache.invalidate(key);
    }

    /*
     * 得到本地缓存
     * */
    public static TokenDto getKey(String key) {
        TokenDto value = null;
        try {
            value = localcache.get(key);
            if (value.getToken() == "" || value.getToken() == null) {
                return null;
            }
            return value;
        } catch (ExecutionException e) {
            log.error("getKey()方法错误",e);
        }
        return null;
    }
}
