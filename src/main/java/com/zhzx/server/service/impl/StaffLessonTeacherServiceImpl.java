/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：任课老师表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import java.util.List;
import java.util.Collection;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.dto.StaffLessonTeacherDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zhzx.server.service.StaffLessonTeacherService;
import com.zhzx.server.repository.StaffLessonTeacherMapper;
import com.zhzx.server.repository.base.StaffLessonTeacherBaseMapper;
import com.zhzx.server.domain.StaffLessonTeacher;

@Service
public class StaffLessonTeacherServiceImpl extends ServiceImpl<StaffLessonTeacherMapper, StaffLessonTeacher> implements StaffLessonTeacherService {

    @Override
    public int updateAllFieldsById(StaffLessonTeacher entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
    }

    @Override
    public List<StaffLessonTeacherDto> selectByGradeAndClazz(Long gradeId, Long subjectId, Long examId, List<Long> clazzIds) {
        return this.baseMapper.selectByGradeAndClazz(gradeId, subjectId, examId, clazzIds);
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
    public boolean saveBatch(Collection<StaffLessonTeacher> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(StaffLessonTeacherBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }


}
