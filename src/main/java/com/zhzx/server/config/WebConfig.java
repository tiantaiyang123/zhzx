/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.SimpleDateFormat;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${web.upload-path}")
    private String uploadPath;


    // 当前跨域请求最大有效时长。这里默认30天
    private long maxAge = 30 * 24 * 60 * 60;

    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*"); // 1 设置访问源地址
        corsConfiguration.addAllowedHeader("*"); // 2 设置访问源请求头
        corsConfiguration.addAllowedMethod("*"); // 3 设置访问源请求方法
        corsConfiguration.setMaxAge(maxAge);
        corsConfiguration.setAllowCredentials(true);
        return corsConfiguration;
    }

    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig());
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        //这里设置优先级最高
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

    @Bean
    public ObjectMapper objectMapper(JacksonProperties jacksonProperties) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setTimeZone(jacksonProperties.getTimeZone());
        mapper.setDateFormat(new SimpleDateFormat(jacksonProperties.getDateFormat()));
        mapper.setDefaultPropertyInclusion(jacksonProperties.getDefaultPropertyInclusion());
        Boolean failOnEmptyBeans = jacksonProperties.getSerialization().get(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, failOnEmptyBeans == null ? true : failOnEmptyBeans);
        return mapper;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");

//        registry.addResourceHandler("swagger-ui.html")
//                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");

        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/upload/**")//自定义静态资源上传文件路径
                .addResourceLocations("file:///" + uploadPath);
    }
}
