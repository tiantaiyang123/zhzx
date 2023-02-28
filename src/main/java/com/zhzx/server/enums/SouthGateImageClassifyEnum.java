/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.enums;

/**
* 南大门情况图片分类
*/
public enum SouthGateImageClassifyEnum {
    
    DOCTOR("校医情况"),
    GUARD("保安情况"),
    THERMOMETER("远红外测温仪情况");

    SouthGateImageClassifyEnum(String name) {
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
