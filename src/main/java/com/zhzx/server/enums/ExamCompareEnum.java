/**
* 项目：中华中学管理平台
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.enums;

/**
* 考试分段大于小于
*/
public enum ExamCompareEnum {
    
    GE("大于等于"),
    LE("小于等于");

    ExamCompareEnum(String name) {
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
