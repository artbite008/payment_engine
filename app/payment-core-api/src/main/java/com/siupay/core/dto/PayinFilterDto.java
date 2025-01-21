package com.siupay.core.dto;

import java.util.List;

import lombok.Data;

@Data
public class PayinFilterDto {

    private String orderType;

    private String fiatCurrency;

    private String cryptoCurrency;

    private String orderId;

    private List<String> status;

    private String paymentMethod;

}
