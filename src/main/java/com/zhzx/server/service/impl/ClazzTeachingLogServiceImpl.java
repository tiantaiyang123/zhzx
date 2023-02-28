/**
 * 项目：中华中学管理平台
 * 模型分组：学生管理
 * 模型名称：班级教学日志表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.*;
import com.zhzx.server.enums.OperationDurationEnum;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.service.*;
import com.zhzx.server.util.StringUtils;
import com.zhzx.server.vo.SchoolWeek;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zhzx.server.repository.ClazzTeachingLogMapper;
import com.zhzx.server.repository.base.ClazzTeachingLogBaseMapper;

import javax.annotation.Resource;

@Service
public class ClazzTeachingLogServiceImpl extends ServiceImpl<ClazzTeachingLogMapper, ClazzTeachingLog> implements ClazzTeachingLogService {
    @Resource
    private SubjectService subjectService;

    @Resource
    private StaffService staffService;

    @Resource
    private CourseService courseService;

    @Resource
    private ClazzService clazzService;

    @Resource
    private ClazzTeachingLogSubjectsService clazzTeachingLogSubjectsService;

    @Resource
    private ClazzTeachingLogOperationService clazzTeachingLogOperationService;

    @Override
    public int updateAllFieldsById(ClazzTeachingLog entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
    }

    /**
     * 批量插入
     *
     * @param entityList ignore
     * @param batchSize  ignore
     * @return ignore
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveBatch(Collection<ClazzTeachingLog> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(ClazzTeachingLogBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ClazzTeachingLog getClazzTeachingLog(Long clazzId, String registerDate) {

        Subject userSubject = SecurityUtils.getSubject();
        User user = (User) userSubject.getPrincipal();
        if (user.getClazz() == null) return null;
        Clazz clazz = clazzService.getBaseMapper().selectById(clazzId);
        QueryWrapper<ClazzTeachingLog> wrapper = new QueryWrapper<ClazzTeachingLog>();
        wrapper.eq("clazz_id", clazzId);
        wrapper.ge("register_date", Date.valueOf(registerDate));
        wrapper.le("register_date", Date.valueOf(registerDate));
        ClazzTeachingLog clazzTeachingLog = this.getOne(wrapper);
        if (clazzTeachingLog == null) {
            clazzTeachingLog = new ClazzTeachingLog();
            clazzTeachingLog.setAcademicYearSemesterId(user.getAcademicYearSemester().getId());
            clazzTeachingLog.setClazzId(clazzId);
            clazzTeachingLog.setRegisterDate(Date.valueOf(registerDate));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            for (SchoolWeek schoolWeek : user.getSchoolWeeks()) {
                if (registerDate.compareTo(sdf.format(schoolWeek.getStartTime())) >= 0 &&
                        registerDate.compareTo(sdf.format(schoolWeek.getEndTime())) <= 0) {
                    clazzTeachingLog.setWeek(schoolWeek.getWeek());
                    break;
                }
            }
            if (registerDate.compareTo(sdf.format(user.getSchoolWeeks().get(0).getStartTime())) < 0) {
                clazzTeachingLog.setWeek(1);
            }
            if (registerDate.compareTo(sdf.format(user.getSchoolWeeks().get(user.getSchoolWeeks().size() - 1).getEndTime())) > 0) {
                clazzTeachingLog.setWeek(user.getSchoolWeeks().get(user.getSchoolWeeks().size() - 1).getWeek());
            }
            clazzTeachingLog.setShouldNum(user.getClazz().getStudentCount());
            clazzTeachingLog.setActualNum(user.getClazz().getStudentCount());
            this.save(clazzTeachingLog);
        }
        List<com.zhzx.server.domain.Subject> subjectList = subjectService.list();
        if (clazzTeachingLog.getClazzTeachingLogSubjectsList() == null || clazzTeachingLog.getClazzTeachingLogSubjectsList().size() == 0) {
            int weekIndex = -1;
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.CHINA);
            if (sdf.format(clazzTeachingLog.getRegisterDate()).equals("星期一")) {
                weekIndex = 1;
            } else if (sdf.format(clazzTeachingLog.getRegisterDate()).equals("星期二")) {
                weekIndex = 2;
            } else if (sdf.format(clazzTeachingLog.getRegisterDate()).equals("星期三")) {
                weekIndex = 3;
            } else if (sdf.format(clazzTeachingLog.getRegisterDate()).equals("星期四")) {
                weekIndex = 4;
            } else if (sdf.format(clazzTeachingLog.getRegisterDate()).equals("星期五")) {
                weekIndex = 5;
            } else if (sdf.format(clazzTeachingLog.getRegisterDate()).equals("星期六")) {
                weekIndex = 6;
            } else {
                weekIndex = 7;
            }
            QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<Course>();
            courseQueryWrapper.eq("academic_year_semester_id", clazzTeachingLog.getAcademicYearSemesterId());
            courseQueryWrapper.eq("grade_id", clazz.getGrade().getId());
            courseQueryWrapper.eq("clazz_id", clazzId);
            courseQueryWrapper.eq("week", weekIndex);
            List<Course> courseList = courseService.list(courseQueryWrapper);
            for (int sortOrder = 1; sortOrder <= 10; sortOrder++) {
                Long subjectId = null;
                Long teacherId = null;
                if (courseList != null) {
                    for (Course course : courseList) {
                        if (course.getSortOrder() != sortOrder) continue;
                        teacherId = course.getTeacherId();
                        for (com.zhzx.server.domain.Subject subj : subjectList) {
                            if (subj.getIsMain().equals(YesNoEnum.NO)) continue;
                            if (!subj.getName().equals(course.getCourseName())) continue;
                            subjectId = subj.getId();
                            break;
                        }
                        break;
                    }
                }
                ClazzTeachingLogSubjects clazzTeachingLogSubjects = new ClazzTeachingLogSubjects();
                clazzTeachingLogSubjects.setClazzTeachingLogId(clazzTeachingLog.getId());
                clazzTeachingLogSubjects.setSortOrder(sortOrder);
                if (subjectId != null)
                    clazzTeachingLogSubjects.setSubjectId(subjectId);
                if (teacherId != null)
                    clazzTeachingLogSubjects.setTeacherId(teacherId);
                clazzTeachingLogSubjectsService.save(clazzTeachingLogSubjects);
            }
        }
        if (clazzTeachingLog.getClazzTeachingLogOperationList() == null || clazzTeachingLog.getClazzTeachingLogOperationList().size() == 0) {
            String ids = "";
            if (!StringUtils.isNullOrEmpty(clazz.getClazzDivision()) && !StringUtils.isNullOrEmpty(clazz.getSubjectLevel())) {
                ids = "," + clazz.getClazzDivision() + "," + clazz.getSubjectLevel() + ",";
            } else if (!StringUtils.isNullOrEmpty(clazz.getClazzDivision())) {
                ids = "," + clazz.getClazzDivision() + ",";
            } else if (!StringUtils.isNullOrEmpty(clazz.getSubjectLevel())) {
                ids = "," + clazz.getSubjectLevel() + ",";
            }
            for (com.zhzx.server.domain.Subject subj : subjectList) {
                if (subj.getIsMain().equals(YesNoEnum.NO)) continue;
                if (!StringUtils.isNullOrEmpty(ids) && ids.indexOf("," + subj.getId() + ",") < 0) {
                    continue;
                }
                ClazzTeachingLogOperation clazzTeachingLogOperation = new ClazzTeachingLogOperation();
                clazzTeachingLogOperation.setClazzTeachingLogId(clazzTeachingLog.getId());
                clazzTeachingLogOperation.setSubjectId(subj.getId());
                clazzTeachingLogOperation.setDuration(OperationDurationEnum.NONE);
                clazzTeachingLogOperation.setOperationDate(DateUtils.addDays(clazzTeachingLog.getRegisterDate(), -1));
                clazzTeachingLogOperation.setOperationTime(0);
                clazzTeachingLogOperationService.save(clazzTeachingLogOperation);
            }
        }
        clazzTeachingLog = this.getById(clazzTeachingLog.getId());
        for (ClazzTeachingLogOperation item : clazzTeachingLog.getClazzTeachingLogOperationList()) {
            if (item.getOperationDate() == null)
                item.setOperationDate(DateUtils.addDays(clazzTeachingLog.getRegisterDate(), -1));
        }
        this.fillSubjectAndStaff(clazzTeachingLog);
        return clazzTeachingLog;
    }

    @Override
    public List<ClazzTeachingLog> listWeekLog(Long schoolyardId, Long academicYearSemesterId, Long gradeId, Long clazzId, Integer week) {
        if (clazzId == null) return new ArrayList<>();
        if (week == null) return new ArrayList<>();
        QueryWrapper<ClazzTeachingLog> wrapper = new QueryWrapper<ClazzTeachingLog>();
        wrapper.eq("clazz_id", clazzId);
        wrapper.eq("week", week);
        wrapper.orderByAsc("register_date");
        List<ClazzTeachingLog> clazzTeachingLogs = this.list(wrapper);
        for (ClazzTeachingLog clazzTeachingLog : clazzTeachingLogs) {
            this.fillSubjectAndStaff(clazzTeachingLog);
        }
        return clazzTeachingLogs;
    }

    private void fillSubjectAndStaff(ClazzTeachingLog clazzTeachingLog){
        for (ClazzTeachingLogSubjects item : clazzTeachingLog.getClazzTeachingLogSubjectsList()) {
            if (item.getSubjectId() !=null) {
                item.setSubject(subjectService.getById(item.getSubjectId()));
            }
            if (item.getTeacherId() !=null) {
                item.setTeacher(staffService.getById(item.getTeacherId()));
            }
        }
        for (ClazzTeachingLogOperation item : clazzTeachingLog.getClazzTeachingLogOperationList()) {
            if (item.getSubjectId() !=null) {
                item.setSubject(subjectService.getById(item.getSubjectId()));
            }
        }
    }
}
