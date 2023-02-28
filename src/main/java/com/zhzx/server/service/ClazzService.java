/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：班级表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.Clazz;
import com.zhzx.server.enums.TeacherDutyTypeEnum;
import com.zhzx.server.vo.ClazzVo;
import com.zhzx.server.vo.GradeVo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface ClazzService extends IService<Clazz> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(Clazz entity);

    IPage<ClazzVo> pageDetail(IPage<ClazzVo> page, QueryWrapper<Clazz> wrapper);

    List<ClazzVo> getListTeacherDutyClazz(Date date,TeacherDutyTypeEnum dutyType);

    void importExcel(Long schoolyardId, Long academicYearSemesterId, String fileUrl);

    List<GradeVo> getGradeStatistics(List<Long> gradeIdList,Long academicYearSemesterId,
                                     Long schoolyardId,Integer week,String registerDateFrom,String registerDateTo);

    List<ClazzVo> getClazzStatistics(List<Long> clazzIdList,List<Long> gradeIdList,Long academicYearSemesterId,
                            Long schoolyardId,Integer week,String registerDateFrom,String registerDateTo);
}
