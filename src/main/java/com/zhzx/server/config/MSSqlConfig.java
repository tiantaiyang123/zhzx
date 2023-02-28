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
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

//@Profile({"prod"})

@Configuration
@Slf4j
@MapperScan(basePackages = "com.zhzx.server.msrepository", sqlSessionTemplateRef = "msSqlSessionTemplate")
public class MSSqlConfig {

    @Bean(name = "msDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.mssql")
    public HikariDataSource dataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Value("${mybatis-plus.mssql.mapper-locations}")
    private String mapperLocations;

    @Value("${mybatis-plus.mssql.type-aliases-package}")
    private String typeAliasesPackage;

    @Value("${mybatis-plus.mssql.configuration.use-generated-keys}")
    private boolean useGeneratedKeys;

    @Value("${mybatis-plus.mssql.configuration.map-underscore-to-camel-case}")
    private boolean mapUnderscoreToCamelCase;

    @Value("${mybatis-plus.mssql.configuration.lazy-loading-enabled}")
    private boolean lazyLoadingEnabled;

    @Value("${mybatis-plus.mssql.configuration.aggressive-lazy-loading}")
    private boolean aggressiveLazyLoading;

    @ConfigurationProperties(prefix = "mybatis-plus.mssql")
    public MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean(HikariDataSource datasource) throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.SQL_SERVER));
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        bean.setPlugins(interceptor);
        bean.setTypeAliasesPackage(typeAliasesPackage);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
        bean.setDataSource(datasource);

        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setUseGeneratedKeys(useGeneratedKeys);
        configuration.setMapUnderscoreToCamelCase(mapUnderscoreToCamelCase);
        configuration.setLazyLoadingEnabled(lazyLoadingEnabled);
        configuration.setAggressiveLazyLoading(aggressiveLazyLoading);
        bean.setConfiguration(configuration);
        return bean;
    }

    @Bean(name = "msSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("msDataSource") HikariDataSource datasource) throws Exception {
        MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean = this.mybatisSqlSessionFactoryBean(datasource);
        return mybatisSqlSessionFactoryBean.getObject();
    }

    @Bean(name = "msSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("msSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
