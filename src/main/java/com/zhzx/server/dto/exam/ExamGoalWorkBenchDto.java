package com.zhzx.server.dto.exam;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class ExamGoalWorkBenchDto {
    private Long clazzId;
    private Long id;
    private String clazzName;
    private String targetName;
    private String clazzNature;
    private String clazzDivision;
    
    private int joinCnt;
    private int chineseJoinCnt;
    private int mathJoinCnt;
    private int englishJoinCnt;
    private int physicsJoinCnt;
    private int chemistryJoinCnt;
    private int biologyJoinCnt;
    private int historyJoinCnt;
    private int politicsJoinCnt;
    private int geographyJoinCnt;
    private int studentCnt;

    private int clazzMeetingCnt;
    private int shouldMeetingCnt;
    private int chineseMeetingCnt;
    private int mathMeetingCnt;
    private int englishMeetingCnt;
    private int physicsMeetingCnt;
    private int chemistryMeetingCnt;
    private int chemistryWeightedMeetingCnt;
    private int biologyMeetingCnt;
    private int biologyWeightedMeetingCnt;
    private int historyMeetingCnt;
    private int politicsMeetingCnt;
    private int politicsWeightedMeetingCnt;
    private int geographyMeetingCnt;
    private int geographyWeightedMeetingCnt;

    private int chineseOnlyMeetingCnt;
    private int mathOnlyMeetingCnt;
    private int englishOnlyMeetingCnt;
    private int physicsOnlyMeetingCnt;
    private int chemistryOnlyMeetingCnt;
    private int chemistryWeightedOnlyMeetingCnt;
    private int biologyOnlyMeetingCnt;
    private int biologyWeightedOnlyMeetingCnt;
    private int historyOnlyMeetingCnt;
    private int politicsOnlyMeetingCnt;
    private int politicsWeightedOnlyMeetingCnt;
    private int geographyOnlyMeetingCnt;
    private int geographyWeightedOnlyMeetingCnt;

    private BigDecimal chineseValidAvgScore;
    private BigDecimal mathValidAvgScore;
    private BigDecimal englishValidAvgScore;
    private BigDecimal physicsValidAvgScore;
    private BigDecimal chemistryValidAvgScore;
    private BigDecimal chemistryWeightedValidAvgScore;
    private BigDecimal biologyValidAvgScore;
    private BigDecimal biologyWeightedValidAvgScore;
    private BigDecimal historyValidAvgScore;
    private BigDecimal politicsValidAvgScore;
    private BigDecimal politicsWeightedValidAvgScore;
    private BigDecimal geographyValidAvgScore;
    private BigDecimal geographyWeightedValidAvgScore;

}
