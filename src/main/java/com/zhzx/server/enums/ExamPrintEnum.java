/**
* 项目：中华中学管理平台
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.enums;

/**
* 考试发布枚举
*/
public enum ExamPrintEnum {

    YES("全发布"),
    NO("未发布"),
    TABLE("仅表格"),
    CHART("仅图表");

    ExamPrintEnum(String name) {
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
