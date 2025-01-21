package com.siupay.core.dal.mybatis.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SpreadConfig {

    //价差
    private BigDecimal spread;

    //用户报价
    private BigDecimal customerQuotation;

    //原始报价
    private BigDecimal quotation;

    //类型
    private String spreadType;
}
