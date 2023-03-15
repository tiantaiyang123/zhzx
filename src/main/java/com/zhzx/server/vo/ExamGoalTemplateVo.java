package com.zhzx.server.vo;

import com.zhzx.server.domain.ExamGoalTemplateSub;
import lombok.Data;

import java.util.List;

@Data
public class ExamGoalTemplateVo {
    /**
     * id 不为null 则表示修改模板
     */
    private Long id;
    private String examGoalTemplateName;
    private Long gradeId;
    private Long academicYearSemesterId;
    private List<ExamGoalTemplateSub> entity;
}
