package com.zhzx.server.util;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import lombok.SneakyThrows;
import net.coobird.thumbnailator.builders.BufferedImageBuilder;
import net.coobird.thumbnailator.resizers.Resizers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class FileUtil {
    @Value("${web.upload-path}")
    private String uploadPath;

    public static String uploadBasePath;

    @PostConstruct
    private void init() {
        uploadBasePath = uploadPath;
    }

    @SneakyThrows
    public static String upload(MultipartFile file) {
        String returnFileName = String.format("/%s/%s.%s",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                UUID.randomUUID(),
                Files.getFileExtension(file.getOriginalFilename()));
        if(returnFileName.endsWith(".")){
            returnFileName = returnFileName+"jpg";
        }
        File uploadFile = new File(uploadBasePath + returnFileName);
        Files.createParentDirs(uploadFile);
        Files.write(ByteStreams.toByteArray(file.getInputStream()), uploadFile);
        return "/upload" + returnFileName;
    }

    public static byte[] zipImage(File file, Integer length, Integer width,String format) throws IOException{
            // 压缩文件
            BufferedImage srcImg = ImageIO.read(file);
            // 按宽300,高200压缩图片
            BufferedImage tarImg = new BufferedImageBuilder(width, length, BufferedImage.TYPE_3BYTE_BGR).build();
            Resizers.BILINEAR.resize(srcImg, tarImg);
            //写压缩文件
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(tarImg, "jpg", out);
            return out.toByteArray();

    }

    public static byte[] zipImage(InputStream stream, Integer length, Integer width) throws IOException{
        // 压缩文件
        BufferedImage srcImg = ImageIO.read(stream);
        // 按宽300,高200压缩图片
        BufferedImage tarImg = new BufferedImageBuilder(width, length, BufferedImage.TYPE_3BYTE_BGR).build();
        Resizers.BILINEAR.resize(srcImg, tarImg);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(tarImg, "jpg", out);
        return out.toByteArray();
    }
}

