/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：访问日志表表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.User;
import com.zhzx.server.util.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zhzx.server.service.VisitLogService;
import com.zhzx.server.repository.VisitLogMapper;
import com.zhzx.server.repository.base.VisitLogBaseMapper;
import com.zhzx.server.domain.VisitLog;
import com.zhzx.server.rest.req.VisitLogParam;

@Service
public class VisitLogServiceImpl extends ServiceImpl<VisitLogMapper, VisitLog> implements VisitLogService {

    @Override
    public int updateAllFieldsById(VisitLog entity) {
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
    public boolean saveBatch(Collection<VisitLog> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(VisitLogBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    @Override
    public int frontCreateOrUpdate(VisitLog entity) {
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        Date date = new Date();

        VisitLog exist = this.baseMapper.selectOne(Wrappers.<VisitLog>lambdaQuery()
                .eq(VisitLog::getEditorId,user.getId())
                .eq(VisitLog::getProjectName,entity.getProjectName())
                .between(VisitLog::getVisitTime, DateUtils.parse("00:00",date),DateUtils.parse("23:59",date))
        );
        if(exist != null){
            return this.baseMapper.update(new VisitLog(),Wrappers.<VisitLog>lambdaUpdate()
                    .set(VisitLog::getNum,exist.getNum()+1)
                    .set(VisitLog::getVisitTime,date)
                    .eq(VisitLog::getId,exist.getId())
            );
        }else{
            entity.setVisitTime(date);
            entity.setEditorId(user.getId());
            entity.setEditorName(user.getRealName());
            entity.setDefault().validate(true);
            return this.baseMapper.insert(entity);
        }
    }

    @Override
    public IPage statistics(String editorName,IPage page,String orderByClause) {
        List<Map<String,Object>> result = this.baseMapper.statistics(new Date(),editorName,page,orderByClause);
        page.setRecords(result);
        return page;
    }
}
