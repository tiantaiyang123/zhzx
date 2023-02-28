/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：走读生提前出门照片表
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
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zhzx.server.service.GoOutImagesService;
import com.zhzx.server.repository.GoOutImagesMapper;
import com.zhzx.server.repository.base.GoOutImagesBaseMapper;
import com.zhzx.server.domain.GoOutImages;
import com.zhzx.server.rest.req.GoOutImagesParam;

@Service
public class GoOutImagesServiceImpl extends ServiceImpl<GoOutImagesMapper, GoOutImages> implements GoOutImagesService {

    @Override
    public int updateAllFieldsById(GoOutImages entity) {
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
    public boolean saveBatch(Collection<GoOutImages> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(GoOutImagesBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }


}
