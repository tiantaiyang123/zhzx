/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.enums;

/**
* 老师值班类型
*/
public enum TeacherDutyTypeEnum {
    
    STAGE_ONE("第一阶段"),
    STAGE_TWO("第二阶段"),
    GRADE_TOTAL_DUTY("年级总值班"),
    TOTAL_DUTY("总值班");

    TeacherDutyTypeEnum(String name) {
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
