/**
* 项目：中华中学管理平台
* 模型分组：学生管理
* 模型名称：晚自习考勤班级概况表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.NightStudyAttendanceSub;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Date;

public interface NightStudyAttendanceSubService extends IService<NightStudyAttendanceSub> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(NightStudyAttendanceSub entity);


    XSSFWorkbook exportExcel(Long schoolyardId, Long academicYearSemesterId, Long gradeId, Long clazzId, Date startTime, Date endTime);
}
