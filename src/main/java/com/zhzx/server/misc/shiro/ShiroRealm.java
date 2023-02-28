/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.misc.shiro;

import com.zhzx.server.domain.Authority;
import com.zhzx.server.domain.Role;
import com.zhzx.server.domain.User;
import com.zhzx.server.service.AuthorityService;
import com.zhzx.server.service.UserService;
import com.zhzx.server.util.JWTUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ShiroRealm extends AuthorizingRealm {

    @Value("${jwt.secret}")
    private String secret;
    private UserService userService;
    private AuthorityService authorityService;

    @Autowired
    public void setUserService(UserService userService, AuthorityService authorityService) {
        this.userService = userService;
        this.authorityService = authorityService;
    }

    /**
     * 必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        User user = (User)principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(user.getRoleList().stream().map(Role::getName).collect(Collectors.toSet()));
        Set<String> permission = new HashSet<>();
        for (Role role: user.getRoleList()) {
            permission.addAll(role.getAuthorityList().stream().map(Authority::getName).collect(Collectors.toSet()));
        }
        simpleAuthorizationInfo.addStringPermissions(permission);
        return simpleAuthorizationInfo;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        // 解密获得username，用于和数据库进行对比
        String username = JWTUtils.getUsername(token);
        if (username == null) {
            throw new AuthenticationException("Invalid auth token!");
        }

        User user = userService.selectByUsername(username);
        if (user == null) {
            throw new AuthenticationException("User does not exist!");
        }

        if (!JWTUtils.verify(token, username, user.getPassword())) {
            throw new AuthenticationException("Invalid auth token!");
        }
        return new SimpleAuthenticationInfo(user, token, "shiro_realm");
    }

}
