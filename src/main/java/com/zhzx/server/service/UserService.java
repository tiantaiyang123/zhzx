/**
 * 项目：中华中学流程自动化管理平台
 * 模型分组：系统管理
 * 模型名称：用户表
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhzx.server.domain.Clazz;
import com.zhzx.server.domain.User;
import com.zhzx.server.enums.YesNoEnum;
import com.zhzx.server.vo.UserVo;

import java.util.List;

public interface UserService extends IService<User> {

    /**
     * 更新全部字段
     * @param entity
     * @return
     */
    int updateAllFieldsById(User entity);

    /**
     * 保存
     * @param entity
     * @return
     */
    boolean save(User entity);

    /**
     * 更新
     * @param entity
     * @return
     */
    boolean updateById(User entity);
    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    UserVo login(String username, String password);

    /**
     * 登录V2
     * @param username
     * @param password
     * @return
     */
    UserVo loginV2(String username, String password,String code, YesNoEnum decode);


    UserVo loginWithoutPassword(String username);

    /**
     * 用户注册
     * @param user
     * @param roleName
     * @return
     */
    User register(User user, String roleName);

    /**
     * 根据username查询
     * @param username
     * @return
     */
    User selectByUsername(String username);

    void updateIsDelete(Long id);

    void resetPassword(Long id);

    UserVo wxLogin(String code,String agentid);

    List<Clazz> mutateYear(Long academicYearSemesterId);

    void sendVerifyCode(String username);

    Integer removeUser(Long id);

    Integer allocStudentDuty(Long id, String type);
}
