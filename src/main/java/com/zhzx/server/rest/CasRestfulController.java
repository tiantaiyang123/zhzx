package com.zhzx.server.rest;

import com.zhzx.server.domain.User;
import com.zhzx.server.dto.CasDto;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.repository.AuthorityMapper;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.UserService;
import com.zhzx.server.util.JWTUtils;
import com.zhzx.server.util.StringUtils;
import com.zhzx.server.util.TreeUtils;
import com.zhzx.server.vo.AuthorityVo;
import com.zhzx.server.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@Api(tags = "CasRestfulController", description = "cas")
@RequestMapping("/v1/cas")
public class CasRestfulController {

    @Value("${cas.ticket_validate_url}")
    private String ticketValidateUrl;
    @Value("${cas.log_out_url}")
    private String logoutUrl;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private UserService userService;
    @Resource
    private AuthorityMapper authorityMapper;

    @ApiOperation("验证票据同时生成本系统JWT")
    @PostMapping("/valid-ticket")
    public ApiResponse<Map<String, Object>> validTicket(@Validated @RequestBody CasDto casDto) throws Exception {
        String stValidateXMLStr = restTemplate.getForObject(ticketValidateUrl, String.class, casDto.getTicket(), casDto.getServiceName());
        if (StringUtils.isNullOrEmpty(stValidateXMLStr) || Objects.requireNonNull(stValidateXMLStr).contains("cas:authenticationFailure")) {
            return ApiResponse.fail(ApiCode.UNAUTHZ);
        }

        int indexStart = stValidateXMLStr.indexOf("<cas:user>");
        int indexEnd = stValidateXMLStr.indexOf("</cas:user>");
        String userId = stValidateXMLStr.substring(indexStart + 10, indexEnd);

        User user = userService.selectByUsername(userId, YesNoEnum.NO);
        UserVo userVo = new UserVo();
        userVo.setUserInfo(user);

        List<AuthorityVo> authorityVos = authorityMapper.selectAuthoritiesByUserId(user.getId());
        List<AuthorityVo> authorityVoList = TreeUtils.listToTree(authorityVos);
        userVo.setAuthorityList(authorityVoList);

        Map<String, Object> map = new HashMap<>(4, 0.8f);
        map.put("userInfo", user);
        map.put("token", JWTUtils.sign(user.getUsername(), user.getPassword()));
        return ApiResponse.ok(map);
    }

    @ApiOperation("登出")
    @GetMapping("/log-out")
    public void logOut(HttpServletResponse httpServletResponse) throws Exception {
        httpServletResponse.setStatus(302);
        httpServletResponse.setHeader("Location", logoutUrl);
    }

}
