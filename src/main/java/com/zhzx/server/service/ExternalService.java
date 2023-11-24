package com.zhzx.server.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhzx.server.domain.Course;
import com.zhzx.server.domain.Settings;
import com.zhzx.server.domain.Staff;
import com.zhzx.server.domain.Student;
import com.zhzx.server.dto.CookieDto;
import com.zhzx.server.vo.StudentParamVo;

import java.util.List;

public interface ExternalService {

    CookieDto acquireTirCookie();

    List<Staff> listSimpleFullStaff(QueryWrapper<Staff> wrapper);

    List<Student> listSimpleIncrStudent(Settings settings, StudentParamVo param);

    List<Course> listSimpleIncrCourse(Settings settings, QueryWrapper<Course> wrapper);
}
