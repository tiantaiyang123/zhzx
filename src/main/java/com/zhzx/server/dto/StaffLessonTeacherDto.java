package com.zhzx.server.dto;

import com.zhzx.server.domain.StaffLessonTeacher;
import com.zhzx.server.enums.YesNoEnum;
import lombok.Data;

@Data
public class StaffLessonTeacherDto extends StaffLessonTeacher {

    /**
     * 映射科目ID sys_subject.id
     */
    private Long mSubjectId;

    private YesNoEnum isClazzAdvisor;

}
