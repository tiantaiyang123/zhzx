/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：教学管理
 * 模型名称：指导学生获奖表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.TeachingResultZdxshj;
import com.zhzx.server.rest.req.TeachingResultZdxshjParam;

public interface TeachingResultZdxshjService extends IService<TeachingResultZdxshj> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(TeachingResultZdxshj entity);


}
