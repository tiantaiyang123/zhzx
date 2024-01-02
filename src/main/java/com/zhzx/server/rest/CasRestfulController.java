package com.zhzx.server.rest;

import com.alibaba.fastjson.JSONObject;
import com.zhzx.server.domain.User;
import com.zhzx.server.dto.CasDto;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.UserService;
import com.zhzx.server.util.JWTUtils;
import com.zhzx.server.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@Api(tags = "CasRestfulController", description = "cas")
@RequestMapping("/v1/cas")
public class CasRestfulController {

    @Value("${cas.ticket_validate_url}")
    private String ticketValidateUrl;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private UserService userService;

    @ApiOperation("验证票据同时生成本系统JWT")
    @PostMapping("/valid-ticket")
    public ApiResponse<Map<String, Object>> validTicket(@Validated @RequestBody CasDto casDto) throws Exception {
        JSONObject jsonObject = restTemplate.getForObject(ticketValidateUrl, JSONObject.class, casDto.getTicket(), casDto.getServiceName());
        String userId;
        if (null == jsonObject || jsonObject.getInteger("code") != 0
                || null ==(userId = jsonObject.getString("userId"))) {
            return ApiResponse.fail(ApiCode.UNAUTHZ);
        }

        User user = userService.selectByUsername(userId, YesNoEnum.NO);
        UserVo userVo = new UserVo();
        userVo.setUserInfo(user);
        Map<String, Object> map = new HashMap<>(4, 0.8f);
        map.put("userInfo", user);
        map.put("token", JWTUtils.sign(user.getUsername(), user.getPassword()));
        return ApiResponse.ok(map);
    }

}
