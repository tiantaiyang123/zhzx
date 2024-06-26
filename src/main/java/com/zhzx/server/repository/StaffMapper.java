/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：教职工表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhzx.server.domain.Staff;
import com.zhzx.server.repository.base.StaffBaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface StaffMapper extends StaffBaseMapper {


    List<Staff> getListNoDutyONE(@Param("time") Date date, @Param("dutyType") String dutyType);
    List<Staff> getListNoDutyTWO(@Param("time") Date date, @Param("dutyType") String dutyType);

    List<Staff> listSimpleFull(@Param("ew") QueryWrapper<Staff> wrapper);

    List<Staff> sendTeacherWxUsername();

    Integer batchUpdateById(List<Staff> ids);

}
