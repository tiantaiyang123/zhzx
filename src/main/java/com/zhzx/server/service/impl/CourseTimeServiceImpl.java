/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：课程时间表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.util.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zhzx.server.service.CourseTimeService;
import com.zhzx.server.repository.CourseTimeMapper;
import com.zhzx.server.repository.base.CourseTimeBaseMapper;
import com.zhzx.server.domain.CourseTime;
import com.zhzx.server.rest.req.CourseTimeParam;

@Service
public class CourseTimeServiceImpl extends ServiceImpl<CourseTimeMapper, CourseTime> implements CourseTimeService {

    @Override
    public int updateAllFieldsById(CourseTime entity) {
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
    public boolean saveBatch(Collection<CourseTime> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(CourseTimeBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    @Override
    public Boolean isStart(Long gradeId) {
        CourseTime courseTime = this.baseMapper.selectOne(Wrappers.<CourseTime>lambdaQuery()
                .eq(CourseTime::getGradeId,gradeId)
                .eq(CourseTime::getSortOrder,11)
        );
        if(courseTime != null){
            Date date = new Date();
            Date startTime = DateUtils.parse(courseTime.getStartTime(),date);
            if(date.compareTo(startTime) >= 0){
                return true;
            }else{
                return false;
            }

        }
        return true;
    }
}
