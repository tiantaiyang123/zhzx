/**
* 项目：中华中学管理平台
* 模型分组：学生管理
* 模型名称：晚自习考勤班级概况表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;
import com.zhzx.server.domain.NightStudyAttendanceSub;
import com.zhzx.server.repository.base.NightStudyAttendanceSubBaseMapper;

@Repository
public interface NightStudyAttendanceSubMapper extends NightStudyAttendanceSubBaseMapper {


    List<NightStudyAttendanceSub> selectListNightAttendance(Long schoolyardId, Long academicYearSemesterId, Long gradeId, Long clazzId, Date startTime, Date endTime);
}
