/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.rest.res;

import lombok.Getter;

public enum ApiCode {
    SUCCESS(0, "success"),
    FAILED(-1, "fail"),
    UNAUTHC(401, "You haven't signed in yet!"),
    UNAUTHZ(403, "You are not authorized!"),
    SERVER_ERROR(500, "server error!");

    private int code = 0;
    private String message = "success";

    ApiCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public ApiException ex() {
        return new ApiException(this);
    }

    public ApiException ex(String message) {
        this.message = message;
        return new ApiException(this);
    }

    @Getter
    public static class ApiException extends RuntimeException {
        private int code;
        private String message;

        public ApiException(ApiCode apiCode) {
            this.code = apiCode.getCode();
            this.message = apiCode.getMessage();
        }

        public ApiException(int code, String message) {
            this.code = code;
            this.message = message;
        }
    }
}
