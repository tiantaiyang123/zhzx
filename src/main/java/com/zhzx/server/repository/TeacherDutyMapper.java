/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：教师值班表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhzx.server.domain.TeacherDuty;
import com.zhzx.server.dto.*;
import com.zhzx.server.enums.TeacherDutyModeEnum;
import com.zhzx.server.enums.TeacherDutyTypeEnum;
import com.zhzx.server.repository.base.TeacherDutyBaseMapper;
import com.zhzx.server.vo.ClazzVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TeacherDutyMapper extends TeacherDutyBaseMapper {

    // 根据年级校区时间查询年级总值班
    TeacherDutyGradeTotalDto selectTeacherDutyGradeTotalDto(@Param("time") Date time,
                                                                        @Param("schoolyardId") Long schoolyardId,
                                                                        @Param("gradeId") Long gradeId);

    List<TeacherDutyDto> nightRoutine(@Param("time") Date time,
                                @Param("type") String type,
                                @Param("leaderId")Long leaderId,
                                @Param("leaderDutyTypeEnum")List<String> leaderDutyTypeEnum);

    List<TeacherServerFormDto > getTeacherDutyForm(@Param("page") Page page,
                                                @Param("timeFrom") Date timeFrom,
                                                @Param("timeTo") Date timeTo,
                                                @Param("teacherDutyName") String teacherDutyName,
                                                @Param("staffId") Long staffId,
                                                   @Param("schoolyardId") Long schoolyardId);

    TeacherDutyGradeTotalDto getTeacherDutyGradeTotalDto(@Param("time") Date time,
                                                         @Param("type") String type,
                                                         @Param("staffId") Long staffId);

    List<TeacherDutyGradeTotalDto> getTeacherDutyGradeTotalDtoList(@Param("timeFrom") Date timeFrom,
                                                                   @Param("timeTo") Date timeTo,
                                                                   @Param("type") String type);

    List<ClazzVo> getFormList(@Param("timeList") List<Date> timeList,@Param("gradeId")Long gradeId, @Param("schoolyardId") Long schoolyardId);

    //查询当天总值班及年级总值班
    List<TeacherDutyDto> getTotalDutyList(@Param("time") Date time);

    void removeByTime(@Param("dutyTime") Date dutyTime,
                      @Param("schoolyardId") Long schoolyardId,
                      @Param("gradeId") Long gradeId,
                      @Param("academicYearSemesterId") Long academicYearSemesterId);

    TeacherDuty getByTimeAndClazzId(@Param("clazzId") Long clazzId,
                                    @Param("time") Date date,
                                    @Param("dutyType") String dutyType);

    TeacherDuty getByTimeAndClazzIds(@Param("clazzIds") List<Long> clazzIds,
                                    @Param("time") Date date,
                                    @Param("dutyType") String dutyType);

    TeacherDuty getByClazz(@Param("teacherId") Long teacherId,
                                       @Param("time") Date date,
                                       @Param("dutyType") String dutyType);

    List<CommentDto> queryCommentById(@Param("nightStudyId") Long nightStudyId);

    Integer countPageDetail(@Param("dutyType") TeacherDutyTypeEnum dutyType,
                            @Param("teacherName") String name,
                            @Param("clazzName") String clazzName,
                            @Param("startTime") Date startTime,
                            @Param("endTime") Date endTime,
                            @Param("gradeId") Long gradeId,
                            @Param("clazzId") Long clazzId,
                            @Param("schoolyardId") Long schoolyardId);

    List<NightDutyClassDto> pageDetail(IPage teacherPage,
                                       @Param("dutyType") TeacherDutyTypeEnum dutyType,
                                       @Param("teacherName") String name,
                                       @Param("clazzName") String clazzName,
                                       @Param("startTime") Date startTime,
                                       @Param("endTime") Date endTime,
                                       @Param("gradeId") Long gradeId,
                                       @Param("clazzId") Long clazzId,
                                       @Param("schoolyardId") Long schoolyardId);

    TeacherDuty getByTimeDutyType(@Param("startTime") Date startTime,
                                  @Param("dutyType") String dutyType,
                                  @Param("teacherId")Long teacherId);

    Integer updateByTime(@Param("time") Date time, @Param("dutyMode") TeacherDutyModeEnum dutyMode, @Param("gradeDutyTeacher") String gradeDutyTeacher);

    List<TeacherDutyDto> selectListWithClazz(@Param("schoolyardId") Long schoolyardId,
                                             @Param("gradeId") Long gradeId,
                                             @Param("startTime") Date startTime,
                                             @Param("endTime") Date endTime);

    TeacherDuty selectByClassId(@Param("classId") Long classId,
                                @Param("startTime") Date startTime,
                                @Param("endTime") Date endTime,
                                @Param("dutyType") TeacherDutyTypeEnum dutyType);
    Integer insertReturnId(TeacherDuty teacherDuty);

}
