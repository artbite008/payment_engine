package com.siupay.core.dal.redis;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import com.siupay.core.domain.TradeDO;


public class CacheClient {

    @Autowired
    private RedissonClient client;

    public void cache(TradeDO tradeDO) {

    }
}
