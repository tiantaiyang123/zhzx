/**
 * 项目：中华中学管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.enums;

/**
* 晚自习考勤阶段
*/
public enum StudentNightDutyTypeEnum {
    
    STAGE_ONE("第一阶段"),
    STAGE_TWO("第二阶段");

    StudentNightDutyTypeEnum(String name) {
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
