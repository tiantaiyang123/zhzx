/**
 * 项目：中华中学管理平台
 * 模型分组：成绩管理
 * 模型名称：考试分数预警表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.ExamGoalWarning;
import com.zhzx.server.domain.Subject;
import com.zhzx.server.dto.exam.ExamGoalDto;
import com.zhzx.server.enums.GoalEnum;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.repository.ExamGoalWarningMapper;
import com.zhzx.server.repository.SubjectMapper;
import com.zhzx.server.repository.base.ExamGoalWarningBaseMapper;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.ExamGoalWarningService;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ExamGoalWarningServiceImpl extends ServiceImpl<ExamGoalWarningMapper, ExamGoalWarning> implements ExamGoalWarningService {

    @Resource
    private SubjectMapper subjectMapper;

    @Override
    public int updateAllFieldsById(ExamGoalWarning entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
    }

    @Override
    public void updateEnhance(ExamGoalWarning entity) {
        Long id = entity.getId();
        Integer rightValue = entity.getRightValue();
        Integer leftValue = entity.getLeftValue();
        if (null == id || null == rightValue) {
            throw new ApiCode.ApiException(-1, "参数校验失败");
        }
        if (null != leftValue && leftValue.compareTo(rightValue) <= 0) {
            throw new ApiCode.ApiException(-1, "左边界应当大于右边界");
        }

        ExamGoalWarning examGoalWarning = this.baseMapper.selectById(id);
        if (examGoalWarning == null) {
            throw new ApiCode.ApiException(-2, "无效ID");
        }
        List<ExamGoalWarning> examGoalWarningList = this.baseMapper.selectList(Wrappers.<ExamGoalWarning>lambdaQuery()
                .eq(ExamGoalWarning::getExamId, examGoalWarning.getExamId())
                .eq(ExamGoalWarning::getGoalId, examGoalWarning.getGoalId())
                .eq(ExamGoalWarning::getSubjectId, examGoalWarning.getSubjectId())
                .eq(ExamGoalWarning::getSubjectType, examGoalWarning.getSubjectType())
                .orderByDesc(ExamGoalWarning::getRightValue));
        ExamGoalWarning examGoalWarningLastLevel = examGoalWarningList.get(0);
        if (null == leftValue && !id.equals(examGoalWarningLastLevel.getId())) {
            throw new ApiCode.ApiException(-3, "最后一级预警线只能有一条");
        }
        // 校验区间是否交叉
        examGoalWarningList = examGoalWarningList.stream().filter(item -> !id.equals(item.getId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(examGoalWarningList)) {
            int size = examGoalWarningList.size();
            examGoalWarningLastLevel = examGoalWarningList.get(0);
            if (examGoalWarningLastLevel.getLeftValue() == null) {
                examGoalWarningLastLevel.setLeftValue(Integer.MAX_VALUE);
            }
            if (rightValue.compareTo(examGoalWarningLastLevel.getLeftValue()) < 0
                    && (null != leftValue && leftValue.compareTo(examGoalWarningList.get(size - 1).getRightValue()) > 0)) {
                for (int i = 0; i <= size - 1; i++) {
                    ExamGoalWarning curr = examGoalWarningList.get(i);
                    ExamGoalWarning next;
                    if (i == size - 1) {
                        next = curr;
                    } else {
                        next = examGoalWarningList.get(i + 1);
                    }
                    if (leftValue.compareTo(curr.getRightValue()) > 0 && rightValue.compareTo(next.getLeftValue()) < 0) {
                        throw new ApiCode.ApiException(-4, "区间交叉");
                    }
                }
            }
        }
        this.baseMapper.update(null, Wrappers.<ExamGoalWarning>lambdaUpdate()
                .set(ExamGoalWarning::getLeftValue, leftValue)
                .set(ExamGoalWarning::getRightValue, rightValue)
                .eq(ExamGoalWarning::getId, id));
    }

    @Override
    @Transactional
    @SneakyThrows
    public Integer insertDefault(List<ExamGoalDto> examGoalDtoList) {
        if (CollectionUtils.isEmpty(examGoalDtoList)) return 0;

        List<Subject> subjectList = this.subjectMapper.selectList(Wrappers.<Subject>lambdaQuery()
                .eq(Subject::getIsMain, YesNoEnum.YES)
                .orderByAsc(Subject::getId));
        for (ExamGoalDto examGoalDto : examGoalDtoList) {
            List<ExamGoalWarning> examGoalWarnings = new ArrayList<>();

            Long examId = examGoalDto.getExamId();
            Long goalId = examGoalDto.getId();
            String subjectType = examGoalDto.getSubjectType();
            Integer count = this.baseMapper.selectCount(Wrappers.<ExamGoalWarning>lambdaQuery()
                    .eq(ExamGoalWarning::getExamId, examId)
                    .eq(ExamGoalWarning::getGoalId, goalId)
                    .eq(ExamGoalWarning::getSubjectType, subjectType));
            if (count > 0) continue;
            // 插入学科预警线，默认每个学科两个预警段
            for (Subject subject : subjectList) {
                if ((subjectType.equals("SCIENCE") && subject.getSubjectAlias().equals("history"))
                        || (subjectType.equals("LIBERAL") && subject.getSubjectAlias().equals("physics"))) {
                    continue;
                }
                ExamGoalWarning examGoalWarning = this.setCommon(examId, goalId, subjectType);
                ExamGoalWarning examGoalWarning1 = this.setCommon(examId, goalId, subjectType);
                examGoalWarning.setSubjectId(subject.getId());
                examGoalWarning1.setSubjectId(subject.getId());
                if ("math".equals(subject.getSubjectAlias())) {
                    examGoalWarning.setLeftValue(10);
                    examGoalWarning.setRightValue(5);
                    examGoalWarning.setSortOrder(1);
                    examGoalWarning1.setRightValue(10);
                    examGoalWarning1.setSortOrder(2);
                } else {
                    examGoalWarning.setLeftValue(6);
                    examGoalWarning.setRightValue(3);
                    examGoalWarning.setSortOrder(1);
                    examGoalWarning1.setRightValue(6);
                    examGoalWarning1.setSortOrder(2);
                }
                examGoalWarning.validate(true);
                examGoalWarnings.add(examGoalWarning);
                examGoalWarnings.add(examGoalWarning1);

                if (YesNoEnum.YES.equals(subject.getHasWeight())) {
                    ExamGoalWarning examGoalWarning2 = new ExamGoalWarning();
                    ExamGoalWarning examGoalWarning3 = new ExamGoalWarning();
                    PropertyUtils.copyProperties(examGoalWarning2, examGoalWarning);
                    PropertyUtils.copyProperties(examGoalWarning3, examGoalWarning1);
                    examGoalWarning2.setSubjectId(subject.getId() + 100000);
                    examGoalWarning3.setSubjectId(subject.getId() + 100000);
                    examGoalWarnings.add(examGoalWarning2);
                    examGoalWarnings.add(examGoalWarning3);
                }
            }
            this.saveBatch(examGoalWarnings);
        }
        return 1;
    }

    private ExamGoalWarning setCommon(Long examId, Long goalId, String subjectType) {
        ExamGoalWarning examGoalWarning = new ExamGoalWarning();
        examGoalWarning.setExamId(examId);
        examGoalWarning.setGoalId(goalId);
        examGoalWarning.setSubjectType(GoalEnum.valueOf(subjectType));
        return examGoalWarning;
    }

    @Override
    public IPage<Map<String, Object>> selectByList(IPage<Map<String, Object>> page, QueryWrapper<ExamGoalWarning> wrapper) {
        IPage<Map<String, Object>> pageMap = this.baseMapper.selectMap(page, wrapper);
        List<Map<String, Object>> records = pageMap.getRecords();
        if (CollectionUtils.isNotEmpty(records)) {
            List<Subject> subjectList = this.subjectMapper.selectList(Wrappers.<Subject>lambdaQuery().eq(Subject::getIsMain, YesNoEnum.YES));
            this.mutate(records, subjectList);
        }
        return pageMap;
    }

    private void mutate(List<Map<String, Object>> records, List<Subject> subjectList) {
        Map<Long, Subject> subjectMap = subjectList.stream().collect(Collectors.toMap(Subject::getId, Function.identity()));
        for (Map<String, Object> record : records) {
            record.put("remark", "");
            Long subjectId = Long.valueOf(record.get("subjectId").toString());
            String subjectName;
            Subject subject = null;
            if (subjectId.equals(90002L)) {
                subjectName = "总分";
            } else {
                if (subjectId > 100000) {
                    record.put("remark", "(赋分)");
                    subjectId = subjectId - 100000;
                }
                subject = subjectMap.get(subjectId);
                subjectName = subject.getName();
            }
            record.put("subjectName", subjectName);
            record.put("subject", subject);
        }
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
    public boolean saveBatch(Collection<ExamGoalWarning> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(ExamGoalWarningBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }


}
