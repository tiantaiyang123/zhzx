/**
 * 项目：中华中学管理平台
 * 模型分组：学生管理
 * 模型名称：因病缺课表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.ExamResult;
import com.zhzx.server.domain.Ill;
import com.zhzx.server.rest.req.IllParam;

public interface IllService extends IService<Ill> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(Ill entity);

    IPage<Ill> searchIll(IPage<Ill> page, Long schoolyardId, Long academicYearSemesterId, Long gradeId, Long clazzId, Integer week, String registerDateFrom, String registerDateTo, String orderByClause);
}
