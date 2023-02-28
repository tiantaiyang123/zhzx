/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：教职工表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import java.util.Date;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.Staff;
import com.zhzx.server.enums.TeacherDutyTypeEnum;

public interface StaffService extends IService<Staff> {

    /**
     * 更新全部字段
     *
     * @param entity
     * @return
     */
    int updateAllFieldsById(Staff entity);

    Staff saveStaff(Staff entity);

    Staff updateStaff(Staff entity, boolean updateAllFields);

    void updateIsDelete(Long id);

    void delete(Long id);

    void importExcel(String fileUrl);

    List<Staff> getListNoDuty(Date date, TeacherDutyTypeEnum dutyType);

    void updateLessonTeacher();
}
