/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.enums;

/**
* 意见与建议状态
*/
public enum CommentStateEnum {
    
    TO_BE_PUSHED("待推送"),
    PUSHED("已推送"),
    TO_BE_INSTRUCTION("待批示"),
    PENDING("处理中"),
    PROCESSED("已处理");

    CommentStateEnum(String name) {
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
