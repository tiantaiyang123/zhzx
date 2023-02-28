package com.zhzx.server.dto.exam;

import com.zhzx.server.domain.ExamGoalWarning;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class ExamGoalDto {

    private Long id;

    private String name;

    private String subjectType;

    private Long examId;

    private int totalCount;

    private BigDecimal transGoalScore;
    private BigDecimal threeTotalGoalScore;
    private BigDecimal fourTotalGoalScore;
    private BigDecimal chineseGoalScore;
    private BigDecimal mathGoalScore;
    private BigDecimal englishGoalScore;
    private BigDecimal physicsGoalScore;
    private BigDecimal chemistryGoalScore;
    private BigDecimal chemistryWeightedGoalScore;
    private BigDecimal biologyGoalScore;
    private BigDecimal biologyWeightedGoalScore;
    private BigDecimal historyGoalScore;
    private BigDecimal politicsGoalScore;
    private BigDecimal politicsWeightedGoalScore;
    private BigDecimal geographyGoalScore;
    private BigDecimal geographyWeightedGoalScore;

    private int chemistryGoalCount;
    private int biologyGoalCount;
    private int politicsGoalCount;
    private int geographyGoalCount;

    private Map<String, List<ExamGoalWarning>> examGoalWarningMap;
}
