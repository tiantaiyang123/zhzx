/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：职能部门表(用于一日常规意见与建议)
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.FunctionDepartment;
import com.zhzx.server.rest.req.FunctionDepartmentParam;

public interface FunctionDepartmentService extends IService<FunctionDepartment> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(FunctionDepartment entity);


}
