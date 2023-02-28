/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：学校管理
 * 模型名称：考试表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhzx.server.domain.Exam;
import com.zhzx.server.repository.base.ExamBaseMapper;
import com.zhzx.server.rest.req.ExamParam;
import com.zhzx.server.vo.ExamVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ExamMapper extends ExamBaseMapper {

    List<ExamVo> getExamBySemmsterAndYard(Map<String, Object> map);

    List<Exam> getList(@Param("schoolyardId") Long schoolyardId,
                       @Param("academicYear") String academicYear,
                       @Param("gradeId") Long gradeId);

    IPage<Map<String, Object>> selectByPageDetail(IPage<Exam> page,
                                                  @Param("orderByClause") String orderByClause,
                                                  @Param("entity") ExamParam param);
}
