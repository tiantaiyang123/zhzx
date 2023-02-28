/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.config;

import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.rest.res.ApiCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class ShiroExceptionHandler {

    @ResponseBody
    @ExceptionHandler(AuthenticationException.class)
    public ApiResponse handleBzException(ApiCode.ApiException e) {
        log.error("authentication error", e);
        return ApiResponse.fail(ApiCode.UNAUTHC);
    }

    @ResponseBody
    @ExceptionHandler(AuthorizationException.class)
    public ApiResponse handleException(Throwable e) {
        log.error("authorization error", e);
        return ApiResponse.fail(ApiCode.UNAUTHZ);
    }
}
