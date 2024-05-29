/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：教师值班表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.Staff;
import com.zhzx.server.domain.TeacherDuty;
import com.zhzx.server.dto.NightDutyClassDto;
import com.zhzx.server.dto.TeacherDutyDto;
import com.zhzx.server.dto.TeacherServerFormDto;
import com.zhzx.server.enums.RoutineEnum;
import com.zhzx.server.enums.TeacherDutyTypeEnum;
import com.zhzx.server.enums.YesNoEnum;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TeacherDutyService extends IService<TeacherDuty> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(TeacherDuty entity);

    BufferedImage getImage(Date timeFrom, Date timeTo, Long gradeId) throws Exception;

    Map<Object,Object> nightRoutine(Date time, RoutineEnum type);

    Page<Map<String,Object>> getTeacherDutyForm(Integer pageNum, Integer pageSize, Date timeFrom, Date timeTo, String teacherDutyName,Long gradeId, Long schoolyardId, YesNoEnum fromApp);

    Page<Map<String,Object>> getTeacherDutyFormV2(Integer pageNum, Integer pageSize, Date timeFrom, Date timeTo, String teacherDutyName,Long gradeId, Long schoolyardId, YesNoEnum fromApp);

    TeacherDutyDto getGradeTeacherDuty(Date time);

    String importTeacherDuty(Long schoolyardId, Long academicYearSemesterId, Long gradeId, String fileUrl);

    Integer updateTeacher(NightDutyClassDto nightDutyClassDto);

    Integer nightStudyConfirm(NightDutyClassDto nightDutyClassDto);

    Integer updateTeacherDuty(NightDutyClassDto nightDutyClassDto);

    IPage pageDetail(IPage teacherPage, TeacherDutyTypeEnum dutyType, String name, String clazzName,Date startTime, Date endTime, Long gradeId, Long clazzId, Long schoolyardId);

    Integer cancelTeacherDuty(NightDutyClassDto nightDutyClassDto);

    List<Staff> cancelTeacherList(Long teacherDutyId);

    List<TeacherServerFormDto> getTeacherDutyByStaffId(Date timeFrom, Date timeTo);

    Integer updateDutyMode(TeacherServerFormDto teacherServerFormDto);

    XSSFWorkbook exportExcelWorkAmount(Long schoolyardId, Long gradeId, Date startTime, Date endTime);

    XSSFWorkbook exportExcel(Long schoolyardId, Long gradeId, Date timeFrom, Date timeTo);

    String searchOneTeacher(Long schoolyardId,Long gradeId, Date time, Long classId,TeacherDutyTypeEnum stage);

    Integer updateOneTeacher(NightDutyClassDto nightDutyClassDto);

}
