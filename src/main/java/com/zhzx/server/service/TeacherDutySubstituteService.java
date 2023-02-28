/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：教师值班代班表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.TeacherDutySubstitute;
import com.zhzx.server.dto.TeacherDutySubstituteDto;
import com.zhzx.server.rest.req.TeacherDutySubstituteParam;

public interface TeacherDutySubstituteService extends IService<TeacherDutySubstitute> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(TeacherDutySubstitute entity);


    IPage<TeacherDutySubstituteDto> searchMyLogPage(IPage<TeacherDutySubstituteDto> page, TeacherDutySubstituteParam param,Boolean bool);

    Integer agree(TeacherDutySubstitute entity);
}
