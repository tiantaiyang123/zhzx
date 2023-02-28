/**
 * 项目：中华中学管理平台
 * 模型分组：一日常规管理
 * 模型名称：偶发事件照片表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.IncidentImages;
import com.zhzx.server.rest.req.IncidentImagesParam;

public interface IncidentImagesService extends IService<IncidentImages> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(IncidentImages entity);


}
