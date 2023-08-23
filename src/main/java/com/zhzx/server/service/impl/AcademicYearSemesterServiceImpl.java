/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：学年学期表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.AcademicYearSemester;
import com.zhzx.server.enums.SemesterEnum;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.repository.AcademicYearSemesterMapper;
import com.zhzx.server.repository.base.AcademicYearSemesterBaseMapper;
import com.zhzx.server.service.AcademicYearSemesterService;
import com.zhzx.server.util.DateUtils;
import com.zhzx.server.vo.SchoolWeek;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class AcademicYearSemesterServiceImpl extends ServiceImpl<AcademicYearSemesterMapper, AcademicYearSemester> implements AcademicYearSemesterService {

    @Override
    public int updateAllFieldsById(AcademicYearSemester entity) {
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
    public boolean saveBatch(Collection<AcademicYearSemester> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(AcademicYearSemesterBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    /**
     * 设置为当前学期
     * @param academicYearSemesterId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void setCurrentYearSemester(Long academicYearSemesterId) {
        AcademicYearSemester academicYearSemester = this.getById(academicYearSemesterId);
        UpdateWrapper<AcademicYearSemester> updateWrapper = new UpdateWrapper<AcademicYearSemester>();
        updateWrapper.set("is_default", "NO");
        updateWrapper.eq("is_default", "YES");
        this.update(updateWrapper);
        updateWrapper = new UpdateWrapper<AcademicYearSemester>();
        updateWrapper.set("is_default", "YES");
        updateWrapper.eq("id", academicYearSemesterId);
        this.update(updateWrapper);
    }

    @Override
    public AcademicYearSemester getCurrentYearSemester(Long academicYearSemesterId) {
        if (null != academicYearSemesterId) {
            return this.getById(academicYearSemesterId);
        }

        AcademicYearSemester currentAcademicYearSemester;

        // 最高优先级
        currentAcademicYearSemester = this.getOne(
                Wrappers.<AcademicYearSemester>lambdaQuery()
                        .eq(AcademicYearSemester::getIsDefault, YesNoEnum.YES)
        );
        if (null != currentAcademicYearSemester) {
            return currentAcademicYearSemester;
        }

        // 迭代时间进行范围匹配
        List<AcademicYearSemester> academicYearSemesterList = this.list(Wrappers.<AcademicYearSemester>query().orderByAsc("start_time"));
        String currentDateStr = DateUtils.format(new Date(), "yyyy-MM-dd");

        for (int i = 0; i < academicYearSemesterList.size(); i++) {
            currentAcademicYearSemester = academicYearSemesterList.get(i);
            if (i == academicYearSemesterList.size() - 1) {
                if (DateUtils.format(currentAcademicYearSemester.getEndTime(), "yyyy-MM-dd").compareTo(currentDateStr) < 0) {
                    currentAcademicYearSemester.setEndTime(new Date());
                }
            } else {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(academicYearSemesterList.get(i + 1).getStartTime());
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                currentAcademicYearSemester.setEndTime(calendar.getTime());
            }
        }

        for (int i = 0; i < academicYearSemesterList.size(); i++) {
            currentAcademicYearSemester = academicYearSemesterList.get(i);
            String startTime = DateUtils.format(academicYearSemesterList.get(i).getStartTime(), "yyyy-MM-dd");
            String endTime = DateUtils.format(academicYearSemesterList.get(i).getEndTime(), "yyyy-MM-dd");
            if (currentDateStr.compareTo(startTime) >= 0 && currentDateStr.compareTo(endTime) <= 0) {
                return currentAcademicYearSemester;
            }
        }
        return null;
    }

    @Override
    public AcademicYearSemester getYearSemesterByDate(String dateStr) {
        AcademicYearSemester currentAcademicYearSemester = null;
        List<AcademicYearSemester> academicYearSemesterList = this.list(Wrappers.<AcademicYearSemester>query().orderByAsc("start_time"));
        for (int i = 0; i < academicYearSemesterList.size(); i++) {
            currentAcademicYearSemester = academicYearSemesterList.get(i);
            if (i == academicYearSemesterList.size() - 1) {
                if (DateUtils.format(currentAcademicYearSemester.getEndTime(), "yyyy-MM-dd").compareTo(dateStr) < 0) {
                    currentAcademicYearSemester.setEndTime(new Date());
                }
            } else {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(academicYearSemesterList.get(i + 1).getStartTime());
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                currentAcademicYearSemester.setEndTime(calendar.getTime());
            }
        }
        for (int i = 0; i < academicYearSemesterList.size(); i++) {
            currentAcademicYearSemester = academicYearSemesterList.get(i);
            String startTime = DateUtils.format(academicYearSemesterList.get(i).getStartTime(), "yyyy-MM-dd");
            String endTime = DateUtils.format(academicYearSemesterList.get(i).getEndTime(), "yyyy-MM-dd");
            if (dateStr.compareTo(startTime) >= 0 && dateStr.compareTo(endTime) <= 0) {
                return currentAcademicYearSemester;
            }
        }
        return null;
    }

    @Value("#{'${academic.year_one_start}'.split('-')}")
    private String[] monthDayAcademicOne;
    @Value("#{'${academic.year_two_start}'.split('-')}")
    private String[] monthDayAcademicTwo;

    @Override
    @SneakyThrows
    public List<SchoolWeek> getWeeks(Long academicYearSemesterId) {
        List<SchoolWeek> result = new ArrayList<>();
        if (academicYearSemesterId == null || academicYearSemesterId <= 0)
            return result;
        Date startTime = null, endTime = null;
        AcademicYearSemester academicYearSemester = this.getCurrentYearSemester(academicYearSemesterId);
        if (academicYearSemester != null) {
            startTime = academicYearSemester.getStartTime();
            endTime = academicYearSemester.getEndTime();
        }
        if (startTime == null || endTime == null)
            return result;
        // 暑假或寒假上课算第0周， 同时修正startTime
        SchoolWeek schoolWeekMutate = null;
        ZonedDateTime zonedDateTime = startTime.toInstant().atZone(ZoneId.systemDefault());
        String month = SemesterEnum.Q1.equals(academicYearSemester.getSemester()) ? monthDayAcademicOne[0] : monthDayAcademicTwo[0];
        String day = SemesterEnum.Q1.equals(academicYearSemester.getSemester()) ? monthDayAcademicOne[1] : monthDayAcademicTwo[1];
        if (!month.equals(String.valueOf(zonedDateTime.getMonthValue())) || !day.equals(String.valueOf(zonedDateTime.getDayOfMonth()))) {
            Date startTimeStd = DateUtils.parse(String.valueOf(zonedDateTime.getYear()).concat("-").concat(month).concat("-").concat(day), "yyyy-MM-dd");
            // 只处理提前情况
            if (startTime.before(startTimeStd)) {
                schoolWeekMutate = new SchoolWeek();
                schoolWeekMutate.setAcademicYearSemesterId(academicYearSemesterId);
                schoolWeekMutate.setWeek(0);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startTimeStd);
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                schoolWeekMutate.setEndTime(calendar.getTime());
                schoolWeekMutate.setStartTime(startTime);
                startTime = startTimeStd;
            }
        }
        int week = 1;
        SchoolWeek schoolWeek = new SchoolWeek();
        while (startTime.compareTo(endTime) <= 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd E", Locale.CHINA);
            String dateStr = sdf.format(startTime);
            if (dateStr.contains("一")) {
                if (result.size() > 0)
                    week++;
                schoolWeek = new SchoolWeek();
            }
            schoolWeek.setAcademicYearSemesterId(academicYearSemesterId);
            schoolWeek.setWeek(week);
            if (schoolWeek.getStartTime() == null)
                schoolWeek.setStartTime(startTime);
            if (dateStr.contains("日") || sdf.format(startTime).equals(sdf.format(endTime))) {
                schoolWeek.setEndTime(startTime);
                result.add(schoolWeek);
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startTime);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            startTime = calendar.getTime();
        }
        if (schoolWeekMutate != null)
            result.add(0, schoolWeekMutate);
        return result;
    }
}
