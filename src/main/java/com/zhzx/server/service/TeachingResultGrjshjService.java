/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：教学管理
 * 模型名称：个人竞赛获奖表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.TeachingResultGrjshj;
import com.zhzx.server.rest.req.TeachingResultGrjshjParam;

public interface TeachingResultGrjshjService extends IService<TeachingResultGrjshj> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(TeachingResultGrjshj entity);


}
