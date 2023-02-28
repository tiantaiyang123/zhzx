/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：偶发事件表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.Incident;
import com.zhzx.server.repository.IncidentImagesMapper;
import com.zhzx.server.repository.IncidentMapper;
import com.zhzx.server.repository.base.IncidentBaseMapper;
import com.zhzx.server.service.IncidentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class IncidentServiceImpl extends ServiceImpl<IncidentMapper, Incident> implements IncidentService {
    @Resource
    private IncidentImagesMapper incidentImagesMapper;


    @Override
    public int updateAllFieldsById(Incident entity) {
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
    public boolean saveBatch(Collection<Incident> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(IncidentBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    @Override
    public IPage pageDetail(IPage page, String leaderName, Date startTime, Date endTime, Long schoolyardId) {
        List<Incident> southGates = this.baseMapper.pageDetail(page,leaderName,startTime,endTime, schoolyardId);
        page.setRecords(southGates);
        return page;
    }

    @Override
    @Transactional
    public Integer createWithPic(Incident entity) {
        this.baseMapper.insert(entity);
        if(CollectionUtils.isNotEmpty(entity.getIncidentImagesList())){
            entity.getIncidentImagesList().stream().forEach(item->{
                item.setIncidentId(entity.getId());
                item.setDefault().validate(true);
            });
            return incidentImagesMapper.batchInsert(entity.getIncidentImagesList());
        }
        return 1;
    }
}
