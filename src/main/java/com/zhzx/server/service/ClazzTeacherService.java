/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：班级任课老师表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.ClazzTeacher;
import com.zhzx.server.rest.req.ClazzTeacherParam;

public interface ClazzTeacherService extends IService<ClazzTeacher> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(ClazzTeacher entity);

    Map<String, Object> getByClazzId(Long clazzId);

    void updateClazzTeacher(List<ClazzTeacher> entityList);
}
