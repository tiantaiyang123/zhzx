/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.enums;

/**
* 年级
*/
public enum GradeEnum {
    
    ONE("高一年级"),
    TWO("高二年级"),
    THREE("高三年级");

    GradeEnum(String name) {
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
