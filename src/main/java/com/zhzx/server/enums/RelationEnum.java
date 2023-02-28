/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.enums;

/**
* 家长学生关系
*/
public enum RelationEnum {
    
    F("父亲"),
    M("母亲"),
    FGF("爷爷"),
    FGM("奶奶"),
    MGF("外公"),
    MGM("外婆"),
    O("其他");

    RelationEnum(String name) {
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
