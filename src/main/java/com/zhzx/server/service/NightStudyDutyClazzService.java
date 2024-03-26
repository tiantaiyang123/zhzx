/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：晚自习行政值班班级情况表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.NightStudyDutyClazz;
import com.zhzx.server.dto.NightDutyClassDto;
import com.zhzx.server.enums.TeacherDutyTypeEnum;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Date;

public interface NightStudyDutyClazzService extends IService<NightStudyDutyClazz> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(NightStudyDutyClazz entity);

    /**
     * pad端第二阶段值班老师更新 ----> 一个老师值多个班级，替换的时候全部替换掉了，但实际上是单个替换
     * @param nightDutyClassDto
     * @return
     */
    NightDutyClassDto createOrUpdate(NightDutyClassDto nightDutyClassDto);

    IPage pageDetail(IPage leaderPage, TeacherDutyTypeEnum dutyType, String name,String clazzName, Date startTime, Date endTime, Long gradeId, Long clazzId, Long schoolyardId);

    Integer leaderConfirm(Date time, Long clazzId,TeacherDutyTypeEnum dutyType);

    XSSFWorkbook exportExcel(TeacherDutyTypeEnum dutyType, String name, String clazzName,Date startTime, Date endTime, Long gradeId, Long clazzId, Long schoolyardId);

    Object refresh();
}
