package com.zhzx.server.vo;


import com.zhzx.server.domain.NightStudyDutyClazz;
import com.zhzx.server.domain.NightStudyDutyClazzDeduction;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

@Data
public class NightStudyDutyClazzVo extends NightStudyDutyClazz {
    /**
     * 开始时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间", required = true)
    private java.util.Date startTime;
    /**
     * 结束时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间", required = true)
    private java.util.Date endTime;

    private List<NightStudyDutyClazzDeduction> nightStudyDutyClazzDeductionList;

    private String leaderName;

    private String gradeName;

    private String clazzName;

    private String schoolyardName;

    private Long nightStudyDutyId;
}
