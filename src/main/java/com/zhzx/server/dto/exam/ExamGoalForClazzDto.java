package com.zhzx.server.dto.exam;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExamGoalForClazzDto {

    private int clazzMeetingCnt;

    private int shouldMeetingCnt;

    private BigDecimal ratio;

    private int pm;

}
