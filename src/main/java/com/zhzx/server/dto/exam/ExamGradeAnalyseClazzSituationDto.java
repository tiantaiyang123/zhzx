package com.zhzx.server.dto.exam;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExamGradeAnalyseClazzSituationDto {

    private Long id;

    private Long examId;

    private BigDecimal score;

    private BigDecimal scoreLast;

    private int pm;

    private int pmLast;

    private String name;

    private String headTeacher;

    private BigDecimal ratio;

    private int studentCount;

    private int joinCount;

    private String clazzNature;

    private String clazzLevel;

    private String otherDivision;

    private String simpleDivision;

    private BigDecimal chineseAvg;

    private BigDecimal mathAvg;

    private BigDecimal englishAvg;

    private BigDecimal physicsAvg;

    private BigDecimal chemistryAvg;

    private BigDecimal chemistryWeightedAvg;

    private BigDecimal biologyAvg;

    private BigDecimal biologyWeightedAvg;

    private BigDecimal historyAvg;

    private BigDecimal politicsAvg;

    private BigDecimal politicsWeightedAvg;

    private BigDecimal geographyAvg;

    private BigDecimal geographyWeightedAvg;

    private BigDecimal totalAvg;

    private BigDecimal totalWeightedAvg;

    private Integer chineseRank;

    private Integer mathRank;

    private Integer englishRank;

    private Integer physicsRank;

    private Integer chemistryRank;

    private Integer chemistryWeightedRank;

    private Integer historyRank;

    private Integer biologyRank;

    private Integer biologyWeightedRank;

    private Integer politicsRank;

    private Integer politicsWeightedRank;

    private Integer geographyRank;

    private Integer geographyWeightedRank;

    private Integer totalRank;

    private Integer totalWeightedRank;

}
