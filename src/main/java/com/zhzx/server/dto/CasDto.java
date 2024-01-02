package com.zhzx.server.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class CasDto {
    CasDto() {}

    @NonNull
    private String ticket;
    @NonNull
    private String serviceName;
}
