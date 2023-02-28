/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：晚自习行政值班年级图片表
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
import com.zhzx.server.rest.res.ApiCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zhzx.server.service.NightStudyDutyGradeImagesService;
import com.zhzx.server.repository.NightStudyDutyGradeImagesMapper;
import com.zhzx.server.repository.base.NightStudyDutyGradeImagesBaseMapper;
import com.zhzx.server.domain.NightStudyDutyGradeImages;
import com.zhzx.server.rest.req.NightStudyDutyGradeImagesParam;

@Service
public class NightStudyDutyGradeImagesServiceImpl extends ServiceImpl<NightStudyDutyGradeImagesMapper, NightStudyDutyGradeImages> implements NightStudyDutyGradeImagesService {

    @Override
    public int updateAllFieldsById(NightStudyDutyGradeImages entity) {
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
    public boolean saveBatch(Collection<NightStudyDutyGradeImages> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(NightStudyDutyGradeImagesBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    @Override
    @Transactional
    public Integer padUpdateImg(List<NightStudyDutyGradeImages> nightStudyDutyGradeImagesList) {
        nightStudyDutyGradeImagesList.stream().forEach(nightStudyDutyGradeImages -> {
            nightStudyDutyGradeImages.setDefault().validate(true);
        });
        Long gradeId = nightStudyDutyGradeImagesList.get(0).getGradeId();
        Long nightStudyDutyId = nightStudyDutyGradeImagesList.get(0).getNightStudyDutyId();
        this.baseMapper.delete(Wrappers.<NightStudyDutyGradeImages>lambdaQuery()
                .eq(NightStudyDutyGradeImages::getNightStudyDutyId,nightStudyDutyId)
                .eq(NightStudyDutyGradeImages::getGradeId,gradeId)
        );
        if(CollectionUtils.isNotEmpty(nightStudyDutyGradeImagesList)){
            return this.baseMapper.batchInsert(nightStudyDutyGradeImagesList);
        }
        return 1;
    }
}
