package com.zhzx.server.dto;

import com.zhzx.server.enums.TeacherDutyModeEnum;
import com.zhzx.server.enums.TeacherDutyTypeEnum;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.vo.ClazzVo;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by A2 on 2022/1/11.
 */
@Data
public class TeacherServerFormDto extends BaseDto{
    private Long staffId;

    private Date time;

    private String week;

    private List<ClazzVo> clazzVoList;

    private String totalDutyTeacher;

    private String gradeDutyTeacher;

    private String totalDutyTeacherYard;

    private String gradeDutyTeacherYard;

    private YesNoEnum isConfirm;

    private List<TeacherServerFormDto> duty;

    private TeacherDutyTypeEnum dutyType;

    private TeacherDutyModeEnum dutyMode;

    private Map<TeacherDutyTypeEnum,List<ClazzVo>> clazzVoListMap;

    private Long schoolyardId;
}
