package com.siupay.constant;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author: Uther Chen
 * @description: deposit 配置参数
 * @create: 2022/4/29 15:14
 **/
@Data
@Component
public class MigrateDepositConstant {
    /**
     * 服务名称
     */
    @Value("${deposit.datasource.jdbcUrl:jdbc:mysql://mysql.siupay:3306/deposit?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&useSSL=false}")
    private String jdbcUrl;

    @Value("${deposit.datasource.driverClass:com.mysql.jdbc.Driver}")
    private String driverClass;

    @Value("${deposit.datasource.userName:siupay}")
    private String userName;

    @Value("${deposit.datasource.password:test_siupay.123.com}")
    private String password;

    @Value("${deposit.order.sync.max.minutes:30}")
    private Integer syncMaxMinutes;

    @Value("${deposit.order.sync.min.minutes:10}")
    private Integer syncMinMinutes;
}
