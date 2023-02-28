package com.zhzx.server.dto;

import com.zhzx.server.domain.StaffLessonLeader;
import lombok.Data;

@Data
public class StaffLessonLeaderDto extends StaffLessonLeader {

    private String leaderName;
    private String subjectName;
    private int subjectCount;
    private String staffIds;
}
