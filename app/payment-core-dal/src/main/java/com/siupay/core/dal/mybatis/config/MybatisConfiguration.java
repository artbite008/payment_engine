package com.siupay.core.dal.mybatis.config;

import com.siupay.common.lib.date.AlterTimeZoneInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;

@MapperScan("com.siupay.core.dal.mybatis.mapper")
public class MybatisConfiguration {

    @Bean
    public AlterTimeZoneInterceptor alterTimeZoneInterceptor() {
        return new AlterTimeZoneInterceptor();
    }
}