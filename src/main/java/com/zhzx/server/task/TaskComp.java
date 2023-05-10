package com.zhzx.server.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhzx.server.domain.*;
import com.zhzx.server.dto.CommentNightDutyDto;
import com.zhzx.server.dto.NightDutyClassDto;
import com.zhzx.server.dto.TeacherDutyDto;
import com.zhzx.server.enums.*;
import com.zhzx.server.msdomain.AccountInfo;
import com.zhzx.server.msrepository.AccountInfoMapper;
import com.zhzx.server.repository.*;
import com.zhzx.server.service.*;
import com.zhzx.server.util.*;
import com.zhzx.server.vo.SchoolWeek;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by A2 on 2022/2/16.
 */
//@Component
@Slf4j
public class TaskComp {
    @Value("${spring.profiles.active}")
    private String env;
    @Autowired(required = false)
    private AccountInfoMapper accountInfoMapper;
    @Resource
    private StudentMapper studentMapper;
    @Resource
    private StaffMapper staffMapper;
    @Resource
    private AcademicYearSemesterService academicYearSemesterService;
    @Resource
    private ClazzMapper clazzMapper;
    @Resource
    private GradeService gradeService;
    @Resource
    private StaffService staffService;
    @Resource
    private StaffMessageRefuseMapper staffMessageRefuseMapper;
    @Resource
    private WxSendMessageService wxSendMessageService;
    @Resource
    private StudentParentService studentParentService;
    @Resource
    private MessageService messageService;
    @Resource
    private CourseService courseService;
    @Resource
    private SettingsService settingsService;
    @Resource
    private NightStudyAttendanceService nightStudyAttendanceService;
    @Resource
    private TeacherDutyClazzService teacherDutyClazzService;
    @Resource
    private TeacherDutyMapper teacherDutyMapper;
    @Resource
    private LeaderDutyMapper leaderDutyMapper;
    @Resource
    private MessageTaskService messageTaskService;
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private NightStudyAttendanceSubMapper nightStudyAttendanceSubMapper;
    @Resource
    private NightStudyMapper nightStudyMapper;
    @Resource
    private PublicCourseService publicCourseService;

    /**
     * 每1分钟更新班级考勤概况
     */
    @Scheduled(cron = "0 0/1 * ? * ?")
    private void syncNightAttendanceSub() {
        Date date = new Date();
        List<NightStudyAttendanceSub> nightStudyAttendanceList = nightStudyAttendanceSubMapper.selectList(Wrappers.<NightStudyAttendanceSub>lambdaQuery()
                .apply("to_days(register_date)" + "=" + "to_days({0})", date)
        );
        if(CollectionUtils.isNotEmpty(nightStudyAttendanceList)){
            Map<Long, List<NightStudyAttendanceSub>> map = nightStudyAttendanceList.stream().collect(Collectors.groupingBy(NightStudyAttendanceSub::getClazzId));
            for (Long clazzId : map.keySet()){
                List<NightStudyAttendanceSub> clazzAttendance = map.get(clazzId);
                Map<StudentNightDutyTypeEnum, NightStudyAttendanceSub> clazzAttendanceMap = clazzAttendance.stream().collect(Collectors.toMap(NightStudyAttendanceSub::getStage, Function.identity()));
                for (StudentNightDutyTypeEnum studentNightDutyTypeEnum : clazzAttendanceMap.keySet()) {
                    NightStudyAttendanceSub nightStudyAttendanceSub = clazzAttendanceMap.get(studentNightDutyTypeEnum);
                    // 这里用老师端的人数同步即可
                    NightStudy nightStudy = this.nightStudyMapper.getNightStudyLeader(nightStudyAttendanceSub.getClazzId(), date, studentNightDutyTypeEnum.toString());
                    if (nightStudy != null) {
                        Integer shouldStudentCount = nightStudy.getShouldStudentCount();
                        Integer actualStudentCount = nightStudy.getActualStudentCount();
                        Integer num1 = nightStudyAttendanceSub.getShouldNum();
                        Integer num2 = nightStudyAttendanceSub.getActualNum();
                        if (shouldStudentCount != 0 && actualStudentCount != 0 && (!shouldStudentCount.equals(num1) || !actualStudentCount.equals(num2))) {
                            this.nightStudyAttendanceSubMapper.update(
                                    null,
                                    Wrappers.<NightStudyAttendanceSub>lambdaUpdate()
                                            .set(NightStudyAttendanceSub::getActualNum, actualStudentCount)
                                            .set(NightStudyAttendanceSub::getShouldNum, shouldStudentCount)
                                            .eq(NightStudyAttendanceSub::getId, nightStudyAttendanceSub.getId()));
                        }
                    }
                }
            }
        }
    }

    /**
     * 每3小时更新卡号
     */
    @Scheduled(cron = "0 0 */3 * * ?")
    private void updateCardNumber() {
        if ("prod".equals(env)) {
            List<AccountInfo> accountInfoList = this.accountInfoMapper.selectList(
                    Wrappers.<AccountInfo>lambdaQuery()
                            .isNotNull(AccountInfo::getId)
                            .isNotNull(AccountInfo::getCPhysicalNo)
                            .and(wrapper -> wrapper.ne(AccountInfo::getYch, "0").ne(AccountInfo::getFlag, "2")));
            if (CollectionUtils.isNotEmpty(accountInfoList)) {
                for (AccountInfo accountInfo : accountInfoList) {
                    if ("学生".equals(accountInfo.getType())) {
                        this.studentMapper.update(null,
                                Wrappers.<Student>lambdaUpdate()
                                        .eq(Student::getIdNumber, accountInfo.getIdNumber())
                                        .set(Student::getCardNumber, accountInfo.getCPhysicalNo()));
                    } else {
                        this.staffMapper.update(null,
                                Wrappers.<Staff>lambdaUpdate()
                                        .eq(Staff::getIdNumber, accountInfo.getIdNumber())
                                        .set(Staff::getCardNumber, accountInfo.getCPhysicalNo()));
                    }
                }
            }
        }
    }
    /**
     * 每5分钟修改班级人数
     */
    @Scheduled(cron = "0 0/5 * ? * ?")
    private void updateUserLockStatus() {
        clazzMapper.resetClazzStudentNum();
    }

    /**
     * 每天00:00开始更新
     */
    @Scheduled(cron = "0 0 0 1/1 * ? ")
    private void updateLessonTeacher() {
        this.staffService.updateLessonTeacher();
    }

    /**
     * 每天1开始更新
     */
    @Scheduled(cron = "0 0 1 1/1 * ? ")
    private void updateCourse() {
        AcademicYearSemester academicYearSemester = academicYearSemesterService.getCurrentYearSemester(null);
        if (academicYearSemester == null) return;
        // 获得课程表最后更新日期
        Map<String, Object> courseMap = this.courseService.getMap(new QueryWrapper<Course>()
                .select("max(update_time) as maxDate")
                .eq("academic_year_semester_id", academicYearSemester.getId()));
        if (CollectionUtils.isEmpty(courseMap) || !courseMap.containsKey("maxDate")) return;
        String dateTimeStr = DateUtils.format((Date) courseMap.get("maxDate"), "yyyyMMddHHmmss");
        // 获得课程结果表最后日期
        Settings settings = this.settingsService.getOne(Wrappers.<Settings>lambdaQuery()
                .like(Settings::getCode, "COURSE\\_" + academicYearSemester.getId() + "\\_")
        );
        // 判断是否需要更新
        Boolean needCalculate = true;
        if (settings != null) {
            String[] codeArray = settings.getCode().split("_");
            if (codeArray.length == 5)
                needCalculate = codeArray[codeArray.length - 1].compareTo(dateTimeStr) < 0;
        }
        if (!needCalculate) return;
        // 清除
        this.settingsService.remove(Wrappers.<Settings>lambdaQuery()
                .like(Settings::getCode, "COURSE\\_" + academicYearSemester.getId() + "\\_")
        );
        // 添加
        List<Grade> gradeList = this.gradeService.list();
        for (Grade grade : gradeList) {
            // 年级课表
            String key = "COURSE_" + academicYearSemester.getId() + "_" + grade.getId() + "_null" + "_" + dateTimeStr;
            List<Map<String, String>> courseList = courseService.getList(academicYearSemester.getId(), grade.getId(), null);
            settings = new Settings();
            settings.setCode(key);
            settings.setRemark(key);
            settings.setParams(JsonUtils.toJson(courseList));
            this.settingsService.save(settings);
            // 班级课表
            List<Clazz> clazzList = this.clazzMapper.selectList(new QueryWrapper<Clazz>()
                    .eq("academic_year_semester_id", academicYearSemester.getId())
                    .eq("grade_id", grade.getId())
            );
            for (Clazz clazz : clazzList) {
                key = "COURSE_" + academicYearSemester.getId() + "_" + grade.getId() + "_" + clazz.getId() + "_" + dateTimeStr;
                courseList = courseService.getList(academicYearSemester.getId(), grade.getId(), clazz.getId());
                settings = new Settings();
                settings.setCode(key);
                settings.setRemark(key);
                settings.setParams(JsonUtils.toJson(courseList));
                this.settingsService.save(settings);
            }
        }
    }

    /**
     * 发送消息
     */
    @Scheduled(cron = "0 0/5 * ? * ?")
    private void sendMessage() {
        Date lastSendTime = null;
        Date now = new Date();
        Settings timeSettings = settingsService.getOne(Wrappers.<Settings>lambdaQuery()
                .eq(Settings::getCode,"MESSAGE_LAST_SEND_TIME")
        );
        if(timeSettings == null){
            timeSettings = new Settings();
            timeSettings.setCode("MESSAGE_LAST_SEND_TIME");
            timeSettings.setRemark("上次发送时间");
            timeSettings.setParams(DateUtils.format(now));
            timeSettings.setDefault().validate(true);
            settingsService.save(timeSettings);
        }else{
            lastSendTime = DateUtils.parse(timeSettings.getParams());
            settingsService.update(new Settings(),Wrappers.<Settings>lambdaUpdate()
                    .set(Settings::getParams,DateUtils.format(now))
                    .eq(Settings::getId,timeSettings.getId())
            );
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DAY_OF_MONTH,-1);

        final Date time = lastSendTime;
        QueryWrapper<Message> wrappers = new QueryWrapper<>();
        wrappers.or(wrapper->wrapper.gt(time !=null ,"send_time", time)
                .le("send_time", now)
                .ge("message_task_id", 1)
                .eq("is_send", YesNoEnum.NO));
        wrappers.or(wrapper->wrapper.gt("repeat_send_time",time)
                .le("repeat_send_time",now)
                .eq("is_write",YesNoEnum.NO)
                .ge("message_task_id", 1)
                .lt("send_num",2));
        List<Message> messageList = messageService.list(wrappers);
        if(CollectionUtils.isNotEmpty(messageList)){
            Map<ReceiverEnum,List<Message>> map = messageList.stream().collect(Collectors.groupingBy(Message::getReceiverType));
            Settings settings = settingsService.getOne(Wrappers.<Settings>lambdaQuery().eq(Settings::getCode,"MESSAGE_URL"));
            for (ReceiverEnum receiverEnum : map.keySet()){
                if(ReceiverEnum.STUDENT.equals(receiverEnum)){
                    for (Message message : map.get(receiverEnum)){
                        try {
                            String token = JWTUtils.sign(message.getReceiverId().toString(),"password_zhzx");
                            List<StudentParent> studentParents = studentParentService.list(Wrappers.<StudentParent>lambdaQuery()
                                    .eq(StudentParent::getStudentId,message.getReceiverId())
                                    .ne(StudentParent::getType,MessageParentEnum.NOT_SEND_MESSAGE)
                            );
                            List<String> stringList = studentParents.stream().filter(item-> !StringUtils.isNullOrEmpty(item.getWxParentId())).map(item->item.getWxParentId()).collect(Collectors.toList());
                            String content = "来自："+message.getName()+"\n标题："+message.getTitle()+"\n<a href='"+settings.getParams()+"?token="+token+"&messageId="+message.getId()+"'>详情请查看</a>\n";
                            Boolean success = wxSendMessageService.sendMessage(content,stringList);
                            if(success){
                                messageService.update(Wrappers.<Message>lambdaUpdate()
                                        .set(Message::getIsSend,YesNoEnum.YES)
                                        .set(Message::getSendNum,message.getSendNum()+1)
                                        .eq(Message::getId,message.getId())
                                );
                            }
                        }catch (Exception e){
                            e.getMessage();
                        }
                    }
                }else if(ReceiverEnum.TEACHER.equals(receiverEnum)){
                    for (Message message : map.get(receiverEnum)){
                        try {
                            List<String> userList = new ArrayList<>();
                            Staff staff = staffService.getById(message.getReceiverId());
                            if(staff.getWxUsername() != null && staff.getWxUsername() != ""){
                                userList.add(staff.getWxUsername());
                            }else{
                                userList.add(staff.getEmployeeNumber());
                                userList.add(staff.getPhone());
                                if(NameToPinyin.format(staff.getName()) != null){
                                    userList.add(NameToPinyin.format(staff.getName()));
                                }
                            }
                            String token = JWTUtils.sign(message.getReceiverId().toString(),"password_zhzx");
                            Boolean success = wxSendMessageService.sendTeacherMessage("来自："+message.getName()+"\n标题："+message.getTitle()+
                                    "\n<a href='"+settings.getParams()+"?token="+token+"&messageId="+message.getId()+"'>详情请查看</a>",userList);
                            if(success){
                                messageService.update(Wrappers.<Message>lambdaUpdate()
                                        .set(Message::getIsSend,YesNoEnum.YES)
                                        .set(Message::getSendNum,message.getSendNum()+1)
                                        .eq(Message::getId,message.getId())
                                );
                            }
                        }catch (Exception e){
                            e.getMessage();
                        }
                    }
                }
            }
        }
    }

    /**
     * 根据corn生成子表发送信息
     */
    @Scheduled(cron = "0 0/3 * ? * ?")
    private void createMessage() {
        Date time = new Date();
        Settings settings = settingsService.getOne(Wrappers.<Settings>lambdaQuery()
                .eq(Settings::getCode,"MESSAGE_TASK_TIME")
        );
        if(settings == null){
            settings = new Settings();
            settings.setCode("MESSAGE_TASK_TIME");
            settings.setRemark("上次生成字表时间");
            settings.setParams(DateUtils.format(time));
            settings.setDefault().validate(true);
            settingsService.save(settings);
        }else{
            time = DateUtils.parse(settings.getParams());
            settingsService.update(new Settings(),Wrappers.<Settings>lambdaUpdate()
                    .set(Settings::getParams,DateUtils.format(new Date()))
                    .eq(Settings::getId,settings.getId())
            );
        }
        List<MessageTask> messageTasks = messageTaskService.list(Wrappers.<MessageTask>lambdaQuery()
                .lt(MessageTask::getStartTime,time)
                .gt(MessageTask::getEndTime,time)
                .eq(MessageTask::getSendType,YesNoEnum.YES)
        );
        for (MessageTask messageTask : messageTasks){
            try{
                CronExpression expression = new CronExpression(messageTask.getCron());
                Date newDate = expression.getNextValidTimeAfter(time);
                if(newDate != null &&
                        newDate.getTime() < messageTask.getEndTime().getTime() &&
                        newDate.getTime() > time.getTime()){
                    int count = messageService.count(Wrappers.<Message>lambdaQuery()
                            .eq(Message::getSendTime,newDate)
                            .eq(Message::getMessageTaskId,messageTask.getId())
                    );
                    if(count > 0){
                        break;
                    }
                    List<Message> messageList = new ArrayList<>();
                    for (MessageTaskReceiver messageTaskReceiver : messageTask.getMessageTaskReceiverList()){
                        Message message = new Message();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(newDate);
                        calendar.add(Calendar.HOUR_OF_DAY,messageTask.getRepeatSend().intValue());
                        message.setRepeatSendTime(calendar.getTime());
                        message.setMessageTaskId(messageTask.getId());
                        message.setName(messageTask.getName() == null ? " ":messageTask.getName());
                        message.setTitle(messageTask.getTitle() == null ? " ":messageTask.getTitle());
                        message.setContent(messageTask.getContent());
                        message.setSenderId(messageTask.getEditorId());
                        message.setSenderName(messageTask.getEditorName());
                        message.setReceiverId(messageTaskReceiver.getReceiverId());
                        message.setReceiverName(messageTaskReceiver.getReceiverName());
                        message.setReceiverType(messageTaskReceiver.getReceiverType());
                        message.setSendTime(newDate);
                        message.setNeedWrite(messageTask.getNeedWrite());
                        message.setDefault().validate(true);
                        messageList.add(message);
                    }
                    messageService.batchInsert(messageList);
                }
            }catch (Exception e){
                log.error(e.getMessage());
            }
        }
    }

    /**
     * 同步学生晚自习信息
     */
    @Schedules(value = {
            @Scheduled(cron = "0 45-59/1 17 ? * ?"),
            @Scheduled(cron = "0 0-14/1 18 ? * ?"),
            @Scheduled(cron = "0 15-55/5 18 ? * ?"),
            @Scheduled(cron = "0 0/5 19-22 ? * ?")
    })
    private void syncNightAttendance() {
        Date date = new Date();
        Date startTime = DateUtils.parse("00:00",date);
        Date endTime = DateUtils.parse("23:59",date);
        List<NightStudyAttendance> nightStudyAttendanceList = nightStudyAttendanceService.list(Wrappers.<NightStudyAttendance>lambdaQuery()
                .ge(NightStudyAttendance::getRegisterDate,startTime)
                .le(NightStudyAttendance::getRegisterDate,endTime)
                .orderByDesc(NightStudyAttendance::getId)
        );
        if(CollectionUtils.isNotEmpty(nightStudyAttendanceList)){
            Map<Long,List<NightStudyAttendance>> map = nightStudyAttendanceList.stream().collect(Collectors.groupingBy(NightStudyAttendance::getClazzId));
            for (Long clazzId : map.keySet()){
                List<NightStudyAttendance> clazzAttendance = map.get(clazzId);
                Map<StudentNightDutyTypeEnum,List<NightStudyAttendance>> clazzAttendanceMap = clazzAttendance.stream().collect(Collectors.groupingBy(NightStudyAttendance::getStage));
                for (StudentNightDutyTypeEnum studentNightDutyTypeEnum :clazzAttendanceMap.keySet()){
                    List<NightStudyAttendance> clazzAttendanceStage = clazzAttendanceMap.get(studentNightDutyTypeEnum);
                    clazzAttendanceStage.sort(Comparator.comparing(NightStudyAttendance::getUpdateTime, Comparator.reverseOrder()));
                    NightDutyClassDto nightDutyClassDto = new NightDutyClassDto();
                    nightDutyClassDto.setClazzId(clazzId);
                    nightDutyClassDto.setShouldStudentCount(clazzAttendanceStage.get(0).getShouldNum());
                    nightDutyClassDto.setActualStudentCount(clazzAttendanceStage.get(0).getActualNum());
                    nightDutyClassDto.setTime(date);
                    if("STAGE_ONE".equals(studentNightDutyTypeEnum.toString())){
                        nightDutyClassDto.setTeacherDutyTypeEnum(TeacherDutyTypeEnum.STAGE_ONE);
                    }else if("STAGE_TWO".equals(studentNightDutyTypeEnum.toString())){
                        nightDutyClassDto.setTeacherDutyTypeEnum(TeacherDutyTypeEnum.STAGE_TWO);
                    }else{
                        continue;
                    }
                    teacherDutyClazzService.updateStudentNum(nightDutyClassDto);
                }
            }
        }
    }

    /**
     * 每天23点 发送晚自习值班意见建议
     * todo
     */
    @Scheduled(cron = "0 0 23 ? * ?")
    private void createTeacherNightDutyMessage() {
        Settings settings = settingsService.getOne(Wrappers.<Settings>lambdaQuery().eq(Settings::getCode,"MESSAGE_SWITCH"));
        JSONObject jsonObject = JSON.parseObject(settings.getParams());
        if(Objects.equals(jsonObject.getString("ADVISOR"),"YES")){
            List<CommentNightDutyDto> commentNightDutyDtoList = this.commentMapper.getNightDutyComments();
            List<String> staffList;
            Map<Long, List<CommentNightDutyDto>> commentNightDutyDtoMap = commentNightDutyDtoList.stream().collect(Collectors.groupingBy(CommentNightDutyDto::getClazzId));
            for (Map.Entry<Long, List<CommentNightDutyDto>> mell : commentNightDutyDtoMap.entrySet()) {
                List<CommentNightDutyDto> commentNightDutyDtos = mell.getValue();
                Staff staff = commentNightDutyDtos.get(0).getAdvisor();
                if (staff == null) continue;
                staffList = new ArrayList<>();
                if(StringUtils.isNullOrEmpty(staff.getWxUsername())) {
                    staffList.add(staff.getEmployeeNumber());
                    staffList.add(staff.getPhone());
                    if(NameToPinyin.format(staff.getName()) != null ){
                        staffList.add(NameToPinyin.format(staff.getName()));
                    }
                } else {
                    staffList.add(staff.getWxUsername());
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("今晚值班情况 \r\n");
                for (CommentNightDutyDto commentNightDutyDto : commentNightDutyDtos) {
                    stringBuilder
                            .append(commentNightDutyDto.getCommentList().stream().map(Comment::getContent).collect(Collectors.joining(", ")))
                            .append("\r\n");
                }
                try {
                    wxSendMessageService.sendTeacherMessage(stringBuilder.toString(),staffList);
                } catch (Exception e){
                    log.error(e.getMessage());
                }
            }
        }
    }

    /**
     * 每天18点，提醒老师值班
     * todo 总值班判断dutymode
     */
    @Scheduled(cron = "0 0 18 ? * ?")
    private void createWorkMessageUrlTeacher() {


        List<TeacherDutyDto> teacherDutyList = teacherDutyMapper.nightRoutine(new Date(),"DAY",null,null);
        if(CollectionUtils.isNotEmpty(teacherDutyList)){
            Settings settings = settingsService.getOne(Wrappers.<Settings>lambdaQuery().eq(Settings::getCode,"MESSAGE_SWITCH"));
            JSONObject jsonObject = JSON.parseObject(settings.getParams());

            if((Objects.equals(jsonObject.getString("TEACHER_DUTY"),"YES"))
                    || (Objects.equals(jsonObject.getString("GRADE_DUTY"),"YES"))){

                Map<Long,List<TeacherDutyDto>> map = teacherDutyList.stream().filter(item->!(item.getTeacherId() == null || item.getTeacher() == null
                        || CollectionUtils.isEmpty(item.getTeacherDutyClazzList()))).collect(Collectors.groupingBy(item->item.getTeacher().getId()));
                List<String> staffList;
                List<Map<String, Object>> articles;
                Map<String, Object> newsMap;
                for (Long teacherId :map.keySet()){
                    staffList = new ArrayList<>();
                    articles = new ArrayList<>();
                    newsMap = new HashMap<>();
                    List<TeacherDutyDto> teacherDutyDtoList = map.get(teacherId);
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("您今天有晚值班");
                    int len = stringBuilder.length();
                    boolean gradeTotalDuty = false;
                    for(TeacherDutyDto teacherDutyDto : teacherDutyDtoList){
                        if(Objects.equals(TeacherDutyTypeEnum.STAGE_ONE,teacherDutyDto.getDutyType())){
                            String clazzName = teacherDutyDto.getTeacherDutyClazzList().stream().map(NightDutyClassDto::getClazzRemark).collect(Collectors.joining("，"));
                            stringBuilder.append("\r\n第一阶段："+ clazzName);
                        }else if(Objects.equals(TeacherDutyTypeEnum.STAGE_TWO,teacherDutyDto.getDutyType())){
                            String clazzName = teacherDutyDto.getTeacherDutyClazzList().stream().map(NightDutyClassDto::getClazzRemark).collect(Collectors.joining("，"));
                            stringBuilder.append("\r\n第二阶段："+ clazzName);
                        }else if(Objects.equals(TeacherDutyTypeEnum.GRADE_TOTAL_DUTY,teacherDutyDto.getDutyType()) && TeacherDutyModeEnum.HOLIDAY.equals(teacherDutyDto.getDutyMode())){
                            String clazzName = teacherDutyDto.getTeacherDutyClazzList().get(0).getGradeName();
                            stringBuilder.append("\r\n年级总值班："+ clazzName);
                            gradeTotalDuty = true;
                        }
                    }
                    if (stringBuilder.length() == len) continue;

                    String phone = teacherDutyDtoList.get(0).getTeacher().getPhone();

                    String jumpUrl = "http://info.njzhzx.net:18089/app/#/pages/nl/nl?code=" + phone;
                    if (gradeTotalDuty) {
                        jumpUrl = jumpUrl + "&redirect=gradeDutyView";
                    }

                    stringBuilder.append("\r\n(点击卡片即可去值班)");

                    Date time = teacherDutyDtoList.get(0).getStartTime();
                    String title = DateUtils.format(time,"yyyy-MM-dd E") + "值班提醒";

                    AcademicYearSemester academicYearSemester = this.academicYearSemesterService.getOne(Wrappers.<AcademicYearSemester>lambdaQuery()
                            .eq(AcademicYearSemester::getIsDefault, YesNoEnum.YES));
                    List<SchoolWeek> schoolWeeks = this.academicYearSemesterService.getWeeks(academicYearSemester.getId());
                    for (SchoolWeek schoolWeek : schoolWeeks) {
                        if (time.compareTo(schoolWeek.getStartTime()) >= 0 &&
                                time.compareTo(schoolWeek.getEndTime()) <= 0) {
                            title = "第" + schoolWeek.getWeek() + "周" + title;
                            break;
                        }
                    }

                    newsMap.put("title", title);
                    newsMap.put("description", stringBuilder.toString());
                    newsMap.put("url", jumpUrl);
                    newsMap.put("picurl", "http://218.94.40.182:18088/upload/zhzx_notice/3.jpg");
                    articles.add(newsMap);

                    if(StringUtils.isNullOrEmpty(teacherDutyDtoList.get(0).getTeacher().getWxUsername())){
                        staffList.add(teacherDutyDtoList.get(0).getTeacher().getEmployeeNumber());
                        staffList.add(teacherDutyDtoList.get(0).getTeacher().getPhone());
                        if(NameToPinyin.format(teacherDutyDtoList.get(0).getTeacher().getName()) != null ){
                            staffList.add(NameToPinyin.format(teacherDutyDtoList.get(0).getTeacher().getName()));
                        }
                    }else{
                        staffList.add(teacherDutyDtoList.get(0).getTeacher().getWxUsername());
                    }
                    try {
                        StaffMessageRefuse staffMessageRefuse = staffMessageRefuseMapper.selectOne(Wrappers.<StaffMessageRefuse>lambdaQuery()
                                .eq(StaffMessageRefuse::getStaffId,teacherDutyDtoList.get(0).getTeacherId())
                                .eq(StaffMessageRefuse::getName,"晚自习值班")
                                .eq(StaffMessageRefuse::getStatus,YesNoEnum.YES)
                        );
                        if(staffMessageRefuse == null){
                            wxSendMessageService.sendTeacherMessageNews(articles,staffList);
                        }
                    }catch (Exception e){
                        log.error(e.getMessage());
                    }
                }
            }
        }

    }

    /**
     * 每天九点十分，发送明日值班
     * todo
     */
    @Scheduled(cron = "0 10 9 ? * ?")
    private void createWorkMessage() {

        Settings settings = settingsService.getOne(Wrappers.<Settings>lambdaQuery().eq(Settings::getCode,"MESSAGE_SWITCH"));
        JSONObject jsonObject = JSON.parseObject(settings.getParams());

        List<Message> messageList = new ArrayList<>();
        Message message;
        List<String> staffList;

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,1);
        // TODO: 2022/9/14 这里查的是今天
        List<TeacherDutyDto> teacherDutyList = teacherDutyMapper.nightRoutine(new Date(),"DAY",null,null);
        if(CollectionUtils.isNotEmpty(teacherDutyList)){
            if((Objects.equals(jsonObject.getString("TEACHER_DUTY"),"YES"))
                    || (Objects.equals(jsonObject.getString("GRADE_DUTY"),"YES"))) {
                Map<Long, List<TeacherDutyDto>> map = teacherDutyList.stream().filter(item -> !(item.getTeacherId() == null || item.getTeacher() == null
                        || CollectionUtils.isEmpty(item.getTeacherDutyClazzList()))).collect(Collectors.groupingBy(item -> item.getTeacher().getId()));
                for (Long teacherId : map.keySet()) {
                    staffList = new ArrayList<>();
                    List<TeacherDutyDto> teacherDutyDtoList = map.get(teacherId);
                    StringBuilder stringBuilder = new StringBuilder("您今天有晚值班。");
                    stringBuilder.append("\r\n" + DateUtils.format(teacherDutyDtoList.get(0).getStartTime(), "yyyy-MM-dd") + "晚自习值班：");
                    int len = stringBuilder.length();
                    for (TeacherDutyDto teacherDutyDto : teacherDutyDtoList) {
                        if (Objects.equals(TeacherDutyTypeEnum.STAGE_ONE, teacherDutyDto.getDutyType())) {
                            String clazzName = teacherDutyDto.getTeacherDutyClazzList().stream().map(NightDutyClassDto::getClazzRemark).collect(Collectors.joining(","));
                            stringBuilder.append("\r\n第一阶段：" + clazzName);
                        } else if (Objects.equals(TeacherDutyTypeEnum.STAGE_TWO, teacherDutyDto.getDutyType())) {
                            String clazzName = teacherDutyDto.getTeacherDutyClazzList().stream().map(NightDutyClassDto::getClazzRemark).collect(Collectors.joining(","));
                            stringBuilder.append("\r\n第二阶段：" + clazzName);
                        } else if (Objects.equals(TeacherDutyTypeEnum.GRADE_TOTAL_DUTY, teacherDutyDto.getDutyType()) && TeacherDutyModeEnum.HOLIDAY.equals(teacherDutyDto.getDutyMode())) {
                            String clazzName = teacherDutyDto.getTeacherDutyClazzList().get(0).getGradeName();
                            stringBuilder.append("\r\n年级总值班：" + clazzName);
                        }
                    }
                    if (stringBuilder.length() == len) continue;

                    message = new Message();
                    message.setMessageTaskId(-1L);
                    message.setName("晚自习值班");
                    message.setTitle("晚自习值班");
                    message.setContent(stringBuilder.toString());
                    message.setSenderId(-1L);
                    message.setSenderName("系统");
                    message.setReceiverId(teacherDutyDtoList.get(0).getTeacherId());
                    message.setReceiverName(teacherDutyDtoList.get(0).getTeacher().getName());
                    message.setReceiverType(ReceiverEnum.TEACHER);
                    message.setSendTime(new Date());
                    message.setIsSend(YesNoEnum.YES);
                    message.setDefault().validate(true);
                    messageList.add(message);
                    if (StringUtils.isNullOrEmpty(teacherDutyDtoList.get(0).getTeacher().getWxUsername())) {
                        staffList.add(teacherDutyDtoList.get(0).getTeacher().getEmployeeNumber());
                        staffList.add(teacherDutyDtoList.get(0).getTeacher().getPhone());
                        if (NameToPinyin.format(teacherDutyDtoList.get(0).getTeacher().getName()) != null) {
                            staffList.add(NameToPinyin.format(teacherDutyDtoList.get(0).getTeacher().getName()));
                        }
                    } else {
                        staffList.add(teacherDutyDtoList.get(0).getTeacher().getWxUsername());
                    }
                    try {
                        StaffMessageRefuse staffMessageRefuse = staffMessageRefuseMapper.selectOne(Wrappers.<StaffMessageRefuse>lambdaQuery()
                                .eq(StaffMessageRefuse::getStaffId, teacherDutyDtoList.get(0).getTeacherId())
                                .eq(StaffMessageRefuse::getName, "晚自习值班")
                                .eq(StaffMessageRefuse::getStatus, YesNoEnum.YES)
                        );
                        if (staffMessageRefuse == null) {
                            wxSendMessageService.sendTeacherMessage(stringBuilder.toString(), staffList);
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }
            }
        }

        if(Objects.equals(jsonObject.getString("TOTAL_DUTY"),"YES")){
            List<LeaderDuty> leaderDuties = leaderDutyMapper.selectList(Wrappers.<LeaderDuty>lambdaQuery()
                    .ge(LeaderDuty::getStartTime,DateUtils.parse("00:00",calendar.getTime()))
                    .le(LeaderDuty::getStartTime,DateUtils.parse("23:59",calendar.getTime()))
            );
            Map<Long,List<LeaderDuty>> leaderDutyMap = leaderDuties.stream().filter(item->item.getLeader() != null).collect(Collectors.groupingBy(LeaderDuty::getLeaderId));
            for (Long leaderId :leaderDutyMap.keySet()){
                LeaderDuty leaderDuty = leaderDutyMap.get(leaderId).get(0);
                staffList = new ArrayList<>();
                if(leaderDuty.getLeader() == null){
                    continue;
                }
                String prefix = "您明天有" + leaderDuty.getSchoolyard().getName() + "值班。";
                message = new Message();
                message.setMessageTaskId(-1L);
                message.setName("领导值班");
                message.setTitle("领导值班");
                message.setContent(prefix + DateUtils.format(leaderDuty.getStartTime(),"yyyy-MM-dd")+" 总值班");
                message.setSenderId(-1L);
                message.setSenderName("系统");
                message.setReceiverId(leaderDuty.getLeaderId());
                message.setReceiverName(leaderDuty.getLeader().getName());
                message.setReceiverType(ReceiverEnum.TEACHER);
                message.setSendTime(new Date());
                message.setIsSend(YesNoEnum.YES);
                message.setDefault().validate(true);
                messageList.add(message);
                if(StringUtils.isNullOrEmpty(leaderDuty.getLeader().getWxUsername())){
                    staffList.add(leaderDuty.getLeader().getEmployeeNumber());
                    staffList.add(leaderDuty.getLeader().getPhone());
                    if(NameToPinyin.format(leaderDuty.getLeader().getName()) != null ){
                        staffList.add(NameToPinyin.format(leaderDuty.getLeader().getName()));
                    }
                }else{
                    staffList.add(leaderDuty.getLeader().getWxUsername());
                }
                try {
                    StaffMessageRefuse staffMessageRefuse = staffMessageRefuseMapper.selectOne(Wrappers.<StaffMessageRefuse>lambdaQuery()
                            .eq(StaffMessageRefuse::getStaffId,leaderDuty.getLeaderId())
                            .eq(StaffMessageRefuse::getName,"领导值班")
                            .eq(StaffMessageRefuse::getStatus,YesNoEnum.YES)
                    );
                    String suffix = " 总值班";
                    if ("雨花校区".equals(leaderDuty.getSchoolyard().getName())) {
                        suffix = "\r\n友情提示：雨花校区总值班室密码：159369";
                    }
                    if(staffMessageRefuse == null){
                        wxSendMessageService.sendTeacherMessage(prefix + "\r\n"+DateUtils.format(leaderDuty.getStartTime(),"yyyy-MM-dd")+ suffix,staffList);
                    }
                }catch (Exception e){
                    log.error(e.getMessage());
                }
            }
        }
        if(CollectionUtils.isNotEmpty(messageList)){
            messageService.batchInsert(messageList);
        }
    }

    /**
     * 计算消息回复统计
     */
    @Scheduled(cron = "0 0/5 * ? * ?")
    private void calculateMessage() {
        Date time = new Date();
        Settings settings = settingsService.getOne(Wrappers.<Settings>lambdaQuery()
                .eq(Settings::getCode,"MESSAGE_STATISTICS")
        );
        if(settings == null){
            settings = new Settings();
            settings.setCode("MESSAGE_STATISTICS");
            settings.setRemark("上次计算消息回复统计");
            settings.setParams(DateUtils.format(time));
            settings.setDefault().validate(true);
            settingsService.save(settings);
        }else{
            time = DateUtils.parse(settings.getParams());
            settingsService.update(new Settings(),Wrappers.<Settings>lambdaUpdate()
                    .set(Settings::getParams,DateUtils.format(new Date()))
                    .eq(Settings::getId,settings.getId())
            );
        }
        messageService.calculateMessage(time);
    }

    /**
     * 每天15点，提醒老师明天公开课
     */
    @Scheduled(cron = "0 0 15 ? * ?")
    private void sendPublicCourseMessage() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,1);
        List<PublicCourse> publicCourseList = publicCourseService.list(Wrappers.<PublicCourse>lambdaQuery()
                .apply("to_days(start_time)" + "=" + "to_days({0})", calendar.getTime())
        );
        if(CollectionUtils.isNotEmpty(publicCourseList)){
            List<String> userList = new ArrayList<>();

            Map<String,List<PublicCourse>> map = publicCourseList.stream().collect(Collectors.groupingBy(item->item.getSubjectName()));
            StringBuilder sb = new StringBuilder();
            sb.append(DateUtils.format(calendar.getTime(),"yyyy-MM-dd")).append("公开课安排如下:").append("\r\n");
            for (String subjectName : map.keySet()) {
                sb.append(subjectName).append(":\r\n");
                for (PublicCourse publicCourse : map.get(subjectName)) {
                    sb.append(publicCourse.getTeacherName()).append(" ")
                            .append(publicCourse.getCourseName()).append(" ")
                            .append("第").append(publicCourse.getSortOrder()).append("节").append(" ")
                            .append(publicCourse.getAddress()).append("\r\n");
                    if(publicCourse.getTeacher().getWxUsername() != null && publicCourse.getTeacher().getWxUsername() != ""){
                        userList.add(publicCourse.getTeacher().getWxUsername());
                    }else{
                        userList.add(publicCourse.getTeacher().getEmployeeNumber());
                        userList.add(publicCourse.getTeacher().getPhone());
                        if(NameToPinyin.format(publicCourse.getTeacher().getName()) != null){
                            userList.add(NameToPinyin.format(publicCourse.getTeacher().getName()));
                        }
                    }
                }
            }
            wxSendMessageService.sendTeacherMessage(sb.toString(), userList);
        }
    }

    /**
     * 每天7点，提醒老师今天公开课
     */
    @Scheduled(cron = "0 0 7 ? * ?")
    private void sendTodayPublicCourseMessage() {
        Date date = new Date();
        List<PublicCourse> publicCourseList = publicCourseService.list(Wrappers.<PublicCourse>lambdaQuery()
                .apply("to_days(start_time)" + "=" + "to_days({0})", date)
        );
        if(CollectionUtils.isNotEmpty(publicCourseList)){
            List<String> userList = new ArrayList<>();
            Map<String,List<PublicCourse>> map = publicCourseList.stream().collect(Collectors.groupingBy(item->item.getSubjectName()));
            StringBuilder sb = new StringBuilder();
            sb.append(DateUtils.format(date,"yyyy-MM-dd")).append("公开课安排如下:").append("\r\n");
            for (String subjectName : map.keySet()) {
                sb.append(subjectName).append(":\r\n");
                for (PublicCourse publicCourse : map.get(subjectName)) {
                    sb.append(publicCourse.getTeacherName()).append(" ")
                            .append(publicCourse.getCourseName()).append(" ")
                            .append("第").append(publicCourse.getSortOrder()).append("节").append(" ")
                            .append(publicCourse.getAddress()).append("\r\n");
                    if(publicCourse.getTeacher().getWxUsername() != null && publicCourse.getTeacher().getWxUsername() != ""){
                        userList.add(publicCourse.getTeacher().getWxUsername());
                    }else{
                        userList.add(publicCourse.getTeacher().getEmployeeNumber());
                        userList.add(publicCourse.getTeacher().getPhone());
                        if(NameToPinyin.format(publicCourse.getTeacher().getName()) != null){
                            userList.add(NameToPinyin.format(publicCourse.getTeacher().getName()));
                        }
                    }
                }
            }
            wxSendMessageService.sendTeacherMessage(sb.toString(), userList);
        }
    }

}