package com.zhzx.server.task;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhzx.server.domain.Message;
import com.zhzx.server.domain.PublicCourse;
import com.zhzx.server.domain.Staff;
import com.zhzx.server.enums.ReceiverEnum;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.repository.DepartmentMapper;
import com.zhzx.server.repository.MessageMapper;
import com.zhzx.server.repository.PublicCourseMapper;
import com.zhzx.server.repository.StaffMapper;
import com.zhzx.server.service.WxSendMessageService;
import com.zhzx.server.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.Calendar.*;

/**
 * 公开课定时任务
 */
//@Component
@Slf4j
public class PublicCourseTaskComp {

    @Resource
    private DepartmentMapper departmentMapper;

    @Resource
    private StaffMapper staffMapper;
    @Resource
    private WxSendMessageService wxSendMessageService;
    @Resource
    private PublicCourseMapper publicCourseMapper;
    @Resource
    private MessageMapper messageMapper;

    /**
     * 查询明天公开课的信息当天下午三点进行推送
     */
    @Scheduled(cron = "0 0 15 * * ?")
    public void afterPublicCourses() {
        //当前的日期
        LocalDate currentDate = LocalDate.now();
        //明天的日期
        LocalDate tomorrow = currentDate.plusDays(1);
        String format = MonthDay.from(tomorrow).format(DateTimeFormatter.ofPattern("MM月dd日"));
        StringBuilder time = new StringBuilder(format);
        String dayOfWeekInChinese = DateUtils.dayOfWeekInChinese(tomorrow);

        List<Staff> staffs = staffMapper.sendTeacherWxUsername();
        List<String> staffList = new ArrayList<>();
        //构建站内信集合
        List<Message> messageList = new ArrayList<>();
        //查询明天公开课的信息
        List<PublicCourse> publicCourses = publicCourseMapper.selectList(Wrappers.<PublicCourse>lambdaQuery().
                eq(PublicCourse::getStartTime, tomorrow));
        log.info("明天的公开课信息是:" + publicCourses.toString());
        StringBuilder publicMes = new StringBuilder(time+" 公开课: ");
        StringBuilder builder = new StringBuilder();
        if (CollectionUtils.isNotEmpty(publicCourses)) {
            for (PublicCourse publicCourse : publicCourses) {
                //构建发送的信息
                if (publicCourse != null) {
                    builder.append("开课时间:" + currentDate+" "+dayOfWeekInChinese + "\r\n")
                            .append("学科:" + publicCourse.getSubjectName() + "\r\n")
                            .append("节次:第" + publicCourse.getSortOrder()+"节" + "\r\n")
                            .append("开课教师:" + publicCourse.getTeacherName() + "\r\n")
                            .append("开课的班级:" + publicCourse.getClazzName() + "\r\n")
                            .append("开课课题:"+publicCourse.getCourseName()+"\r\n")
                            .append("开课地点:" + publicCourse.getAddress() + "\r\n")
                            .append("类别:" + publicCourse.getClassify()+"\r\n")
                            .append("—————————————\r\n");
                }

            }
            for (Staff staff :staffs) {
                if (StringUtils.isNotEmpty(staff.getWxUsername())) {
                    staffList.add(staff.getWxUsername());
                }
                Message message = new Message();
                message.setMessageTaskId(-1L);
                message.setName("公开课通知");
                message.setTitle("公开课通知");
                //发送企业微信消息
                message.setContent(publicMes.toString() + "\r\n" + builder.toString());
                message.setSenderId(-1L);
                message.setSenderName("系统");
                message.setReceiverId(staff.getId());
                message.setReceiverName(staff.getName());
                message.setReceiverType(ReceiverEnum.TEACHER);
                message.setSendTime(new Date());
                message.setIsSend(YesNoEnum.YES);
                message.setDefault().validate(true);
                messageList.add(message);
            }
            if (StringUtils.isNotEmpty(builder.toString())){
                //wxSendMessageService.sendTeacherMessage(publicMes.toString() + "\r\n" + builder.toString(), staffList);
            }
        }
        //消息入库
        if (CollectionUtils.isNotEmpty(messageList)) {
            messageMapper.batchInsert(messageList);
        }

    }


}
