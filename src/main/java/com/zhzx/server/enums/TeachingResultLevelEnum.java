/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.enums;

/**
* 教学成果级别
*/
public enum TeachingResultLevelEnum {
    
    HXQK("核心期刊"),
    GJJ("国家级"),
    SJ("省级"),
    DSJ("市级"),
    QJ("区级"),
    XJ("校级"),
    NJZ("年级组"),
    ZYZ("教研组");

    TeachingResultLevelEnum(String name) {
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
