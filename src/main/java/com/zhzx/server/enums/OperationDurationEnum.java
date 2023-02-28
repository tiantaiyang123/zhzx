/**
 * 项目：中华中学管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.enums;

/**
* 作业时长
*/
public enum OperationDurationEnum {
    
    NONE("无"),
    D2("20分钟左右"),
    D3("30分钟左右"),
    D4("40分钟左右"),
    D5("50分钟左右"),
    D6("60分钟左右"),
    D7("70分钟左右"),
    D8("80分钟左右"),
    D9("90分钟以上");

    OperationDurationEnum(String name) {
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
