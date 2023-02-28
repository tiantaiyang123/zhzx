/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：学校管理
 * 模型名称：考试目标班级表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.ExamGoal;
import com.zhzx.server.domain.ExamGoalClazz;
import com.zhzx.server.domain.Settings;
import com.zhzx.server.dto.exam.ExamGoalDto;
import com.zhzx.server.repository.ExamGoalClazzMapper;
import com.zhzx.server.repository.SettingsMapper;
import com.zhzx.server.repository.base.ExamGoalClazzBaseMapper;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.ExamGoalClazzService;
import com.zhzx.server.service.ExamGoalService;
import com.zhzx.server.service.ExamGoalWarningService;
import com.zhzx.server.util.JsonUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExamGoalClazzServiceImpl extends ServiceImpl<ExamGoalClazzMapper, ExamGoalClazz> implements ExamGoalClazzService {

    @Resource
    private ExamGoalService examGoalService;

    @Resource
    private ExamGoalWarningService examGoalWarningService;

    @Resource
    private SettingsMapper settingsMapper;

    @Override
    public int updateAllFieldsById(ExamGoalClazz entity) {
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
    public boolean saveBatch(Collection<ExamGoalClazz> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(ExamGoalClazzBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ExamGoalClazz saveExamGoalClazz(ExamGoalClazz entity) {
        QueryWrapper<ExamGoalClazz> queryWrapper = new QueryWrapper<ExamGoalClazz>().eq("exam_goal_id", entity.getExamGoalId()).eq("clazz_id", entity.getClazzId());
        if (this.count(queryWrapper) != 0) {
            throw new ApiCode.ApiException(-1, "该班级考试目标已设置！");
        }
        entity.setDefault().validate(true);
        this.save(entity);
        this.updateExamlGoal(entity);
        return this.getById(entity.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ExamGoalClazz updateExamGoalClazz(ExamGoalClazz entity, boolean updateAllFields) {
        if (updateAllFields) {
            this.updateAllFieldsById(entity);
        } else {
            this.updateById(entity);
        }
        this.updateExamlGoal(entity);
        return this.getById(entity.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteExamGoalClazz(Long id) {
        ExamGoalClazz entity = this.getById(id);
        this.removeById(id);
        this.updateExamlGoal(entity);
    }

    private void updateExamlGoal(ExamGoalClazz entity) {
        QueryWrapper<ExamGoalClazz> queryWrapper = new QueryWrapper<ExamGoalClazz>().eq("exam_goal_id", entity.getExamGoalId());
        List<ExamGoalClazz> examGoalClazzes = this.list(queryWrapper);
        List<Map<String, Long>> items = new ArrayList<>();
        for (ExamGoalClazz examGoalClazz : examGoalClazzes) {
            Map<String, Long> item = new HashMap<>();
            item.put("id", examGoalClazz.getClazzId());
            item.put("value1", Long.valueOf(examGoalClazz.getGoalValue()));
            item.put("value2", Long.valueOf(examGoalClazz.getSubjectValue()));
            items.add(item);
        }
        String jsonStr = "{\"goalReference\":" + JsonUtils.toJson(items) + "}";
        ExamGoal examGoal = this.examGoalService.getById(entity.getExamGoalId());
        if (examGoal == null) {
            throw new ApiCode.ApiException(-1, "考试目标不存在！");
        }
        examGoal.setGoalReference(jsonStr);
        this.examGoalService.updateById(examGoal);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int saveBatchExamGoalClazz(List<ExamGoalClazz> entityList) {
        if (entityList.size() == 0) return 0;
        QueryWrapper<ExamGoalClazz> queryWrapper = new QueryWrapper<ExamGoalClazz>().eq("exam_goal_id", entityList.get(0).getExamGoalId());
        this.remove(queryWrapper);
        List<Map<String, Long>> items = new ArrayList<>();
        for (ExamGoalClazz examGoalClazz : entityList) {
            this.save(examGoalClazz);
            Map<String, Long> item = new HashMap<>();
            item.put("id", examGoalClazz.getClazzId());
            item.put("value", Long.valueOf(examGoalClazz.getGoalValue()));
            items.add(item);
        }
        String jsonStr = "{\"goalReference\":" + JsonUtils.toJson(items) + "}";
        ExamGoal examGoal = this.examGoalService.getById(entityList.get(0).getExamGoalId());
        if (examGoal == null) {
            throw new ApiCode.ApiException(-1, "考试目标不存在！");
        }
        examGoal.setGoalReference(jsonStr);
        this.examGoalService.updateById(examGoal);

        List<ExamGoalDto> examGoalDtoList = this.examGoalService.getAllGoal(examGoal.getExamId(), examGoal.getId());
        if (CollectionUtils.isNotEmpty(examGoalDtoList)) {
            List<Settings> settingsList = examGoalDtoList.stream().map(item -> {
                Settings settings = new Settings();
                settings.setCode("GOAL_" + examGoal.getExamId() + "_" + item.getId() + "_" + item.getSubjectType());
                settings.setRemark(settings.getCode());
                settings.setParams(JSONObject.toJSONString(item));
                return settings;
            }).collect(Collectors.toList());
            this.settingsMapper.delete(Wrappers.<Settings>lambdaQuery().likeRight(Settings::getCode, "GOAL\\_" + examGoal.getExamId() + "\\_" + examGoal.getId() + "\\_"));
            this.settingsMapper.batchInsert(settingsList);
            this.examGoalWarningService.insertDefault(examGoalDtoList);
        }
        return entityList.size();
    }
}
