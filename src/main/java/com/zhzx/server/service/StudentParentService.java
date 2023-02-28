/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：学生家长表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.StudentParent;
import com.zhzx.server.rest.req.StudentParentParam;

public interface StudentParentService extends IService<StudentParent> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(StudentParent entity);


    void importStudentParent(String fileUrl);
}
