package com.zhzx.server.rest;

import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.util.FileUtil;
import com.zhzx.server.util.SFTPUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Slf4j
@RestController
@RequestMapping("/api/file")
@Api(tags = "FileController", description = "文件上传")
public class FileController {

    @Resource
    private SFTPUtils sftpUtils;
    @Value("${web.upload-path}")
    private String uploadPath;

    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public ApiResponse upload(@RequestParam MultipartFile file) {
        String upload = FileUtil.upload(file);
        return ApiResponse.ok(upload);
    }

    @PostMapping("/uploadFtp")
    @ApiOperation("文件上传到FTP")
    public ApiResponse uploadFtp(@RequestParam MultipartFile file) {
        try {
            String upload = sftpUtils.uploadFtp(file);
            return ApiResponse.ok(upload);
        } catch (Exception e) {
            log.error("上传FTP失败", e);
            throw new ApiCode.ApiException(-1, "上传FTP失败");
        }
    }

    @PostMapping("/uploadCloud")
    @ApiOperation("文件上传到云")
    public ApiResponse uploadCloud(@RequestParam MultipartFile file) {
        try {
            String upload = sftpUtils.uploadCloud(file);
            return ApiResponse.ok(upload);
        } catch (Exception e) {
            log.error("上传云失败", e);
            throw new ApiCode.ApiException(-1, "上传云失败");
        }
    }

    @GetMapping("/attachment-image-stream")
    @ApiOperation("文件转换对应图片流")
    public void attachmentToImageStream(@RequestParam String fileUrl,
                                        @RequestParam Integer length,
                                        @RequestParam Integer width,
                                        HttpServletResponse response) throws IOException {
        byte[] imageByteArray = null;
        if (fileUrl == null || fileUrl == "") {
            imageByteArray = FileUtil.zipImage(this.getClass().getResourceAsStream("/static/img/pic.png"), length, width);
            response.setContentType("image/jpeg");
        } else {
            String[] items = fileUrl.split("/");
            File file = new File(uploadPath + "\\" + items[items.length - 2] + "\\" + items[items.length - 1]);
            if (!file.exists()) {
                imageByteArray = FileUtil.zipImage(this.getClass().getResourceAsStream("/static/img/pic.png"), length, width);
                response.setContentType("image/jpeg");
            }
            Tika tika = new Tika();
            String mimeType = tika.detect(file);
            log.info(mimeType);
            switch (mimeType) {
                case "image/png":
                    imageByteArray = FileUtil.zipImage(file, length, width, "pbg");
                    response.setContentType("image/png");
                    break;
                case "image/jpeg":
                    imageByteArray = FileUtil.zipImage(file, length, width, "jpg");
                    response.setContentType("image/jpeg");
                    break;
                case "application/pdf":
                case "text/plain":
                case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
                default:
                    imageByteArray = FileUtil.zipImage(this.getClass().getResourceAsStream("/static/img/file.jpg"), length, width);
                    response.setContentType("image/jpeg");
                    break;
            }
        }
        OutputStream out = response.getOutputStream();
        out.write(imageByteArray);
        out.flush();
        out.close();
    }
}

