package com.siupay.config;

import com.siupay.common.api.enums.PaymentSystem;
import com.siupay.common.lib.idgenerator.IDGeneratorService;
import com.siupay.common.lib.idgenerator.IDServiceImplV1;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ApplicationConfig {

    @Autowired
    private RedissonClient client;

    @Bean("idService")
    public IDGeneratorService initIdService(RedissonClient client){
        Long instanceId = client.getAtomicLong(
                PaymentSystem.PAYMENT_CORE.getDesc().toLowerCase() + "_instanceid_counter"
        ).incrementAndGet();
        log.info("instanceId = $instanceId");
        return new IDServiceImplV1(instanceId.toString(), PaymentSystem.PAYMENT_CORE);
    }
}
