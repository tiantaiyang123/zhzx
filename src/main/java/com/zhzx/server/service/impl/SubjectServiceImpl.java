/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：科目表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.Clazz;
import com.zhzx.server.domain.Subject;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.repository.ClazzMapper;
import com.zhzx.server.repository.SubjectMapper;
import com.zhzx.server.repository.base.SubjectBaseMapper;
import com.zhzx.server.service.SubjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {

    @Resource
    private ClazzMapper clazzMapper;

    @Override
    public int updateAllFieldsById(Subject entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
    }

    @Override
    public List<Subject> clazzCommon(List<Long> clazzIds, String type) {
        if (CollectionUtils.isEmpty(clazzIds)) {
            return this.baseMapper.selectList(Wrappers.<Subject>lambdaQuery().eq(Subject::getIsMain, YesNoEnum.YES));
        }
        List<Clazz> clazzList = this.clazzMapper.selectList(Wrappers.<Clazz>lambdaQuery()
                .select(Clazz::getClazzDivision)
                .in(Clazz::getId, clazzIds));
        List<String> divisionList = clazzList.stream().map(item -> Arrays.asList(item.getClazzDivision().split(",")))
                .reduce(new ArrayList<>(), (a, b) -> {
                    a.addAll(b);
                    return a;
                }).stream().distinct().collect(Collectors.toList());
        if ("01".equals(type)) {
            return CollectionUtils.isEmpty(divisionList) ? new ArrayList<>() : this.baseMapper.selectList(Wrappers.<Subject>lambdaQuery().in(Subject::getId, divisionList));
        } else if ("02".equals(type)) {
            List<Subject> allSubjects = this.baseMapper.selectList(Wrappers.<Subject>lambdaQuery().eq(Subject::getIsMain, YesNoEnum.YES));
            return CollectionUtils.isEmpty(divisionList) ? allSubjects : allSubjects.stream().filter(item -> !divisionList.contains(item.getId().toString())).collect(Collectors.toList());
        }
        return new ArrayList<>();
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
    public boolean saveBatch(Collection<Subject> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(SubjectBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }


}
