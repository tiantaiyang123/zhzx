package com.zhzx.server.rest;

import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.util.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: zthu
 * @Date: 2020-09-16
 */
@Api(tags = "文件上传")
@Slf4j
@RestController
@RequestMapping("/v1/system/common")
public class UploadController {
    @Value("${upload.DOMAIN_URL}")
    private String uploadUrl;

    @ApiOperation("文件上传(本地)")
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public ApiResponse upload(@RequestParam MultipartFile file) {
        String upload = FileUtil.upload(file);
        return ApiResponse.ok(uploadUrl+upload);
    }
}
