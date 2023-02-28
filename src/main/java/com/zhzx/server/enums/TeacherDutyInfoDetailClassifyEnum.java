/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.enums;

/**
* 教师值班信息明细分类
*/
public enum TeacherDutyInfoDetailClassifyEnum {
    
    ABSENT("缺席"),
    BE_LATE("迟到"),
    IN_OUT_CLASSROOM("中途出入教室"),
    OTHER("其他");

    TeacherDutyInfoDetailClassifyEnum(String name) {
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
