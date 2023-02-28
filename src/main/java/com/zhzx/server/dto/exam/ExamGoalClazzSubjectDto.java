package com.zhzx.server.dto.exam;

import com.zhzx.server.enums.ClazzNatureEnum;
import com.zhzx.server.enums.GoalEnum;
import lombok.Data;

@Data
public class ExamGoalClazzSubjectDto {

    private Long id;

    private Long clazzId;

    private String targetName;

    private String clazzName;

    /**
     * 枚举 同班级性质
     */
    private GoalEnum subjectType;

    private ClazzNatureEnum clazzNature;

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

    private int shouldMeetingCnt;

}
