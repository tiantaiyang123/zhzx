/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：晚自习行政值班表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.NightStudyDuty;
import com.zhzx.server.dto.LeaderNightStudyDutyDto;
import com.zhzx.server.enums.RoutineEnum;
import com.zhzx.server.enums.TeacherDutyTypeEnum;
import com.zhzx.server.rest.req.NightStudyDutyParam;

public interface NightStudyDutyService extends IService<NightStudyDuty> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(NightStudyDuty entity);


    Map<String,Object> nightStudyDuty(Date time, RoutineEnum type);

    Map<String,Object> selectNightStudyDutyClazzTeacher(Date time);

    Map<String,Object> selectNightStudyDutyClazzConfirm(Date time);

    Map<String,Object> getNightRoutineComment(Date time);

    List<LeaderNightStudyDutyDto> getDetail(Date time, Long clazzId);
}
