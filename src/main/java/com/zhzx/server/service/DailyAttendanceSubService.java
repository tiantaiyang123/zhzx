/**
 * 项目：中华中学管理平台
 * 模型分组：学生管理
 * 模型名称：日常考勤概况表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.DailyAttendanceSub;

public interface DailyAttendanceSubService extends IService<DailyAttendanceSub> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(DailyAttendanceSub entity);


}
