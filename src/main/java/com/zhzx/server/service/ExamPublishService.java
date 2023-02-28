/**
 * 项目：中华中学管理平台
 * 模型分组：成绩管理
 * 模型名称：考试发布表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.ExamPublish;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.rest.req.ExamPublishParam;
import com.zhzx.server.vo.ExamPublishVo;

public interface ExamPublishService extends IService<ExamPublish> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(ExamPublish entity);


    ExamPublish createUpdate(ExamPublishVo entity);

    Integer removeAll(Long id);

    IPage<ExamPublish> listByGrade(IPage<ExamPublish> page, ExamPublishParam param, YesNoEnum containsCurrent, Long gradeId, Long academicYearSemesterId, String orderByClause);
}
