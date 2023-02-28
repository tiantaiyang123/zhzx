/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：班级任课老师表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service.impl;

import java.io.Serializable;
import java.util.*;
import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zhzx.server.domain.Subject;
import com.zhzx.server.domain.User;
import com.zhzx.server.service.SubjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zhzx.server.service.ClazzTeacherService;
import com.zhzx.server.repository.ClazzTeacherMapper;
import com.zhzx.server.repository.base.ClazzTeacherBaseMapper;
import com.zhzx.server.domain.ClazzTeacher;
import com.zhzx.server.rest.req.ClazzTeacherParam;

@Service
public class ClazzTeacherServiceImpl extends ServiceImpl<ClazzTeacherMapper, ClazzTeacher> implements ClazzTeacherService {
    @Resource
    private SubjectService subjectService;

    @Override
    public int updateAllFieldsById(ClazzTeacher entity) {
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
    public boolean saveBatch(Collection<ClazzTeacher> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(ClazzTeacherBaseMapper.class, SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    @Override
    public Map<String, Object> getByClazzId(Long clazzId) {
        List<Subject> subjects = subjectService.list();
        QueryWrapper<ClazzTeacher> wrapper = new QueryWrapper<ClazzTeacher>().eq("clazz_id", clazzId);
        List<ClazzTeacher> clazzTeachers = this.list(wrapper);
        Map<String, String> map = new HashMap<String, String>();
        for (Subject subject : subjects) {
            for (ClazzTeacher clazzTeacher : clazzTeachers) {
                if (clazzTeacher.getName().equals(subject.getName())) {
                    map.put("S" + subject.getId(), clazzTeacher.getTeacher());
                    break;
                }
            }
            if (!map.containsKey("S" + subject.getId()))
                map.put("S" + subject.getId(), "");
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("teacher", map);
        result.put("subject", subjects);
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateClazzTeacher(List<ClazzTeacher> entityList) {
        if (entityList.size()==0) return;
        Long clazzId = entityList.get(0).getClazzId();
        QueryWrapper<ClazzTeacher> wrapper = new QueryWrapper<ClazzTeacher>().eq("clazz_id", clazzId);
        this.remove(wrapper);
        for (ClazzTeacher entity : entityList) {
            if (entity.getTeacher() == null || entity.getTeacher() == "") continue;
            entity.setDefault().validate(true);
            this.save(entity);
        }
    }
}
