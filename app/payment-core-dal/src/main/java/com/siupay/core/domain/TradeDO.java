package com.siupay.core.domain;

import java.util.Date;

import lombok.Data;

@Data
public class TradeDO {

    private Long id;

    private String tradeNo;

    private Integer tradeType;

    private Integer tradeSource;

    private Date tradeTime;

    private String desc;

    private String userId;
}
