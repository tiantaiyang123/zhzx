package com.zhzx.server.dto;

import com.zhzx.server.domain.*;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by A2 on 2022/1/11.
 */
@Data
public class LeaderNightStudyDutyDto extends NightStudyDuty{

    private List<NightStudyDutyGradeImages> nightStudyDutyGradeImages;

    private List<Map<String,Object>> map;

    private List<NightDutyClassDto> nightDutyClassDtoList;

    //偶发时间情况
    private List<Incident> incidentList;

    //意见与建议表
    private List<Comment> comment;

    private Staff staff;

    private Schoolyard schoolyard;
}
