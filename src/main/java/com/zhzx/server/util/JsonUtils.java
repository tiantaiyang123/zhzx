/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.*;

public class JsonUtils {

    private static final ObjectMapper JSON = new ObjectMapper();

    static {
        JSON.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        JSON.configure(SerializationFeature.INDENT_OUTPUT, Boolean.TRUE);
    }

    public static Object fromJson(String str, Class<?> cls) throws JsonProcessingException {
        return JSON.readValue(str, cls);
    }


    public static String toJson(Object obj) {
        try {
            return JSON.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Map<String, Object> objectToMap(Object obj) {
        try {
            return JSON.readValue(toJson(obj), new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
