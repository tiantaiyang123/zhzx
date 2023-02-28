/**
* 项目：中华中学管理平台
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.enums;

/**
* 微信创建家长
*/
public enum MessageParentEnum {
    
    NOT_CREATE("未创建"),
    CREATED("已创建"),
    NOT_SEND_MESSAGE("不发送信息"),
    NO_CREATE("不创建");

    MessageParentEnum(String name) {
        this.name = name;
    }

    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
