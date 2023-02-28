/**
* 项目：中华中学管理平台
* 模型分组：成绩管理
* 模型名称：小题得分情况表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.Clazz;
import com.zhzx.server.domain.PracticeTopic;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;
import java.util.Map;

public interface PracticeTopicService extends IService<PracticeTopic> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(PracticeTopic entity);

    void importExcel(Long schoolyardId, Long academicYearSemesterId, Long gradeId, String fileUrl);

    Map<String, Object> benchTable(IPage<PracticeTopic> page, List<Clazz> clazzList);

    XSSFWorkbook benchTableExportExcel(IPage<PracticeTopic> page);
}
