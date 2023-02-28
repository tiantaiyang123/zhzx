/**
 * 项目：中华中学管理平台
 * 模型分组：学生管理
 * 模型名称：班级教学日志科目表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.ClazzTeachingLogSubjects;
import com.zhzx.server.vo.ClazzTeachingLogSubjectsVo;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Date;
import java.util.List;

public interface ClazzTeachingLogSubjectsService extends IService<ClazzTeachingLogSubjects> {

    /**
     * 更新全部字段
     *
     * @param entity
     * @return
     */
    int updateAllFieldsById(ClazzTeachingLogSubjects entity);

    IPage<ClazzTeachingLogSubjectsVo> listAuditPage(IPage<ClazzTeachingLogSubjectsVo> page,
                                                    Long schoolyardId,
                                                    Long academicYearSemesterId,
                                                    Long gradeId,
                                                    Long clazzId,
                                                    Integer week,
                                                    String state);

    void batchAudit(List<Long> idList);

    XSSFWorkbook exportExcel(Long schoolyardId, Long academicYearSemesterId, Long gradeId, Long clazzId, Integer week, Date startTime, Date endTime);

    Integer studentSure(Date startTime);
}
