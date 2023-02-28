/**
 * 项目：中华中学管理平台
 * 模型分组：成绩管理
 * 模型名称：目标模板详情表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.Exam;
import com.zhzx.server.domain.ExamGoalTemplateSub;
import com.zhzx.server.repository.ExamGoalTemplateSubMapper;
import com.zhzx.server.repository.ExamMapper;
import com.zhzx.server.repository.base.ExamGoalTemplateSubBaseMapper;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.ExamGoalTemplateSubService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamGoalTemplateSubServiceImpl extends ServiceImpl<ExamGoalTemplateSubMapper, ExamGoalTemplateSub> implements ExamGoalTemplateSubService {

    @Resource
    private ExamMapper examMapper;

    @Override
    public int updateAllFieldsById(ExamGoalTemplateSub entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ExamGoalTemplateSub> createOrUpdate(List<ExamGoalTemplateSub> entity) {
        if (CollectionUtils.isEmpty(entity)) return null;
        ExamGoalTemplateSub examGoalTemplateSub = entity.get(0);
        Long examGoalTemplateId = examGoalTemplateSub.getExamGoalTemplateId();
        Long clazzId = examGoalTemplateSub.getClazzId();
        boolean updateTemplate = entity.stream().anyMatch(item -> null != item.getId());
        if (updateTemplate) {
            // 该模板如果已被使用，则不能修改
            Integer count = this.examMapper.selectCount(Wrappers.<Exam>lambdaQuery().eq(Exam::getExamGoalTemplateId, examGoalTemplateId));
            if (count > 0) throw new ApiCode.ApiException(-1, "该模板已被使用，无法编辑");
        }

        // 名称是否重复
        List<String> examGoalTemplateSubs = entity.stream().filter(item -> item.getClazzId().equals(clazzId)).map(ExamGoalTemplateSub::getGoalName).collect(Collectors.toList());
        List<String> examGoalTemplateSubsDistinct = examGoalTemplateSubs.stream().distinct().collect(Collectors.toList());
        if (examGoalTemplateSubsDistinct.size() != examGoalTemplateSubs.size()) throw new ApiCode.ApiException(-1, "名称重复");

        entity.forEach(item -> {
            if (item.getId() != null) {
                this.baseMapper.update(null, Wrappers.<ExamGoalTemplateSub>lambdaUpdate()
                                .set(ExamGoalTemplateSub::getGoalValue, item.getGoalValue())
                                .eq(ExamGoalTemplateSub::getId, item.getId()));
            } else {
                item.validate(true);
                this.baseMapper.insert(item);
            }
        });
        return entity;
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
    public boolean saveBatch(Collection<ExamGoalTemplateSub> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(ExamGoalTemplateSubBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }


}
