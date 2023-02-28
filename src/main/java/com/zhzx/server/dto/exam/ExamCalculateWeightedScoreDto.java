package com.zhzx.server.dto.exam;

import com.zhzx.server.domain.ExamResult;
import lombok.Data;

@Data
public class ExamCalculateWeightedScoreDto extends ExamResult {

    private String weightedSubjects;

    private String[] arr;

}
