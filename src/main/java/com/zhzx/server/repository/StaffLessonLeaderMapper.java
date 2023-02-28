/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：备课组长表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.repository;

import com.zhzx.server.dto.StaffLessonLeaderDto;
import com.zhzx.server.repository.base.StaffLessonLeaderBaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffLessonLeaderMapper extends StaffLessonLeaderBaseMapper {

    List<StaffLessonLeaderDto> selectLessonLeader(@Param("academicYearSemesterId") Long academicYearSemesterId,
                                                  @Param("gradeId") Long gradeId);

}
