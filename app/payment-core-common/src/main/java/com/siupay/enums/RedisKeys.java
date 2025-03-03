package com.siupay.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 
 * @description
 * @date
 */
@Getter
@AllArgsConstructor
public enum RedisKeys {
    /**
     * 检测数字货币余额是否充足的时间拦截
     */
    LOCK_BALANCE_CHECK_WARNING_KEY("payment:core:check:balance:available:lock", 15 * 60L, "检测数字货币余额是否充足的时间拦截"),
    /**
     * 同步channel-core订单信息锁
     */
    LOCK_SYNC_CHANNEL_ORDER_INFO_KEY("payment:core:sync:channel:core:order:lock", 10L, "同步channel-core订单信息锁");;

    private String key;
    /**
     * 过期时间（单位s）
     */
    private Long timeout;

    private String desc;

    public String buildKey(String... params) {
        StringBuilder keyBuilder = new StringBuilder(getKey());
        if (Objects.nonNull(params)) {
            for (String param : params) {
                keyBuilder.append(":").append(param);
            }
        }
        return keyBuilder.toString();
    }
}
