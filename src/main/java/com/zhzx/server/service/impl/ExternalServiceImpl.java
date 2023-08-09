package com.zhzx.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.zhzx.server.config.bean.TirConfiguration;
import com.zhzx.server.domain.User;
import com.zhzx.server.dto.CookieDto;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.ExternalService;
import com.zhzx.server.util.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Service
public class ExternalServiceImpl implements ExternalService {
    @Resource
    private TirConfiguration tirConfiguration;
    @Resource
    private RestTemplate restTemplate;

    @Override
    public CookieDto acquireTirCookie() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        String realName = user.getStaff().getName();
        String phone = user.getStaff().getPhone();
        if (StringUtils.isNullOrEmpty(realName) || StringUtils.isNullOrEmpty(phone)) {
            throw new ApiCode.ApiException(-1, "请先配置您的电话号码");
        }


        String code = getCode(realName, phone);
        List<String> headerCookieList = getCookie(phone, code);

        String redisSession = "";
        for (String headerCookie : headerCookieList) {
            String[] arr = headerCookie.split("[;]");
            for (String s : arr) {
                if (s.startsWith("REDISSESSION")) {
                    redisSession = s;
                    break;
                }
            }
        }

        CookieDto cookieDto = new CookieDto();
        cookieDto.setName("REDISSESSION");
        cookieDto.setValue(redisSession.replace("REDISSESSION=", ""));
        cookieDto.setDomain("dc.njzhzx.net");
        cookieDto.setPath("/");

        return cookieDto;
    }

    private String getCode(String realName, String phone) {
        JSONObject jsonObject = restTemplate.getForObject(
                tirConfiguration.getPcLoginCodeUrl(),
                JSONObject.class,
                new HashMap<String, Object>(){
                    {
                        this.put("name", realName);
                        this.put("mobile", phone);
                    }
                }
        );
        if (null == jsonObject || jsonObject.getInteger("code") != 0) {
            throw new ApiCode.ApiException(-2, "获取code服务内部出错");
        }
        String par1 = jsonObject.getString("par1");
        if (StringUtils.isNullOrEmpty(par1)) {
            throw new ApiCode.ApiException(-3, "获取code参数失败");
        }
        return par1;
    }

    private List<String> getCookie(String phone, String code) {
        HttpHeaders httpHeaders = new HttpHeaders();
        ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(
                tirConfiguration.getPcLoginCookieUrl() + "?mobile={mobile}&par1={par1}",
                HttpMethod.POST,
                new HttpEntity<>(null, httpHeaders),
                JSONObject.class,
                phone,
                code
        );

        if (responseEntity.getStatusCodeValue() != HttpStatus.OK.value()) {
            throw new ApiCode.ApiException(-4, "获取cookie请求失败");
        }

        JSONObject jsonObject = responseEntity.getBody();
        if (jsonObject == null || jsonObject.getInteger("code") != 0) {
            throw new ApiCode.ApiException(-5, "获取cookie服务内部出错");
        }

        List<String> headerCookieList = responseEntity.getHeaders().get("Set-Cookie");
        if (CollectionUtils.isEmpty(headerCookieList)) {
            throw new ApiCode.ApiException(-6, "获取cookie失败");
        }
        return headerCookieList;
    }

}
