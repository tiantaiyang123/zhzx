/**
 * 项目：中华中学管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.enums;

/**
* 消息类型
*/
public enum MessageTypeEnum {

    DUTY("值班"),
    OFFICE("办公"),
    NORMAL_RESULT("普通消息_成果"),
    NORMAL_QYWX("普通消息_企业微信");

    MessageTypeEnum(String name) {
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
