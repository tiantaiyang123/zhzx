/**
* 项目：中华中学管理平台
* 模型分组：系统管理
* 模型名称：宿舍学生表
* @Author: xiongwei
* @Date: 2020-07-23 17:08:00
*/

package com.zhzx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.StudentDormitory;

public interface StudentDormitoryService extends IService<StudentDormitory> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(StudentDormitory entity);

    StudentDormitory saveEntity(StudentDormitory entity);

    void importStudentDormitory(String fileUrl);
}
