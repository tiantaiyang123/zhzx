/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：领导值班表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.LeaderDuty;
import com.zhzx.server.domain.Staff;
import com.zhzx.server.dto.DayRoutineDto;
import com.zhzx.server.dto.LeaderDutyDto;
import com.zhzx.server.enums.RoutineEnum;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.vo.CheckItemVo;
import com.zhzx.server.vo.NightStudyInfoVo;
import com.zhzx.server.vo.RoutineInfoVo;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface LeaderDutyService extends IService<LeaderDuty> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(LeaderDuty entity);

    /**
     * 根据leaderduty 表查询该教职工今日的一日常规数据
     * @param time
     * @param type
     * @return
     */
    DayRoutineDto dayRoutine(Date time, RoutineEnum type);

    Page<LeaderDutyDto> getLeaderDutyForm(Integer pageNum, Integer pageSize, Date timeFrom, Date timeTo, String teacherDutyName, String phone, Long schoolyardId);

    RoutineInfoVo getRoutineInfo(Long staffId, String dutyDate);

    List<CheckItemVo> getRoutineInfoWarning(String dutyDate, YesNoEnum warning,YesNoEnum today) throws ParseException;

    NightStudyInfoVo getNightStudyInfo(Long staffId, String dutyDate);

    Integer updateLeader(Long id);

    String importLeaderDuty(Long schoolyardId, String fileUrl);

    Integer cancelUpdateLeader(Long leaderDutyId, Long leaderId);

    List<Staff> cancelLeaderChoosePeople(Long leaderDutyId);

    Map<String,Object> getDayRoutineComment(Date time);

    Integer dayRoutineClickFinish(String type, Long leaderDutyId);
}
