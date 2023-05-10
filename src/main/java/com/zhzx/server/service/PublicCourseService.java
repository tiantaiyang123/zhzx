/**
 * 项目：中华中学管理平台
 * 模型分组：系统管理
 * 模型名称：公开课表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
 */

package com.zhzx.server.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.PublicCourse;

public interface PublicCourseService extends IService<PublicCourse> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(PublicCourse entity);

    void importExcel(Long academicYearSemesterId,Long gradeId, String fileUrl);
}