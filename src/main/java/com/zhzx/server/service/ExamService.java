/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：学校管理
 * 模型名称：考试表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.Exam;
import com.zhzx.server.rest.req.ExamParam;
import com.zhzx.server.vo.ExamVo;

import java.util.List;
import java.util.Map;

public interface ExamService extends IService<Exam> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(Exam entity);

    /**
     * 根据校区学年查询考试
     * @param map
     * @return
     */
    List<ExamVo> getExamBySemmsterAndYard(Map<String, Object> map);

    List<Exam> getList(Long schoolyardId, String academicYear, Long gradeId);

    IPage<Map<String, Object>> selectByPageDetail(IPage<Exam> page, String orderByClause, ExamParam param);

    void publish(Long examId);

    List<Exam> getListByGrade(Long schoolyardId, Long gradeId);
}
