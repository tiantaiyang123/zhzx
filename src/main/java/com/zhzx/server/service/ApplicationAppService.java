/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：手机app应用配置表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.ApplicationApp;
import com.zhzx.server.dto.ApplicationAppDto;
import com.zhzx.server.rest.req.ApplicationAppParam;

public interface ApplicationAppService extends IService<ApplicationApp> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(ApplicationApp entity);


    List<ApplicationAppDto> searchByRole();
}
