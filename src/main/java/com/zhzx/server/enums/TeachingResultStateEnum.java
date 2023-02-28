/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.enums;

/**
* 教学成果状态
*/
public enum TeachingResultStateEnum {
    
    PENDING_REVIEW("待审核"),
    PASSED("已通过"),
    REJECTED("已驳回");

    TeachingResultStateEnum(String name) {
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
