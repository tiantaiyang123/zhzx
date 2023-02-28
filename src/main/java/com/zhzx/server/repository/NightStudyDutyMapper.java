/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：晚自习行政值班表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.repository;

import com.zhzx.server.domain.NightStudyDuty;
import com.zhzx.server.dto.LeaderNightStudyDutyDto;
import com.zhzx.server.repository.base.NightStudyDutyBaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface NightStudyDutyMapper extends NightStudyDutyBaseMapper {


    List<LeaderNightStudyDutyDto> nightRoutine(@Param("time") Date time, @Param("type") String routineEnum, @Param("leaderId") Long id);

    List<LeaderNightStudyDutyDto> getDetail(@Param("time") Date time, @Param("clazzId") Long clazzId);

    NightStudyDuty getCorrespondLeaderNightStudyDuty(@Param("schoolyardId")Long schoolyardId, @Param("time") Date time);
}
