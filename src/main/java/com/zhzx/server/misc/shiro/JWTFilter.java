/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.misc.shiro;

import com.zhzx.server.misc.shiro.JWTFilter;
import com.zhzx.server.misc.shiro.ShiroRealm;
import com.zhzx.server.rest.res.ApiResponse;
import com.zhzx.server.rest.res.ApiCode;
import com.zhzx.server.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JWTFilter extends AuthenticationFilter {

    private static final Logger log = LoggerFactory.getLogger(JWTFilter.class);

    // 登录标识
    public static String AUTH_HEADER = "Authorization";

    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        String authentication = req.getHeader(AUTH_HEADER);
        if (StringUtils.isEmpty(authentication)) {
            return false;
        }
        JWTToken token = new JWTToken(authentication);
        getSubject(request, response).login(token);
        return true;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest req = WebUtils.toHttp(request);
            if (req.getRequestURL().indexOf("share") >= 0) {
                return true;
            }
            if (((HttpServletRequest) request).getMethod().toUpperCase().equals("OPTIONS")) {
                return true;
            }
        }
        try {
            return executeLogin(request, response);
        } catch (Exception e) {
            log.error("JWT认证出错", e);
            return false;
        }
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        httpResponse.setContentType("application/json");
        httpResponse.setCharacterEncoding("utf-8");
        httpResponse.getWriter().write(JsonUtils.toJson(ApiResponse.fail(ApiCode.UNAUTHC)));
        return false;
    }

}
