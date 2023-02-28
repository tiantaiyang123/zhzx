package com.zhzx.server.rest;

import com.zhzx.server.service.WxSendMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by 11345 on 2022/6/14.
 */
@Slf4j
@RestController
@Api(tags = "WxCallBackController", description = "工作台")
@RequestMapping("/v1/system/wx")
public class WxCallBackController {
    @Resource
    private WxSendMessageService wxSendMessageService;


    @GetMapping("/callback")
    @ApiOperation("回调")
    public void applicationMessageGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        wxSendMessageService.applicationMessageGet(response,request);
    }

    @PostMapping("/callback")
    @ApiOperation("回调")
    public void applicationMessagePost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        wxSendMessageService.applicationMessagePost(response,request);
    }
}
