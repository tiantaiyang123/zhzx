/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest.res;

import com.zhzx.server.rest.res.ApiCode;
import lombok.Data;

@Data
public class ApiResponse<T> {
    private int code = 0;
    private String message = "success";
    private T result;
    private String detailMessage;

    public static <T> ApiResponse ok(T result) {
        return new ApiResponse(0, "success", result);
    }

    public static <T> ApiResponse fail(int code, String message) {
        return new ApiResponse(code, message, null);
    }

    public static <T> ApiResponse fail(ApiCode apiCode) {
        return new ApiResponse(apiCode.getCode(), apiCode.getMessage(), null);
    }

    public ApiResponse(int code, String message, T result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }
}
