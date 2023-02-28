/**
 * 项目：中华中学管理平台
 * 模型分组：学生管理
 * 模型名称：班级教学日志表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.ClazzTeachingLog;
import com.zhzx.server.rest.req.ClazzTeachingLogParam;

public interface ClazzTeachingLogService extends IService<ClazzTeachingLog> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(ClazzTeachingLog entity);

    ClazzTeachingLog getClazzTeachingLog(Long clazzId, String registerDate);

    List<ClazzTeachingLog> listWeekLog(Long schoolyardId, Long academicYearSemesterId, Long gradeId, Long clazzId, Integer week);
}
