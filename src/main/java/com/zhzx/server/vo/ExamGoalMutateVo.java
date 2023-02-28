package com.zhzx.server.vo;

import com.zhzx.server.dto.exam.ExamGoalMutateTotalDto;
import lombok.Data;

import java.util.List;

@Data
public class ExamGoalMutateVo {
    private Long examId;
    private String subjectType;
    private List<ExamGoalMutateTotalDto> examGoalMutateTotalDtoList;
}
