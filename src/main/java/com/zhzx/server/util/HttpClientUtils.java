package com.zhzx.server.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author:fengtaowen
 * @date:2024/4/15 10:10
 * @version:1.0
 */
@Slf4j
public class HttpClientUtils {

    /**
     * 调用第三方接口
     * get请求
     */
    public static String doGet(String username, String mobile) {
        if (!username.isEmpty() && !mobile.isEmpty()) {
            try {
                username = URLEncoder.encode(username, StandardCharsets.UTF_8.toString());
                mobile = URLEncoder.encode(mobile, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String url = "http://dc.njzhzx.net/api/api/app/checkMobile/{name}/{mobile}";
            url = url.replace("{name}", username).replace("{mobile}", mobile);
            log.info("url is {}",url);
            try {
                HttpClient client = new HttpClient();
                GetMethod get = new GetMethod(url);
                client.executeMethod(get);
                String response = get.getResponseBodyAsString();
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            return "用户名手机号码错误";
        }
        return null;
    }


    /**
     * 调取第三方接口-->用于验证e办公平台是否能够正常登录
     * post请求
     */
    public static String doPost(String url, String requestBody) {
        try {
            HttpClient client = new HttpClient();
            PostMethod post = new PostMethod(url);
            if (requestBody != null) {
                RequestEntity requestEntity = new StringRequestEntity(requestBody, "application/json", "UTF-8");
                post.setRequestEntity(requestEntity);
                post.setRequestHeader("Content-Type", "application/json");
            }
            //System.out.println(post.getRequestHeaders());
            log.info("requestBody is {}",requestBody);
            //发送http请求
            String respStr = "";
            try {
                client.executeMethod(post);
                respStr = post.getResponseBodyAsString();
            } catch (HttpException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return respStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
