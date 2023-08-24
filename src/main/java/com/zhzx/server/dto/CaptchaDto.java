package com.zhzx.server.dto;

import lombok.Data;

@Data
public class CaptchaDto {
    private String code;
    private String imageUrl;
    private Long timeStamp;
}
