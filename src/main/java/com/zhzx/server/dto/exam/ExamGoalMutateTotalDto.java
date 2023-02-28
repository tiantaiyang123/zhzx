package com.zhzx.server.dto.exam;

import lombok.Data;

@Data
public class ExamGoalMutateTotalDto {
    private Integer score;
    private Integer chineseGoalScore;
    private Integer mathGoalScore;
    private Integer englishGoalScore;
    private Integer physicsGoalScore;
    private Integer historyGoalScore;
    private Integer chemistryGoalScore;
    private Integer chemistryWeightedGoalScore;
    private Integer biologyGoalScore;
    private Integer biologyWeightedGoalScore;
    private Integer geographyGoalScore;
    private Integer geographyWeightedGoalScore;
    private Integer politicsGoalScore;
    private Integer politicsWeightedGoalScore;
    private Long goalId;
    private String goalName;
}
