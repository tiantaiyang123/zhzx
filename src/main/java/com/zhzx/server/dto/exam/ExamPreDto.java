package com.zhzx.server.dto.exam;

import com.zhzx.server.enums.ClazzNatureEnum;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExamPreDto {

    private BigDecimal totalScore;

    private BigDecimal totalWeightedScore;

    private Long studentId;

    private ClazzNatureEnum nature;
}
