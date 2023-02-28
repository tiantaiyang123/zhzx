/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.misc.hasor.spi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.hasor.dataway.spi.ApiInfo;
import net.hasor.dataway.spi.SerializationChainSpi;
import net.hasor.web.MimeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Set;

@Component
public class ApiResponseSpi implements SerializationChainSpi {
    @Autowired
    private ObjectMapper objectMapper;

    public ApiResponseSpi(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Object doSerialization(ApiInfo apiInfo, MimeType mimeType, Object o) {
        if (o instanceof LinkedHashMap) {
            LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) o;
            map.put("result", map.get("value"));
            map.remove("value");
            String message = (String) map.get("message");
            // 去掉消息中的行数信息
            map.put("message", message.replaceFirst("\\[.*\\]", ""));
        }
        try {
            return this.objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return o;
    }
}
