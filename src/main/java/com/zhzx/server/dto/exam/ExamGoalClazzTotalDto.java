package com.zhzx.server.dto.exam;

import com.zhzx.server.enums.ClazzNatureEnum;
import com.zhzx.server.enums.GoalEnum;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExamGoalClazzTotalDto {

    private Long clazzId;

    private String targetName;

    private BigDecimal targetScore;

    /**
     * 枚举 同班级性质
     */
    private GoalEnum subjectType;

    /**
     * 原始分或总赋分对应人数
     */
    private int goalValue;

    private Long id;

    private String clazzName;

    private ClazzNatureEnum clazzNature;

    private int joinCnt;

    private int studentCnt;

    private int clazzMeetingCnt;

    private int shouldMeetingCnt;

    private BigDecimal ratio;

    private int pm;
}
