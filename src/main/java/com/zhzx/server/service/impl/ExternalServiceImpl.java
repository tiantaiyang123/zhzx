package com.zhzx.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhzx.server.config.bean.TirConfiguration;
import com.zhzx.server.domain.*;
import com.zhzx.server.dto.CookieDto;
import com.zhzx.server.repository.*;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.ExternalService;
import com.zhzx.server.util.DateUtils;
import com.zhzx.server.util.StringUtils;
import com.zhzx.server.vo.StudentParamVo;
import org.apache.shiro.SecurityUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ExternalServiceImpl implements ExternalService {
    @Resource
    private StaffMapper staffMapper;
    @Resource
    private TirConfiguration tirConfiguration;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private StudentMapper studentMapper;
    @Resource
    private SettingsMapper settingsMapper;
    @Resource
    private CourseMapper courseMapper;
    @Resource
    private CourseTimeMapper courseTimeMapper;

    @Override
    public CookieDto acquireTirCookie() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        String realName = user.getStaff().getName();
        String phone = user.getStaff().getPhone();
        if (StringUtils.isNullOrEmpty(realName) || StringUtils.isNullOrEmpty(phone)) {
            throw new ApiCode.ApiException(-1, "请先配置您的电话号码");
        }


        String code = getCode(realName, phone);
        List<String> headerCookieList = getCookie(phone, code);

        String redisSession = "";
        for (String headerCookie : headerCookieList) {
            String[] arr = headerCookie.split("[;]");
            for (String s : arr) {
                if (s.startsWith("REDISSESSION")) {
                    redisSession = s;
                    break;
                }
            }
        }

        CookieDto cookieDto = new CookieDto();
        cookieDto.setName("REDISSESSION");
        cookieDto.setValue(redisSession.replace("REDISSESSION=", ""));
        cookieDto.setDomain("dc.njzhzx.net");
        cookieDto.setPath("/");

        return cookieDto;
    }

    @Override
    public List<Staff> listSimpleFullStaff(QueryWrapper<Staff> wrapper) {
        return this.staffMapper.listSimpleFull(wrapper);
    }

    @Override
    public List<Student> listSimpleIncrStudent(Settings settings, StudentParamVo param) {
        String currentDateTime = DateUtils.format(new Date());
        if (null == settings) {
            settings = new Settings();
            settings.setParams(currentDateTime);
            settings.setCode("SYNC_STUDENT_INFO");
            settings.setRemark("学生信息同步时间点");
            this.settingsMapper.insert(settings);
        } else {
            settings.setParams(currentDateTime);
            this.settingsMapper.updateById(settings);
        }
        return this.studentMapper.listSimpleIncrStudent(param);
    }

    @Override
    public List<Course> listSimpleIncrCourse(Settings settings, QueryWrapper<Course> wrapper) {
        String currentDateTime = DateUtils.format(new Date());
        if (null == settings) {
            settings = new Settings();
            settings.setParams(currentDateTime);
            settings.setCode("SYNC_COURSE_INFO");
            settings.setRemark("课表信息同步时间点");
            this.settingsMapper.insert(settings);
        } else {
            settings.setParams(currentDateTime);
            this.settingsMapper.updateById(settings);
        }
        List<Course> courseList = this.courseMapper.selectListSimple(wrapper);

        List<CourseTime> courseTimeList = this.courseTimeMapper.selectList(Wrappers.emptyWrapper());
        Map<String, CourseTime> map = courseTimeList.stream()
                .collect(Collectors.toMap(courseTime -> courseTime.getGradeId().toString() + courseTime.getSortOrder().toString(), Function.identity()));
        if (CollectionUtils.isNotEmpty(courseList)) {
            for (Course course : courseList) {
                CourseTime courseTime = map.get(course.getGradeId().toString() + course.getSortOrder().toString());
                if (null != courseTime) {
                    course.setCourseEndTime(courseTime.getEndTime());
                    course.setCourseStartTime(courseTime.getStartTime());
                }
            }
        }
        return courseList;
    }

    private String getCode(String realName, String phone) {
        JSONObject jsonObject = restTemplate.getForObject(
                tirConfiguration.getPcLoginCodeUrl(),
                JSONObject.class,
                new HashMap<String, Object>(){
                    {
                        this.put("name", realName);
                        this.put("mobile", phone);
                    }
                }
        );
        if (null == jsonObject || jsonObject.getInteger("code") != 0) {
            throw new ApiCode.ApiException(-2, "获取code服务内部出错");
        }
        String par1 = jsonObject.getString("par1");
        if (StringUtils.isNullOrEmpty(par1)) {
            throw new ApiCode.ApiException(-3, "获取code参数失败");
        }
        return par1;
    }

    private List<String> getCookie(String phone, String code) {
        HttpHeaders httpHeaders = new HttpHeaders();
        ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(
                tirConfiguration.getPcLoginCookieUrl() + "?mobile={mobile}&par1={par1}",
                HttpMethod.POST,
                new HttpEntity<>(null, httpHeaders),
                JSONObject.class,
                phone,
                code
        );

        if (responseEntity.getStatusCodeValue() != HttpStatus.OK.value()) {
            throw new ApiCode.ApiException(-4, "获取cookie请求失败");
        }

        JSONObject jsonObject = responseEntity.getBody();
        if (jsonObject == null || jsonObject.getInteger("code") != 0) {
            throw new ApiCode.ApiException(-5, "获取cookie服务内部出错");
        }

        List<String> headerCookieList = responseEntity.getHeaders().get("Set-Cookie");
        if (CollectionUtils.isEmpty(headerCookieList)) {
            throw new ApiCode.ApiException(-6, "获取cookie失败");
        }
        return headerCookieList;
    }

}
