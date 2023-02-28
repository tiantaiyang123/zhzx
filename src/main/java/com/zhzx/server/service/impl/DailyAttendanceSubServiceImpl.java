/**
 * 项目：中华中学管理平台
 * 模型分组：学生管理
 * 模型名称：日常考勤概况表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.DailyAttendanceSub;
import com.zhzx.server.repository.DailyAttendanceSubMapper;
import com.zhzx.server.repository.base.DailyAttendanceSubBaseMapper;
import com.zhzx.server.service.DailyAttendanceSubService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class DailyAttendanceSubServiceImpl extends ServiceImpl<DailyAttendanceSubMapper, DailyAttendanceSub> implements DailyAttendanceSubService {

    @Override
    public int updateAllFieldsById(DailyAttendanceSub entity) {
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
    public boolean saveBatch(Collection<DailyAttendanceSub> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(DailyAttendanceSubBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }


}
