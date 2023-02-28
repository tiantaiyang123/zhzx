package com.zhzx.server.util;

import com.zhzx.server.rest.res.ApiCode;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.Map;

public class HttpUtils {

    private static final RequestConfig requestConfig;

    static{
        requestConfig = RequestConfig.custom().setConnectTimeout(10000)
                                                .setSocketTimeout(10000)
                .setConnectionRequestTimeout(5000).build();
    }

    public static String doGet(String url, Map<String, String> map) throws Exception {
        String str = "";
        String res = addParam(url, map);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet get = new HttpGet(res);
        get.setConfig(requestConfig);
        HttpResponse response = httpclient.execute(get);
        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            throw new ApiCode.ApiException(response.getStatusLine().getStatusCode(), "发送请求失败：" + response.getStatusLine().getReasonPhrase());
        }
        str = EntityUtils.toString(response.getEntity());
        httpclient.close();
        return str;
    }

    public static String addParam(String url, Map<String, String> map) throws Exception {
        URIBuilder uriBuilder = new URIBuilder(url);
        if (map != null) {
            map.forEach((k, v) -> {
                if (v != null) {
                    uriBuilder.addParameter(k, v);
                }
            });
            return uriBuilder.build().toString();
        }
        return url;
    }

}
