package com.zhzx.server.dto.exam;

import com.zhzx.server.enums.ClazzNatureEnum;
import lombok.Data;

import java.util.Map;

@Data
public class ExamGoalTotalCombineDto {

    private Long clazzId;

    private String clazzName;

    private ClazzNatureEnum clazzNature;

    private int joinCnt;

    private int studentCnt;

    private Map<String, ExamGoalForClazzDto> goals;

}
