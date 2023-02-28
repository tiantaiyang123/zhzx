/**
 * 项目：中华中学管理平台
 * 模型分组：学生管理
 * 模型名称：晚自习考勤表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.Clazz;
import com.zhzx.server.domain.NightStudyAttendance;
import com.zhzx.server.domain.NightStudyAttendanceSub;
import com.zhzx.server.domain.User;
import com.zhzx.server.dto.NightDutyClassDto;
import com.zhzx.server.enums.StudentNightDutyTypeEnum;
import com.zhzx.server.enums.TeacherDutyTypeEnum;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.repository.*;
import com.zhzx.server.repository.base.NightStudyAttendanceBaseMapper;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.NightStudyAttendanceService;
import com.zhzx.server.util.DateUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class NightStudyAttendanceServiceImpl extends ServiceImpl<NightStudyAttendanceMapper, NightStudyAttendance> implements NightStudyAttendanceService {

    @Resource
    private ClazzMapper clazzMapper;

    @Resource
    private NightStudyMapper nightStudyMapper;

    @Resource
    private NightStudyDutyClazzMapper nightStudyDutyClazzMapper;

    @Resource
    private NightStudyAttendanceSubMapper nightStudyAttendanceSubMapper;

    @Override
    public int updateAllFieldsById(NightStudyAttendance entity) {
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
    public boolean saveBatch(Collection<NightStudyAttendance> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(NightStudyAttendanceBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    @Override
    public IPage<NightStudyAttendance> searchNightStudyAttendance(IPage<NightStudyAttendance> page, Long schoolyardId, Long academicYearSemesterId, Long gradeId, Long clazzId, Integer week, String registerDateFrom, String registerDateTo, String orderByClause) {
        return this.getBaseMapper().searchNightStudyAttendance(page, schoolyardId, academicYearSemesterId, gradeId, clazzId, week, null,registerDateFrom, registerDateTo, orderByClause);
    }

    @Override
    public List<Map<String, Object>> searchNightStatisticsGroupByTime(Long schoolyardId, Long academicYearSemesterId, Long gradeId, Long clazzId, Integer week, List<String> classifies, Date registerDateFrom, Date registerDateTo, String orderByClause, TeacherDutyTypeEnum dutyType) {
        String duty = dutyType == null ? null : dutyType.toString();
        List<Map<String, Object>> list = this.baseMapper.searchNightStatisticsGroupByTime(academicYearSemesterId, gradeId, clazzId, week, registerDateFrom, registerDateTo, classifies,orderByClause,duty);
        return parse(list,registerDateFrom,registerDateTo);
    }

    @Override
    public List<Map<String, Object>> searchNightStatisticsGroupByClazz(Long schoolyardId, Long academicYearSemesterId, Long gradeId, Long clazzId, Integer week, List<String> classifies, Date registerDateFrom, Date registerDateTo, String orderByClause, TeacherDutyTypeEnum dutyType) {
        String duty = dutyType == null ? null : dutyType.toString();
        List<Map<String,Object>> list = this.baseMapper.searchNightStatisticsGroupByClazz(academicYearSemesterId, gradeId, clazzId, week, registerDateFrom, registerDateTo, classifies,orderByClause,duty);
        List<Clazz> clazzList = clazzMapper.selectList(Wrappers.<Clazz>lambdaQuery()
                .eq(null != academicYearSemesterId,Clazz::getAcademicYearSemesterId,academicYearSemesterId)
                .eq(null != gradeId,Clazz::getGradeId,gradeId)
                .eq(null != clazzId,Clazz::getId,clazzId)
                .orderByAsc(Clazz::getId)
        );
        List<Map<String,Object>> mapList = new ArrayList<>();
        for(Clazz clazz : clazzList){
            Boolean flag = false;
            for (int i = 0; i < list.size(); i++) {
                if(clazz.getName().equals(list.get(i).get("clazzName"))){
                    flag = true;
                    mapList.add(list.get(i));
                    break;
                }
            }
            if(!flag){
                Map<String,Object> map = new HashMap<>();
                map.put("clazzName",clazz.getName());
                mapList.add(map);
            }
        }
        return mapList;

    }

    @Override
    @Transactional
    public int saveMultiStudent(NightStudyAttendance entity, String studentIds) {
        NightStudyAttendanceSub nightStudyAttendanceSub = this.nightStudyAttendanceSubMapper.selectOne(Wrappers.<NightStudyAttendanceSub>lambdaQuery()
                .eq(NightStudyAttendanceSub::getClazzId, entity.getClazzId())
                .eq(NightStudyAttendanceSub::getStage, entity.getStage())
                .apply("to_days(register_date)" + "=" + "to_days({0})", entity.getRegisterDate()));
        if (nightStudyAttendanceSub != null && YesNoEnum.YES.equals(nightStudyAttendanceSub.getIsFullAttendence()))
            throw new ApiCode.ApiException(-1, "已全勤 无法填报");
        Arrays.stream(studentIds.split(",")).forEach(item -> {
            entity.setId(null);
            entity.setStudentId(Long.parseLong(item));
            this.baseMapper.insert(entity);
        });
        if (nightStudyAttendanceSub == null) {
            try {
                nightStudyAttendanceSub = new NightStudyAttendanceSub();
                BeanUtils.copyProperties(nightStudyAttendanceSub, entity);
            } catch (Exception e) {
                throw new ApiCode.ApiException(-1, "bean copy error");
            }
            nightStudyAttendanceSub.setClassify("");
            nightStudyAttendanceSub.setIsFullAttendence(YesNoEnum.NO);
            nightStudyAttendanceSub.setDefault().validate(true);
            this.nightStudyAttendanceSubMapper.insert(nightStudyAttendanceSub);
        } else {
            nightStudyAttendanceSub.setActualNum(entity.getActualNum());
            nightStudyAttendanceSub.setShouldNum(entity.getShouldNum());
            nightStudyAttendanceSub.setRegisterDate(entity.getRegisterDate());
            this.nightStudyAttendanceSubMapper.updateById(nightStudyAttendanceSub);
        }
        return 1;
    }

    private List<Map<String, Object>> parse(List<Map<String, Object>> list, Date registerDateFrom, Date registerDateTo){
        List<Map<String, Object>> newList = new ArrayList<>();
        if(CollectionUtils.isEmpty(list) && registerDateFrom == null){
            return list;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(registerDateFrom);
        while (calendar.getTime().compareTo(registerDateTo) <= 0 ){
            Boolean flag = false;
            for (int i = 0; i < list.size(); i++) {
                if(list.get(i).get("registerDate").toString().equals(DateUtils.format(calendar.getTime(),"yyyy-MM-dd"))){
                    flag = true;
                    newList.add(list.get(i));
                    break;
                }
            }
            if(!flag){
                Map<String, Object> map = new HashMap<>();
                map.put("registerDate",DateUtils.format(calendar.getTime(),"yyyy-MM-dd"));
                newList.add(map);
            }
            calendar.add(Calendar.DATE,1);
        }
        return newList;
    }

    @Override
    public Object fullAttendance(NightDutyClassDto nightDutyClassDto, Integer week) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        nightDutyClassDto.setActualStudentCount(nightDutyClassDto.getShouldStudentCount());
        if(nightDutyClassDto.getClazzId() == null || nightDutyClassDto.getShouldStudentCount() == null
                || nightDutyClassDto.getActualStudentCount() == null || nightDutyClassDto.getTeacherDutyTypeEnum() == null
                || nightDutyClassDto.getTime() == null){
            throw new ApiCode.ApiException(-5,"请传入完整参数！");
        }

        Integer count = this.baseMapper.selectCount(Wrappers.<NightStudyAttendance>lambdaQuery()
                .eq(NightStudyAttendance::getClazzId, nightDutyClassDto.getClazzId())
                .eq(NightStudyAttendance::getStage, nightDutyClassDto.getTeacherDutyTypeEnum().toString())
                .apply("to_days(register_date)" + "=" + "to_days({0})", nightDutyClassDto.getTime()));
        if (count > 0) {
            throw new ApiCode.ApiException(-5,"已填写缺勤，不能全勤");
        }

        NightStudyAttendanceSub nightStudyAttendanceSub = this.nightStudyAttendanceSubMapper.selectOne(Wrappers.<NightStudyAttendanceSub>lambdaQuery()
                .eq(NightStudyAttendanceSub::getClazzId, nightDutyClassDto.getClazzId())
                .eq(NightStudyAttendanceSub::getStage, nightDutyClassDto.getTeacherDutyTypeEnum().toString())
                .apply("to_days(register_date)" + "=" + "to_days({0})", nightDutyClassDto.getTime()));
        if (nightStudyAttendanceSub != null) {
            nightStudyAttendanceSub.setActualNum(nightDutyClassDto.getActualStudentCount());
            nightStudyAttendanceSub.setShouldNum(nightDutyClassDto.getActualStudentCount());
            this.nightStudyAttendanceSubMapper.updateById(nightStudyAttendanceSub);
        } else {
            nightStudyAttendanceSub = new NightStudyAttendanceSub();
            nightStudyAttendanceSub.setRegisterDate(nightDutyClassDto.getTime());
            nightStudyAttendanceSub.setClazzId(nightDutyClassDto.getClazzId());
            nightStudyAttendanceSub.setAcademicYearSemesterId(user.getAcademicYearSemester().getId());
            nightStudyAttendanceSub.setWeek(week);
            nightStudyAttendanceSub.setActualNum(nightDutyClassDto.getActualStudentCount());
            nightStudyAttendanceSub.setShouldNum(nightDutyClassDto.getActualStudentCount());
            nightStudyAttendanceSub.setSign(user.getStudent().getName());
            TeacherDutyTypeEnum teacherDutyTypeEnum = nightDutyClassDto.getTeacherDutyTypeEnum();
            nightStudyAttendanceSub.setStage(StudentNightDutyTypeEnum.valueOf(teacherDutyTypeEnum.toString()));
            nightStudyAttendanceSub.setIsFullAttendence(YesNoEnum.YES);
            nightStudyAttendanceSub.setClassify("");
            nightStudyAttendanceSub.setDefault().validate(true);
            this.nightStudyAttendanceSubMapper.insert(nightStudyAttendanceSub);
        }

        nightStudyMapper.updateStudentNum(nightDutyClassDto.getClazzId(), nightDutyClassDto.getTime(),
                nightDutyClassDto.getShouldStudentCount(),nightDutyClassDto.getActualStudentCount(),nightDutyClassDto.getTeacherDutyTypeEnum().toString());

        if(nightDutyClassDto.getTeacherDutyTypeEnum().equals(TeacherDutyTypeEnum.STAGE_ONE)){
            return nightStudyDutyClazzMapper.updateStudentNum(nightDutyClassDto.getClazzId(),nightDutyClassDto.getTime(),
                    nightDutyClassDto.getShouldStudentCount(),nightDutyClassDto.getActualStudentCount(),0,1 );
        }else if(nightDutyClassDto.getTeacherDutyTypeEnum().equals(TeacherDutyTypeEnum.STAGE_TWO)){
            return nightStudyDutyClazzMapper.updateStudentNum(nightDutyClassDto.getClazzId(),nightDutyClassDto.getTime(),
                    nightDutyClassDto.getShouldStudentCount(),nightDutyClassDto.getActualStudentCount(),1,1);
        }else {
            throw new ApiCode.ApiException(-5,"阶段不匹配！");
        }

    }

    @Override
    public Page<NightStudyAttendance> pageDetail(Page<NightStudyAttendance> page, QueryWrapper<NightStudyAttendance> wrapper) {
        return this.baseMapper.pageDetail(page, wrapper);
    }


}
