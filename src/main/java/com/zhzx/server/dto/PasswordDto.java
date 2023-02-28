/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.dto;

import lombok.Data;

@Data
public class PasswordDto extends BaseDto {
    private Integer userId;
    private String oldPassword;
    private String newPassword;
}
