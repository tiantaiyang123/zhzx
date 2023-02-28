/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：晚自习明细表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.NightStudyDetailImage;
import com.zhzx.server.repository.NightStudyDetailImageMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zhzx.server.service.NightStudyDetailService;
import com.zhzx.server.repository.NightStudyDetailMapper;
import com.zhzx.server.repository.base.NightStudyDetailBaseMapper;
import com.zhzx.server.domain.NightStudyDetail;
import com.zhzx.server.rest.req.NightStudyDetailParam;

@Service
public class NightStudyDetailServiceImpl extends ServiceImpl<NightStudyDetailMapper, NightStudyDetail> implements NightStudyDetailService {

    @Resource
    private NightStudyDetailImageMapper nightStudyDetailImageMapper;

    @Override
    public int updateAllFieldsById(NightStudyDetail entity) {
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
    public boolean saveBatch(Collection<NightStudyDetail> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(NightStudyDetailBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    @Override
    @Transactional
    public NightStudyDetail create(NightStudyDetail entity) {
        entity.setDefault().validate(true);
        this.baseMapper.insert(entity);
        if(CollectionUtils.isNotEmpty(entity.getNightStudyDetailImageList())){
            entity.getNightStudyDetailImageList().stream().forEach(nightStudyDetailImage -> {
                nightStudyDetailImage.setNightStudyDetailId(entity.getId());
                nightStudyDetailImage.setDefault().validate(true);
            });
            nightStudyDetailImageMapper.batchInsert(entity.getNightStudyDetailImageList());
        }
        return this.getById(entity.getId());
    }
}
