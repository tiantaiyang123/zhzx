/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：晚自习表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;
import com.zhzx.server.domain.NightStudy;
import com.zhzx.server.repository.base.NightStudyBaseMapper;

@Repository
public interface NightStudyMapper extends NightStudyBaseMapper {

    NightStudy getNightStudyLeader(@Param("clazzId") Long clazzId,
                                   @Param("time") Date time,
                                   @Param("dutyType")String dutyType);

    Integer updateStudentNum(@Param("clazzId") Long clazzId,
                             @Param("time") Date time,
                             @Param("shouldStudentCount") Integer shouldStudentCount,
                             @Param("actualStudentCount")Integer actualStudentCount,
                             @Param("dutyType")String dutyType);
}
