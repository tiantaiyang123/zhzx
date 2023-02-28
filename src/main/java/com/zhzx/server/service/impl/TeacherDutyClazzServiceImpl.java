/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：教师值班班级表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.NightStudy;
import com.zhzx.server.domain.NightStudyAttendanceSub;
import com.zhzx.server.dto.NightDutyClassDto;
import com.zhzx.server.enums.StudentNightDutyTypeEnum;
import com.zhzx.server.enums.TeacherDutyTypeEnum;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.repository.*;
import com.zhzx.server.rest.res.ApiCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zhzx.server.service.TeacherDutyClazzService;
import com.zhzx.server.repository.base.TeacherDutyClazzBaseMapper;
import com.zhzx.server.domain.TeacherDutyClazz;
import com.zhzx.server.rest.req.TeacherDutyClazzParam;

@Service
public class TeacherDutyClazzServiceImpl extends ServiceImpl<TeacherDutyClazzMapper, TeacherDutyClazz> implements TeacherDutyClazzService {
    @Resource
    private NightStudyMapper nightStudyMapper;
    @Resource
    private NightStudyAttendanceMapper nightStudyAttendanceMapper;
    @Resource
    private NightStudyAttendanceSubMapper nightStudyAttendanceSubMapper;
    @Resource
    private NightStudyDutyClazzMapper nightStudyDutyClazzMapper;

    @Override
    public int updateAllFieldsById(TeacherDutyClazz entity) {
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
    public boolean saveBatch(Collection<TeacherDutyClazz> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(TeacherDutyClazzBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    @Override
    @Transactional
    public Integer updateStudentNum(NightDutyClassDto nightDutyClassDto) {
        if(nightDutyClassDto.getClazzId() == null || nightDutyClassDto.getShouldStudentCount() == null
                || nightDutyClassDto.getActualStudentCount() == null || nightDutyClassDto.getTeacherDutyTypeEnum() == null){
            throw new ApiCode.ApiException(-5,"请传入完整参数！");
        }
        nightStudyMapper.updateStudentNum(nightDutyClassDto.getClazzId(), nightDutyClassDto.getTime(),
                nightDutyClassDto.getShouldStudentCount(),nightDutyClassDto.getActualStudentCount(),nightDutyClassDto.getTeacherDutyTypeEnum().toString());

        nightStudyAttendanceMapper.updateStudentNum(nightDutyClassDto.getClazzId(),nightDutyClassDto.getTime(),
                nightDutyClassDto.getShouldStudentCount(),nightDutyClassDto.getActualStudentCount(),nightDutyClassDto.getTeacherDutyTypeEnum().toString());

        NightStudyAttendanceSub nightStudyAttendanceSub;
        if(nightDutyClassDto.getTeacherDutyTypeEnum().equals(TeacherDutyTypeEnum.STAGE_ONE)){
            // 这里根据NightStudyDutyClazz是否有记录生成进行处理比较好
            // 如果填过全勤 直接更掉全勤人数
            nightStudyAttendanceSub = this.nightStudyAttendanceSubMapper.selectOne(Wrappers.<NightStudyAttendanceSub>lambdaQuery()
                    .eq(NightStudyAttendanceSub::getClazzId, nightDutyClassDto.getClazzId())
                    .eq(NightStudyAttendanceSub::getStage, StudentNightDutyTypeEnum.STAGE_ONE)
                    .eq(NightStudyAttendanceSub::getIsFullAttendence, YesNoEnum.YES)
                    .apply("to_days(register_date)" + "=" + "to_days({0})", nightDutyClassDto.getStartTime()));
            if (nightStudyAttendanceSub != null) {
                // 只更人数
                nightStudyAttendanceSub.setActualNum(nightDutyClassDto.getActualStudentCount());
                nightStudyAttendanceSub.setShouldNum(nightDutyClassDto.getShouldStudentCount());
                this.nightStudyAttendanceSubMapper.updateById(nightStudyAttendanceSub);
            }

            return nightStudyDutyClazzMapper.updateStudentNum(nightDutyClassDto.getClazzId(),nightDutyClassDto.getTime(),
                    nightDutyClassDto.getShouldStudentCount(),nightDutyClassDto.getActualStudentCount(),0,1 );
        }else if(nightDutyClassDto.getTeacherDutyTypeEnum().equals(TeacherDutyTypeEnum.STAGE_TWO)){
            // 这里根据NightStudyDutyClazz是否有记录生成进行处理比较好
            // 如果填过全勤 直接更掉全勤人数
            nightStudyAttendanceSub = this.nightStudyAttendanceSubMapper.selectOne(Wrappers.<NightStudyAttendanceSub>lambdaQuery()
                    .eq(NightStudyAttendanceSub::getClazzId, nightDutyClassDto.getClazzId())
                    .eq(NightStudyAttendanceSub::getStage, StudentNightDutyTypeEnum.STAGE_TWO)
                    .eq(NightStudyAttendanceSub::getIsFullAttendence, YesNoEnum.YES)
                    .apply("to_days(register_date)" + "=" + "to_days({0})", nightDutyClassDto.getStartTime()));
            if (nightStudyAttendanceSub != null) {
                // 只更人数
                nightStudyAttendanceSub.setActualNum(nightDutyClassDto.getActualStudentCount());
                nightStudyAttendanceSub.setShouldNum(nightDutyClassDto.getShouldStudentCount());
                this.nightStudyAttendanceSubMapper.updateById(nightStudyAttendanceSub);
            }

           return nightStudyDutyClazzMapper.updateStudentNum(nightDutyClassDto.getClazzId(),nightDutyClassDto.getTime(),
                    nightDutyClassDto.getShouldStudentCount(),nightDutyClassDto.getActualStudentCount(),1,1);
        }else {
            throw new ApiCode.ApiException(-5,"阶段不匹配！");
        }
    }
}
