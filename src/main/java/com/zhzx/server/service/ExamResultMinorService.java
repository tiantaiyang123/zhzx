/**
 * 项目：中华中学管理平台
 * 模型分组：成绩管理
 * 模型名称：副科考试结果表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.ExamResultMinor;

public interface ExamResultMinorService extends IService<ExamResultMinor> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(ExamResultMinor entity);


}
