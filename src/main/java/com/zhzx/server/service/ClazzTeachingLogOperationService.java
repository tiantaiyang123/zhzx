/**
 * 项目：中华中学管理平台
 * 模型分组：学生管理
 * 模型名称：班级教学日志作业量反馈表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import java.util.Date;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.ClazzTeachingLogOperation;
import com.zhzx.server.rest.req.ClazzTeachingLogOperationParam;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public interface ClazzTeachingLogOperationService extends IService<ClazzTeachingLogOperation> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(ClazzTeachingLogOperation entity);

    boolean updateList(List<ClazzTeachingLogOperation> entityList);

    XSSFWorkbook exportExcel(Long schoolyardId, Long academicYearSemesterId, Long gradeId, Long clazzId, Integer week, Date startTime, Date endTime);
}
