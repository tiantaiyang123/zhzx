package com.zhzx.server.dto;

import lombok.Data;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

import java.util.HashMap;
import java.util.Map;

@Data
public class MapResultHandlerDto<K, V> implements ResultHandler<Map<K, V>> {

    private final Map<K, V> mapRes = new HashMap<>();

    @Override
    @SuppressWarnings("all")
    public void handleResult(ResultContext<? extends Map<K, V>> resultContext) {
        Object o = resultContext.getResultObject();
        if (o instanceof Map) {
            Map map = (Map) o;
            mapRes.put((K) map.get("key"), (V) map.get("value"));
        }
    }
}
