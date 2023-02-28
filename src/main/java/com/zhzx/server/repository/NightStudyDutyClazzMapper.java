/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：晚自习行政值班班级情况表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhzx.server.domain.NightStudyDutyClazz;
import com.zhzx.server.dto.NightStudyDutyClazzDto;
import com.zhzx.server.repository.base.NightStudyDutyClazzBaseMapper;
import com.zhzx.server.vo.NightStudyDutyClazzVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface NightStudyDutyClazzMapper extends NightStudyDutyClazzBaseMapper {

    List<NightStudyDutyClazzVo> getDutyClazzList(@Param("leaderDutyId") Long leaderDutyId);

    List<NightStudyDutyClazzDto> pageDetail(IPage leaderPage,
                                            @Param("dutyType") String dutyType,
                                            @Param("name") String name,
                                            @Param("clazzName") String clazzName,
                                            @Param("startTime") Date startTime,
                                            @Param("endTime") Date endTime,
                                            @Param("gradeId") Long gradeId,
                                            @Param("clazzId") Long clazzId,
                                            @Param("schoolyardId") Long schoolyardId);

    Integer updateStudentNum(@Param("clazzId") Long clazzId,
                             @Param("time") Date time,
                             @Param("shouldStudentCount") Integer shouldStudentCount,
                             @Param("actualStudentCount") Integer actualStudentCount,
                             @Param("numStart") Integer start,
                             @Param("numEnd") Integer end);

    int batchInsertWithId(@Param("records") List<NightStudyDutyClazz> nightStudyDutyClazzes);
}
