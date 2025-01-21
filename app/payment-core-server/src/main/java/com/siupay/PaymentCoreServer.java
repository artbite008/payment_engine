/*
 * Copyright 2019 Mek Global Limited
 */

package com.siupay;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siupay.common.lib.sensors.annotation.EnableSensors;
import com.siupay.core.dal.mybatis.config.EnableMybatis;
import io.vavr.jackson.datatype.VavrModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.TimeZone;

@Slf4j
@SpringBootApplication
@EnableMybatis
@EnableApolloConfig(value = {"application", "env"})
@EnableFeignClients(basePackages = "com.siupay")
@EnableSensors
@EnableDiscoveryClient
@EnableEurekaClient
@EnableAsync
public class PaymentCoreServer implements ApplicationListener<ApplicationReadyEvent> {

    public static void main(String[] args) {
        log.info("application starting");
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(PaymentCoreServer.class, args);
    }

    @Autowired
    public void configureJackson(Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder) {
        jackson2ObjectMapperBuilder.modulesToInstall(VavrModule.class);
    }

    @Bean
    public ObjectMapper jacksonBuilder() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.registerModule(new VavrModule());
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("application is up");
    }
}
