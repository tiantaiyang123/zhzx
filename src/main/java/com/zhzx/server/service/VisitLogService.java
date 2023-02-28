/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：访问日志表表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.VisitLog;
import com.zhzx.server.rest.req.VisitLogParam;

public interface VisitLogService extends IService<VisitLog> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(VisitLog entity);


    int frontCreateOrUpdate(VisitLog entity);

    IPage statistics(String editorName,IPage page,String orderByClause);
}
