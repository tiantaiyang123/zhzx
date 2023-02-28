/**
 * 项目：中华中学管理平台
 * 模型分组：学生管理
 * 模型名称：班级教学日志科目表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhzx.server.repository.base.ClazzTeachingLogSubjectsBaseMapper;
import com.zhzx.server.vo.ClazzTeachingLogSubjectsVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface ClazzTeachingLogSubjectsMapper extends ClazzTeachingLogSubjectsBaseMapper {

    IPage<ClazzTeachingLogSubjectsVo> listAuditPage(IPage<ClazzTeachingLogSubjectsVo> page,
                                                    @Param("schoolyardId") Long schoolyardId,
                                                    @Param("academicYearSemesterId") Long academicYearSemesterId,
                                                    @Param("gradeId") Long gradeId,
                                                    @Param("clazzId") Long clazzId,
                                                    @Param("week") Integer week,
                                                    @Param("state") String state,
                                                    @Param("startTime") Date startTime,
                                                    @Param("endTime") Date endTime);

}
