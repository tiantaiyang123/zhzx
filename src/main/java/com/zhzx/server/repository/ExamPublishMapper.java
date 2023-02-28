/**
 * 项目：中华中学管理平台
 * 模型分组：成绩管理
 * 模型名称：考试发布表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhzx.server.domain.ExamPublish;
import com.zhzx.server.repository.base.ExamPublishBaseMapper;
import com.zhzx.server.rest.req.ExamPublishParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamPublishMapper extends ExamPublishBaseMapper {

    IPage<ExamPublish> listByGrade(IPage<ExamPublish> page,
                                   @Param("param") ExamPublishParam param,
                                   @Param("paramList") List<String> paramList,
                                   @Param("orderByClause") String orderByClause);
}
