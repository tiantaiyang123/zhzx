/**
 * 项目：中华中学管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.enums;

/**
* 一日常规分类
*/
public enum ClassifyEnum {
    
    DAY_NIGHT_STUDY("晚自习"),
    DAY_SOUTH_GATE("南大门准备情况"),
    DAY_BREAKFAST("早餐就餐情况"),
    DAY_MORNING_READING_1("早读情况-高一年级"),
    DAY_MORNING_READING_2("早读情况-高二年级"),
    DAY_MORNING_READING_3("早读情况-高三年级"),
    DAY_BREAK_ACTIVITY("大课间活动情况"),
    DAY_LUNCH("午餐就餐情况"),
    DAY_TEACHING_AREA_1("教学区秩序-高一年级"),
    DAY_TEACHING_AREA_2("教学区秩序-高二年级"),
    DAY_TEACHING_AREA_3("教学区秩序-高三年级"),
    DAY_NOON_SPORT_AREA("午班运动区秩序"),
    DAY_DINNER("晚餐就餐情况"),
    DAY_GO_OUT("走读生提前出门"),
    DAY_NIGHT_SPORT_AREA("晚班运动区秩序"),
    DAY_NIGHT_STUDY_DUTY("晚自习行政值班表"),
    DAY_OTHER_1("白班其他"),
    DAY_OTHER_2("晚班其他");

    ClassifyEnum(String name) {
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
