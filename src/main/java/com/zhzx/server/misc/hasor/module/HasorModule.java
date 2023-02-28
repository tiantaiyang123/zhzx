/**
 * 项目：中华中学流程自动化管理平台
 * @Author: xiongwei
 * @Date: 2021-08-12 10:10:00
*/

package com.zhzx.server.misc.hasor.module;

import com.zhzx.server.misc.hasor.spi.ApiResponseSpi;
import com.zhzx.server.misc.hasor.spi.JWTAuthPreExecuteChainSpi;
import lombok.extern.slf4j.Slf4j;
import net.hasor.core.ApiBinder;
import net.hasor.core.DimModule;
import net.hasor.dataql.DimUdf;
import net.hasor.dataql.DimUdfSource;
import net.hasor.dataql.QueryApiBinder;
import net.hasor.dataway.spi.PreExecuteChainSpi;
import net.hasor.dataway.spi.SerializationChainSpi;
import net.hasor.db.JdbcModule;
import net.hasor.db.Level;
import net.hasor.spring.SpringModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@DimModule
@Component
@Slf4j
public class HasorModule implements SpringModule {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private ApiResponseSpi apiResponseSpi;
    @Autowired
    private JWTAuthPreExecuteChainSpi jwtAuthPreExecuteChainSpi;

    @Override
    public void loadModule(ApiBinder apiBinder) throws Throwable {
        // .DataSource form Spring boot into Hasor
        apiBinder.installModule(new JdbcModule(Level.Full, this.dataSource));
        apiBinder.bindSpiListener(SerializationChainSpi.class, this.apiResponseSpi);
        apiBinder.bindSpiListener(PreExecuteChainSpi.class, this.jwtAuthPreExecuteChainSpi);
        QueryApiBinder queryBinder = apiBinder.tryCast(QueryApiBinder.class);
        queryBinder.loadUdf(queryBinder.findClass(DimUdf.class));
        queryBinder.loadUdfSource(queryBinder.findClass(DimUdfSource.class));
    }
}
