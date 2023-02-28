package com.zhzx.server.dto.exam;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExamClazzAnalyseClazzAvgDto {

    private String clazzName;

    private Long examId;

    private String examName;

    private BigDecimal chineseAvg;

    private BigDecimal mathAvg;

    private BigDecimal englishAvg;

    private BigDecimal physicsAvg;

    private BigDecimal chemistryAvg;

    private BigDecimal chemistryWeightedAvg;

    private BigDecimal historyAvg;

    private BigDecimal biologyAvg;

    private BigDecimal biologyWeightedAvg;

    private BigDecimal geographyAvg;

    private BigDecimal geographyWeightedAvg;

    private BigDecimal politicsAvg;

    private BigDecimal politicsWeightedAvg;

    private BigDecimal threeTotalAvg;

    private BigDecimal fourTotalAvg;

    private BigDecimal totalAvg;

    private BigDecimal totalWeightedAvg;

}
