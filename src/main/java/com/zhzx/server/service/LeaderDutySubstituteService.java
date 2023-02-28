/**
 * 项目：中华中学管理平台
 * 模型分组：一日常规管理
 * 模型名称：领导值班代班表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.LeaderDutySubstitute;
import com.zhzx.server.domain.TeacherDutySubstitute;
import com.zhzx.server.rest.req.LeaderDutySubstituteParam;


public interface LeaderDutySubstituteService extends IService<LeaderDutySubstitute> {
    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(LeaderDutySubstitute entity);

    IPage searchMyLogPage(IPage page, LeaderDutySubstituteParam param, Boolean bool);
}
