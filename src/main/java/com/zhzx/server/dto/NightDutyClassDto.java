package com.zhzx.server.dto;

import com.zhzx.server.domain.CommentImages;
import com.zhzx.server.domain.NightStudyAttendance;
import com.zhzx.server.domain.NightStudyDetail;
import com.zhzx.server.domain.NightStudyDutyClazzDeduction;
import com.zhzx.server.enums.TeacherDutyTypeEnum;
import com.zhzx.server.enums.YesNoEnum;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by A2 on 2022/1/11.
 */
@Data
public class NightDutyClassDto extends BaseDto{

    private Long  nightStudyDutyId;

    private Long  nightStudyDutyClazzId;

    private Long  nightStudyId;

    private Long  teacherDutyId;

    private Long  teacherDutyClassId;

    private Long  clazzId;

    private Long  gradeId;

    private Long  updateClazzId;

    private String gradeName;

    private String schoolyardName;

    private Long schoolyardIdd;

    private String clazzName;

    private String teacherName;

    private Long teacherNewId;

    private Integer shouldStudentCount;

    private Integer actualStudentCount;

    private Integer allStudentCount;

    private Integer score;

    private String phone;

    private String leaderName;

    private YesNoEnum isConfirm;

    private YesNoEnum isLeaderConfirm;

    private List<NightStudyAttendance> nightStudyAttendances;

    private List<NightStudyDetail> nightStudyDetailList;

    private List<NightStudyDutyClazzDeduction> nightStudyDutyClazzDeductions;

    private Map<String,List<NightStudyAttendance>> nightStudyDetailMap;

    private List<CommentDto> commentDtoList;

    private List<CommentImages> picList;

    private TeacherDutyTypeEnum teacherDutyTypeEnum;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date time;
}
