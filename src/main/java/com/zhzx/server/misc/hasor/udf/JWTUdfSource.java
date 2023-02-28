/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.misc.hasor.udf;

import com.zhzx.server.domain.User;
import com.zhzx.server.misc.SpringUtils;
import com.zhzx.server.misc.shiro.JWTFilter;
import com.zhzx.server.service.UserService;
import com.zhzx.server.util.JWTUtils;
import net.hasor.dataql.DimUdfSource;
import net.hasor.dataql.UdfSourceAssembly;
import org.springframework.util.StringUtils;

import java.util.Map;

@DimUdfSource("jwt")
public class JWTUdfSource implements UdfSourceAssembly {

    /**
     * 根据JWT判断是否认证，认证成功则返回用户
     * @param headers
     * @return
     */
    public static User auth(Map<String, String> headers) {
        String token = headers.get(JWTFilter.AUTH_HEADER.toLowerCase());
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        // 解密获得username，用于和数据库进行对比
        String username = JWTUtils.getUsername(token);
        if (username == null) {
            return null;
        }

        UserService userService = SpringUtils.getBean(UserService.class);
        User user = userService.selectByUsername(username);
        if (user == null) {
            return null;
        }

        if (!JWTUtils.verify(token, username, user.getPassword())) {
            return null;
        }
        return user;
    }

}
