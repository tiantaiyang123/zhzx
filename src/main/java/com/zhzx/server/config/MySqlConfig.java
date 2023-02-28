package com.zhzx.server.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
@Slf4j
@MapperScan(basePackages = "com.zhzx.server.repository", sqlSessionTemplateRef = "mySqlSessionTemplate")
public class MySqlConfig {

    @Bean(name = "myDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.mysql")
    public HikariDataSource dataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Value("${mybatis-plus.mysql.mapper-locations}")
    private String mapperLocations;

    @Value("${mybatis-plus.mysql.type-aliases-package}")
    private String typeAliasesPackage;

    @Value("${mybatis-plus.mysql.configuration.use-generated-keys}")
    private boolean useGeneratedKeys;

    @Value("${mybatis-plus.mysql.configuration.map-underscore-to-camel-case}")
    private boolean mapUnderscoreToCamelCase;

    @Value("${mybatis-plus.mysql.configuration.lazy-loading-enabled}")
    private boolean lazyLoadingEnabled;

    @Value("${mybatis-plus.mysql.configuration.aggressive-lazy-loading}")
    private boolean aggressiveLazyLoading;

    @Value("${spring.profiles.active}")
    private String env;

    public MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean(HikariDataSource datasource) throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        bean.setPlugins(interceptor);
        bean.setTypeAliasesPackage(typeAliasesPackage);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
        bean.setDataSource(datasource);

        MybatisConfiguration configuration = new MybatisConfiguration();
        if (!"prod".equals(env))
            configuration.setLogImpl(org.apache.ibatis.logging.stdout.StdOutImpl.class);
        configuration.setUseGeneratedKeys(useGeneratedKeys);
        configuration.setMapUnderscoreToCamelCase(mapUnderscoreToCamelCase);
        configuration.setLazyLoadingEnabled(lazyLoadingEnabled);
        configuration.setAggressiveLazyLoading(aggressiveLazyLoading);
        bean.setConfiguration(configuration);
        return bean;
    }

    @Bean(name = "mySqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory(@Qualifier("myDataSource") HikariDataSource datasource) throws Exception {
        MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean = this.mybatisSqlSessionFactoryBean(datasource);
        return mybatisSqlSessionFactoryBean.getObject();
    }

    @Bean(name = "mySqlSessionTemplate")
    @Primary
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("mySqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
