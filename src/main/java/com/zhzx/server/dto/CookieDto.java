package com.zhzx.server.dto;

import lombok.Data;

@Data
public class CookieDto {
    private String name;
    private String value;
    private String path;
    private String domain;
}
