/**
* 项目：中华中学管理平台
* 模型分组：成绩管理
* 模型名称：考试赋分模板表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhzx.server.domain.ExamGoalNaturalTemplate;
import com.zhzx.server.enums.ExamNaturalSettingEnum;
import com.zhzx.server.repository.ExamGoalNaturalTemplateMapper;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.ExamGoalNaturalTemplateService;
import com.zhzx.server.util.StringUtils;
import com.zhzx.server.vo.ExamGoalNaturalTemplateVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamGoalNaturalTemplateServiceImpl extends ServiceImpl<ExamGoalNaturalTemplateMapper, ExamGoalNaturalTemplate> implements ExamGoalNaturalTemplateService {

    @Override
    public int updateAllFieldsById(ExamGoalNaturalTemplate entity) {
        return this.getBaseMapper().updateAllFieldsById(entity);
    }

    @Override
    public void deleteAll(ExamGoalNaturalTemplateVo examGoalNaturalTemplateVo) {
        if (StringUtils.isNullOrEmpty(examGoalNaturalTemplateVo.getName())
                || examGoalNaturalTemplateVo.getGradeId() == null) {
            throw new ApiCode.ApiException(-5, "模板名称或年级为空！");
        }
        this.getBaseMapper().delete(
                Wrappers.<ExamGoalNaturalTemplate>lambdaQuery()
                        .eq(ExamGoalNaturalTemplate::getSettingType, examGoalNaturalTemplateVo.getSettingType())
                        .eq(ExamGoalNaturalTemplate::getGradeId, examGoalNaturalTemplateVo.getGradeId())
                        .eq(ExamGoalNaturalTemplate::getName, examGoalNaturalTemplateVo.getName()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ExamGoalNaturalTemplate> saveAll(ExamGoalNaturalTemplateVo examGoalNaturalTemplateVo) {
        if (StringUtils.isNullOrEmpty(examGoalNaturalTemplateVo.getName())
                || StringUtils.isNullOrEmpty(examGoalNaturalTemplateVo.getSubjectRatio())
                || examGoalNaturalTemplateVo.getGradeId() == null
                || examGoalNaturalTemplateVo.getSettingType() == null) {
            throw new ApiCode.ApiException(-5, "模板名称或科目比例或年级或赋分类型为空！");
        }
        // 保证名称唯一性
        LambdaQueryWrapper<ExamGoalNaturalTemplate> wrapper = Wrappers.<ExamGoalNaturalTemplate>lambdaQuery()
                .eq(ExamGoalNaturalTemplate::getGradeId, examGoalNaturalTemplateVo.getGradeId())
                .eq(ExamGoalNaturalTemplate::getName, examGoalNaturalTemplateVo.getName());
        List<ExamGoalNaturalTemplate> currList = this.getBaseMapper().selectList(wrapper);
        if (CollectionUtils.isNotEmpty(currList)) {
            String ids = examGoalNaturalTemplateVo.getIds();
            if (StringUtils.isNullOrEmpty(ids)) {
                throw new ApiCode.ApiException(-5, "该年级模板名称已经存在！");
            }
            Long id0 = Long.parseLong(ids.split(",")[0]);
            List<Long> idList = currList.stream().map(ExamGoalNaturalTemplate::getId).collect(Collectors.toList());
            // 保证更新此条
            if (!idList.contains(id0)) {
                throw new ApiCode.ApiException(-5, "该年级模板名称已经存在！");
            }
            this.getBaseMapper().delete(Wrappers.<ExamGoalNaturalTemplate>lambdaQuery().in(ExamGoalNaturalTemplate::getId, idList));
        }
        List<ExamGoalNaturalTemplate> inserts = new ArrayList<>();
        String[] arr = examGoalNaturalTemplateVo.getSubjectRatio().split(",");
        if (arr.length % 6 != 0)
            throw new ApiCode.ApiException(-5, "参数拼接有误！");
        for (String str : arr) {
            if (str.contains("-"))
                throw new ApiCode.ApiException(-5, "比例或分数不能为负！");
        }
        for (int i = 0, j = 0; i < arr.length / 6; i++) {
            ExamGoalNaturalTemplate examGoalNaturalTemplate = new ExamGoalNaturalTemplate();
            examGoalNaturalTemplate.setGradeId(examGoalNaturalTemplateVo.getGradeId());
            examGoalNaturalTemplate.setName(examGoalNaturalTemplateVo.getName());
            examGoalNaturalTemplate.setSettingType(examGoalNaturalTemplateVo.getSettingType());
            examGoalNaturalTemplate.setSubjectId(Long.parseLong(arr[j++]));
            examGoalNaturalTemplate.setAcademyRatioA(Long.parseLong(arr[j++]));
            examGoalNaturalTemplate.setAcademyRatioB(Long.parseLong(arr[j++]));
            examGoalNaturalTemplate.setAcademyRatioC(Long.parseLong(arr[j++]));
            examGoalNaturalTemplate.setAcademyRatioD(Long.parseLong(arr[j++]));
            examGoalNaturalTemplate.setAcademyRatioE(Long.parseLong(arr[j++]));
            if (ExamNaturalSettingEnum.COUNT.equals(examGoalNaturalTemplateVo.getSettingType())) {
                if (examGoalNaturalTemplate.getAcademyRatioA() + examGoalNaturalTemplate.getAcademyRatioB()
                        + examGoalNaturalTemplate.getAcademyRatioC() + examGoalNaturalTemplate.getAcademyRatioD()
                        + examGoalNaturalTemplate.getAcademyRatioE() != 100) {
                    throw new ApiCode.ApiException(-5, "比例总和不为100！");
                }
            } else {
                // 分数模式校验分数是否递减
                if (examGoalNaturalTemplate.getAcademyRatioA().compareTo(examGoalNaturalTemplate.getAcademyRatioB()) <= 0
                        || examGoalNaturalTemplate.getAcademyRatioB().compareTo(examGoalNaturalTemplate.getAcademyRatioC()) <= 0
                        || examGoalNaturalTemplate.getAcademyRatioC().compareTo(examGoalNaturalTemplate.getAcademyRatioD()) <= 0) {
                    throw new ApiCode.ApiException(-5, "分数模式下A-E等必须严格递减");
                }
                examGoalNaturalTemplate.setAcademyRatioE(0L);
            }
            inserts.add(examGoalNaturalTemplate);
        }
        this.getBaseMapper().batchInsert(inserts);
        return inserts;
    }


}
