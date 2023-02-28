package com.zhzx.server.dto.exam;

import com.zhzx.server.domain.ExamResult;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExamClazzAnalyseStudentDto extends ExamResult {

    private String name;

    private String examEndDate;

    private String type;

    private String examSubjects;

    private BigDecimal threeTotal;

    private BigDecimal fourTotal;

    private int chineseRankBj;

    private int mathRankBj;

    private int englishRankBj;

    private int physicsRankBj;

    private int chemistryRankBj;

    private int chemistryWeightedRankBj;

    private int historyRankBj;

    private int biologyRankBj;

    private int biologyWeightedRankBj;

    private int politicsRankBj;

    private int politicsWeightedRankBj;

    private int geographyRankBj;

    private int geographyWeightedRankBj;

    private int totalRankBj;

    private int totalWeightedRankBj;

    private int threeTotalRankBj;

    private int fourTotalRankBj;

    private int totalRankNj;

    private int totalWeightedRankNj;

    private int threeTotalRankNj;

    private int fourTotalRankNj;

}
