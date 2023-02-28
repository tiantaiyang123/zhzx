/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：领导值班表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.LeaderDuty;
import com.zhzx.server.dto.DayRoutineDto;
import com.zhzx.server.dto.LeaderDutyDto;
import com.zhzx.server.repository.base.LeaderDutyBaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LeaderDutyMapper extends LeaderDutyBaseMapper {


    DayRoutineDto dayRoutine(@Param("time") Date time,
                             @Param("type") String type,
                             @Param("leaderId")Long leaderId,
                             @Param("leaderDutyTypeEnum")String leaderDutyTypeEnum);

    List<LeaderDutyDto> getLeaderDutyFormTime(@Param("page")Page page,
                                              @Param("timeFrom") Date timeFrom,
                                              @Param("timeTo") Date timeTo,
                                              @Param("leaderDutyName") String leaderDutyName,
                                              @Param("phone") String phone,
                                              @Param("schoolyardId")Long schoolyardId);

    List<LeaderDutyDto> getLeaderDutyForm(@Param("startTime")Date startTime,
                                          @Param("endTime")Date endTime,
                                          @Param("schoolyardId")Long schoolyardId);

    LeaderDuty getByTime(@Param("startTime") Date startTime,
                         @Param("dutyType")String dutyType,
                         @Param("schoolyardId")Long schoolyardId);
}
