/**
 * 项目：中华中学管理平台
 * 模型分组：成绩管理
 * 模型名称：考试打印日志表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.ExamPrintLog;
import com.zhzx.server.rest.req.ExamPrintLogParam;

public interface ExamPrintLogService extends IService<ExamPrintLog> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(ExamPrintLog entity);


}
