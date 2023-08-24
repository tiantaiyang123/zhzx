/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.config;

import com.zhzx.server.misc.shiro.JWTFilter;
import com.zhzx.server.misc.shiro.ShiroRealm;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Bean("securityManager")
    public DefaultWebSecurityManager securityManager(ShiroRealm shiroRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm);

        // 关闭自带session
        DefaultSessionStorageEvaluator evaluator = new DefaultSessionStorageEvaluator();
        evaluator.setSessionStorageEnabled(false);

        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        subjectDAO.setSessionStorageEvaluator(evaluator);

        securityManager.setSubjectDAO(subjectDAO);

        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();

        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("jwt", new JWTFilter());

        factoryBean.setFilters(filterMap);
        factoryBean.setSecurityManager(securityManager);

        Map<String, String> filterRuleMap = new LinkedHashMap<>();
        // 静态资源
        filterRuleMap.put("/webjars/**", "anon");
        filterRuleMap.put("/swagger**", "anon");
        filterRuleMap.put("/v2/api-docs", "anon");
        filterRuleMap.put("/static/templates/**", "anon");
        // 导出 excel
        filterRuleMap.put("/**/export-excel", "anon");
        // 登录接口
        filterRuleMap.put("/v1/system/user/login", "anon");
        filterRuleMap.put("/v1/system/user/login-from-wx-url", "anon");
        filterRuleMap.put("/v1/system/user/wx/login", "anon");
        filterRuleMap.put("/v1/system/user/login/staff", "anon");
        filterRuleMap.put("/v1/system/user/register", "anon");
        filterRuleMap.put("/v1/system/user/resetPwd", "anon");
        // 读卡器
        filterRuleMap.put("/v1/card/read-login", "anon");
        // 家长直接判断token，不登录
        filterRuleMap.put("/v1/message/message/parent/**", "anon");
        //验证码
        filterRuleMap.put("/v1/system/user/send/verify-Code", "anon");
        filterRuleMap.put("/v1/captcha/**", "anon");
        //企业微信回调，不需要权限
        filterRuleMap.put("/v1/system/wx/**", "anon");
        // 外部控制器 单独走认证
        filterRuleMap.put("/v1/external/sync/wx/xcx/message", "anon");
        // 其他的接口需要认证
        filterRuleMap.put("/v1/**", "jwt");

        factoryBean.setFilterChainDefinitionMap(filterRuleMap);

        return factoryBean;
    }

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

}
