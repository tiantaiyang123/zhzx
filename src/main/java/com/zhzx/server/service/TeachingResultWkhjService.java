/**
 * 项目：中华中学管理平台
 * 模型分组：教学管理
 * 模型名称：微课获奖表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.TeachingResultWkhj;
import com.zhzx.server.rest.req.TeachingResultWkhjParam;

public interface TeachingResultWkhjService extends IService<TeachingResultWkhj> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(TeachingResultWkhj entity);


}
