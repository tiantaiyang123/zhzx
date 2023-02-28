package com.zhzx.server.dto.exam;

import com.zhzx.server.enums.ClazzNatureEnum;
import lombok.Data;

@Data
public class ExamResultSimpleDto {

    private ClazzNatureEnum clazzNature;

    private String clazzLevel;

    private String otherDivision;

    private String clazzDivision;

    private Long studentId;

    private String studentName;

    private int studentCount;

    private String clazzName;

    private String other;

    private String otherPre;

    private Long clazzId;

    private java.math.BigDecimal score;
    private java.math.BigDecimal scoreWeighted;
    private java.math.BigDecimal totalScore;
    private java.math.BigDecimal totalWeightedScore;
    private java.math.BigDecimal chineseScore;
    private java.math.BigDecimal mathScore;
    private java.math.BigDecimal englishScore;
    private java.math.BigDecimal physicsScore;
    private java.math.BigDecimal chemistryScore;
    private java.math.BigDecimal chemistryWeightedScore;
    private java.math.BigDecimal biologyScore;
    private java.math.BigDecimal biologyWeightedScore;
    private java.math.BigDecimal historyScore;
    private java.math.BigDecimal politicsScore;
    private java.math.BigDecimal politicsWeightedScore;
    private java.math.BigDecimal geographyScore;
    private java.math.BigDecimal geographyWeightedScore;
    private java.math.BigDecimal threeTotalScore;
    private java.math.BigDecimal fourTotalScore;


}
