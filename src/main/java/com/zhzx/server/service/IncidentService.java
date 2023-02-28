/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：一日常规管理
 * 模型名称：偶发事件表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.Incident;

import java.util.Date;

public interface IncidentService extends IService<Incident> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(Incident entity);


    IPage pageDetail(IPage page, String leaderName, Date startTime,Date endTime, Long schoolyardId);

    Integer createWithPic(Incident entity);
}
