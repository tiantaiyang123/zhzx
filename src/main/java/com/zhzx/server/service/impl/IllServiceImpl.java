/**
 * 项目：中华中学管理平台
 * 模型分组：学生管理
 * 模型名称：因病缺课表
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
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.ExamResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zhzx.server.service.IllService;
import com.zhzx.server.repository.IllMapper;
import com.zhzx.server.repository.base.IllBaseMapper;
import com.zhzx.server.domain.Ill;
import com.zhzx.server.rest.req.IllParam;

@Service
public class IllServiceImpl extends ServiceImpl<IllMapper, Ill> implements IllService {

    @Override
    public int updateAllFieldsById(Ill entity) {
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
    public boolean saveBatch(Collection<Ill> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(IllBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    @Override
    public IPage<Ill> searchIll(IPage<Ill> page, Long schoolyardId, Long academicYearSemesterId, Long gradeId, Long clazzId, Integer week, String registerDateFrom, String registerDateTo, String orderByClause) {
        return this.getBaseMapper().searchIll(page, schoolyardId, academicYearSemesterId, gradeId, clazzId, week, registerDateFrom, registerDateTo, orderByClause);
    }
}
