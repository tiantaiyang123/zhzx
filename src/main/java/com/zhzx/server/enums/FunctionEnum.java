/**
* 项目：中华中学管理平台
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.enums;

/**
* 职能
*/
public enum FunctionEnum {
    
    DEAN("教务处"),
    PRINCIPAL("校长"),
    COMMITTEE("团委"),
    MORAL("德育处"),
    OTHER("其他");

    FunctionEnum(String name) {
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
