package com.zhzx.server.rest;

import com.zhzx.server.dto.annotation.TirAuth;
import com.zhzx.server.dto.xcx.WxXcxMessageDto;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.service.WxXcxMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@Api(tags = "ExternalController", description = "外部系统调用接口控制器")
@RequestMapping("/v1/external")
public class ExternalController {

    @Resource
    private WxXcxMessageService wxXcxMessageService;

    @PostMapping("/sync/wx/xcx/message")
    @ApiOperation("小程序消息同步")
    @TirAuth
    public ApiResponse<Void> syncWxXcxMessage(@RequestParam(name = "code") String code,
                                                @RequestBody List<WxXcxMessageDto> wxXcxMessageDtoList) {
        wxXcxMessageService.syncWxXcxMessage(code, wxXcxMessageDtoList);
        return ApiResponse.ok(null);
    }

}
