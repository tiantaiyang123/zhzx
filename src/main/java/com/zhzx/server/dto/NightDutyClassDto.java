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
 * 老师代班entity
 */
@Data
public class NightDutyClassDto extends BaseDto{

    private Long  nightStudyDutyId;

    private Long  nightStudyDutyClazzId;

    private Long  nightStudyId;

    private Long  teacherDutyId;

    private Long  teacherDutyClassId;

    private Long  clazzId;//班级id

    private Long  gradeId;//年级id

    private Long  updateClazzId;

    private String gradeName;//年级名称

    private String schoolyardName;

    private Long schoolyardIdd;

    private String clazzName;//班级名称

    private String clazzRemark;

    private String teacherName;//老师名称

    private Long teacherNewId;//老师id

    private Integer shouldStudentCount;//应到人数

    private Integer actualStudentCount;//实到人数

    private Integer allStudentCount;//班级总人数

    private Integer score;//得分

    private String phone;

    private String leaderName;

    private YesNoEnum isConfirm;

    private YesNoEnum isLeaderConfirm;//领导是否确认

    private YesNoEnum dutySituation;//值班情况

    private List<NightStudyAttendance> nightStudyAttendances;

    private List<NightStudyDetail> nightStudyDetailList;

    private List<NightStudyDutyClazzDeduction> nightStudyDutyClazzDeductions;//扣分情况

    private Map<String,List<NightStudyAttendance>> nightStudyDetailMap;

    private List<CommentDto> commentDtoList;

    private List<CommentImages> picList;

    private TeacherDutyTypeEnum teacherDutyTypeEnum;//晚自习阶段

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date time;//时间
}
