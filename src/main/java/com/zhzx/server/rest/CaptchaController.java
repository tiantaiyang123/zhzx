package com.zhzx.server.rest;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.lang.UUID;
import com.zhzx.server.dto.CaptchaDto;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
@Api(tags = "CaptchaController", description = "图形验证码")
@RequestMapping("/v1/captcha")
public class CaptchaController {

    private static final Map<String, String> MAP_CAPTCHA = new ConcurrentHashMap<>();
    private static final Integer CODE_COUNT = 4;

    @GetMapping("/get")
    @ApiOperation("获取验证码")
    @SneakyThrows
    public ApiResponse<CaptchaDto> get() {
        CaptchaDto captchaDto = new CaptchaDto();
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(150, 40, CODE_COUNT, 100);

        long timeStamp = System.currentTimeMillis();
        String uuid = UUID.randomUUID().toString();
        String correctCode = lineCaptcha.getCode();
        MAP_CAPTCHA.put(uuid, correctCode + timeStamp);

        captchaDto.setImageUrl("data:image/png;base64," + lineCaptcha.getImageBase64());
        captchaDto.setCode(uuid);
        captchaDto.setTimeStamp(timeStamp);

        return ApiResponse.ok(captchaDto);
    }

    @ApiOperation("校验验证码")
    @PostMapping("/verify")
    public ApiResponse<Map<String, Object>> verify(@RequestParam(name = "code") String code,
                                                  @RequestParam(name = "authingCode") String authingCode) {
        String correctCode = MAP_CAPTCHA.remove(code);
        if (StringUtils.isNullOrEmpty(correctCode) || !correctCode.substring(0, CODE_COUNT).equals(authingCode)) {
            return ApiResponse.fail(ApiCode.FAILED);
        }
        return ApiResponse.ok(null);
    }

}
