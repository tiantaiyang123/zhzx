/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：学校管理
 * 模型名称：目标模板表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.Exam;
import com.zhzx.server.domain.ExamGoalTemplate;
import com.zhzx.server.domain.ExamGoalTemplateSub;
import com.zhzx.server.repository.ExamGoalTemplateMapper;
import com.zhzx.server.repository.ExamGoalTemplateSubMapper;
import com.zhzx.server.repository.ExamMapper;
import com.zhzx.server.repository.base.ExamGoalTemplateBaseMapper;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.ExamGoalTemplateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;

@Service
public class ExamGoalTemplateServiceImpl extends ServiceImpl<ExamGoalTemplateMapper, ExamGoalTemplate> implements ExamGoalTemplateService {

    @Resource
    private ExamGoalTemplateSubMapper examGoalTemplateSubMapper;

    @Resource
    private ExamMapper examMapper;

    @Override
    public int updateAllFieldsById(ExamGoalTemplate entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object removeAll(Long id) {
        Integer count = this.examMapper.selectCount(Wrappers.<Exam>lambdaQuery().eq(Exam::getExamGoalTemplateId, id));
        if (count > 0) throw new ApiCode.ApiException(-1, "该模板已被使用，无法删除");
        this.baseMapper.deleteById(id);
        this.examGoalTemplateSubMapper.delete(Wrappers.<ExamGoalTemplateSub>lambdaQuery().eq(ExamGoalTemplateSub::getExamGoalTemplateId, id));
        return 1;
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
    public boolean saveBatch(Collection<ExamGoalTemplate> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(ExamGoalTemplateBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }


}
