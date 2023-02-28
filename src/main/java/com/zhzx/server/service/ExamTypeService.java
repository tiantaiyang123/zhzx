/**
* 项目：中华中学管理平台
* 模型分组：成绩管理
* 模型名称：考试分类表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.ExamType;
import com.zhzx.server.rest.req.ExamTypeParam;

public interface ExamTypeService extends IService<ExamType> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(ExamType entity);


}
