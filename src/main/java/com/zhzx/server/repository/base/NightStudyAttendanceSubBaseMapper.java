/**
* 项目：中华中学管理平台
* 模型分组：学生管理
* 模型名称：晚自习考勤班级概况表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.repository.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.zhzx.server.domain.NightStudyAttendanceSub;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NightStudyAttendanceSubBaseMapper extends BaseMapper<NightStudyAttendanceSub> {
    /**
     * 根据 ID 更新所有字段
     *
     * @param entity 实体对象
     */
    int updateAllFieldsById(@Param(Constants.ENTITY) NightStudyAttendanceSub entity);
}
