package com.zhzx.server.dto;

import com.zhzx.server.enums.LeaderDutyTypeEnum;
import lombok.Data;

import java.util.Date;

/**
 * Created by A2 on 2022/1/11.
 */
@Data
public class LeaderDutyDto extends BaseDto{

    private Long leaderDutyId;

    private String week;

    private Date time;

    private String teacherName;

    private String routineLeaderName;

    private String nightLeaderName;

    private String phone;

    private LeaderDutyTypeEnum dutyType;

    // y
    private String schoolyardName;
    private Long schoolyardId;
}
