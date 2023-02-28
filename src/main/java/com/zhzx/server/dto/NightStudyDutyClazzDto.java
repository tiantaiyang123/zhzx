package com.zhzx.server.dto;

import com.zhzx.server.domain.NightStudyAttendance;
import com.zhzx.server.domain.NightStudyDetail;
import com.zhzx.server.domain.NightStudyDetailImage;
import com.zhzx.server.domain.NightStudyDutyClazzDeduction;
import com.zhzx.server.enums.TeacherDutyTypeEnum;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.vo.NightStudyDutyClazzVo;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by A2 on 2022/1/11.
 */
@Data
public class NightStudyDutyClazzDto extends BaseDto{

    private String groupTime;

    private List<NightStudyDutyClazzVo> nightStudyDutyClazzVoList;
}
