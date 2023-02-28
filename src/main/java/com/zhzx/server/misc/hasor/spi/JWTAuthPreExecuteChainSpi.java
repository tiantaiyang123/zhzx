/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.misc.hasor.spi;

import com.zhzx.server.domain.User;
import com.zhzx.server.misc.SpringUtils;
import com.zhzx.server.misc.shiro.JWTFilter;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.service.UserService;
import com.zhzx.server.util.JWTUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.hasor.dataway.spi.ApiInfo;
import net.hasor.dataway.spi.PreExecuteChainSpi;
import net.hasor.utils.future.BasicFuture;
import net.hasor.web.invoker.HttpParameters;
import org.apache.shiro.util.AntPathMatcher;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@ConfigurationProperties(prefix = "hasor.dataway.auth")
public class JWTAuthPreExecuteChainSpi implements PreExecuteChainSpi {
    @Setter
    private List<String> ignorePath;
    private AntPathMatcher matcher = new AntPathMatcher();

    @Override
    public void preExecute(ApiInfo apiInfo, BasicFuture<Object> basicFuture) {
        if (this.shouldIgnore(apiInfo)) {
            return;
        }
        Map<String, String> headers = HttpParameters.headerMap();
        String token = headers.get(JWTFilter.AUTH_HEADER.toLowerCase());
        if (StringUtils.isEmpty(token)) {
            basicFuture.failed(new ApiCode.ApiException(ApiCode.UNAUTHC));
            return;
        }
        // 解密获得username，用于和数据库进行对比
        String username = JWTUtils.getUsername(token);
        if (username == null) {
            basicFuture.failed(new ApiCode.ApiException(ApiCode.UNAUTHC));
            return;
        }

        UserService userService = SpringUtils.getBean(UserService.class);
        User user = userService.selectByUsername(username);
        if (user == null) {
            basicFuture.failed(new ApiCode.ApiException(ApiCode.UNAUTHC));
            return;
        }

        if (!JWTUtils.verify(token, username, user.getPassword())) {
            basicFuture.failed(new ApiCode.ApiException(ApiCode.UNAUTHC));
            return;
        }
        apiInfo.getParameterMap().put("currentUser", user);
    }

    private boolean shouldIgnore(ApiInfo apiInfo) {
        if (ignorePath.size() == 0) {
            return true;
        }
        boolean flag = false;
        for (int i = 0; i < ignorePath.size(); i++) {
            String path = ignorePath.get(i);
            if (path.contains(":")) {
                String[] paths = path.split(":");
                if (!apiInfo.getMethod().equalsIgnoreCase(paths[0])) {
                    continue;
                }
                path = paths[1];
            }
            flag = matcher.match(path, apiInfo.getApiPath());
        }
        return flag;
    }
}
