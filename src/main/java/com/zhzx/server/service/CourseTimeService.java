/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：课程时间表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.CourseTime;
import com.zhzx.server.rest.req.CourseTimeParam;

public interface CourseTimeService extends IService<CourseTime> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(CourseTime entity);


    Boolean isStart(Long gradeId);
}
