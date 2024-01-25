/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：权限表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import com.zhzx.server.domain.Student;
import com.zhzx.server.domain.StudentParent;
import com.zhzx.server.dto.wx.WxStudent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface WxSendMessageService {

    Boolean sendMessage(String content, List<String> userList);

    Boolean sendTeacherMessage(String content, List<String> userList);

    void sendTeacherMessageNews(List<Map<String, Object>> articles, List<String> userList);

    String getWxToken(String secret,String appId);

    Boolean isExpire(String key);

    String createWxDepartmentId();

    String createWxParent(List<StudentParent> studentParents);

    void removeWxParent(List<StudentParent> studentParents);

    void syncStudentParent(Long clazzId);

    List<WxStudent> syncStudentParent(Student student);

    void applicationMessageGet(HttpServletResponse response, HttpServletRequest request) throws IOException;

    void applicationMessagePost(HttpServletResponse response, HttpServletRequest request) throws IOException;
}
