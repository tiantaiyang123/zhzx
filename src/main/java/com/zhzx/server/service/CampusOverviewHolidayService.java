/**
 * 项目：中华中学管理平台
 * 模型分组：一日常规管理
 * 模型名称：假期校园概况表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.CampusOverviewHoliday;

import java.util.Date;

public interface CampusOverviewHolidayService extends IService<CampusOverviewHoliday> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(CampusOverviewHoliday entity);


    CampusOverviewHoliday padSearch(Date time);

    CampusOverviewHoliday padSave(CampusOverviewHoliday entity);
}
