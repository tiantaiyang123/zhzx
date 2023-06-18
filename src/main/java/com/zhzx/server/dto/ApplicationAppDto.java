package com.zhzx.server.dto;

import com.zhzx.server.domain.ApplicationApp;
import lombok.Data;

import java.util.List;

@Data
public class ApplicationAppDto {
    private String tagName;
    private List<ApplicationApp> applicationAppList;
}
