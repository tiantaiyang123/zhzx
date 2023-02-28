/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.config;

import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.rest.res.ApiCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @org.springframework.web.bind.annotation.ExceptionHandler(ApiCode.ApiException.class)
    public ApiResponse handleBzException(ApiCode.ApiException e) {
        return ApiResponse.fail(e.getCode(), e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @org.springframework.web.bind.annotation.ExceptionHandler(Throwable.class)
    public ApiResponse handleException(Throwable e) {
        log.error("server error", e);
        return ApiResponse.fail(ApiCode.SERVER_ERROR);
    }
}
